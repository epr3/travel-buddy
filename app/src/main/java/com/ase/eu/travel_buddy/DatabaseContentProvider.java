package com.ase.eu.travel_buddy;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseContentProvider {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static String TAG="Database Error:";
    private static List<Traseu> traseuList = new ArrayList<>();
    private static boolean dbReinitialization = true;
    //public static Uri downloadUrl;

    public DatabaseContentProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference(mAuth.getCurrentUser().getUid());
    }

    public void uploadFileToDatabase(final String placeDescription, final Uri imageUri, final String fileExtension){
        if(imageUri != null){
            StorageReference fileReference = mStorageRef.child(placeDescription+"."+fileExtension);

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(SetLocationPicture.this,"Upload successful",Toast.LENGTH_SHORT).show();
                    //pictureData.setDownloadUri(taskSnapshot.getDownloadUrl());
                    //pictureData.setLocationName(placeDescription);
                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(placeDescription).child("Picture").setValue(taskSnapshot.getDownloadUrl().toString());
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(SetLocationPicture.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }else{
            //Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    public void writeRouteData(String locationName, List<Punct> pointList, Date startDate, Date endDate)
    {
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(locationName).child("Data").child("Start Date").setValue(dateToStringConverter(startDate));
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(locationName).child("Data").child("End Date").setValue(dateToStringConverter(endDate));

        int index=1;
        for (Punct p:pointList) {
            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(locationName).child("Puncte").child("Punct "+index).setValue(p.getLatitude()+"-"+p.getLongitude());
            index++;
        }
    }

    public void readRouteData()
    {
        final ArrayList<RawRouteData> rawData = new ArrayList<>();
        //List<Traseu> traseuList = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dbReinitialization) {
                    // Get Post object and use the values to update the UI
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //System.out.println("parent "+ds.child(mAuth.getCurrentUser().getUid()).getValue());
                        for (DataSnapshot dss : ds.child(mAuth.getCurrentUser().getUid()).getChildren()) {
                            ArrayList<String> dateList = new ArrayList<>();
                            ArrayList<String> pointsList = new ArrayList<>();
                            RawRouteData rawRouteData = new RawRouteData();
                            rawRouteData.setName(dss.getKey());
                            //System.out.print("child key "+dss.getKey());
                            //System.out.println(" child "+dss.getValue());
                            for (DataSnapshot dsss : dss.child("Data").getChildren()) {
                                //System.out.println(" child data"+dsss.getValue());
                                dateList.add(dsss.getValue().toString());
                            }
                            if(dss.child("Picture").getValue() != null) rawRouteData.setImageUri(dss.child("Picture").getValue().toString());
                            //System.out.println("Picture "+dss.child("Picture").getKey());
                            for (DataSnapshot dsss : dss.child("Puncte").getChildren()) {
                                //System.out.println(" child punct"+dsss.getValue());
                                pointsList.add(dsss.getValue().toString());
                            }
                            rawRouteData.setDates(dateList);
                            rawRouteData.setPoints(pointsList);
                            rawData.add(rawRouteData);
                            System.out.println(rawRouteData.toString());
                        }
                    }
                    traseuList = mapFromRawDataToRouteData(rawData);
                    System.out.println("Traseu: " + traseuList.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
        //return traseuList;
    }

    private static String dateToStringConverter(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = format.format(date);
        return formattedDate;
    }

    private List<Traseu> mapFromRawDataToRouteData(List<RawRouteData> rawRouteDataList)
    {
        List<Traseu> traseuList = new ArrayList<>();
        for (RawRouteData rrd:rawRouteDataList) {
            Traseu t = new Traseu();
            t.setDenumire(rrd.getName());
            if(rrd.getImageUri() != null) t.setPicture(Uri.parse(rrd.getImageUri()));

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                t.setDataFinal(format.parse(rrd.getDates().get(0)));
                t.setDataStart(format.parse(rrd.getDates().get(1)));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Punct> puncte = new ArrayList<>();
            for (String s:rrd.getPoints()) {
                String[] newPoint = s.split("-");
                Double longitudine = Double.valueOf(newPoint[0]);
                Double latitudine = Double.valueOf(newPoint[1]);
                puncte.add(new Punct(longitudine,latitudine));
            }

            t.setListaPuncte(puncte);

            traseuList.add(t);
        }
        return traseuList;
    }

    public static List<Traseu> getTraseuList() {
        return traseuList;
    }

    public static void setTraseuList(List<Traseu> traseuList) {
        DatabaseContentProvider.traseuList = traseuList;
    }

    public static boolean isDbReinitialization() {
        return dbReinitialization;
    }

    public static void setDbReinitialization(boolean dbReinitialization) {
        DatabaseContentProvider.dbReinitialization = dbReinitialization;
    }
}

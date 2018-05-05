package com.ase.eu.travel_buddy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;

import pub.devrel.easypermissions.EasyPermissions;

public class SetLocationPicture extends AppCompatActivity {

    private PictureData pictureData  = PictureData.getInstance();
    private DatabaseContentProvider db;
    private static final int SELECTED_PICTURE=1;
    ImageView pic;

    String placeDescription;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_picture);

        Intent intent = getIntent();
        placeDescription = intent.getStringExtra("pic");
        pic = (ImageView)findViewById(R.id.locationPicture);
        db = new DatabaseContentProvider();

    }

    public void btnClickGetPhoto(View v){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECTED_PICTURE);
    }

    public void btnClickSetPhoto(View v){
        db.uploadFileToDatabase(placeDescription,imageUri,getFileExtension(imageUri));
        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Uploading image...");
        dlg.setCancelable(false);
        dlg.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dlg.dismiss();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }, 2000);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SELECTED_PICTURE:
                if(resultCode == RESULT_OK){
                    String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
                        Uri uri = data.getData();
                        imageUri = data.getData();

                        String[] projection={MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(uri, projection, null, null ,null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(projection[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                        Drawable d = new BitmapDrawable(selectedImage);

                        pic.setBackground(d);
                    } else {
                        EasyPermissions.requestPermissions(this, "Access for storage", 101, galleryPermissions);
                    }
                }
                break;

                default:
                    break;
        }
    }
}

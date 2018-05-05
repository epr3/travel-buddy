package com.ase.eu.travel_buddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends BaseAdapter {
    Context c;
    ArrayList<Traseu> spacecrafts;

    public AlbumAdapter(Context c, ArrayList<Traseu> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int position) {
        return spacecrafts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.album_row,parent,false);
        }

        TextView nameTxt= (TextView) convertView.findViewById(R.id.gallery_title);
        TextView propTxt= (TextView) convertView.findViewById(R.id.gallery_count);
        ImageView image= (ImageView) convertView.findViewById(R.id.galleryImage);

        final Traseu t = (Traseu) this.getItem(position);

        nameTxt.setText(t.getDenumire());
        propTxt.setText(t.getDataStart().toString());
        Picasso.get().load(t.getPicture()).fit().centerCrop().placeholder( R.drawable.placeholder).into(image);

        //ONITECLICK
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GalleryPreviewActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}

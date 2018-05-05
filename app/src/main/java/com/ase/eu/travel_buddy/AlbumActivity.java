package com.ase.eu.travel_buddy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AlbumActivity extends AppCompatActivity {

    GridView galleryGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        galleryGridView = (GridView) findViewById(R.id.galleryGridView);

        AlbumAdapter adapter = new AlbumAdapter(AlbumActivity.this, (ArrayList<Traseu>) DatabaseContentProvider.getTraseuList());
        galleryGridView.setAdapter(adapter);
    }
}

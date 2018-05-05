package com.ase.eu.travel_buddy;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PictureData pictureData = PictureData.getInstance();
    private List<TraseuPuncte> traseuList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TraseuAdapter mAdapter;
    private static int ADD_TRASEU_REQUEST_CODE = 5;
    private FirebaseAuth mAuth;
    private DatabaseContentProvider db;
    private List<Traseu> traseu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        db = new DatabaseContentProvider();

        shareMenuInitializer();

        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Fetching Data...");
        dlg.setCancelable(false);
        dlg.show();

        db.readRouteData();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.traseu_recycler_view);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                traseu = DatabaseContentProvider.getTraseuList();
                mAdapter = new TraseuAdapter(traseu);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                dlg.dismiss();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lista) {
            // Handle the camera acti
        } else if (id == R.id.nav_adaugare) {
            Intent intent = new Intent(MainActivity.this, AddTraseuActivity.class);
            startActivityForResult(intent, ADD_TRASEU_REQUEST_CODE);
        } else if (id == R.id.nav_setari) {

        } else if (id == R.id.nav_despre) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //DatabaseContentProvider.setDbReinitialization(false);
            Traseu traseuResult = (Traseu) data.getSerializableExtra("traseu");

            //Log.i("Numar", traseu.getListaPuncte().size() + "");
            traseu.add(traseuResult);
            //TraseuDatabase.getInstance(getApplicationContext()).getTraseuDAO().insertWithPuncte(traseuPuncte.getTraseu(), traseuPuncte.getListaPuncte());
            //mAdapter.notifyItemInserted(traseu.size());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void shareMenuInitializer()
    {
        final ImageView menuIcon = new ImageView(this);
        menuIcon.setImageResource(R.drawable.finalplus);
        final com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton fab = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this).setContentView(menuIcon).setBackgroundDrawable(R.drawable.fab).build();

        SubActionButton.Builder builder = new SubActionButton.Builder(this);

        ImageView addIcon = new ImageView(this);
        addIcon.setImageResource(R.drawable.add);
        SubActionButton addBtn = builder.setContentView(addIcon).build();

        ImageView picIcon = new ImageView(this);
        picIcon.setImageResource(R.drawable.picture);
        SubActionButton picBtn = builder.setContentView(picIcon).build();

        ImageView exitIcon = new ImageView(this);
        exitIcon.setImageResource(R.drawable.exit);
        SubActionButton exitBtn = builder.setContentView(exitIcon).build();


        final FloatingActionMenu fam = new FloatingActionMenu.Builder(this)
                .addSubActionView(exitBtn)
                .addSubActionView(picBtn)
                .addSubActionView(addBtn)
                .attachTo(fab)
                .build();

        fam.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                menuIcon.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION,90);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(menuIcon,pvhR);
                animator.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                menuIcon.setRotation(90);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION,0);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(menuIcon,pvhR);
                animator.start();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTraseuActivity.class);
                startActivityForResult(intent, ADD_TRASEU_REQUEST_CODE);
                fam.close(true);
            }
        });

        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                startActivity(intent);
                fam.close(true);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null) FirebaseAuth.getInstance().signOut();
                System.exit(0);
                fam.close(true);
            }
        });

    }
}
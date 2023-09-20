package com.sproductions.bgeraser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private final int galreqcode=1000;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=100;
    public final String key1="ok";
    ImageView imggallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar myToolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitle("ok");
        myToolbar.setSubtitle("Version 1.0");
        getSupportActionBar().setTitle("BG Eraser");
       /* imggallery=findViewById(R.id.imageView);
        Intent intess=getIntent();
        Uri imeges=intess.getData();
        imggallery.setImageURI(imeges);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(MainActivity.this).inflate(R.menu.opt_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if (itemId==R.id.opt_new){
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.opt_new2){
            Toast.makeText(this, "ok again", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.opt_new3){
            Toast.makeText(this, "ok ok", Toast.LENGTH_SHORT).show();
        } /*else if (itemId==android.R.id.home){
            super.onBackPressed();
        }*/
        return super.onOptionsItemSelected(item);
    }
    public void createClick(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user
                // You can show a dialog or a Snackbar message explaining why the permission is needed
                Toast.makeText(MainActivity.this, "If you denied the permissions again, you have to reinstall the app", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                // Request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // Permission is already granted, continue with your task
            Intent igallery=new Intent(Intent.ACTION_PICK);
            igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(igallery, galreqcode);
        }

      /*  igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(igallery, galreqcode);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==galreqcode){
                Intent i=new Intent(MainActivity.this,MainActivity2.class);
               // imggallery.setImageURI(data.getData());
                Uri myimage=data.getData();
                i.setData(myimage);
                startActivity(i);
            }
        }
    }
    public void howtoclick(View v){

    }
}
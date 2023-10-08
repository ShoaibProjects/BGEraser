package com.sproductions.bgeraser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int galreqcode=1000;
    private static final int camreqcode=2000;
    private static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=100;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar myToolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("ok");
        myToolbar.setSubtitle("Version 1.0");
        getSupportActionBar().setTitle("BG Eraser");
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
            Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.opt_new2){
            Toast.makeText(this, "feature unavaiblable", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.opt_new3){
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }
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

    }
    public void camclick(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show an explanation to the user
                // You can show a dialog or a Snackbar message explaining why the permissions are needed
                Toast.makeText(MainActivity.this, "If you denied the permissions again, you have to reinstall the app", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                // Request the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            // Permissions are already granted, continue with your task
            Intent icam=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (icam.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(icam, camreqcode);
        }

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==galreqcode && data!=null){
                Intent i=new Intent(MainActivity.this,MainActivity2.class);
                Uri myimage=data.getData();
                i.setData(myimage);
                startActivity(i);
            }
            else if (requestCode==camreqcode && data!=null){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri imguri=bitmptouri(imageBitmap,MainActivity.this);
                Intent cami=new Intent(this,MainActivity2.class);
                cami.setData(imguri);
                startActivity(cami);
            }
        }
    }
    public Uri bitmptouri(Bitmap bitmap, Context context){

        // Create a directory to save the image (you can change the directory as needed)
        File directory = new File(context.getCacheDir(), "images");
        Uri uri=null;
        try {
            directory.mkdirs(); // Create the directory if it doesn't exist
            File imageFile = new File(directory, "image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", imageFile);
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return uri;
    }
}
package com.sproductions.bgeraser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    variables ver=new variables();
    @Override
    protected void onResume() {
        super.onResume();
        deleteOldImages();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar myToolbar2=(Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar2.setTitle("ok");
        getSupportActionBar().setTitle("Crop");
        ImageView im=findViewById(R.id.imageView2);
        Intent in=getIntent();
        Uri imge=in.getData();
        CropImageView cropImageView=findViewById(R.id.cropImageView);
        cropImageView.setImageUriAsync(imge);
       Button myButton = findViewById(R.id.button4);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Bitmap cropped=cropImageView.getCroppedImage();
             Bitmap crop=cropped;
             im.setImageBitmap(cropped);
                /**/
               ver.setBtmp(crop);
            }
        });
        Button myBtn = findViewById(R.id.buttondone);
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap b=ver.btmp;
                if(b==null){
                    Toast.makeText(MainActivity2.this, "Please first crop the image", Toast.LENGTH_SHORT).show();
                }
                else {
                    Uri croppedImageUri=btmpgiver(b);
                    Intent intes=new Intent(MainActivity2.this,MainActivity3.class);
                    intes.setData(croppedImageUri);
                    startActivity(intes);
                }
            }
        });

    }
    public Uri btmpgiver (Bitmap mybtmp){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mybtmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String filename = "ByBGEraser_" + System.currentTimeMillis() + ".jpeg";
        String uristring=MediaStore.Images.Media.insertImage(getContentResolver(), mybtmp, filename, null);
        Uri croppedImageUri;
        croppedImageUri= Uri.parse(uristring);
        return croppedImageUri;
    }
    private void deleteOldImages() {
        // Define the prefix for your app's images
        String prefix = "ByBGEraser_";

        // Calculate the expiration date (in milliseconds)
        long expirationTime = System.currentTimeMillis() - (1* 24 * 60 * 60 * 1000); // 7 days

        // Define the selection criteria
        String selection = MediaStore.Images.Media.DISPLAY_NAME + " LIKE ? AND " + MediaStore.Images.Media.DATE_ADDED + " < ?";

        // Define the selection arguments
        String[] selectionArgs = new String[]{prefix + "%", String.valueOf(expirationTime / 1000)};

        // Query the MediaStore for images matching the selection criteria
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get the ID of the image to delete
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));

                // Make sure the ID is valid
                if (id >= 0) {
                    // Construct the content URI for the image
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    // Delete the image
                    getContentResolver().delete(contentUri, null, null);
                }
            } while (cursor.moveToNext());
        }

        // Close the cursor
        if (cursor != null) {
            cursor.close();
        }
    }


    public void btnclk(View v){
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(MainActivity2.this).inflate(R.menu.opt_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if (itemId==R.id.opt_help){
            Toast.makeText(this, "helping", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.opt_done){
            Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
        } else if (itemId==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
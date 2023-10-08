package com.sproductions.bgeraser;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    variables ver=new variables();
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
        Intent in = getIntent();
        Uri imge = in.getData();
        CropImageView cropImageView=findViewById(R.id.cropImageView);
        cropImageView.setImageUriAsync(imge);
       Button myButton = findViewById(R.id.button4);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Bitmap cropped=cropImageView.getCroppedImage();
             Bitmap crop=cropped;
             im.setImageBitmap(cropped);
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
                    Uri croppedImageUri=btmpgiver(b,MainActivity2.this);
                    Intent intes=new Intent(MainActivity2.this,MainActivity3.class);
                    intes.setData(croppedImageUri);
                    startActivity(intes);
                }
            }
        });

    }
    public Uri btmpgiver (Bitmap bitmap, Context context){

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
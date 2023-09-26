 package com.sproductions.bgeraser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

 public class MainActivity3 extends AppCompatActivity {
    private static final String TAG = "MyActivity";
     private final ExecutorService conversionExecutor = Executors.newSingleThreadExecutor();
    bgkot BGErase=new bgkot();
    variables var2=new variables();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar myToolbar2=(Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(myToolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imggallery=findViewById(R.id.editView);
        Intent intesns=getIntent();
        Uri imeges=intesns.getData();
        imggallery.setImageURI(imeges);

        Button saveBtn=(Button)findViewById(R.id.toolb3donebutton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent saveintent=new Intent(MainActivity3.this,MainActivity4.class);
                if (var2.bgruri==null){
                saveintent.setData(imeges);}
                else {
                    saveintent.setData(var2.bgruri);
                }
                startActivity(saveintent);
            }
        });
        ToggleButton toggleButton1 = findViewById(R.id.manualbutton);
        ToggleButton toggleButton2 = findViewById(R.id.autobutton);
        RadioGroup toggleGroup = findViewById(R.id.toggle_group);

// Set the default selected button
        toggleButton1.setChecked(true);

// Set an OnCheckedChangeListener on each ToggleButton
        toggleButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (toggleButton1.isChecked()) {
                    return true;
                }
                return false;
            }
        });

        toggleButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (toggleButton2.isChecked()) {
                    return true;
                }
                return false;
            }
        });



        // Set OnCheckedChangeListener for each button
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Intermidiate botton", Toast.LENGTH_SHORT).show();
                    // Perform action when button is selected
                    toggleButton2.setChecked(false);
                    toggleButton1.setTextColor(Color.WHITE);
                    Drawable drawable = toggleButton1.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable.mutate().setColorFilter(colorFilter);

// Set the updated Drawable back to the Button
                    toggleButton1.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                } else {
                    // Perform action when button is deselected
                    toggleButton1.setTextColor(Color.BLACK);
                    Drawable drawable = toggleButton1.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable.mutate().setColorFilter(colorFilter);

// Set the updated Drawable back to the Button
                    toggleButton1.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                }
            }
        });

        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Processing image, please wait", Toast.LENGTH_SHORT).show();
                    // Perform action when button is selected
                    toggleButton1.setChecked(false);
                    toggleButton2.setTextColor(Color.WHITE);
                    Drawable drawable2 = toggleButton2.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter2 = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable2.mutate().setColorFilter(colorFilter2);

// Set the updated Drawable back to the Button
                    toggleButton2.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null);
                    Bitmap bitmapa3;
                          try {
                              bitmapa3 = MediaStore.Images.Media.getBitmap(getContentResolver(), imeges);
                              Log.d(TAG, "Input bitmap width: " + bitmapa3.getWidth());
                              Log.d(TAG, "Input bitmap height: " + bitmapa3.getHeight());
                              } catch (IOException e) {
                              e.printStackTrace();
                              return;
                       }

                          bgprocessed(bitmapa3,imggallery);

                } else {
                    // Perform action when button is deselected
                    toggleButton2.setTextColor(Color.BLACK);
                    Drawable drawable2 = toggleButton2.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter2 = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable2.mutate().setColorFilter(colorFilter2);

// Set the updated Drawable back to the Button
                    toggleButton2.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if (itemId==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void bgprocessed(Bitmap bitmapa3,ImageView imggallery){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Make API request to remove background and get a processed Bitmap
                BGErase.bgfun(bitmapa3);
                Bitmap reslt=BGErase.bgbitmap;

                // Use ExecutorService to convert Bitmap to Uri in a separate thread
                Future<Uri> uriConversionFuture = conversionExecutor.submit(new Callable<Uri>() {
                    @Override
                    public Uri call() throws Exception {
                        return bitmptouri(reslt);
                    }
                });

                try {
                    // Get the Uri result from the conversion task (this may block until it's done)
                    Uri imageUrireuri = uriConversionFuture.get();

                    // Update UI with the processed bitmap and imageUri
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Display the processed bitmap
                            imggallery.setImageBitmap(reslt);

                            // Use the imageUri as needed
                            var2.setBgruri(imageUrireuri);
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    // Handle exceptions here
                }
            }
        }).start();
    }
    public Uri bitmptouri(Bitmap reslt){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                reslt.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String filename = "ByBGEraser_" + System.currentTimeMillis() + ".jpeg";
                String uristring=MediaStore.Images.Media.insertImage(getContentResolver(), reslt, filename, null);
                Uri resuri;
                resuri= Uri.parse(uristring);
                return resuri;
    }
}
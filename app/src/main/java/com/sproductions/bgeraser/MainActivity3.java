 package com.sproductions.bgeraser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
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
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.sproductions.bgeraser.ml.Tfmodel;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

 public class MainActivity3 extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private CursorView cursorView;
    private SeekBar cursorSizeSeekBar;
    private SeekBar handleOffsetSeekBar;
     private final ExecutorService conversionExecutor = Executors.newSingleThreadExecutor();
//    private MyBackgroundEraser backgroundEraser;
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

        /*try {
            backgroundEraser = new MyBackgroundEraser(this);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // Get CursorView reference
      /*  cursorView = findViewById(R.id.cursor_view);
        cursorSizeSeekBar = findViewById(R.id.cursorSizeSeekBar);
        handleOffsetSeekBar = findViewById(R.id.cursorOffsetSeekBar);

        cursorView.setCursorSizeSeekBar(cursorSizeSeekBar);
        cursorView.setCursorOffsetSeekBar(handleOffsetSeekBar);*/
        Button helpBtn=(Button)findViewById(R.id.toolb3settingbutton);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        ToggleButton toggleButton3 = findViewById(R.id.magicbutton);
        ToggleButton toggleButton4 = findViewById(R.id.repairbutton);
        ToggleButton toggleButton5 = findViewById(R.id.zoombutton);
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

        toggleButton3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (toggleButton3.isChecked()) {
                    return true;
                }
                return false;
            }
        });
        toggleButton4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (toggleButton4.isChecked()) {
                    return true;
                }
                return false;
            }
        });
        toggleButton5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (toggleButton5.isChecked()) {
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
                    Toast.makeText(getApplicationContext(), "Button 1 selected", Toast.LENGTH_SHORT).show();
                    // Perform action when button is selected
                    toggleButton2.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton4.setChecked(false);
                    toggleButton5.setChecked(false);
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
                    Toast.makeText(getApplicationContext(), "Button 2 selected", Toast.LENGTH_SHORT).show();
                    // Perform action when button is selected
                    toggleButton1.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton4.setChecked(false);
                    toggleButton5.setChecked(false);
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


                          /*BGErase.bgfun(bitmapa3);
                          Bitmap reslt=BGErase.bgbitmap;
                          imggallery.setImageBitmap(reslt);*/
                    /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    reslt.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String filename = "ByBGEraser_" + System.currentTimeMillis() + ".jpeg";
                    String uristring=MediaStore.Images.Media.insertImage(getContentResolver(), reslt, filename, null);
                    Uri resuri;
                    resuri= Uri.parse(uristring);
                    var2.setBgruri(resuri);*/
                    /*Bitmap outputBitmap = backgroundEraser.eraseBackground(bitmapa3);
                          imggallery.setImageBitmap(outputBitmap);*/
                    /*try {
                        Tfmodel model = Tfmodel.newInstance(MainActivity3.this);

                        // Creates inputs for reference.
                        TensorImage image = TensorImage.fromBitmap(bitmapa3);

                        // Runs model inference and gets result.
                        Tfmodel.Outputs outputs = model.process(image);
                        List<Category> segmentationMasks = outputs.getSegmentationMasksAsCategoryList();

                        // Releases model resources if no longer used.
                        model.close();
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }*/



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

        toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Button 3 selected", Toast.LENGTH_SHORT).show();
                    // Perform action when button is selected
                    toggleButton1.setChecked(false);
                    toggleButton2.setChecked(false);
                    toggleButton4.setChecked(false);
                    toggleButton5.setChecked(false);
                    toggleButton3.setTextColor(Color.WHITE);
                    Drawable drawable3 = toggleButton3.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter3 = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable3.mutate().setColorFilter(colorFilter3);

// Set the updated Drawable back to the Button
                    toggleButton3.setCompoundDrawablesWithIntrinsicBounds(null, drawable3, null, null);
                } else {
                    // Perform action when button is deselected
                    toggleButton3.setTextColor(Color.BLACK);
                    Drawable drawable3 = toggleButton3.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter3 = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable3.mutate().setColorFilter(colorFilter3);

// Set the updated Drawable back to the Button
                    toggleButton3.setCompoundDrawablesWithIntrinsicBounds(null, drawable3, null, null);
                }
            }
        });
        toggleButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Button 4 selected", Toast.LENGTH_SHORT).show();
                    // Perform action when button is selected
                    toggleButton2.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton1.setChecked(false);
                    toggleButton5.setChecked(false);
                    toggleButton4.setTextColor(Color.WHITE);
                    Drawable drawable4 = toggleButton4.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter4 = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable4.mutate().setColorFilter(colorFilter4);

// Set the updated Drawable back to the Button
                    toggleButton4.setCompoundDrawablesWithIntrinsicBounds(null, drawable4, null, null);
                } else {
                    // Perform action when button is deselected
                    toggleButton4.setTextColor(Color.BLACK);
                    Drawable drawable4 = toggleButton4.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter4 = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable4.mutate().setColorFilter(colorFilter4);

// Set the updated Drawable back to the Button
                    toggleButton4.setCompoundDrawablesWithIntrinsicBounds(null, drawable4, null, null);
                }
            }
        });
        toggleButton5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Button 5 selected", Toast.LENGTH_SHORT).show();
                    // Perform action when button is selected
                    toggleButton2.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton4.setChecked(false);
                    toggleButton1.setChecked(false);
                    toggleButton5.setTextColor(Color.WHITE);
                    Drawable drawable5 = toggleButton5.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter5 = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable5.mutate().setColorFilter(colorFilter5);

// Set the updated Drawable back to the Button
                    toggleButton5.setCompoundDrawablesWithIntrinsicBounds(null, drawable5, null, null);
                } else {
                    // Perform action when button is deselected
                    toggleButton5.setTextColor(Color.BLACK);
                    Drawable drawable5 = toggleButton5.getCompoundDrawables()[1]; // Index 1 represents the DrawableTop

// Create a ColorFilter object with the desired color
                    ColorFilter colorFilter5 = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

// Apply the ColorFilter to the Drawable
                    drawable5.mutate().setColorFilter(colorFilter5);

// Set the updated Drawable back to the Button
                    toggleButton5.setCompoundDrawablesWithIntrinsicBounds(null, drawable5, null, null);
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
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                // Make API request to remove background
                BGErase.bgfun(bitmapa3);
                Bitmap reslt=BGErase.bgbitmap;

                // Update UI with processed bitmap on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Display the processed bitmap
                        imggallery.setImageBitmap(reslt);

                        // Convert bitmap to URI if needed
                        //bitmptouri(reslt);
var2.setBgruri(resuri);
                    }
                });
            }
        }).start();*/
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
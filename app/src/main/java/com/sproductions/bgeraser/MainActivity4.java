package com.sproductions.bgeraser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class
MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Toolbar myToolbar2=(Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(myToolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imggallery=findViewById(R.id.testiv);
        Bitmap bitmap = BitmapHolder.getBitmap();
        imggallery.setImageBitmap(bitmap);
        Button saveBton=(Button)findViewById(R.id.saverBton);
        EditText filenameet=findViewById(R.id.filenamevar);
        saveBton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                String folderName = "BG Erase Outputs";

                String fileName=null;
                fileName=filenameet.getText().toString()+".png";
                if(fileName==(null+".png")){
                    Toast.makeText(MainActivity4.this,"Please give a name to your file",Toast.LENGTH_SHORT).show();
                }
                else {
                    File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), folderName);
                    if (!folder.exists()) {
                        boolean created = folder.mkdir();
                        if (!created) {
                            Log.e("TAG", "Failed to create directory");
                            return;
                        }
                    }

                    File file = new File(folder, fileName);
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, fos);
                        fos.flush();
                        fos.close();
                        String filepath = file.getAbsolutePath();
                        Toast.makeText(MainActivity4.this, "File get saved at " + filepath, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
}
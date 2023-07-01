package com.example.zipfileapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import java.io.File;

public class MainActivity extends AppCompatActivity {


    private static final String DOWNLOAD_URL = "https://www.dwsamplefiles.com/?dl_id=559";
    private static final String DESTINATION_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "extracted_data";

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;


    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(hasPermission()){
                    downloadZipAndUnZip();
                 }else{
                     getPermission();
                 }
            }
        });




    }

    private void downloadZipAndUnZip() {
        ZipDownloader zipDownloader = new ZipDownloader(this, DOWNLOAD_URL, DESTINATION_FOLDER);
        zipDownloader.downloadAndExtractZip();
    }

    private boolean hasPermission() {
//        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE }, STORAGE_PERMISSION_REQUEST_CODE);
    }


}
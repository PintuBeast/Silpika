package com.example.silpika_v2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.graphics.Bitmap;

import android.graphics.Paint;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;


public class demo extends AppCompatActivity {
    private Uri videoUri,videoUri_1,imageUri;
    private Bitmap bitmap;
    private static final int REQUEST_CODE=100;
    // private StorageReference videoRef;
    private Paint paint;

    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;

    private static final int PICK_VIDEO=1;




    int MODEL_WIDTH = 257;
    int MODEL_HEIGHT = 257;


    private ImageView imageView1;


    DrawingTheBall v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }




    @RequiresApi(api = Build.VERSION_CODES.P)
    public void record (View view) {

        String videoPath="android.resource://"+getPackageName()+"/"+R.raw.v1;
        videoUri=Uri.parse(videoPath);

        String videoPath1="android.resource://"+getPackageName()+"/"+R.raw.v2;
        videoUri_1=Uri.parse(videoPath1);

        v=new DrawingTheBall(demo.this, videoUri,videoUri_1);
        setContentView(v);

    }


    public void forward (View view) {

        Intent i =new Intent(demo.this,signIn.class);
        startActivity(i);


    }



}



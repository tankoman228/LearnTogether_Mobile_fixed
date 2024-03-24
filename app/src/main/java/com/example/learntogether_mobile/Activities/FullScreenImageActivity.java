package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.learntogether_mobile.R;
import com.github.chrisbanes.photoview.PhotoView;

public class FullScreenImageActivity extends AppCompatActivity {

    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        PhotoView pv = findViewById(R.id.pv);

        if (bitmap != null) {
            pv.setImageBitmap(bitmap);
        }
    }
}
package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.learntogether_mobile.R;

public class ActHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        findViewById(R.id.btnBack).setOnClickListener(l -> finish());
    }
}
package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.R;

public class EditUser extends AppCompatActivity {

    public static ListU User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
    }
}
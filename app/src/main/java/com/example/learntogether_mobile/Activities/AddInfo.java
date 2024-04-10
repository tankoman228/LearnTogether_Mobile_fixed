package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.learntogether_mobile.R;

public class AddInfo extends AppCompatActivity {

    EditText etTitle, etText, etTags;
    RecyclerView recyclerView;
    Button btnAdd, btnDelete, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        etTags = findViewById(R.id.etTaglist);
        etText = findViewById(R.id.etDescription);
        etTitle = findViewById(R.id.etHeader);
        recyclerView = findViewById(R.id.recyclerView2);
        btnAdd = findViewById(R.id.btnAddImage);
        btnDelete = findViewById(R.id.btnDeleteImage);
        btnSave = findViewById(R.id.btnSave);

        btnAdd.setOnClickListener(l -> {

        });
        btnDelete.setOnClickListener(l -> {

        });
        btnSave.setOnClickListener(l -> {

        });
    }
}
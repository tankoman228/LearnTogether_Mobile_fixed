package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learntogether_mobile.API.NotificationService;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinGroup extends AppCompatActivity {

    EditText etToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        etToken = findViewById(R.id.etPassword);
        findViewById(R.id.ibBack).setOnClickListener(l -> finish());
        findViewById(R.id.btnEnter).setOnClickListener(l -> {

            RequestU requestU = new RequestU();
            requestU.setSession_token(Variables.SessionToken);
            requestU.token = etToken.getText().toString();

            new RetrofitRequest().apiService.join_group(requestU).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    if (response.body().Error != null) {
                        Toast.makeText(JoinGroup.this, response.body().Error, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Toast.makeText(JoinGroup.this, "The new group added to list", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {

                }
            });
        });
    }
}
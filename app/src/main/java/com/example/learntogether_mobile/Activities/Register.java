package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Register extends AppCompatActivity {

    EditText[] editText = new EditText[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText[0] = findViewById(R.id.etUsername);
        editText[1] = findViewById(R.id.etTitle);
        editText[2] = findViewById(R.id.etContact);
        editText[3] = findViewById(R.id.etPassword);
        editText[4] = findViewById(R.id.etPasswordConfirm);
        editText[5] = findViewById(R.id.etToken);

        findViewById(R.id.btnRegisterProfile).setOnClickListener(l -> {

            if (!editText[3].getText().toString().equals(editText[4].getText().toString())) {
                Toast.makeText(Register.this, "Password not confirmed", Toast.LENGTH_SHORT).show();
            }

            RequestU request = new RequestU() {{
                token = editText[5].getText().toString();
                username = editText[0].getText().toString();
                password = editText[3].getText().toString();
                contact = editText[2].getText().toString();
                title = editText[1].getText().toString();
            }};

            RetrofitRequest r = new RetrofitRequest();
            Call<ResponseU> call = r.apiService.register(request);
            call.enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    Register.this.runOnUiThread(() -> {

                        if (response.body().Error != null) {
                            Toast.makeText(Register.this, "Error: " + response.body().Error, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (response.body().Token != null) {
                            Variables.SessionToken = response.body().Token;
                            Variables.username = request.username;
                            Variables.password = request.password;
                            Variables.saveValues(Register.this);
                            Variables.requireMyAccountInfo();

                            startActivity(new Intent(Register.this, News.class));
                            startForegroundService(new Intent(Register.this, NotificationService.class));
                        }

                        Toast.makeText(Register.this, response.body().Result, Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                    Log.d("API", "ERROR: " + t.getMessage());
                }
            });
        });
    }
}
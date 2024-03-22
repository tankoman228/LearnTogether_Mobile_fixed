package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.NotificationService;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText etLogin, etPassword, etServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etServer = findViewById(R.id.etServerAddress);
        etServer.setText(Variables.server);

        findViewById(R.id.btnEnter).setOnClickListener(l -> {
            RequestU request = new RequestU() {{
                username = etLogin.getText().toString();
                password = etPassword.getText().toString();
            }};

            Variables.server = etServer.getText().toString();
            RetrofitRequest r = new RetrofitRequest();
            Call<ResponseU> call = r.apiService.login(request);
            call.enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    Login.this.runOnUiThread(() -> {
                        if (response.body().Token != null) {
                            Variables.SessionToken = response.body().Token;
                            Variables.username = request.username;
                            Variables.password = request.password;
                            Variables.saveValues(Login.this);

                            startActivity(new Intent(Login.this, News.class));
                            startForegroundService(new Intent(Login.this, NotificationService.class));
                        }
                        Toast.makeText(Login.this, response.body().Result, Toast.LENGTH_SHORT).show();
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
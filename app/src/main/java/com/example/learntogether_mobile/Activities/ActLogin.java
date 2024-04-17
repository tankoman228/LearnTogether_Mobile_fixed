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
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Активность для авторизации
 */
public class ActLogin extends AppCompatActivity {

    EditText etLogin, etPassword, etServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etServer = findViewById(R.id.etServerAddress);
        etServer.setText(GlobalVariables.server);

        findViewById(R.id.ibBack).setOnClickListener(l -> finish());
        findViewById(R.id.btnEnter).setOnClickListener(l -> {
            RequestU request = new RequestU() {{
                username = etLogin.getText().toString();
                password = etPassword.getText().toString();
            }};

            GlobalVariables.server = etServer.getText().toString();
            RetrofitRequest r = new RetrofitRequest();
            Call<ResponseU> call = r.apiService.login(request);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    ActLogin.this.runOnUiThread(() -> {
                        if (response.body().Token != null) {
                            GlobalVariables.SessionToken = response.body().Token;
                            GlobalVariables.username = request.username;
                            GlobalVariables.password = request.password;
                            GlobalVariables.saveValuesToSharedPrefs(ActLogin.this);
                            GlobalVariables.requireMyAccountInfo(ActLogin.this);

                            startActivity(new Intent(ActLogin.this, ActNews.class));
                            startForegroundService(new Intent(ActLogin.this, NotificationService.class));
                        }
                        Toast.makeText(ActLogin.this, response.body().Result, Toast.LENGTH_SHORT).show();
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
package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.Manifest;
import android.view.View;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.NotificationService;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 10);
            }
        }

        Variables.loadValues(this);
        if (Variables.password != null) {

            try {
                Intent intent = new Intent(this, NotificationService.class);
                startForegroundService(intent);
            }
            catch (Exception e) {
               e.printStackTrace();
            }


            findViewById(R.id.btnRegister).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnLogin).setVisibility(View.INVISIBLE);

            RequestU request = new RequestU() {{
                username = Variables.username;
                password = Variables.password;
            }};

            RetrofitRequest r = new RetrofitRequest();
            Call<ResponseU> call = r.apiService.login(request);
            call.enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    MainActivity.this.runOnUiThread(() -> {
                        if (response.body() != null && response.body().Token != null) {
                            Variables.SessionToken = response.body().Token;
                            Variables.username = request.username;
                            Variables.password = request.password;
                            Variables.saveValues(MainActivity.this);
                            Variables.requireMyAccountInfo(MainActivity.this);

                            startActivity(new Intent(MainActivity.this, News.class));
                            //startForegroundService(new Intent(MainActivity.this, NotificationService.class));
                        }
                        if (response.body() != null)
                            Toast.makeText(MainActivity.this, response.body().Result, Toast.LENGTH_SHORT).show();

                        findViewById(R.id.btnRegister).setVisibility(View.VISIBLE);
                        findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
                    });
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                    Log.d("API", "ERROR: " + t.getMessage());
                    findViewById(R.id.btnRegister).setVisibility(View.VISIBLE);
                    findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
                }
            });

        }


        findViewById(R.id.btnRegister).setOnClickListener(l -> {
            startActivity(new Intent(this, Register.class));
        });

        findViewById(R.id.btnLogin).setOnClickListener(l -> {
            startActivity(new Intent(this, Login.class));
        });
    }
}
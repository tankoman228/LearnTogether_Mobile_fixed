package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.view.View;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ImageConverter;
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
 * Переход на нужный экран, если удалась автоматическая авторизация, запуск службы для уведомлений
 */
public class ActMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageConverter.DefaultIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 10);
            }
        }
        findViewById(R.id.imageButton).setOnClickListener(l -> startActivity(new Intent(this, ActHelp.class)));

        GlobalVariables.loadValuesFromSharedPrefs(this);
        if (GlobalVariables.password != null) {

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
                username = GlobalVariables.username;
                password = GlobalVariables.password;
            }};

            RetrofitRequest r = new RetrofitRequest();
            Call<ResponseU> call = r.apiService.login(request);
            call.enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    ActMain.this.runOnUiThread(() -> {
                        if (response.body() != null && response.body().Token != null) {
                            GlobalVariables.SessionToken = response.body().Token;
                            GlobalVariables.username = request.username;
                            GlobalVariables.password = request.password;
                            GlobalVariables.saveValuesToSharedPrefs(ActMain.this);
                            GlobalVariables.requireMyAccountInfo(ActMain.this);

                            startActivity(new Intent(ActMain.this, ActNews.class));
                            //startForegroundService(new Intent(MainActivity.this, NotificationService.class));
                        }
                        if (response.body() != null)
                            Toast.makeText(ActMain.this, response.body().Result, Toast.LENGTH_SHORT).show();

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
            startActivity(new Intent(this, ActRegister.class));
        });

        findViewById(R.id.btnLogin).setOnClickListener(l -> {
            startActivity(new Intent(this, ActLogin.class));
        });
    }
}
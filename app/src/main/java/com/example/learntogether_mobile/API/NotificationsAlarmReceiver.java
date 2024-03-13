package com.example.learntogether_mobile.API;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.learntogether_mobile.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        sendRetrofit(context);
    }

    private static final String CHANNEL_ID = "LT";
    private static final int notificationId = 4238;


    private void sendRetrofit(Context context) {
        Log.d("API", "Checking notifications");
        RequestU request = new RequestU() {{
            session_token = Variables.SessionToken;
        }};
        RetrofitRequest r = new RetrofitRequest();
        Call<ResponseU> call = r.apiService.check_notifications(request);
        call.enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {


                if (response.body().Error != null) {
                    Log.d("API", "Not critical error: " + response.body().Error);
                    if (response.body().Error.equals("Unknown session")) {
                        Log.d("API", "Autologin");

                        RequestU request = new RequestU() {{
                            username = "tank228";
                            password = "123";
                        }};

                        RetrofitRequest r = new RetrofitRequest();
                        Call<ResponseU> call2 = r.apiService.login(request);

                        call2.enqueue(new Callback<ResponseU>() {
                            @Override
                            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                                if (response.body().Token != null) {
                                    Variables.SessionToken = response.body().Token;

                                    RequestU request = new RequestU() {{
                                        session_token = Variables.SessionToken;
                                    }};
                                    RetrofitRequest r = new RetrofitRequest();
                                    Call<ResponseU> call3 = r.apiService.check_notifications(request);
                                    call3.enqueue(new Callback<ResponseU>() {
                                        @Override
                                        public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                                            showNotifications(response.body().stringList, context);
                                        }
                                        @Override
                                        public void onFailure(Call<ResponseU> call, Throwable t) {
                                            Log.d("API", "ERROR: " + t.getMessage());
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseU> call, Throwable t) {
                                Log.d("API", "ERROR: " + t.getMessage());
                            }
                        });
                    }
                }
                else
                    showNotifications(response.body().stringList, context);
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "ERROR: " + t.getMessage());
            }
        });
    }

    private void showNotifications(List<String> list, Context context) {

        if (list == null)
            return;

        for (String message: list) {
            Log.d("API", message);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "LearnTogether", importance);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Learn Together")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            notificationManager.notify(notificationId, builder.build());
        }
    }
}
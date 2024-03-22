package com.example.learntogether_mobile.API;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.learntogether_mobile.Activities.MainActivity;
import com.example.learntogether_mobile.R;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service {

    private static Handler mHandler;
    private static Runnable mRunnable;
    private static final int FOREGROUND_NOTIFICATION_ID = 25;
    private static final String FOREGROUND_CHANNEL_ID = "Notification Channel Service";

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mRunnable = () -> new NetworkTask().execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationChannel channel = new NotificationChannel(FOREGROUND_CHANNEL_ID, "My Service Channel",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Notification Service")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("If you see this text, notification channel is open and if something new appears you can see it"))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        startForeground(FOREGROUND_NOTIFICATION_ID, notification);

        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable); // Остановка выполнения процесса при уничтожении службы
        stopForeground(true);
    }


    private static final String CHANNEL_ID = "LT";

    private class NetworkTask extends AsyncTask<Void, Void, Void> {
        private Handler handler = new Handler();

        public final int interval = 15000; //ms

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sendRetrofit(NotificationService.this);
                handler.postDelayed(this, interval);
            }
        };

        @Override
        protected Void doInBackground(Void... params) {
            handler.post(runnable);
            return null;
        }
    }

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

            notificationManager.notify( (new Random().nextInt()) / 100, builder.build());
        }
    }
}
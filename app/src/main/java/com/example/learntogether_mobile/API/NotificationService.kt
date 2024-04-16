package com.example.learntogether_mobile.API

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.learntogether_mobile.Activities.ActMain
import com.example.learntogether_mobile.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

class NotificationService : Service() {

    companion object {
        private var mHandler: Handler? = null
        private var mRunnable: Runnable? = null
        private const val FOREGROUND_NOTIFICATION_ID = 25
        private const val FOREGROUND_CHANNEL_ID = "Notification Channel Service"
        private const val CHANNEL_ID = "LT"
    }

    override fun onCreate() {
        super.onCreate()
        mHandler = Handler()
        mRunnable = Runnable { NetworkTask().execute() }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val channel = NotificationChannel(
            FOREGROUND_CHANNEL_ID, "My Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Notification Service")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("If you see this text, notification channel is open and if something new appears you can see it")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationIntent = Intent(this, ActMain::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent)
        val notification = builder.build()
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)

        mHandler!!.removeCallbacks(mRunnable!!)
        mHandler!!.post(mRunnable!!)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler!!.removeCallbacks(mRunnable!!) // Остановка выполнения процесса при уничтожении службы
        stopForeground(true)
    }

    private inner class NetworkTask : AsyncTask<Void?, Void?, Void?>() {
        private val handler = Handler()
        val interval = 75000 //ms
        private val runnable: Runnable = object : Runnable {
            override fun run() {
                if (Variables.password != null) {
                    sendRetrofit(this@NotificationService)
                    handler.postDelayed(this, interval.toLong())
                }
                else {
                    Log.d("API", "Auto authentification cancelled, notifications won't work")
                    stopSelf()
                }
            }
        }
        override fun doInBackground(vararg params: Void?): Void? {
            if (Variables.password != null)
                handler.post(runnable)
            return null
        }
    }

    private fun sendRetrofit(context: Context) {

        Log.d("API", "Checking notifications")

        val request0 = RequestU();
        request0.session_token = Variables.SessionToken

        val r0 = RetrofitRequest()
        val call = r0.apiService.check_notifications(request0)
        //First try
        call.enqueue(object : Callback<ResponseU> {
            override fun onResponse(call: Call<ResponseU>, response: Response<ResponseU>) {
                if (response.body()!!.Error != null) {
                    Log.d("API", "Not critical error: " + response.body()!!.Error)
                    if (response.body()!!.Error == "Unknown session") {
                        Log.d("API", "Autologin")
                        val request1 = RequestU();
                        request1.username = Variables.username
                        request1.password = Variables.password

                        val r1 = RetrofitRequest()
                        val call2 = r1.apiService.login(request1)
                        call2.enqueue(object : Callback<ResponseU> {
                            override fun onResponse(
                                call: Call<ResponseU>,
                                response: Response<ResponseU>
                            ) {
                                if (response.body()!!.Token != null) {
                                    Variables.SessionToken = response.body()!!.Token
                                    val request3 = RequestU();
                                    request3.session_token = Variables.SessionToken

                                    val r3 = RetrofitRequest()
                                    val call3 = r3.apiService.check_notifications(request3)
                                    call3.enqueue(object : Callback<ResponseU> {
                                        override fun onResponse(
                                            call: Call<ResponseU>,
                                            response: Response<ResponseU>
                                        ) {
                                            showNotifications(response.body()!!.stringList, context)
                                        }

                                        override fun onFailure(
                                            call: Call<ResponseU>,
                                            t: Throwable
                                        ) {
                                            Log.d("API", "ERROR: " + t.message)
                                        }
                                    })
                                }
                            }
                            //Failure login
                            override fun onFailure(call: Call<ResponseU>, t: Throwable) {
                                Log.d("API", "ERROR: " + t.message)
                                Variables.password = null
                                showNotification("Error: you are unregistered", this@NotificationService)
                            }
                        })
                    }
                } else showNotifications(response.body()!!.stringList, context)
            }

            override fun onFailure(call: Call<ResponseU>, t: Throwable) {
                Log.d("API", "ERROR: " + t.message)
            }
        })
    }

    private fun showNotifications(list: List<String>?, context: Context) {
        if (list == null) return
        for (message in list) {
            showNotification(message, context)
        }
    }

    private fun showNotification(message: String, context: Context) {

            Log.d("API", message)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "LearnTogether", importance)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Learn Together")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManager.notify(Random().nextInt() / 100, builder.build())

    }
}
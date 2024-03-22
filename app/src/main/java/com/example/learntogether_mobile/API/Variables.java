package com.example.learntogether_mobile.API;

import android.content.Context;
import android.content.SharedPreferences;

public class Variables {
    public static String SessionToken, username, password, server = "http://80.89.196.150:8000/";

    public static void saveValues(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("SessionToken", SessionToken);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("server", server);
        editor.apply();
    }

    public static void loadValues(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SessionToken = sharedPref.getString("SessionToken", null);
        username = sharedPref.getString("username", null);
        password = sharedPref.getString("password", null);
        server = sharedPref.getString("server", "http://80.89.196.150:8000/");
    }
}
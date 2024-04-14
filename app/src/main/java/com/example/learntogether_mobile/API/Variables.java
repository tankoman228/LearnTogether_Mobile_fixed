package com.example.learntogether_mobile.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Variables {
    public static String SessionToken, username, password, server = "http://80.89.196.150:8000/";
    public static int current_id_group = 1;
    public static String Title, AboutMe;
    public static Bitmap myIcon;
    public static List<String> myPermissions;

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

    public static void requireMyAccountInfo() {
        RequestU requestU = new RequestU();
        requestU.setSession_token(Variables.SessionToken);
        requestU.setId_group(current_id_group);
        new RetrofitRequest().apiService.my_account_info(requestU).enqueue(new Callback<ListU>() {
            @Override
            public void onResponse(Call<ListU> call, Response<ListU> response) {
                Title = response.body().getTitle();
                AboutMe = response.body().getText();
                myIcon = ImageConverter.decodeImage(response.body().getIcon());
                myPermissions = response.body().getItems();
            }

            @Override
            public void onFailure(Call<ListU> call, Throwable t) {

            }
        });
    }

    public static boolean IsAllowed(String permission) {
        for (String s: myPermissions) {
            if (permission.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
package com.example.learntogether_mobile.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.learntogether_mobile.API.Loaders.GroupsAndUsersLoader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Глобальные переменные о данных сессии, пользователе и т.д., нужны для отправки запросов через Retrofit
 * и отображения некоторых данных.
 * Выполняет сохранение и загрузку из shared prefs
 */
public class GlobalVariables {
    public static String SessionToken, username, password, server = "http://80.89.196.150:8000/";
    public static int current_id_group = -1;
    public static String Title, AboutMe;
    public static Bitmap myIcon;
    public static List<String> myPermissions;

    public static void saveValuesToSharedPrefs(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("SessionToken", SessionToken);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("server", server);
        editor.putInt("current_id_group", current_id_group);
        editor.apply();
    }

    public static void loadValuesFromSharedPrefs(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SessionToken = sharedPref.getString("SessionToken", null);
        username = sharedPref.getString("username", null);
        password = sharedPref.getString("password", null);
        server = sharedPref.getString("server", "http://80.89.196.150:8000/");
        current_id_group = sharedPref.getInt("current_id_group", -1);
    }

    /**
     * Спрашивает у сервера о том, какие разрешения есть у пользователя в текущей группе
     */
    public static void requireMyAccountInfo(Context context) {
        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        if (current_id_group == -1) {
            GroupsAndUsersLoader.UpdateCacheGroups(() -> {
                current_id_group = GroupsAndUsersLoader.Groups.get(0).getID_Group();
                saveValuesToSharedPrefs(context);

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
            });
        }
        else {
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
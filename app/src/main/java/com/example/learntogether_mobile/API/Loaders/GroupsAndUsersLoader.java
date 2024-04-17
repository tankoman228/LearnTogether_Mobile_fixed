package com.example.learntogether_mobile.API.Loaders;

import android.util.Log;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Загрузка данных в оперативную память из базы, обратная связь через интерфейсы
 */
public class GroupsAndUsersLoader {

    public static List<ListU> UsersListForCurrentGroup = new ArrayList<>();
    public static List<ListU> Groups = new ArrayList<>();

    public static void UpdateCacheUsersForCurrentGroup(CallbackAfterLoaded callback) {

        RequestU request = new RequestU();
        request.setSession_token(GlobalVariables.SessionToken);
        request.setGroup(GlobalVariables.current_id_group);

        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_users(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                UsersListForCurrentGroup = new ArrayList<>();

                UsersListForCurrentGroup = response.body().users;
                callback.updateInterface();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("APPLOG", "error");
            }
        });
    }

    public static void UpdateCacheGroups(CallbackAfterLoaded callback) {

        RequestU request = new RequestU();
        request.setSession_token(GlobalVariables.SessionToken);

        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_groups(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                UsersListForCurrentGroup = new ArrayList<>();
                Groups = response.body().Results;
                callback.updateInterface();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("APPLOG", "error");
            }
        });
    }
}

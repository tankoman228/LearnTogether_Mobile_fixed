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
public class InfosLoader {
    public static List<ListU> Infos = new ArrayList<>();

    public static void Reload(CallbackAfterLoaded activityCentral, String searchString) {

        RequestU request = new RequestU();
        request.setGroup(GlobalVariables.current_id_group);
        request.setSearch_string(searchString);
        request.setNumber(20);
        request.setSession_token(GlobalVariables.SessionToken);
        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_infos(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                if (response.body() != null) {
                    if (response.body().Error != null) {
                        Log.d("API", response.body().Error);
                        return;
                    }
                    Infos = response.body().getInfos();
                    activityCentral.updateInterface();
                }
                else
                    Log.d("API", "NULL BODY 500");
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "REQUEST ERROR");
            }
        });
    }

    public static void Load(CallbackAfterLoaded activityCentral, String searchString) {

        RequestU request = new RequestU();
        request.setGroup(GlobalVariables.current_id_group);
        request.setSearch_string(searchString);
        request.setNumber(20);
        request.setId_max(findMinId() - 1);
        request.setSession_token(GlobalVariables.SessionToken);
        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_infos(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                if (response.body() != null) {
                    if (response.body().Error != null) {
                        Log.d("API", response.body().Error);
                        return;
                    }
                    if (response.body().getInfos().size() == 0) {
                        return;
                    }
                    Infos.addAll(response.body().getInfos());

                    Infos = response.body().getInfos();
                    activityCentral.updateInterface();
                }
                else
                    Log.d("API", "NULL BODY 500");
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "REQUEST ERROR");
            }
        });
    }

    private static int findMinId() {
        int minId = Integer.MAX_VALUE;
        for (ListU ask : Infos) {
            if (ask.getID_InfoBase() < minId) {
                minId = ask.getID_InfoBase();
            }
        }
        return minId;
    }
}

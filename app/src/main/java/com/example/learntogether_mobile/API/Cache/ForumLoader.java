package com.example.learntogether_mobile.API.Cache;

import android.util.Log;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumLoader {
    public static List<ListU> Asks = new ArrayList<>();
    public static volatile CallbackAfterLoaded activityCentral;

    public static void Reload(CallbackAfterLoaded activityCentral, String searchString) {

        RequestU request = new RequestU();
        request.setGroup(Variables.current_id_group);
        request.setSearch_string(searchString);
        request.setNumber(20);
        request.setSession_token(Variables.SessionToken);
        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_asks(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                if (response.body() != null) {
                    if (response.body().Error != null) {
                        Log.d("API", response.body().Error);
                        return;
                    }
                    Asks = response.body().getAsks();
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

    public static void loadLater(CallbackAfterLoaded activityCentral, String searchString) {

        RequestU request = new RequestU();
        request.setGroup(Variables.current_id_group);
        request.setSearch_string(searchString);
        request.setNumber(20);

        request.setId_max(findMinId() - 1);
        request.setSession_token(Variables.SessionToken);
        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_asks(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                if (response.body() != null) {
                    if (response.body().Error != null) {
                        Log.d("API", response.body().Error);
                        return;
                    }

                    if (response.body().getAsks() == null || response.body().getAsks().size() == 0)
                        return;

                    Asks.addAll(response.body().getAsks());
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
        for (ListU ask : Asks) {
            if (ask.getID_InfoBase() < minId) {
                minId = ask.getID_InfoBase();
            }
        }
        return minId;
    }
}

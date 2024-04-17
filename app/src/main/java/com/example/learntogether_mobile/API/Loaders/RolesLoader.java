package com.example.learntogether_mobile.API.Loaders;

import android.util.Log;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Загрузка данных в оперативную память из базы, обратная связь через интерфейсы
 */
public class RolesLoader {

    public static List<ListU> Roles;

    public static void UpdateRolesList(CallbackAfterLoaded callbackAfterLoaded) {

        Log.d("API", "Update roles list");
        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        new RetrofitRequest().apiService.get_roles(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                Roles = response.body().getRoles();
                callbackAfterLoaded.updateInterface();
                Log.d("API", "Roles loaded");
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void UpdateRolesListIfNotLoaded(CallbackAfterLoaded callbackAfterLoaded) {

        if (Roles != null && Roles.size() > 0) {
            Log.d("API", "Roles not loaded");
            callbackAfterLoaded.updateInterface();
            return;
        }
        UpdateRolesList(callbackAfterLoaded);
    }
}

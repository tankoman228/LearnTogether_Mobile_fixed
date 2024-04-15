package com.example.learntogether_mobile.API.Cache;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RolesLoader {

    public static List<ListU> Roles;

    public static void UpdateRolesList(CallbackAfterLoaded callbackAfterLoaded) {

        RequestU requestU = new RequestU();
        requestU.setSession_token(Variables.SessionToken);
        new RetrofitRequest().apiService.get_roles(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                Roles = response.body().getRoles();
                callbackAfterLoaded.updateInterface();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {

            }
        });
    }
}

package com.example.learntogether_mobile.API;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    private static String BASE_URL = "http://80.89.196.150:8000/";

    public ApiServiceInterface apiService;

    public RetrofitRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiServiceInterface.class);
    }
}

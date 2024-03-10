package com.example.learntogether_mobile.API;

import com.example.learntogether_mobile.API.Requests.LoginRequests;
import com.example.learntogether_mobile.API.Responses.LoginResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServiceInterface {

    @POST("/login/login")
    Call<LoginResponses.Login> login(@Body LoginRequests.Login request);

}

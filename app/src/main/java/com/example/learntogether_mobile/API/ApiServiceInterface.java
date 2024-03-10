package com.example.learntogether_mobile.API;

import com.example.learntogether_mobile.API.Requests.LoginRequests;
import com.example.learntogether_mobile.API.Responses.Comments;
import com.example.learntogether_mobile.API.Responses.LoginResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServiceInterface {

    @POST("/login/login")
    Call<LoginResponses.Login> login(@Body LoginRequests.Login request);

    @POST("/login/register")
    Call<LoginResponses.Register> register(@Body LoginRequests.Register request);

    @POST("/login/request_recovery")
    Call<LoginResponses.Recovery> recovery(@Body LoginRequests.Recovery request);


    @POST("/comments/get_comments")
    Call<Comments.GetCommentsResponse> get_comments(@Body com.example.learntogether_mobile.API.Requests.Comments.GetCommentsRequest request);
}

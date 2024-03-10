package com.example.learntogether_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.example.learntogether_mobile.API.Requests.Comments;
import com.example.learntogether_mobile.API.Requests.LoginRequests;
import com.example.learntogether_mobile.API.Responses.LoginResponses;
import com.example.learntogether_mobile.API.RetrofitRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Comments.GetCommentsRequest request = new Comments.GetCommentsRequest() {{
            sessionToken="1";
            objectId=5;
        }};

        RetrofitRequest r = new RetrofitRequest();
        Call<com.example.learntogether_mobile.API.Responses.Comments.GetCommentsResponse> call = r.apiService.get_comments(request);
        call.enqueue(new Callback<com.example.learntogether_mobile.API.Responses.Comments.GetCommentsResponse>() {
            @Override
            public void onResponse(Call<com.example.learntogether_mobile.API.Responses.Comments.GetCommentsResponse> call, Response<com.example.learntogether_mobile.API.Responses.Comments.GetCommentsResponse> response) {

                java.util.List<com.example.learntogether_mobile.API.Responses.Comments.Comment> comments = response.body().comments;
                for (int i = 0; i < comments.size(); i++) {
                    Log.d("API", comments.get(i).author);
                    Log.d("API", comments.get(i).dateTime);
                    Log.d("API", comments.get(i).text);
                }
            }

            @Override
            public void onFailure(Call<com.example.learntogether_mobile.API.Responses.Comments.GetCommentsResponse> call, Throwable t) {
                Log.d("API", t.getMessage());
            }
        });
    }
}
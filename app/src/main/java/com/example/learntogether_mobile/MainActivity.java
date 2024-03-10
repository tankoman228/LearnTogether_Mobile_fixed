package com.example.learntogether_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

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

        LoginRequests.Login request = new LoginRequests.Login() {{
            username = "tank228";
            password = "123";
        }};

        RetrofitRequest r = new RetrofitRequest();
        Call<LoginResponses.Login> call = r.apiService.login(request);
        call.enqueue(new Callback<LoginResponses.Login>() {
            @Override
            public void onResponse(Call<LoginResponses.Login> call, Response<LoginResponses.Login> response) {
                Log.d("API", response.body().result);
                try {
                    Log.d("API", response.body().token);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginResponses.Login> call, Throwable t) {
                Log.d("API", t.getMessage());
            }
        });
    }
}
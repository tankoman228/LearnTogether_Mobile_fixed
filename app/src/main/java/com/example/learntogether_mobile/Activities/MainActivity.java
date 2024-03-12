package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestU request = new RequestU() {{
            session_token="1";
            id_object=5;
        }};

        RetrofitRequest r = new RetrofitRequest();
        Call<ResponseU> call = r.apiService.get_comments(request);
        call.enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                java.util.List<ListU> comments = response.body().Comments;
                for (int i = 0; i < comments.size(); i++) {
                    Log.d("API", comments.get(i).Author);
                    Log.d("API", comments.get(i).DateTime);
                    Log.d("API", comments.get(i).Text);
                }
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "ERROR: " + t.getMessage());
            }
        });
    }
}
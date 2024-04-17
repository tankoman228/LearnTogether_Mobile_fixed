package com.example.learntogether_mobile.Activities.InsertRequestsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Присоединение к группе по приглашению
 */
public class ActJoinGroup extends AppCompatActivity {

    EditText etToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        etToken = findViewById(R.id.etPassword);
        findViewById(R.id.ibBack).setOnClickListener(l -> finish());
        findViewById(R.id.btnEnter).setOnClickListener(l -> {

            RequestU requestU = new RequestU();
            requestU.setSession_token(GlobalVariables.SessionToken);
            requestU.token = etToken.getText().toString();

            new RetrofitRequest().apiService.join_group(requestU).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    if (response.body().Error != null) {
                        Toast.makeText(ActJoinGroup.this, response.body().Error, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Toast.makeText(ActJoinGroup.this, R.string.the_new_group_added_to_list, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {

                }
            });
        });
    }
}
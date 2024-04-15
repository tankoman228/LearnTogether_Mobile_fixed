package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        TextView tvC = findViewById(R.id.tvClausesList);

        findViewById(R.id.btnReturn).setOnClickListener(l -> {
            finish();
        });
        findViewById(R.id.btnManageRoles).setOnClickListener(l -> {
            startActivity(new Intent(this, RolesEdit.class));
        });

        RequestU requestU = new RequestU();
        requestU.setID_Group(Variables.current_id_group);
        requestU.setSession_token(Variables.SessionToken);
        new RetrofitRequest().apiService.get_complaints(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                String s = "";
                for (var c: response.body().getComplaints()) {
                    s += "\n[" + c.getSender() + "] --!> [" + c.getSuspected()  + "] <=> " + c.getReason() + " : " + c.getDateTime() + "\n";
                }
                String finalS = s;
                runOnUiThread(() -> {
                    tvC.setText(finalS);
                });
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {

            }
        });
    }
}
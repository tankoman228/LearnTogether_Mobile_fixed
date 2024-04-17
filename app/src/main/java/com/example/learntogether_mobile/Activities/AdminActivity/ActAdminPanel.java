package com.example.learntogether_mobile.Activities.AdminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Панель со списком жалоб и переходом к редактированию ролей
 */
public class ActAdminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        TextView tvC = findViewById(R.id.tvClausesList);

        findViewById(R.id.btnReturn).setOnClickListener(l -> {
            finish();
        });
        findViewById(R.id.btnManageRoles).setOnClickListener(l -> {
            startActivity(new Intent(this, ActRolesEdit.class));
        });

        RequestU requestU = new RequestU();
        requestU.setID_Group(GlobalVariables.current_id_group);
        requestU.setSession_token(GlobalVariables.SessionToken);
        new RetrofitRequest().apiService.get_complaints(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                List<ListU> complaintsList = new ArrayList<>(response.body().getComplaints());
                Collections.reverse(complaintsList);

                StringBuilder s = new StringBuilder();
                for (var c : complaintsList) {
                    s.append("\n[").append(c.getSender()).append("] --!> [").append(c.getSuspected()).append("] <=> ").append(c.getReason()).append(" : ").append(c.getDateTime()).append("\n");
                }

                String finalS = s.toString();
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
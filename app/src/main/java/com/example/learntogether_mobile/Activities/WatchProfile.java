package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchProfile extends AppCompatActivity {

    public static ListU Profile;
    ImageView ivAvatar;
    TextView tvUserTitle, tvUsername, tvAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_profile);

        ivAvatar = findViewById(R.id.ivAvatar);
        tvUserTitle = findViewById(R.id.tvUserTitle);
        tvUsername = findViewById(R.id.tvUsername);
        tvAboutMe = findViewById(R.id.tvAboutMe);

        if (Profile.getIcon() != null && !Profile.getIcon().equals("")) {
            ivAvatar.setImageBitmap(ImageConverter.decodeImage(Profile.getIcon()));
        }
        tvUserTitle.setText(Profile.getTitle());
        tvUsername.setText(Profile.getUsername());
        tvAboutMe.setText(Profile.getAbout());

        findViewById(R.id.btnClause).setOnClickListener(l -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Complaint for user: " + Profile.getUsername());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_complaint, null);
            builder.setView(dialogView);

            EditText editTextComplaintReason = dialogView.findViewById(R.id.editTextComplaintReason);

            builder.setPositiveButton("Send complaint to admins", (dialog, which) -> {
                String complaintReason = editTextComplaintReason.getText().toString();
                RequestU requestU = new RequestU();
                requestU.setSession_token(Variables.SessionToken);
                requestU.setID_Group(Variables.current_id_group);
                requestU.setID_Account(Profile.getID_Account());
                requestU.setReason(complaintReason);
                new RetrofitRequest().apiService.complaint(requestU).enqueue(new Callback<ResponseU>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {

                    }
                });
                dialog.dismiss();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        if (Variables.IsAllowed("ban_accounts") || Variables.IsAllowed("edit_roles")) {
            findViewById(R.id.btnEditUserRole).setOnClickListener(l -> {
                EditUser.User = Profile;
                startActivity(new Intent(this, EditUser.class));
            });
        }
        else {
            findViewById(R.id.btnEditUserRole).setVisibility(View.GONE);
        }
    }
}
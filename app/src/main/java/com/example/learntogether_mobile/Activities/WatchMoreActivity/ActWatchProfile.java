package com.example.learntogether_mobile.Activities.WatchMoreActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.Activities.AdminActivity.ActEditUser;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Просмотр профиля другого пользователя
 */
public class ActWatchProfile extends AppCompatActivity {

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
            builder.setTitle(getString(R.string.complaint_for_user) + Profile.getUsername());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_complaint, null);
            builder.setView(dialogView);

            EditText editTextComplaintReason = dialogView.findViewById(R.id.editTextComplaintReason);

            builder.setPositiveButton(R.string.send_complaint_to_admins, (dialog, which) -> {
                String complaintReason = editTextComplaintReason.getText().toString();
                RequestU requestU = new RequestU();
                requestU.setSession_token(GlobalVariables.SessionToken);
                requestU.setID_Group(GlobalVariables.current_id_group);
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

            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        if (GlobalVariables.IsAllowed("ban_accounts") || GlobalVariables.IsAllowed("edit_roles")) {
            findViewById(R.id.btnEditUserRole).setOnClickListener(l -> {
                ActEditUser.User = Profile;
                startActivity(new Intent(this, ActEditUser.class));
            });
        }
        else {
            findViewById(R.id.btnEditUserRole).setVisibility(View.GONE);
        }
    }
}
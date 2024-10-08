package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.R;

import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Редактирование своего профиля пользователем
 */
public class ActEditMyProfile extends AppCompatActivity {

    ImageButton ibAvatar;
    EditText etName, etDescr;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        ibAvatar = findViewById(R.id.ibAvatar);
        etName = findViewById(R.id.etNameDisplayed);
        etDescr = findViewById(R.id.etDescription);

        etName.setText(GlobalVariables.Title);
        etDescr.setText(GlobalVariables.AboutMe);
        if (GlobalVariables.myIcon != null)
            ibAvatar.setImageBitmap(GlobalVariables.myIcon);

        findViewById(R.id.btnReturnBack).setOnClickListener(l -> finish());
        ibAvatar.setOnClickListener(l -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), 1);
        });
        findViewById(R.id.btnSaveChanges).setOnClickListener(l -> {
            RequestU requestU = new RequestU();
            requestU.setNewName(etName.getText().toString());
            if (selectedImage != null) {
                requestU.setNewIcon(ImageConverter.encodeImage(selectedImage));
                GlobalVariables.myIcon = selectedImage;
                GlobalVariables.Title = requestU.getNewName();
                GlobalVariables.AboutMe = requestU.getNewDescription();
            }
            else {
                requestU.setNewIcon(ImageConverter.encodeImage(GlobalVariables.myIcon));
                GlobalVariables.Title = requestU.getNewName();
                GlobalVariables.AboutMe = requestU.getNewDescription();
            }
            requestU.setNewDescription(etDescr.getText().toString());
            requestU.setSession_token(GlobalVariables.SessionToken);
            new RetrofitRequest().apiService.edit_profile(requestU).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    Toast.makeText(ActEditMyProfile.this, Objects.requireNonNullElse(response.body().Error, getString(R.string.success)), Toast.LENGTH_SHORT).show();
                    GlobalVariables.requireMyAccountInfo(ActEditMyProfile.this);
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {

                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                selectedImage = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                int width = selectedImage.getWidth();
                int height = selectedImage.getHeight();

                int size = Math.min(width, height);
                int x = (width - size) / 2;
                int y = (height - size) / 2;

                selectedImage = Bitmap.createBitmap(selectedImage, x, y, size, size);

                selectedImage = Bitmap.createScaledBitmap(selectedImage, 512, 512, true);
                ibAvatar.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
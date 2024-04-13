package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMyProfile extends AppCompatActivity {

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

        etName.setText(Variables.Title);
        etDescr.setText(Variables.AboutMe);
        ibAvatar.setImageBitmap(Variables.myIcon);

        findViewById(R.id.btnReturnBack).setOnClickListener(l -> finish());
        ibAvatar.setOnClickListener(l -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });
        findViewById(R.id.btnSaveChanges).setOnClickListener(l -> {
            RequestU requestU = new RequestU();
            requestU.setNewName(etName.getText().toString());
            requestU.setNewIcon(ImageConverter.encodeImage(selectedImage));
            requestU.setNewDescription(etDescr.getText().toString());
            requestU.setSession_token(Variables.SessionToken);
            new RetrofitRequest().apiService.edit_profile(requestU).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    Toast.makeText(EditMyProfile.this, Objects.requireNonNullElse(response.body().Error, "Success"), Toast.LENGTH_SHORT).show();
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

                if (width > 512 || height > 512) {
                    float scale = Math.min(512f / width, 512f / height);
                    Matrix matrix = new Matrix();
                    matrix.postScale(scale, scale);
                    selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, width, height, matrix, false);
                }
                ibAvatar.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
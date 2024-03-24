package com.example.learntogether_mobile.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNews extends AppCompatActivity {

    private ImageButton ibNext, ibPrevious;
    private TextView tvNum;
    private PhotoView ivImage;
    private Spinner spType;
    private Button btnSave;
    private TextView tvWarningNotModerator;
    private TextView tvSelectType;
    private EditText etHeader;
    private EditText etDescription;
    private EditText editTextDate;
    private Button btnAddImage;
    private Button btnDeleteImage;
    private EditText etItems;
    private TextView tvDivideWith;
    private CheckBox cbAnon;
    private CheckBox cbMultiAnswer;
    private EditText etTaglist;


    private static final int PICK_IMAGE_REQUEST = 1;
    private String selectedType = "N";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        ibNext = findViewById(R.id.ibNext);
        ibPrevious = findViewById(R.id.ibPrevious);
        tvNum = findViewById(R.id.tvNum);
        ivImage = findViewById(R.id.ivImage);
        spType = findViewById(R.id.spType);
        btnSave = findViewById(R.id.btnSave);
        tvWarningNotModerator = findViewById(R.id.tvWarningNotModerator);
        tvSelectType = findViewById(R.id.tvSelectType);
        etHeader = findViewById(R.id.etHeader);
        etDescription = findViewById(R.id.etDescription);
        editTextDate = findViewById(R.id.editTextDate);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnDeleteImage = findViewById(R.id.btnDeleteImage);
        etItems = findViewById(R.id.etItems);
        tvDivideWith = findViewById(R.id.tvDivideWith);
        cbAnon = findViewById(R.id.cbAnon);
        cbMultiAnswer = findViewById(R.id.cbMultiAnswer);
        etTaglist = findViewById(R.id.etTaglist);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = spType.getSelectedItem().toString().substring(0, 1);
                Log.d("APPLOG", selectedType);
                updateUi();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = "N";
            }
        });
        btnSave.setOnClickListener(l -> btnSaveOnclickListener());

        initImagesLoading();

        updateUi();
    }
    private void updateUi() {
        switch (selectedType) {
            case "N" -> {
                ibNext.setVisibility(View.VISIBLE);
                ibPrevious.setVisibility(View.VISIBLE);
                tvNum.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.VISIBLE);
                editTextDate.setVisibility(View.GONE);
                btnAddImage.setVisibility(View.VISIBLE);
                btnDeleteImage.setVisibility(View.VISIBLE);
                etItems.setVisibility(View.GONE);
                cbAnon.setVisibility(View.GONE);
                cbMultiAnswer.setVisibility(View.GONE);
                tvDivideWith.setVisibility(View.GONE);
            }
            case "T" -> {
                ibNext.setVisibility(View.GONE);
                ibPrevious.setVisibility(View.GONE);
                tvNum.setVisibility(View.GONE);
                ivImage.setVisibility(View.GONE);
                editTextDate.setVisibility(View.VISIBLE);
                btnAddImage.setVisibility(View.GONE);
                btnDeleteImage.setVisibility(View.GONE);
                etItems.setVisibility(View.GONE);
                cbAnon.setVisibility(View.GONE);
                cbMultiAnswer.setVisibility(View.GONE);
                tvDivideWith.setVisibility(View.GONE);
            }
            case "V" -> {
                ibNext.setVisibility(View.GONE);
                ibPrevious.setVisibility(View.GONE);
                tvNum.setVisibility(View.GONE);
                ivImage.setVisibility(View.GONE);
                editTextDate.setVisibility(View.GONE);
                btnAddImage.setVisibility(View.GONE);
                btnDeleteImage.setVisibility(View.GONE);
                etItems.setVisibility(View.VISIBLE);
                cbAnon.setVisibility(View.VISIBLE);
                cbMultiAnswer.setVisibility(View.VISIBLE);
                tvDivideWith.setVisibility(View.VISIBLE);
            }
            default ->
                    Toast.makeText(this, "Select news type", Toast.LENGTH_SHORT).show();
        }
    }


    //News special UI logic
    int currentPhotoI = 0;
    private Bitmap selectedImage;
    private List<Bitmap> selectedImages = new ArrayList<>();
    private void initImagesLoading() {

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(galleryIntent);
            } else {
                Toast.makeText(this, "Access to your gallery denied", Toast.LENGTH_SHORT).show();
            }
        });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    selectedImages.add(selectedImage);
                    currentPhotoI = -1;
                    changeCurrentPhotoI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ibNext.setOnClickListener(l -> {
            currentPhotoI++;
            changeCurrentPhotoI();
        });
        ibPrevious.setOnClickListener(l -> {
            currentPhotoI--;
            changeCurrentPhotoI();
        });
        btnAddImage.setOnClickListener(l -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(galleryIntent);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
                else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });
        btnDeleteImage.setOnClickListener(l -> {
            try {
                selectedImages.remove(currentPhotoI);
            } catch (Exception e) {e.printStackTrace();}
            changeCurrentPhotoI();
        });
    }
    private void changeCurrentPhotoI() {

        if (selectedImages.size() == 0) {
            ivImage.setImageResource(R.drawable.logo);
            tvNum.setText("0/0");
            return;
        }

        if (currentPhotoI >= selectedImages.size())
            currentPhotoI = 0;
        else if (currentPhotoI < 0) {
            currentPhotoI = selectedImages.size() - 1;
        }

        ivImage.setImageBitmap(selectedImages.get(currentPhotoI));
        tvNum.setText(new StringBuilder().append(currentPhotoI + 1).append("/").append(selectedImages.size()).toString());
    }
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    //Vote special UI logic
    String[] items;
    private void initVoteUI() {
        etItems.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("APPLOG", "skip tc");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("APPLOG", "skip tc");
            }
            @Override
            public void afterTextChanged(Editable s) {
                items = s.toString().split(";");
                StringBuilder text = new StringBuilder("Divide with \",\"\n");
                for (String item: items) {
                    text.append(item).append("\n");
                }
                tvDivideWith.setText(text);
            }
        });
    }


    //Sending to server
    private void btnSaveOnclickListener() {

        if (etHeader.getText().toString().length() < 2 ||
            etDescription.getText().toString().length() < 2 ||
            etTaglist.getText().toString().length() < 2
        ) {
            Toast.makeText(this, "Unfilled values!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setVisibility(View.GONE);
        switch (selectedType) {
            case "N" -> {
                saveNews();
            }
            case "T" -> {
                saveTask();
            }
            case "V" -> {
                saveVote();
            }
            default ->
                    Toast.makeText(this, "Select news type", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveNews() {
        RequestU request = new RequestU();
        request.setSession_token(Variables.SessionToken);
        request.setId_group(Variables.current_id_group);
        request.title = etHeader.getText().toString();
        request.setText(etDescription.getText().toString());
        request.setTags(etTaglist.getText().toString());
        request.setImages(ImageConverter.encodeImages(selectedImages));

        RetrofitRequest r = new RetrofitRequest();
        r.apiService.add_news(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                if (response.body().Error != null) {
                    Toast.makeText(AddNews.this, response.body().Error, Toast.LENGTH_SHORT).show();
                    return;
                }
                AddNews.this.finish();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Toast.makeText(AddNews.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveTask() {
        RequestU request = new RequestU();
        request.setSession_token(Variables.SessionToken);
        request.setId_group(Variables.current_id_group);
        request.title = etHeader.getText().toString();
        request.setText(etDescription.getText().toString());
        request.setTags(etTaglist.getText().toString());
        request.setDeadline(editTextDate.getText().toString());


    }
    private void saveVote(){
        RequestU request = new RequestU();
        request.setSession_token(Variables.SessionToken);
        request.setId_group(Variables.current_id_group);
        request.title = etHeader.getText().toString();
        request.setText(etDescription.getText().toString());
        request.setTags(etTaglist.getText().toString());
    }
}
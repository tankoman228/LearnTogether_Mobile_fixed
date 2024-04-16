package com.example.learntogether_mobile.Activities.InsertRequests;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.FileEncoderDecoder;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAddInfo extends AppCompatActivity {

    EditText etTitle, etText, etTags;
    TextView tvFilenames;
    Button btnAdd, btnDelete, btnSave;
    Uri[] uris = new Uri[0];
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        etTags = findViewById(R.id.etTaglist);
        etText = findViewById(R.id.etDescription);
        etTitle = findViewById(R.id.etHeader);
        tvFilenames = findViewById(R.id.tvFilenames);
        btnAdd = findViewById(R.id.btnAddImage);
        btnDelete = findViewById(R.id.btnDeleteImage);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        btnAdd.setOnClickListener(l -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.getExternalStorageDirectory().toURI());
            }

            startActivityForResult(intent, REQUEST_CODE);
        });
        btnDelete.setOnClickListener(l -> {
            uris = new Uri[0];
            tvFilenames.setText(R.string.no_files_selected);
        });
        btnSave.setOnClickListener(l -> {

            String title = etTitle.getText().toString();
            String tags = etTags.getText().toString();
            String text = etText.getText().toString();
            if (uris.length == 0 || title.length() < 2 || tags.length() < 2 || text.length() < 2) {
                Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            btnSave.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            RequestU requestU = new RequestU();
            requestU.setSession_token(Variables.SessionToken);
            requestU.setId_group(Variables.current_id_group);
            requestU.title = title;
            requestU.setText(text);
            requestU.setTags(tags);
            requestU.setContents(FileEncoderDecoder.encodeFilesToBase64(uris, this));
            requestU.setType("f");
            new RetrofitRequest().apiService.add_info(requestU).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    if (response.body() == null) {
                        Toast.makeText(ActAddInfo.this, "500", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> { btnSave.setEnabled(true); progressBar.setVisibility(View.GONE);});
                        return;
                    }
                    else if (response.body().Error != null) {
                        Toast.makeText(ActAddInfo.this, response.body().Error, Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> { btnSave.setEnabled(true); progressBar.setVisibility(View.GONE);});
                        return;
                    }
                    Toast.makeText(ActAddInfo.this, R.string.success, Toast.LENGTH_SHORT).show();
                    ActAddInfo.this.runOnUiThread(ActAddInfo.this::finish);
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {

                }
            });
        });
    }


    final int REQUEST_CODE = 150150300;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
            uris = new Uri[]{uri};
        } else if (data != null && data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            uris = new Uri[count];
            for (int i = 0; i < count; i++) {
                uris[i] = data.getClipData().getItemAt(i).getUri();
            }
        } else {
            uris = new Uri[0];
        }
        afterSelectedFiles();
    }

    private void afterSelectedFiles() {
        StringBuilder str = new StringBuilder("Files: ");
        for (Uri file : uris) {
            str.append(file.getPath() + "\n");
        }
        tvFilenames.setText(str.toString());
    }
}
package com.example.learntogether_mobile.Activities.InsertRequestsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActForumAskAdd extends AppCompatActivity {

    EditText etTitle, etText, etTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_ask_add);

        etTitle = findViewById(R.id.etHeader);
        etText = findViewById(R.id.etText);
        etTags = findViewById(R.id.etTags);

        findViewById(R.id.btnCancel).setOnClickListener(l -> {
            finish();
        });
        findViewById(R.id.btnAdd).setOnClickListener(l -> {

            String title = etTitle.getText().toString();
            String text = etText.getText().toString();
            String tags = etTags.getText().toString();

            if (title.equals("") || text.equals("") || tags.equals("")) {
                Toast.makeText(this, "Unfilled fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] tagss = tags.split(",");
            for (String tagg: tagss) {
                if (tagg.length() > 32 || tagg.length() < 1) {
                    Toast.makeText(this, "Tag can\'t mustn\'t be empty or longer than 32 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(this, "Query started. Sending response to server", Toast.LENGTH_LONG).show();

            RequestU request = new RequestU();
            request.setId_group(Variables.current_id_group);
            request.setTags(tags);
            request.setSession_token(Variables.SessionToken);
            request.title = title;
            request.setText(text);

            var r = new RetrofitRequest();
            r.apiService.add_forum_ask(request).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    if (response.body() == null) {
                        Toast.makeText(ActForumAskAdd.this, "Request error 500", Toast.LENGTH_LONG).show();
                        return;
                    } else if (response.body().Error != null) {
                        Toast.makeText(ActForumAskAdd.this, response.body().Error, Toast.LENGTH_LONG).show();
                        return;
                    }
                    ActForumAskAdd.this.finish();
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                    Toast.makeText(ActForumAskAdd.this, "Request error", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
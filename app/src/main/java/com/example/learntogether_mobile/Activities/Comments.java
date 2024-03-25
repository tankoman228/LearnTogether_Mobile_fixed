package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.Cache.CallbackAfterLoaded;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.Adapters.AdapterComment;
import com.example.learntogether_mobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Comments extends AppCompatActivity implements CallbackAfterLoaded {

    public static int ID_InfoBase = -1;
    public List<ListU> comments = new ArrayList<>();
    private ConstraintLayout commentForm;
    private EditText et;

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        lv = findViewById(R.id.lv);
        et = findViewById(R.id.editTextTextMultiLine);
        commentForm = findViewById(R.id.commentAddForm);


        findViewById(R.id.btnBack).setOnClickListener(l -> {
            finish();
        });
        findViewById(R.id.floatingActionButton).setOnClickListener(l -> {
            commentForm.setVisibility(View.VISIBLE);
        });
        findViewById(R.id.btnCancel).setOnClickListener(l -> {
            commentForm.setVisibility(View.INVISIBLE);
        });
        findViewById(R.id.btnSend).setOnClickListener(l -> {

            String text = et.getText().toString();

            if (text.length() < 2) {
                Toast.makeText(this, "Too short message!", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestU request = new RequestU();
            request.setId_object(ID_InfoBase);
            request.setSession_token(Variables.SessionToken);
            request.setText(text);
            RetrofitRequest r = new RetrofitRequest();
            r.apiService.add_comment(request).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    if (response.body() == null) {
                        Toast.makeText(Comments.this, "500", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.body().Error != null) {
                        Toast.makeText(Comments.this, response.body().Error, Toast.LENGTH_SHORT).show();
                    }
                    Comments.this.runOnUiThread(() -> {
                        et.setText("");
                        commentForm.setVisibility(View.GONE);
                        reloadComments();
                    });
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                    Toast.makeText(Comments.this, ":(", Toast.LENGTH_SHORT).show();
                }
            });
        });

        reloadComments();
    }

    @Override
    public void updateInterface() {
        this.runOnUiThread(() -> {
            lv.setAdapter(new AdapterComment(this, new ArrayList<>(comments)));
        });
    }

    private void reloadComments() {
        RequestU request = new RequestU();
        request.setId_object(ID_InfoBase);
        request.setSession_token(Variables.SessionToken);
        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_comments(request).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                comments = response.body().getComments();
                updateInterface();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "Comments ERROR");
            }
        });
    }
}
package com.example.learntogether_mobile.Activities.InsertRequests;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAddMeeting extends AppCompatActivity {

    Date deadlineDate;

    TextView tv_Deadline;
    Button btnSave;
    TextView tvSelectType;
    EditText etHeader;
    EditText etDescription;
    EditText etPlace;
    EditText etTaglist;
    Button btnWhenDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        btnSave = findViewById(R.id.btnSave);
        tvSelectType = findViewById(R.id.tvSelectType);
        etHeader = findViewById(R.id.etHeader);
        etDescription = findViewById(R.id.etDescription);
        etPlace = findViewById(R.id.etPlace);
        etTaglist = findViewById(R.id.etTaglist);
        btnWhenDate = findViewById(R.id.btnWhenDate);
        tv_Deadline = findViewById(R.id.tv_Deadline);

        btnWhenDate.setOnClickListener(l -> {
            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                deadlineDate = calendar.getTime();
                updateDeadlineText();
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        btnSave.setOnClickListener(l -> {

            if (    etHeader.getText().toString().length() == 0 ||
                    etDescription.getText().toString().length() == 0 ||
                    etTaglist.getText().toString().length() == 0 ||
                    etPlace.getText().toString().length() == 0) {
                Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            if (deadlineDate == null) {
                Toast.makeText(this, R.string.select_date_of_meeting, Toast.LENGTH_SHORT).show();
                return;
            }

            RequestU requestU = new RequestU();
            requestU.setId_group(Variables.current_id_group);
            requestU.title = etHeader.getText().toString();
            requestU.setText(etDescription.getText().toString());
            requestU.setTags(etTaglist.getText().toString());
            requestU.setStarts(tv_Deadline.getText().toString());
            requestU.setPlace(etPlace.getText().toString());
            requestU.setSession_token(Variables.SessionToken);

            btnSave.setEnabled(false);
            RetrofitRequest r = new RetrofitRequest();
            r.apiService.add_meeting(requestU).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    if (response.body().Error != null) {
                        Toast.makeText(ActAddMeeting.this, response.body().Error, Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> btnSave.setEnabled(true));
                    }
                    else {
                        ActAddMeeting.this.finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {

                }
            });
        });

        updateDeadlineText();
    }

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private void  updateDeadlineText() {
        if (deadlineDate == null) {
            tv_Deadline.setText(R.string.select_meeting_date);
        }
        else {
            tv_Deadline.setText(dateFormat.format(deadlineDate));
        }
    }
}
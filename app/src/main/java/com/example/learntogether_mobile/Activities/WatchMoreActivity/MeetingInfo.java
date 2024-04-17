package com.example.learntogether_mobile.Activities.WatchMoreActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.Activities.Adapters.GanttAdapter;
import com.example.learntogether_mobile.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Отображение, кто пришёл на встречу, присоединение, диаграмма Ганта
 */
public class MeetingInfo extends AppCompatActivity {

    public static ListU meeting;

    TextView tvTitle, tvText, tvWhen, tvWhere;
    SeekBar sbFrom, sbUntil, sbSure;
    RecyclerView rvJoined, rvRefused;
    ToggleButton toggleButton;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info);

        tvTitle = findViewById(R.id.tvTitle);
        tvText = findViewById(R.id.tvText);
        tvWhen = findViewById(R.id.tvWhen);
        tvWhere = findViewById(R.id.tvWhere);
        sbFrom = findViewById(R.id.seekBarFrom);
        sbSure = findViewById(R.id.seekBarSure);
        sbUntil = findViewById(R.id.seekBarUntil);
        rvJoined = findViewById(R.id.rvJoined);
        rvRefused = findViewById(R.id.rvRefused);
        toggleButton = findViewById(R.id.toggleButton);
        btnJoin = findViewById(R.id.btnJoin);

        tvTitle.setText(meeting.getTitle());
        tvText.setText(meeting.getText());
        tvWhere.setText(meeting.getPlace());
        tvWhen.setText(meeting.getStartsAt());

        sbFrom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (sbUntil.getProgress() < progress) {
                    sbUntil.setProgress(progress);
                }
                updateSelectedTime();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbUntil.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (sbFrom.getProgress() > progress) {
                    sbFrom.setProgress(progress);
                }
                updateSelectedTime();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbSure.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSelectedTime();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnJoin.setOnClickListener(l -> {
            JoinRetrofit();
        });
        toggleButton.setOnClickListener(l -> {
            LoadRetrofit();
        });

        updateSelectedTime();
        LoadRetrofit();
    }

    private void updateSelectedTime() {

        if (sbSure.getProgress() == 0) {
            btnJoin.setText("Refuse meeting");
            return;
        }

        String text = getTimeStringFromMinutes(sbFrom.getProgress());;
        text += " - ";
        text += getTimeStringFromMinutes(sbUntil.getProgress());
        text += "\t Sure in ";
        text += sbSure.getProgress();
        text += "%";
        btnJoin.setText(text);
    }
    public static String getTimeStringFromMinutes(int minutes) {
        int hours = minutes / 60;
        int mints = minutes % 60;

        String hh = String.valueOf(hours), mm = String.valueOf(mints);
        if (hh.length() < 2) hh = 0 + hh;
        if (mm.length() < 2) mm = 0 + mm;

        return hh + ":" + mm;
    }


    private void JoinRetrofit() {
        GanttAdapter.ShowUsername = toggleButton.isChecked();

        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        requestU.setId_object(meeting.getID_Meeting());
        requestU.setStarts(String.valueOf(sbFrom.getProgress()));
        requestU.setEnd(String.valueOf(sbUntil.getProgress()));
        requestU.setSurety(sbSure.getProgress());
        new RetrofitRequest().apiService.join_meeting(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                LoadRetrofit();
                Toast.makeText(MeetingInfo.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {

            }
        });
    }

    private void LoadRetrofit() {
        GanttAdapter.ShowUsername = !toggleButton.isChecked();

        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        requestU.setId_object(meeting.getID_Meeting());
        new RetrofitRequest().apiService.get_meeting(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                MeetingInfo.this.runOnUiThread(() -> {

                    var joined = new ArrayList<ListU>();
                    var refused = new ArrayList<ListU>();

                    assert response.body() != null;
                    assert response.body().Results != null;
                    for (var resp: response.body().Results) {
                        if (resp.getSurety() <= 1) {
                            refused.add(resp);
                        }
                        else {
                            joined.add(resp);
                        }
                    }

                    rvJoined.setLayoutManager(new LinearLayoutManager(MeetingInfo.this));
                    rvRefused.setLayoutManager(new LinearLayoutManager(MeetingInfo.this));

                    rvJoined.setAdapter(new GanttAdapter(joined));
                    rvRefused.setAdapter(new GanttAdapter(refused));
                });
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {

            }
        });
    }
}
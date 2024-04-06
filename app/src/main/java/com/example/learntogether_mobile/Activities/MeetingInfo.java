package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.R;

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
        tvWhen.setText(meeting.getDateTime());

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

        });
        LoadRetrofit();
    }

    private void updateSelectedTime() {
        String text = getTimeStringFromMinutes(sbFrom.getProgress());;
        text += " - ";
        text += getTimeStringFromMinutes(sbUntil.getProgress());
        text += "\t ±";
        text += getTimeStringFromMinutes(sbSure.getProgress());
        text += "%";
        btnJoin.setText(text);
    }
    private String getTimeStringFromMinutes(int minutes) {
        int hours = minutes / 60;
        int mints = minutes % 60;
        return hours + ":" + mints;
    }


    private void JoinRetrofit() {

    }

    private void LoadRetrofit() {

    }
}
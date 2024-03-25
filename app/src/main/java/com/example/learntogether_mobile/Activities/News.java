package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.learntogether_mobile.API.Cache.CallbackAfterLoaded;
import com.example.learntogether_mobile.API.Cache.ForumLoader;
import com.example.learntogether_mobile.API.Cache.NewsLoader;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.Activities.Adapters.AdapterForum;
import com.example.learntogether_mobile.Activities.Adapters.AdapterNews;
import com.example.learntogether_mobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class News extends AppCompatActivity implements CallbackAfterLoaded {

    protected final int tabNews = 1, tabInfo = 2, tabMeetings = 3, tabForum = 4, tabPeople = 5;
    protected int currentTab = tabNews;


    EditText etSearch;
    ImageButton btnNews, btnInfo, btnMeetings, btnForum, btnPeople, btnHelp, btnOptions, btnMyProfile;
    ListView listView;
    FloatingActionButton fb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        etSearch = findViewById(R.id.etSearch);
        btnNews = findViewById(R.id.btnNews);
        btnInfo = findViewById(R.id.btnFiles);
        btnMeetings = findViewById(R.id.btnMeet);
        btnForum = findViewById(R.id.btnForum);
        btnPeople = findViewById(R.id.btnPeople);
        btnHelp = findViewById(R.id.ibHelp);
        btnMyProfile = findViewById(R.id.ibProfile);
        btnOptions = findViewById(R.id.ibOptions);
        listView = findViewById(R.id.rv);
        fb = findViewById(R.id.floatingActionButton2);

        btnNews.setOnClickListener(l -> loadTab(tabNews));
        //...
        btnForum.setOnClickListener(l -> loadTab(tabForum));

        fb.setOnClickListener(l -> {
            switch (currentTab) {
                case tabNews:
                    startActivity(new Intent(this, AddNews.class));
                    break;
                case tabForum:
                    startActivity(new Intent(this, ForumAskAdd.class));
                    break;
                default:
                    break;
            }

        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadTab(currentTab);
            }
        });

        Log.d("API", "1");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTab(currentTab);
    }

    protected void loadTab(int tabId) {
        currentTab = tabId;
        Log.d("API", "2");
        switch (currentTab) {
            case tabNews:
                Log.d("API", "3");
                NewsLoader.loadFromRetrofit(this,this, etSearch.getText().toString(), 999999);
                break;
            case tabForum:
                ForumLoader.Reload(this, etSearch.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void updateInterface() {
        this.runOnUiThread(() -> {
            if (currentTab == tabNews) {
                listView.setAdapter(new AdapterNews(this, NewsLoader.news_list));
                Log.d("API", "news loaded to adapter");
            }
            else if (currentTab == tabForum)  {
                listView.setAdapter(new AdapterForum(this, new ArrayList<>(ForumLoader.Asks)));
                Log.d("API", "forum loaded to adapter");
            }
        });
    }
}
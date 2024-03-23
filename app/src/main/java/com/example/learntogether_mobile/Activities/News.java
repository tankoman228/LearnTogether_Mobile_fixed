package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.learntogether_mobile.API.Cache.NewsLoader;
import com.example.learntogether_mobile.Activities.Adapters.AdapterNews;
import com.example.learntogether_mobile.R;

public class News extends AppCompatActivity {

    protected final int tabNews = 1, tabInfo = 2, tabMeetings = 3, tabForum = 4, tabPeople = 5;
    protected int currentTab = tabNews;


    EditText etSearch;
    ImageButton btnNews, btnInfo, btnMeetings, btnForum, btnPeople, btnHelp, btnOptions, btnMyProfile;
    ListView listView;


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

        loadTab(currentTab);
    }

    protected void loadTab(int tabId) {
        currentTab = tabId;
        switch (currentTab) {
            case tabNews:
                loadNews();
                break;
            default:
                break;
        }
    }

    protected void loadNews() {
        NewsLoader.loadFromRetrofit(this, etSearch.getText().toString(), 999999);
        listView.setAdapter(new AdapterNews(this, NewsLoader.news_list));
    }
}
package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.learntogether_mobile.API.Cache.CallbackAfterLoaded;
import com.example.learntogether_mobile.API.Cache.ForumLoader;
import com.example.learntogether_mobile.API.Cache.InfosLoader;
import com.example.learntogether_mobile.API.Cache.MeetingsLoader;
import com.example.learntogether_mobile.API.Cache.NewsLoader;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.Activities.Adapters.AdapterForum;
import com.example.learntogether_mobile.Activities.Adapters.AdapterInfo;
import com.example.learntogether_mobile.Activities.Adapters.AdapterMeetings;
import com.example.learntogether_mobile.Activities.Adapters.AdapterNews;
import com.example.learntogether_mobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class News extends AppCompatActivity implements CallbackAfterLoaded {

    protected final int tabNews = 1, tabInfo = 2, tabMeetings = 3, tabForum = 4, tabPeople = 5;
    protected int currentTab = tabNews;


    EditText etSearch;
    ImageButton btnNews, btnInfo, btnMeetings, btnForum, btnPeople, btnHelp, btnOptions;
    Button btnMyProfile;
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
        btnMyProfile = findViewById(R.id.btnProfile);
        //btnOptions = findViewById(R.id.ibOptions);
        listView = findViewById(R.id.rv);
        fb = findViewById(R.id.floatingActionButton2);

        btnNews.setOnClickListener(l -> loadTab(tabNews));
        btnInfo.setOnClickListener(l -> loadTab(tabInfo));
        btnMeetings.setOnClickListener(l -> loadTab(tabMeetings));
        //...
        btnForum.setOnClickListener(l -> loadTab(tabForum));
        btnMyProfile.setOnClickListener(l -> startActivity(new Intent(this, EditMyProfile.class)));

        fb.setOnClickListener(l -> {
            switch (currentTab) {
                case tabNews:
                    startActivity(new Intent(this, AddNews.class));
                    break;
                case tabInfo:
                    startActivity(new Intent(this, AddInfo.class));
                    break;
                case tabMeetings:
                    startActivity(new Intent(this, AddMeeting.class));
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (listView.getAdapter().getCount() - 1)) {
                    switch (currentTab) {
                        case tabNews ->
                                NewsLoader.loadFromRetrofitMore(News.this, News.this, etSearch.getText().toString());
                        case tabInfo -> InfosLoader.Load(News.this, etSearch.getText().toString());
                        case tabMeetings ->
                                MeetingsLoader.Load(News.this, etSearch.getText().toString());
                        case tabForum ->
                                ForumLoader.loadLater(News.this, etSearch.getText().toString());
                        default -> {
                        }
                    }
                    // Now your listview has hit the bottom
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
            case tabNews -> {
                Log.d("API", "3");
                NewsLoader.loadFromRetrofit(this, this, etSearch.getText().toString());
            }
            case tabInfo -> {
                InfosLoader.Reload(this, etSearch.getText().toString());
            }
            case tabMeetings -> MeetingsLoader.Reload(this, etSearch.getText().toString());
            case tabForum -> ForumLoader.Reload(this, etSearch.getText().toString());
            default -> {
            }
        }
    }

    @Override
    public void updateInterface() {
        this.runOnUiThread(() -> {
            int firstVisibleItem = listView.getFirstVisiblePosition();
            View firstVisibleView = listView.getChildAt(0);
            int top = (firstVisibleView == null) ? 0 : (firstVisibleView.getTop() - listView.getPaddingTop());

            if (currentTab == tabNews) {
                AdapterNews adapter = new AdapterNews(this, NewsLoader.news_list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("API", "news loaded to adapter");
            } else if (currentTab == tabInfo) {
                AdapterInfo adapter = new AdapterInfo(this, new ArrayList<>(InfosLoader.Infos));
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else if (currentTab == tabMeetings) {
                AdapterMeetings adapter = new AdapterMeetings(this, new ArrayList<>(MeetingsLoader.Meetings));
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("API", "MEETINGS loaded to adapter");
            } else if (currentTab == tabForum) {
                AdapterForum adapter = new AdapterForum(this, new ArrayList<>(ForumLoader.Asks));
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("API", "forum loaded to adapter");
            }

            listView.setSelectionFromTop(firstVisibleItem, top);
        });
    }
}
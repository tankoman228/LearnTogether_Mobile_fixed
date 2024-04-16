package com.example.learntogether_mobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.Cache.CallbackAfterLoaded;
import com.example.learntogether_mobile.API.Cache.ForumLoader;
import com.example.learntogether_mobile.API.Cache.GroupsAndUsers;
import com.example.learntogether_mobile.API.Cache.InfosLoader;
import com.example.learntogether_mobile.API.Cache.MeetingsLoader;
import com.example.learntogether_mobile.API.Cache.NewsLoader;
import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.Adapters.AdapterForum;
import com.example.learntogether_mobile.Activities.Adapters.AdapterInfo;
import com.example.learntogether_mobile.Activities.Adapters.AdapterMeetings;
import com.example.learntogether_mobile.Activities.Adapters.AdapterNews;
import com.example.learntogether_mobile.Activities.Adapters.AdapterUsersGroups;
import com.example.learntogether_mobile.Activities.InsertRequests.ActAddInfo;
import com.example.learntogether_mobile.Activities.InsertRequests.ActAddMeeting;
import com.example.learntogether_mobile.Activities.InsertRequests.ActAddNews;
import com.example.learntogether_mobile.Activities.InsertRequests.ActForumAskAdd;
import com.example.learntogether_mobile.Activities.InsertRequests.ActJoinGroup;
import com.example.learntogether_mobile.Activities.InsertRequests.ActRegisterTokens;
import com.example.learntogether_mobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        btnPeople.setOnClickListener(l -> loadTab(tabPeople));
        btnForum.setOnClickListener(l -> loadTab(tabForum));
        btnMyProfile.setOnClickListener(l -> startActivity(new Intent(this, EditMyProfile.class)));

        fb.setOnClickListener(l -> {
            switch (currentTab) {
                case tabNews:
                    startActivity(new Intent(this, ActAddNews.class));
                    break;
                case tabInfo:
                    startActivity(new Intent(this, ActAddInfo.class));
                    break;
                case tabMeetings:
                    startActivity(new Intent(this, ActAddMeeting.class));
                    break;
                case tabForum:
                    startActivity(new Intent(this, ActForumAskAdd.class));
                    break;
                case tabPeople:
                    if (AdapterUsersGroups.GroupList) {
                        startActivity(new Intent(News.this, ActJoinGroup.class));
                    } else {
                        if (!Variables.IsAllowed("create_tokens")) {
                            Toast.makeText(News.this, "not allowed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(News.this, ActRegisterTokens.class));
                    }
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
        GroupsAndUsers.UpdateCacheGroups(this);

        Log.d("API", "1");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTab(currentTab);
    }

    protected void loadTab(int tabId) {

        btnNews.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
        btnInfo.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
        btnMeetings.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
        btnForum.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
        btnPeople.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));

        currentTab = tabId;
        Log.d("API", "2");
        switch (currentTab) {
            case tabNews -> {
                Log.d("API", "3");
                NewsLoader.loadFromRetrofit(this, this, etSearch.getText().toString());
                btnNews.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black2)));
            }
            case tabInfo -> {
                InfosLoader.Reload(this, etSearch.getText().toString());
                btnInfo.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black2)));
            }
            case tabMeetings -> {
                MeetingsLoader.Reload(this, etSearch.getText().toString());
                btnMeetings.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black2)));
            }
            case tabForum -> {
                ForumLoader.Reload(this, etSearch.getText().toString());
                btnForum.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black2)));
            }
            case tabPeople -> {
                btnPeople.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black2)));
                if (AdapterUsersGroups.GroupList) {
                    updateInterface();
                    GroupsAndUsers.UpdateCacheGroups(this);
                } else
                    GroupsAndUsers.UpdateCacheUsersForCurrentGroup(this);
            }
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
            }
            else if (currentTab == tabInfo) {
                AdapterInfo adapter = new AdapterInfo(this, new ArrayList<>(InfosLoader.Infos));
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else if (currentTab == tabMeetings) {
                AdapterMeetings adapter = new AdapterMeetings(this, new ArrayList<>(MeetingsLoader.Meetings));
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("API", "MEETINGS loaded to adapter");
            }
            else if (currentTab == tabForum) {
                AdapterForum adapter = new AdapterForum(this, new ArrayList<>(ForumLoader.Asks));
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("API", "forum loaded to adapter");
            }
            else if (currentTab == tabPeople) {

                List<ListU> listUS;
                if (AdapterUsersGroups.GroupList) {
                    listUS = GroupsAndUsers.Groups;
                }
                else {
                    listUS = GroupsAndUsers.UsersListForCurrentGroup;
                }

                AdapterUsersGroups adapterUsersGroups = new AdapterUsersGroups(listUS, this, new AdapterUsersGroups.AdapterUsersGroupsChoiceCallback() {
                    @Override
                    public void callback(boolean GroupList) {
                        AdapterUsersGroups.GroupList = GroupList;
                        loadTab(currentTab);
                        if (GroupList) {
                            Variables.requireMyAccountInfo(News.this);
                        }
                    }

                    @Override
                    public void selectGroupIconRequired() {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 150150401);
                    }
                });
                listView.setAdapter(adapterUsersGroups);
                adapterUsersGroups.notifyDataSetChanged();
            }

            listView.setSelectionFromTop(firstVisibleItem, top);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 150150401 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);

                AdapterUsersGroups.currentIcon = BitmapFactory.decodeStream(inputStream);
                RequestU requestU = new RequestU();
                requestU.setSession_token(Variables.SessionToken);
                requestU.setGroup(Variables.current_id_group);
                requestU.setNewIcon(ImageConverter.encodeImage(AdapterUsersGroups.currentIcon));
                new RetrofitRequest().apiService.edit_group(requestU).enqueue(new Callback<ResponseU>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                        runOnUiThread(() -> {
                            loadTab(currentTab);
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {

                    }
                });

                updateInterface();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
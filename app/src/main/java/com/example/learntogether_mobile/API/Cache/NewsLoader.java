package com.example.learntogether_mobile.API.Cache;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.News;

import java.util.ArrayList;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsLoader {

    public static ArrayList<ListU> news_list = new ArrayList<>()/*, tasks_list  = new ArrayList<>(), votes_list = new ArrayList<>()*/;

    public static void loadFromRetrofit(CallbackAfterLoaded callback, Context context, String serachString) {

        RequestU req = new RequestU();
        req.setSearch_string(serachString);
        req.setGroup(Variables.current_id_group);
        req.setNumber(20);
        req.setSession_token(Variables.SessionToken);

        RetrofitRequest r = new RetrofitRequest();
        Call<ResponseU> call = r.apiService.get_news(req);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                if (response.body() == null) {
                    return;
                }

                if (response.body().Error != null) {
                    Toast.makeText(context, response.body().Error, Toast.LENGTH_SHORT).show();
                    return;
                }
                news_list = new ArrayList<>();
                //tasks_list = new ArrayList<>();
                //votes_list = new ArrayList<>();

                for (ListU news : response.body().getNews()) {
                    news_list.add(news.initByType("n"));
                }
                //news_list.sort(Comparator.comparingInt(ListU::getID_InfoBase));

                for (ListU task : response.body().getTasks()) {
                    //tasks_list.add(task);
                    news_list.add(task.initByType("t"));
                }
                //tasks_list.sort(Comparator.comparingInt(ListU::getID_InfoBase));

                for (ListU vote : response.body().getVotes()) {
                   // votes_list.add(vote);
                    news_list.add(vote.initByType("v"));
                }
                //votes_list.sort(Comparator.comparingInt(ListU::getID_InfoBase));

                news_list.sort(Comparator.comparingInt(ListU::getID_InfoBase).reversed());
                callback.updateInterface();

                Log.d("API", "news loaded: " + news_list.size());
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "news error");
            }
        });

    }

    public static void loadFromRetrofitMore(CallbackAfterLoaded callback, Context context, String serachString) {

        RequestU req = new RequestU();
        req.setSearch_string(serachString);
        req.setGroup(Variables.current_id_group);
        req.setNumber(20);
        req.setSession_token(Variables.SessionToken);
        req.setId_max(findMinId() - 1);

        RetrofitRequest r = new RetrofitRequest();
        Call<ResponseU> call = r.apiService.get_news(req);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                if (response.body() == null) {
                    return;
                }

                if (response.body().Error != null) {
                    Toast.makeText(context, response.body().Error, Toast.LENGTH_SHORT).show();
                    return;
                }

                int previous_n_list_size = news_list.size();
                for (ListU news : response.body().getNews()) {
                    news_list.add(news.initByType("n"));
                }
                for (ListU task : response.body().getTasks()) {
                    news_list.add(task.initByType("t"));
                }

                for (ListU vote : response.body().getVotes()) {
                    news_list.add(vote.initByType("v"));
                }

                news_list.sort(Comparator.comparingInt(ListU::getID_InfoBase).reversed());

                if (news_list.size() == previous_n_list_size) {
                    return;
                }
                callback.updateInterface();

                Log.d("API", "news loaded: " + news_list.size());
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "news error");
            }
        });

    }


    private static int findMinId() {
        int minId = Integer.MAX_VALUE;
        for (ListU ask : news_list) {
            if (ask.getID_InfoBase() < minId) {
                minId = ask.getID_InfoBase();
            }
        }
        return minId;
    }
}

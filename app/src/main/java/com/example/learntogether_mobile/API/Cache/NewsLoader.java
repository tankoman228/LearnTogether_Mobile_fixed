package com.example.learntogether_mobile.API.Cache;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsLoader {

    public static ArrayList<ListU> news_list = new ArrayList<>()/*, tasks_list  = new ArrayList<>(), votes_list = new ArrayList<>()*/;

    public static void loadFromRetrofit(CallbackAfterLoaded callback, Context context, String serachString, int max_id ) {
        if (max_id == 0)
            max_id = 999999999;

        RequestU req = new RequestU();
        req.setSearch_string(serachString);
        req.setGroup(Variables.current_id_group);
        req.setNumber(4);
        req.setSession_token(Variables.SessionToken);

        RetrofitRequest r = new RetrofitRequest();
        Call<ResponseU> call = r.apiService.get_news(req);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                if (response.body().Error != null) {
                    Toast.makeText(context, response.body().Error, Toast.LENGTH_SHORT);
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

                news_list.sort(Comparator.comparingInt(ListU::getID_InfoBase));
                callback.updateAdapter();

                Log.d("API", "news loaded: " + news_list.size());
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                Log.d("API", "news error");
            }
        });

    }

}

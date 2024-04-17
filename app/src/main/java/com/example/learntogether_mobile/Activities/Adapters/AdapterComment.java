package com.example.learntogether_mobile.Activities.Adapters;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.Loaders.GroupsAndUsersLoader;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.Activities.Dialogs.DialogAttachment;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.ActWatchProfile;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Адаптер для ленты с комментариями
 */
public class AdapterComment extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;

    private ArrayList<ListU> objects;


    public AdapterComment(Context context, ArrayList<ListU> objList) {
        ctx = context;
        objects = objList;

        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Стандартная тема для адаптера
        View view = lInflater.inflate(R.layout.item_comment, parent, false);


        //Задание значений комментариям, интерфейсу тобишь
        ListU thisComment = getComment(position);


            ImageView iv = view.findViewById(R.id.ivAvatar);
            iv.setClickable(true);
            iv.setImageBitmap(ImageConverter.decodeImage(thisComment.getAvatar()));
            iv.setOnClickListener(l -> {
                Log.d("API", "avatar clicked");
                if (GroupsAndUsersLoader.UsersListForCurrentGroup == null || GroupsAndUsersLoader.UsersListForCurrentGroup.size() == 0) {
                    Log.d("API", "avatar clicked, reloading users");
                    GroupsAndUsersLoader.UpdateCacheUsersForCurrentGroup(() -> {
                        for (var a : GroupsAndUsersLoader.UsersListForCurrentGroup) {
                            if (a.getID_Account() == thisComment.getID_Account()) {
                                ActWatchProfile.Profile = a;
                                ctx.startActivity(new Intent(ctx, ActWatchProfile.class));
                                break;
                            }
                        }
                    });
                } else {
                    Log.d("API", "avatar clicked, loading from cache");
                    for (var a : GroupsAndUsersLoader.UsersListForCurrentGroup) {
                        if (a.getID_Account() == thisComment.getID_Account()) {
                            ActWatchProfile.Profile = a;
                            ctx.startActivity(new Intent(ctx, ActWatchProfile.class));
                            break;
                        }
                    }
                }
            });



        ((TextView) view.findViewById(R.id.tvAuthor)).setText(thisComment.getAuthor());
        ((TextView) view.findViewById(R.id.tvText)).setText(thisComment.getText());
        ((TextView) view.findViewById(R.id.tvDateTime)).setText(thisComment.getDateTime());

        Button btnAttachment = view.findViewById(R.id.btnAttachment);
        if (thisComment.getAttachment() == null || thisComment.getAttachment().equals("")) {
            btnAttachment.setVisibility(View.GONE);
        }
        else {
            //Просмотр вложений
            btnAttachment.setOnClickListener(l -> {
                DialogAttachment.AttachmentJson = thisComment.getAttachment();
                DialogAttachment.WatchOnly = true;
                DialogAttachment d = new DialogAttachment();
                d.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "custom");
            });
        }

        //Удаление из базы
        view.findViewById(R.id.ibDelete).setOnClickListener(l -> {

            RequestU req = new RequestU();
            req.setSession_token(GlobalVariables.SessionToken);
            req.setId_comment(thisComment.getID_Comment());

            RetrofitRequest r = new RetrofitRequest();
            r.apiService.delete_comment(req).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    if (response.body() == null) {
                        Toast.makeText(ctx, R.string.request_error, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.body().Error != null) {
                        Toast.makeText(ctx, ctx.getString(R.string.error) + response.body().Error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ctx, R.string.deleted, Toast.LENGTH_SHORT).show();
                    ((AppCompatActivity)ctx).runOnUiThread(() -> view.setVisibility(View.GONE));
                }
                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                }
            });
        });

        return view;
    }

    ListU getComment(int position) {
        return ((ListU) getItem(position));
    }
}
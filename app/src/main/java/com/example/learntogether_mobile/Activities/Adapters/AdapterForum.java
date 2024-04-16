package com.example.learntogether_mobile.Activities.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.Comments;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Адаптер для ленты с вопросами с форума
 */
public class AdapterForum extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;

    private ArrayList<ListU> objects;


    public AdapterForum(Context context, ArrayList<ListU> objList) {
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

        View view = lInflater.inflate(R.layout.item_forum, parent, false);

        ListU thisForumAsk = getForumAsk(position);
        //Задание интерфейсу нужных строк
        ((TextView) view.findViewById(R.id.tvAuthor)).setText(thisForumAsk.getAuthorTitle());
        ((TextView) view.findViewById(R.id.tvTitle)).setText(thisForumAsk.getTitle());
        ((TextView) view.findViewById(R.id.tvText)).setText(thisForumAsk.getText());
        ((TextView) view.findViewById(R.id.tvDateTime)).setText(thisForumAsk.getWhenAdd());
        ((TextView) view.findViewById(R.id.tvCommentsNum)).setText(String.valueOf(thisForumAsk.getCommentsFound()));

        //Удаление из базы
        view.findViewById(R.id.ibDelete).setOnClickListener(l -> {
            RequestU req = new RequestU();
            req.setID_InfoBase(thisForumAsk.getID_InfoBase());
            req.setSession_token(Variables.SessionToken);

            RetrofitRequest r = new RetrofitRequest();
            r.apiService.delete_ib(req).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    if (response.body().Error != null) {
                        Toast.makeText(ctx, ctx.getString(R.string.error) + response.body().Error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ctx, ctx.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    ((AppCompatActivity)ctx).runOnUiThread(() -> view.setVisibility(View.GONE));
                }
                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                    Toast.makeText(ctx, ctx.getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });
        });

        //Пометка как решённого
        if (!thisForumAsk.getSolved()) {

            view.findViewById(R.id.btnMarkSolved).setOnClickListener(l -> {
                RequestU req = new RequestU();
                req.setID_InfoBase(thisForumAsk.getID_InfoBase());
                req.setSession_token(Variables.SessionToken);

                RetrofitRequest r = new RetrofitRequest();
                r.apiService.mark_solved(req).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                        if (response.body().Error != null) {
                            Toast.makeText(ctx, ctx.getString(R.string.error) + response.body().Error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(ctx, R.string.marked_as_solved, Toast.LENGTH_SHORT).show();
                        ((AppCompatActivity)ctx).runOnUiThread(() -> view.findViewById(R.id.btnMarkSolved).setVisibility(View.GONE));
                    }

                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {
                        Toast.makeText(ctx, ctx.getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                });
            });
            view.findViewById(R.id.tvSolved).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.btnMarkSolved).setVisibility(View.GONE);
        }

        //Переход в комментарии
        view.findViewById(R.id.ivComments).setOnClickListener(l -> {
            ((AppCompatActivity)ctx).runOnUiThread(() -> {
                Comments.ID_InfoBase = thisForumAsk.getID_InfoBase();
                ctx.startActivity(new Intent(ctx, Comments.class));
            });
        });

        return view;
    }

    ListU getForumAsk(int position) {
        return ((ListU) getItem(position));
    }
}
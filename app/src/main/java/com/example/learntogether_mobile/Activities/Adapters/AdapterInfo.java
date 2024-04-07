package com.example.learntogether_mobile.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learntogether_mobile.API.FileEncoderDecoder;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.Comments;
import com.example.learntogether_mobile.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterInfo  extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;

    private ArrayList<ListU> objects;


    public AdapterInfo(Context context, ArrayList<ListU> objList) {
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

        View view = lInflater.inflate(R.layout.item_info, parent, false);

        ListU thisInfo = getForumAsk(position);

        ((TextView) view.findViewById(R.id.tvUsername)).setText(thisInfo.getAuthorTitle());
        ((TextView) view.findViewById(R.id.tvTitle)).setText(thisInfo.getTitle());
        ((TextView) view.findViewById(R.id.tvTextDescription)).setText(thisInfo.getText());
        ((TextView) view.findViewById(R.id.tvWhen)).setText(thisInfo.getWhenAdd());
        ((TextView) view.findViewById(R.id.tvCommentsNum)).setText(String.valueOf(thisInfo.getCommentsFound()));

        view.findViewById(R.id.ibDelete).setOnClickListener(l -> {
            RequestU req = new RequestU();
            req.setID_InfoBase(thisInfo.getID_InfoBase());
            req.setSession_token(Variables.SessionToken);

            RetrofitRequest r = new RetrofitRequest();
            r.apiService.delete_ib(req).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    if (response.body().Error != null) {
                        Toast.makeText(ctx, "Error: " + response.body().Error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ctx, "Deleted", Toast.LENGTH_SHORT).show();
                    ((AppCompatActivity)ctx).runOnUiThread(() -> view.setVisibility(View.GONE));
                }
                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        view.findViewById(R.id.ibComments).setOnClickListener(l -> {
            ((AppCompatActivity)ctx).runOnUiThread(() -> {
                Comments.ID_InfoBase = thisInfo.getID_InfoBase();
                ctx.startActivity(new Intent(ctx, Comments.class));
            });
        });

        if (FileEncoderDecoder.checkFilesDownloaded(thisInfo.getID_InfoBase(), ctx)) {
            view.findViewById(R.id.btnDownload).setVisibility(View.GONE);
        }
        else {
            view.findViewById(R.id.btnDownload).setOnClickListener(l -> {
                thisInfo.getID_InfoBase(); //Идентификация данных (набора файлов)
                //код для загрузки

                RequestU requestU = new RequestU();
                requestU.setSession_token(Variables.SessionToken);

            });
        }

        return view;
    }

    ListU getForumAsk(int position) {
        return ((ListU) getItem(position));
    }
}
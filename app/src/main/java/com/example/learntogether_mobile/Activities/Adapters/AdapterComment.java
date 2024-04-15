package com.example.learntogether_mobile.Activities.Adapters;

import java.util.ArrayList;
import android.content.Context;
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
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.Dialogs.DialogAttachment;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        View view = lInflater.inflate(R.layout.item_comment, parent, false);

        ListU thisComment = getComment(position);
        if (thisComment.getAvatar() != null) {
            ((ImageView) view.findViewById(R.id.ivAvatar)).setImageBitmap(ImageConverter.decodeImage(thisComment.getAvatar()));
        }

        ((TextView) view.findViewById(R.id.tvAuthor)).setText(thisComment.getAuthor());
        ((TextView) view.findViewById(R.id.tvText)).setText(thisComment.getText());
        ((TextView) view.findViewById(R.id.tvDateTime)).setText(thisComment.getDateTime());

        Button btnAttachment = view.findViewById(R.id.btnAttachment);
        if (thisComment.getAttachment() == null || thisComment.getAttachment().equals("")) {
            btnAttachment.setVisibility(View.GONE);
        }
        else {
            btnAttachment.setOnClickListener(l -> {
                DialogAttachment.AttachmentJson = thisComment.getAttachment();
                DialogAttachment.WatchOnly = true;
                DialogAttachment d = new DialogAttachment();
                d.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "custom");
            });
        }

        view.findViewById(R.id.ibDelete).setOnClickListener(l -> {

            RequestU req = new RequestU();
            req.setSession_token(Variables.SessionToken);
            req.setId_comment(thisComment.getID_Comment());

            RetrofitRequest r = new RetrofitRequest();
            r.apiService.delete_comment(req).enqueue(new Callback<>() {
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

        return view;
    }

    ListU getComment(int position) {
        return ((ListU) getItem(position));
    }
}
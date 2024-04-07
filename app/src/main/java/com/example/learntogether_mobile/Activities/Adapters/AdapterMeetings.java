package com.example.learntogether_mobile.Activities.Adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.Comments;
import com.example.learntogether_mobile.Activities.MeetingInfo;
import com.example.learntogether_mobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMeetings extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;

    private ArrayList<ListU> objects;


    public AdapterMeetings(Context context, ArrayList<ListU> objList) {
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


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = lInflater.inflate(R.layout.item_meetings, parent, false);
        ListU thisMeeting = getMeeting(position);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvWhenAndWhere = view.findViewById(R.id.tvWhenAndWhere);

        tvTitle.setText(thisMeeting.getTitle());
        tvDescription.setText(thisMeeting.getText());
        tvUsername.setText(tvUsername.getText().toString().replace("[NAME]", thisMeeting.getAuthorTitle()));
        tvWhenAndWhere.setText("At " + thisMeeting.getStartsAt() + " in " + thisMeeting.getPlace());

        view.findViewById(R.id.btnGo).setOnClickListener(l -> {
            MeetingInfo.meeting = thisMeeting;
            ctx.startActivity(new Intent(ctx, MeetingInfo.class));
        });
        ((TextView)view.findViewById(R.id.tvCommentsNum)).setText(thisMeeting.getCommentsFound());
        view.findViewById(R.id.ibComments).setOnClickListener(l -> {
            Comments.ID_InfoBase = thisMeeting.getID_InfoBase();
            ctx.startActivity(new Intent(ctx, Comments.class));
        });
        view.findViewById(R.id.ibDelete).setOnClickListener(l -> {
            RequestU req = new RequestU();
            req.setID_InfoBase(thisMeeting.getID_InfoBase());
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

        return view;
    }

    ListU getMeeting(int position) {
        return ((ListU) getItem(position));
    }
}
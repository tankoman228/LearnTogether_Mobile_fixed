package com.example.learntogether_mobile.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.MeetingInfo;
import com.example.learntogether_mobile.R;

import java.util.List;

/**
 * Адаптер для построения диаграммы Ганта при отображении назначенного времени (встречи)
 */
public class GanttAdapter extends RecyclerView.Adapter<GanttAdapter.GanttViewHolder> {

    private List<ListU> responsesList;
    public static boolean ShowUsername = true;

    public GanttAdapter(List<ListU> responsesList) {
        this.responsesList = responsesList;
    }

    @NonNull
    @Override
    public GanttViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gantt, parent, false);
        return new GanttViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GanttViewHolder holder, int position) {

        ListU response = responsesList.get(position);
        if (response.getSurety() <= 1) {
            holder.tvUsername.setVisibility(View.VISIBLE);
            holder.pbLeft.setVisibility(View.GONE);
            holder.pbRight.setVisibility(View.GONE);
            holder.pbBg.setVisibility(View.GONE);
            holder.tvUsername.setText(response.getAccount());
            return;
        }


        // Set data to views
        if (ShowUsername) {
            holder.tvUsername.setText(new StringBuilder().
                    append(MeetingInfo.getTimeStringFromMinutes(response.getStart())).
                    append("-").
                    append(MeetingInfo.getTimeStringFromMinutes(response.getEnd())).
                    append("\t").
                    append(response.getAccount()).
                    append(" ").
                    append((int)response.getSurety()).
                    append("% ")
                    .toString());
        }
        else {
            holder.tvUsername.setVisibility(View.GONE);
        }

        int startTime = response.getStart();
        int endTime = response.getEnd();

        int leftProgress = startTime;
        int rightProgress = 1440 - endTime;

        holder.pbLeft.setProgress(leftProgress);
        holder.pbRight.setProgress(rightProgress);
    }

    @Override
    public int getItemCount() {
        return responsesList.size();
    }

    public static class GanttViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        ProgressBar pbLeft;
        ProgressBar pbRight;
        ProgressBar pbBg;

        public GanttViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            pbLeft = itemView.findViewById(R.id.pbLeft);
            pbRight = itemView.findViewById(R.id.pbRight);
            pbBg = itemView.findViewById(R.id.pbBg);
        }
    }
}

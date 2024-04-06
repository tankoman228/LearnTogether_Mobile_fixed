package com.example.learntogether_mobile.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.R;

import java.util.List;

public class GanttAdapter extends RecyclerView.Adapter<GanttAdapter.GanttViewHolder> {

    private List<ListU> responsesList;
    public static boolean ShowUsername = false;

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

        // Set data to views
        if (ShowUsername) {
            holder.tvUsername.setText(response.getSurety() + "% " + response.getAccount());
        }
        else {
            holder.tvUsername.setText(new StringBuilder().
                    append(response.getSurety()).
                    append("% ").
                    append(response.getStart() / 60).
                    append(":").
                    append(response.getStart() % 60).
                    append("-").
                    append(response.getEnd() / 60).
                    append(":").
                    append(response.getEnd() % 60).toString());
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

        public GanttViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            pbLeft = itemView.findViewById(R.id.pbLeft);
            pbRight = itemView.findViewById(R.id.pbRight);
        }
    }
}

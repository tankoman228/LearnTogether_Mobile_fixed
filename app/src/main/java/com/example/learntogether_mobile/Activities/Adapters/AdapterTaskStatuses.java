package com.example.learntogether_mobile.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.R;

import java.util.List;

public class AdapterTaskStatuses extends BaseAdapter {

    List<ListU> items;
    Context context;
    LayoutInflater lInflater;
    public boolean hideUsernamesAndShowTasksInfo;
    Callback callback;

    public interface Callback {

        void callback_task_clicked(ListU item);
    }


    public AdapterTaskStatuses(Context context, List<ListU> items, boolean hideUsernamesAndShowTasksInfo, Callback callback) {
        this.items = items;
        this.context = context;
        this.hideUsernamesAndShowTasksInfo = hideUsernamesAndShowTasksInfo;
        this.callback = callback;

        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        view = lInflater.inflate(R.layout.item_task_status, parent, false);

        TextView
                tvUsername = view.findViewById(R.id.tvUsername)
                , tvTaskTitle = view.findViewById(R.id.tvTaskTitle)
                , tvDescr = view.findViewById(R.id.tvTaskDescription),
                tvNeedHelp = view.findViewById(R.id.tvNeedHelp);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ListU status = (ListU) getItem(position);


        if (hideUsernamesAndShowTasksInfo) {
            tvUsername.setVisibility(View.GONE);
            tvDescr.setText(status.getText());
            tvTaskTitle.setText(status.getTaskTitle());
        }
        else {
            tvUsername.setText(status.getAccountTitle());
            tvDescr.setVisibility(View.GONE);
            tvTaskTitle.setVisibility(View.GONE);
        }

        if (!status.getNeedHelp())
            tvNeedHelp.setVisibility(View.GONE);

        if (status.getFinished()) {
            progressBar.setVisibility(View.GONE);
            tvNeedHelp.setVisibility(View.VISIBLE);
            tvNeedHelp.setText("Finished!");
        }
        else
            progressBar.setProgress((int)status.getPriority());

        ((TextView)view.findViewById(R.id.tvDeadline)).setText("Deadline: " + status.getDeadline());

        view.setClickable(true);
        view.setOnClickListener(l -> {
            callback.callback_task_clicked(status);
        });

        return view;
    }
}

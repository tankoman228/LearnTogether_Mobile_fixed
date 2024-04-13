package com.example.learntogether_mobile.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.R;

import java.util.List;

public class AdapterUsersGroups extends BaseAdapter {

    public interface AdapterUsersGroupsChoiceCallback {
        void callback(boolean GroupList);
    }


    private List<ListU> items;
    private boolean GroupList = false;
    Context ctx;
    LayoutInflater lInflater;


    public AdapterUsersGroups(List<ListU> items, boolean GroupList, Context context) {
        this.items = items;
        this.GroupList = GroupList;
        ctx = context;

        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        }
        return items.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            View view = lInflater.inflate(R.layout.item_additional_to_main_layout, parent, false);

            return view;
        }

        ListU item = (ListU)getItem(position);
        View view = lInflater.inflate(R.layout.item_group, parent, false);
        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvText = view.findViewById(R.id.tvSomeText);
        ImageButton imageButtonGo = view.findViewById(R.id.imageButton2);

        if (GroupList) {
            tvName.setText(item.getName());
            tvText.setVisibility(View.GONE);
        }
        else {
            tvName.setText(item.getTitle());
            tvText.setText(item.getUsername());
        }
        return view;
    }
}

package com.example.learntogether_mobile.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learntogether_mobile.API.Loaders.GroupsAndUsersLoader;
import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.AdminActivity.ActAdminPanel;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.ActWatchProfile;
import com.example.learntogether_mobile.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Адаптер для отображения списка групп или пользователей (зависит от выбранного GroupList)
 */
public class AdapterUsersGroups extends BaseAdapter {

    /**
     * Вызов при изменении иконки группы и при выборе другого списка (переключение между 2-мя списками)
     */
    public interface AdapterUsersGroupsChoiceCallback {
        void callback(boolean GroupList);
        void selectGroupIconRequired();
    }


    private List<ListU> items;
    public static boolean GroupList = false;
    Context ctx;
    LayoutInflater lInflater;
    AdapterUsersGroupsChoiceCallback callback;
    public static Bitmap currentIcon;


    public AdapterUsersGroups(List<ListU> items,Context context, AdapterUsersGroupsChoiceCallback callback) {
        this.items = items;
        ctx = context;
        this.callback = callback;

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
            // Верхний элемент с опцией переключения и информацией о текущей группе
            View view = lInflater.inflate(R.layout.item_additional_to_main_layout, parent, false);

            Button btnGroups = view.findViewById(R.id.btnAllGroups);
            Button btnUsers = view.findViewById(R.id.btnCurrentGroup);
            ImageButton ibCurrentGroup = view.findViewById(R.id.ibGroupAvatar);
            TextView tvName = view.findViewById(R.id.tvGroupName);
            EditText etDescr = view.findViewById(R.id.etGroupDescription);

            for (ListU group: GroupsAndUsersLoader.Groups) {
                if (group.getID_Group() == Variables.current_id_group) {
                    if (group.getIcon() != null)
                        currentIcon = ImageConverter.decodeImage(group.getIcon());
                    ibCurrentGroup.setImageBitmap(currentIcon);
                    tvName.setText(group.getName());
                    etDescr.setText(group.getDescription());
                    break;
                }
            }

            if (GroupList) {
                btnUsers.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.gray)));
                btnUsers.setTextColor(view.getResources().getColor(R.color.white));

                btnUsers.setOnClickListener(l -> {
                    callback.callback(false);
                    btnUsers.setClickable(false);
                });

            }
            else {
                btnGroups.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.gray)));
                btnGroups.setTextColor(view.getResources().getColor(R.color.white));

                btnGroups.setOnClickListener(l -> {
                    callback.callback(true);
                    btnGroups.setClickable(false);
                });

            }

            if (Variables.IsAllowed("edit_group")) {
                ibCurrentGroup.setOnClickListener(l -> {
                    callback.selectGroupIconRequired();
                });
            } else {
                etDescr.setEnabled(false);
            }
            etDescr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    RequestU requestU = new RequestU();
                    requestU.setSession_token(Variables.SessionToken);
                    requestU.setNewDescription(etDescr.getText().toString());
                    requestU.setGroup(Variables.current_id_group);
                    new RetrofitRequest().apiService.edit_group(requestU).enqueue(new Callback<ResponseU>() {
                        @Override
                        public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseU> call, Throwable t) {

                        }
                    });
                }
            });


            if (Variables.IsAllowed("edit_group") || Variables.IsAllowed("edit_roles")) {
                Button adp = view.findViewById(R.id.btnAdminPanel);
                adp.setVisibility(View.VISIBLE);
                adp.setOnClickListener(l -> {
                    ctx.startActivity(new Intent(ctx, ActAdminPanel.class));
                });
            }

            return view;
        }

        //Группа, либо человек
        View view;
        if (convertView == null) {
            view = lInflater.inflate(R.layout.item_group, parent, false);
        } else {
            view = convertView;
        }

        ListU item = (ListU)getItem(position);
        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvText = view.findViewById(R.id.tvSomeText);

        if (GroupList) {
            tvName.setText(item.getName());
            tvText.setVisibility(View.GONE);
            view.setOnClickListener(l -> {
                Variables.current_id_group = item.getID_Group();
                callback.callback(true);
            });
            if (item.getID_Group() == Variables.current_id_group) {
                view.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.black)));
            }
        }
        else {
            tvName.setText(item.getTitle());
            tvText.setText(item.getUsername());
            view.setOnClickListener(l -> {
                ActWatchProfile.Profile = item;
                ctx.startActivity(new Intent(ctx, ActWatchProfile.class));
            });
        }
        if (item.getIcon() != null) {
            ivIcon.setImageBitmap(ImageConverter.decodeImage(item.getIcon()));
        }

        return view;
    }
}

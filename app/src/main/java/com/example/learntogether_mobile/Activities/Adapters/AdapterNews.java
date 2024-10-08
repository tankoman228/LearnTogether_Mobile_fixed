package com.example.learntogether_mobile.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.Loaders.GroupsAndUsersLoader;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.ActComments;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.ActFullScreenImageActivity;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.ActTaskStatus;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.ActWatchProfile;
import com.example.learntogether_mobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Адаптер для денты новостей: новости, задачи и голосования
 */
public class AdapterNews extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    final ExecutorService executor = Executors.newFixedThreadPool(1);

    private ArrayList<ListU> objects;


    public AdapterNews(Context context, ArrayList<ListU> objList) {
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

        ListU item = (ListU) getItem(position);
        View view;

        //Проверка на тип записи для определения макета
        if (item.type_.equals("n")) {

            view = lInflater.inflate(R.layout.item_news, parent, false);
            // Новость
            TextView tvUsername = view.findViewById(R.id.tvUsername);
            TextView tvWhen = view.findViewById(R.id.tvWhen);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvDescr = view.findViewById(R.id.tvTextDescription);
            TextView tvCommentsNum = view.findViewById(R.id.tvCommentsNum);
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            ImageButton ibNext = view.findViewById(R.id.ibNext);
            ImageButton ibPrevious = view.findViewById(R.id.ibPrevious);
            ImageView ivImage = view.findViewById(R.id.ivImage);
            TextView tvPhotosNum = view.findViewById(R.id.tvNum);

            tvUsername.setText(item.getAuthorTitle());
            tvWhen.setText(item.getWhenAdd());
            tvTitle.setText(item.getTitle());
            tvDescr.setText(item.getText());
            tvCommentsNum.setText(String.valueOf(item.getCommentsFound()));
            progressBar.setProgress((int) (item.Rate * 20f));

            final List<Bitmap>[] bitmaps = new List[]{null};

            AtomicInteger i = new AtomicInteger(0);
            ibNext.setOnClickListener(l -> {
                i.getAndIncrement();
                if (i.get() >= bitmaps[0].size())
                    i.set(0);
                ivImage.setImageBitmap(bitmaps[0].get(i.get()));
                tvPhotosNum.setText(new StringBuilder().append(i.get() + 1).append("/").append(bitmaps[0].size()).toString());
            });

            ibPrevious.setOnClickListener(l -> {
                i.getAndDecrement();
                if (i.get() < 0)
                    i.set(bitmaps[0].size() - 1);
                ivImage.setImageBitmap(bitmaps[0].get(i.get()));
                tvPhotosNum.setText(new StringBuilder().append(i.get() + 1).append("/").append(bitmaps[0].size()).toString());
            });

            ivImage.setOnClickListener(l -> {
                ActFullScreenImageActivity.bitmap = bitmaps[0].get(i.get());
                ctx.startActivity(new Intent(ctx, ActFullScreenImageActivity.class));
            });

            ivImage.setVisibility(View.GONE);
            ibNext.setVisibility(View.GONE);
            ibPrevious.setVisibility(View.GONE);
            tvPhotosNum.setVisibility(View.GONE);

            executor.execute(() -> {
                try {
                    bitmaps[0] = ImageConverter.decodeImages(item.getImages());
                    ((AppCompatActivity) ctx).runOnUiThread(() -> {
                        if (bitmaps[0].size() != 0) {
                            ivImage.setVisibility(View.VISIBLE);
                            ibNext.setVisibility(View.VISIBLE);
                            ibPrevious.setVisibility(View.VISIBLE);
                            tvPhotosNum.setVisibility(View.VISIBLE);

                            ivImage.setImageBitmap(bitmaps[0].get(0));
                            tvPhotosNum.setText(new StringBuilder().append("1/").append(bitmaps[0].size()).toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else if (item.type_.equals("t")) {

            view = lInflater.inflate(R.layout.item_task, parent, false);
            // Задача
            TextView tvUsername = view.findViewById(R.id.tvUsername);
            TextView tvWhen = view.findViewById(R.id.tvWhen);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvTextDescription = view.findViewById(R.id.tvTextDescription);
            TextView tvCommentsNum = view.findViewById(R.id.tvCommentsNum);
            TextView tvDeadline = view.findViewById(R.id.tvDeadline);

            ProgressBar progressBar = view.findViewById(R.id.progressBar);

            view.findViewById(R.id.btnMyStatus).setOnClickListener(l -> {
                ActTaskStatus.ShowMy = true;
                ActTaskStatus.Task = item;
                ctx.startActivity(new Intent(ctx, ActTaskStatus.class));
            });

            tvUsername.setText(item.getAuthorTitle());
            tvWhen.setText(item.getWhenAdd());
            tvTitle.setText(item.getTitle());
            tvTextDescription.setText(item.getText());
            tvCommentsNum.setText(String.valueOf(item.getCommentsFound()));
            progressBar.setProgress((int) (item.Rate * 20f));
            tvDeadline.setText(new StringBuilder().append(tvDeadline.getText().toString()).append(item.getDeadline()).toString().replace("T", " "));
        }
        else {// (item.type_.equals("v")) {

            view = lInflater.inflate(R.layout.item_vote, parent, false);
            // Голосование
            TextView tvUsername = view.findViewById(R.id.tvUsername);
            TextView tvWhen = view.findViewById(R.id.tvWhen);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvTextDescription = view.findViewById(R.id.tvTextDescription);
            TextView tvCommentsNum = view.findViewById(R.id.tvCommentsNum);

            Button btnSave = view.findViewById(R.id.btnSave);
            Button btnShowResults = view.findViewById(R.id.btnShowResults);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

            tvUsername.setText(item.getAuthorTitle());
            tvWhen.setText(item.getWhenAdd());
            tvTitle.setText(item.getTitle());
            tvTextDescription.setText(item.getText());
            tvCommentsNum.setText(String.valueOf(item.getCommentsFound()));

            List<VoteOption> options = new ArrayList<>();
            for (String option : item.getItems()) {
                options.add(new VoteOption(option, ""));
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            final AdapterVoteItem[] adapterVoteItem = {new AdapterVoteItem(options)};
            recyclerView.setAdapter(adapterVoteItem[0]);

            btnSave.setOnClickListener(l -> {

                RequestU requestU = new RequestU();
                requestU.setSession_token(GlobalVariables.SessionToken);
                requestU.items = new ArrayList<>();
                requestU.setId_object(item.getID_Vote());

                for (var voteOption : adapterVoteItem[0].dataList) {
                    if (voteOption.selected)
                        requestU.items.add(voteOption.text);
                }

                new RetrofitRequest().apiService.vote(requestU).enqueue(new Callback<ResponseU>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                        if (response.body() == null) {
                            Log.d("API", "vote error");
                            return;
                        }

                        for (var voteOption : adapterVoteItem[0].dataList) {
                            voteOption.selected = false;
                        }

                        Toast.makeText(ctx, Objects.requireNonNullElse(response.body().Error, ctx.getString(R.string.success)), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {

                    }
                });
            });
            btnShowResults.setOnClickListener(l -> {
                RequestU requestU = new RequestU();
                requestU.setSession_token(GlobalVariables.SessionToken);
                requestU.setId_object(item.getID_Vote());

                new RetrofitRequest().apiService.get_vote_info(requestU).enqueue(new Callback<ResponseU>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                        if (response.body() == null) {
                            Log.d("API", "vote get info error");
                            return;
                        }

                        var voteOptions = adapterVoteItem[0].dataList;

                        if (item.getAnonymous()) {
                            for (var have : voteOptions) {
                                for (var got : response.body().Results) {
                                    if (Objects.equals(got.getItem(), have.text)) {
                                        have.result = ctx.getString(R.string.voted) + got.getCount();
                                    }
                                }
                            }
                        } else {
                            for (var have : voteOptions) {
                                have.result = "";
                                for (var got : response.body().Results) {
                                    if (Objects.equals(got.getItem(), have.text)) {
                                        have.result += got.getName() + "\n";
                                    }
                                }
                            }
                        }
                        ((AppCompatActivity) ctx).runOnUiThread(() -> {
                            adapterVoteItem[0] = new AdapterVoteItem(voteOptions);
                            recyclerView.setAdapter(adapterVoteItem[0]);
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {

                    }
                });
            });
        }


        //Общие для всех элементы
        Button btnMarkModerated = view.findViewById(R.id.btnMarkModerated);
        ImageButton ibDelete = view.findViewById(R.id.imageView);

        if (!item.type_.equals("v")) {
            ImageButton ibComments = view.findViewById(R.id.ibComments);
            ibComments.setOnClickListener(l -> {
                ActComments.ID_InfoBase = item.getID_InfoBase();
                ctx.startActivity(new Intent(ctx, ActComments.class));
            });
            Button btnRates[] = {
                    view.findViewById(R.id.btn1),
                    view.findViewById(R.id.btn2),
                    view.findViewById(R.id.btn3),
                    view.findViewById(R.id.btn4),
                    view.findViewById(R.id.btn5)
            };
            for (int i = 1; i < 6; i++) {
                int finalI = i;
                btnRates[i - 1].setOnClickListener(l -> {
                    RequestU req = new RequestU();
                    req.setID_InfoBase(item.getID_InfoBase());
                    req.setSession_token(GlobalVariables.SessionToken);
                    req.Rank = finalI;
                    RetrofitRequest r = new RetrofitRequest();
                    r.apiService.rate(req).enqueue(new Callback<ResponseU>() {
                        @Override
                        public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                            Toast.makeText(ctx, R.string.thanks_for_your_rate, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseU> call, Throwable t) {
                            Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        }
        if (item.getModerated()) {
            btnMarkModerated.setVisibility(View.GONE);
        } else {
            btnMarkModerated.setOnClickListener(l -> {

                RequestU req = new RequestU();
                req.setSession_token(GlobalVariables.SessionToken);
                req.setType(item.type_);
                req.setGroup(GlobalVariables.current_id_group);
                switch (item.type_) {
                    case "n" -> req.id = item.getID_News();
                    case "t" -> req.id = item.getID_Task();
                    case "v" -> req.id = item.getID_Vote();
                }

                RetrofitRequest r = new RetrofitRequest();
                r.apiService.accept_news(req).enqueue(new Callback<ResponseU>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                        assert response.body() != null;
                        if (response.body().Error != null) {
                            Toast.makeText(ctx, ctx.getString(R.string.error) + response.body().Error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(ctx, R.string.published, Toast.LENGTH_SHORT).show();
                        ((AppCompatActivity) ctx).runOnUiThread(() -> {
                            btnMarkModerated.setVisibility(View.GONE);
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {
                    }
                });
            });
        }

        ibDelete.setOnClickListener(l -> {

            RequestU req = new RequestU();
            req.setID_InfoBase(item.getID_InfoBase());
            req.setSession_token(GlobalVariables.SessionToken);

            RetrofitRequest r = new RetrofitRequest();
            r.apiService.delete_ib(req).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    if (response.body().Error != null) {
                        Toast.makeText(ctx, "Error: " + response.body().Error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ctx, "Deleted", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        ImageButton ibAvatar = view.findViewById(R.id.ibAvatar);
        ibAvatar.setImageBitmap(ImageConverter.decodeImage(item.getAvatar()));
        ibAvatar.setOnClickListener(l -> {
            if (GroupsAndUsersLoader.UsersListForCurrentGroup == null || GroupsAndUsersLoader.UsersListForCurrentGroup.size() == 0) {
                GroupsAndUsersLoader.UpdateCacheUsersForCurrentGroup(() -> {
                    for (var a : GroupsAndUsersLoader.UsersListForCurrentGroup) {
                        if (a.getID_Account() == item.getID_Account()) {
                            ActWatchProfile.Profile = a;
                            ctx.startActivity(new Intent(ctx, ActWatchProfile.class));
                            break;
                        }
                    }
                });
            } else {
                for (var a : GroupsAndUsersLoader.UsersListForCurrentGroup) {
                    if (a.getID_Account() == item.getID_Account()) {
                        ActWatchProfile.Profile = a;
                        ctx.startActivity(new Intent(ctx, ActWatchProfile.class));
                        break;
                    }
                }
            }
        });


        return view;
    }

    /**
     * Адаптер для отображения результатов голосования
     */
    public class AdapterVoteItem extends RecyclerView.Adapter<AdapterVoteItem.ViewHolder> {

        private List<VoteOption> dataList;

        public AdapterVoteItem(List<VoteOption> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voteitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VoteOption item = dataList.get(position);

            holder.checkBox.setText(item.text);
            holder.tvResult.setText(item.result);
            if (item.result.equals("")) {
                holder.tvResult.setVisibility(View.GONE);
            }
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.selected = isChecked;
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            TextView tvResult;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.checkBox);
                tvResult = itemView.findViewById(R.id.tvResult);
            }
        }
    }

    /**
     * Для отображения элементов в голосовании, использован в адаптере AdapterVoteItem
     */
    public class VoteOption {
        public String text, result;
        public boolean selected = false;

        public VoteOption(String text, String result) {
            this.text = text;
            this.result = result;
        }
    }
}
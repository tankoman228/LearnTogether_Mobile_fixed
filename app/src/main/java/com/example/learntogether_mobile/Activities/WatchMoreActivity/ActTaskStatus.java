package com.example.learntogether_mobile.Activities.WatchMoreActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.Activities.Adapters.AdapterTaskStatuses;
import com.example.learntogether_mobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Отслеживание задач, отображения своего и чужих статусов выполнения
 * Таск-менеджер
 */
public class ActTaskStatus extends AppCompatActivity implements AdapterTaskStatuses.Callback {

    public static ListU Task = null;
    public static boolean ShowMy = true;

    Button btnUpdTaskStatus, btnShowForThisTask, btnAllYourTasks;
    TextView tvTitle, tvDescr, tvDeadline;
    CheckBox cbNeedHelp, cbFinished;
    ListView listView;
    SeekBar seekBar;

    final List<ListU> tasks = new ArrayList<>();
    AdapterTaskStatuses adapterTaskStatuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_status);

        btnUpdTaskStatus = findViewById(R.id.btnUpdateTaskStatus);
        btnAllYourTasks = findViewById(R.id.btnAllYourTasks);
        btnShowForThisTask = findViewById(R.id.btnShowForThis);
        listView = findViewById(R.id.lv);
        tvTitle = findViewById(R.id.tvHeader);
        tvDeadline = findViewById(R.id.tvDeadline);
        tvDescr = findViewById(R.id.tvDescription);
        cbFinished = findViewById(R.id.cbFinished);
        cbNeedHelp = findViewById(R.id.cbNeedHelp);
        seekBar = findViewById(R.id.sbPriority);

        callback_task_clicked(Task);
        adapterTaskStatuses = new AdapterTaskStatuses(ActTaskStatus.this, tasks, ShowMy, ActTaskStatus.this);
        listView.setAdapter(adapterTaskStatuses);

        if (Task != null)
            tvTitle.setText(Task.getTitle());

        cbFinished.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                seekBar.setVisibility(View.GONE);
                cbNeedHelp.setChecked(false);
                cbNeedHelp.setEnabled(false);
            }
            else {
                seekBar.setVisibility(View.VISIBLE);
                cbNeedHelp.setEnabled(true);
            }
        });
        loadTaskStatuses();

        btnAllYourTasks.setOnClickListener(l -> {
            ShowMy = true;
            loadTaskStatuses();
        });
        btnShowForThisTask.setOnClickListener(l -> {
            ShowMy = false;

            if (Task == null) {
                Toast.makeText(this, "No task chosen", Toast.LENGTH_SHORT).show();
                return;
            }

            loadTaskStatuses();
        });
        btnUpdTaskStatus.setOnClickListener(l -> {
            updTaskStatus("update_or_create");
        });
        findViewById(R.id.btnDeleteTaskStatus).setOnClickListener(l -> {
            updTaskStatus("delete");
        });
    }


    private void loadTaskStatuses() {
        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        requestU.setId_group(GlobalVariables.current_id_group);
        requestU.setOnly_mine(ShowMy);
        if (Task != null)
            requestU.setId_object(Task.getID_Task());

        RetrofitRequest r = new RetrofitRequest();
        r.apiService.get_tasks_statuses(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                try {
                    ActTaskStatus.this.runOnUiThread(() -> {
                        tasks.clear();
                        for (ListU task: response.body().getTasks()) {
                            tasks.add(task);
                        }
                        adapterTaskStatuses.hideUsernamesAndShowTasksInfo = ShowMy;
                        adapterTaskStatuses.notifyDataSetChanged();
                    });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updTaskStatus(String todo) {
        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        requestU.setId_group(GlobalVariables.current_id_group);
        requestU.setTodo(todo);
        requestU.setFinished(cbFinished.isChecked());
        requestU.setNeedHelp(cbNeedHelp.isChecked());
        requestU.setPriority(seekBar.getProgress());
        requestU.ID_Task = Task.getID_Task();

        RetrofitRequest r = new RetrofitRequest();
        r.apiService.update_task_status(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                loadTaskStatuses();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void callback_task_clicked(ListU item) {

        Task = item;
        try {
            if (Task != null) {
                tvTitle.setVisibility(View.VISIBLE);
                tvDeadline.setVisibility(View.VISIBLE);
                tvDescr.setVisibility(View.VISIBLE);
                cbFinished.setVisibility(View.VISIBLE);
                cbNeedHelp.setVisibility(View.VISIBLE);

                tvTitle.setText(item.getTaskTitle());
                tvDeadline.setText(item.getDeadline());
                tvDescr.setText(item.getText());
                cbNeedHelp.setChecked(item.getNeedHelp());
                cbFinished.setChecked(item.getFinished());
            } else {
                tvTitle.setVisibility(View.GONE);
                tvDeadline.setVisibility(View.GONE);
                tvDescr.setVisibility(View.GONE);
                cbFinished.setVisibility(View.GONE);
                cbNeedHelp.setVisibility(View.GONE);
            }
        }
        catch (Exception rx) {
            rx.printStackTrace();
        }
    }
}
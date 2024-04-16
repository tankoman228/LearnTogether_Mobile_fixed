package com.example.learntogether_mobile.Activities.AdminActivity;

import static android.widget.AdapterView.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.Loaders.RolesLoader;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.R;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Активность редактирования роли пользователя и возможность его забанить
 */
public class ActEditUser extends AppCompatActivity {

    public static ListU User;
    TextView tvUsername;
    Spinner spRole;
    int selectedRoleId = -1;
    boolean flagRequireQuery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        flagRequireQuery = true;
        tvUsername = findViewById(R.id.tvUsername);
        spRole = findViewById(R.id.spinner3);

        RolesLoader.UpdateRolesListIfNotLoaded(() -> runOnUiThread(() -> {
            List<ListU> roles = RolesLoader.Roles;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            int selectedRoleIndex = -1;

            for (int i = 0; i < roles.size(); i++) {
                ListU role = roles.get(i);
                adapter.add(role.getName());

                if (Objects.equals(role.getName(), User.getRole())) {
                    selectedRoleIndex = i;
                }
            }

            spRole.setAdapter(adapter);
            flagRequireQuery = false;

            if (selectedRoleIndex != -1) {
                spRole.setSelection(selectedRoleIndex);
            }
        }));

        spRole.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (User.getUsername().equals(Variables.username)) {
                    Toast.makeText(ActEditUser.this, R.string.cannot_edit_yourself_changes_will_not_be_saved, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Получение выбранного элемента из Spinner
                String selectedRoleName = (String) parentView.getItemAtPosition(position);

                // Нахождение соответствующей роли по имени
                for (ListU role : RolesLoader.Roles) {
                    if (role.getName().equals(selectedRoleName)) {
                        if (flagRequireQuery)
                            trySaveNewRole(selectedRoleId - 1, role.getID_Role(), role.getName());
                        selectedRoleId = role.getID_Role();
                        flagRequireQuery = true;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(l -> finish());
        findViewById(R.id.btnDeleteFromGroup).setOnClickListener(l -> {

        });
        findViewById(R.id.btnBanInSystem).setOnClickListener(l -> {

        });
    }


    private void trySaveNewRole(int oldSelection, int newRoleId, String newRoleName) {

        RequestU requestU = new RequestU();
        requestU.setSession_token(Variables.SessionToken);
        requestU.setID_Group(Variables.current_id_group);
        requestU.setID_Account(User.getID_Account());
        requestU.setID_Role(newRoleId);
        new RetrofitRequest().apiService.change_user_role(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                if (response.body().Error != null) {
                    Toast.makeText(ActEditUser.this, response.body().Error, Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> {
                        flagRequireQuery = false;
                        spRole.setSelection(oldSelection);
                        flagRequireQuery = true;
                    });
                    return;
                }
                User.setRole(newRoleName);
                Toast.makeText(ActEditUser.this, R.string.successfully_changed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {

            }
        });
    }
}
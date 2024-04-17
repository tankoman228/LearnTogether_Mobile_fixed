package com.example.learntogether_mobile.Activities.AdminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learntogether_mobile.API.Loaders.RolesLoader;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Создание приглашений для других пользователей, в текущую группу и в систему вообще
 */
public class ActRegisterTokens extends AppCompatActivity {

    private int selectedRoleId = -1;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tokens);

        textView = findViewById(R.id.rv);
        EditText editText = findViewById(R.id.etToken);

        loadTokens();

        Spinner spinner = findViewById(R.id.spinner);
        RolesLoader.UpdateRolesListIfNotLoaded(() -> runOnUiThread(() -> {
            List<ListU> roles = RolesLoader.Roles;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for (ListU role : roles) {
                adapter.add(role.getName());
                Log.d("API", role.getName());
            }
            spinner.setAdapter(adapter);
        }));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Получение выбранного элемента из Spinner
                String selectedRoleName = (String) parentView.getItemAtPosition(position);

                // Нахождение соответствующей роли по имени
                for (ListU role : RolesLoader.Roles) {
                    if (role.getName().equals(selectedRoleName)) {
                        selectedRoleId  = role.getID_Role();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedRoleId  = -1;
            }
        });

        findViewById(R.id.btnInsertToken).setOnClickListener(l -> {
            if (selectedRoleId == -1) {
                Toast.makeText(this, R.string.no_role_selected, Toast.LENGTH_SHORT).show(); return;
            } else if (editText.getText().toString().length() < 5) {
                Toast.makeText(this, R.string.too_short_invite_code, Toast.LENGTH_SHORT).show(); return;
            }

            RequestU requestU = new RequestU();
            requestU.setSession_token(GlobalVariables.SessionToken);
            requestU.setID_Group(GlobalVariables.current_id_group);
            requestU.setText(editText.getText().toString());
            requestU.setID_Role(selectedRoleId);
            editText.setText("");
            new RetrofitRequest().apiService.create_token(requestU).enqueue(new Callback<ResponseU>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                    if (response.body().Error != null) {
                        Toast.makeText(ActRegisterTokens.this, response.body().Error, Toast.LENGTH_SHORT).show(); return;
                    }

                    runOnUiThread(() -> {
                        loadTokens();
                    });
                }

                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {

                }
            });
        });
    }

    private void loadTokens() {
        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        requestU.setID_Group(GlobalVariables.current_id_group);
        new RetrofitRequest().apiService.get_tokens(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                runOnUiThread(() -> {
                    String s = "";
                    for (String t: response.body().getStringList()) {
                        s += t + "\n";
                    }
                    textView.setText(s);
                });
            }

            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {

            }
        });
    }
}
package com.example.learntogether_mobile.Activities.AdminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.learntogether_mobile.API.GlobalVariables;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.Loaders.RolesLoader;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Редактирование ролей в системе, управление их правами
 */
public class ActRolesEdit extends AppCompatActivity {

    Spinner spRole;
    EditText etRoleName, etAdminLevel;
    CheckBox cbModerateP, cbOffer, cbRoles, cbGroup, cbForumAllowed, cbCommentsAllowed, cbModerateComments, cbInvite, cbBan, cbAdmin;
    PermissionCheckBox[] permissionCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles_edit);

        spRole = findViewById(R.id.spinner2);
        etRoleName = findViewById(R.id.etRolename);
        etAdminLevel = findViewById(R.id.etAdminLevel);

        cbModerateP = findViewById(R.id.cbModerateP);
        cbOffer  = findViewById(R.id.cbOffer);
        cbRoles = findViewById(R.id.cbRoles);
        cbGroup = findViewById(R.id.cbGroup);
        cbForumAllowed = findViewById(R.id.cbForumAllowed);
        cbCommentsAllowed = findViewById(R.id.cbCommentsAllowed);
        cbModerateComments  = findViewById(R.id.cbModerareCpmments);
        cbInvite  = findViewById(R.id.cbInvite);
        cbBan  = findViewById(R.id.cbBan);
        cbAdmin = findViewById(R.id.cbAdmin);

        findViewById(R.id.btnSave).setOnClickListener(l -> save());
        findViewById(R.id.btnDelete).setOnClickListener(l -> delete());

        permissionCheckBoxes = new PermissionCheckBox[] {
                new PermissionCheckBox("moderate_publications", cbModerateP),
                new PermissionCheckBox("offer_publications", cbOffer),
                new PermissionCheckBox("edit_roles", cbRoles),
                new PermissionCheckBox("edit_group", cbGroup),
                new PermissionCheckBox("forum_allowed", cbForumAllowed),
                new PermissionCheckBox("comments_allowed", cbCommentsAllowed),
                new PermissionCheckBox("moderate_comments", cbModerateComments),
                new PermissionCheckBox("create_tokens", cbInvite),
                new PermissionCheckBox("ban_accounts", cbBan)
        };

        for (var c: permissionCheckBoxes) {
            c.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {

                    for (var p: GlobalVariables.myPermissions) {
                        if (p.equals(c.permission)) {
                            return;
                        }
                    }
                    c.checkBox.setChecked(false);
                    Toast.makeText(this, R.string.permission_is_unavailable, Toast.LENGTH_SHORT).show();
                }
            });
        }

        RolesLoader.UpdateRolesListIfNotLoaded(() -> runOnUiThread(() -> {
            List<ListU> roles = RolesLoader.Roles;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            for (int i = 0; i < roles.size(); i++) {
                ListU role = roles.get(i);
                adapter.add(role.getName());
            }

            spRole.setAdapter(adapter);
            spRole.setSelection(1);
        }));

        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedRoleName = (String) parentView.getItemAtPosition(position);

                // Нахождение соответствующей роли по имени
                for (ListU role : RolesLoader.Roles) {
                    if (role.getName().equals(selectedRoleName)) {

                        etRoleName.setText(role.getName());
                        for (var c: permissionCheckBoxes) {
                            c.checkBox.setChecked(false);
                           for (var p: role.getPermissions()) {
                               if (c.permission.equals(p)) {
                                   c.checkBox.setChecked(true);
                                   break;
                               }
                           }
                        }
                        cbAdmin.setChecked(role.getIsAdmin());
                        etAdminLevel.setText(String.valueOf(role.getAdminLevel()));
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private static class PermissionCheckBox {
        public String permission;
        public CheckBox checkBox;

        public PermissionCheckBox(String permission, CheckBox checkBox_) {
            this.permission = permission;
            checkBox = checkBox_;
        }
    }


    private void save() {
        RequestU requestU = new RequestU();
        requestU.setSession_token(GlobalVariables.SessionToken);
        requestU.Items = new ArrayList<>();
        for (var ac: permissionCheckBoxes) {
            if (ac.checkBox.isChecked()) {
                requestU.Items.add(ac.permission);
            }
        }
        requestU.setName(etRoleName.getText().toString());
        requestU.setIsAdmin(cbAdmin.isChecked());
        try {
            requestU.setAdminLevel(Integer.parseInt(etAdminLevel.getText().toString()));
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Admin level must be a number!", Toast.LENGTH_SHORT).show();
            return;
        }
        requestU.setID_Group(GlobalVariables.current_id_group);
        new RetrofitRequest().apiService.new_role(requestU).enqueue(new Callback<ResponseU>() {
            @Override
            public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                assert response.body() != null;
                if (response.body().Error != null) {
                    Toast.makeText(ActRolesEdit.this, response.body().Error, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ActRolesEdit.this, R.string.success, Toast.LENGTH_SHORT).show();
                    RolesLoader.UpdateRolesList(() -> {
                        List<ListU> roles = RolesLoader.Roles;
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ActRolesEdit.this, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        for (int i = 0; i < roles.size(); i++) {
                            ListU role = roles.get(i);
                            adapter.add(role.getName());
                        }
                        spRole.setAdapter(adapter);
                        spRole.setSelection(0);
                    });
                }
            }
            @Override
            public void onFailure(Call<ResponseU> call, Throwable t) {

            }
        });
    }

    private void delete() {

        int id_role = RolesLoader.Roles.stream().filter(r -> Objects.equals(r.getName(), etRoleName.getText().toString())).findFirst().map(ListU::getID_Role).orElse(-1);
        if (id_role == -1) {
            Toast.makeText(this, R.string.no_role_with_such_name_select_from_list_at_first, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ActRolesEdit.this);
        builder.setMessage(getString(R.string.a_you_sure_you_want_to_remove_this_role_from_system) + " " + etRoleName.getText().toString())
                .setPositiveButton(R.string.yes, (dialog, id) -> {

                    RequestU requestU = new RequestU();
                    requestU.setSession_token(GlobalVariables.SessionToken);
                    requestU.setID_Role(id_role);
                    requestU.setID_Group(GlobalVariables.current_id_group);

                    new RetrofitRequest().apiService.delete_role(requestU).enqueue(new Callback<ResponseU>() {
                        @Override
                        public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                            assert response.body() != null;
                            if (response.body().Error != null) {
                                Toast.makeText(ActRolesEdit.this, response.body().Error, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ActRolesEdit.this, R.string.success, Toast.LENGTH_SHORT).show();
                                RolesLoader.UpdateRolesList(() -> {
                                    List<ListU> roles = RolesLoader.Roles;
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ActRolesEdit.this, android.R.layout.simple_spinner_item);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    for (int i = 0; i < roles.size(); i++) {
                                        ListU role = roles.get(i);
                                        adapter.add(role.getName());
                                    }
                                    spRole.setAdapter(adapter);
                                    spRole.setSelection(0);
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseU> call, Throwable t) {

                        }
                    });
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {});
        builder.create().show();
    }
}
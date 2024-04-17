package com.example.learntogether_mobile.Activities.AdminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.learntogether_mobile.R;

public class ActRolesEdit extends AppCompatActivity {

    Spinner spRole;
    ToggleButton tbEditWhat;
    EditText etRoleName, etAdminLevel;
    CheckBox cbModerateP, cbOffer, cbRoles, cbGroup, cbForumAllowed, cbCommentsAllowed, cbModerateComments, cbInvite, cbBan, cbAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles_edit);

        spRole = findViewById(R.id.spinner2);
        tbEditWhat = findViewById(R.id.tbSwitchMode);
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
    }
}
package com.ark.studentmonitoring.View.User.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Profile;
import com.ark.studentmonitoring.View.User.Teacher.ManageStudent;
import com.ark.studentmonitoring.databinding.ActivityDashboardAdminBinding;


public class DashboardAdmin extends AppCompatActivity {

    private ActivityDashboardAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(DashboardAdmin.this, Profile.class);
            finish();
        });

        binding.cardManageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.updateUI(DashboardAdmin.this, ManageAccount.class);
                finish();
            }
        });

        binding.card6Sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.updateUI(DashboardAdmin.this, ManageStudentClass.class);
                finish();
            }
        });
    }
}
package com.ark.studentmonitoring.View.User.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Profile;
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

        binding.cardManageAccount.setOnClickListener(view -> Utility.updateUI(DashboardAdmin.this, ManageAccount.class));

        binding.card1Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "1");
            startActivity(intent);
        });

        binding.card2Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "2");
            startActivity(intent);
        });

        binding.card3Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "3");
            startActivity(intent);
        });

        binding.card4Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "4");
            startActivity(intent);
        });

        binding.card5Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "5");
            startActivity(intent);
        });

        binding.card6Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "6");
            startActivity(intent);
        });

        binding.cardManageYearSchool.setOnClickListener(view -> Utility.updateUI(DashboardAdmin.this, ManageYearSchool.class));
    }
}
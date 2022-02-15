package com.ark.studentmonitoring.View.User.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Profile;
import com.ark.studentmonitoring.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(Dashboard.this, Profile.class);
            finish();
        });
    }
}
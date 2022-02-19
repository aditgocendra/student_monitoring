package com.ark.studentmonitoring.View.User.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Profile;
import com.ark.studentmonitoring.databinding.ActivityDashboardTeacherBinding;

public class DashboardTeacher extends AppCompatActivity {

    private ActivityDashboardTeacherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

        binding.cardManageStudent.setOnClickListener(view -> {
            Utility.updateUI(DashboardTeacher.this, ManageStudent.class);

        });

        binding.cardManageMyClass.setOnClickListener(view -> {
            Utility.updateUI(DashboardTeacher.this, ManageMyClass.class);

        });

        binding.cardManageValueStudent.setOnClickListener(view -> Utility.updateUI(DashboardTeacher.this, ManageValueStudent.class));
    }
}
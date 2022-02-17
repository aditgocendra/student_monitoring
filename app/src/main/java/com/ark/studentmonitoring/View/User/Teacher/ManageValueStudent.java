package com.ark.studentmonitoring.View.User.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageValueStudentBinding;

public class ManageValueStudent extends AppCompatActivity {

    private ActivityManageValueStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageValueStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(ManageValueStudent.this, DashboardTeacher.class);
            finish();
        });

        binding.cardClass.setOnClickListener(view -> Utility.updateUI(ManageValueStudent.this, StudentChoice.class));
    }
}
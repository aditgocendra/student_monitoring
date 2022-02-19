package com.ark.studentmonitoring.View.User.Parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityStudentListBinding;

public class StudentList extends AppCompatActivity {

    private ActivityStudentListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

        binding.cardStudent.setOnClickListener(view -> Utility.updateUI(StudentList.this, ChildValue.class));
    }


}
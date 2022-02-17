package com.ark.studentmonitoring.View.User.Parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.HomeApp;
import com.ark.studentmonitoring.databinding.ActivityClassListBinding;

public class ClassList extends AppCompatActivity {

    private ActivityClassListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.updateUI(ClassList.this, HomeApp.class);
                finish();
            }
        });

        binding.cardClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.updateUI(ClassList.this, StudentList.class);
            }
        });
    }
}
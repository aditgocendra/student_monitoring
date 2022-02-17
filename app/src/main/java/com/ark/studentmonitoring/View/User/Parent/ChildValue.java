package com.ark.studentmonitoring.View.User.Parent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.HomeApp;
import com.ark.studentmonitoring.databinding.ActivityChildValueBinding;

public class ChildValue extends AppCompatActivity {

    private ActivityChildValueBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildValueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        listenerClick();
    }

    private void listenerClick() {

        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(ChildValue.this, HomeApp.class);
            finish();
        });

    }
}
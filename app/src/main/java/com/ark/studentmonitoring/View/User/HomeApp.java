package com.ark.studentmonitoring.View.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.Auth.SignIn;
import com.ark.studentmonitoring.databinding.ActivityHomeAppBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeApp extends AppCompatActivity {

    private ActivityHomeAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listenerClick();
    }

    private void listenerClick() {
        binding.logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Utility.updateUI(HomeApp.this, SignIn.class);
            finish();
        });
    }
}
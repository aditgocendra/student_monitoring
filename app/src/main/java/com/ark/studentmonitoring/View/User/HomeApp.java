package com.ark.studentmonitoring.View.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ark.studentmonitoring.Model.ModelUser;
import com.ark.studentmonitoring.NetworkChangeListener;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityHomeAppBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeApp extends AppCompatActivity {

    private ActivityHomeAppBinding binding;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        listenerClick();
        setRoleUserCurrent();
    }

    @Override
    protected void onStart() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(networkChangeListener, filter);
            }
        }, 1000);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void listenerClick() {
        binding.accountImg.setOnClickListener(view -> {
            Utility.updateUI(HomeApp.this, Profile.class);
            finish();
        });
    }

    private void setRoleUserCurrent(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(Utility.uidCurrentUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelUser modelUser = task.getResult().getValue(ModelUser.class);
                Utility.roleCurrentUser = modelUser.getRole();
                Utility.usernameCurrentUser = modelUser.getUsername();
                setLayoutWithRole();
            }else {
                Utility.toastLS(HomeApp.this, task.getException().getMessage());
            }
        });
    }

    private void setLayoutWithRole() {
        if (Utility.roleCurrentUser.equals("parent")){
            binding.textPresent.setText("Haloo "+ Utility.usernameCurrentUser);
            binding.textPresent2.setText("Yuk pantau perkembangan anakmu");
        }else if(Utility.roleCurrentUser.equals("teacher")) {
            binding.textPresent.setText("Haloo "+ Utility.usernameCurrentUser);
            binding.textPresent2.setText("Yuk bantu orang tua untuk perkembangan anaknya");
        }else {
            binding.textPresent.setText("Haloo Administrator");
            binding.textPresent2.setText("Apa yang ingin anda lakukan hari ini");
            binding.cardMyChild.setVisibility(View.GONE);
            binding.textHeaderChild.setVisibility(View.GONE);
        }
    }


}
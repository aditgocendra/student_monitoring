package com.ark.studentmonitoring.View.User.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.ark.studentmonitoring.Adapter.AdapterManageAccount;
import com.ark.studentmonitoring.Model.ModelUser;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageAccountBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ManageAccount extends AppCompatActivity {

    private ActivityManageAccountBinding binding;
    private AdapterManageAccount adapterManageAccount;
    private List<ModelUser> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleManageAccount.setLayoutManager(layoutManager);
        binding.recycleManageAccount.setItemAnimator(new DefaultItemAnimator());

        setDataAccount();
    }


    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
    }

    private void setDataAccount() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelUser modelUser = dataSnapshot.getValue(ModelUser.class);
                    modelUser.setKey(dataSnapshot.getKey());
                    if (!modelUser.getRole().equals("admin")){
                        userList.add(modelUser);
                    }
                }
                adapterManageAccount = new AdapterManageAccount(ManageAccount.this, userList);
                binding.recycleManageAccount.setAdapter(adapterManageAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
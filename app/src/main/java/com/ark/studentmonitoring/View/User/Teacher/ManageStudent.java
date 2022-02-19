package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;


import com.ark.studentmonitoring.Adapter.AdapterManageStudent;
import com.ark.studentmonitoring.Model.ModelStudent;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageStudentBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageStudent extends AppCompatActivity {

    private ActivityManageStudentBinding binding;

    private List<ModelStudent> listStudent;
    private AdapterManageStudent adapterManageStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleManageStudent.setLayoutManager(layoutManager);
        binding.recycleManageStudent.setItemAnimator(new DefaultItemAnimator());

        setDataStudent();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

        binding.floatAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.updateUI(ManageStudent.this, AddStudent.class);
            }
        });
    }
    private void setDataStudent() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelStudent modelStudent = ds.getValue(ModelStudent.class);
                    modelStudent.setKey(ds.getKey());
                    listStudent.add(modelStudent);
                }
                adapterManageStudent = new AdapterManageStudent(ManageStudent.this, listStudent);
                binding.recycleManageStudent.setAdapter(adapterManageStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ManageStudent.this, error.getMessage());
            }
        });
    }

}
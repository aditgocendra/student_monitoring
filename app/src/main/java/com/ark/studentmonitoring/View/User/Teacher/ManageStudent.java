package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.ark.studentmonitoring.Adapter.AdapterManageStudent;
import com.ark.studentmonitoring.Model.ModelStudent;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageStudentBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageStudent extends AppCompatActivity {

    private ActivityManageStudentBinding binding;

    private List<ModelStudent> listStudent;
    private AdapterManageStudent adapterManageStudent;
    private int maxLoadData = 5;
    private long countData;


    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

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

        reference.child("student").get().addOnCompleteListener(this::setDataStudent);

        binding.recycleManageStudent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!binding.recycleManageStudent.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE){
                    Log.d("Current data", String.valueOf(listStudent.size())+" / "+countData);
                    if (listStudent.size() < countData){
                        if (listStudent.size() + maxLoadData <= countData){
                            int startData = (int) (countData - listStudent.size());
                            int endData = startData - maxLoadData;
                            nextLoadData(maxLoadData, startData + 1 , endData);
                            Log.d("Bottom Scrolled", "Load Next Data");
                        }else {
                            int calculateNextLoad = (int) (countData - listStudent.size());
                            int startData = (int) (countData - listStudent.size());
                            int endData = startData - calculateNextLoad;
                            nextLoadData(calculateNextLoad, startData + 1, endData);
                            Log.d("Calculate next load", String.valueOf(calculateNextLoad));
                        }
                    }
                }
            }
        });
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

        binding.floatAddStudent.setOnClickListener(view -> Utility.updateUI(ManageStudent.this, AddStudent.class));
    }
    private void setDataStudent(Task<DataSnapshot> taskCount) {
        countData = Objects.requireNonNull(taskCount.getResult()).getChildrenCount();
        reference.child("student").orderByChild("index_student").startAfter(countData - maxLoadData).limitToLast(maxLoadData).addValueEventListener(new ValueEventListener() {
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

    private void nextLoadData(int limitNextLoad, int startIndex, int endIndex){
        reference.child("student").orderByChild("index_student").startAfter(endIndex).endBefore(startIndex).limitToLast(limitNextLoad).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    ModelStudent modelStudent = ds.getValue(ModelStudent.class);
                    modelStudent.setKey(ds.getKey());
                    listStudent.add(modelStudent);
                }
                adapterManageStudent.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ManageStudent.this, "Database : "+error.getMessage());
            }
        });


    }





}
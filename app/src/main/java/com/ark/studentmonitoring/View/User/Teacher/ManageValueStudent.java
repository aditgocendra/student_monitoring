package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.ark.studentmonitoring.Adapter.AdapterManageValueStudent;
import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageValueStudentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageValueStudent extends AppCompatActivity {

    private ActivityManageValueStudentBinding binding;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private List<ModelStudentClass> listStudentClass;
    private AdapterManageValueStudent adapterManageValueStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageValueStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleManageValueStudent.setLayoutManager(layoutManager);
        binding.recycleManageValueStudent.setItemAnimator(new DefaultItemAnimator());

        setDataMyClass();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

//        binding.cardClass.setOnClickListener(view -> Utility.updateUI(ManageValueStudent.this, StudentChoice.class));
    }

    private void setDataMyClass() {
        reference.child("class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudentClass = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot : ds.getChildren()){
                        ModelStudentClass modelStudentClass = dataSnapshot.getValue(ModelStudentClass.class);
                        modelStudentClass.setKey(dataSnapshot.getKey());
                        if (modelStudentClass.getTeacher().equals(Utility.uidCurrentUser)){
                            listStudentClass.add(modelStudentClass);
                        }
                    }
                }
                adapterManageValueStudent = new AdapterManageValueStudent(ManageValueStudent.this, listStudentClass);
                binding.recycleManageValueStudent.setAdapter(adapterManageValueStudent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ManageValueStudent.this, error.getMessage());
            }
        });
    }
}
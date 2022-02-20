package com.ark.studentmonitoring.View.User.Parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ark.studentmonitoring.Adapter.AdapterStudentList;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.Model.ModelTeacher;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityStudentListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentList extends AppCompatActivity {

    private ActivityStudentListBinding binding;

    private AdapterStudentList adapterStudentList;
    private List<ModelStudentInClass> studentInClassList;

    private String classStudent, keyClass, keyTeacher;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();

        classStudent = getIntent().getStringExtra("class");
        keyClass = getIntent().getStringExtra("key_class");
        keyTeacher = getIntent().getStringExtra("key_teacher");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleStudentList.setLayoutManager(layoutManager);
        binding.recycleStudentList.setItemAnimator(new DefaultItemAnimator());

        setDataListStudent();
        setDataTeacher();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

    }

    private void setDataTeacher() {
        if (!keyTeacher.equals("-")){
            reference.child("user_detail").child(keyTeacher).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    ModelTeacher modelTeacher = task.getResult().getValue(ModelTeacher.class);
                    if (modelTeacher != null){
                        binding.nameTeacher.setText(modelTeacher.getFull_name());
                        binding.nameTeacher.setVisibility(View.VISIBLE);
                    }else {
                        binding.nameTeacher.setVisibility(View.GONE);
                    }
                }else {
                    Utility.toastLS(StudentList.this, task.getException().getMessage());
                }
            });
        }
    }

    private void setDataListStudent() {

        reference.child("student_in_class").child(classStudent).child(keyClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentInClassList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelStudentInClass modelStudentInClass = ds.getValue(ModelStudentInClass.class);
                    modelStudentInClass.setKey(ds.getKey());
                    studentInClassList.add(modelStudentInClass);
                }
                adapterStudentList = new AdapterStudentList(StudentList.this, studentInClassList);
                binding.recycleStudentList.setAdapter(adapterStudentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
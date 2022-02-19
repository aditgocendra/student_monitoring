package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ark.studentmonitoring.Adapter.AdapterSeeStudentMyClass;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivitySeeStudentMyClassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeeStudentMyClass extends AppCompatActivity {

    private ActivitySeeStudentMyClassBinding binding;
    private String classStudent, keyClass;

    private AdapterSeeStudentMyClass adapterSeeStudentMyClass;
    private List<ModelStudent> listStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeStudentMyClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        keyClass = getIntent().getStringExtra("key_class");

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleSeeStudentClass.setLayoutManager(layoutManager);
        binding.recycleSeeStudentClass.setItemAnimator(new DefaultItemAnimator());

        setDataStudentMyClass();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setDataStudentMyClass() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("student_in_class").child(classStudent).child(keyClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelStudentInClass modelStudentInClass = ds.getValue(ModelStudentInClass.class);
                    modelStudentInClass.setKey(ds.getKey());

                    reference.child("student").child(modelStudentInClass.getKey_student()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            ModelStudent modelStudent = task.getResult().getValue(ModelStudent.class);
                            modelStudent.setKey(task.getResult().getKey());
                            listStudent.add(modelStudent);
                        }else {
                            Utility.toastLS(SeeStudentMyClass.this, "Task : "+task.getException().getMessage());
                        }
                    });
                }

                adapterSeeStudentMyClass = new AdapterSeeStudentMyClass(SeeStudentMyClass.this, listStudent);
                binding.recycleSeeStudentClass.setAdapter(adapterSeeStudentMyClass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(SeeStudentMyClass.this, "Database : "+error.getMessage());
            }
        });
    }



}
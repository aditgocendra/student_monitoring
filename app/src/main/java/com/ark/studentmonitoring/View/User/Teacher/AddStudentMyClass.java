package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ark.studentmonitoring.Adapter.AdapterAddStudentMyClass;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityAddStudentMyClassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddStudentMyClass extends AppCompatActivity {

    private ActivityAddStudentMyClassBinding binding;

    private String classStudent, keyClass;
    private List<ModelStudent> listModelStudent;
    private AdapterAddStudentMyClass adapterAddStudentMyClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentMyClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        keyClass = getIntent().getStringExtra("key_class");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleAddStudentClass.setLayoutManager(layoutManager);
        binding.recycleAddStudentClass.setItemAnimator(new DefaultItemAnimator());

        setDataStudent();
        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
    }

    private void setDataStudent() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  listModelStudent = new ArrayList<>();

                  for (DataSnapshot ds : snapshot.getChildren()){
                      ModelStudent modelStudent = ds.getValue(ModelStudent.class);
                      modelStudent.setKey(ds.getKey());

                      reference
                              .child("student_in_class")
                              .child(classStudent)
                              .child(keyClass).child(modelStudent.getKey()).get().addOnCompleteListener(task -> {
                                  if (task.isSuccessful()){
                                      ModelStudentInClass modelStudentInClass = task.getResult().getValue(ModelStudentInClass.class);
                                      if (modelStudentInClass == null){
                                          listModelStudent.add(modelStudent);
                                      }
                                  }else {
                                      Utility.toastLS(AddStudentMyClass.this, "Data gagal dimuat");
                                  }

                              });
                  }

                  adapterAddStudentMyClass = new AdapterAddStudentMyClass(AddStudentMyClass.this, listModelStudent, classStudent, keyClass);
                  binding.recycleAddStudentClass.setAdapter(adapterAddStudentMyClass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(AddStudentMyClass.this, error.getMessage());
            }
        });
    }
}
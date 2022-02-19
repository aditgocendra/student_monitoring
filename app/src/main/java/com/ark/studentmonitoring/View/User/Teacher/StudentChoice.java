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
import com.ark.studentmonitoring.Adapter.AdapterStudentChoice;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityStudentChoiceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentChoice extends AppCompatActivity {

    private ActivityStudentChoiceBinding binding;
    private String classStudent, keyClass;
    private List<ModelStudent> listStudent;
    private AdapterStudentChoice adapterStudentChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentChoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        keyClass = getIntent().getStringExtra("key_class");

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleStudentChoice.setLayoutManager(layoutManager);
        binding.recycleStudentChoice.setItemAnimator(new DefaultItemAnimator());

        setDataStudentMyClass();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

//        binding.cardStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Utility.updateUI(StudentChoice.this, ValueStudent.class);
//            }
//        });
    }

    private void setDataStudentMyClass() {
        binding.progressLoadData.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student_in_class").child(classStudent).child(keyClass).limitToLast(20).addValueEventListener(new ValueEventListener() {
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
                            Utility.toastLS(StudentChoice.this, "Task : "+task.getException().getMessage());
                        }
                    });
                }
                Log.d("test", String.valueOf(listStudent.size()));
                adapterStudentChoice = new AdapterStudentChoice(StudentChoice.this, listStudent, classStudent, keyClass);
                binding.recycleStudentChoice.setAdapter(adapterStudentChoice);
                binding.progressLoadData.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(StudentChoice.this, "Database : "+error.getMessage());
            }
        });
    }
}
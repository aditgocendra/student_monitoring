package com.ark.studentmonitoring.View.User.Parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ark.studentmonitoring.Adapter.AdapterChildValue;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.Model.ModelValueStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.HomeApp;
import com.ark.studentmonitoring.databinding.ActivityChildValueBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildValue extends AppCompatActivity {

    private ActivityChildValueBinding binding;

    private String keyStudent;
    private String keyClass;
    private String studentClass;

    private List<ModelValueStudent> listValueStudent;
    private AdapterChildValue adapterChildValue;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildValueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        keyStudent = getIntent().getStringExtra("key_student");
        keyClass = getIntent().getStringExtra("key_class");
        studentClass = getIntent().getStringExtra("class");

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleChildValue.setLayoutManager(layoutManager);
        binding.recycleChildValue.setItemAnimator(new DefaultItemAnimator());

        setDataStudent();
        setDataValueStudent();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });


    }

    private void setDataStudent(){
        reference.child("student").child(keyStudent).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelStudent modelStudent = task.getResult().getValue(ModelStudent.class);
                binding.textNameStudent.setText(modelStudent.getName());

                if (modelStudent.getGender().equals("Laki-laki")){
                    binding.profileImg.setImageResource(R.drawable.man_student);
                }else {
                    binding.profileImg.setImageResource(R.drawable.women_student);
                }
            }
        });
    }

    private void setDataValueStudent(){
        reference.child("value_student").child(keyStudent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listValueStudent = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()){
                    for (DataSnapshot ds1 : ds.getChildren()){
                        ModelValueStudent modelValueStudent = ds1.getValue(ModelValueStudent.class);
                        modelValueStudent.setKey(ds1.getKey());
                        listValueStudent.add(modelValueStudent);
                    }

                }

                adapterChildValue = new AdapterChildValue(ChildValue.this, listValueStudent, keyStudent, keyClass, studentClass);
                binding.recycleChildValue.setAdapter(adapterChildValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ChildValue.this, "Database :"+error.getMessage());
            }
        });
    }
}
package com.ark.studentmonitoring.View.User.Parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ark.studentmonitoring.Adapter.AdapterClassList;
import com.ark.studentmonitoring.Adapter.AdapterManageAccount;
import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Administrator.ManageAccount;
import com.ark.studentmonitoring.databinding.ActivityClassListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassList extends AppCompatActivity {

    private ActivityClassListBinding binding;
    private String classStudent;
    private List<ModelStudentClass> listStudentClass;
    private AdapterClassList adapterClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleClassList.setLayoutManager(layoutManager);
        binding.recycleClassList.setItemAnimator(new DefaultItemAnimator());

        setDataClass();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> finish());

//        binding.cardClass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Utility.updateUI(ClassList.this, StudentList.class);
//            }
//        });
    }

    private void setDataClass() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("class").child(classStudent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudentClass = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelStudentClass modelStudentClass = dataSnapshot.getValue(ModelStudentClass.class);
                    modelStudentClass.setKey(dataSnapshot.getKey());
                    listStudentClass.add(modelStudentClass);
                }
                adapterClassList = new AdapterClassList(ClassList.this, listStudentClass);
                binding.recycleClassList.setAdapter(adapterClassList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ClassList.this, "Database : "+error.getMessage());
            }
        });
    }
}
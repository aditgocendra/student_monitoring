package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.ark.studentmonitoring.Adapter.AdapterAddStudentMyClass;
import com.ark.studentmonitoring.Adapter.AdapterSeeStudentMyClass;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivitySeeStudentMyClassBinding;
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
        binding.recycleSeeStudentClass.setHasFixedSize(true);
        binding.recycleSeeStudentClass.setLayoutManager(layoutManager);
        binding.recycleSeeStudentClass.setItemAnimator(new DefaultItemAnimator());

        setDataStudentMyClass();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> finish());

        binding.searchStudentByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0){
                    setDataStudentMyClass();
                }else {
                    String keySearch = binding.searchStudentByName.getText().toString();
                    searchDataStudentMyClass(keySearch);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.searchStudentByName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Utility.hideKeyboard(SeeStudentMyClass.this);
                handled = true;
            }
            return handled;
        });

    }

    private void setDataStudentMyClass() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelStudent modelStudent = ds.getValue(ModelStudent.class);
                    modelStudent.setKey(ds.getKey());
                    reference
                            .child("student_in_class")
                            .child(classStudent)
                            .child(keyClass).child(modelStudent.getKey()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            ModelStudentInClass modelStudentInClass = task.getResult().getValue(ModelStudentInClass.class);
                            if (modelStudentInClass != null){
                                modelStudentInClass.setKey(task.getResult().getKey());
                                listStudent.add(modelStudent);
                                Log.d("test", modelStudent.getName());
                            }
                        }else {
                            Utility.toastLS(SeeStudentMyClass.this, "Data gagal dimuat");
                        }
                    });
                }
                Log.d("test1", String.valueOf(listStudent.size()));
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    adapterSeeStudentMyClass = new AdapterSeeStudentMyClass(SeeStudentMyClass.this, listStudent, classStudent, keyClass);
                    binding.recycleSeeStudentClass.setAdapter(adapterSeeStudentMyClass);
                }, 1000);

                Log.d("test2", "adapter set");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(SeeStudentMyClass.this, error.getMessage());
            }
        });
    }

    private void searchDataStudentMyClass(String keyword) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").orderByChild("name").startAt(keyword).endAt(keyword + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelStudent modelStudent = ds.getValue(ModelStudent.class);
                    modelStudent.setKey(ds.getKey());

                    reference
                            .child("student_in_class")
                            .child(classStudent)
                            .child(keyClass).child(modelStudent.getKey()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            ModelStudentInClass modelStudentInClass = task.getResult().getValue(ModelStudentInClass.class);
                            if (modelStudentInClass != null){
                                modelStudentInClass.setKey(task.getResult().getKey());
                                listStudent.add(modelStudent);
                                Log.d("test", modelStudent.getName());
                            }
                        }else {
                            Utility.toastLS(SeeStudentMyClass.this, "Data gagal dimuat");
                        }
                    });
                }
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    adapterSeeStudentMyClass = new AdapterSeeStudentMyClass(SeeStudentMyClass.this, listStudent, classStudent, keyClass);
                    binding.recycleSeeStudentClass.setAdapter(adapterSeeStudentMyClass);
                }, 500);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(SeeStudentMyClass.this, error.getMessage());
            }
        });
    }


}
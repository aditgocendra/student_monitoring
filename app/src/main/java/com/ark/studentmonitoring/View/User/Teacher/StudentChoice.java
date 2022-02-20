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
    private String classStudent, keyClass, subClass;
    private List<ModelStudent> listStudent;
    private AdapterStudentChoice adapterStudentChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentChoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        subClass = getIntent().getStringExtra("sub_class");
        keyClass = getIntent().getStringExtra("key_class");

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleStudentChoice.setHasFixedSize(true);
        binding.recycleStudentChoice.setLayoutManager(layoutManager);
        binding.recycleStudentChoice.setItemAnimator(new DefaultItemAnimator());

        setDataStudentMyClass();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

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
                Utility.hideKeyboard(StudentChoice.this);
                handled = true;
            }
            return handled;
        });

    }

    private void setDataStudentMyClass() {
        binding.progressLoadData.setVisibility(View.VISIBLE);
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
                            }
                        }else {
                            Utility.toastLS(StudentChoice.this, "Data gagal dimuat");
                        }
                    });
                }
                Log.d("test", String.valueOf(listStudent.size()));
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    adapterStudentChoice = new AdapterStudentChoice(StudentChoice.this, listStudent, classStudent, subClass, keyClass);
                    binding.recycleStudentChoice.setAdapter(adapterStudentChoice);
                }, 1000);

                binding.progressLoadData.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(StudentChoice.this, "Database : "+error.getMessage());
            }
        });
    }

    private void searchDataStudentMyClass(String keyword){
        binding.progressLoadData.setVisibility(View.VISIBLE);
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
                            }
                        }else {
                            Utility.toastLS(StudentChoice.this, "Data gagal dimuat");
                        }
                    });
                }
                Log.d("test", String.valueOf(listStudent.size()));
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    adapterStudentChoice = new AdapterStudentChoice(StudentChoice.this, listStudent, classStudent, subClass, keyClass);
                    binding.recycleStudentChoice.setAdapter(adapterStudentChoice);
                }, 1000);
                binding.progressLoadData.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(StudentChoice.this, "Database : "+error.getMessage());
            }
        });
    }
}
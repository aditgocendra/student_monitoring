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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeeStudentMyClass extends AppCompatActivity {

    private ActivitySeeStudentMyClassBinding binding;
    private String classStudent, keyClass;

    private AdapterSeeStudentMyClass adapterSeeStudentMyClass;
    private List<ModelStudent> listStudent;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private int maxLoadData = 10;
    private long countData;
    private long dataLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeStudentMyClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        keyClass = getIntent().getStringExtra("key_class");



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleSeeStudentClass.setLayoutManager(layoutManager);
        binding.recycleSeeStudentClass.setItemAnimator(new DefaultItemAnimator());

        reference.child("student").get().addOnCompleteListener(this::setDataStudentMyClass);

        listenerClick();

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
                    reference.child("student").get().addOnCompleteListener(this::setDataStudentAfterSearch);
                }else {
                    String keySearch = binding.searchStudentByName.getText().toString();
                    searchDataStudentMyClass(keySearch);
                }
            }

            private void setDataStudentAfterSearch(Task<DataSnapshot> task) {
                setDataStudentMyClass(task);
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

        binding.recycleSeeStudentClass.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (binding.searchStudentByName.getText().toString().isEmpty()){
                    if (!binding.recycleSeeStudentClass.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE){
                        Log.d("Current data", String.valueOf(dataLoad)+" / "+countData);
                        if (dataLoad < countData){
                            if (dataLoad + maxLoadData <= countData){
                                int startData = (int) (countData - dataLoad);
                                int endData = startData - maxLoadData;
                                nextLoadData(maxLoadData, startData + 1 , endData);
                                dataLoad += maxLoadData;
                                Log.d("Bottom Scrolled", "Load Next Data");
                            }else {
                                int calculateNextLoad = (int) (countData - dataLoad);
                                int startData = (int) (countData - dataLoad);
                                int endData = startData - calculateNextLoad;
                                nextLoadData(calculateNextLoad, startData + 1, endData);
                                dataLoad += calculateNextLoad;
                                Log.d("Calculate next load", String.valueOf(calculateNextLoad));
                            }
                        }
                    }
                }
            }
        });

    }


    private void setDataStudentMyClass(Task<DataSnapshot> taskCount) {
        countData = Objects.requireNonNull(taskCount.getResult()).getChildrenCount();
        reference.child("student").orderByChild("index_student").startAfter(countData - maxLoadData).limitToLast(maxLoadData).addValueEventListener(new ValueEventListener() {
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
                    if (listStudent.size() == 0){
                        binding.warningTextNull.setVisibility(View.VISIBLE);
                    }else {
                        binding.warningTextNull.setVisibility(View.GONE);
                    }
                    adapterSeeStudentMyClass = new AdapterSeeStudentMyClass(SeeStudentMyClass.this, listStudent, classStudent, keyClass);
                    binding.recycleSeeStudentClass.setAdapter(adapterSeeStudentMyClass);
                    dataLoad = maxLoadData;

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

    private void nextLoadData(int limitNextLoad, int startIndex, int endIndex) {
        reference.child("student").orderByChild("index_student").startAfter(endIndex).endBefore(startIndex).limitToLast(limitNextLoad).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                                listStudent.add(modelStudent);
                            }
                        }else {
                            Utility.toastLS(SeeStudentMyClass.this, "Data gagal dimuat");
                        }
                    });
                }
                Log.d("test1", String.valueOf(listStudent.size()));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterSeeStudentMyClass.notifyDataSetChanged();
                    }
                },1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(SeeStudentMyClass.this, error.getMessage());
            }
        });
    }


}
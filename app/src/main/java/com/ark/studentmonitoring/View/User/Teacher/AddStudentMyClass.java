package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

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

    private String classStudent, subClass, keyClass;
    private List<ModelStudent> listModelStudent;
    private AdapterAddStudentMyClass adapterAddStudentMyClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentMyClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        subClass = getIntent().getStringExtra("sub_class");
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


        binding.searchStudentByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0){
                    setDataStudent();
                }else {
                    String keySearch = binding.searchStudentByName.getText().toString();
                    searchStudent(keySearch);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.searchStudentByName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Utility.hideKeyboard(AddStudentMyClass.this);
                handled = true;
            }
            return handled;
        });
    }

    private void setDataStudent() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").limitToLast(10).addValueEventListener(new ValueEventListener() {
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
                                          adapterAddStudentMyClass.notifyDataSetChanged();
                                      }
                                  }else {
                                      Utility.toastLS(AddStudentMyClass.this, "Data gagal dimuat");
                                  }
                              });
                  }
                  Log.d("test1", String.valueOf(listModelStudent.size()));
                  Handler handler = new Handler();
                  handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          adapterAddStudentMyClass = new AdapterAddStudentMyClass(AddStudentMyClass.this, listModelStudent, classStudent, keyClass, subClass);
                          binding.recycleAddStudentClass.setAdapter(adapterAddStudentMyClass);
                      }
                  },1000);

                  Log.d("test", "adapter set");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(AddStudentMyClass.this, error.getMessage());
            }
        });
    }

    private void searchStudent(String keyword){
        binding.progressCircular.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").orderByChild("name").startAt(keyword).endAt(keyword + "\uf8ff").addValueEventListener(new ValueEventListener() {
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
                                binding.progressCircular.setVisibility(View.GONE);
                            }else {
                                binding.progressCircular.setVisibility(View.GONE);
                            }
                        }else {
                            Utility.toastLS(AddStudentMyClass.this, "Data gagal dimuat");
                            binding.progressCircular.setVisibility(View.GONE);
                        }
                    });
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterAddStudentMyClass = new AdapterAddStudentMyClass(AddStudentMyClass.this, listModelStudent, classStudent, keyClass, subClass);
                        binding.recycleAddStudentClass.setAdapter(adapterAddStudentMyClass);
                    }
                },500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(AddStudentMyClass.this, error.getMessage());
            }
        });
    }


}
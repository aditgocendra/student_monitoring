package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;


import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;

import com.ark.studentmonitoring.databinding.ActivityAddStudentBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class AddStudent extends AppCompatActivity {

    private ActivityAddStudentBinding binding;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();
        setAutoCompleteText();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> finish());

        binding.finishAddBtn.setOnClickListener(view -> {
            String name = binding.nameStudentAdd.getText().toString();
            String nisn = binding.nisnStudentAdd.getText().toString();
            String studentNowClass = binding.studentClassNow.getText().toString();
            String age = binding.ageStudent.getText().toString();
            String gender = binding.autoCompleteGender.getText().toString();
            String diagnosa = binding.autoCompleteDiagnosa.getText().toString();

            if (name.isEmpty()){
                binding.nameStudentAdd.setError("Nama tidak boleh kosong");
            }else if(studentNowClass.isEmpty()){
                binding.studentClassNow.setError("Kelas siswa sekarang tidak boleh kosong");
            }else if (age.isEmpty()){
                binding.ageStudent.setError("Umur tidak boleh kosong");
            }else if (gender.isEmpty()){
                binding.autoCompleteGender.setError("Jenis kelamin tidak boleh kosong");
            }else if (diagnosa.isEmpty()){
                binding.autoCompleteDiagnosa.setError("Diagnosa tidak boleh kosong");
            }else if (nisn.isEmpty()){
                binding.nisnStudentAdd.setError("NISN tidak boleh kosong");
            }
            else {
                binding.progressCircular.setVisibility(View.VISIBLE);

                // check nisn
                reference.child("student").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelStudent modelStudent = ds.getValue(ModelStudent.class);
                            modelStudent.setKey(ds.getKey());
                            if (modelStudent.getNisn().equals(nisn)){
                                Utility.toastLS(AddStudent.this, "NISN sudah digunakan");
                                binding.progressCircular.setVisibility(View.GONE);
                                return;
                            }else {
                                // save data
                                saveDataStudent(name, nisn, studentNowClass, age, gender, diagnosa);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Utility.toastLS(AddStudent.this, "Database : "+error.getMessage());
                    }
                });
            }
        });
    }

    private void saveDataStudent(String name, String nisn,  String studentNowClass, String age, String gender, String diagnosa) {
//        get index count
        reference.child("student").get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                ModelStudent modelStudent = new ModelStudent(
                        task1.getResult().getChildrenCount() + 1,
                        name.toLowerCase(Locale.ROOT),
                        nisn,
                        studentNowClass,
                        age,
                        gender,
                        diagnosa
                );

                reference.child("student").push().setValue(modelStudent).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        binding.progressCircular.setVisibility(View.GONE);
                        Utility.toastLS(AddStudent.this, "Data berhasil ditambahkan");
                        finish();
                    }else {
                        binding.progressCircular.setVisibility(View.GONE);
                        Utility.toastLS(AddStudent.this, task.getException().getMessage());
                    }
                });
            }else {
                binding.progressCircular.setVisibility(View.GONE);
                Utility.toastLS(AddStudent.this, task1.getException().getMessage());
            }
        });
    }

    private void setAutoCompleteText(){
        String[] choiceGender = {"Laki-laki", "perempuan"};
        String[] choiceDiagnosa = {"Hyperaktif", "Speech Delay", "Keterbelakangan Mental"};

        ArrayAdapter<String> genderAdapter;
        genderAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, choiceGender);
        binding.autoCompleteGender.setAdapter(genderAdapter);

        ArrayAdapter<String> diagnosaAdapter;
        diagnosaAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, choiceDiagnosa);
        binding.autoCompleteDiagnosa.setAdapter(diagnosaAdapter);

    }


}
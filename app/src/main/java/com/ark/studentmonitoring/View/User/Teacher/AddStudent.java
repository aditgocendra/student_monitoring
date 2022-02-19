package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityAddStudentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudent extends AppCompatActivity {

    private ActivityAddStudentBinding binding;

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
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.finishAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    saveDataStudent(name, nisn, studentNowClass, age, gender, diagnosa);
                }
            }
        });
    }

    private void saveDataStudent(String name, String nisn,  String studentNowClass, String age, String gender, String diagnosa) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ModelStudent modelStudent = new ModelStudent(
                name,
                nisn,
                studentNowClass,
                age,
                gender,
                diagnosa
        );

        reference.child("student").push().setValue(modelStudent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    binding.progressCircular.setVisibility(View.GONE);
                    Utility.toastLS(AddStudent.this, "Data berhasil ditambahkan");
                    Utility.updateUI(AddStudent.this, ManageStudent.class);
                    finish();
                }else {
                    binding.progressCircular.setVisibility(View.GONE);
                    Utility.toastLS(AddStudent.this, task.getException().getMessage());
                }
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
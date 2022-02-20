package com.ark.studentmonitoring.View.User.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelValueStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityValueStudentBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ValueStudent extends AppCompatActivity {

    private ActivityValueStudentBinding binding;
    private String classStudent, keyClass, subClass, keyStudent;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValueStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        keyClass = getIntent().getStringExtra("key_class");
        subClass = getIntent().getStringExtra("sub_class");
        keyStudent = getIntent().getStringExtra("key_student");

        listenerClick();
        setDataStudent();

    }

    private void listenerClick() {
        String[] choiceValue = {"A","B","C","D"};
        ArrayAdapter<String> choiceAdapter;
        choiceAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, choiceValue);
        binding.autoCompleteValueSikap.setAdapter(choiceAdapter);

        binding.backBtn.setOnClickListener(view -> finish());

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        MaterialDatePicker<Long> materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            binding.datePick.setText(materialDatePicker.getHeaderText());

        });

        binding.datePick.setOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), "Date Picker"));

        binding.addValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = binding.autoCompleteValueSikap.getText().toString();
                String date = binding.datePick.getText().toString();
                String description = binding.descriptionValue.getText().toString();

                if (value.isEmpty()){
                    binding.autoCompleteValueSikap.setError("Nilai sikap kosong");
                }else if (date.equals("Pilih Tanggal")){
                    Utility.toastLS(ValueStudent.this, "Anda belum memilih tanggal");
                }else if(description.isEmpty()){
                    Utility.toastLS(ValueStudent.this, "Deskripsi masih kosong");
                }
                else {
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    binding.addValueBtn.setEnabled(false);
                    saveDataValueStudent(date, value, description);
                }
            }
        });
    }

    private void setDataStudent() {
        // set data student
        reference.child("student").child(keyStudent).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelStudent modelStudent = task.getResult().getValue(ModelStudent.class);
                modelStudent.setKey(task.getResult().getKey());

                binding.nameStudentText.setText(modelStudent.getName());

                if (modelStudent.getGender().equals("Laki-laki")){
                    binding.iconImg.setImageResource(R.drawable.man_student);
                }else {
                    binding.iconImg.setImageResource(R.drawable.women_student);
                }

            }else {
                Utility.toastLS(ValueStudent.this, task.getException().getMessage());
            }
        });
    }


    private void saveDataValueStudent(String date,String value, String description){
        ModelValueStudent modelValueStudent = new ModelValueStudent(
                date,
                value,
                description,
                classStudent+subClass
        );

        reference.child("value_student")
                .child(keyStudent)  // key student
                .child(keyClass)    // key class
                .push()             // key value
                .setValue(modelValueStudent).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        binding.progressCircular.setVisibility(View.GONE);
                        binding.addValueBtn.setEnabled(true);
                        Utility.toastLS(ValueStudent.this, "Berhasil menambahkan nilai siswa");
                        finish();
                    }else {
                        Utility.toastLS(ValueStudent.this, task.getException().getMessage());
                        binding.progressCircular.setVisibility(View.GONE);
                        binding.addValueBtn.setEnabled(true);
                    }
                });
    }
}
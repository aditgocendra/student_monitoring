package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityValueStudentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ValueStudent extends AppCompatActivity {

    private ActivityValueStudentBinding binding;
    private String classStudent, keyClass, keyStudent;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValueStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        classStudent = getIntent().getStringExtra("class");
        keyClass = getIntent().getStringExtra("key_class");
        keyStudent = getIntent().getStringExtra("key_student");

        listenerClick();

        setDataStudent();
        setDataValue();

    }

    private void listenerClick() {
        String[] choiceValue = {"A","B","C","D"};
        ArrayAdapter<String> choiceAdapter;
        choiceAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, choiceValue);

        binding.autoCompleteValueSikap.setAdapter(choiceAdapter);
        binding.backBtn.setOnClickListener(view -> finish());

        binding.editDataValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.autoCompleteValueSikap.getText().toString().isEmpty()){
                    binding.autoCompleteValueSikap.setError("Nilai sikap kosong");
                }else {
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    binding.editDataValueBtn.setEnabled(false);
                    changeDataValueStudent(binding.autoCompleteValueSikap.getText().toString());
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

    private void setDataValue(){
        // set value
        reference.child("student_in_class").child(classStudent).child(keyClass).child(keyStudent).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    ModelStudentInClass modelStudentInClass = task.getResult().getValue(ModelStudentInClass.class);
                    if (modelStudentInClass != null){
                        binding.textValue.setText("Nilai Sikap Siswa : "+modelStudentInClass.getValue_student());
                    }
                }else {
                    Utility.toastLS(ValueStudent.this, task.getException().getMessage());
                }
            }
        });
    }

    private void changeDataValueStudent(String value){
        reference.child("student_in_class")
                .child(classStudent)
                .child(keyClass)
                .child(keyStudent)
                .child("value_student").setValue(value).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        binding.progressCircular.setVisibility(View.GONE);
                        binding.editDataValueBtn.setEnabled(true);
                        binding.textValue.setText("Nilai Sikap Siswa : "+value);
                        Utility.toastLS(ValueStudent.this, "Berhasil mengubah nilai siswa");
                    }else {
                        Utility.toastLS(ValueStudent.this, task.getException().getMessage());
                        binding.progressCircular.setVisibility(View.GONE);
                        binding.editDataValueBtn.setEnabled(true);
                    }
                });
    }
}
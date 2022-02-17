package com.ark.studentmonitoring.View.User.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageStudentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ManageStudent extends AppCompatActivity {

    private ActivityManageStudentBinding binding;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(ManageStudent.this, DashboardTeacher.class);
            finish();
        });

        binding.floatAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.updateUI(ManageStudent.this, AddStudent.class);
            }
        });

        bottomSheetDialog = new BottomSheetDialog(ManageStudent.this);
        setBottomDialog();
        binding.cardEditDataStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();

            }
        });
    }

    private void setBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_edit_student, null, false);
        AutoCompleteTextView autoCompleteGender = viewBottomDialog.findViewById(R.id.auto_complete_gender);
        AutoCompleteTextView autoCompleteDiagnosa = viewBottomDialog.findViewById(R.id.auto_complete_diagnosa);
        Button finishEditBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        String[] gender = {"Laki-laki","Perempuan"};
        ArrayAdapter<String> genderAdapter;
        genderAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, gender);
        autoCompleteGender.setAdapter(genderAdapter);
        autoCompleteGender.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
        });

        String[] diagnosa = {"Keterbelakangan Mental","Hyperaktif", "Speech Delay"};
        ArrayAdapter<String> diagnosaAdapter;
        diagnosaAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, diagnosa);
        autoCompleteDiagnosa.setAdapter(diagnosaAdapter);
        autoCompleteDiagnosa.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
        });

        finishEditBtn.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(viewBottomDialog);
    }
}
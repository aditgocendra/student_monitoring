package com.ark.studentmonitoring.View.User.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityValueStudentBinding;

public class ValueStudent extends AppCompatActivity {

    private ActivityValueStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValueStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        listenerClick();

    }

    private void listenerClick() {
        String[] choiceValue = {"A","B","C","D"};
        ArrayAdapter<String> choiceAdapter;
        choiceAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, choiceValue);

        binding.autoCompleteValueSikap.setAdapter(choiceAdapter);
        binding.autoCompleteValueSikap.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
        });

        binding.backBtn.setOnClickListener(view -> Utility.updateUI(ValueStudent.this, StudentChoice.class));


    }
}
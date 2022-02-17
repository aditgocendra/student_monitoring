package com.ark.studentmonitoring.View.User.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Administrator.ManageStudentClass;
import com.ark.studentmonitoring.databinding.ActivityManageMyClassBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

public class ManageMyClass extends AppCompatActivity {

    private ActivityManageMyClassBinding binding;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageMyClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.updateUI(ManageMyClass.this, DashboardTeacher.class);
                finish();
            }
        });

        binding.cardEditMyclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(ManageMyClass.this);
                setEditBottomDialog();
                bottomSheetDialog.show();
            }
        });

        binding.floatAddMyClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(ManageMyClass.this);
                setAddBottomDialog();
                bottomSheetDialog.show();
            }
        });
    }

    private void setAddBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_add_myclass, null, false);
        TextInputEditText priceWholesaleTiAdd = viewBottomDialog.findViewById(R.id.price_wholesale_ti_add);
        Button finishAddBtn = viewBottomDialog.findViewById(R.id.finish_add_btn);

        finishAddBtn.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void setEditBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_edit_myclass, null, false);
        TextInputEditText priceWholesaleTiAdd = viewBottomDialog.findViewById(R.id.price_wholesale_ti_add);
        Button finishEditBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        finishEditBtn.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(viewBottomDialog);
    }
}
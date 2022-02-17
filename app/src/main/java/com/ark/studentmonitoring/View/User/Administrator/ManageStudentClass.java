package com.ark.studentmonitoring.View.User.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageStudentClassBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

public class ManageStudentClass extends AppCompatActivity {

    private ActivityManageStudentClassBinding binding;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageStudentClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(ManageStudentClass.this, DashboardAdmin.class);
            finish();
        });

        binding.cardEditAccount.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(ManageStudentClass.this);
            setEditBottomDialog();
            bottomSheetDialog.show();
        });

        binding.floatAddStudentClass.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(ManageStudentClass.this);
            setAddBottomDialog();
            bottomSheetDialog.show();
        });
    }

    private void setAddBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_add_class, null, false);
        TextInputEditText priceWholesaleTiAdd = viewBottomDialog.findViewById(R.id.price_wholesale_ti_add);
        Button finishAddBtn = viewBottomDialog.findViewById(R.id.finish_add_btn);

        finishAddBtn.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void setEditBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_edit_class, null, false);
        TextInputEditText priceWholesaleTiAdd = viewBottomDialog.findViewById(R.id.price_wholesale_ti_add);
        Button finishEditBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        finishEditBtn.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(viewBottomDialog);
    }
}
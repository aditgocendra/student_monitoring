package com.ark.studentmonitoring.View.User.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageAccountBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class ManageAccount extends AppCompatActivity {

    private ActivityManageAccountBinding binding;
    private BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        listenerClick();

        bottomSheetDialog = new BottomSheetDialog(ManageAccount.this);
        setBottomDialog();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(ManageAccount.this, DashboardAdmin.class);
            finish();
        });

        binding.cardEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
    }

    private void setBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_edit_account, null, false);
        AutoCompleteTextView autoCompleteRole = viewBottomDialog.findViewById(R.id.auto_complete_role);
        Button finishEditBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        String[] role = {"Administrator","Guru", "Orang Tua"};
        ArrayAdapter<String> accountAdapter;
        accountAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, role);
        autoCompleteRole.setAdapter(accountAdapter);
        autoCompleteRole.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
        });

        finishEditBtn.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(viewBottomDialog);
    }
}
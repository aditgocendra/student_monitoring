package com.ark.studentmonitoring.View.User.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ark.studentmonitoring.Adapter.AdapterManageYearSchool;
import com.ark.studentmonitoring.Model.ModelYearSchool;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageYearSchoolBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageYearSchool extends AppCompatActivity {

    private ActivityManageYearSchoolBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private List<ModelYearSchool> listYearSchool;
    private AdapterManageYearSchool adapterManageYearSchool;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageYearSchoolBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        bottomSheetDialog = new BottomSheetDialog(this);
        setAddBottomDialog();
        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleYearSchool.setLayoutManager(layoutManager);
        binding.recycleYearSchool.setItemAnimator(new DefaultItemAnimator());

        setDataYearSchool();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
        binding.floatAddYearSchool.setOnClickListener(view -> bottomSheetDialog.show());
    }

    private void setAddBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_add_year_school, null, false);
        TextInputEditText yearSchoolTiFrom = viewBottomDialog.findViewById(R.id.year_school_ti_add_from);
        TextInputEditText yearSchoolTiTo = viewBottomDialog.findViewById(R.id.year_school_ti_add_to);
        Button finishAddBtn = viewBottomDialog.findViewById(R.id.finish_add_btn);

        finishAddBtn.setOnClickListener(view -> {
            String yearAddFrom = yearSchoolTiFrom.getText().toString();
            String yearAddTo = yearSchoolTiTo.getText().toString();
            if (yearAddFrom.isEmpty()){
                yearSchoolTiFrom.setError("Tahun Ajaran mulai kosong");
            }else if (yearAddTo.isEmpty()){
                yearSchoolTiTo.setError("TahuTahun Ajaran sampai kosong");
            }else {
                binding.progressCircular.setVisibility(View.VISIBLE);
                saveDataYearSchool(yearAddFrom, yearAddTo);
            }
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void setDataYearSchool() {
        reference.child("year_school").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listYearSchool = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelYearSchool modelYearSchool = ds.getValue(ModelYearSchool.class);
                    modelYearSchool.setKey(ds.getKey());
                    listYearSchool.add(modelYearSchool);
                }
                adapterManageYearSchool = new AdapterManageYearSchool(ManageYearSchool.this, listYearSchool);
                binding.recycleYearSchool.setAdapter(adapterManageYearSchool);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ManageYearSchool.this, error.getMessage());
            }
        });
    }

    private void saveDataYearSchool(String yearFrom, String yearTo){
        ModelYearSchool modelYearSchool = new ModelYearSchool(
                yearFrom,
                yearTo);

        reference.child("year_school").push().setValue(modelYearSchool).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.toastLS(ManageYearSchool.this, "Berhasil menambahkan tahun ajaran");

            }else {
                Utility.toastLS(ManageYearSchool.this, task.getException().getMessage());
            }
            binding.progressCircular.setVisibility(View.GONE);
        });
    }

}
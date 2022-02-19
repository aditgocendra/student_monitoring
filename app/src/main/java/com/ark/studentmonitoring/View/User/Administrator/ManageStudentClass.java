package com.ark.studentmonitoring.View.User.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.ark.studentmonitoring.Adapter.AdapterManageStudentClass;
import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.Model.ModelYearSchool;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityManageStudentClassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageStudentClass extends AppCompatActivity {

    private ActivityManageStudentClassBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private String student_class;

    private List<ModelStudentClass> listStudentClass;
    private AdapterManageStudentClass adapterManageStudentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageStudentClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        student_class = getIntent().getStringExtra("class");
        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleManageStudentClass.setLayoutManager(layoutManager);
        binding.recycleManageStudentClass.setItemAnimator(new DefaultItemAnimator());

        setDataStudentClass();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

        binding.floatAddStudentClass.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(ManageStudentClass.this);
            setAddBottomDialog();
            bottomSheetDialog.show();
        });
    }

    private void setAddBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_add_class, null, false);
        AutoCompleteTextView autoCompleteYear = viewBottomDialog.findViewById(R.id.auto_complete_choice_year);
        TextInputEditText subClassAddTi = viewBottomDialog.findViewById(R.id.sub_class_ti_add);
        Button finishAddBtn = viewBottomDialog.findViewById(R.id.finish_add_btn);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item);
        reference.child("year_school").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelYearSchool modelYearSchool = ds.getValue(ModelYearSchool.class);
                    yearAdapter.add(modelYearSchool.getFrom_year()+" / "+modelYearSchool.getTo_year());
                }
                autoCompleteYear.setAdapter(yearAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ManageStudentClass.this, error.getMessage());
            }
        });

        finishAddBtn.setOnClickListener(view -> {
            String yearSchool = autoCompleteYear.getText().toString();
            String sub_class = subClassAddTi.getText().toString();

            if (yearSchool.isEmpty()){
                autoCompleteYear.setError("Pilih tahun ajaran");
            }else if (sub_class.isEmpty()){
                subClassAddTi.setError("Sub kelas kosong");
            }else {
                saveDataClass(sub_class, yearSchool);
            }
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void saveDataClass(String subClass, String yearSchool){
        ModelStudentClass modelStudentClass = new ModelStudentClass(
            student_class,
                subClass,
                yearSchool,
                "-"
        );
        reference.child("class").child(student_class).push().setValue(modelStudentClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.toastLS(ManageStudentClass.this, "Berhasil menambahkan data");
            }else{
                Utility.toastLS(ManageStudentClass.this, "Gagal menambahkan data");
            }
        });
    }

    private void setDataStudentClass() {
        reference.child("class").child(student_class).orderByChild("sub_student_class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudentClass = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelStudentClass modelStudentClass = ds.getValue(ModelStudentClass.class);
                    modelStudentClass.setKey(ds.getKey());
                    listStudentClass.add(modelStudentClass);
                }
                adapterManageStudentClass = new AdapterManageStudentClass(ManageStudentClass.this, listStudentClass);
                binding.recycleManageStudentClass.setAdapter(adapterManageStudentClass);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ManageStudentClass.this, error.getMessage());
            }
        });

    }

}
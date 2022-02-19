package com.ark.studentmonitoring.View.User.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.ark.studentmonitoring.Adapter.AdapterManageMyClass;
import com.ark.studentmonitoring.Model.ModelMyClass;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.Model.ModelYearSchool;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;

import com.ark.studentmonitoring.View.User.Administrator.ManageStudentClass;
import com.ark.studentmonitoring.databinding.ActivityManageMyClassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ManageMyClass extends AppCompatActivity {

    private ActivityManageMyClassBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private AdapterManageMyClass adapterManageMyClass;
    private List<ModelStudentClass> listStudentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageMyClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleManageMyClass.setLayoutManager(layoutManager);
        binding.recycleManageMyClass.setItemAnimator(new DefaultItemAnimator());

        setDataMyClass();
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

        binding.floatAddMyClass.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(ManageMyClass.this);
            setAddBottomDialog();
            bottomSheetDialog.show();
        });
    }

    private void setDataMyClass() {
        reference.child("class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudentClass = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot : ds.getChildren()){
                        ModelStudentClass modelStudentClass = dataSnapshot.getValue(ModelStudentClass.class);
                        modelStudentClass.setKey(dataSnapshot.getKey());
                        if (modelStudentClass.getTeacher().equals(Utility.uidCurrentUser)){
                            listStudentClass.add(modelStudentClass);
                        }
                    }
                }
                adapterManageMyClass = new AdapterManageMyClass(ManageMyClass.this, listStudentClass);
                binding.recycleManageMyClass.setAdapter(adapterManageMyClass);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(ManageMyClass.this, error.getMessage());
            }
        });
    }

    private void setAddBottomDialog(){
        View viewBottomDialog = getLayoutInflater().inflate(R.layout.layout_dialog_add_myclass, null, false);
        AutoCompleteTextView autoCompleteChoiceClass = viewBottomDialog.findViewById(R.id.auto_complete_choice_class);
        AutoCompleteTextView autoCompleteChoiceSubClass = viewBottomDialog.findViewById(R.id.auto_complete_choice_sub_class);
        TextInputLayout layoutSubClass = viewBottomDialog.findViewById(R.id.layout_sub_class);


        String[] choiceClass = {"1", "2", "3","4","5","6"};

        ArrayAdapter<String> genderAdapter;
        genderAdapter = new ArrayAdapter<>(this, R.layout.layout_option_item, choiceClass);
        autoCompleteChoiceClass.setAdapter(genderAdapter);

        autoCompleteChoiceClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                ArrayAdapter<ModelMyClass> subClassAdapter = new ArrayAdapter<>(ManageMyClass.this, R.layout.layout_option_item);
                reference.child("class").child(item).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelStudentClass modelStudentClass = ds.getValue(ModelStudentClass.class);

                            if (modelStudentClass != null){
                                modelStudentClass.setKey(ds.getKey());
                                if (modelStudentClass.getTeacher().equals("-")){
                                    subClassAdapter.add(
                                            new ModelMyClass(modelStudentClass.getKey(),
                                                    "TA : "+modelStudentClass.getYear_school()+" - "+
                                                            "Kelas : "+modelStudentClass.getStudent_class()+
                                                            modelStudentClass.getSub_student_class()));

                                }
                            }
                        }
                        if (!subClassAdapter.isEmpty()){
                            autoCompleteChoiceSubClass.setAdapter(subClassAdapter);
                            layoutSubClass.setVisibility(View.VISIBLE);
                        }else {
                            Utility.toastLL(ManageMyClass.this, "Tidak ditemukan sub class yang tersedia pada kelas ini");
                            bottomSheetDialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Utility.toastLS(ManageMyClass.this, error.getMessage());
                    }
                });
            }
        });

        autoCompleteChoiceSubClass.setOnItemClickListener((adapterView, view, i, l) -> {
            ModelMyClass modelMyClass = (ModelMyClass) adapterView.getItemAtPosition(i);
            reference
                    .child("class")
                    .child(autoCompleteChoiceClass.getText().toString())
                    .child(modelMyClass.getKey()).child("teacher").setValue(Utility.uidCurrentUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Utility.toastLS(ManageMyClass.this, "berhasil menambahkan kelas");
                            }else {
                                Utility.toastLS(ManageMyClass.this, task.getException().getMessage());
                            }
                        }
                    });

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(viewBottomDialog);
    }


}
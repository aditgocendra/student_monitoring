package com.ark.studentmonitoring.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterManageStudent extends RecyclerView.Adapter<AdapterManageStudent.MyViewHolder> {

    private Context mContext;
    private List<ModelStudent> listStudent;
    private BottomSheetDialog bottomSheetDialog;

    public AdapterManageStudent(Context mContext, List<ModelStudent> listStudent) {
        this.mContext = mContext;
        this.listStudent = listStudent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_manage_student, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelStudent modelStudent = listStudent.get(position);

        if (modelStudent.getGender().equals("Laki-laki")){
            holder.iconStudent.setImageResource(R.drawable.man_student);
        }else {
            holder.iconStudent.setImageResource(R.drawable.women_student);
        }

        holder.nameText.setText("Nama : "+modelStudent.getName());
        holder.nisnText.setText("NISN : "+modelStudent.getNisn());

        holder.cardDelete.setOnClickListener(view -> {
            //Create the Dialog here
            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.custom_dialog_delete);
            dialog.getWindow().setBackgroundDrawable(mContext.getDrawable(R.drawable.custom_dialog_background));

            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog.setCancelable(false); //Optional
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

            Button Okay = dialog.findViewById(R.id.btn_okay);
            Button Cancel = dialog.findViewById(R.id.btn_cancel);

            dialog.show();
            Okay.setOnClickListener(v -> {
                deleteStudent(modelStudent.getKey());
                dialog.dismiss();
            });

            Cancel.setOnClickListener(v -> dialog.dismiss());
        });

        holder.cardEdit.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(mContext);
            setBottomDialog(modelStudent);
            bottomSheetDialog.show();
        });
    }


    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, nisnText;
        ImageView iconStudent;
        CardView cardEdit, cardDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_child_text);
            nisnText = itemView.findViewById(R.id.nisn_child_text);
            iconStudent = itemView.findViewById(R.id.icon_student);
            cardEdit = itemView.findViewById(R.id.card_edit_data_student);
            cardDelete = itemView.findViewById(R.id.card_delete_data_student);
        }
    }

    private void setBottomDialog(ModelStudent modelStudent){
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewBottomDialog = li.inflate(R.layout.layout_dialog_edit_student, null, false);

        TextInputEditText nameTiEdit = viewBottomDialog.findViewById(R.id.name_edit_student);
        TextInputEditText nisnTiEdit = viewBottomDialog.findViewById(R.id.nisn_edit_student);
        TextInputEditText classTiEdit = viewBottomDialog.findViewById(R.id.student_class_now_edit);
        TextInputEditText ageTiEdit = viewBottomDialog.findViewById(R.id.age_student_edit);
        AutoCompleteTextView autoCompleteGender = viewBottomDialog.findViewById(R.id.auto_complete_gender);
        AutoCompleteTextView autoCompleteDiagnosa = viewBottomDialog.findViewById(R.id.auto_complete_diagnosa);
        Button finishEditBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        nameTiEdit.setText(modelStudent.getName());
        nisnTiEdit.setText(modelStudent.getNisn());
        classTiEdit.setText(modelStudent.getClass_now());
        ageTiEdit.setText(modelStudent.getAge());

        String[] gender = {"Laki-laki","Perempuan"};
        ArrayAdapter<String> genderAdapter;
        genderAdapter = new ArrayAdapter<>(mContext, R.layout.layout_option_item, gender);
        autoCompleteGender.setAdapter(genderAdapter);

        String[] diagnosa = {"Keterbelakangan Mental","Hyperaktif", "Speech Delay"};
        ArrayAdapter<String> diagnosaAdapter;
        diagnosaAdapter = new ArrayAdapter<>(mContext, R.layout.layout_option_item, diagnosa);
        autoCompleteDiagnosa.setAdapter(diagnosaAdapter);

        finishEditBtn.setOnClickListener(view -> {
            String name = nameTiEdit.getText().toString();
            String nisn = nisnTiEdit.getText().toString();
            String classStudent = classTiEdit.getText().toString();
            String age = ageTiEdit.getText().toString();
            String gender1 = autoCompleteGender.getText().toString();
            String diagnosa1 = autoCompleteDiagnosa.getText().toString();

            if (name.isEmpty()){
                nameTiEdit.setError("Nama kosong");
            }else if (nisn.isEmpty()){
                nisnTiEdit.setError("NISN kosong");
            }else if (classStudent.isEmpty()){
                classTiEdit.setError("Kelas siswa kosong");
            }else if (age.isEmpty()){
                ageTiEdit.setError("Umur kosong");
            }else if (gender1.isEmpty()){
                autoCompleteGender.setError("Jenis kelamin kosong");
            }else if (diagnosa1.isEmpty()){
                autoCompleteDiagnosa.setError("Diagnosa kosong");
            }else {
                changeDataStudent(name, nisn, classStudent, age, gender1, diagnosa1, modelStudent.getKey());
            }

            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void changeDataStudent(String name, String nisn, String classStudent, String age, String gender, String diagnosa, String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ModelStudent modelStudent = new ModelStudent(
            name,
            nisn,
            classStudent,
            age,
            gender,
            diagnosa
        );

        reference.child("student").child(key).setValue(modelStudent).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.toastLS(mContext, "Berhasil mengubah data");
            }else {
                Utility.toastLS(mContext, task.getException().getMessage());
            }
        });
    }

    private void deleteStudent(String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").child(key).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.toastLS(mContext, "Data berhasil dihapus");
            }else {
                Utility.toastLS(mContext, "Data gagal dihapus");
            }
        });

        reference.child("student_in_class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot : ds.getChildren()){
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            ModelStudentInClass modelStudentInClass = dataSnapshot1.getValue(ModelStudentInClass.class);
                            if (modelStudentInClass.getKey_student().equals(key)){
                                dataSnapshot1.getRef().removeValue();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(mContext, "Database : "+error);
            }
        });
    }

}

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

import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.Model.ModelYearSchool;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Administrator.ManageStudentClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterManageStudentClass extends RecyclerView.Adapter<AdapterManageStudentClass.MyViewHolder> {

    private Context mContext;
    private List<ModelStudentClass> listStudentClass;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private BottomSheetDialog bottomSheetDialog;

    public AdapterManageStudentClass(Context mContext, List<ModelStudentClass> listStudentClass) {
        this.mContext = mContext;
        this.listStudentClass = listStudentClass;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_manage_student_class, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelStudentClass modelStudentClass = listStudentClass.get(position);

        // set image icon
        if (modelStudentClass.getStudent_class().equals("6")){
            holder.viewIconClass.setImageResource(R.drawable.icon_class_6);
        }
        if (modelStudentClass.getStudent_class().equals("5")){
            holder.viewIconClass.setImageResource(R.drawable.icon_class_5);
        }
        if (modelStudentClass.getStudent_class().equals("4")){
            holder.viewIconClass.setImageResource(R.drawable.icon_class_4);
        }
        if (modelStudentClass.getStudent_class().equals("3")){
            holder.viewIconClass.setImageResource(R.drawable.icon_class_3);
        }
        if (modelStudentClass.getStudent_class().equals("2")){
            holder.viewIconClass.setImageResource(R.drawable.icon_class_2);
        }
        if (modelStudentClass.getStudent_class().equals("1")){
            holder.viewIconClass.setImageResource(R.drawable.icon_class_2);
        }

        holder.subClass.setText("Kelas : "+modelStudentClass.getStudent_class()+modelStudentClass.getSub_student_class());
        holder.yearSchool.setText("TA : "+modelStudentClass.getYear_school());

        bottomSheetDialog = new BottomSheetDialog(mContext);
        setEditBottomDialog(modelStudentClass);

        holder.cardEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        holder.cardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the Dialog here
                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.custom_dialog_confirmation_year_school);
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
                    deleteDataStudentClass(modelStudentClass);
                    dialog.dismiss();
                });

                Cancel.setOnClickListener(v -> dialog.dismiss());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listStudentClass.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subClass, yearSchool;
        CardView cardEdit, cardDelete;
        ImageView viewIconClass;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subClass = itemView.findViewById(R.id.sub_class_student);
            yearSchool = itemView.findViewById(R.id.ta_student_class);
            cardEdit = itemView.findViewById(R.id.card_edit_student_class);
            cardDelete = itemView.findViewById(R.id.card_delete_student_class);
            viewIconClass = itemView.findViewById(R.id.icon_class_manage);
        }
    }

    private void setEditBottomDialog(ModelStudentClass modelStudentClass){
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewBottomDialog = li.inflate(R.layout.layout_dialog_edit_class, null, false);
        AutoCompleteTextView autoCompleteYear = viewBottomDialog.findViewById(R.id.auto_complete_choice_year);
        TextInputEditText subClassEditTi = viewBottomDialog.findViewById(R.id.sub_class_ti_edit);
        Button finishAddBtn = viewBottomDialog.findViewById(R.id.finish_add_btn);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(mContext, R.layout.layout_option_item);
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
                Utility.toastLS(mContext, error.getMessage());
            }
        });

        subClassEditTi.setText(modelStudentClass.getSub_student_class());

        finishAddBtn.setOnClickListener(view -> {
            String yearSchool = autoCompleteYear.getText().toString();
            String sub_class = subClassEditTi.getText().toString();

            if (yearSchool.isEmpty()){
                autoCompleteYear.setError("Pilih tahun ajaran");
            }else if (sub_class.isEmpty()){
                subClassEditTi.setError("Sub kelas kosong");
            }else {
                changeDataClass(modelStudentClass, sub_class, yearSchool);
            }
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void changeDataClass(ModelStudentClass modelStudentClass, String newSubClass, String newYear){
        ModelStudentClass studentClass = new ModelStudentClass(
            modelStudentClass.getStudent_class(),
            newSubClass,
            newYear
        );
        reference.child("class")
                .child(modelStudentClass.getStudent_class())
                .child(modelStudentClass.getKey()).setValue(studentClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.toastLS(mContext, "Data berhasil diubah");
                }else {
                    Utility.toastLS(mContext, task.getException().getMessage());
                }
            }
        });
    }

    private void deleteDataStudentClass(ModelStudentClass modelStudentClass){
        reference.child("class")
                .child(modelStudentClass.getStudent_class())
                .child(modelStudentClass.getKey())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.toastLS(mContext, "Data berhasil dihapus");
                }else {
                    Utility.toastLS(mContext, "Gagal menghapus data");
                }
            }
        });
    }
}

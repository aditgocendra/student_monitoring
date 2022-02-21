package com.ark.studentmonitoring.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ark.studentmonitoring.View.User.Teacher.AddStudentMyClass;
import com.ark.studentmonitoring.View.User.Teacher.ManageMyClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterAddStudentMyClass extends RecyclerView.Adapter<AdapterAddStudentMyClass.MyViewHolder> {

    private Context mContext;
    private List<ModelStudent> listStudent;
    private String classStudent;
    private String keyClass;
    private String subClass;

    public AdapterAddStudentMyClass(Context mContext, List<ModelStudent> listStudent, String classStudent, String keyClass, String subClass) {
        this.mContext = mContext;
        this.listStudent = listStudent;
        this.classStudent = classStudent;
        this.keyClass = keyClass;
        this.subClass = subClass;
    }

    @NonNull
    @Override
    public AdapterAddStudentMyClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_student_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAddStudentMyClass.MyViewHolder holder, int position) {
        ModelStudent modelStudent = listStudent.get(position);
        if (modelStudent.getGender().equals("Laki-laki")){
            holder.iconImage.setImageResource(R.drawable.man_student);
        }else {
            holder.iconImage.setImageResource(R.drawable.women_student);
        }
        holder.textName.setText("Nama : "+Utility.capitalizeWord(modelStudent.getName()));
        holder.textNisn.setText("NISN : "+modelStudent.getNisn());

        holder.cardStudent.setOnClickListener(view -> {
            //Create the Dialog here
            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.custom_dialog_add_student_my_class);
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
                addStudentInClass(modelStudent.getKey());
                dialog.dismiss();
            });

            Cancel.setOnClickListener(v -> dialog.dismiss());
        });
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textNisn;
        ImageView iconImage;
        CardView cardStudent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.name_child_text);
            textNisn = itemView.findViewById(R.id.nisn_child_text);
            iconImage = itemView.findViewById(R.id.icon_image);
            cardStudent = itemView.findViewById(R.id.card_student);
        }
    }

    private void addStudentInClass(String keyStudent) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ModelStudentInClass modelStudentInClass = new ModelStudentInClass(
                keyStudent
        );

//        Log.d("test", classStudent + " / "+ keyClass + " / "+ keyStudent);
        reference
                .child("student_in_class")
                .child(classStudent)
                .child(keyClass)
                .child(keyStudent)
                .setValue(modelStudentInClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Utility.toastLS(mContext, "Siswa berhasil ditambahkan");
                        Utility.updateUI(mContext, ManageMyClass.class);
                        ((Activity)mContext).finish();
                    }else {
                        Utility.toastLS(mContext, task.getException().getMessage());
                    }
                });
    }


}

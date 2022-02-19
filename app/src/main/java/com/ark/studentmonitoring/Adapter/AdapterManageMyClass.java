package com.ark.studentmonitoring.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Teacher.AddStudentMyClass;
import com.ark.studentmonitoring.View.User.Teacher.SeeStudentMyClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterManageMyClass extends RecyclerView.Adapter<AdapterManageMyClass.MyViewHolder> {

    private Context mContext;
    private List<ModelStudentClass> listModelStudentClass;

    public AdapterManageMyClass(Context mContext, List<ModelStudentClass> listModelStudentClass) {
        this.mContext = mContext;
        this.listModelStudentClass = listModelStudentClass;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_manage_my_class, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelStudentClass modelStudentClass = listModelStudentClass.get(position);
        if (modelStudentClass.getStudent_class().equals("1")){
            holder.iconClass.setImageResource(R.drawable.icon_class_1);
        }
        if (modelStudentClass.getStudent_class().equals("2")){
            holder.iconClass.setImageResource(R.drawable.icon_class_2);
        }
        if (modelStudentClass.getStudent_class().equals("3")){
            holder.iconClass.setImageResource(R.drawable.icon_class_3);
        }
        if (modelStudentClass.getStudent_class().equals("4")){
            holder.iconClass.setImageResource(R.drawable.icon_class_4);
        }
        if (modelStudentClass.getStudent_class().equals("5")){
            holder.iconClass.setImageResource(R.drawable.icon_class_5);
        }
        if (modelStudentClass.getStudent_class().equals("6")){
            holder.iconClass.setImageResource(R.drawable.icon_class_6);
        }
        holder.subClass.setText("Kelas : "+modelStudentClass.getStudent_class()+modelStudentClass.getSub_student_class());
        holder.yearSchoolClass.setText("TA : "+modelStudentClass.getYear_school());

        holder.cardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    deleteMyClassStudent(modelStudentClass);
                    dialog.dismiss();
                });

                Cancel.setOnClickListener(v -> dialog.dismiss());
            }
        });

        holder.cardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddStudentMyClass.class);
                intent.putExtra("class", modelStudentClass.getStudent_class());
                intent.putExtra("key_class", modelStudentClass.getKey());
                mContext.startActivity(intent);
            }
        });

        holder.cardSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SeeStudentMyClass.class);
                intent.putExtra("class", modelStudentClass.getStudent_class());
                intent.putExtra("key_class", modelStudentClass.getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listModelStudentClass.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iconClass;
        TextView subClass,yearSchoolClass;
        CardView cardDelete, cardSee, cardAdd;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iconClass = itemView.findViewById(R.id.icon_class_my_class);
            subClass = itemView.findViewById(R.id.sub_class_student);
            yearSchoolClass = itemView.findViewById(R.id.ta_student_class);
            cardDelete = itemView.findViewById(R.id.card_delete_my_class);
            cardSee = itemView.findViewById(R.id.card_see_myclass);
            cardAdd = itemView.findViewById(R.id.card_add_student_my_class);
        }
    }

    private void deleteMyClassStudent(ModelStudentClass studentClass) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ModelStudentClass modelStudentClass = new ModelStudentClass(
                studentClass.getStudent_class(),
                studentClass.getSub_student_class(),
                studentClass.getYear_school(),
                "-"
        );
        reference
                .child("class")
                .child(studentClass.getStudent_class())
                .child(studentClass.getKey())
                .setValue(modelStudentClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Utility.toastLS(mContext, "Berhasil menghapus kelas");
                    }else {
                        Utility.toastLS(mContext, task.getException().getMessage());
                    }
                });
    }
}

package com.ark.studentmonitoring.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.ark.studentmonitoring.Model.ModelValueStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterSeeStudentMyClass extends RecyclerView.Adapter<AdapterSeeStudentMyClass.MyViewHolder> {

    private Context mContext;
    private List<ModelStudent> lisStudent;
    private String classStudent;
    private String keyClass;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public AdapterSeeStudentMyClass(Context mContext, List<ModelStudent> lisStudent, String classStudent, String keyClass) {
        this.mContext = mContext;
        this.lisStudent = lisStudent;
        this.classStudent = classStudent;
        this.keyClass = keyClass;
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

        ModelStudent modelStudent = lisStudent.get(position);
        holder.cardEdit.setVisibility(View.GONE);

        if (modelStudent.getGender().equals("Laki-laki")){
            holder.iconStudent.setImageResource(R.drawable.man_student);
        }else {
            holder.iconStudent.setImageResource(R.drawable.women_student);
        }

        holder.nameText.setText("Name : "+ Utility.capitalizeWord(modelStudent.getName()));
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
                deleteStudentInClass(modelStudent.getKey());
                dialog.dismiss();
            });

            Cancel.setOnClickListener(v -> dialog.dismiss());
        });
    }

    @Override
    public int getItemCount() {
        return lisStudent.size();
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

    private void deleteStudentInClass(String key) {
        reference.child("student_in_class").child(classStudent).child(keyClass).child(key).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                removeValueStudent(key);
                Utility.toastLS(mContext, "Data siswa di kelas ini berhasil dihapus");
            }else {
                Utility.toastLS(mContext, task.getException().getMessage());
            }
        });
    }

    private void removeValueStudent(String key){
        reference.child("value_student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("test", key+ " / "+ ds.getKey());
                    if (ds.getKey().equals(key)){
                        for (DataSnapshot ds1 :ds.getChildren()){
                            for (DataSnapshot ds2 : ds1.getChildren()){
                                ModelValueStudent modelValueStudent = ds2.getValue(ModelValueStudent.class);
                                if (modelValueStudent != null){
                                    ds2.getRef().removeValue();
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(mContext, "Database :"+error.getMessage());
            }
        });

        ((Activity)mContext).finish();
    }
}

package com.ark.studentmonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterStudentList extends RecyclerView.Adapter<AdapterStudentList.MyViewHolder> {

    private Context mContext;
    private List<ModelStudentInClass> listStudentInClassList;

    public AdapterStudentList(Context mContext, List<ModelStudentInClass> listStudentInClassList) {
        this.mContext = mContext;
        this.listStudentInClassList = listStudentInClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_student_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelStudentInClass modelStudentInClass = listStudentInClassList.get(position);

        setDataStudent(modelStudentInClass, holder);
    }



    @Override
    public int getItemCount() {
        return listStudentInClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView nameStudent, nisnStudent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImage = itemView.findViewById(R.id.icon_image);
            nameStudent = itemView.findViewById(R.id.name_child_text);
            nisnStudent = itemView.findViewById(R.id.nisn_child_text);

        }
    }

    private void setDataStudent(ModelStudentInClass modelStudentInClass, MyViewHolder holder) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("student").child(modelStudentInClass.getKey_student()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    ModelStudent modelStudent = task.getResult().getValue(ModelStudent.class);
                    modelStudent.setKey(task.getResult().getKey());

                    if (modelStudent.getGender().equals("Laki-laki")){
                        holder.iconImage.setImageResource(R.drawable.man_student);
                    }else {
                        holder.iconImage.setImageResource(R.drawable.women_student);
                    }

                    holder.nameStudent.setText("Nama : "+Utility.capitalizeWord(modelStudent.getName()));
                    holder.nisnStudent.setText("Kelas : "+modelStudent.getNisn());

                }else {
                    Utility.toastLS(mContext, task.getException().getMessage());
                }
            }
        });
    }
}

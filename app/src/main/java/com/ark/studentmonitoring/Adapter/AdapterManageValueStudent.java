package com.ark.studentmonitoring.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.View.User.Teacher.SeeStudentMyClass;
import com.ark.studentmonitoring.View.User.Teacher.StudentChoice;

import java.util.List;


public class AdapterManageValueStudent extends RecyclerView.Adapter<AdapterManageValueStudent.MyViewHolder> {

    private Context mContext;
    private List<ModelStudentClass> listStudentClass;

    public AdapterManageValueStudent(Context mContext, List<ModelStudentClass> listStudentClass) {
        this.mContext = mContext;
        this.listStudentClass = listStudentClass;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_manage_value_student, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelStudentClass modelStudentClass = listStudentClass.get(position);
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
        holder.taStudent.setText("TA : "+modelStudentClass.getYear_school());

        holder.cardClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentChoice.class);
                intent.putExtra("class", modelStudentClass.getStudent_class());
                intent.putExtra("key_class", modelStudentClass.getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStudentClass.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardClass;
        ImageView iconClass;
        TextView subClass, taStudent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardClass = itemView.findViewById(R.id.card_class);
            iconClass = itemView.findViewById(R.id.icon_class);
            subClass = itemView.findViewById(R.id.sub_class);
            taStudent = itemView.findViewById(R.id.ta_student_class);
        }
    }
}

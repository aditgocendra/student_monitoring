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

import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.View.User.Teacher.ValueStudent;

import java.util.List;

public class AdapterStudentChoice extends RecyclerView.Adapter<AdapterStudentChoice.MyViewHolder> {

    private Context mContext;
    private List<ModelStudent> listStudentInClass;
    private String classStudent;
    private String keyClass;

    public AdapterStudentChoice(Context mContext, List<ModelStudent> listStudentInClass, String classStudent, String keyClass) {
        this.mContext = mContext;
        this.listStudentInClass = listStudentInClass;
        this.classStudent = classStudent;
        this.keyClass = keyClass;
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
        ModelStudent modelStudent = listStudentInClass.get(position);

        if (modelStudent.getGender().equals("Laki-laki")){
            holder.iconImage.setImageResource(R.drawable.man_student);
        }else {
            holder.iconImage.setImageResource(R.drawable.women_student);
        }

        holder.textName.setText(modelStudent.getName());
        holder.textNisn.setText(modelStudent.getNisn());

        holder.cardStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ValueStudent.class);
                intent.putExtra("class", classStudent);
                intent.putExtra("key_class", keyClass);
                intent.putExtra("key_student", modelStudent.getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStudentInClass.size();
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
}

package com.ark.studentmonitoring.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Parent.ChildValue;
import com.ark.studentmonitoring.View.User.Teacher.ValueStudent;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;

public class AdapterStudentChoice extends RecyclerView.Adapter<AdapterStudentChoice.MyViewHolder> {

    private Context mContext;
    private List<ModelStudent> listStudentInClass;
    private String classStudent;
    private String subClassStudent;
    private String keyClass;

    public AdapterStudentChoice(Context mContext, List<ModelStudent> listStudentInClass, String classStudent, String subClassStudent, String keyClass) {
        this.mContext = mContext;
        this.listStudentInClass = listStudentInClass;
        this.classStudent = classStudent;
        this.subClassStudent = subClassStudent;
        this.keyClass = keyClass;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_manage_student_choice, parent, false);
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

        holder.textName.setText("Nama : "+Utility.capitalizeWord(modelStudent.getName()));
        holder.textNisn.setText("NISN : "+modelStudent.getNisn());

        holder.cardAddValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ValueStudent.class);
                intent.putExtra("class", classStudent);
                intent.putExtra("sub_class", subClassStudent);
                intent.putExtra("key_class", keyClass);
                intent.putExtra("key_student", modelStudent.getKey());
                mContext.startActivity(intent);
            }
        });

        holder.cardEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChildValue.class);
                intent.putExtra("key_student", modelStudent.getKey());
                intent.putExtra("key_class", keyClass);
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
        CardView cardEye, cardAddValue;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.name_child_text);
            textNisn = itemView.findViewById(R.id.nisn_child_text);
            iconImage = itemView.findViewById(R.id.icon_student);
            cardAddValue = itemView.findViewById(R.id.card_add_data_value);
            cardEye = itemView.findViewById(R.id.card_see_value_student);
        }
    }


}

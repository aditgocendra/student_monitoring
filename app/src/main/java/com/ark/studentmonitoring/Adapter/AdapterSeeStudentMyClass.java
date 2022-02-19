package com.ark.studentmonitoring.Adapter;

import android.content.Context;
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

import java.util.List;

public class AdapterSeeStudentMyClass extends RecyclerView.Adapter<AdapterSeeStudentMyClass.MyViewHolder> {

    private Context mContext;
    private List<ModelStudent> listStudentInClass;

    public AdapterSeeStudentMyClass(Context mContext, List<ModelStudent> listStudentInClass) {
        this.mContext = mContext;
        this.listStudentInClass = listStudentInClass;
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
        ModelStudent modelStudent = listStudentInClass.get(position);
        holder.cardEdit.setVisibility(View.GONE);

        if (modelStudent.getGender().equals("Laki-laki")){
            holder.iconStudent.setImageResource(R.drawable.man_student);
        }else {
            holder.iconStudent.setImageResource(R.drawable.women_student);
        }

        holder.nameText.setText("Name : "+ modelStudent.getName());
        holder.nisnText.setText("NISN : "+modelStudent.getNisn());

    }

    @Override
    public int getItemCount() {
        return listStudentInClass.size();
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
}

package com.ark.studentmonitoring.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.studentmonitoring.Model.ModelYearSchool;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterManageYearSchool extends RecyclerView.Adapter<AdapterManageYearSchool.MyViewHolder> {

    private Context mContext;
    private List<ModelYearSchool> listYearSchool;
    private BottomSheetDialog bottomSheetDialog;

    public AdapterManageYearSchool(Context mContext, List<ModelYearSchool> listYearSchool){
        this.mContext = mContext;
        this.listYearSchool = listYearSchool;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_manage_year_school, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelYearSchool modelYearSchool = listYearSchool.get(position);

        holder.textYear.setText("Tahun Ajaran "+modelYearSchool.getFrom_year()+" / "+modelYearSchool.getTo_year());

        holder.cardDelete.setOnClickListener(view -> {
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
                deleteDataYearSchool(modelYearSchool.getKey());
                dialog.dismiss();
            });

            Cancel.setOnClickListener(v -> dialog.dismiss());
        });

        holder.cardEdit.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(mContext);
            setEditBottomDialog(modelYearSchool);
            bottomSheetDialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return listYearSchool.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textYear;
        CardView cardEdit, cardDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textYear = itemView.findViewById(R.id.year_school_text);
            cardEdit = itemView.findViewById(R.id.card_edit_year_school);
            cardDelete = itemView.findViewById(R.id.card_delete_year_school);
        }
    }

    private void setEditBottomDialog(ModelYearSchool modelYearSchool){
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewBottomDialog = li.inflate(R.layout.layout_dialog_edit_year_school, null, false);
        TextInputEditText yearSchoolTiEditFrom = viewBottomDialog.findViewById(R.id.year_school_ti_edit_from);
        TextInputEditText yearSchoolTiEditTo = viewBottomDialog.findViewById(R.id.year_school_ti_edit_to);
        Button finishEditBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        yearSchoolTiEditFrom.setText(modelYearSchool.getFrom_year());
        yearSchoolTiEditTo.setText(modelYearSchool.getTo_year());

        finishEditBtn.setOnClickListener(view -> {
            String new_year_school_from = yearSchoolTiEditFrom.getText().toString();
            String new_year_school_to = yearSchoolTiEditTo.getText().toString();

            if (new_year_school_from.isEmpty()){
                yearSchoolTiEditFrom.setError("Tahun ajaran mulai kosong");
            }else if (new_year_school_to.isEmpty()){
                yearSchoolTiEditTo.setError("Tahun ajaran sampai kosong");
            }
            else {
                changeDataYearSchool(modelYearSchool, new_year_school_from, new_year_school_to);
            }
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void changeDataYearSchool(ModelYearSchool modelYearSchool, String yearFrom, String yearTo){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ModelYearSchool yearSchool = new ModelYearSchool(
                yearFrom,
                yearTo
        );

        reference.child("year_school").child(modelYearSchool.getKey()).setValue(yearSchool).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.toastLS(mContext, "Tahun ajaran berhasil diubah");
            }else {
                Utility.toastLS(mContext, "Tahun ajaran gagal diubah");
            }
        });
    }

    private void deleteDataYearSchool(String key){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("year_school").child(key).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.toastLS(mContext, "Berhasil menghapus data");
            }else {
                Utility.toastLS(mContext, "Data gagal dihapus");
            }
        });

    }


}

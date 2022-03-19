package com.ark.studentmonitoring.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelStudentClass;
import com.ark.studentmonitoring.Model.ModelStudentInClass;
import com.ark.studentmonitoring.Model.ModelValueStudent;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Chatting.PersonalChat;
import com.ark.studentmonitoring.View.User.Parent.ChildValue;
import com.ark.studentmonitoring.View.User.Parent.StudentList;
import com.ark.studentmonitoring.View.User.Teacher.ValueStudent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class AdapterChildValue extends RecyclerView.Adapter<AdapterChildValue.MyViewHolder> {

    private Context mContext;
    private List<ModelValueStudent> listValueStudents;
    private String keyStudent;
    private String keyClass;
    private String studentClass;


    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private BottomSheetDialog bottomSheetDialog;

    public AdapterChildValue(Context mContext, List<ModelValueStudent> listValueStudents, String keyStudent, String keyClass, String studentClass) {
        this.mContext = mContext;
        this.listValueStudents = listValueStudents;
        this.keyStudent = keyStudent;
        this.keyClass = keyClass;
        this.studentClass = studentClass;
    }

    @NonNull
    @Override
    public AdapterChildValue.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_child_value, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChildValue.MyViewHolder holder, int position) {
        ModelValueStudent modelValueStudent = listValueStudents.get(position);
        holder.classStudent.setText("Kelas : "+modelValueStudent.getClasses());
        holder.valueStudent.setText(": "+modelValueStudent.getValue());
        holder.dateValue.setText(modelValueStudent.getDate());
        holder.desc.setText(": "+modelValueStudent.getDescription());

        if (Utility.roleCurrentUser.equals("parent")){
            holder.cardEdit.setVisibility(View.GONE);
            holder.cardDelete.setVisibility(View.GONE);
        }

        holder.cardChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.roleCurrentUser.equals("parent")){
                    chatToTeacher();
                }else {
                    chatToParent();
                }
            }
        });


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
                deleteValueStudent(modelValueStudent.getKey());
                dialog.dismiss();
            });

            Cancel.setOnClickListener(v -> dialog.dismiss());
        });

        holder.cardEdit.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(mContext);
            setBottomDialog(modelValueStudent);
            bottomSheetDialog.show();
        });

    }



    @Override
    public int getItemCount() {
        return listValueStudents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView classStudent, dateValue, valueStudent, desc;
        CardView cardEdit, cardDelete, cardChat;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            classStudent = itemView.findViewById(R.id.class_student);
            valueStudent = itemView.findViewById(R.id.value_student);
            dateValue = itemView.findViewById(R.id.date_value);
            desc = itemView.findViewById(R.id.description_value);
            cardChat = itemView.findViewById(R.id.card_chat_btn);
            cardEdit = itemView.findViewById(R.id.card_edit_value_student);
            cardDelete = itemView.findViewById(R.id.card_delete_value_student);
        }
    }

    private void deleteValueStudent(String keyValue) {
        reference.child("value_student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    for (DataSnapshot ds1 : ds.getChildren()){
                        for (DataSnapshot ds2 : ds1.getChildren()){
                            if(ds2.getKey().equals(keyValue)){
                                ds2.getRef().removeValue();

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
    }

    private void setBottomDialog(ModelValueStudent modelValueStudent) {
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewBottomDialog = li.inflate(R.layout.layout_dialog_edit_value, null, false);
        AutoCompleteTextView autoCompleteValueSikap = viewBottomDialog.findViewById(R.id.auto_complete_value_sikap);
        TextInputEditText descValue = viewBottomDialog.findViewById(R.id.description_value);
        TextView classStudent = viewBottomDialog.findViewById(R.id.class_student);
        TextView datePick = viewBottomDialog.findViewById(R.id.date_pick);
        Button finishBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        String[] choiceValue = {"A","B","C","D"};
        ArrayAdapter<String> choiceAdapter;
        choiceAdapter = new ArrayAdapter<>(mContext, R.layout.layout_option_item, choiceValue);
        autoCompleteValueSikap.setAdapter(choiceAdapter);

        classStudent.setText("Kelas : "+modelValueStudent.getClasses());
        datePick.setText(modelValueStudent.getDate());
        descValue.setText(modelValueStudent.getDescription());

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        MaterialDatePicker<Long> materialDatePicker = builder.build();

        datePick.setOnClickListener(view -> materialDatePicker.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Date Picker"));


        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            datePick.setText(materialDatePicker.getHeaderText());

        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = datePick.getText().toString();
                String value = autoCompleteValueSikap.getText().toString();
                String desc = descValue.getText().toString();
                String classes = modelValueStudent.getClasses();
                String keyValue = modelValueStudent.getKey();

                if (date.equals("Pilih Tanggal")){
                    Utility.toastLS(mContext, "Anda belum memilih tanggal");
                }else if (value.isEmpty()){
                    Utility.toastLS(mContext, "Anda belum memilih nilai");
                }else if (desc.isEmpty()){
                    Utility.toastLS(mContext, "Anda belum mengisi deskripsi");
                }else {
                    changeDataValue(date, value, desc, classes, keyValue);
                }


                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void changeDataValue(String date, String value, String description, String classes, String keyValue) {
        ModelValueStudent newModelValueStudent = new ModelValueStudent(
                date,
                value,
                description,
                classes
        );

        reference.child("value_student")
                .child(keyStudent)  // key student
                .child(keyClass)    // key class
                .child(keyValue)            // key value
                .setValue(newModelValueStudent).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.toastLS(mContext, "Berhasil merubah nilai siswa");
            }else {
                Utility.toastLS(mContext, task.getException().getMessage());

            }
        });
    }

    private void chatToTeacher(){
        reference.child("class").child(studentClass).child(keyClass).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelStudentClass modelStudentClass = task.getResult().getValue(ModelStudentClass.class);
                if (modelStudentClass != null){
                    Intent intent = new Intent(mContext, PersonalChat.class);
                    intent.putExtra("key", modelStudentClass.getTeacher());
                    mContext.startActivity(intent);
                }else {
                    Toast.makeText(mContext, "Data guru kosong", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chatToParent() {
        reference.child("user_detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    String keyStudentParent = ds.child("key_student").getValue(String.class);
                    if (keyStudentParent != null){
                        if (keyStudentParent.equals(keyStudent)){
                            Intent intent = new Intent(mContext, PersonalChat.class);
                            intent.putExtra("key", ds.getKey());
                            mContext.startActivity(intent);
                            i +=1;
                            break;
                        }
                    }
                }

                if (i == 0){
                    Toast.makeText(mContext, "Data orang tua pada anak ini, belum ada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

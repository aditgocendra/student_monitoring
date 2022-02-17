package com.ark.studentmonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelUser;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterManageAccount extends RecyclerView.Adapter<AdapterManageAccount.MyViewHolder> {

    private Context mContext;
    private List<ModelUser> userList;
    private BottomSheetDialog bottomSheetDialog;

    public AdapterManageAccount(Context mContext, List<ModelUser> userList){
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdapterManageAccount.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_manage_account, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterManageAccount.MyViewHolder holder, int position) {
        ModelUser modelUser = userList.get(position);

        if (modelUser.getRole().equals("teacher")){
            holder.iconUser.setImageResource(R.drawable.default_teacher_photo);
            holder.roleAccount.setText("Peran : Guru");
        }else {
            holder.iconUser.setImageResource(R.drawable.default_parent_photo);
            holder.roleAccount.setText("Peran : Orang Tua");
        }
        holder.usernameAccount.setText(modelUser.getUsername());


        holder.cardEdit.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(mContext);
            setBottomDialog(modelUser);
            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iconUser;
        TextView usernameAccount, roleAccount;
        CardView cardEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iconUser = itemView.findViewById(R.id.icon_user);
            usernameAccount = itemView.findViewById(R.id.username_account);
            cardEdit = itemView.findViewById(R.id.card_edit_account);
            roleAccount = itemView.findViewById(R.id.role_account_text);
        }
    }

    private void setBottomDialog(ModelUser modelUser){
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewBottomDialog = li.inflate(R.layout.layout_dialog_edit_account, null, false);
        AutoCompleteTextView autoCompleteRole = viewBottomDialog.findViewById(R.id.auto_complete_role);
        Button finishEditBtn = viewBottomDialog.findViewById(R.id.finish_edit_btn);

        String[] role = {"Guru", "Orang Tua"};
        ArrayAdapter<String> accountAdapter;
        accountAdapter = new ArrayAdapter<>(mContext, R.layout.layout_option_item, role);
        autoCompleteRole.setAdapter(accountAdapter);

        finishEditBtn.setOnClickListener(view -> {
            if (!autoCompleteRole.getText().toString().isEmpty()){
                changeRoleAccount(modelUser, autoCompleteRole.getText().toString());
                bottomSheetDialog.dismiss();
            }else {
                Utility.toastLS(mContext, "Anda belum memilih role");
            }
        });

        bottomSheetDialog.setContentView(viewBottomDialog);
    }

    private void changeRoleAccount(ModelUser modelUser, String newRole) {
        if (newRole.equals("Guru") && modelUser.getRole().equals("teacher") ||
                newRole.equals("Orang Tua") && modelUser.getRole().equals("parent")){
            Utility.toastLS(mContext, "Peran yang anda pilih sama dengan peran sebelumnya");
        }else {
            String role;
            if (newRole.equals("Guru")){
                role = "teacher";
            }else {
                role = "parent";
            }

            ModelUser newModelUser = new ModelUser(
                    modelUser.getUsername(),
                    modelUser.getEmail(),
                    role,
                    modelUser.getPhone_number(),
                    modelUser.getAddress()
            );

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("users").child(modelUser.getKey()).setValue(newModelUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Utility.toastLS(mContext, "Berhasil mengubah data");
                }else {
                    Utility.toastLS(mContext, task.getException().getMessage());
                }
            });
        }
    }
}

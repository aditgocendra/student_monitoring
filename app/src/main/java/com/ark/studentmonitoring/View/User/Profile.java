package com.ark.studentmonitoring.View.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Model.ModelParent;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelTeacher;
import com.ark.studentmonitoring.Model.ModelUser;
import com.ark.studentmonitoring.NetworkChangeListener;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.Auth.SignIn;
import com.ark.studentmonitoring.View.User.Administrator.DashboardAdmin;
import com.ark.studentmonitoring.View.User.Teacher.DashboardTeacher;
import com.ark.studentmonitoring.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        setDataUser();
        setLayoutRole();
        listenerClick();

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void setLayoutRole() {

        if (Utility.roleCurrentUser.equals("admin")){
            binding.administratorBtn.setVisibility(View.VISIBLE);
        }

        if (Utility.roleCurrentUser.equals("teacher")){
            binding.teacherBtn.setVisibility(View.VISIBLE);
            binding.cardFeatureRollTeacher.setVisibility(View.VISIBLE);
            binding.profileImg.setImageResource(R.drawable.default_teacher_photo);
            setDataTeacher();
        }

        if (Utility.roleCurrentUser.equals("parent")){
            binding.cardFeatureRollParent.setVisibility(View.VISIBLE);
            binding.profileImg.setImageResource(R.drawable.default_parent_photo);
            setDataChildParent();
        }
    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            Utility.updateUI(Profile.this, HomeApp.class);
            finish();
        });

        binding.logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Utility.updateUI(Profile.this, SignIn.class);
            finish();
        });

        binding.administratorBtn.setOnClickListener(view -> {
            Utility.updateUI(Profile.this, DashboardAdmin.class);
        });

        binding.teacherBtn.setOnClickListener(view -> Utility.updateUI(Profile.this, DashboardTeacher.class));

        binding.aboutBtn.setOnClickListener(view -> Utility.updateUI(Profile.this, About.class));

        binding.editDataProfileBtn.setOnClickListener(view -> {
            String email = binding.emailEditTi.getText().toString();
            String username = binding.usernameEditTi.getText().toString();
            String phone_number = binding.phoneNumberEditTi.getText().toString();
            String address = binding.streetProfileTi.getText().toString();

            if (email.isEmpty()){
                binding.emailEditTi.setError("Email tidak boleh kosong");
            }else if (username.isEmpty()){
                binding.usernameEditTi.setError("Username tidak boleh kosong");
            }else if (phone_number.isEmpty()){
                binding.phoneNumberEditTi.setError("Nomor telepon tidak boleh kosong");
            }else if (address.isEmpty()){
                binding.streetProfileTi.setError("Alamat tidak boleh kosong");
            }else {
                binding.progressCircular.setVisibility(View.VISIBLE);
                binding.progressCircular.setEnabled(false);
                saveDataProfile(email, username, phone_number, address);
            }
        });

        binding.editDataTeacherBtn.setOnClickListener(view -> {
            String full_name = binding.fullnameTeacherTi.getText().toString();
            String nip = binding.nipGuruTi.getText().toString();

            if (full_name.isEmpty()){
                binding.fullnameTeacherTi.setError("Nama lengkap harus diisi");
            }else if (nip.isEmpty()){
                binding.nipGuruTi.setError("Nip harus diisi");
            }else {
                if (Utility.roleCurrentUser.equals("teacher")){
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    binding.progressCircular.setEnabled(false);
                    saveDataTeacher(full_name, nip);
                }else {
                    Utility.toastLS(Profile.this, "Anda bukan guru");
                }
            }
        });

        binding.editDataParentChild.setOnClickListener(view -> {
            String nisn = binding.nisnEditTi.getText().toString();

            if (nisn.isEmpty()){
                binding.nisnEditTi.setError("nisn tidak boleh kosong");
            }else {
                if (Utility.roleCurrentUser.equals("parent")){
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    binding.progressCircular.setEnabled(false);
                    checkRegNumber(nisn);
                }else {
                    Utility.toastLS(Profile.this, "Anda bukan wali murid");
                }
            }
        });
    }

    private void saveDataProfile(String email, String username, String phone_number, String address) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ModelUser modelUser = new ModelUser(
                username,
                email,
                Utility.roleCurrentUser,
                phone_number,
                address
        );

        reference.child("users").child(Utility.uidCurrentUser).setValue(modelUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                binding.progressCircular.setVisibility(View.GONE);
                binding.progressCircular.setEnabled(true);
                Utility.toastLS(Profile.this, "Berhasil mengubah profile");
            }else {
                binding.progressCircular.setVisibility(View.GONE);
                binding.progressCircular.setEnabled(true);
                Utility.toastLS(Profile.this, task.getException().getMessage());
            }
        });
    }

    private void saveDataTeacher(String full_name, String nip){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ModelTeacher modelTeacher = new ModelTeacher(
                full_name,
                nip
        );

        reference.child("user_detail").child(Utility.uidCurrentUser).setValue(modelTeacher).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                binding.progressCircular.setVisibility(View.GONE);
                binding.progressCircular.setEnabled(true);
                Utility.toastLS(Profile.this, "Berhasil mengubah data guru");
            }else {
                binding.progressCircular.setVisibility(View.GONE);
                binding.progressCircular.setEnabled(true);
                Utility.toastLS(Profile.this, task.getException().getMessage());
            }
        });
    }

    private void saveDataParent(String nisn, String keyStudent){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ModelParent modelParent = new ModelParent(
                nisn,
                keyStudent
        );
        reference.child("user_detail").child(Utility.uidCurrentUser).setValue(modelParent).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                binding.progressCircular.setVisibility(View.GONE);
                binding.progressCircular.setEnabled(true);
                Utility.toastLS(Profile.this, "Berhasil mengubah data orang tua");
            }else {
                binding.progressCircular.setVisibility(View.GONE);
                binding.progressCircular.setEnabled(true);
                Utility.toastLS(Profile.this, task.getException().getMessage());
            }
        });
    }

    private void setDataUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(Utility.uidCurrentUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelUser modelUser = task.getResult().getValue(ModelUser.class);
                binding.emailEditTi.setText(modelUser.getEmail());
                binding.usernameEditTi.setText(modelUser.getUsername());

                if (!modelUser.getPhone_number().equals("-")){
                    binding.phoneNumberEditTi.setText(modelUser.getPhone_number());
                }

                if(!modelUser.getAddress().equals("-")){
                    binding.streetProfileTi.setText(modelUser.getAddress());
                }

            }else {
                Utility.toastLS(Profile.this, task.getException().getMessage());
            }
        });
    }

    private void setDataTeacher(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("user_detail").child(Utility.uidCurrentUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelTeacher modelTeacher = task.getResult().getValue(ModelTeacher.class);
                if (modelTeacher != null){
                    binding.fullnameTeacherTi.setText(modelTeacher.getFull_name());
                    binding.nipGuruTi.setText(modelTeacher.getNip());
                }

            }else {
                Utility.toastLS(Profile.this, task.getException().getMessage());
            }
        });
    }

    private void setDataChildParent(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("user_detail").child(Utility.uidCurrentUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelParent modelParent = task.getResult().getValue(ModelParent.class);
                if (modelParent != null){
                    binding.nisnEditTi.setText(modelParent.getNisn());
                }
            }else {
                Utility.toastLS(Profile.this, task.getException().getMessage());
            }
        });
    }

    private void checkRegNumber(String nisn){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelStudent modelStudent = ds.getValue(ModelStudent.class);
                    modelStudent.setKey(ds.getKey());
                    if (modelStudent.getNisn().equals(nisn)){
                        saveDataParent(nisn, modelStudent.getKey());
                        return;
                    }
                }
                Utility.toastLS(Profile.this, "NISN tidak ditemukan");
                binding.progressCircular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(Profile.this, "Database : "+error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.updateUI(Profile.this, HomeApp.class);
        finish();
    }
}
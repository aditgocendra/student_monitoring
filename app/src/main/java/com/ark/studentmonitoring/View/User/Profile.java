package com.ark.studentmonitoring.View.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        setLayoutRole();
        listenerClick();
        setDataUser();
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
        }

        if (Utility.roleCurrentUser.equals("parent")){
            binding.cardFeatureRollParent.setVisibility(View.VISIBLE);
            binding.profileImg.setImageResource(R.drawable.default_parent_photo);
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

            if (email.isEmpty()){
                binding.emailEditTi.setError("Email tidak boleh kosong");
            }else if (username.isEmpty()){
                binding.usernameEditTi.setError("Username tidak boleh kosong");
            }else if (phone_number.isEmpty()){
                binding.phoneNumberEditTi.setError("Nomor telepon tidak boleh kosong");
            }else {
                saveDataProfile(email, username, phone_number);
            }
        });
    }

    private void saveDataProfile(String email, String username, String phone_number) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ModelUser modelUser = new ModelUser(
                username,
                email,
                Utility.roleCurrentUser,
                phone_number
        );

        reference.child("users").child(Utility.uidCurrentUser).setValue(modelUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.toastLS(Profile.this, "Berhasil mengubah profile");
                }else {
                    Utility.toastLS(Profile.this, task.getException().getMessage());
                }
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
            }else {
                Utility.toastLS(Profile.this, task.getException().getMessage());
            }
        });
    }

}
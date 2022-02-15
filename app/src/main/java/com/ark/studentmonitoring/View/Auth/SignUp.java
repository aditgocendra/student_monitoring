package com.ark.studentmonitoring.View.Auth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.ark.studentmonitoring.Model.ModelUser;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utility.checkWindowSetFlag(this);
        listenerClick();
    }

    private void listenerClick() {
        binding.redirectSignIn.setOnClickListener(view -> Utility.updateUI(SignUp.this, SignIn.class));

        binding.signUpBtn.setOnClickListener(view -> {
            String username = binding.usernameEdt.getText().toString();
            String email = binding.emailSignUpEdt.getText().toString();
            String pass = binding.passUser.getText().toString();
            String re_pass = binding.confirmPassUser.getText().toString();

            if (username.isEmpty()){
                binding.usernameEdt.setError("Username tidak boleh kososng");
            }else if (email.isEmpty()){
                binding.emailSignUpEdt.setError("Email tidak boleh kosong");
            }else if (pass.isEmpty()){
                binding.passUser.setError("Password tidak boleh kosong");
            }else if (re_pass.isEmpty()){
                binding.confirmPassUser.setError("Konfirmasi password tidak boleh kosong");
            }else {
                if (Utility.ValidateEmail(email)){
                    if (pass.equals(re_pass)){
                        signUp(username, email, pass);
                        binding.signUpBtn.setEnabled(false);
                        binding.progressCircular.setVisibility(View.VISIBLE);
                    }else {
                        Utility.toastLS(SignUp.this, "Password dan Konfirmasi password tidak sama");
                    }
                }else {
                    Utility.toastLS(SignUp.this, "Format email salah");
                }
            }
        });
    }

    private void signUp(String username, String email, String pass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                saveDataUser(username, user.getUid(),email);
            }else {
                binding.signUpBtn.setEnabled(true);
                binding.progressCircular.setVisibility(View.GONE);
                Utility.toastLS(SignUp.this, task.getException().getMessage());
            }
        });
    }

    private void saveDataUser(String username, String uid,String email){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ModelUser modelUser = new ModelUser(
                username,
                email,
                "parent",
                "-"
        );

        reference.child("users").child(uid).setValue(modelUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                binding.signUpBtn.setEnabled(true);
                binding.progressCircular.setVisibility(View.GONE);
                Utility.toastLS(SignUp.this, "Berhasil mendaftar");
                Utility.updateUI(SignUp.this, SignIn.class);
                finish();
            }else {
                binding.signUpBtn.setEnabled(true);
                binding.progressCircular.setVisibility(View.GONE);
                Utility.toastLS(SignUp.this, task.getException().getMessage());
            }
        });
    }
}
package com.ark.studentmonitoring.View.Auth;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utility.checkWindowSetFlag(this);
        listenerClick();
    }

    private void listenerClick() {
        binding.redirectSignIn.setOnClickListener(view -> Utility.updateUI(ForgotPassword.this, SignIn.class));

        binding.resetPassBtn.setOnClickListener(view -> {
            String email = binding.emailResetPass.getText().toString();
            if (email.isEmpty()){
                binding.emailResetPass.setError("Password kosong");
            }else {
                if (Utility.ValidateEmail(email)){
                    binding.resetPassBtn.setEnabled(false);
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    resetPass(email);
                }else {
                    Utility.toastLS(ForgotPassword.this, "Format email salah");
                }
            }
        });
    }

    private void resetPass(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                binding.resetPassBtn.setEnabled(true);
                binding.progressCircular.setVisibility(View.GONE);
                Utility.toastLL(ForgotPassword.this, "Kami telah mengirimkan bantuan ke email anda.");
                Utility.updateUI(ForgotPassword.this, SignIn.class);
                finish();
            }else {
                binding.resetPassBtn.setEnabled(true);
                binding.progressCircular.setVisibility(View.GONE);
                Utility.toastLS(ForgotPassword.this, task.getException().getMessage());
            }
        });
    }
}
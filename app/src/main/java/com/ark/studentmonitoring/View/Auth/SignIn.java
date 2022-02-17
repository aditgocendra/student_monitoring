package com.ark.studentmonitoring.View.Auth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.HomeApp;
import com.ark.studentmonitoring.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utility.checkWindowSetFlag(this);
        listenerClick();

    }

    private void listenerClick() {
        binding.redirectSignUp.setOnClickListener(view -> Utility.updateUI(SignIn.this, SignUp.class));

        binding.redirectForgotPass.setOnClickListener(view -> Utility.updateUI(SignIn.this, ForgotPassword.class));

        binding.signInBtn.setOnClickListener(view -> {
            String email = binding.emailSignIn.getText().toString();
            String pass = binding.passUserSignIn.getText().toString();

            if (email.isEmpty()){
                binding.emailSignIn.setError("Email tidak boleh kosong");
            }else if (pass.isEmpty()){
                binding.passUserSignIn.setError("Password tidak boleh kosong");
            }else {
                if (Utility.ValidateEmail(email)){
                    signIn(email, pass);
                    binding.signInBtn.setEnabled(false);
                    binding.progressCircular.setVisibility(View.VISIBLE);
                }else {
                    Utility.toastLS(SignIn.this, "Format email salah");
                }

            }
        });
    }

    private void signIn(String email, String pass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                binding.signInBtn.setEnabled(true);
                binding.progressCircular.setVisibility(View.GONE);

                FirebaseUser user = auth.getCurrentUser();
                Utility.uidCurrentUser = user.getUid();

                Utility.updateUI(SignIn.this, HomeApp.class);
                finish();
            }else {
                binding.signInBtn.setEnabled(true);
                binding.progressCircular.setVisibility(View.GONE);
                Utility.toastLS(SignIn.this, task.getException().getMessage());
            }
        });
    }

}
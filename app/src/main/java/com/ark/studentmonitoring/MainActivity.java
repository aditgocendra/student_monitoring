package com.ark.studentmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ark.studentmonitoring.View.Auth.SignIn;
import com.ark.studentmonitoring.View.User.HomeApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            Utility.updateUI(MainActivity.this, HomeApp.class);
        }else {
            Utility.updateUI(MainActivity.this, SignIn.class);
        }
        finish();
    }
}
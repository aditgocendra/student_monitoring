package com.ark.studentmonitoring;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import com.ark.studentmonitoring.View.Auth.SignIn;
import com.ark.studentmonitoring.View.User.HomeApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.checkWindowSetFlag(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Handler handler = new Handler();

        handler.postDelayed(() -> {
            if (user != null){
                Utility.uidCurrentUser = user.getUid();
                Utility.updateUI(MainActivity.this, HomeApp.class);
            }else {
                Utility.updateUI(MainActivity.this, SignIn.class);
            }
            finish();
        }, 1000);

    }
}
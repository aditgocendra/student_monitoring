package com.ark.studentmonitoring.View.User.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityDashboardAdminBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DashboardAdmin extends AppCompatActivity {

    private ActivityDashboardAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerClick();
        setLayoutData();

    }

    private void listenerClick() {
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

        binding.cardManageAccount.setOnClickListener(view -> Utility.updateUI(DashboardAdmin.this, ManageAccount.class));

        binding.card1Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "1");
            startActivity(intent);
        });

        binding.card2Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "2");
            startActivity(intent);
        });

        binding.card3Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "3");
            startActivity(intent);
        });

        binding.card4Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "4");
            startActivity(intent);
        });

        binding.card5Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "5");
            startActivity(intent);
        });

        binding.card6Sd.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardAdmin.this, ManageStudentClass.class);
            intent.putExtra("class", "6");
            startActivity(intent);
        });

        binding.cardManageYearSchool.setOnClickListener(view -> Utility.updateUI(DashboardAdmin.this, ManageYearSchool.class));
    }

    private long countClass = 0;
    private void setLayoutData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // count student
        reference.child("student").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    binding.textCountStudent.setText("Jumlah Siswa : "+task.getResult().getChildrenCount());
                }else {
                    Utility.toastLS(DashboardAdmin.this, task.getException().getMessage());
                }
            }
        });

        // count class
        reference.child("class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    countClass += ds.getChildrenCount();
                    Log.d("test", String.valueOf(ds.getChildrenCount()));
                }
                Handler handler = new Handler();
                handler.postDelayed(() -> binding.textCountClass.setText("Jumlah Kelas : "+String.valueOf(countClass)), 500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utility.toastLS(DashboardAdmin.this, "Database : "+error.getMessage());
            }
        });

    }
}
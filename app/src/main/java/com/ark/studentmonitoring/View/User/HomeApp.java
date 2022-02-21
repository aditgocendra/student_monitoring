package com.ark.studentmonitoring.View.User;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ark.studentmonitoring.Model.ModelParent;
import com.ark.studentmonitoring.Model.ModelStudent;
import com.ark.studentmonitoring.Model.ModelUser;
import com.ark.studentmonitoring.NetworkChangeListener;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Parent.ChildValue;
import com.ark.studentmonitoring.View.User.Parent.ClassList;
import com.ark.studentmonitoring.View.User.Parent.SearchViewStudent;
import com.ark.studentmonitoring.databinding.ActivityHomeAppBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeApp extends AppCompatActivity {

    private ActivityHomeAppBinding binding;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);
        setRoleUserCurrent();

        Handler handler = new Handler();
        handler.postDelayed(() -> listenerClick(), 1000);

    }

    @Override
    protected void onStart() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkChangeListener, filter);
        }, 1000);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void listenerClick() {
        binding.accountImg.setOnClickListener(view -> {
            if (!Utility.roleCurrentUser.isEmpty()){
                Utility.updateUI(HomeApp.this, Profile.class);
            }
        });

        binding.cardMyChild.setOnClickListener(view -> {
            String keyStudent = binding.keyStudentCard.getText().toString();

            if (keyStudent.equals("-")){
                //Create the Dialog here
                dialog = new Dialog(HomeApp.this);
                dialog.setContentView(R.layout.custom_dialog_warning);
                dialog.getWindow().setBackgroundDrawable(HomeApp.this.getDrawable(R.drawable.custom_dialog_background));

                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                dialog.setCancelable(false); //Optional
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                Button Okay = dialog.findViewById(R.id.btn_okay);
                Okay.setOnClickListener(v -> {
                    dialog.dismiss();
                });

                dialog.show();
            }else {
                Intent intent = new Intent(HomeApp.this, ChildValue.class);
                Log.d("key", keyStudent);
                intent.putExtra("key_student", keyStudent);
                startActivity(intent);
            }
        });

//        binding.redirectSearchView.setOnClickListener(view -> Utility.updateUI(HomeApp.this, SearchViewStudent.class));

        binding.card1Sd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeApp.this, ClassList.class);
            intent.putExtra("class", "1");
            startActivity(intent);
        });

        binding.card2Sd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeApp.this, ClassList.class);
            intent.putExtra("class", "2");
            startActivity(intent);
        });

        binding.card3Sd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeApp.this, ClassList.class);
            intent.putExtra("class", "3");
            startActivity(intent);
        });

        binding.card4Sd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeApp.this, ClassList.class);
            intent.putExtra("class", "4");
            startActivity(intent);
        });

        binding.card5Sd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeApp.this, ClassList.class);
            intent.putExtra("class", "5");
            startActivity(intent);
        });

        binding.card6Sd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeApp.this, ClassList.class);
            intent.putExtra("class", "6");
            startActivity(intent);
        });
    }

    private void setRoleUserCurrent(){
        reference.child("users").child(Utility.uidCurrentUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelUser modelUser = task.getResult().getValue(ModelUser.class);
                Utility.roleCurrentUser = modelUser.getRole();
                Utility.usernameCurrentUser = modelUser.getUsername();
                Log.d("Current set", Utility.uidCurrentUser+" / "+Utility.roleCurrentUser);
                setLayoutWithRole();
            }else {
                Utility.toastLS(HomeApp.this, task.getException().getMessage());
            }
        });
    }

    private void setLayoutWithRole() {
        if (Utility.roleCurrentUser.equals("parent")){
            binding.textPresent.setText("Haloo "+ Utility.usernameCurrentUser);
            binding.textPresent2.setText("Yuk pantau perkembangan anakmu");
            binding.accountImg.setImageResource(R.drawable.default_parent_photo);
            binding.cardMyChild.setVisibility(View.VISIBLE);
            binding.textHeaderChild.setVisibility(View.VISIBLE);

            reference.child("user_detail").child(Utility.uidCurrentUser).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    ModelParent modelParent = task.getResult().getValue(ModelParent.class);
                    if (modelParent != null){
                        reference.child("student").child(modelParent.getKey_student()).get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                ModelStudent modelStudent = task1.getResult().getValue(ModelStudent.class);
                                if (modelStudent != null){
                                    binding.nameStudentCard.setText("Nama : "+modelStudent.getName());
                                    binding.keyStudentCard.setText(task1.getResult().getKey());
                                }else {
                                    Utility.toastLS(HomeApp.this, "Data anak anda tidak ditemukan");
                                }
                            }else {
                                Utility.toastLS(HomeApp.this, task1.getException().getMessage());
                            }
                        });
                    }
                }else {
                    Utility.toastLS(HomeApp.this, task.getException().getMessage());
                }
            });


        }else if(Utility.roleCurrentUser.equals("teacher")) {
            binding.textPresent.setText("Haloo "+ Utility.usernameCurrentUser);
            binding.textPresent2.setText("Yuk bantu orang tua untuk perkembangan anaknya");
            binding.accountImg.setImageResource(R.drawable.default_teacher_photo);
        }else {
            binding.textPresent.setText("Haloo Administrator");
            binding.textPresent2.setText("Apa yang ingin anda lakukan hari ini");

        }
    }


}
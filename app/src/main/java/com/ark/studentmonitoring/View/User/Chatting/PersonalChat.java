package com.ark.studentmonitoring.View.User.Chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ark.studentmonitoring.Adapter.AdapterMessage;
import com.ark.studentmonitoring.Model.ModelChat;
import com.ark.studentmonitoring.Model.ModelMessage;
import com.ark.studentmonitoring.Model.ModelUser;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityPersonalChatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonalChat extends AppCompatActivity {

    private ActivityPersonalChatBinding binding;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private String keyToUser;
    private String keyMessage = null;

    private List<ModelMessage> listMessage;
    private AdapterMessage adapterMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.purple_custom));

        keyToUser = getIntent().getStringExtra("key");
        setDataReceiver();

        binding.recycleMessage.setHasFixedSize(true);
        LinearLayoutManager mLayout = new LinearLayoutManager(this);
        mLayout.setStackFromEnd(true);
        binding.recycleMessage.setLayoutManager(mLayout);
        binding.recycleMessage.setItemAnimator(new DefaultItemAnimator());

        reference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelChat modelChat = ds.getValue(ModelChat.class);
                    assert modelChat != null;
                    if (modelChat.getUid1().equals(Utility.uidCurrentUser) && modelChat.getUid2().equals(keyToUser) ||
                            modelChat.getUid1().equals(keyToUser) && modelChat.getUid2().equals(Utility.uidCurrentUser)){
                        keyMessage = ds.getKey();
                        setDataMessage();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalChat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        listenerComponent();
    }

    private void listenerComponent() {
        binding.backBtn.setOnClickListener(view -> finish());

        binding.messageSendBtn.setOnClickListener(view -> {
            String message = binding.message.getText().toString();
            if (!message.isEmpty()){
                if (keyMessage != null){
                    sendMessages(message, keyMessage);
                }else {
                    setChat(message);
                }
            }
        });
    }

    private void setDataReceiver() {
        reference.child("users").child(keyToUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelUser modelUser = task.getResult().getValue(ModelUser.class);
                if (modelUser != null){
                    if (modelUser.getRole().equals("teacher")){
                        binding.imageUserPersonalChat.setImageResource(R.drawable.default_teacher_photo);
                    }

                    if (modelUser.getRole().equals("admin")){
                        binding.imageUserPersonalChat.setImageResource(R.drawable.default_teacher_photo);
                    }

                    if (modelUser.getRole().equals("parent")){
                        binding.imageUserPersonalChat.setImageResource(R.drawable.default_parent_photo);
                    }

                    binding.usernameReceiver.setText(modelUser.getUsername());
                }
            }else {
                Toast.makeText(PersonalChat.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setChat(String message) {
        ModelChat modelChat1 = new ModelChat(keyToUser, Utility.uidCurrentUser);
        keyMessage = reference.push().getKey();
        setDataMessage();
        reference.child("chat").child(keyMessage).setValue(modelChat1).addOnSuccessListener(unused -> {
            sendMessages(message, keyMessage);
        }).addOnFailureListener(e -> Toast.makeText(PersonalChat.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setDataMessage() {
        reference.child("message").child(keyMessage).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("test", "jalan");
                listMessage = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelMessage modelMessage = ds.getValue(ModelMessage.class);
                    if (modelMessage != null){
                        listMessage.add(modelMessage);
                    }
                }
                if (listMessage.size() != 0){
                    adapterMessage = new AdapterMessage(listMessage, PersonalChat.this);
                    binding.recycleMessage.setAdapter(adapterMessage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalChat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessages(String message, String key_message){
        long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = Long.toString(tsLong);

        ModelMessage modelMessage = new ModelMessage(
                message,
                timeStamp,
                Utility.uidCurrentUser,
                keyToUser,
                false
        );

        reference.child("message").child(key_message).push().setValue(modelMessage)
                .addOnSuccessListener(unused -> binding.message.setText(""))
                .addOnFailureListener(e -> Toast.makeText(PersonalChat.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}
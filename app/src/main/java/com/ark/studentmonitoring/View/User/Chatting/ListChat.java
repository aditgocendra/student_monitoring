package com.ark.studentmonitoring.View.User.Chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.ark.studentmonitoring.Adapter.AdapterListChat;
import com.ark.studentmonitoring.Model.ModelChat;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.databinding.ActivityListChatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListChat extends AppCompatActivity {

    private ActivityListChatBinding binding;
    private List<ModelChat> listChat;
    private AdapterListChat adapterListChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.checkWindowSetFlag(this);

        listenerComponent();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleListChat.setLayoutManager(layoutManager);
        binding.recycleListChat.setItemAnimator(new DefaultItemAnimator());

        setDataChat();
    }

    private void listenerComponent() {
        binding.backBtn.setOnClickListener(view -> finish());
    }

    private void setDataChat(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listChat = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelChat modelChat = ds.getValue(ModelChat.class);

                    assert modelChat != null;
                    if (modelChat.getUid1().equals(Utility.uidCurrentUser) || modelChat.getUid2().equals(Utility.uidCurrentUser)){
                        modelChat.setKey(ds.getKey());
                        listChat.add(modelChat);
                    }
                }

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (listChat.size() == 0){
                        Toast.makeText(ListChat.this, "Anda belum pernah melakukan obrolan terhadap siapapun", Toast.LENGTH_SHORT).show();
                    }else {
                        adapterListChat = new AdapterListChat(listChat, ListChat.this);
                        binding.recycleListChat.setAdapter(adapterListChat);
                    }
                }, 500);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListChat.this, "Database:"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
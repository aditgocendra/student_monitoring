package com.ark.studentmonitoring.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.studentmonitoring.Model.ModelChat;
import com.ark.studentmonitoring.Model.ModelMessage;
import com.ark.studentmonitoring.Model.ModelUser;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;
import com.ark.studentmonitoring.View.User.Chatting.PersonalChat;
import com.ark.studentmonitoring.View.User.Parent.StudentList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterListChat extends RecyclerView.Adapter<AdapterListChat.ListChatViewHolder> {

    private List<ModelChat> listChat;
    private Context mContext;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public AdapterListChat(List<ModelChat> listChat, Context mContext) {
        this.listChat = listChat;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ListChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_chat, parent, false);
        return new ListChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatViewHolder holder, int position) {
        ModelChat modelChat = listChat.get(position);

        setUserData(modelChat, holder);
        setLastMessage(modelChat.getKey(), holder);
    }


    @Override
    public int getItemCount() {
        return listChat.size();
    }

    public static class ListChatViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUser;
        CardView cardChat;
        TextView textUsername, textLastChat;
        public ListChatViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUser = itemView.findViewById(R.id.image_user_chat);
            cardChat = itemView.findViewById(R.id.card_chat);
            textUsername = itemView.findViewById(R.id.text_name_user);
            textLastChat = itemView.findViewById(R.id.text_last_chat);
        }
    }

    private void setUserData(ModelChat modelChat, ListChatViewHolder holder) {
        Query query;
        String receiver;
        if (modelChat.getUid1().equals(Utility.uidCurrentUser)){
            query = reference.child("users").child(modelChat.getUid2());
            receiver = modelChat.getUid2();
        }else {
            query = reference.child("users").child(modelChat.getUid1());
            receiver = modelChat.getUid1();
        }

        holder.cardChat.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, PersonalChat.class);
            intent.putExtra("key", receiver);
            mContext.startActivity(intent);
        });

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ModelUser modelUser = task.getResult().getValue(ModelUser.class);
                if (modelUser != null){
                    holder.textUsername.setText(modelUser.getUsername());

                    if (modelUser.getRole().equals("teacher")){
                        holder.imageUser.setImageResource(R.drawable.default_teacher_photo);
                    }

                    if (modelUser.getRole().equals("admin")){
                        holder.imageUser.setImageResource(R.drawable.default_teacher_photo);
                    }

                    if (modelUser.getRole().equals("parent")){
                        holder.imageUser.setImageResource(R.drawable.default_parent_photo);
                    }
                }
            }
        });
    }

    private void setLastMessage(String key_message, ListChatViewHolder holder){
        reference.child("message").child(key_message).orderByChild("timestamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelMessage modelMessage = dataSnapshot.getValue(ModelMessage.class);
                    if (modelMessage != null){
                        holder.textLastChat.setText(modelMessage.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

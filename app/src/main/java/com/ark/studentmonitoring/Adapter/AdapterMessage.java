package com.ark.studentmonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.studentmonitoring.Model.ModelMessage;
import com.ark.studentmonitoring.R;
import com.ark.studentmonitoring.Utility;

import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter {

    private List<ModelMessage> listMessage;
    private Context mContext;

    public AdapterMessage(List<ModelMessage> listMessage, Context mContext) {
        this.listMessage = listMessage;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if (listMessage.get(position).getUidSender().equals(Utility.uidCurrentUser)){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0){
            view = layoutInflater.inflate(R.layout.layout_chat_sender, parent, false);
            return new ViewHolderSender(view);
        }else {
            view = layoutInflater.inflate(R.layout.layout_chat_reveiver, parent, false);
            return new ViewHolderReceiver(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModelMessage modelMessage = listMessage.get(position);
        if (modelMessage.getUidSender().equals(Utility.uidCurrentUser)){
            ViewHolderSender holderSender = (ViewHolderSender) holder;
            holderSender.chatTextSender.setText(modelMessage.getMessage());
        }else {
            ViewHolderReceiver holderReceiver = (ViewHolderReceiver) holder;
            holderReceiver.chatTextReceiver.setText(modelMessage.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    static class ViewHolderSender extends RecyclerView.ViewHolder {
        TextView chatTextSender;
        public ViewHolderSender(@NonNull View itemView) {
            super(itemView);
            chatTextSender = itemView.findViewById(R.id.textChatSender);
        }
    }

    static class ViewHolderReceiver extends RecyclerView.ViewHolder {
        TextView chatTextReceiver;
        public ViewHolderReceiver(@NonNull View itemView) {
            super(itemView);
            chatTextReceiver = itemView.findViewById(R.id.textChatReceiver);
        }
    }


}

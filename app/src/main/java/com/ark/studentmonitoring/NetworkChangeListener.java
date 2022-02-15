package com.ark.studentmonitoring;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.ark.studentmonitoring.R;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Utility.checkConnection(context)){       // is not connected internet
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.layout_check_connection, null);
            builder.setView(layout_dialog);

            AppCompatButton btnRetry = layout_dialog.findViewById(R.id.retry_connection_btn);

            // Show dialog
            AlertDialog dialog = builder.create();
            if (!Utility.isConnected){
                dialog.show();
                dialog.setCancelable(false);
                dialog.getWindow().setGravity(Gravity.CENTER);

                btnRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        onReceive(context, intent);
                    }
                });
            }
        }
    }


}

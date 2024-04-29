package com.example.yamenandroidacteen.classes.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import com.example.yamenandroidacteen.R;

public class NoConnectionDialog {

    public static AlertDialog create(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.activity_no_connection, null);

        TextView messageTextView = dialogView.findViewById(R.id.no_connection_text);
        Button reconnectButton = dialogView.findViewById(R.id.retry_button);
        ImageView imageView = dialogView.findViewById(R.id.no_wifi_image);

        messageTextView.setText(message);


        reconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check network connectivity
                if (NetworkUtils.isNetworkConnected(context)) {
                    // Handle reconnection logic (e.g., reload data)
                    Intent intent = new Intent(context, context.getClass());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    Toast.makeText(context, "Connected Back!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No connection available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(dialogView);


        return builder.create();
    }
}

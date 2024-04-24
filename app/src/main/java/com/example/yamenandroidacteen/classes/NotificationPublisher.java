package com.example.yamenandroidacteen.classes;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.yamenandroidacteen.R;

public class NotificationPublisher extends BroadcastReceiver {

    public static final String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION_TITLE = "notification-title";
    public static final String NOTIFICATION_TEXT = "notification-text";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String text = intent.getStringExtra(NOTIFICATION_TEXT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
        }
    }
}

package com.example.yamenandroidacteen.home.activist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.NotificationPublisher;
import java.util.concurrent.TimeUnit;

public class ActivistNotificationsFragment extends Fragment {

    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final int NOTIFICATION_ID = 100;

    private Button btnAllowNotifications;
    private Button btnTestNotification;
    private SwitchCompat switchNotifications;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activist_notifications, container, false);

        btnAllowNotifications = view.findViewById(R.id.btnAllowNotifications);
        btnTestNotification = view.findViewById(R.id.btnTestNotification);
        switchNotifications = view.findViewById(R.id.switchNotifications);

        // Check if notifications permission is granted
        boolean isNotificationsEnabled = checkNotificationsEnabled();
        updateUI(isNotificationsEnabled);

        btnAllowNotifications.setOnClickListener(v -> requestNotificationsPermissions());

        btnTestNotification.setOnClickListener(v -> sendTestNotification());

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle switch toggle for notifications
            if (isChecked) {
                // Notifications enabled
                Toast.makeText(getActivity(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Notifications disabled
                Toast.makeText(getActivity(), "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateUI(boolean isNotificationsEnabled) {
        if (isNotificationsEnabled) {
            btnAllowNotifications.setVisibility(View.GONE);
            switchNotifications.setVisibility(View.VISIBLE);
        } else {
            btnAllowNotifications.setVisibility(View.VISIBLE);
            switchNotifications.setVisibility(View.GONE);
        }
    }

    private void requestNotificationsPermissions() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
        startActivity(intent);
    }

    private boolean checkNotificationsEnabled() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        return notificationManager.areNotificationsEnabled();
    }

    @SuppressLint("MissingPermission")
    private void sendTestNotification() {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.acteen_logo)
                .setContentTitle("Acteen Notification")
                .setContentText("This is a test notification.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyNotificationChannel";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

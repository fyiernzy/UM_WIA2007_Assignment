package com.example.betterher.Notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.betterher.R;

public class ReminderBroadcast extends BroadcastReceiver {
    private static final String CHANNEL_ID = "notifyBetterHer";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        // Create a NotificationCompat.Builder with BigTextStyle
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("BetterHer UPDATE:")
                .setContentText("- New Video on 'A Passionate, personal case for protection' by Michelle Obama is released! Have A Look!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("- New Video on 'A Passionate, personal case for protection' by Michelle Obama is released! Have A Look!"))
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set high priority for heads-up notification
                .setAutoCancel(true);

        // Get NotificationManagerCompat instance
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Check for permission and OS version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Log or handle the absence of required permission
                return;
            }
        }

        // Show the notification
        notificationManager.notify(200, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "BetterHerReminderChannel";
            String description = "Channel for BetterHer Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Set to HIGH for heads-up
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}



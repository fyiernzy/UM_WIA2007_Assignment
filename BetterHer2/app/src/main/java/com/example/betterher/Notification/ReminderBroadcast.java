package com.example.betterher.Notification;

import android.Manifest;
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
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a NotificationCompat.Builder with BigTextStyle
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyBetterHer")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("BetterHer UPDATE:")
                .setContentText("- New Video on 'A Passionate, personal case for protection' by Michelle Obama is released! Have A Look!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("- New Video on 'A Passionate, personal case for protection' by Michelle Obama is released! Have A Look!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

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
}


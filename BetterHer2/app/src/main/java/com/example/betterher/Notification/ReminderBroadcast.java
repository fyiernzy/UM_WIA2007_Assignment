package com.example.betterher.Notification;

import android.os.Build;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyBetterHer")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("BetterHer UPDATE:")
                .setContentText("-\tNew Video on 'Women Empowerment' by Harper BAZAAR is released! Have A Look! ")
                //.setContentTitle("BetterHer DAILY QUOTE:")
                //.setContentText("-\tThis is Your Moment! Own It! ")
                //.setContentTitle("BetterHer REMINDER:")
                //.setContentText("-\tYou are Powerful, Beautiful, Brilliant & Brave! ")
                //.setContentTitle("BetterHer UPDATE:")
                //.setContentText("-\tNew set of Revision Card on 'Discrimination' is out! Try It Out! ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(200, builder.build());

        // Check for permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            // If running on Android 6.0 (API level 23) or above, request permission dynamically
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // TODO: Handle the case where the user doesn't have the required permission
                // You might want to show a dialog or request the permission using ActivityCompat.requestPermissions
            }

            return;
        }

        // If permission is granted, proceed to show the notification
        try {
            notificationManager.notify(200, builder.build());
        } catch (SecurityException e) {
            // Handle the SecurityException, for example, by requesting the permission
            // ActivityCompat.requestPermissions or show a dialog to inform the user
            e.printStackTrace();


    }
} }

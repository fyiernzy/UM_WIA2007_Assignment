package com.example.myapplication_1;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication_1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertPathBuilder;

public class MainActivity extends AppCompatActivity {

    //private Intent ReminderBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        Button button = findViewById(R.id.button);

        if(SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }
        
        button.setOnClickListener(v -> {
            Toast.makeText(this,"Reminder Set!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, com.example.myapplication_1.ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long timeAtButtonCLick = System.currentTimeMillis();

            long tenSecondsInMills = 1000 * 10;

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    timeAtButtonCLick + tenSecondsInMills,
                    pendingIntent);
        });
    }

    private void createNotificationChannel(){

        double v = .0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1)
        {
            CharSequence name = "BetterHerReminderChannel";
            String description = "Channel for BetterHer Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyBetterHer", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        //notificationManager.notify(0.builder.build());
    }
}

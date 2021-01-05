package com.example.project2.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.project2.Activities.MainActivity;

import androidx.core.app.NotificationCompat;


public class ReminderReceiver extends BroadcastReceiver {
    private static final String TAG = "ReminderReceiver";
    private static final int IDReminder = 1106;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive:cccccccc ");
        String channelId = "toeic.default";
        String channelTitle = "Reminder";
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(channelId, channelTitle, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Intent intenGotoApp = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intenGotoApp,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setContentTitle("Time to Study")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentText("Tap to  start")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(IDReminder, notification);
    }
}
package com.example.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        final String CHANNEL_ID = "Channel ID";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "Channel Name";
            final String CHANNEL_DESCRIPTION = "Channel Description";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            // add in API level 26
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setVibrate(new long[]{500, 500});
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}

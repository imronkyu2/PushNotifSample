package com.nexsoft.pushnotiftestingservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class FirebaseService extends FirebaseMessagingService {
    private final String chanelId = "12345";
    private NotificationCompat.Builder builder;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        //refresh token dan wajib dikirim ke App server melalui API karena ini sebagai id pengenal
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            setChannelId();
        triggerNotification(remoteMessage.getData());

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setChannelId() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(chanelId, "Notification", importance);
        channel.setDescription("Channel for notification");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void triggerNotification(Map<String, String> data) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("value", data.get("value"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder = new NotificationCompat.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder = new NotificationCompat.Builder(this, chanelId);

        builder.setSmallIcon(R.drawable.ic_message)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("body"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (data.get("image") != null) {
            String imageUrl = data.get("image");
            builder.setLargeIcon(buildBitmap(imageUrl));
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(FirebaseService.this);
            notificationManager.notify(Integer.parseInt(Objects.requireNonNull(data.get("tag"))), builder.build());
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(FirebaseService.this);
        notificationManager.notify(Integer.parseInt(Objects.requireNonNull(data.get("tag"))), builder.build());

    }

    private Bitmap buildBitmap(String PHOTOS_BASE_URL) {
        String imageUrl = PHOTOS_BASE_URL;
        InputStream in = null;

        try {
            in = (InputStream) new URL(imageUrl).getContent();
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

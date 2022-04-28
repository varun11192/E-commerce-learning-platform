package com.varunsen.gurughantal.classes;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.varunsen.gurughantal.MainActivity;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.ProductDetailsActivity;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getTitle();


        Class cl = MainActivity.class;

        if (remoteMessage.getData().containsKey("intent_type")) {
            String intentType = remoteMessage.getData().get("intent_type");

            if (intentType.equals("product_page")) {

                cl = ProductDetailsActivity.class;

            }

        }

        if (remoteMessage.getData().containsKey("product_id")) {
            String productId = remoteMessage.getData().get("product_id");

            triggerNotification(title, body, productId, cl);


        }

    }

    private void triggerNotification(String title, String body, String ids, Class activity) {

        Random random = new Random();
        int id = random.nextInt(9999);

        Intent intent = new Intent(this, activity);
        intent.putExtra("product_id", ids);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL001")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, builder.build());

    }


}

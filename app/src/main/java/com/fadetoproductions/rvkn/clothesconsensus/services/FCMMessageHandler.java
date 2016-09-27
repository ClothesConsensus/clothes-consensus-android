package com.fadetoproductions.rvkn.clothesconsensus.services;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.activity.ProfileV2Activity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


/**
 * Created by sdass on 9/20/16.
 */
public class FCMMessageHandler extends FirebaseMessagingService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    public static final String ACTION = "Local_broadcast_message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String from = remoteMessage.getFrom();
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if(notification != null) {
            createNotification(notification);
        } else {
            Context context = getBaseContext();
            int color = ContextCompat.getColor(context, R.color.cursorBlueColor);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.c_hanger)
                    .setContentTitle("" + data.values().toArray()[0])
                    .setColor(color);
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
        }
    }

    // Creates notification based on title and body received
    private void createNotification(RemoteMessage.Notification notification) {
        Context context = getBaseContext();
        Intent in = new Intent(ACTION);
        int color = ContextCompat.getColor(context, R.color.cursorBlueColor);
        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("resultValue",notification.getTitle()+". "+notification.getBody());
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
        Log.v("LOCAL_BROADCAST","Broadcast Send.");

        Intent viewIntent = new Intent(context, ProfileV2Activity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(context, 0, viewIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.c_hanger).setContentTitle(notification.getTitle())
                .setContentText(notification.getBody()).setContentIntent(viewPendingIntent)
                .setAutoCancel(true)
                .setColor(color);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }
}

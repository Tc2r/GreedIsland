package com.tc2r.greedisland.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.tc2r.greedisland.R;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

public class EventServiceReceiver extends BroadcastReceiver {
    private MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Create Mediaplayer for Sound Effects
        mp = MediaPlayer.create(context, R.raw.greed);
        // Play Sound Effect
        mp.start();

        // Create notification and send it!
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification eventNotification = getNotification(context, "Something Happened While Resting!");
        eventNotification.defaults |= Notification.DEFAULT_LIGHTS;
        eventNotification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(003, eventNotification);

    }

    private Notification getNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_stat_note);

        // Set up actions for Notification
        Intent resultIntent = new Intent(context, eventtest.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 3, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        builder.setVibrate(new long[]{1000, 500, 1000});
        return builder.build();
    }
}

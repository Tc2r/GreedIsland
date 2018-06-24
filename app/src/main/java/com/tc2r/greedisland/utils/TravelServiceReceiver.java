package com.tc2r.greedisland.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.tc2r.greedisland.R;
import com.tc2r.greedisland.map.MapActivity;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

public class TravelServiceReceiver extends BroadcastReceiver {
    private MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Access Saved Variables
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();


        // Create Mediaplayer for Sound Effects
        mp = MediaPlayer.create(context, R.raw.greed);

        // Reset saved Variables to allow user to Travel
        editor.putBoolean(context.getString(R.string.pref_can_travel_key), true);
        editor.putBoolean("AlarmTravelSet", false);
        editor.apply();

        // Play Sound Effect

        mp.start();

        // Create notification and send it!
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = getNotification(context, context.getString(R.string.new_Travel_Notification));
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(002, notification);

    }

    private Notification getNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_notification);

        // Set up actions for Notification
        Intent resultIntent = new Intent(context, MapActivity.class);
        resultIntent.putExtra("viewpager_position", TravelHelper.getBaseTownID(context));

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 2, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        builder.setVibrate(new long[]{1000, 1000});
        return builder.build();
    }
}

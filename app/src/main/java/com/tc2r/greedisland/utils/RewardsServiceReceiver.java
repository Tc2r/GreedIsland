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
import com.tc2r.greedisland.book.BookActivity;

/**
 * Created by Tc2r on 2/7/2017.
 * <p>
 * Description:
 */

public class RewardsServiceReceiver extends BroadcastReceiver {

	private MediaPlayer mp;

	@Override
	public void onReceive(Context context, Intent intent) {


		// Access Saved Variables
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();

		// Create MediaPlayer for Sound Effect
		mp = MediaPlayer.create(context, R.raw.greed);

		// Reset saved Variables to show new cards
		editor.putBoolean("DailyCards", true);
		editor.putInt("Rewards", 0);
		editor.apply();

		// Play Sound Effect
		mp.start();

		// Testing!
		//Toast.makeText(context, String.valueOf(context.getClass().getSimpleName()), Toast.LENGTH_LONG).show();
		//Log.wtf("Alarm Worked", String.valueOf(settings.getAll()));

		// Create notification and send it!
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = getNotification(context, context.getString(R.string.new_Card_Notification));
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE;
		notificationManager.notify(001, notification);

	}

	private Notification getNotification(Context context, String content) {
		Notification.Builder builder = new Notification.Builder(context);
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.ic_launcher);

		// Set up actions for Notification
		Intent resultIntent = new Intent(context, BookActivity.class);

		PendingIntent resultPendingIntent =
						PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(resultPendingIntent);
		builder.setAutoCancel(true);
		builder.setVibrate(new long[]{1000, 500});
		return builder.build();
	}

}

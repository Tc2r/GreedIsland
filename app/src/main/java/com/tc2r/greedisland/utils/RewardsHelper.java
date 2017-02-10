package com.tc2r.greedisland.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.GregorianCalendar;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

class RewardsHelper {


	// Reset Alarm When Phone Is Rebooted
	public static void BootAlarm(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		long currentTime = new GregorianCalendar().getTimeInMillis();
		long rewardTime = settings.getLong("RewardTime", currentTime);
		Log.wtf("REWARD TIME ", String.valueOf(rewardTime) + "   " + String.valueOf(currentTime));
		Intent intentAlarm = new Intent(context, RewardsServiceReceiver.class);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, rewardTime, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
	}

	// Set Alarm When Cards are all gone
	public static void setAlarm(Context context) {
		Long time = new GregorianCalendar().getTimeInMillis() + 2 * 60 * 1000;
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("RewardTime", time);
		editor.apply();
		Intent intentAlarm = new Intent(context, RewardsServiceReceiver.class);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
		Toast.makeText(context, "TESTING Card Reset In 2 minutes!", Toast.LENGTH_LONG).show();
	}
}

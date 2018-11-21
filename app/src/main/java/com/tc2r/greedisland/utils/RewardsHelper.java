package com.tc2r.greedisland.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.tc2r.greedisland.R;

import java.util.GregorianCalendar;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

public class RewardsHelper {

    // Reset Alarm When Phone Is Rebooted
    public static void bootAlarm(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        long currentTime = new GregorianCalendar().getTimeInMillis();
        long rewardTime = settings.getLong("RewardTime", currentTime);
        PerformanceTracking.TrackEvent("Reset Alarms: " + String.valueOf(rewardTime) + "  " +String.valueOf(currentTime));
        if (rewardTime < currentTime) {
            PerformanceTracking.TrackEvent("Sound Alarm Now!");
            rewardTime = currentTime + 3000;
        }
        Intent intentAlarm = new Intent(context, RewardsServiceReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, rewardTime, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    // Set Alarm When Cards are all gone
    public static void setAlarm(View view) {

        Context context = view.getContext();
        Long time = new GregorianCalendar().getTimeInMillis() + 24 * 60 * 60 * 1000;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("RewardTime", time);
        editor.putBoolean("AlarmRewardSet", true);
        editor.apply();
        Intent intentAlarm = new Intent(context, RewardsServiceReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        GreedSnackbar.createSnackBar(view, R.string.daily_cards_alert, Snackbar.LENGTH_LONG).show();
    }

    public static void EnableBroadcast(Context context) {
        PackageManager pm = context.getPackageManager();
        ComponentName compName = new ComponentName(context.getApplicationContext(), OnBootRewardReceiver.class);
        pm.setComponentEnabledSetting(compName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }
}

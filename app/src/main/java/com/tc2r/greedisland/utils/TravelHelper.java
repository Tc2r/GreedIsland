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
import com.tc2r.greedisland.map.MapActivity;

import java.util.GregorianCalendar;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

public class TravelHelper {


    private static int delay = 24 * 60 * 60 * 1000;

    // Reset Alarm When Phone Is Rebooted
    public static void BootAlarm(Context cnxt) {
        Context context = cnxt;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        long currentTime = new GregorianCalendar().getTimeInMillis();
        long travelTime = settings.getLong("TravelTime", currentTime);
        PerformanceTracking.TrackEvent("Reset Travel Alarm: " + String.valueOf(travelTime) + "  " +String.valueOf(currentTime));
        Intent intentAlarm = new Intent(context, TravelServiceReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, travelTime, PendingIntent.getBroadcast(context, 2, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        if (settings.getBoolean("EventSwitch", false)) {
            long eventMax = (travelTime - (travelTime / 5));
            long eventMin = currentTime + 60 * 1000;
            long eventTime = eventMin + (long) (Math.random() * (eventMax - eventMin));
            Intent eventAlarm = new Intent(context, EventServiceReceiver.class);
            AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager1.set(AlarmManager.RTC_WAKEUP, eventTime, PendingIntent.getBroadcast(context, 2, eventAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("EventSwitch", false);
            editor.apply();
        }


    }

    // Set Alarm When Cards are all gone
    public static void setAlarm(View view) {
        Context context = view.getContext();

        // Alarm Time = Current time + Delay in Millis
        // Current time 24000000 + 170000 hmm
        // event min should be 2400000 + a fraction of delay
        Long time = new GregorianCalendar().getTimeInMillis() + delay;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("TravelTime", time);
        editor.putBoolean("EventSwitch", true);
        editor.apply();
        Intent travelAlarm = new Intent(context, TravelServiceReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, travelAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        GreedSnackbar.createSnackBar(view, R.string.Daily_Travel_Text, Snackbar.LENGTH_LONG).show();

        // Latest time event happen is 90% of travel alarm
        long eventMax = time - delay * (100 / 90);

        // Earliest is 10% of travel alarm.
        long eventMin = time + delay / 9;

        long eventTime = eventMin + (long) (Math.random() * (eventMax - eventMin));

        Intent eventAlarm = new Intent(context, EventServiceReceiver.class);
        AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager1.set(AlarmManager.RTC_WAKEUP, eventTime, PendingIntent.getBroadcast(context, 3, eventAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

    }

    public static void ViewTown(Context context, int mode) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String currentLocation = settings.getString(context.getString(R.string.pref_current_location_key), context.getString(R.string.pref_town_default));
        Intent travel = new Intent(context, MapActivity.class);
        String currentBase = settings.getString(context.getString(R.string.pref_current_home_key), context.getString(R.string.pref_town_default));
        Intent base = new Intent(context, MapActivity.class);
        switch (mode) {
            case 1:
            default:

                // Default position Start
                travel.putExtra("viewpager_position", 4);

                // else position = location.
                if (currentLocation.equals("Masadora")) {
                    // Auto View Masadora
                    travel.putExtra("viewpager_position", 0);
                }
                if (currentLocation.equals("Soufrabi")) {
                    // Auto View Soufrabi
                    travel.putExtra("viewpager_position", 1);
                }
                if (currentLocation.equals("Aiai")) {
                    // Auto View Aiai
                    travel.putExtra("viewpager_position", 2);
                }
                if (currentLocation.equals("Antokiba")) {
                    // Auto View Antokiba
                    travel.putExtra("viewpager_position", 3);
                }
                if (currentLocation.equals("Start")) {
                    // Auto View Start
                    travel.putExtra("viewpager_position", 4);
                }
                if (currentLocation.equals("Rubicuta")) {
                    // Auto View Rubicuta
                    travel.putExtra("viewpager_position", 5);
                }
                if (currentLocation.equals("Dorias")) {
                    // Auto View Dorias
                    travel.putExtra("viewpager_position", 6);
                }
                if (currentLocation.equals("Limeiro")) {
                    // Auto View Limeiro
                    travel.putExtra("viewpager_position", 7);
                }
                // start the activity.
                context.startActivity(travel);
                break;
            case 2:

                // Default position Start
                base.putExtra("viewpager_position", 4);

                // else position = location.
                if (currentBase.equals("Masadora")) {
                    // Auto View Masadora
                    base.putExtra("viewpager_position", 0);
                }
                if (currentBase.equals("Soufrabi")) {
                    // Auto View Soufrabi
                    base.putExtra("viewpager_position", 1);
                }
                if (currentBase.equals("Aiai")) {
                    // Auto View Aiai
                    base.putExtra("viewpager_position", 2);
                }
                if (currentBase.equals("Antokiba")) {
                    // Auto View Antokiba
                    base.putExtra("viewpager_position", 3);
                }
                if (currentBase.equals("Start")) {
                    // Auto View Start
                    base.putExtra("viewpager_position", 4);
                }
                if (currentBase.equals("Rubicuta")) {
                    // Auto View Rubicuta
                    base.putExtra("viewpager_position", 5);
                }
                if (currentBase.equals("Dorias")) {
                    // Auto View Dorias
                    base.putExtra("viewpager_position", 6);
                }
                if (currentBase.equals("Limeiro")) {
                    // Auto View Limeiro
                    base.putExtra("viewpager_position", 7);
                }

                // start the activity.
                context.startActivity(base);
                break;
        }
    }

    public static int getBaseTownID(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String currentHome = settings.getString(context.getString(R.string.pref_current_home_key), context.getString(R.string.pref_town_default));
        // Default position Start

        int baseID = 4;
        // else position = saved base

        switch (currentHome) {
            case "Masadora":
                // Auto View Masadora
                baseID = 0;
                break;
            case "Soufrabi":
                // Auto View Soufrabi
                baseID = 1;
                break;
            case "Aiai":
                // Auto View Aiai
                baseID = 2;
                break;
            case "Antokiba":
                // Auto View Antokiba
                baseID = 3;
                break;
            case "Start":
                // Auto View Start
                baseID = 4;
                break;
            case "Rubicuta":
                // Auto View Rubicuta
                baseID = 5;
                break;
            case "Dorias":
                // Auto View Dorias
                baseID = 6;
                break;
            case "Limeiro":
                // Auto View Limeiro
                baseID = 7;
                break;
        }
        return baseID;
    }

    public static void EnableBroadcast(Context context) {
        PackageManager pm = context.getPackageManager();
        ComponentName compName = new ComponentName(context.getApplicationContext(), OnBootRewardReceiver.class);
        pm.setComponentEnabledSetting(compName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }
}

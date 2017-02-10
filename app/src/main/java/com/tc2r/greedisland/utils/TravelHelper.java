package com.tc2r.greedisland.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.tc2r.greedisland.map.MapActivity;

import java.util.GregorianCalendar;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

public class TravelHelper {


	// Reset Alarm When Phone Is Rebooted
	public static void BootAlarm(Context cnxt) {
		Context context = cnxt;
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		long currentTime = new GregorianCalendar().getTimeInMillis();
		long travelTime = settings.getLong("TravelTime", currentTime);
		Log.wtf("Travel TIME ", String.valueOf(travelTime) + "   " + String.valueOf(currentTime));
		Intent intentAlarm = new Intent(context, TravelServiceReceiver.class);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, travelTime, PendingIntent.getBroadcast(context, 2, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
	}

	// Set Alarm When Cards are all gone
	public static void SetAlarm(Context cnxt) {
		Context context = cnxt;
		Long time = new GregorianCalendar().getTimeInMillis() + 20 * 1000;
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("TravelTime", time);
		editor.apply();
		Intent travelAlarm = new Intent(context, TravelServiceReceiver.class);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, travelAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
		Toast.makeText(context, "TESTING Travel Reset In 2 minutes!", Toast.LENGTH_LONG).show();
	}

	public static void ViewTown(Context context, int mode) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String currentLocation = settings.getString("CurrentLocation", "Start");
		Intent travel = new Intent(context, MapActivity.class);
		String currentBase = settings.getString("CurrentHome", "Start");
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
		String currentHome = settings.getString("CurrentHome", "Start");
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
}

package com.tc2r.greedisland.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

public class OnBootRewardReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //OnBootHelper onBootHelper = new OnBootHelper(context);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            boolean checkTravel = settings.getBoolean("AlarmTravelSet", false);
            boolean checkReward = settings.getBoolean("AlarmRewardSet", false);
            PerformanceTracking.TrackEvent("OnBoot Triggered");

            if (checkReward) {
                if (!settings.getBoolean("AlarmTravelSet", false)) {
                    PackageManager pm = context.getPackageManager();
                    ComponentName compName = new ComponentName(context.getApplicationContext(), OnBootRewardReceiver.class);
                    pm.setComponentEnabledSetting(compName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                }
                RewardsHelper.bootAlarm(context);
                PerformanceTracking.TrackEvent("OnBootReward Rewards Alarm!");
            }
            if (checkTravel) {
                if (!settings.getBoolean("AlarmRewardSet", false)) {
                    PackageManager pm = context.getPackageManager();
                    ComponentName compName = new ComponentName(context.getApplicationContext(), OnBootRewardReceiver.class);
                    pm.setComponentEnabledSetting(compName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                }
                TravelHelper.BootAlarm(context);
                PerformanceTracking.TrackEvent("OnBoot Travel Alarm!");
            }


        }
    }
}

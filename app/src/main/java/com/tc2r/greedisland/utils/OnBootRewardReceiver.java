package com.tc2r.greedisland.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tc2r on 2/8/2017.
 * <p>
 * Description:
 */

public class OnBootRewardReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//RewardsHelper.BootAlarm(context);
		TravelHelper.BootAlarm(context);
	}
}

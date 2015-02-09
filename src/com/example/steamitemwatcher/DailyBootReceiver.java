package com.example.steamitemwatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DailyBootReceiver extends BroadcastReceiver {
	
	DailyReceiver alarm = new DailyReceiver();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			alarm.setAlarm(context);
        }

	}

}

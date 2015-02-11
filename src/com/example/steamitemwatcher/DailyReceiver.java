package com.example.steamitemwatcher;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class DailyReceiver extends WakefulBroadcastReceiver {


	 private AlarmManager alarmMgr;
	 private PendingIntent alarmIntent;
	 

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("Alarm goes off.");
		
		Intent service = new Intent(context, PriceRecordingService.class);

        
		
		
		
		
        
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
	    File directory = cw.getDir("historyDir", Context.MODE_PRIVATE);
	    File temppath = new File(directory, "alarmlength");
	    int i;
	    try{
			FileInputStream fis = new FileInputStream(temppath);
			Scanner sc = new Scanner(fis);
			i = Integer.parseInt(sc.nextLine());
			sc.close();
	    }
	    catch(IOException e){
	    	i = 24;
		}
        
        
        
        calendar.add(Calendar.HOUR, i);
        System.out.println("Silent Alarm SeT");
        
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), alarmIntent);
     
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);

	}
	
	
	public void setAlarm(Context context, int length) {
        
		
		
		ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
	    File directory = cw.getDir("historyDir", Context.MODE_PRIVATE);
	    File temppath = new File(directory, "alarmlength");
	    PrintWriter out;
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(temppath)));
			out.append("" + length);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		
		System.out.println("Alarm has been set.");
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        
        calendar.add(Calendar.SECOND, 3);
        System.out.println("Silent Alarm SeT");
        
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), alarmIntent);
        enableBoot(context);
	}
	//to be used later
	public void killAlarm(Context context){
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, DailyReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);
        disableBoot(context);
	}
	
	private void enableBoot(Context context){
		ComponentName receiver = new ComponentName(context, DailyBootReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP); 
	}
	
	private void disableBoot(Context context){
		ComponentName receiver = new ComponentName(context, DailyBootReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	
	}
	

}

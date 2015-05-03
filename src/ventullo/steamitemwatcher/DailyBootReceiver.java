package ventullo.steamitemwatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

public class DailyBootReceiver extends BroadcastReceiver {
	
	DailyReceiver alarm = new DailyReceiver();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			
			ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
		    File directory = cw.getDir("historyDir", Context.MODE_PRIVATE);
		    File temppath = new File(directory, "alarmlength");
		    int i;
		    try{
				FileInputStream fis = new FileInputStream(temppath);
				Scanner sc = new Scanner(fis);
				i = Integer.parseInt(sc.nextLine());
				alarm.setAlarm(context,  i);
				sc.close();
		    }
		    catch(IOException e){
		    	alarm.setAlarm(context, 24);
			}
			
			
			
        }

	}

}

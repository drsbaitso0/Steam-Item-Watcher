package com.example.steamitemwatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends FragmentActivity  implements OnItemSelectedListener {

	int timeid = -1;
	Spinner spinner;
	DailyReceiver alarm = new DailyReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		spinner = (Spinner) findViewById(R.id.update_time);
		spinner.setOnItemSelectedListener(this);
		String[] times = new String[]{"Hour", "Day", "Week"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);
		spinner.setAdapter(adapter);
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
       timeid = pos;
    }

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		timeid = -1;
	}
	
	public void setAlarm(View view){
		int len = -1;
		if(timeid == -1){
			Context context = getApplicationContext();
	    	CharSequence text = "Choose a frequency interval";
	    	int duration = Toast.LENGTH_SHORT;
	    	Toast toast = Toast.makeText(context, text, duration);
	    	toast.show();
			
			return;
		}
		else if(timeid == 0) len = 1;
		else if(timeid == 1) len = 24;
		else if(timeid == 2) len = 168;
		else len = 3;//testing purposes; this line should never be executed
		
		alarm.setAlarm(this, len);
		Context context = getApplicationContext();
    	CharSequence text = "Price/Quantity Recording On";
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
	}
	
	public void killAlarm(View view){
		alarm.killAlarm(this);
		Context context = getApplicationContext();
    	CharSequence text = "Price/Quantity Recording Off";
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
		
		
		
	}
	
	public void about(View view){
		Intent intent = new Intent(this, About.class);
		startActivity(intent);
	}
	
	

}

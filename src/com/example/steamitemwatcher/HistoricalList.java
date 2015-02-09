package com.example.steamitemwatcher;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class HistoricalList extends ListActivity {

	String[] values;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
		int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
		String[] values = new String[count];
		for(int i=0; i<count; i++){
			values[i] = sharedPref.getString(getString(R.string.preference_base_item)+i, "ERROR");
		}
		
		 HistoricalListAdapter adapter = new HistoricalListAdapter(this, values);
		 this.values = values;
		 setListAdapter(adapter);
	}
	
	protected void onListItemClick (ListView l, View v, int position, long id){
		Intent intent = new Intent(this, DataDump.class);
		intent.putExtra("position", position);
		startActivity(intent);
	}
	

}

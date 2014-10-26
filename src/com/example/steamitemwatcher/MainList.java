package com.example.steamitemwatcher;



import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainList extends ListActivity {

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
		int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
		String[] values = new String[count];
		for(int i=0; i<count; i++){
			values[i] = sharedPref.getString(getString(R.string.preference_base_item)+i, "ERROR");
		}
		
		 SteamArrayAdapter adapter = new SteamArrayAdapter(this, values);
		 setListAdapter(adapter);
	}
}

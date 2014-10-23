package com.example.steamitemwatcher;



import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

public class MainList extends ListActivity {

	SteamItemsAdapter siTable;
	Cursor c;
	
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
		//Grab the ID's from the SQLdatabase. Put them in a string and pass that to the adapter
		
		
		
		
	}
	
	
	
	
	
}

package com.example.steamitemwatcher;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class DeleteItem extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
		int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
		String[] values = new String[count];
		for(int i=0; i<count; i++){
			values[i] = sharedPref.getString(getString(R.string.preference_base_item)+i, "ERROR");
		}
		
		DeleteItemAdapter adapter = new DeleteItemAdapter(this, values);
		
		setListAdapter(adapter);
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
    	int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	for(int i=position; i<count-1; i++){
    		String temp = sharedPref.getString(getString(R.string.preference_base_item)+(i+1), "ERROR");
    		editor.putString(getString(R.string.preference_base_item)+i, temp);
    	}
    	editor.remove(getString(R.string.preference_base_item)+(count-1));
    	--count;
    	
    	editor.putInt(getString(R.string.preference_url_list_length), count);
    	editor.commit();
		
    	Context context = getApplicationContext();
    	CharSequence text = "Item Deleted!";
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	
    	
    	finish();
		
		
		
    }
	
	
	
}

package ventullo.steamitemwatcher;



import ventullo.steamitemwatcher.R.id;
import ventullo.steamitemwatcher.R.menu;
import ventullo.steamitemwatcher.R.string;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainList extends ListActivity {

	SteamArrayAdapter adapter;
	String[] values;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
		int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
		String[] values = new String[count];
		for(int i=0; i<count; i++){
			values[i] = sharedPref.getString(getString(R.string.preference_base_item)+i, "ERROR");
		}
		
		 SteamArrayAdapter adapter = new SteamArrayAdapter(this, values);
		 this.adapter = adapter;
		 this.values = values;
		 setListAdapter(adapter);
	}
	
	
	protected void onListItemClick (ListView l, View v, int position, long id){
		
		SteamArrayAdapter.ImageDownloader i = new SteamArrayAdapter.ImageDownloader();
		((TextView) v.findViewById(R.id.itemname)).setText("Loading...");
		((TextView) v.findViewById(R.id.price)).setText("");
		((TextView) v.findViewById(R.id.quantity)).setText("");
		i.downloadtexts(values[position], (TextView) v.findViewById(R.id.itemname), (TextView) v.findViewById(R.id.price), (TextView) v.findViewById(R.id.quantity));
		
	    
		
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainlistmenu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	        	setListAdapter(adapter);
	        	
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}

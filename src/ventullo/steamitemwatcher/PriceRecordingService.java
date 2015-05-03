package ventullo.steamitemwatcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ventullo.steamitemwatcher.R.string;


import android.app.IntentService;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;

public class PriceRecordingService extends IntentService {


	
	public PriceRecordingService() {
		super("PriceRecordingService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("Writing Files");
		ContextWrapper cw = new ContextWrapper(getApplicationContext());
	    File directory = cw.getDir("historyDir", Context.MODE_PRIVATE);
	       
	    //Store all my data as Time(Long) Price(String) Quantity(String)
	    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
		int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
	    
		for(int i=0; i<count; i++){
			String itemname = sharedPref.getString(getString(R.string.preference_base_item)+i, "ERROR");
			String itemfilename = itemname.replace('/', ' ');
			File temppath = new File(directory, itemfilename);
			PrintWriter out;
			
			try {
				out
				   = new PrintWriter(new BufferedWriter(new FileWriter(temppath, true)));
			} catch (IOException e) {
				
				e.printStackTrace();
				return;
			}
			int lines = 4;
			//For each item, 
			out.append((new Date(System.currentTimeMillis())).toString()+"\n");
			--lines;
			try {
		    	Document doc = Jsoup.connect("http://steamcommunity.com/market/search?q=" + itemname).get();
		    	Element e = doc.getElementsByClass("market_table_value").first().getElementsByAttribute("style").first();
		    	out.append(e.text()); 
		    	out.append("\n");
		    	--lines;
		    	e = doc.getElementsByClass("market_listing_num_listings_qty").first();
		    	out.append(e.text()); 
		    	out.append("\n\n");
		    	--lines; --lines;
			}catch (IOException e1) {
				for(int j = 0; j<lines-1; ++j){
					out.append("Error\n");
				}
				out.append("\n");
			}
			out.close();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	    
		
		DailyReceiver.completeWakefulIntent(intent);
	}
	
	
	

}

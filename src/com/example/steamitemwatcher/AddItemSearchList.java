package com.example.steamitemwatcher;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ListActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemSearchList extends ListActivity {

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//Display "Loading" Page before results obtained.
		Intent intent = getIntent();
		String search = intent.getStringExtra(AddItem.USER_SEARCH_STRING);
		setContentView(R.layout.additemlist);
		DownloadSearchResults(search);
		
		
		
		
		//Search with string. Get the names, and urls of the images.
		//Pass both to the adapter *via the callback*. The adapter calls its super with just the strings
		
		
		
		
	}
	
	
	
	public void onListItemClick(ListView l, View v, int position, long id){
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
    	int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	
    	TextView temp = (TextView) v.findViewById(R.id.itemname);
    	ImageView tempimage = (ImageView) v.findViewById(R.id.itempic);
    	Bitmap bitmap = drawableToBitmap(tempimage.getDrawable());
    	String name = temp.getText().toString();
    	
    	ContextWrapper cw = new ContextWrapper(getApplicationContext());
       File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
       File mypath=new File(directory,name + ".jpg");

       FileOutputStream fos = null;
       try {           

           fos = new FileOutputStream(mypath);

      // Use the compress method on the BitMap object to write image to the OutputStream
           bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
           fos.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
    	
    	
    	editor.putString(getString(R.string.preference_base_item)+count, name);
    	count++;
    	editor.putInt(getString(R.string.preference_url_list_length), count);
    	editor.commit();
    	Context context = getApplicationContext();
    	CharSequence text = "Item Added!";
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	
    	
    	
    	finish();
		
		
	}
	
	
	
	public void DownloadSearchResults(String search){
		SearchDownloaderTask task = new SearchDownloaderTask();
		task.execute(search);
	}
		
	class SearchDownloaderTask extends AsyncTask<String, Void, String[]>{
		
		
		public SearchDownloaderTask(){}
		
		protected String[] doInBackground(String... params){
			
			
			try {
				
		    	Document doc = Jsoup.connect("http://steamcommunity.com/market/search?q=" + params[0]).get();
				String[] nameurls = new String[18];
				int i;
		    	for(i = 0; i < 9; i++){
		    		Element e1 = doc.getElementById("result_" + i + "_name");
			    	Element e2 = doc.getElementById("result_" + i + "_image");
			    	if (e1==null) break;
			    	nameurls[2*i] = e1.text();
			    	nameurls[2*i+1] = e2.getElementsByTag("img").first().attr("src");
		    	}
		    	if(i==0){
		    		String[] ret = new String[1];
		    		Element e = doc.getElementsByClass("market_listing_table_message").first();
		    		if(e!=null) ret[0] = e.text();
		    		else ret[0] = "An error occurred while connecting to Steam servers.";
		    		return ret;
		    	}
		    	
		    	
				String[] ret = new String[2*i];
				System.arraycopy(nameurls, 0, ret, 0, 2*i);
				return ret;
				
				
				
				
			} catch (IOException e1) {
				String[] ret = new String[1];
				ret[0] = "An error occurred while connecting to Steam servers.";
				return ret;
				//e1.printStackTrace();
			}
			
		}
		
		protected void onPostExecute(String[] both){
			TextView empty = (TextView) findViewById(R.id.empty);
			if(both.length == 1){
				empty.setText(both[0]);
				return;
			}
			
			
			String[] names = new String[both.length/2];
			String[] picurls = new String[both.length/2];
			for(int i = 0; i<both.length/2; i++){
				names[i] = both[2*i];
				picurls[i] = both[2*i + 1];
			}
			
			empty.setVisibility(View.GONE);
			AddItemSearchAdapter adapter = new AddItemSearchAdapter(AddItemSearchList.this, names, picurls);
			AddItemSearchList.this.setListAdapter(adapter);
			return;
			
		}
		
		
	}
	
	
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	
	
}

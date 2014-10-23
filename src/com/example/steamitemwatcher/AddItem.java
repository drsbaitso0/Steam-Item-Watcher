package com.example.steamitemwatcher;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.example.steamitemwatcher.SteamArrayAdapter.ImageDownloader;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddItem extends ActionBarActivity {

	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_page);
    }
	
	
	 public void addItem(View view){
	    	
	    	EditText editText = (EditText) findViewById(R.id.edit_message);
	    	String description = editText.getText().toString();
	    	SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
	    	int count = sharedPref.getInt(getString(R.string.preference_url_list_length), 0);
	    	SharedPreferences.Editor editor = sharedPref.edit();
	    	editor.putString(getString(R.string.preference_base_item)+count, description);
	    	count++;
	    	editor.putInt(getString(R.string.preference_url_list_length), count);
	    	editor.commit();
	    	
	    	
	    	
	    	ImageDownloader i = new ImageDownloader();
		    
		    
		    
		    
		    i.downloadpic(description);
	    	
	    	
	    	
	    	
	    	Context context = getApplicationContext();
	    	CharSequence text = "Item Added!";
	    	int duration = Toast.LENGTH_SHORT;

	    	Toast toast = Toast.makeText(context, text, duration);
	    	toast.show();
	    	
	    	
	    	
	    	finish();
	 }
	 
	 public class ImageDownloader {
		 public void downloadpic(String description) {
	         BitmapDownloaderTask task = new BitmapDownloaderTask(description);
	         task.execute(description);
		 }
	 }
 
	 
	 
	 
	 
	 class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		    
		    private final String description;

		    public BitmapDownloaderTask(String d) {
		        description = d;
		    }

		    @Override
		    // Actual download method, run in the task thread
		    protected Bitmap doInBackground(String... params) {
		        String pictureurl;
		        try {
					
			    	Document doc = Jsoup.connect("http://steamcommunity.com/market/search?q=" + params[0]).get();
					Element e = doc.getElementById("result_0_image");
					pictureurl = e.getElementsByTag("img").first().attr("src");
					
				} catch (IOException e1) {
					pictureurl = "http://www.clker.com/cliparts/Z/u/b/a/j/i/big-red-x-md.png";
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
		    	
		    	// params comes from the execute() call: params[0] is the url.
		         return downloadBitmap(pictureurl);
		    }

		    @Override
		    // Once the image is downloaded, associates it to the imageView
		    protected void onPostExecute(Bitmap bitmap) {
		        if (isCancelled()) {
		            bitmap = null;
		        }
		        ContextWrapper cw = new ContextWrapper(getApplicationContext());
		         // path to /data/data/yourapp/app_data/imageDir
		        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		        // Create imageDir
		        File mypath=new File(directory,description + ".jpg");

		        FileOutputStream fos = null;
		        try {           

		            fos = new FileOutputStream(mypath);

		       // Use the compress method on the BitMap object to write image to the OutputStream
		            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		            fos.close();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}
	 
	 
	 
	 
	 
	 
	 
	 
	 
		
		
		static Bitmap downloadBitmap(String url) {
		    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		    final HttpGet getRequest = new HttpGet(url);

		    try {
		        HttpResponse response = client.execute(getRequest);
		        final int statusCode = response.getStatusLine().getStatusCode();
		        if (statusCode != HttpStatus.SC_OK) { 
		            Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
		            return null;
		        }
		        
		        final HttpEntity entity = response.getEntity();
		        if (entity != null) {
		            InputStream inputStream = null;
		            try {
		                inputStream = entity.getContent(); 
		                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		                return bitmap;
		            } finally {
		                if (inputStream != null) {
		                    inputStream.close();  
		                }
		                entity.consumeContent();
		            }
		        }
		    } catch (Exception e) {
		        // Could provide a more explicit error message for IOException or IllegalStateException
		        getRequest.abort();
		        Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e);
		    } finally {
		        if (client != null) {
		            client.close();
		        }
		    }
		    return null;
		}
	 
	 
	
	
}

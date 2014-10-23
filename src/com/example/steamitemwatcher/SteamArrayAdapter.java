package com.example.steamitemwatcher;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SteamArrayAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	
	
	
	public SteamArrayAdapter(Context context, String[] values) {
	    super(context, R.layout.rowlayout, values);
	    this.values = values;
	    this.context = context;
	  }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
	    
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.itempic);
	    TextView Name_TextView = (TextView) rowView.findViewById(R.id.itemname);
	    TextView Price_TextView = (TextView) rowView.findViewById(R.id.price);
	    TextView Quant_TextView = (TextView) rowView.findViewById(R.id.quantity);
	    
	    
	    String description = values[position];
	   
	    
	    ImageDownloader i = new ImageDownloader();
	    
	    imageView.setImageResource(R.drawable.ic_loading);
	    Name_TextView.setText("Loading...");
	    ContextWrapper cw = new ContextWrapper(((Activity) context).getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
       File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
       try {
	         File f=new File(directory,description + ".jpg");
	         Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
	         imageView.setImageBitmap(b);
	     } 
	     catch (FileNotFoundException e) 
	     {
	         e.printStackTrace();
	     }
	    
	    //i.downloadpic(description, imageView);
	    i.downloadtexts(description, Name_TextView, Price_TextView, Quant_TextView);
	    
	   
	    
	    return rowView;
	}
	
	
	
	
	
	
	
	
	
	
	public class ImageDownloader {
		
		public void downloadtexts(String url, TextView Name, TextView Price, TextView Quant){
			TextDownloaderTask task = new TextDownloaderTask(Name, Price, Quant);
			task.execute(url);
		}
		
	    public void downloadpic(String url, ImageView imageView) {
	            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
	            task.execute(url);
	        }
	    }

	    /* class BitmapDownloaderTask, see below */
	
	class TextDownloaderTask extends AsyncTask<String, Void, String[] >{
		
		private final WeakReference<TextView> NameViewReference;
		private final WeakReference<TextView> PriceViewReference;
		private final WeakReference<TextView> QuantViewReference;
		
		public TextDownloaderTask(TextView Name, TextView Price, TextView Quant){
			NameViewReference = new WeakReference<TextView>(Name);
			PriceViewReference = new WeakReference<TextView>(Price);
			QuantViewReference = new WeakReference<TextView>(Quant); 
			//Make these weak in case my program determines it no longer needs them; it sets them to null
			//and I no longer need to do the assignments below.
		}
		
		protected String[] doInBackground(String... params){
				String[] v = new String[3];
				try {
			    	Document doc = Jsoup.connect("http://steamcommunity.com/market/search?q=" + params[0]).get();
					Element e = doc.getElementById("result_0_name");
					v[0] = e.text();
					e = doc.getElementsByClass("market_listing_num_listings_qty").first();
					v[2] = e.text();
					e = doc.getElementsByClass("market_table_value").first().getElementsByAttribute("style").first();
					v[1] = e.text();
					
				} catch (IOException e1) {
					String[] u = new String[3];
					u[0] = "Error";
					return u;
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				return v;
		}
		
		protected void onPostExecute(String[] strings) {
	        if (isCancelled()) {
	            strings = null;
	        }

	        if (NameViewReference != null && PriceViewReference != null && QuantViewReference != null) {
	            TextView NameView = NameViewReference.get();
	            TextView PriceView = PriceViewReference.get();
	            TextView QuantView = QuantViewReference.get();
	            if (NameView != null && PriceView!=null && QuantView!=null) {
	                NameView.setText(strings[0]);
	                PriceView.setText(strings[1]);
	                QuantView.setText(strings[2]);
	            }
	        }
	    }
	}
	
	
	
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	    
	    private final WeakReference<ImageView> imageViewReference;

	    public BitmapDownloaderTask(ImageView imageView) {
	        imageViewReference = new WeakReference<ImageView>(imageView);
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

	        if (imageViewReference != null) {
	            ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
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

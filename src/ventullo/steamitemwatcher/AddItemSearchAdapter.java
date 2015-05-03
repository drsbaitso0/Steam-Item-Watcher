package ventullo.steamitemwatcher;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import ventullo.steamitemwatcher.R.id;
import ventullo.steamitemwatcher.R.layout;


import android.content.Context;
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

public class AddItemSearchAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final String[] names;
	private final String[] urls;
	
	
	public AddItemSearchAdapter(Context context, String[] names, String[] urls){
		super(context, R.layout.additemrowlayout, names);
		this.context = context;
		this.names = names;
	    this.urls = urls;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.additemrowlayout, parent, false);
		    ImageView imageView = (ImageView) rowView.findViewById(R.id.itempic);
		    TextView Name_TextView = (TextView) rowView.findViewById(R.id.itemname);
		    Name_TextView.setText(names[position]);
		    downloadpic(urls[position], imageView);
		    return rowView;
	}
	
	
	
	public void downloadpic(String url, ImageView imageView) {
        BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
        task.execute(url);
    }

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	    
	    private final WeakReference<ImageView> imageViewReference;

	    public BitmapDownloaderTask(ImageView imageView) {
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

	    @Override
	    // Actual download method, run in the task thread
	    protected Bitmap doInBackground(String... params) {
	         return downloadBitmap(params[0]);
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

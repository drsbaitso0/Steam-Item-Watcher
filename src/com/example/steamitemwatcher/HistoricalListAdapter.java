package com.example.steamitemwatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoricalListAdapter extends ArrayAdapter<String> {

	String[] values;
	Context context;

	public HistoricalListAdapter(Context context, String[] values) {
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
		    String description = values[position];
		    
		    ContextWrapper cw = new ContextWrapper(((Activity) context).getApplicationContext());
		    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		       try {
		    	   	String descriptionimage = description.replace('/', ' ');
			         File f=new File(directory,descriptionimage + ".jpg");
			         Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
			         imageView.setImageBitmap(b);
			     } 
			     catch (FileNotFoundException e) 
			     {
			         e.printStackTrace();
			     }
		    Name_TextView.setText(values[position]);
		    return rowView;
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

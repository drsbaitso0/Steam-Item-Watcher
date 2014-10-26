package com.example.steamitemwatcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeleteItemAdapter extends ArrayAdapter<String> {

	
	private final Context context;
	private final String[] values;
	
	
	
	public DeleteItemAdapter(Context context, String[] values) {
	    super(context, R.layout.rowlayout, values);
	    this.values = values;
	    this.context = context;
	  }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.delete_row_layout, parent, false);
	    
	    TextView item = (TextView) rowView.findViewById(R.id.item_to_delete);
	    item.setText(values[position]);
	    return rowView;
	}
	
	
	
	
	
}

package com.example.steamitemwatcher;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MarkerView;

public class SteamMarkerView extends MarkerView {

	private TextView tvContent;
	
	public SteamMarkerView(Context context, int layoutResource) {
		super(context, layoutResource);
		tvContent = (TextView) findViewById(R.id.tvContent);
	}

	@Override
	public void refreshContent(Entry e, int dataSetIndex) {
		if(e.getData() == null)
			tvContent.setText("$" + String.format("%.2f", e.getVal()));
		else{ 
			//if (dataSetIndex == 1)
			tvContent.setText("" + (int) (float) e.getData());
		}
	}

	@Override
	public void draw(Canvas canvas, float posx, float posy)
	{
	    // take offsets into consideration
	    posx += -(getWidth() / 2);
	    posy += -getHeight();

	    // translate to the correct position and draw
	    canvas.translate(posx, posy);
	    draw(canvas);
	    canvas.translate(-posx, -posy);
	}

}

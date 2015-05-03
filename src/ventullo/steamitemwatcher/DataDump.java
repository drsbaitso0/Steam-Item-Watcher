package ventullo.steamitemwatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ventullo.steamitemwatcher.R.id;
import ventullo.steamitemwatcher.R.layout;
import ventullo.steamitemwatcher.R.menu;
import ventullo.steamitemwatcher.R.string;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartGestureListener;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

public class DataDump extends FragmentActivity implements OnSeekBarChangeListener,
OnChartGestureListener, OnChartValueSelectedListener{
	public static final int holo_orange_dark = 17170457;
	private LineChart mChart;
	private SeekBar priceToQuantRatio;
	private CheckBox priceCheck, quantCheck;
	private float a, b;
	private boolean circlesOn;
	private ArrayList<String> dateVals;
	private ArrayList<String> verbDateVals;
	private ArrayList<String> brevDateVals;
	
	private ArrayList<Entry> priceVals;
	private ArrayList<Entry> quantVals;
	
	int position;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.data_dump_layout);
		
		mChart = (LineChart) findViewById(R.id.chart1);
		priceToQuantRatio = (SeekBar) findViewById(R.id.priceToQuantSeek);
		priceCheck = (CheckBox) findViewById(R.id.showPrice);
		quantCheck = (CheckBox) findViewById(R.id.showQuant);
		circlesOn = false;
		
		priceCheck.setChecked(true);
		quantCheck.setChecked(true);
		
		priceToQuantRatio.setProgress(100);
		
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		
		
		ContextWrapper cw = new ContextWrapper(getApplicationContext());
	    File directory = cw.getDir("historyDir", Context.MODE_PRIVATE);
	       
	    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_url_list), 0);
		
	    String itemname = sharedPref.getString(getString(R.string.preference_base_item)+position, "ERROR");
		String itemfilename = itemname.replace('/', ' ');
		File file = new File(directory, itemfilename);
		
		try{
			FileInputStream fis = new FileInputStream(file);
			Scanner sc = new Scanner(fis);
			
			int i = 0;
			verbDateVals = new ArrayList<String>();
			brevDateVals = new ArrayList<String>();
			dateVals = new ArrayList<String>();
			priceVals = new ArrayList<Entry>();
			quantVals = new ArrayList<Entry>();
			
			float maxprice = 0, maxquant = 0, minprice = Float.MAX_VALUE, minquant = Float.MAX_VALUE;
			
			while(sc.hasNextLine()){
				String date = sc.nextLine();
				
				
				String stringprice = sc.nextLine();
				String stringquant = sc.nextLine().replaceAll(",","");
				sc.nextLine();
				Pattern pattern = Pattern.compile("\\d*\\.\\d*");
				Matcher matcher = pattern.matcher(stringprice);
				matcher.find();
				
				
				float price;
				float quant;
				try{
					price = Float.parseFloat(matcher.group());
					quant = Integer.parseInt(stringquant);
				}
				catch(Exception e){
					++i;
					continue;
				}
				
				maxprice = Math.max(maxprice, price);
				maxquant = Math.max(maxquant, quant);
				minprice = Math.min(minprice, price);
				minquant = Math.min(minquant, quant);
				
				verbDateVals.add(date);
				brevDateVals.add(date.substring(4,10));
				
		        priceVals.add(new Entry(price, i));
		        quantVals.add(new Entry(quant, i));
		        
		        ++i;
		        
			}
			dateVals = brevDateVals;
			
			minquant = Math.max(minquant, 1);
			maxquant = Math.max(maxquant, 2);
			float twicemaxp2qrat = 2*maxprice/minquant;
			float halfminp2qrat = minprice/(2*maxquant);
			b = (float) Math.log(halfminp2qrat);
			a = (float) (Math.log(twicemaxp2qrat) - Math.log(halfminp2qrat))/200;
			
			//(0, 200) -> add->mult -> (halfmin, twicemax)
			//(0, 200) -> add'ly -> (log(halfmin), log(twicemax))
			// x |-> ax + b; a = (log(twicemax) - log(halfmin))/200, b = log(halfmin)
			// f(x) = exp(ax + b)
			
			//quantvals are not accurate here though, need to be modified.
			
			//ArrayList<Entry> scaledquantVals = somemethodtobedefined(quantvals, seekbarval 100);
			
			setGraph(dateVals, priceVals, quantVals);
			sc.close();
			
			
			
			
			
			//fis.read(data);
			fis.close();
			//str = new String(data, "UTF-8");
		}catch(IOException e){
			return;
		}
		
		
	}
	
	private ArrayList<Entry> rescaleQuants(ArrayList<Entry> quantvals, float progress){
		ArrayList<Entry> rtn = new ArrayList<Entry>();
		for(Entry e : quantvals){
			Entry scalede = new Entry((float) Math.exp(a*progress + b)*e.getVal(), e.getXIndex(), (Float) e.getVal());
			rtn.add(scalede);
		}
		return rtn;
	}
	
	
	
	
	private void setGraph(ArrayList<String> dateVals, ArrayList<Entry> priceVals, ArrayList<Entry> quantVals){
		
		
		priceToQuantRatio.setOnSeekBarChangeListener(this);
		mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data yet.");
        mChart.setUnit("");
        mChart.setDrawUnitsInChart(true);

        mChart.setStartAtZero(false);
        mChart.setDrawYValues(false);
        mChart.setDrawBorder(true);
        mChart.setBorderPositions(new BorderPosition[] {
                BorderPosition.BOTTOM
        });
        mChart.setHighlightEnabled(true);
        mChart.setHighlightIndicatorEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);

        
        mChart.setDrawGridBackground(false);
        mChart.setDrawVerticalGrid(true);
        mChart.setDrawHorizontalGrid(true);
        
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        
        LineDataSet priceset = new LineDataSet(priceVals, "Price");
        LineDataSet quantset = new LineDataSet(rescaleQuants(quantVals, 100), "Quantity"); //rescale the Quantity values to be at the midpoint of my scaling function
        
        priceset.setColor(ColorTemplate.getHoloBlue());
        priceset.setCircleColor(ColorTemplate.getHoloBlue());
        priceset.setLineWidth(2f);
        priceset.setCircleSize(4f);
        priceset.setFillAlpha(65);
        priceset.setFillColor(ColorTemplate.getHoloBlue());
        priceset.setDrawCircles(circlesOn);

        quantset.setColor(Color.rgb(255, 255, 0));
        quantset.setCircleColor(Color.rgb(255, 255, 0));
        quantset.setLineWidth(2f);
        quantset.setCircleSize(4f);
        quantset.setFillAlpha(65);
        quantset.setFillColor(Color.rgb(255, 255, 0));
        quantset.setDrawCircles(circlesOn);
        
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(priceset); 
        dataSets.add(quantset);
        
        LineData data = new LineData(dateVals, dataSets);
        SteamMarkerView mv = new SteamMarkerView(this, R.layout.custom_marker_view);
        mChart.setMarkerView(mv);
        
        
        mChart.setData(data);
        
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.LINE);
        //l.setTypeface(tf);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(13f);
        l.setFormToTextSpace(1f);
        
        
        XLabels xl = mChart.getXLabels();
        xl.setTextColor(Color.WHITE);
        
        
        YLabels yl = mChart.getYLabels();
        yl.setTextColor(Color.WHITE);
        
        
        yl.setFormatter(new PriceQuantityFormatter(a,b,100));
      	//MyFormatter will be a ValueFormatter whose getformattervalue method returns 
      	//a string containing both the value it's passed (the price), as well as quantity i.e. price divided by exp(a*progress+b). Perhaps rounded in some fashion.
      	//String x = new DecimalFormat("@@").format("3.14159"); returns 3.1
      	//exp(a*progress + b) is the p/q ratio. 
        
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		//In here we'll have to do a few things.
		
		
		
		YLabels yl = mChart.getYLabels();
		yl.setFormatter(new PriceQuantityFormatter(a, b, progress));
		 
		
		
		LineDataSet priceset = new LineDataSet(priceVals, "Price");
        LineDataSet quantset = new LineDataSet(rescaleQuants(quantVals, progress), "Quantity");
        
        priceset.setColor(ColorTemplate.getHoloBlue());
        priceset.setCircleColor(ColorTemplate.getHoloBlue());
        priceset.setLineWidth(2f);
        priceset.setCircleSize(4f);
        priceset.setFillAlpha(65);
        priceset.setFillColor(ColorTemplate.getHoloBlue());
        priceset.setDrawCircles(circlesOn);
        quantset.setColor(Color.rgb(255, 255, 0));
        quantset.setCircleColor(Color.rgb(255, 255, 0));
        quantset.setLineWidth(2f);
        quantset.setCircleSize(4f);
        quantset.setFillAlpha(65);
        quantset.setFillColor(Color.rgb(255, 255, 0));
        quantset.setDrawCircles(circlesOn);
        
        
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        if(priceCheck.isChecked()) dataSets.add(priceset);
        if(quantCheck.isChecked()) dataSets.add(quantset);
        
        LineData data = new LineData(dateVals, dataSets);
        mChart.setData(data);
        
		mChart.invalidate();
		
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.datamenu, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {

	        switch (item.getItemId()) {
	            case R.id.actionToggleCircles: {
	            	circlesOn = !circlesOn; 
	            	ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
	                         .getDataSets();

	                 for (LineDataSet set : sets) {
	                     if (set.isDrawCirclesEnabled())
	                         set.setDrawCircles(false);
	                     else
	                         set.setDrawCircles(true);
	                 }
	                 mChart.invalidate();
	                 break;
	            }
	            case R.id.actionToggleDateVerbose: {
	                if(dateVals == verbDateVals) dateVals = brevDateVals;
	                else dateVals = verbDateVals;
	                onProgressChanged(priceToQuantRatio, priceToQuantRatio.getProgress(), true);
	                break;
	            }
	        }
	        return true;
	    }

	
	public void onPriceBoxClicked(View pricebox){
		mChart.highlightValue(-1,-1);
		onProgressChanged(priceToQuantRatio, priceToQuantRatio.getProgress(), true);
	}
	
	public void onQuantBoxClicked(View quantbox){
		mChart.highlightValue(-1,-1);
		onProgressChanged(priceToQuantRatio, priceToQuantRatio.getProgress(), true);
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChartLongPressed(MotionEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChartDoubleTapped(MotionEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChartSingleTapped(MotionEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	

}

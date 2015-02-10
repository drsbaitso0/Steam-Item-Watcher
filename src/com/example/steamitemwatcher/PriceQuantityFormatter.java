package com.example.steamitemwatcher;

import java.text.DecimalFormat;

import com.github.mikephil.charting.utils.ValueFormatter;

public class PriceQuantityFormatter implements ValueFormatter{

	double scalefactor;
	
	public PriceQuantityFormatter(double a, double b, double progress) {
		scalefactor = Math.exp(-a*progress - b);
	}

	
	public String getFormattedValue(float value){
		int quant = (int) (scalefactor*value);
		
		return ("$" + String.format("%.2f",value) + "/" + quant);
	}
	
}

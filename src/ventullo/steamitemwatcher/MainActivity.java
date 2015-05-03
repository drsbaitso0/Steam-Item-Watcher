package ventullo.steamitemwatcher;



import ventullo.steamitemwatcher.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class MainActivity extends FragmentActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		
		
		
	}

	

	
	
	
	public void viewList(View view){
		Intent intent = new Intent(this, MainList.class);
		startActivity(intent);
	}
	
	public void addItem(View view){
		Intent intent = new Intent(this, AddItem.class);
		startActivity(intent);
	}
	
	
	public void deleteItem(View view){
		Intent intent = new Intent(this, DeleteItem.class);
		startActivity(intent);
	}
	
	
	public void historicalList(View view){
		Intent intent = new Intent(this, HistoricalList.class);
		startActivity(intent);
	}
	
	public void settings(View view){
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}
	
	
}

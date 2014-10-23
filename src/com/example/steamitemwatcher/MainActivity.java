package com.example.steamitemwatcher;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {

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
		//Inside here we instantiate a helper class, call getWritableDatabase to get an SQLiteDatabase object db.
		//Then populate a ContentValues object with your data, the run db.insert(...) on this object.
		
	}
	
	
	public void deleteItem(View view){
		Intent intent = new Intent(this, DeleteItem.class);
		startActivity(intent);
		
		
	}
	
	
	
	
	
	
	
}

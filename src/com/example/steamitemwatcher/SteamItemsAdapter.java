package com.example.steamitemwatcher;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class SteamItemsAdapter {

	// Database table name
	public static final String DATABASE_TABLE_1 = "steam_items";

	// Database table columns for DATABASE_TABLE_1
	public static final String ROWID = "_id";
	public static final String LISTING_URL = "listing_url";
	public static final String ITEM_NAME = "item_name";
	public static final String PRICE = "price";
	public static final String QUANTITY = "quantity";

	// Object for SQLiteDatabase
	private SQLiteDatabase database;

	// 
	public static final String TAG = "STEAM_ITEMS_TABLE";	
	private SteamItemsDBHelper steam_items_db_helper;

	public SteamItemsAdapter() {
	}

	public SteamItemsAdapter open(Context context) throws SQLException {
		Log.i(TAG, "OPening DataBase Connection....");
		steam_items_db_helper = new SteamItemsDBHelper(context);
		database = steam_items_db_helper.getWritableDatabase();
		return this;
	}

	public void close() {
		database.close();
	}

	public boolean deleteSteamItem(long rowId) {
		return database.delete(DATABASE_TABLE_1, ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllSteamItems() {	 
		return database.query(DATABASE_TABLE_1, new String[] {ROWID, LISTING_URL , ITEM_NAME, QUANTITY, PRICE}, null, null, null, null, ITEM_NAME);
	}

	public Cursor fetchCommonName(long steamItemId) throws SQLException {

		Cursor mCursor = database.query(true, DATABASE_TABLE_1, new String[] {
				ROWID, LISTING_URL , ITEM_NAME, QUANTITY, PRICE}, ROWID + "=" +
						steamItemId, null, null, null, null, null);

		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	public void insert(String url){
		
		
		
	}
	
	
	class GetItemsTask extends AsyncTask<String, Void, Void> {
		//they've already run constructor and open
		private final String url;
		
		public GetItemsTask(String s){
			url = s;
		}
		
		protected Void doInBackground(String... params){
				String itemname;
				String price;
				String quantity;
				
			
				try {
				
		    	Document doc = Jsoup.connect(params[0]).get(); //make this position later!!!
				
				
				Element e = doc.getElementsByClass("market_listing_nav").first();
				itemname = e.getElementsByTag("a").get(1).text();
				
				
				
				
			} catch (IOException e1) {
				
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
			
			
			
			return null;
		}
		
		
	}
	

	/**public Cursor fetchCommonNameCount(long commonNameId) throws SQLException {

		Cursor mCursor = database.query(true, DATABASE_TABLE_1, new String[] {COMMON_NAME_ROWID, COMMON_NAME_COUNT}, 
				COMMON_NAME_ROWID + "=" + commonNameId, null, null, null, null, null);

		if(mCursor!=null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateCommonName(int commonNameId, String commonName, String commonNameCount) {
		ContentValues args = new ContentValues();
		args.put(COMMON_NAME, commonName);
		args.put(COMMON_NAME_COUNT, commonNameCount);

		return database.update(DATABASE_TABLE_1, args, COMMON_NAME_ROWID + "=" + commonNameId, null) > 0;
	}*/
}
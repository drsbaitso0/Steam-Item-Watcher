package com.example.steamitemwatcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SteamItemsDBHelper extends SQLiteOpenHelper {

	private final Context context;
	
	public static final String DATABASE_NAME = "item_names_database";
	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_TABLE_1 = "steam_items";

	// Columns present in DATABASE_TABLE
	public static final String ROWID = "_id";
	public static final String LISTING_URL = "listing_url";
	public static final String ITEM_NAME = "item_name";
	public static final String QUANTITY = "quantity";
	public static final String PRICE = "price";

	// SQL query string for creating DATABASE_TABLE
	static final String CREATE_DATABASE_TABLE_1 =
	        "create table if not exists" + DATABASE_TABLE_1 + "(" + ROWID + 
	        " integer primary key autoincrement, " + LISTING_URL +
	        " text, " + QUANTITY +
	        " text, " + PRICE + " text, " + ITEM_NAME + " text)";

	// Constructor
	public SteamItemsDBHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    this.context = context;
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DATABASE_TABLE_1);
		ContentValues initialValues = new ContentValues();

		initialValues.put(ROWID, 0);
	    initialValues.put(LISTING_URL, "http://steamcommunity.com/market/listings/730/AWP%20%7C%20BOOM%20%28Factory%20New%29");
	    initialValues.put(PRICE, "Five Bucks");
	    initialValues.put(QUANTITY, "11");
	    initialValues.put(ITEM_NAME, "Awp boom");

	    db.insert(DATABASE_TABLE_1, null, initialValues);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

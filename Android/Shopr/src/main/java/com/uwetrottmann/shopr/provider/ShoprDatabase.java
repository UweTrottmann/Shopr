package com.uwetrottmann.shopr.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoprDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "shopr.db";
	
	public static final int DBVER_INITIAL = 1;

	public static final int DATABASE_VERSION = DBVER_INITIAL;
	
	public ShoprDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}

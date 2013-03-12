package com.clommunicate.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClommunicateSQLiteHelper extends SQLiteOpenHelper {
	
	public static String TABLE = "tasks" + User.user.getId();

	private static final String DATABASE_NAME = "stats.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement

	public ClommunicateSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		//database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ClommunicateSQLiteHelper.TABLE);
		onCreate(db);
	}

}

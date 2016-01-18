package com.aj.recyclerviewuse;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "dictionary";

	// Contacts table name
	private static final String TABLE_DATA = "data";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_WORD = "word";
	private static final String KEY_MEANING = "meaning";
	private static final String KEY_RATIO = "ratio";
	public List<WordObject> words;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_WORD_TABLE = "CREATE TABLE " + TABLE_DATA + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_WORD + " TEXT," + KEY_MEANING
				+ " TEXT," + KEY_RATIO + " TEXT" + ")";
		db.execSQL(CREATE_WORD_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

		// Create tables again
		onCreate(db);
	}

	public void insertdata(int id, String word, String meaning, String ratio) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, id);
		values.put(KEY_WORD, word); // user data
		values.put(KEY_MEANING, meaning);
		values.put(KEY_RATIO, ratio);
		// Inserting Row
		db.insert(TABLE_DATA, null, values);
		db.close(); // Closing database connection
	}

	public List<WordObject> getAllData() {
		words = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DATA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor == null) {
			return null;
		}
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				words.add(new WordObject(cursor.getString(1), cursor
						.getString(2), "http://appsculture.com/vocab/images/"
						+ cursor.getString(0) + ".png"));
				Log.e("check",
						"http://appsculture.com/vocab/images/"
								+ cursor.getString(0) + ".png");

				// Adding data to list

			} while (cursor.moveToNext());
		}

		// return data List
		return words;
	}
}

package com.iotta.mydays.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.iotta.mydays.utils.Logger;

import java.util.ArrayList;

/**
 * Created by Galina Litkin on 05/08/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "iotta.db";
    private static final String TABLE_DATES = "dates";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_MONTH = "month";

    private static final String CREATE_TABLE_DATES = "CREATE TABLE " + TABLE_DATES +
            "(" + KEY_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
            + KEY_DATE + " INTEGER ,"
            + KEY_MONTH + " TEXT " + ", CONSTRAINT unique_date UNIQUE(" + KEY_DATE + "," + KEY_MONTH + ")" +
            ");";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DATES);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.warn("Upgrading the database from version " + oldVersion + " to " + newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DATES);
        // recreate the tables
        onCreate(db);
    }

    public long addDate(MyDate date) {
        // Opening db connection
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date.getDay());
        values.put(KEY_MONTH, date.getMonth());

        long dateId = db.insert(TABLE_DATES, null, values);
        db.close();
        return dateId;
    }

    public ArrayList<MyDate> getAllDates() {

        ArrayList<MyDate> tracksList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DATES + " ORDER BY " + KEY_DATE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MyDate track = new MyDate(cursor.getInt(1), cursor.getString(2));
                tracksList.add(track);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tracksList;
    }
}

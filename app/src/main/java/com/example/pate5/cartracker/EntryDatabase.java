package com.example.pate5.cartracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EntryDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EntryLog";

    // Entry data that is common
    private static final String KEY_ID = "id";
    private static final String KEY_ENTRYID = "entryid";
    private static final String TABLE_ENTRIES = "entries";
    private static final String KEY_DATE = "date";
    private static final String KEY_ODOMETER = "odometer";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_TYPE = "entrytype";

    // Entry data for gas type entries
    private static final String KEY_GASPRICE = "gasprice";
    private static final String KEY_GASVOLUME = "gasvolume";
    private static final String KEY_GASTOTAL = "gastotal";

    // Entry data for repair type entries
    private static final String KEY_REPAIRPRICE = "repairprice";
    private static final String KEY_REPAIRNAME = "repairname";

    // Entry data for maintenance type entries
    private static final String KEY_MAINTTYPE = "mainttype";
    private static final String KEY_MAINTCOST = "maintcost";

    public EntryDatabase(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String TableCreate_sql = "CREATE TABLE " + TABLE_ENTRIES + "(";
        TableCreate_sql += KEY_ID + " INTEGER PRIMARY KEY,";
        TableCreate_sql += KEY_ENTRYID + " TEXT,"; // String 1
        TableCreate_sql += KEY_DATE + " TEXT,";
        TableCreate_sql += KEY_ODOMETER + " TEXT,";
        TableCreate_sql += KEY_COMMENTS + " TEXT,";
        TableCreate_sql += KEY_TYPE + " TEXT,";
        TableCreate_sql += KEY_GASPRICE + " TEXT,";
        TableCreate_sql += KEY_GASVOLUME + " TEXT,";
        TableCreate_sql += KEY_GASTOTAL + " TEXT,";
        TableCreate_sql += KEY_REPAIRPRICE + " TEXT,";
        TableCreate_sql += KEY_REPAIRNAME + " TEXT,";
        TableCreate_sql += KEY_MAINTTYPE + " TEXT,";
        TableCreate_sql += KEY_MAINTCOST + " TEXT)"; //String 12
        database.execSQL(TableCreate_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldV, int newV) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(database);
    }

    // To store information, the subject of the information will be in REPAIRNAME
    // Re-purposing this keeps the database simpler.
    public void addInformation(String subject, String body, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        // If entry exists, update by deleting it.
        try {
            String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;
            Cursor cursor = db.rawQuery(sqlQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(5).equals("INFORMATION")) {
                        if (cursor.getString(10).equals(subject)) {
                            db.delete(TABLE_ENTRIES, KEY_ID + "=?", new String[]{
                                    String.valueOf(cursor.getLong(cursor.getColumnIndex("id")))});
                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Could not delete information row: " + e.toString());
        }

        ContentValues entryAttribs = new ContentValues();
        entryAttribs.put(KEY_TYPE, "INFORMATION");
        entryAttribs.put(KEY_REPAIRNAME, subject);
        entryAttribs.put(KEY_COMMENTS, body);
        entryAttribs.put(KEY_MAINTTYPE, value);
        db.insert(TABLE_ENTRIES, null, entryAttribs);
        db.close();
    }

    public String getInformation(String subject) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        // KEY_REPAIRNAME was used to hold the subject of the information
        // because it is a string value, and keeps the database simpler.

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(5).equals("INFORMATION")) {
                    if (cursor.getString(10).equals(subject)) {
                        return cursor.getString(4);
                    }
                }
            } while (cursor.moveToNext());
        }
        return null;
    }

    public String getInformationValue(String subject) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        // KEY_MAINTTYPE was used to hold the numerical value of the option bar
        // because it is a string value, and keeps the database simpler.

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(5).equals("INFORMATION")) {
                    if (cursor.getString(10).equals(subject)) {
                        return cursor.getString(11);
                    }
                }
            } while (cursor.moveToNext());
        }
        return null;
    }


}

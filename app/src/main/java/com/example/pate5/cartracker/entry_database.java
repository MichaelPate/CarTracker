package com.example.pate5.cartracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

class entry_database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EntryLog";
    // Entry data that is common
    private static final String KEY_ID = "id";
    private static final String KEY_ENTRYID = "entryid";
    private static final String TABLE_ENTRIES = "entries";
    static final String KEY_DATE = "date";
    static final String KEY_ODOMETER = "odometer";
    static final String KEY_COMMENTS = "comments";
    static final String KEY_TYPE = "entrytype";
    // Entry data for gas type entries
    static final String KEY_GASPRICE = "gasprice";
    static final String KEY_GASVOLUME = "gasvolume";
    static final String KEY_GASTOTAL = "gastotal";
    // Entry data for repair type entries
    static final String KEY_REPAIRPRICE = "repairprice";
    static final String KEY_REPAIRNAME = "repairname";
    // Entry data for maintenance type entries
    static final String KEY_MAINTTYPE = "mainttype";
    static final String KEY_MAINTCOST = "maintcost";
    // Public constants for string numbers
    static final int col_id = 0;
    static final int col_entryid = 1;
    static final int col_date = 2;
    static final int col_odometer = 3;
    static final int col_comments = 4;
    static final int col_type = 5;
    static final int col_gasprice = 6;
    static final int col_gasvolume = 7;
    static final int col_gastotal = 8;
    static final int col_repairprice = 9;
    static final int col_repairname = 10;
    static final int col_mainttype = 11;
    static final int col_maintcost = 12;


    entry_database(Context c) {
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
    void addInformation(String subject, String body) {
        SQLiteDatabase db = this.getWritableDatabase();

        // If entry exists, update by deleting it.
        try {
            String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;
            Cursor cursor = db.rawQuery(sqlQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    // Attempt to delete, if no entry is found then error out.
                    if (Objects.equals(cursor.getString(col_type), "INFORMATION")) {
                        if (Objects.equals(cursor.getString(col_repairname), subject)) {
                            db.delete(TABLE_ENTRIES, KEY_ID + "=?", new String[]{
                                    String.valueOf(cursor.getLong(col_id))});
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Not necessarily a bad thing to catch this exception, maybe the row never existed.
            Log.e("CarTrackerSQL", "Couldn't delete row. Maybe it doesn't exist? : " + e.toString());
        }

        ContentValues entryAttribs = new ContentValues();
        entryAttribs.put(KEY_TYPE, "INFORMATION");
        entryAttribs.put(KEY_REPAIRNAME, subject);
        entryAttribs.put(KEY_COMMENTS, body);
        db.insert(TABLE_ENTRIES, null, entryAttribs);
        db.close();
    }

    // Returns an incremented entryid, for use in creating sequential entries
    // Start with the eid, date, odometer, and type.
    // Edd everything else later in entry processing.
    public String addEntry(String eid, String type, String date, String odometer) {
        SQLiteDatabase db = this.getWritableDatabase();

        // If entry exists, update by deleting it.
        try {
            String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;
            Cursor cursor = db.rawQuery(sqlQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    if (Objects.equals(cursor.getString(col_type), type)) {
                        if (Objects.equals(cursor.getString(col_date), date) && Objects.equals(cursor.getString(col_odometer), odometer)) {
                            db.delete(TABLE_ENTRIES, KEY_ID + "=?", new String[]{
                                    String.valueOf(cursor.getLong(col_id))});
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Could not delete information row: " + e.toString());
        }

        ContentValues entryAttribs = new ContentValues();
        entryAttribs.put(KEY_TYPE, type);
        entryAttribs.put(KEY_DATE, date);
        entryAttribs.put(KEY_ENTRYID, eid);
        entryAttribs.put(KEY_ODOMETER, odometer);
        db.insert(TABLE_ENTRIES, null, entryAttribs);
        db.close();
        return String.valueOf(Integer.parseInt(eid)+1);
    }

    String getInformation(String subject) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // KEY_REPAIRNAME was used to hold the subject of the information
                // because it is a string value, and keeps the database simpler.
                if (Objects.equals(cursor.getString(col_type), "INFORMATION")) {
                    if (Objects.equals(cursor.getString(col_repairname), subject)) {
                        return cursor.getString(col_comments);
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    // Methods for getting attributes
    String getKeyIdByEid(String eid) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (Objects.equals(cursor.getString(col_id), eid)) {
                    return cursor.getString(col_entryid);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    String getEidByKeyId(String id) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (Objects.equals(cursor.getString(col_id), id)) {
                    return cursor.getString(col_entryid);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    String getAttribByEid(String eid, int colNum) {
        if (colNum < 0 || colNum > 12) return "invalid.";

        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (Objects.equals(cursor.getString(col_entryid), eid)) {
                    return cursor.getString(colNum);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    String getAttribByKeyId(String id, int colNum) {return getAttribByEid(getEidByKeyId(id), colNum); }

    String setAttribByEid(String eid, String key, String value) { return setAttribByKeyId(getKeyIdByEid(eid), key, value); }

    String setAttribByKeyId(String id, String key, String value) {
        String uQuery = "UPDATE " + TABLE_ENTRIES + " SET ";
        uQuery += key + " = " + value;
        uQuery += " WHERE ID = " + id + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(uQuery);
        db.close();
        return uQuery;
    }

    // If keyOrEntry = True: method returns key id (id)
    // If keyOrEntry = False: method returns entry id (eid)
    String getInformationId(String subject, boolean KeyOrEntry) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (Objects.equals(cursor.getString(col_type), "INFORMATION")) {
                    if (Objects.equals(cursor.getString(col_repairname), subject)) {
                        if (KeyOrEntry) return cursor.getString(col_id);
                        else return cursor.getString(col_entryid);
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }


    // Returns a nicely formatted string of the row of information.
    String exportEntryByKeyId(String id) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    if (Objects.equals(cursor.getString(col_id), id)) {
                        // Build string here
                        String type = cursor.getString(col_type);

                        // Add common data
                        String output = "ID: " + cursor.getString(col_id) + "\t";
                        output += "Date: " + cursor.getString(col_date) + "\t";
                        output += "Odometer: " + cursor.getString(col_odometer) + "\t";
                        output += "Type: " + type + "\t";
                        output += "Comments: " + cursor.getString(col_comments) + "\t";

                        if (Objects.equals(type, "Gas")) {
                            output += "Gas Price: " + cursor.getString(col_gasprice) + "\t";
                            output += "Gas Volume: " + cursor.getString(col_gasvolume) + "\t";
                            output += "Gas Total: " + cursor.getString(col_gastotal);
                        } else if (Objects.equals(type, "Repair")) {
                            output += "Repair Name: " + cursor.getString(col_repairname) + "\t";
                            output += "Repair Price: " + cursor.getString(col_repairprice);
                        } else if (Objects.equals(type, "Maintenance")) {
                            output += "Maintenance Type: " + cursor.getString(col_mainttype) + "\t";
                            output += "Maintenance Price: " + cursor.getString(col_maintcost);
                        } else if (Objects.equals(type, "INFORMATION")) {
                            output += "Information Subject: " + cursor.getString(col_repairname) + "\t";
                            output += "Information Body: " + cursor.getString(col_comments);
                        } else {
                            throw new Exception("Invalid Entry Type");
                        }

                        return output;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Could not export row by Key ID.");
            return "Invalid Export.";
        }
        return null;
    }

    String exportEntryByEid(String eid) {
        return exportEntryByKeyId(getKeyIdByEid(eid));
    }

    // This is used for internals, the exportEntryByKeyId is for exportation.
    ArrayList<String> getEntryByKeyId(String id) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    if (Objects.equals(cursor.getString(col_id), id)) {
                        // Build string here
                        String type = cursor.getString(col_type);

                        // Add common data
                        ArrayList<String> output = new ArrayList<String>();
                        output.add("ID:" + cursor.getString(col_id));
                        output.add("Date:" + cursor.getString(col_date));
                        output.add("Odometer:" + cursor.getString(col_odometer));
                        output.add("Type:" + type);
                        output.add("Comments:" + cursor.getString(col_comments));

                        if (Objects.equals(type, "Gas")) {
                            output.add("Gas Price:" + cursor.getString(col_gasprice));
                            output.add("Gas Volume:" + cursor.getString(col_gasvolume));
                            output.add("Gas Total:" + cursor.getString(col_gastotal));
                        } else if (Objects.equals(type, "Repair")) {
                            output.add("Repair Name:" + cursor.getString(col_repairname));
                            output.add("Repair Price:" + cursor.getString(col_repairprice));
                        } else if (Objects.equals(type, "Maintenance")) {
                            output.add("Maintenance Type:" + cursor.getString(col_mainttype));
                            output.add("Maintenance Price:" + cursor.getString(col_maintcost));
                        } else if (Objects.equals(type, "INFORMATION")) {
                            output.add("Information Subject:" + cursor.getString(col_repairname));
                            output.add("Information Body:" + cursor.getString(col_comments));
                        } else {
                            throw new Exception("Invalid Entry Type");
                        }
                        return output;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Could not export row by Key ID.");
            ArrayList<String> nullList = new ArrayList<String>();
            nullList.add("Null:");
            return nullList;
        }
        return null;
    }

    ArrayList<String> getEntryByEid(String eid) { return getEntryByKeyId(getKeyIdByEid(eid)); }

    /**
     * Deletes an entry in the database based on its Entry ID.
     *
     * @param eid the Entry ID to be deleted
     */
    void deleteEntryByEid(String eid) {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (Objects.equals(cursor.getString(col_id), eid)) {
                    database.delete(TABLE_ENTRIES, KEY_ID+"=?", new String[]
                            {String.valueOf(Integer.parseInt(cursor.getString(col_id)))});
                    database.close();
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * Same as deleteEntryByEid but with a Key ID.
     *
     * @param id the Key ID of the entry
     */
    void deleteEntryByKeyId(String id) {
        deleteEntryByEid(getEidByKeyId(id));
    }

    /**
     * Deletes all entries in the database.
     */
    void deleteAllEntries() {
        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                database.delete(TABLE_ENTRIES, KEY_ID+"=?", new String[]
                        {String.valueOf(Integer.parseInt(cursor.getString(col_id)))});
                database.close();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
 
    /**
     * Returns a count of entries/rows in the database.
     *
     * @param includeInformation If true, includes information rows in count
     * @return the count of entries(rows) in the database.
     */
    int countAllEntries(boolean includeInformation) {
        int c = 0;

        String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (Objects.equals(cursor.getString(col_type), "INFORMATION"))
                    c += includeInformation? 1 : 0;
                else c++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return c;
    }
}

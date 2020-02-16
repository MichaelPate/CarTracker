package com.example.pate5.cartracker;

import java.util.Date;

interface TrackerEntry {
    Date entryDate = null;
    String entryType = null;
    float entryOdometer = 0;
    String entryComments = null;

    Date getEntryDate();

    void setEntryDate(Date date);

    float getEntryOdometer();

    void setEntryOdometer(float odo);

    String getEntryComments();

    void setEntryComments(String comments);

    String getEntryType();

    void setEntryType(String type);
}
/*
  public class TrackerEntry {
  <p>
  private Date entryDate;
  private String entryType;
  private float entryOdometer;
  private String entryComments;
  <p>
  /**
  // Attributes for type Gas
  private float gas_gallons;
  private float gas_pricePer;
  private float gas_total;
  <p>
  // Attributes for type Repair
  private String repair_shopName;
  private float repair_cost;
  <p>
  // Attributes for type Maintenance
  private String maint_type;      // i.e. brakes, transmission, engine, etc.
  private float maint_cost;
  <p>
  <p>
  TrackerEntry(Date date, float odo, String type, String comments) {
  this.entryDate = date;
  this.entryOdometer = odo;
  this.entryType = type;
  this.entryComments = comments;
  }
  <p>
  public Date getEntryDate() {
  return entryDate;
  }
  public float getEntryOdometer() {
  return entryOdometer;
  }
  public String getEntryComments() {
  return entryComments;
  }
  public String getEntryType() {
  return entryType;
  }
  <p>
  public void setEntryComments(String entryComments) {
  this.entryComments = entryComments;
  }
  public void setEntryDate(Date entryDate) {
  this.entryDate = entryDate;
  }
  public void setEntryOdometer(float entryOdometer) {
  this.entryOdometer = entryOdometer;
  }
  public void setEntryType(String entryType) {
  this.entryType = entryType;
  }
  }
  <p>
  <p>
  *********************************************************************************
  <p>
  Skip to content
  Search or jump to…
  <p>
  Pull requests
  Issues
  Marketplace
  Explore

  @MichaelPate 0
 * 00MichaelPate/GasTracker
 * Code Issues 0 Pull requests 0 Actions Projects 0 Wiki Security Insights Settings
 * GasTracker/app/src/main/java/com/example/michael/gastracker/sqliteHelperClass.java
 * @MichaelPate MichaelPate Added final polish, version 1.0 is complete
 * 191311c on Jan 2
 * 189 lines (159 sloc)  6.96 KB
 * <p>
 * package com.example.michael.gastracker;
 * <p>
 * import android.content.ContentValues;
 * import android.content.Context;
 * import android.database.Cursor;
 * import android.database.sqlite.SQLiteDatabase;
 * import android.database.sqlite.SQLiteOpenHelper;
 * import android.util.Log;
 * <p>
 * import java.util.ArrayList;
 * import java.util.List;
 * <p>
 * public class sqliteHelperClass extends SQLiteOpenHelper {
 * private static final int DATABASE_VERSION = 1;
 * private static final String DATABASE_NAME = "gasLog";
 * private static final String TABLE_ENTRIES = "entries";
 * private static final String KEY_ID = "id";
 * private static final String KEY_ENTRYID = "entryId";
 * private static final String KEY_DATE = "date";
 * private static final String KEY_DISTANCE = "distance";
 * private static final String KEY_PRICE = "price";
 * private static final String KEY_VOLUME = "volume";
 * private static final String KEY_COST = "cost";
 * private static final String KEY_MEMO = "memo";
 * <p>
 * // Default constructor
 * public sqliteHelperClass(Context c) {
 * super(c, DATABASE_NAME, null, DATABASE_VERSION);
 * }
 * <p>
 * // For creating tables
 * @Override public void onCreate(SQLiteDatabase database) {
 * String TableCreate_sql = "CREATE TABLE " + TABLE_ENTRIES + "(";
 * TableCreate_sql += KEY_ID + " INTEGER PRIMARY KEY,";
 * TableCreate_sql += KEY_ENTRYID + " TEXT,";
 * TableCreate_sql += KEY_DATE + " TEXT,";
 * TableCreate_sql += KEY_DISTANCE + " TEXT,";
 * TableCreate_sql += KEY_PRICE + " TEXT,";
 * TableCreate_sql += KEY_VOLUME + " TEXT,";
 * TableCreate_sql += KEY_COST + " TEXT,";
 * TableCreate_sql += KEY_MEMO + " TEXT)";
 * database.execSQL(TableCreate_sql);
 * }
 * <p>
 * // For upgrading tables
 * @Override public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
 * // Drop the older table
 * database.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
 * <p>
 * // Recreate the table
 * onCreate(database);
 * }
 * <p>
 * // Adding an entry
 * public void addEntry(logEntryClass newEntry) {
 * SQLiteDatabase db = this.getWritableDatabase();
 * <p>
 * ContentValues entryAttribs = new ContentValues();
 * entryAttribs.put(KEY_ENTRYID, Integer.toString(newEntry.getId()));
 * entryAttribs.put(KEY_DATE, newEntry.getDate());
 * entryAttribs.put(KEY_DISTANCE, newEntry.getDistance());
 * entryAttribs.put(KEY_PRICE, newEntry.getPrice());
 * entryAttribs.put(KEY_VOLUME, newEntry.getVolume());
 * entryAttribs.put(KEY_COST, newEntry.getCost());
 * entryAttribs.put(KEY_MEMO, newEntry.getMemo());
 * <p>
 * db.insert(TABLE_ENTRIES, null, entryAttribs);
 * db.close();
 * }
 * <p>
 * // Returns an entry given the id, not the primary key
 * // NOTE: Assumes that the entry exists
 * public logEntryClass getEntry(int id) {
 * logEntryClass entryResult = new logEntryClass(id);
 * <p>
 * // Iterate through all the entries, and find the one with the matching entryId
 * for (logEntryClass entry : this.getAllEntries()) {
 * if (entry.getId() == id) {
 * // The entry has been found, so return it.
 * entryResult.setId(entry.getId());
 * entryResult.setDate(entry.getDate());
 * entryResult.setDistance(entry.getDistance());
 * entryResult.setPrice(entry.getPrice());
 * entryResult.setVolume(entry.getVolume());
 * entryResult.setCost(entry.getCost());
 * entryResult.setMemo(entry.getMemo());
 * }
 * }
 * return entryResult;
 * }
 * <p>
 * // Returns all entries as a list
 * public List<logEntryClass> getAllEntries() {
 * List<logEntryClass> entryList = new ArrayList<logEntryClass>();
 * <p>
 * //Submit the query
 * String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;
 * <p>
 * SQLiteDatabase database = this.getWritableDatabase();
 * Cursor cursor = database.rawQuery(sqlQuery, null);
 * <p>
 * // Iterate through the list ad add the entries to the list
 * if (cursor.moveToFirst()) {
 * do {
 * logEntryClass entry =
 * new logEntryClass(Integer.parseInt(cursor.getString(1)));
 * entry.setDate(cursor.getString(2));
 * entry.setDistance(cursor.getString(3));
 * entry.setPrice(cursor.getString(4));
 * entry.setVolume(cursor.getString(5));
 * entry.setCost(cursor.getString(6));
 * entry.setMemo(cursor.getString(7));
 * entryList.add(entry);
 * } while (cursor.moveToNext());
 * }
 * <p>
 * return entryList;
 * }
 * <p>
 * // Updates a single entry
 * public int updateEntry(logEntryClass entry, boolean decIdWhenDone) {
 * // Get the primary key value of entry, so that we have the whereClause of db.update().
 * int primaryKeyId = 0;
 * String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;
 * <p>
 * SQLiteDatabase db = this.getWritableDatabase();
 * Cursor cursor = db.rawQuery(sqlQuery, null);
 * <p>
 * // Iterate through the list ad add the entries to the list
 * if (cursor.moveToFirst()) {
 * do {
 * if (cursor.getString(1).equals(Integer.toString(entry.getId()))) {
 * primaryKeyId = Integer.parseInt(cursor.getString(0));
 * break;
 * }
 * } while (cursor.moveToNext());
 * }
 * <p>
 * ContentValues entryAttribs = new ContentValues();
 * <p>
 * if(decIdWhenDone) {
 * entryAttribs.put(KEY_ENTRYID, Integer.toString(entry.getId() - 1));
 * } else {
 * entryAttribs.put(KEY_ENTRYID, Integer.toString(entry.getId()));
 * }
 * <p>
 * entryAttribs.put(KEY_DATE, entry.getDate());
 * entryAttribs.put(KEY_DISTANCE, entry.getDistance());
 * entryAttribs.put(KEY_PRICE, entry.getPrice());
 * entryAttribs.put(KEY_VOLUME, entry.getVolume());
 * entryAttribs.put(KEY_COST, entry.getCost());
 * entryAttribs.put(KEY_MEMO, entry.getMemo());
 * <p>
 * // Update the row at return
 * return db.update(TABLE_ENTRIES, entryAttribs, KEY_ID+"=?",
 * new String[] {String.valueOf(primaryKeyId)});
 * <p>
 * }
 * <p>
 * // Delete a single entry
 * public void deleteEntry(logEntryClass entry) {
 * int primaryKeyId = 0;
 * String sqlQuery = "SELECT * FROM " + TABLE_ENTRIES;
 * <p>
 * SQLiteDatabase db = this.getWritableDatabase();
 * Cursor cursor = db.rawQuery(sqlQuery, null);
 * <p>
 * // Iterate through the list ad add the entries to the list
 * if (cursor.moveToFirst()) {
 * do {
 * if (cursor.getString(1).equals(Integer.toString(entry.getId()))) {
 * primaryKeyId = Integer.parseInt(cursor.getString(0));
 * break;
 * }
 * } while (cursor.moveToNext());
 * }
 * <p>
 * db.delete(TABLE_ENTRIES, KEY_ID+"=?",
 * new String[] {String.valueOf(primaryKeyId)});
 * db.close();
 * }
 * <p>
 * // Get a count of all entries
 * public int getEntryCount() {
 * return this.getAllEntries().size();
 * }
 * }
 */
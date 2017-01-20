package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Database handler class used to handle all operations of the database.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 116;

    // Database Name
    private static final String DATABASE_NAME = "ListStats";

    // Contacts table name
    private static final String TABLE_RUNSTATS = "RunStats";

    // RunStats Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DIST = "distance";
    private static final String KEY_DUR = "duration";
    private static final String KEY_SPEED = "average_speed";
    private static final String KEY_CA_BURN = "calories_burned";

    // Constructor
    public  DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates table on creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RUNSTATS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DIST + " DOUBLE,"
                + KEY_DUR + " STRING," + KEY_SPEED + " DOUBLE,"
                + KEY_CA_BURN + " DOUBLE" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUNSTATS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new stats
    public void addRunStats(RunStats runStats) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DIST, runStats.getDistance()); // distance
        values.put(KEY_DUR, runStats.getDuration()); // duration
        values.put(KEY_SPEED, runStats.getAverageSpeed()); // average speed
        values.put(KEY_CA_BURN, runStats.getCalorieBurn()); // calories burned

        // Inserting Row
        db.insert(TABLE_RUNSTATS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one set of stats
    public RunStats getRunStats(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RUNSTATS, new String[]{KEY_ID,
                        KEY_DIST, KEY_DUR, KEY_SPEED, KEY_CA_BURN}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RunStats contact = new RunStats(Integer.parseInt(cursor.getString(0)),
                (Double.parseDouble(cursor.getString(1))), cursor.getString(2), (Double.parseDouble(cursor.getString(3))), (Double.parseDouble(cursor.getString(4))));

        // return stats
        return contact;
    }

    // Getting All runStats
    public List<RunStats> getAllRunStats() {
        List<RunStats> statsList = new ArrayList<RunStats>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RUNSTATS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RunStats runStats = new RunStats();
                runStats.setId(Integer.parseInt(cursor.getString(0)));
                runStats.setDistance(Double.parseDouble(cursor.getString(1)));
                runStats.setDuration(cursor.getString(2));
                runStats.setAverageSpeed(Double.parseDouble(cursor.getString(3)));
                runStats.setCalorieBurn(Double.parseDouble(cursor.getString(4)));
                // Adding to list
                statsList.add(runStats);
            } while (cursor.moveToNext());
        }

        // return contact list
        return statsList;
    }

    // Getting Count
    public int getRunStatsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RUNSTATS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Updating a runStat
    public int updateRunStats(RunStats runStats) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DIST, runStats.getDistance());
        values.put(KEY_DUR, runStats.getDuration());
        values.put(KEY_SPEED, runStats.getAverageSpeed());
        values.put(KEY_CA_BURN, runStats.getCalorieBurn());

        // updating row
        return db.update(TABLE_RUNSTATS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(runStats.getId())});
    }

    // Deleting a stat (not used)
    public void deleteStats(RunStats runStats) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RUNSTATS, KEY_ID + " = ?",
                new String[] { String.valueOf(runStats.getId()) });
        db.close();
    }
}


package edu.dartmouth.cs.jgualtieri.amina.Data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Justin Gualtieri on 2/26/17.
 *
 * Class to serve as a database helper - make it easy to work with the database
 * from other Activities in the application
 */

public class PinDBHelper {
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] columns = {Constants.COLUMN_ENTRY_ID,
                                Constants.COLUMN_USER_ID,
                                Constants.COLUMN_LOCATION_X,
                                Constants.COLUMN_LOCATION_Y,
                                Constants.COLUMN_DATE_TIME};

    // Constructor
    public PinDBHelper(Context context) {
        sqLiteHelper = new SQLiteHelper(context);
    }

    // Open the database
    public void open() {
        try {
            sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close the database
    public void close() {
        sqLiteHelper.close();
    }

    // Add an entry to the database - return the id of the entry that has been added
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long addEntryToDatabase(Pin pin) {

        // Format the date
        SimpleDateFormat format = new SimpleDateFormat("H:mm:ss MMM d', 'yyyy");
        String formattedDate = format.format(pin.getDateTime().getTime());

        // Populate a ContentVales object with the info stored in the Pin
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_USER_ID, pin.getUserId());
        contentValues.put(Constants.COLUMN_LOCATION_X, pin.getLocationX());
        contentValues.put(Constants.COLUMN_LOCATION_Y, pin.getLocationY());
        contentValues.put(Constants.COLUMN_DATE_TIME, formattedDate);

        return sqLiteDatabase.insert(Constants.TABLE_PROMPT, null, contentValues);
    }

    // Get all entries from the database
    public ArrayList<Pin> getAllEntries() {
        ArrayList<Pin> responses = new ArrayList<>();

        // Get all of the entries
        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_PROMPT, columns, null, null, null,
                null, null);
        cursor.moveToFirst();

        // Iterate through the entries and add them to the list
        while (!cursor.isAfterLast()) {

            cursor.moveToNext();
        }

        //TODO
        return responses;
    }

    // Create a prompt response object from a cursor
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Pin entryToPromptResponse(Cursor cursor) {
        Pin pin = new Pin();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("H:mm:ss MMM d', 'yyyy", Locale.ENGLISH);

        try {
            calendar.setTime(format.parse(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATE_TIME))));
        } catch (ParseException pe){
            pe.printStackTrace();
        }

        // pin.setEntryId(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ENTRY_ID)));
        pin.setUserId(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_USER_ID)));
        pin.setLocationX(cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_LOCATION_X)));
        pin.setLocationY(cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_LOCATION_Y)));

        // TODO

        return pin;
    }
}

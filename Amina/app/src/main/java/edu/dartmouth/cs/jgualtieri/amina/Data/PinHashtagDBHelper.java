package edu.dartmouth.cs.jgualtieri.amina.Data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Justin Gualtieri on 2/26/17.
 *
 * Class to serve as a database helper - make it easy to work with the database
 * from other Activities in the application
 */

public class PinHashtagDBHelper {
    private static final String TAG = "DB Helper";
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] hashtagsTableColumns = {Constants.HASHTAGS_COLUMN_ENTRY_ID,
                                             Constants.HASHTAGS_COLUMN_VALUE,
                                             Constants.HASHTAGS_COLUMN_ASSOCIATED_PINS};
    private String[] pinsTableColumns = {Constants.PINS_COLUMN_ENTRY_ID,
                                         Constants.PINS_COLUMN_USER_ID,
                                         Constants.PINS_COLUMN_LOCATION_X,
                                         Constants.PINS_COLUMN_LOCATION_Y,
                                         Constants.PINS_COLUMN_DATE_TIME};

    // Constructor
    public PinHashtagDBHelper(Context context) {
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

    // Add a pin entry to the database - return the id of the entry that has been added
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long addPinToDatabase(Pin pin) {

        // Format the date
        SimpleDateFormat format = new SimpleDateFormat("H:mm:ss MMM d', 'yyyy");
        String formattedDate = format.format(pin.getDateTime().getTime());

        // Populate a ContentVales object with the info stored in the Pin
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.PINS_COLUMN_USER_ID, pin.getUserId());
        contentValues.put(Constants.PINS_COLUMN_LOCATION_X, pin.getLocationX());
        contentValues.put(Constants.PINS_COLUMN_LOCATION_Y, pin.getLocationY());
        contentValues.put(Constants.PINS_COLUMN_DATE_TIME, formattedDate);
        contentValues.put(Constants.PINS_COLUMN_SAFETY, pin.getSafetyStatus());
        contentValues.put(Constants.PINS_COLUMN_COMMENT, pin.getComment());

        return sqLiteDatabase.insert(Constants.TABLE_PINS, null, contentValues);
    }

    // Add a Hashtag entry to the database - return the id of the entry that has been added
    // Associated pin is the entryId of the Pin object that the Hashtag corresponds to -
    // used to maintain the relationship between the Hashtag and the Pin
    public long addHashtagToDatabase(Hashtag hashtag, long associatedPin) {

        // First, check if the Hashtag already exists
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_HASHTAGS +
                " WHERE TRIM(" + Constants.HASHTAGS_COLUMN_VALUE + ") = '" +
                hashtag.getValue().trim() + "'", null);
        cursor.moveToFirst();

        // If the entry already exists, update it to include the new Pin association
        if (cursor.getCount() > 0) {

            Log.d(TAG, "Updating entry");

            // First, get the existing entry Id and the existing Pin associations
            long existingHashtagEntryId = cursor.getLong(cursor
                    .getColumnIndex(Constants.HASHTAGS_COLUMN_ENTRY_ID));
            String existingAssociatedPinsText = cursor.getString(cursor
                    .getColumnIndex(Constants.HASHTAGS_COLUMN_ASSOCIATED_PINS));
            String[] existingAssociatedPins = convertStringToArray(existingAssociatedPinsText);

            // Create a new array to include the new association, convert to string to store in DB
            String[] newAssociatedPins = new String[existingAssociatedPins.length + 1];
            for (int i = 0; i < existingAssociatedPins.length; i++) {
                newAssociatedPins[i] = existingAssociatedPins[i];
            }
            newAssociatedPins[existingAssociatedPins.length] = String.valueOf(associatedPin);
            String newAssociatedPinsText = convertArrayToString(newAssociatedPins);

            // Update the existing entry
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.HASHTAGS_COLUMN_ASSOCIATED_PINS, newAssociatedPinsText);
            sqLiteDatabase.update(Constants.TABLE_HASHTAGS, contentValues,
                    Constants.HASHTAGS_COLUMN_ENTRY_ID + " = " + existingHashtagEntryId, null);

            cursor.close();
            return existingHashtagEntryId;

        // Else, create a new Hashtag entry
        } else {

            Log.d(TAG, "Creating entry");

            String associatedPinsText = convertArrayToString(hashtag.getAssociatedPins());

            // Create a new content values for the Hashtags
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.HASHTAGS_COLUMN_VALUE, hashtag.getValue());
            contentValues.put(Constants.HASHTAGS_COLUMN_ASSOCIATED_PINS, associatedPinsText);

            cursor.close();
            return sqLiteDatabase.insert(Constants.TABLE_HASHTAGS, null, contentValues);
        }
    }

//    // Get all entries from the database
//    public ArrayList<Pin> getAllEntries() {
//        ArrayList<Pin> responses = new ArrayList<>();
//
//        // Get all of the entries
//        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_PINS, pinsTableColumns, null, null,
//                null, null, null);
//        cursor.moveToFirst();
//
//        // Iterate through the entries and add them to the list
//        while (!cursor.isAfterLast()) {
//
//            cursor.moveToNext();
//        }
//
//        //TODO
//        return responses;
//    }
//
//    // Create a prompt response object from a cursor
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private Pin entryToPin(Cursor cursor) {
//        Pin pin = new Pin();
//
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat format = new SimpleDateFormat("H:mm:ss MMM d', 'yyyy", Locale.ENGLISH);
//
//        try {
//            calendar.setTime(format.parse(cursor.getString(cursor
//                    .getColumnIndex(Constants.PINS_COLUMN_DATE_TIME))));
//        } catch (ParseException pe){
//            pe.printStackTrace();
//        }
//
//        pin.setEntryId(cursor.getLong(cursor.getColumnIndex(Constants.PINS_COLUMN_ENTRY_ID)));
//        pin.setUserId(cursor.getString(cursor.getColumnIndex(Constants.PINS_COLUMN_USER_ID)));
//        pin.setLocationX(cursor.getFloat(cursor.getColumnIndex(Constants.PINS_COLUMN_LOCATION_X)));
//        pin.setLocationY(cursor.getFloat(cursor.getColumnIndex(Constants.PINS_COLUMN_LOCATION_Y)));
//
//        return pin;
//    }

    private static String strSeparator = "__,__";

    private static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    private static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}

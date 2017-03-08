package edu.dartmouth.cs.jgualtieri.amina.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    private String[] hashtagsCloudTableColumns = {Constants.HASHTAGS_CLOUD_COLUMN_ENTRY_ID,
            Constants.HASHTAGS_CLOUD_COLUMN_VALUE,
            Constants.HASHTAGS_CLOUD_COLUMN_ASSOCIATED_PINS};

    private String[] pinsTableColumns = {Constants.PINS_COLUMN_ENTRY_ID,
            Constants.PINS_COLUMN_USER_ID,
            Constants.PINS_COLUMN_LOCATION_X,
            Constants.PINS_COLUMN_LOCATION_Y,
            Constants.PINS_COLUMN_DATE_TIME,
            Constants.PINS_COLUMN_SAFETY,
            Constants.PINS_COLUMN_COMMENT,
            Constants.PINS_COLUMN_HASHTAGS};

    private String[] pinsCloudTableColumns = {Constants.PINS_COLUMN_ENTRY_ID,
            Constants.PINS_COLUMN_USER_ID,
            Constants.PINS_COLUMN_LOCATION_X,
            Constants.PINS_COLUMN_LOCATION_Y,
            Constants.PINS_COLUMN_DATE_TIME,
            Constants.PINS_COLUMN_SAFETY,
            Constants.PINS_COLUMN_COMMENT,
            Constants.PINS_COLUMN_HASHTAGS};

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
        Gson gsonLocationArray = new Gson();
        contentValues.put(Constants.PINS_COLUMN_HASHTAGS, gsonLocationArray.toJson(pin.getHashtags()).getBytes());

        return sqLiteDatabase.insert(Constants.TABLE_PINS, null, contentValues);
    }

    // Add a pin entry to the database - return the id of the entry that has been added
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addCloudPinToDatabase(Pin pin) {

        // Format the date
        SimpleDateFormat format = new SimpleDateFormat("H:mm:ss MMM d', 'yyyy");
        String formattedDate = format.format(pin.getDateTime().getTime());

        // Populate a ContentVales object with the info stored in the Pin
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.PINS_CLOUD_COLUMN_ENTRY_ID, pin.getHashId());
        contentValues.put(Constants.PINS_CLOUD_COLUMN_USER_ID, pin.getUserId());
        contentValues.put(Constants.PINS_CLOUD_COLUMN_LOCATION_X, pin.getLocationX());
        contentValues.put(Constants.PINS_CLOUD_COLUMN_LOCATION_Y, pin.getLocationY());
        contentValues.put(Constants.PINS_CLOUD_COLUMN_DATE_TIME, formattedDate);
        contentValues.put(Constants.PINS_CLOUD_COLUMN_SAFETY, pin.getSafetyStatus());
        contentValues.put(Constants.PINS_CLOUD_COLUMN_COMMENT, pin.getComment());
        Gson gsonLocationArray = new Gson();
        contentValues.put(Constants.PINS_CLOUD_COLUMN_HASHTAGS, gsonLocationArray.toJson(pin.getHashtags()).getBytes());

        sqLiteDatabase.insert(Constants.TABLE_CLOUD_PINS, null, contentValues);
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

    // Add a Hashtag entry to the database - return the id of the entry that has been added
    // Associated pin is the entryId of the Pin object that the Hashtag corresponds to -
    // used to maintain the relationship between the Hashtag and the Pin
    public void addCloudHashtagToDatabase(Hashtag hashtag) {

        // First, check if the Hashtag already exists
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_CLOUD_HASHTAGS +
                " WHERE TRIM(" + Constants.HASHTAGS_CLOUD_COLUMN_VALUE + ") = '" +
                hashtag.getValue().trim() + "'", null);
        cursor.moveToFirst();

        // If the entry already exists, update it to include the new Pin association
        if (cursor.getCount() > 0) {

            Log.d(TAG, "Updating entry");

            // Update the existing entry
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.HASHTAGS_CLOUD_COLUMN_ASSOCIATED_PINS,
                    convertArrayToString(hashtag.getAssociatedPins()));
            sqLiteDatabase.update(Constants.TABLE_CLOUD_HASHTAGS, contentValues,
                    Constants.HASHTAGS_CLOUD_COLUMN_VALUE + " = " + hashtag.getValue(), null);

            cursor.close();

            // Else, create a new Hashtag entry
        } else {

            Log.d(TAG, "Creating entry");

            String associatedPinsText = convertArrayToString(hashtag.getAssociatedPins());

            // Create a new content values for the Hashtags
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.HASHTAGS_CLOUD_COLUMN_VALUE, hashtag.getValue());
            contentValues.put(Constants.HASHTAGS_CLOUD_COLUMN_ASSOCIATED_PINS, associatedPinsText);
            cursor.close();

            sqLiteDatabase.insert(Constants.TABLE_CLOUD_HASHTAGS, null, contentValues);
        }
    }

    // get all entries in the table
    public List<Pin> getAllEntries() {
        List<Pin> pins = new ArrayList<Pin>();

        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_PINS,
                pinsTableColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Pin pin = cursorToPin(cursor);
            pins.add(pin);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        Cursor cursorCloud = sqLiteDatabase.query(Constants.TABLE_CLOUD_PINS,
                pinsCloudTableColumns, null, null, null, null, null);

        cursorCloud.moveToFirst();
        while (!cursorCloud.isAfterLast()) {
            Pin pin = cursorToPin(cursorCloud);
            pins.add(pin);
            cursorCloud.moveToNext();
        }

        // Make sure to close the cursor
        cursorCloud.close();

        return pins;
    }

    // Query the both the local and the cloud databases and return and display all
    // pins containing a given hashtag
    public List<Pin> queryDatabaseForPins(String hashtagValue) {
        List<Pin> pins = new ArrayList<>();

        // First, query the local database to get the hashtag value
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_HASHTAGS +
                " WHERE TRIM(" + Constants.HASHTAGS_COLUMN_VALUE + ") = '" +
                hashtagValue.trim() + "'", null);

        // Retrieve all pins associated with the specific hashtag from the local database
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Hashtag hashtag = cursorToHashtag(cursor);
            String[] associatedPins = hashtag.getAssociatedPins();
            for (String pin : associatedPins) {
                Log.d("Value of entry id", pin);
                Pin localPin = queryLocalDatabaseForPin(Long.valueOf(pin));
                Log.d("Value of local comment", localPin.getComment());
                pins.add(localPin);
            }
        }

        // Next, query the cloud database to get the hashtag value
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_CLOUD_HASHTAGS +
                " WHERE TRIM(" + Constants.HASHTAGS_CLOUD_COLUMN_VALUE + ") = '" +
                hashtagValue.trim() + "'", null);

        // Get all of the hashtags from the cloud database
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Hashtag hashtag = cursorToHashtag(cursor);
            String[] associatedPins = hashtag.getAssociatedPins();
            for (String pin : associatedPins) {
                Pin cloudPin = queryCloudDatabaseForPin(pin);
                pins.add(cloudPin);
            }
        }
        return pins;
    }

    // Queries the local database for a pin given the entry Id
    public Pin queryLocalDatabaseForPin(long entryId) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_PINS +
                " WHERE TRIM(" + Constants.PINS_COLUMN_ENTRY_ID + ") = '" +
                String.valueOf(entryId) + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Log.d("Query local database", "pin found");
        } else {
            Log.d("Query local database", "pin not found");
        }
        return cursorToPin(cursor);
    }

    // Queries the local database for a pin given the entry Id
    public Pin queryCloudDatabaseForPin(String hashId) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_CLOUD_PINS +
                " WHERE TRIM(" + Constants.PINS_CLOUD_COLUMN_ENTRY_ID + ") = '" +
                hashId.trim() + "'", null);
        return cursorToPin(cursor);
    }

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

    // create FitnessEntry object, assign the values from db, and return it
    private Pin cursorToPin(Cursor cursor) {

        // create FitnessEntry object
        Pin pin = new Pin();

        //set FitnessEntry fields
        // pin.setEntryId(cursor.getLong(cursor.getColumnIndex(Constants.PINS_COLUMN_ENTRY_ID)));
        pin.setUserId(cursor.getString(cursor.getColumnIndex(Constants.PINS_COLUMN_USER_ID)));
        // TODO figure out how to retrieve date time
        //String datetime = cursor.getString(cursor.getColumnIndex(Constants.PINS_COLUMN_DATE_TIME));
        pin.setLocationX(cursor.getDouble(cursor.getColumnIndex(Constants.PINS_COLUMN_LOCATION_X)));
        pin.setLocationY(cursor.getDouble(cursor.getColumnIndex(Constants.PINS_COLUMN_LOCATION_Y)));
        pin.setSafetyStatus(cursor.getInt(cursor.getColumnIndex(Constants.PINS_COLUMN_SAFETY)));
        pin.setComment(cursor.getString(cursor.getColumnIndex(Constants.PINS_COLUMN_COMMENT)));

        Gson gsonHashtagArray = new Gson();
        String jsonHashtagArray = new String(cursor.getBlob(cursor.getColumnIndex(Constants.PINS_COLUMN_HASHTAGS)));
        Type typeHash = new TypeToken<ArrayList<String>>() {}.getType();
        pin.setHashtags(((ArrayList<String>) gsonHashtagArray.fromJson(jsonHashtagArray, typeHash)));

        return pin;
    }

    // Convert a cursor to a Hashtag object
    private Hashtag cursorToHashtag(Cursor cursor) {

        // create Hashtag object
        Hashtag hashtag = new Hashtag();

        // set Hashtag fields
        hashtag.setEntryId(cursor.getLong(cursor.getColumnIndex(Constants.HASHTAGS_COLUMN_ENTRY_ID)));
        hashtag.setValue(cursor.getString(cursor.getColumnIndex(Constants.HASHTAGS_COLUMN_VALUE)));
        String associatedPinsText = cursor.getString(cursor
                .getColumnIndex(Constants.HASHTAGS_COLUMN_ASSOCIATED_PINS));
        String[] associatedPins = convertStringToArray(associatedPinsText);
        hashtag.setAssociatedPins(associatedPins);

        return hashtag;
    }
}

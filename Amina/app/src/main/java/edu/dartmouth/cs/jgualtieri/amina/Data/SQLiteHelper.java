package edu.dartmouth.cs.jgualtieri.amina.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JustinGualtieri on 2/26/17.
 *
 * Class to help out with database operations.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String HASHTAGS_TABLE_CREATE = "CREATE TABLE "
            + Constants.TABLE_HASHTAGS
            + " ( "
            + Constants.HASHTAGS_COLUMN_ENTRY_ID + " integer primary key autoincrement, "
            + Constants.HASHTAGS_COLUMN_VALUE + " text not null, "
            + Constants.HASHTAGS_COLUMN_ASSOCIATED_PINS + " text not null );";

    private static final String PINS_TABLE_CREATE = "CREATE TABLE "
            + Constants.TABLE_PINS
            + "( "
            + Constants.PINS_COLUMN_ENTRY_ID + " integer primary key autoincrement, "
            + Constants.PINS_COLUMN_USER_ID + " text, "
            + Constants.PINS_COLUMN_LOCATION_X + " float, "
            + Constants.PINS_COLUMN_LOCATION_Y + " float, "
            + Constants.PINS_COLUMN_COMMENT + " text, "
            + Constants.PINS_COLUMN_SAFETY + " integer, "
            + Constants.PINS_COLUMN_DATE_TIME + " datetime not null, "
            + Constants.PINS_COLUMN_HASHTAGS + " blob );";

    // Constructor
    public SQLiteHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Execute the create command on create
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(HASHTAGS_TABLE_CREATE);
        database.execSQL(PINS_TABLE_CREATE);
    }

    // Update the database
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_PINS);
        onCreate(database);
    }
}

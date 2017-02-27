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

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + Constants.TABLE_PROMPT
            + "( "
            + Constants.COLUMN_ENTRY_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_USER_ID + " text, "
            + Constants.COLUMN_LOCATION_X + " float, "
            + Constants.COLUMN_LOCATION_Y + " float, "
            + Constants.COLUMN_DATE_TIME + " datetime not null );";

    // Constructor
    public SQLiteHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Execute the create command on create
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Update the database
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_PROMPT);
        onCreate(database);
    }
}

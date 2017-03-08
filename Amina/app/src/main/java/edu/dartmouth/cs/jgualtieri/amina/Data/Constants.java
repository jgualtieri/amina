package edu.dartmouth.cs.jgualtieri.amina.Data;

/**
 * Created by JustinGualtieri on 2/26/17.
 */

public class Constants {

    // Used for database access
    static final String TABLE_HASHTAGS = "hashtags";
    static final String TABLE_PINS = "pins";
    static final String TABLE_CLOUD_PINS = "cloudPins";

    static final String HASHTAGS_COLUMN_ENTRY_ID = "entryId";
    static final String HASHTAGS_COLUMN_VALUE = "value";
    static final String HASHTAGS_COLUMN_ASSOCIATED_PINS = "associatedPins";

    static final String PINS_COLUMN_ENTRY_ID = "entryId";
    static final String PINS_COLUMN_USER_ID = "userId";
    static final String PINS_COLUMN_LOCATION_X = "locationX";
    static final String PINS_COLUMN_LOCATION_Y = "locationY";
    static final String PINS_COLUMN_DATE_TIME = "dateTime";
    static final String PINS_COLUMN_COMMENT = "comment";
    static final String PINS_COLUMN_SAFETY = "safety";
    static final String PINS_COLUMN_HASHTAGS = "pinHashtags";

    static final String PINS_CLOUD_COLUMN_ENTRY_ID = "entryId";
    static final String PINS_CLOUD_COLUMN_USER_ID = "userId";
    static final String PINS_CLOUD_COLUMN_LOCATION_X = "locationX";
    static final String PINS_CLOUD_COLUMN_LOCATION_Y = "locationY";
    static final String PINS_CLOUD_COLUMN_DATE_TIME = "dateTime";
    static final String PINS_CLOUD_COLUMN_COMMENT = "comment";
    static final String PINS_CLOUD_COLUMN_SAFETY = "safety";
    static final String PINS_CLOUD_COLUMN_HASHTAGS = "pinHashtags";

    static final String DATABASE_NAME = "amina_database";
    static final int DATABASE_VERSION = 1;

    public static final String SERVER_ADDRESS = "http://127.0.0.1:8080";
}

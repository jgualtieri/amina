package com.example.JustinGualtieri.myapplication.backend;

/**
 * Created by JustinGualtieri on 3/6/17.
 */

public class Pin {
    public static final String PIN_PARENT_ENTITY_NAME = "PinParent";
    public static final String PIN_PARENT_KEY_NAME = "PinParent";

    public static final String PIN_ENTITY_NAME = "Pin";

    public static final String FIELD_NAME_ENTRY_ID = "entryId";
    public static final String FIELD_NAME_USER_ID = "userId";
    public static final String FIELD_NAME_LOCATION_X = "locationX";
    public static final String FIELD_NAME_LOCATION_Y = "locationY";
    public static final String FIELD_NAME_DATETIME = "dateTime";
    public static final String FIELD_NAME_SAFETY_STATUS = "safetyStatus";
    public static final String FIELD_NAME_COMMENT = "comment";
    public static final String FIELD_NAME_HASHTAG = "pinHashtag";

    public static final String KEY_NAME = FIELD_NAME_ENTRY_ID;

    public String entryId;
    public String userId;
    public String locationX;
    public String locationY;
    public String dateTime;
    public String safetyStatus;
    public String comment;
    public String hashtags;

    public Pin(String entryId,
               String userId,
               String locationX,
               String locationY,
               String dateTime,
               String safetyStatus,
               String comment,
               String hashtags) {
        this.entryId = entryId;
        this.userId = userId;
        this.locationX = locationX;
        this.locationY = locationY;
        this.dateTime = dateTime;
        this.safetyStatus = safetyStatus;
        this.comment = comment;
        this.hashtags = hashtags;
    }

    public String toString(){
        return "Pin/" + entryId + "/" + userId + "/" + locationX + "/" + locationY + "/"
                + safetyStatus + "/" + comment + "/" + hashtags;
    }
}

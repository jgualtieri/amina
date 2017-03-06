package com.example.JustinGualtieri.myapplication.backend;

/**
 * Created by JustinGualtieri on 3/6/17.
 */

public class Hashtag {
    public static final String HASHTAG_PARENT_ENTITY_NAME = "HashtagParent";
    public static final String HASHTAG_PARENT_KEY_NAME = "HashtagParent";

    public static final String HASHTAG_ENTITY_NAME = "Hashtag";

    public static final String FIELD_NAME_VALUE = "value";
    public static final String FIELD_NAME_COUNT = "count";
    public static final String FIELD_NAME_ASSOCIATED_PINS = "associatedPins";

    public static final String KEY_NAME = FIELD_NAME_VALUE;

    public String value;
    public String count;
    public String associatedPins;

    public Hashtag(String value,
               String count,
               String associatedPins) {
        this.value = value;
        this.count = count;
        this.associatedPins = associatedPins;
    }
}

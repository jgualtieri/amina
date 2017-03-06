package com.example.JustinGualtieri.myapplication.backend;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.ExtendableEntityUtil;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.repackaged.com.google.common.collect.HashBasedTable;
import com.google.appengine.repackaged.com.google.common.collect.Interner;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by JustinGualtieri on 3/6/17.
 */

public class DatastoreHelper {

    private static final Logger logger = Logger.getLogger(DatastoreHelper.class.getName());
    private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // Create and return a Pin key
    private static Key getPinKey() {
        return KeyFactory.createKey(Pin.PIN_PARENT_ENTITY_NAME, Pin.PIN_PARENT_KEY_NAME);
    }

    // Create and return a Hashtag key
    private static Key getHashtagKey() {
        return KeyFactory.createKey(Hashtag.HASHTAG_PARENT_ENTITY_NAME,
                Hashtag.HASHTAG_PARENT_KEY_NAME);
    }

    // Add a pin to the datastore
    public static boolean addPin(Pin pin) {
        if (getPinByEntryId(pin.entryId, null) != null) {
            logger.log(Level.INFO, "Pin already exists");
            return false;
        }

        Key parentKey = getPinKey();

        // Create the entity
        Entity entity = new Entity(Pin.PIN_ENTITY_NAME, pin.entryId, parentKey);
        entity.setProperty(Pin.FIELD_NAME_ENTRY_ID, pin.entryId);
        entity.setProperty(Pin.FIELD_NAME_USER_ID, pin.userId);
        entity.setProperty(Pin.FIELD_NAME_LOCATION_X, pin.locationX);
        entity.setProperty(Pin.FIELD_NAME_LOCATION_Y, pin.locationY);
        entity.setProperty(Pin.FIELD_NAME_DATETIME, pin.dateTime);
        entity.setProperty(Pin.FIELD_NAME_SAFETY_STATUS, pin.safetyStatus);
        entity.setProperty(Pin.FIELD_NAME_COMMENT, pin.comment);

        datastore.put(entity);
        return true;
    }

    // Add a hashtag to the datastore or update it if it already exists
    public static boolean addUpdateHashtag(Hashtag hashtag) {

        // Check to see if the hashtag already exists in the datastore
        Hashtag existingHashtag = getHashtagByValue(hashtag.value, null);
        Key parentKey = getHashtagKey();

        // If the hashtag does not exist, add it
        if (existingHashtag == null) {

            // Create the entity
            Entity entity = new Entity(Hashtag.HASHTAG_ENTITY_NAME, hashtag.value, parentKey);
            entity.setProperty(Hashtag.FIELD_NAME_VALUE, hashtag.value);
            entity.setProperty(Hashtag.FIELD_NAME_COUNT, "1");
            entity.setProperty(Hashtag.FIELD_NAME_ASSOCIATED_PINS, hashtag.associatedPins);

            datastore.put(entity);
            return true;

            // Otherwise, update the entry
        } else {

            // Compute the new count
            int originalCount = Integer.valueOf(existingHashtag.count);
            originalCount++;
            String updatedCount = String.valueOf(originalCount);

            // Add the new association to the list of associations
            String originalAssociations = existingHashtag.associatedPins;
            String updatedAssociations = originalAssociations + "," + hashtag.associatedPins;

            // Create the updated entity
            Entity entity = new Entity(Hashtag.HASHTAG_ENTITY_NAME, hashtag.value, parentKey);
            entity.setProperty(Hashtag.FIELD_NAME_VALUE, hashtag.value);
            entity.setProperty(Hashtag.FIELD_NAME_COUNT, updatedCount);
            entity.setProperty(Hashtag.FIELD_NAME_ASSOCIATED_PINS, updatedAssociations);

            datastore.put(entity);
            return true;
        }
    }

    public static Pin getPinByEntryId(String entryId, Transaction transaction) {
        Entity result = null;
        try {
            result = datastore.get(KeyFactory.createKey(getPinKey(), Pin.PIN_ENTITY_NAME, entryId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getPinFromEntity(result);
    }

    public static Hashtag getHashtagByValue(String value, Transaction transaction) {
        Entity result = null;
        try {
            result = datastore.get(KeyFactory.createKey(getHashtagKey(),
                    Hashtag.HASHTAG_ENTITY_NAME, value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getHashtagFromEntity(result);
    }

    public static Pin getPinFromEntity(Entity entity) {
        if (entity == null) {
            return null;
        }
        return new Pin(
                (String) entity.getProperty(Pin.FIELD_NAME_ENTRY_ID),
                (String) entity.getProperty(Pin.FIELD_NAME_USER_ID),
                (String) entity.getProperty(Pin.FIELD_NAME_LOCATION_X),
                (String) entity.getProperty(Pin.FIELD_NAME_LOCATION_Y),
                (String) entity.getProperty(Pin.FIELD_NAME_DATETIME),
                (String) entity.getProperty(Pin.FIELD_NAME_SAFETY_STATUS),
                (String) entity.getProperty(Pin.FIELD_NAME_COMMENT)
        );
    }

    public static Hashtag getHashtagFromEntity(Entity entity) {
        if (entity == null) {
            return null;
        }
        return new Hashtag(
                (String) entity.getProperty(Hashtag.FIELD_NAME_VALUE),
                (String) entity.getProperty(Hashtag.FIELD_NAME_COUNT),
                (String) entity.getProperty(Hashtag.FIELD_NAME_ASSOCIATED_PINS)
        );
    }
}

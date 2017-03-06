package edu.dartmouth.cs.jgualtieri.amina.Data;

import android.icu.util.Calendar;

/**
 * Created by Justin Gualtieri on 2/26/17.
 *
 * Pin will be the data structure used to encapsulate users' responses
 * to questions that will be periodically prompted by the Amina App.
 */

public class Pin {
    private long entryId;
    private String userId;
    private double locationX;
    private double locationY;
    private Calendar dateTime;
    private int safetyStatus;
    private String comment;

    public int getSafetyStatus() {
        return safetyStatus;
    }

    public void setSafetyStatus(int safetyStatus) {
        this.safetyStatus = safetyStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getEntryId(){ return entryId; }

    public void setEntryId(long entryId){ this.entryId = entryId; }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }
}

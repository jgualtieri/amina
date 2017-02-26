package edu.dartmouth.cs.jgualtieri.amina.Data;

import android.icu.util.Calendar;

/**
 * Created by JustinGualtieri on 2/26/17.
 *
 * PromptResponse will be the data structure used to encapsulate users' responses
 * to questions that will be periodically prompted by the Amina App.
 */

public class PromptResponse {
    private String userId;
    private double locationX;
    private double locationY;
    private Calendar dateTime;
    private boolean isSafe;
    private boolean hasFoodAndWater;
    private boolean hasShelter;
    private boolean hasElectricity;

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

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public boolean isHasFoodAndWater() {
        return hasFoodAndWater;
    }

    public void setHasFoodAndWater(boolean hasFoodAndWater) {
        this.hasFoodAndWater = hasFoodAndWater;
    }

    public boolean isHasShelter() {
        return hasShelter;
    }

    public void setHasShelter(boolean hasShelter) {
        this.hasShelter = hasShelter;
    }

    public boolean isHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }
}

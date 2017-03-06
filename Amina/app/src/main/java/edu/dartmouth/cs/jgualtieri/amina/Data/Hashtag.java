package edu.dartmouth.cs.jgualtieri.amina.Data;

/**
 * Created by JustinGualtieri on 3/2/17.
 *
 * Represent a Hashtag that a user enters to describe a particular location that is
 * stored in a Pin data structure.  The Pin stores information about the user who created
 * the Pin, the location where the pin was created and the date and time the pin was
 * created.  Each pin can have multiple Hashtags,
 */

public class Hashtag {
    private long entryId;
    private String value;
    private String[] associatedPins;

    public long getEntryId () {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getAssociatedPins() {
        return associatedPins;
    }

    public void setAssociatedPins(String[] associatedPins) {
        this.associatedPins = associatedPins;
    }
}

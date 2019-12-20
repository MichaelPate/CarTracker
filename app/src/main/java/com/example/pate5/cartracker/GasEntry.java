package com.example.pate5.cartracker;

import java.util.Date;

class GasEntry implements TrackerEntry {
    // Default interface attributes
    private Date entryDate = null;
    private String entryType = null;
    private float entryOdometer = 0;
    private String entryComments = null;


    // Default interface getters and setters
    @Override
    public Date getEntryDate() {
        return this.entryDate;
    }

    @Override
    public void setEntryDate(Date date) {
        this.entryDate = date;
    }

    @Override
    public float getEntryOdometer() {
        return this.entryOdometer;
    }

    @Override
    public void setEntryOdometer(float odo) {
        this.entryOdometer = odo;
    }

    @Override
    public String getEntryComments() {
        return this.entryComments;
    }

    @Override
    public void setEntryComments(String comments) {
        this.entryComments = comments;
    }

    @Override
    public String getEntryType() {
        return this.entryType;
    }

    @Override
    public void setEntryType(String type) {
        this.entryType = type;
    }
}

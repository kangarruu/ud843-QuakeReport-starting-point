package com.example.android.quakereport;

import android.location.Location;

public class Earthquake {
    //Name of the location the earthquake took place
    private String mPlace;
    //The date the month, day, year the earthquake took place
    private long mTimeInMilliseconds;
    //the value of the magnitude of the earthquake
    private double mMag;
    //url associate with USGS information about a given earthquake
    private String mUrl;

    //create an Earthquake object
    public Earthquake(String place, long timeInMilliseconds, double mag, String url) {
        mPlace = place;
        mTimeInMilliseconds = timeInMilliseconds;
        mMag = mag;
        mUrl= url;
    }

    //create getter methods to access Earthquake variables
    public String getPlace(){
        return mPlace;
    }

    public long getTimeInMilliseconds(){
        return mTimeInMilliseconds;
    }

    public double getMag(){
        return mMag;
    }

    public String getUrl(){
        return mUrl;
    }


}

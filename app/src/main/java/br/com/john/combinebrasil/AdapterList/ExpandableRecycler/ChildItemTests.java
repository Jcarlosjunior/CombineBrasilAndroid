package br.com.john.combinebrasil.AdapterList.ExpandableRecycler;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.john.combinebrasil.Classes.Tests;

/**
 * Created by GTAC on 24/07/2017.
 */

public class ChildItemTests implements Parcelable {
    private String name;
    private String Id;
    private String Type;
    private String Athlete;
    private String Selective;
    private long FirstValue;
    private long SecondValue;
    private float Rating;
    private String Wingspan;
    private String User;

    public ChildItemTests(Parcel in) {
        name = in.readString();
    }

    public ChildItemTests(String name, String id, String type, String athlete, String selective, long firstValue, long secondValue, float rating, String wingspan, String user) {
        this.name = name;
        Id = id;
        Type = type;
        Athlete = athlete;
        Selective = selective;
        FirstValue = firstValue;
        SecondValue = secondValue;
        Rating = rating;
        Wingspan = wingspan;
        User = user;
    }

    public  ChildItemTests(Tests tests){
        this.name = tests.getType();
        Id = getId();
        Type = tests.getType();;
        Athlete = tests.getAthlete();
        Selective = tests.getAthlete();
        FirstValue = tests.getFirstValue();
        SecondValue = tests.getSecondValue();
        Rating = tests.getRating();
        Wingspan = tests.getWingspan();
        User = tests.getUser();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAthlete() {
        return Athlete;
    }

    public void setAthlete(String athlete) {
        Athlete = athlete;
    }

    public String getSelective() {
        return Selective;
    }

    public void setSelective(String selective) {
        Selective = selective;
    }

    public long getFirstValue() {
        return FirstValue;
    }

    public void setFirstValue(long firstValue) {
        FirstValue = firstValue;
    }

    public long getSecondValue() {
        return SecondValue;
    }

    public void setSecondValue(long secondValue) {
        SecondValue = secondValue;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getWingspan() {
        return Wingspan;
    }

    public void setWingspan(String wingspan) {
        Wingspan = wingspan;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChildItemTests> CREATOR = new Creator<ChildItemTests>() {
        @Override
        public ChildItemTests createFromParcel(Parcel in) {
            return new ChildItemTests(in);
        }

        @Override
        public ChildItemTests[] newArray(int size) {
            return new ChildItemTests[size];
        }
    };
}

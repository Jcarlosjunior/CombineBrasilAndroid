package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 19/10/2016.
 */

public class Tests {
  private String Id;
    private String Type;
    private String Athlete;
    private String FirstValue;
    private String SecondValue;
    private float Rating;
    private float Wingspan;
    private int Sync;

    public Tests(){}

    public Tests(String id, String type, String athlete, String firstValue, String secondValue, float rating, float wingspan,int sync) {
        Id = id;
        Type = type;
        Athlete = athlete;
        FirstValue = firstValue;
        SecondValue = secondValue;
        Rating = rating;
        Wingspan = wingspan;
        Sync = sync;
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

    public String getFirstValue() {
        return FirstValue;
    }

    public void setFirstValue(String firstValue) {
        FirstValue = firstValue;
    }

    public String getSecondValue() {
        return SecondValue;
    }

    public void setSecondValue(String secondValue) {
        SecondValue = secondValue;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public int getSync() {
        return Sync;
    }

    public void setSync(int sync) {
        Sync = sync;
    }

    public double getWingspan() {
        return Wingspan;
    }

    public void setWingspan(float wingspan) {
        Wingspan = wingspan;
    }
}

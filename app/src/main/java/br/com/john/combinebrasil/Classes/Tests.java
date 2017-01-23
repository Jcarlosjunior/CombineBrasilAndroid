package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 19/10/2016.
 */

public class Tests {
  private String Id;
    private String Type;
    private String Athlete;
    private String Selective;
    private long FirstValue;
    private long SecondValue;
    private float Rating;
    private String Wingspan;
    private String User;
    private int Sync;
    private boolean CanSync;


    public Tests(){}

    public Tests(String id, String type, String athlete, String selective, long firstValue, long secondValue,
                 float rating, String wingspan, String user, int sync, boolean canSync) {
        Id = id;
        Type = type;
        Athlete = athlete;
        Selective = selective;
        FirstValue = firstValue;
        SecondValue = secondValue;
        Rating = rating;
        Wingspan = wingspan;
        User = user;
        Sync = sync;
        CanSync = canSync;
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

    public int getSync() {
        return Sync;
    }

    public void setSync(int sync) {
        Sync = sync;
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

    public String getSelective() {
        return Selective;
    }

    public void setSelective(String selective) {
        Selective = selective;
    }

    public boolean getCanSync() {
        return CanSync;
    }

    public void setCanSync(boolean canSync) {
        CanSync = canSync;
    }
}

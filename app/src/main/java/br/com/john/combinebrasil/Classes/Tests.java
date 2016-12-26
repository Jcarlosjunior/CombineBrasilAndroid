package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 19/10/2016.
 */

public class Tests {
  private String Id;
    private String Type;
    private String Athlete;
    private String Value;
    private String Rating;

    public Tests(){}

    public Tests(String id, String type, String athlete, String value, String rating) {
        Id = id;
        Type = type;
        Athlete = athlete;
        Value = value;
        Rating = rating;
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

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}

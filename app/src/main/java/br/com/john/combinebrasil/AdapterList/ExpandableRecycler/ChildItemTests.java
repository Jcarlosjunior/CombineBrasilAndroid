package br.com.john.combinebrasil.AdapterList.ExpandableRecycler;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.john.combinebrasil.Classes.Tests;

/**
 * Created by GTAC on 24/07/2017.
 */

public class ChildItemTests implements Parcelable {
    private String Type;
    private String IdAthlete;
    private String NameAthlete;
    private String Id;
    private String name;
    private int FirstResult;
    private int SecondResult;
    private int Raiting;
    private int Position;
    private String Positions;
    private String Wingspan;
    private String User;
    private float Rating;
    private String ImageURL;
    private String ValueType;

    public ChildItemTests(Parcel in) {
        name = in.readString();
    }

    public ChildItemTests() {
    }

    public static Creator<ChildItemTests> getCREATOR() {
        return CREATOR;
    }

    public ChildItemTests(String type, String idAthlete, String nameAthlete, String id, String name, int firstResult, int secondResult, int raiting, int position, String positions, String wingspan, String user, float rating, String imageURL) {
        Type = type;
        IdAthlete = idAthlete;
        NameAthlete = nameAthlete;
        Id = id;
        this.name = name;
        FirstResult = firstResult;
        SecondResult = secondResult;
        Raiting = raiting;
        Position = position;
        Positions = positions;
        Wingspan = wingspan;
        User = user;
        Rating = rating;
        ImageURL = imageURL;
    }

    public  ChildItemTests(Tests tests){
        this.name = tests.getType();
        Id = getId();
        Type = tests.getType();;
        IdAthlete = tests.getAthlete();
        Rating = tests.getRating();
        FirstResult = (int)tests.getFirstValue();
        SecondResult = (int)tests.getSecondValue();
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

    public String getIdAthlete() {
        return IdAthlete;
    }

    public void setIdAthlete(String idAthlete) {
        IdAthlete = idAthlete;
    }

    public String getNameAthlete() {
        return NameAthlete;
    }

    public void setNameAthlete(String nameAthlete) {
        NameAthlete = nameAthlete;
    }

    public int getFirstResult() {
        return FirstResult;
    }

    public void setFirstResult(int firstResult) {
        FirstResult = firstResult;
    }

    public int getSecondResult() {
        return SecondResult;
    }

    public void setSecondResult(int secondResult) {
        SecondResult = secondResult;
    }

    public int getRaiting() {
        return Raiting;
    }

    public void setRaiting(int raiting) {
        Raiting = raiting;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public String getPositions() {
        return Positions;
    }

    public void setPositions(String positions) {
        Positions = positions;
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

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getValueType() {
        return ValueType;
    }

    public void setValueType(String valueType) {
        ValueType = valueType;
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

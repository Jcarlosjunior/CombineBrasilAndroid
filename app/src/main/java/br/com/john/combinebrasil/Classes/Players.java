package br.com.john.combinebrasil.Classes;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by GTAC on 24/10/2016.
 */

public class Players{
    String id;
    String Name;
    String Age;
    String idSelective;
    String details;

    public Players(){}

    public Players(String id, String name, String age, String idSelective, String details) {
        this.id = id;
        Name = name;
        Age = age;
        this.idSelective = idSelective;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getIdSelective() {
        return idSelective;
    }

    public void setIdSelective(String idSelective) {
        this.idSelective = idSelective;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

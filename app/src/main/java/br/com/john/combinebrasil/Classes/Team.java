package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class Team {
    private String Id;
    private String Name;
    private String City;
    private String Modality;

    public Team(){}

    public Team(String id, String name, String city, String modality) {
        Id = id;
        Name = name;
        City = city;
        Modality = modality;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getModality() {
        return Modality;
    }

    public void setModality(String modality) {
        Modality = modality;
    }
}

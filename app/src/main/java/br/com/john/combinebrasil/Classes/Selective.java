package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class Selective {
    private String Id;
    private String Title;
    private String Team;
    private String Date;
    private String CodeSelective;
    private boolean CanSync;
    private String City;
    private String Neighborhood;
    private String State;
    private String Street;
    private String PostalCode;
    private String Notes;
    private String Address;

    public Selective() {
    }

    public Selective(String id, String title, String team, String date, String codeSelective, boolean canSync, String city, String neighbothood, String state, String street, String postalCode, String notes, String address) {
        Id = id;
        Title = title;
        Team = team;
        Date = date;
        CodeSelective = codeSelective;
        CanSync = canSync;
        City = city;
        Neighborhood = neighbothood;
        State = state;
        Street = street;
        PostalCode = postalCode;
        Notes = notes;
        Address = address;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCodeSelective() {
        return CodeSelective;
    }

    public void setCodeSelective(String codeSelective) {
        CodeSelective = codeSelective;
    }

    public boolean getCanSync() {
        return CanSync;
    }

    public void setCanSync(boolean canSync) {
        CanSync = canSync;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getNeighborhood() {
        return Neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        Neighborhood = neighborhood;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

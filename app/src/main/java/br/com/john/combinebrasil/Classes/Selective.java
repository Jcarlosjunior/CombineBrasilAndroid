package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class Selective {
    private String Id;
    private String Title;
    private String Team;
    private String Date;

    public Selective() {
    }

    public Selective(String id, String title, String team, String date) {
        Id = id;
        Title = title;
        Team = team;
        Date = date;
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
}

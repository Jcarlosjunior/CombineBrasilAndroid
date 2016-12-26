package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class TeamUsers {
    private String Id;
    private String User;
    private String Team;

    public TeamUsers(){

    }

    public TeamUsers(String id, String user, String team) {
        Id = id;
        User = user;
        Team = team;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }
}

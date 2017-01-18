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

    public Selective() {
    }

    public Selective(String id, String title, String team, String date, String codeSelective, boolean canSync) {
        Id = id;
        Title = title;
        Team = team;
        Date = date;
        CodeSelective = codeSelective;
        CanSync = canSync;
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
}

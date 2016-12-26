package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class Positions
{
    private String ID;
    private String NAME;
    private String DESCRIPTION;

    public Positions(){}

    public Positions (String id, String name, String description){
        ID = id;
        NAME = name;
        DESCRIPTION = description;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }
}

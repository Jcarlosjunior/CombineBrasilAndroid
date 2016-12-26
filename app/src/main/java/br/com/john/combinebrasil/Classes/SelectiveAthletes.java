package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class SelectiveAthletes {
    private String Id;
    private String Athlete;
    private String Selective;
    private String InscriptionNumber;
    private boolean Presence;

    public SelectiveAthletes() {
    }

    public SelectiveAthletes(String id, String athlete, String selective, String inscriptionNumber, boolean presence) {
        Id = id;
        Athlete = athlete;
        Selective = selective;
        InscriptionNumber = inscriptionNumber;
        Presence = presence;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAthlete() {
        return Athlete;
    }

    public void setAthlete(String athlete) {
        Athlete = athlete;
    }

    public String getSelective() {
        return Selective;
    }

    public void setSelective(String selective) {
        Selective = selective;
    }

    public String getInscriptionNumber() {
        return InscriptionNumber;
    }

    public void setInscriptionNumber(String inscriptionNumber) {
        InscriptionNumber = inscriptionNumber;
    }

    public boolean getPresence() {
        return Presence;
    }

    public void setPresence(boolean presence) {
        Presence = presence;
    }
}

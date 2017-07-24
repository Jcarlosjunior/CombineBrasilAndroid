package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 24/07/2017.
 */

public class RankingPositions {
    private String ATHLETE;
    private String ID_POSITION;
    private String ID_ATHLETE;
    private String POSITION;
    private String DESCRIPTION;

    public RankingPositions(){
    }

    public RankingPositions(String ATHLETE, String ID_POSITION, String ID_ATHLETE, String POSITION, String DESCRIPTION) {
        this.ATHLETE = ATHLETE;
        this.ID_POSITION = ID_POSITION;
        this.ID_ATHLETE = ID_ATHLETE;
        this.POSITION = POSITION;
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getATHLETE() {
        return ATHLETE;
    }

    public void setATHLETE(String ATHLETE) {
        this.ATHLETE = ATHLETE;
    }

    public String getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(String POSITION) {
        this.POSITION = POSITION;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getID_POSITION() {
        return ID_POSITION;
    }

    public void setID_POSITION(String ID_POSITION) {
        this.ID_POSITION = ID_POSITION;
    }

    public String getID_ATHLETE() {
        return ID_ATHLETE;
    }

    public void setID_ATHLETE(String ID_ATHLETE) {
        this.ID_ATHLETE = ID_ATHLETE;
    }
}

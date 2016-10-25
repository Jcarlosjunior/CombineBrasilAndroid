package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 24/10/2016.
 */

public class Results {
    String id;
    String idSelective;
    String idTest;
    String idPlayer;
    String status;
    String firstValue;
    String secondValue;

    public Results(){}

    public Results(String id, String idSelective, String idTest, String idPlayer, String status, String firstValue, String secondValue) {
        this.id = id;
        this.idSelective = idSelective;
        this.idTest = idTest;
        this.idPlayer = idPlayer;
        this.status = status;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSelective() {
        return idSelective;
    }

    public void setIdSelective(String idSelective) {
        this.idSelective = idSelective;
    }

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }
}

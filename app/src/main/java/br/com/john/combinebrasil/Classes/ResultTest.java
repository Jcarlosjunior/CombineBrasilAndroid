package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 03/08/17.
 */

public class ResultTest {
    String IdAthlete;
    String NameAthlete;
    String Id;
    String name;
    int FirstResult;
    int SecondResult;
    int Raiting;
    int Position;
    String Positions;

    public ResultTest(){

    }

    public ResultTest(String idAthelte, String nameAthlete, String id, String name, int firstResult, int secondResult, int raiting, int position, String positions) {
        IdAthlete = idAthelte;
        NameAthlete = nameAthlete;
        Id = id;
        this.name = name;
        FirstResult = firstResult;
        SecondResult = secondResult;
        Raiting = raiting;
        Position = position;
        Positions = positions;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFirstResult() {
        return FirstResult;
    }

    public void setFirstResult(int firstResult) {
        FirstResult = firstResult;
    }

    public int getSecondResult() {
        return SecondResult;
    }

    public void setSecondResult(int secondResult) {
        SecondResult = secondResult;
    }

    public int getRaiting() {
        return Raiting;
    }

    public void setRaiting(int raiting) {
        Raiting = raiting;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public String getPositions() {
        return Positions;
    }

    public void setPositions(String positions) {
        Positions = positions;
    }

    public String getIdAthlete() {
        return IdAthlete;
    }

    public void setIdAthlete(String idAthlete) {
        IdAthlete = idAthlete;
    }

    public String getNameAthlete() {
        return NameAthlete;
    }

    public void setNameAthlete(String nameAthlete) {
        NameAthlete = nameAthlete;
    }
}

package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 20/03/2017.
 */

public class CEP {
    private String Cep;
    private String Street;
    private String Neighborhood;
    private String State;
    private String City;

    public CEP(){}
    public CEP(String cep, String street,  String neighborhood, String state, String city) {
        Cep = cep;
        Street = street;
        Neighborhood = neighborhood;
        State = state;
        City = city;
    }

    public String getCep() {
        return Cep;
    }

    public void setCep(String cep) {
        Cep = cep;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
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

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}

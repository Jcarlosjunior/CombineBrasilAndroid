package br.com.john.combinebrasil.Classes;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.ExpandableRecycler.ChildItemTests;

/**
 * Created by GTAC on 03/08/17.
 */

public class ResultTestsAthletes {
    TestTypes Type;
    ArrayList<ChildItemTests> Tests;

    public ResultTestsAthletes(){

    }

    public ResultTestsAthletes(TestTypes types, ArrayList<ChildItemTests> results){
        this.Type = types;
        this.Tests = results;

    }

    public TestTypes getType() {
        return Type;
    }

    public void setType(TestTypes type) {
        Type = type;
    }

    public ArrayList<ChildItemTests> getTests() {
        return Tests;
    }

    public void setTests(ArrayList<ChildItemTests> tests) {
        Tests = tests;
    }
}

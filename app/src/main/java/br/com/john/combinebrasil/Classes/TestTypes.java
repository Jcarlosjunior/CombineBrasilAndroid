package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class TestTypes {
    private String Id;
    private String Name;
    private String AttemptsLimit;
    private String VisibleToReport;

    public TestTypes(){}

    public TestTypes(String id, String name, String attemptsLimit, String visibleToReport) {
        Id = id;
        Name = name;
        AttemptsLimit = attemptsLimit;
        VisibleToReport = visibleToReport;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAttemptsLimit() {
        return AttemptsLimit;
    }

    public void setAttemptsLimit(String attemptsLimit) {
        AttemptsLimit = attemptsLimit;
    }

    public String getVisibleToReport() {
        return VisibleToReport;
    }

    public void setVisibleToReport(String visibleToReport) {
        VisibleToReport = visibleToReport;
    }
}

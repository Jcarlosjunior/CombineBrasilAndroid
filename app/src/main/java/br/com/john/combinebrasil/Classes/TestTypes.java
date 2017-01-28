package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class TestTypes {
    private String Id;
    private String Name;
    private String AttemptsLimit;
    private boolean VisibleToReport;
    private String Description;
    private String ValueType;
    private String IconImageURL;
    private String TutorialImageURL;

    public TestTypes(){}

    public TestTypes(String id, String name, String attemptsLimit, boolean visibleToReport, String description, String valueType, String iconImageURL, String tutorialImageURL) {
        Id = id;
        Name = name;
        AttemptsLimit = attemptsLimit;
        VisibleToReport = visibleToReport;
        Description = description;
        ValueType = valueType;
        IconImageURL = iconImageURL;
        TutorialImageURL = tutorialImageURL;
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

    public boolean getVisibleToReport() {
        return VisibleToReport;
    }

    public void setVisibleToReport(boolean visibleToReport) {
        VisibleToReport = visibleToReport;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getValueType() {
        return ValueType;
    }

    public void setValueType(String valueType) {
        ValueType = valueType;
    }

    public String getIconImageURL() {
        return IconImageURL;
    }

    public void setIconImageURL(String iconImageURL) {
        IconImageURL = iconImageURL;
    }

    public String getTutorialImageURL() {
        return TutorialImageURL;
    }

    public void setTutorialImageURL(String tutorialImageURL) {
        TutorialImageURL = tutorialImageURL;
    }
}

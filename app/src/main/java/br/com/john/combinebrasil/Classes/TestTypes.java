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
    private boolean Selected;
    private boolean DefaultTest;
    private String SiblingTestType;
    private boolean RequiredToReport;
    private boolean MainTest;

    public TestTypes(){
        VisibleToReport = false;
        Selected = false;
        DefaultTest = false;
    }

    public TestTypes(String id, String name, String attemptsLimit, boolean visibleToReport,
                     String description, String valueType, String iconImageURL, String tutorialImageURL,
                     String siblingTestType, boolean requiredToReport, boolean mainTest) {
        Id = id;
        Name = name;
        AttemptsLimit = attemptsLimit;
        VisibleToReport = visibleToReport;
        Description = description;
        ValueType = valueType;
        IconImageURL = iconImageURL;
        TutorialImageURL = tutorialImageURL;
        SiblingTestType = siblingTestType;
        RequiredToReport = requiredToReport;
        MainTest = mainTest;
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

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public boolean isDefaultTest() {
        return DefaultTest;
    }

    public void setDefaultTest(boolean defaultTest) {
        DefaultTest = defaultTest;
    }

    public boolean isVisibleToReport() {
        return VisibleToReport;
    }

    public String getSiblingTestType() {
        return SiblingTestType;
    }

    public void setSiblingTestType(String siblingTestType) {
        SiblingTestType = siblingTestType;
    }

    public boolean isRequiredToReport() {
        return RequiredToReport;
    }

    public void setRequiredToReport(boolean requiredToReport) {
        RequiredToReport = requiredToReport;
    }

    public boolean isMainTest() {
        return MainTest;
    }

    public void setMainTest(boolean mainTest) {
        MainTest = mainTest;
    }
}

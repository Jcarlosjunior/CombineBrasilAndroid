package br.com.john.combinebrasil.Connection.JSONServices;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.CEP;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Classes.TeamUsers;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Services;

/**
 * Created by GTAC on 19/10/2016.
 */


public class DeserializerJsonElements {
    private String response;

    public DeserializerJsonElements() {
    }

    public DeserializerJsonElements(String response) {
        this.response = response;
    }

    public User getLogin() {
        User login = new User();
        try {
            JSONObject json = new JSONObject(this.response);
            login.setEmail(json.optString(Constants.LOGIN_EMAIL));
            login.setIsAdmin(json.optBoolean(Constants.LOGIN_ISADMIN));
            login.setCanWrite(json.optBoolean(Constants.LOGIN_CANWRITE));
        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return login;
    }

    /*
    ***********************************DESERIALIZER USER********************************************
    **/
    public User getUsers() {
        User user = null;
        try {
            JSONArray jsonArray = new JSONArray(response);

            for(int i=0;i<=jsonArray.length()-1;i++) {
                JSONObject json = new JSONObject(jsonArray.getString(i));

                user = new User(
                        json.optString(Constants.USER_ID),
                        json.optString(Constants.USER_NAME),
                        json.optString(Constants.USER_EMAIL),
                        json.optBoolean(Constants.USER_ISADMIN),
                        json.optBoolean(Constants.USER_CANWRITE),
                        json.optString(Constants.USER_TOKEN)
                );
            }
        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return user;
    }

    public User getObjectsUser() {
        User user = null;
        try {
            JSONObject json = new JSONObject(this.response);
            user = new User(
                    json.optString(Constants.USER_ID),
                    json.optString(Constants.USER_NAME),
                    json.optString(Constants.USER_EMAIL),
                    json.optBoolean(Constants.USER_ISADMIN),
                    json.optBoolean(Constants.USER_CANWRITE),
                    json.optString(Constants.USER_TOKEN)
            );
        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return user;
    }


    /*
    ************************************DESERIALIZER ROUTEACTIVITYDATAS*****************************
    */


    /********************************************************
     * ATHLETES
     ******************************/

    public ArrayList<Athletes> getAthletes() {
        ArrayList<Athletes> AthletesList = new ArrayList<Athletes>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject json = new JSONObject(jsonArray.getString(i));
                Athletes athletesEntity = new Athletes(
                        json.optString(Constants.ATHLETES_ID),
                        json.optString(Constants.ATHLETES_NAME),
                        json.optString(Constants.ATHLETES_BIRTHDAY),
                        json.optString(Constants.ATHLETES_CPF),
                        json.optString(Constants.ATHLETES_ADDRESS),
                        json.optString(Constants.ATHLETES_DESIRABLE_POSITION),
                        json.optDouble(Constants.ATHLETES_HEIGHT),
                        json.optDouble(Constants.ATHLETES_WEIGHT),
                        json.optString(Constants.ATHLETES_CREATEDAT),
                        json.optString(Constants.ATHLETES_UPDATEAT),
                        "",
                        json.optString(Constants.ATHLETES_EMAIL),
                        json.optString(Constants.ATHLETES_PHONE),
                        true,
                        json.optBoolean(Constants.ATHLETES_TERMSACCEPTED, true)
                );
                AthletesList.add(athletesEntity);
            }

        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return AthletesList;
    }

    public Athletes getAthlete() {
        Athletes athlete = new Athletes();
        try {

            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject json = new JSONObject(jsonArray.getString(i));
                athlete = new Athletes(
                        json.optString(Constants.ATHLETES_ID),
                        json.optString(Constants.ATHLETES_NAME),
                        json.optString(Constants.ATHLETES_BIRTHDAY),
                        json.optString(Constants.ATHLETES_CPF),
                        json.optString(Constants.ATHLETES_ADDRESS),
                        json.optString(Constants.ATHLETES_DESIRABLE_POSITION),
                        json.optDouble(Constants.ATHLETES_HEIGHT),
                        json.optDouble(Constants.ATHLETES_WEIGHT),
                        json.optString(Constants.ATHLETES_CREATEDAT),
                        json.optString(Constants.ATHLETES_UPDATEAT),
                        "",
                        json.optString(Constants.ATHLETES_EMAIL),
                        json.optString(Constants.ATHLETES_PHONE),
                        true,
                        json.optBoolean(Constants.ATHLETES_TERMSACCEPTED, true)
                );
            }
        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
            athlete = null;
        }
        return athlete;
    }

    public Athletes getObjAthlete() {
        Athletes athlete = new Athletes();
        try {

            JSONObject json = new JSONObject(response);

                athlete = new Athletes(
                        json.optString(Constants.ATHLETES_ID),
                        json.optString(Constants.ATHLETES_NAME),
                        json.optString(Constants.ATHLETES_BIRTHDAY),
                        json.optString(Constants.ATHLETES_CPF),
                        json.optString(Constants.ATHLETES_ADDRESS),
                        json.optString(Constants.ATHLETES_DESIRABLE_POSITION),
                        json.optDouble(Constants.ATHLETES_HEIGHT),
                        json.optDouble(Constants.ATHLETES_WEIGHT),
                        json.optString(Constants.ATHLETES_CREATEDAT),
                        json.optString(Constants.ATHLETES_UPDATEAT),
                        "",
                        json.optString(Constants.ATHLETES_EMAIL),
                        json.optString(Constants.ATHLETES_PHONE),
                        true,
                        json.optBoolean(Constants.ATHLETES_TERMSACCEPTED, true)
                );
        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
            athlete = null;
        }
        return athlete;
    }
    /***************************************
     * POSITIONS
     ********************************************/
    public ArrayList<Positions> getPositions() {
        ArrayList<Positions> positions = new ArrayList<Positions>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Positions obj = new Positions(json.optString(Constants.POSITIONS_ID),
                            json.optString(Constants.POSITIONS_NAME),
                            json.optString(Constants.POSITIONS_DESCRIPTIONS));

                    positions.add(obj);
                }
            }
        } catch (JSONException e) {
            positions = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return positions;
    }

    /***************************************
     * SELECTIVE ATHLETES
     ********************************************/
    public ArrayList<SelectiveAthletes> getSelectiveAthletes() {
        ArrayList<SelectiveAthletes> selectiveAthletes = new ArrayList<SelectiveAthletes>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    SelectiveAthletes obj = new SelectiveAthletes(
                            json.optString(Constants.SELECTIVEATHLETES_ID),
                            json.optString(Constants.SELECTIVEATHLETES_ATHLETE),
                            json.optString(Constants.SELECTIVEATHLETES_SELECTIVE),
                            json.optString(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER),
                            json.optBoolean(Constants.SELECTIVEATHLETES_PRESENCE)
                    );

                    selectiveAthletes.add(obj);
                }
            }
        } catch (JSONException e) {
            selectiveAthletes = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return selectiveAthletes;
    }

    public SelectiveAthletes getSelectiveAthlete() {
        SelectiveAthletes selectiveAthletes = new SelectiveAthletes();
        try {
            JSONArray jsonArray = new JSONArray(response);

            for(int i=0; i<=jsonArray.length()-1; i++) {
                JSONObject json = new JSONObject(jsonArray.getString(i));
                selectiveAthletes = new SelectiveAthletes(
                        json.optString(Constants.SELECTIVEATHLETES_ID),
                        json.optString(Constants.SELECTIVEATHLETES_ATHLETE),
                        json.optString(Constants.SELECTIVEATHLETES_SELECTIVE),
                        json.optString(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER),
                        json.optBoolean(Constants.SELECTIVEATHLETES_PRESENCE)
                );
            }
        } catch (JSONException e) {
            selectiveAthletes = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return selectiveAthletes;

    }

    public SelectiveAthletes getObjSelectiveAthlete() {
        SelectiveAthletes selectiveAthletes = new SelectiveAthletes();
        try {
            JSONObject json = new JSONObject(response);

                selectiveAthletes = new SelectiveAthletes(
                        json.optString(Constants.SELECTIVEATHLETES_ID),
                        json.optString(Constants.SELECTIVEATHLETES_ATHLETE),
                        json.optString(Constants.SELECTIVEATHLETES_SELECTIVE),
                        json.optString(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER),
                        json.optBoolean(Constants.SELECTIVEATHLETES_PRESENCE)
                );

        } catch (JSONException e) {
            selectiveAthletes = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return selectiveAthletes;

    }

    /***************************************
     * SELECTIVE
     ********************************************/
    public ArrayList<Selective> getSelective() {
        ArrayList<Selective> selectives = new ArrayList<Selective>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Selective obj = new Selective(
                            json.optString(Constants.SELECTIVES_ID),
                            json.optString(Constants.SELECTIVES_TITLE),
                            json.optString(Constants.SELECTIVES_TEAM),
                            json.optString(Constants.SELECTIVES_DATE),
                            json.optString(Constants.SELECTIVES_CODESELECTIVE),
                            json.optBoolean(Constants.SELECTIVES_CANSYNC, false)
                    );

                    selectives.add(obj);
                }
            }
        } catch (JSONException e) {
            selectives = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return selectives;
    }

    /***************************************
     * TEAMUSERS
     ********************************************/
    public ArrayList<TeamUsers> getTeamUsers() {
        ArrayList<TeamUsers> teamUserses = new ArrayList<TeamUsers>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    TeamUsers obj = new TeamUsers(
                            json.optString(Constants.TEAMUSERS_ID),
                            json.optString(Constants.TEAMUSERS_USER),
                            json.optString(Constants.TEAMUSERS_TEAM)
                    );

                    teamUserses.add(obj);
                }
            }
        } catch (JSONException e) {
            teamUserses = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return teamUserses;
    }

    public TeamUsers getTeamUser() {
        TeamUsers team = new TeamUsers();
        try {
            JSONArray jsonArray = new JSONArray(response);

            for(int i=0; i<=jsonArray.length()-1; i++){
                JSONObject json = new JSONObject(jsonArray.getString(i));
                team = new TeamUsers(
                        json.optString(Constants.TEAMUSERS_ID),
                        json.optString(Constants.TEAMUSERS_USER),
                        json.optString(Constants.TEAMUSERS_TEAM)
                );
            }
        } catch (JSONException e) {
            team = null;
            Log.i("ERROR: getTeamUser", e.getMessage());
        }
        return team;
    }

    /***************************************
     * TEAM
     ********************************************/
    public ArrayList<Team> getTeam() {
        ArrayList<Team> teams = new ArrayList<Team>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Team obj = new Team(
                            json.optString(Constants.TEAM_ID),
                            json.optString(Constants.TEAM_NAME),
                            json.optString(Constants.TEAM_CITY),
                            json.optString(Constants.TEAM_MODALITY)
                    );

                    teams.add(obj);
                }
            }
        } catch (JSONException e) {
            teams = null;
            Log.i("ERROR: getTeam", e.getMessage());
        }
        return teams;
    }

    /***************************************
     * TESTTYPES
     ********************************************/
    public ArrayList<TestTypes> getTestTypes() {
        ArrayList<TestTypes> teams = new ArrayList<TestTypes>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    TestTypes obj = new TestTypes(
                            json.optString(Constants.TESTTYPES_ID),
                            json.optString(Constants.TESTTYPES_NAME),
                            json.optString(Constants.TESTTYPES_ATTEMPTSLIMIT),
                            json.optBoolean(Constants.TESTTYPES_VISIBLETOREPORT),
                            json.optString(Constants.TESTTYPES_DESCRIPTION),
                            json.optString(Constants.TESTTYPES_VALUETYPES),
                            json.optString(Constants.TESTTYPES_ICONIMAGEURL),
                            json.optString(Constants.TESTTYPES_TUTORIALIMAGEURL)
                    );

                    teams.add(obj);
                }
            }
        } catch (JSONException e) {
            teams = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return teams;
    }

    /***************************************
     * TESTS
     ********************************************/
    public ArrayList<Tests> getTest() {
        ArrayList<Tests> testses = new ArrayList<Tests>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Tests obj = new Tests(
                            json.optString(Constants.TESTS_ID),
                            json.optString(Constants.TESTS_TYPE),
                            json.optString(Constants.TESTS_ATHLETE),
                            json.optString(Constants.TESTS_SELECTIVE),
                            json.optLong(Constants.TESTS_FIRST_VALUE),
                            json.optLong(Constants.TESTS_SECOND_VALUE),
                            (float) (json.optDouble(Constants.TESTS_RATING)),
                            json.optString(Constants.TESTS_WINGSPAN),
                            json.optString(Constants.TESTS_USER),
                            Services.convertBoolInInt(true),
                            true
                    );

                    testses.add(obj);
                }
            }
        } catch (JSONException e) {
            testses = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return testses;
    }
    public Tests getTestObject() {
        Tests test = new Tests();
        try {
            JSONObject json = new JSONObject(response);
            test = new Tests(
                json.optString(Constants.TESTS_ID),
                json.optString(Constants.TESTS_TYPE),
                json.optString(Constants.TESTS_ATHLETE),
                json.optString(Constants.TESTS_SELECTIVE),
                json.optLong(Constants.TESTS_FIRST_VALUE),
                json.optLong(Constants.TESTS_SECOND_VALUE),
                (float) (json.optDouble(Constants.TESTS_RATING)),
                json.optString(Constants.TESTS_WINGSPAN),
                json.optString(Constants.TESTS_USER),
                Services.convertBoolInInt(true),
                true
            );
        } catch (JSONException e) {
            test = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return test;
    }

    public Tests getTestObjectTest() {
        Tests tests = new Tests();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                     tests = new Tests(
                            json.optString(Constants.TESTS_ID),
                            json.optString(Constants.TESTS_TYPE),
                            json.optString(Constants.TESTS_ATHLETE),
                            json.optString(Constants.TESTS_SELECTIVE),
                            json.optLong(Constants.TESTS_FIRST_VALUE),
                            json.optLong(Constants.TESTS_SECOND_VALUE),
                            (float) (json.optDouble(Constants.TESTS_RATING)),
                            json.optString(Constants.TESTS_WINGSPAN),
                            json.optString(Constants.TESTS_USER),
                            Services.convertBoolInInt(true),
                            true
                    );
                }
            }
        } catch (JSONException e) {
            tests = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return tests;
    }

    /**********************************CEP**********************************/
    public CEP getCep() {
        CEP cep = null;
        try {
            JSONObject json = new JSONObject(this.response);
            cep = new CEP(
                    json.optString(Constants.CEP),
                    json.optString(Constants.STREET),
                    json.optString(Constants.NEIGHBORHOOD),
                    json.optString(Constants.STATE),
                    json.optString(Constants.CITY)
            );
        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return cep;
    }
}

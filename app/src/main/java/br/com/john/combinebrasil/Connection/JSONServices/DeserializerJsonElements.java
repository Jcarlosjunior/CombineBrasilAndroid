package br.com.john.combinebrasil.Connection.JSONServices;

import android.util.Log;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Login;
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

    public DeserializerJsonElements(){}

    public DeserializerJsonElements(String response){
        this.response = response;
    }

    public Login getLogin(){
        Login login = new Login();
        try{
            JSONObject json = new JSONObject(this.response);
            login.setEmail(json.getString(Constants.LOGIN_EMAIL));
            login.setIsAdmin(json.getBoolean(Constants.LOGIN_ISADMIN));
            login.setCanWrite(json.getBoolean(Constants.LOGIN_CANWRITE));
        }catch (JSONException jsonExc){
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return login;
    }

    /*
    ***********************************DESERIALIZER USER********************************************
    **/
    public User getObjectsUser(){
        User user = new User();
        try{
            JSONObject json = new JSONObject(this.response);
            user.setName(json.optString(Constants.USER_NAME));
            user.setEmail(json.optString(Constants.USER_EMAIL));
            user.setIsAdmin(json.optBoolean(Constants.LOGIN_ISADMIN));
            user.setCanWrite(json.optBoolean(Constants.LOGIN_CANWRITE));
            user.setToken(json.optString(Constants.USER_TOKEN));

        }catch (JSONException jsonExc){
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return user;
    }


    /*
    ************************************DESERIALIZER ROUTEACTIVITYDATAS*****************************
    */


    /********************************************************ATHLETES******************************/

    public ArrayList<Athletes> getAthletes() {
        ArrayList<Athletes> AthletesList = new ArrayList<Athletes>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject json = new JSONObject(jsonArray.getString(i));
                Athletes athletesEntity = new Athletes();
                    athletesEntity.setId(json.getString(Constants.ATHLETES_ID));
                    athletesEntity.setName(json.getString(Constants.ATHLETES_NAME));
                    athletesEntity.setCPF(json.getString(Constants.ATHLETES_CPF ));
                    athletesEntity.setBirthday(json.getString(Constants.ATHLETES_BIRTHDAY));
                    athletesEntity.setHeight(json.getInt(Constants.ATHLETES_HEIGHT));
                    athletesEntity.setWeight(json.getInt(Constants.ATHLETES_WEIGHT));
                    athletesEntity.setCreatedAt(json.getString(Constants.ATHLETES_CREATEDAT));
                    athletesEntity.setUpdateAt(json.getString(Constants.ATHLETES_UPDATEAT));
                AthletesList.add(athletesEntity);
            }

        } catch (JSONException jsonExc) {
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return AthletesList;
    }

    /***************************************POSITIONS********************************************/
    public ArrayList<Positions> getPositions(){
        ArrayList<Positions> positions = new ArrayList<Positions>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length()>0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Positions obj = new Positions(json.getString(Constants.POSITIONS_ID),
                            json.getString(Constants.POSITIONS_NAME),
                           "");

                    positions.add(obj);
                }
            }
        }catch (JSONException e){
            positions  = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return positions;
    }

    /***************************************SELECTIVE ATHLETES********************************************/
    public ArrayList<SelectiveAthletes> getSelectiveAthletes(){
        ArrayList<SelectiveAthletes> selectiveAthletes = new ArrayList<SelectiveAthletes>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length()>0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    SelectiveAthletes obj = new SelectiveAthletes(
                            json.getString(Constants.SELECTIVEATHLETES_ID),
                            json.getString(Constants.SELECTIVEATHLETES_ATHLETE),
                            json.getString(Constants.SELECTIVEATHLETES_SELECTIVE),
                            json.getString(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER),
                            json.getBoolean(Constants.SELECTIVEATHLETES_PRESENCE)
                            );

                    selectiveAthletes.add(obj);
                }
            }
        }catch (JSONException e){
            selectiveAthletes  = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return selectiveAthletes;
    }

    /***************************************SELECTIVE********************************************/
    public ArrayList<Selective> getSelective(){
        ArrayList<Selective> selectives = new ArrayList<Selective>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length()>0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Selective obj = new Selective(
                            json.getString(Constants.SELECTIVES_ID),
                            json.getString(Constants.SELECTIVES_TITLE),
                            json.getString(Constants.SELECTIVES_TEAM),
                            json.getString(Constants.SELECTIVES_DATE)
                    );

                    selectives.add(obj);
                }
            }
        }catch (JSONException e){
            selectives  = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return selectives;
    }

    /***************************************TEAMUSERS********************************************/
    public ArrayList<TeamUsers> getTeamUsers(){
        ArrayList<TeamUsers> teamUserses = new ArrayList<TeamUsers>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length()>0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    TeamUsers obj = new TeamUsers(
                            json.getString(Constants.TEAMUSERS_ID),
                            json.getString(Constants.TEAMUSERS_USER),
                            json.getString(Constants.TEAMUSERS_TEAM)
                    );

                    teamUserses.add(obj);
                }
            }
        }catch (JSONException e){
            teamUserses  = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return teamUserses;
    }

    /***************************************TEAM********************************************/
    public ArrayList<Team> getTeam(){
        ArrayList<Team> teams = new ArrayList<Team>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length()>0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Team obj = new Team(
                            json.getString(Constants.TEAM_ID),
                            json.getString(Constants.TEAM_NAME),
                            json.getString(Constants.TEAM_CITY), ""
                    );

                    teams.add(obj);
                }
            }
        }catch (JSONException e){
            teams  = null;
            Log.i("ERROR: getTeam", e.getMessage());
        }
        return teams;
    }

    /***************************************TESTTYPES********************************************/
    public ArrayList<TestTypes> getTestTypes(){
        ArrayList<TestTypes> teams = new ArrayList<TestTypes>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length()>0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    TestTypes obj = new TestTypes(
                            json.getString(Constants.TESTTYPES_ID),
                            json.getString(Constants.TESTTYPES_NAME),
                            json.getString(Constants.TESTTYPES_ATTEMPTSLIMIT),
                            json.getBoolean(Constants.TESTTYPES_VISIBLETOREPORT)
                    );

                    teams.add(obj);
                }
            }
        }catch (JSONException e){
            teams  = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return teams;
    }

    /***************************************TESTS********************************************/
    public ArrayList<Tests> getTest(){
        ArrayList<Tests> testses = new ArrayList<Tests>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length()>0) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = new JSONObject(jsonArray.getString(i));
                    Tests obj = new Tests(
                            json.getString(Constants.TESTS_ID),
                            json.getString(Constants.TESTS_TYPE),
                            json.getString(Constants.TESTS_ATHLETE),
                            json.getString(Constants.TESTS_VALUE),
                            json.getString(Constants.TESTS_RATING)
                    );

                    testses.add(obj);
                }
            }
        }catch (JSONException e){
            testses  = null;
            Log.i("ERROR: getPositions", e.getMessage());
        }
        return testses;
    }
}

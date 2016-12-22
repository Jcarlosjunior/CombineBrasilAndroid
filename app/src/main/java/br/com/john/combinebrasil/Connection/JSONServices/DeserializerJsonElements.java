package br.com.john.combinebrasil.Connection.JSONServices;

import android.util.Log;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Login;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Services.Constants;

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

    public Tests getObjectTest(){
        Tests test = new Tests();
        try{
            JSONObject json = new JSONObject(response);
            test.setId(json.getString(Constants.ID));
            test.setName(json.getString(Constants.TEST_NAME));
            test.setType(json.getString(Constants.TEST_TYPE));
            test.setDescription(json.getString(Constants.TEST_DESCRIPTION));
        }catch (JSONException jsonExc){
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return test;
    }


    public ArrayList<Tests> getListTests(){
        ArrayList<Tests> testsArrayList = new ArrayList<Tests>();
        try{
            JSONArray jsonArray = new JSONArray(response);

            for(int i=0; i<=jsonArray.length()-1; i++){
                JSONObject json = new JSONObject(jsonArray.getString(i));
                Tests test = new Tests();
                test.setId(json.getString(Constants.ID));
                test.setName(json.getString(Constants.TEST_NAME));
                test.setType(json.getString(Constants.TEST_TYPE));
                test.setDescription(json.getString(Constants.TEST_DESCRIPTION));
                testsArrayList.add(test);
            }

        }catch (JSONException jsonExc){
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return testsArrayList;
    }

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
}
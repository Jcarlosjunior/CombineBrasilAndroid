package br.com.john.combinebrasil.Connection.JSONServices;

import android.util.Log;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    /*
    ***********************************DESERIALIZER USER********************************************
    **/
    public User getObjectsUser(){
        User user = new User();
        try{
            JSONObject json = new JSONObject(this.response);
            user.setToken(json.getString(Constants.TOKEN));
            user.setUsername(json.getString(Constants.USERNAME));
            user.setName(json.getString(Constants.NAME));

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
            test.setName(json.getString(Constants.NAME));
            test.setType(json.getString(Constants.TYPE));
            test.setDescription(json.getString(Constants.DESCRIPTION));
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
                test.setName(json.getString(Constants.NAME));
                test.setType(json.getString(Constants.TYPE));
                test.setDescription(json.getString(Constants.DESCRIPTION));
                testsArrayList.add(test);
            }

        }catch (JSONException jsonExc){
            Log.i("JSON ERROR", jsonExc.toString());
        }
        return testsArrayList;
    }
}
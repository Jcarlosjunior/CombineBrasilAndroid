package br.com.john.combinebrasil.Connection.JSONServices;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;

/**
 * Created by GTAC on 31/01/2017.
 */

public class CreateJSON {
    public static JSONObject createObject(Tests test) {
        JSONObject object = new JSONObject();

        try {
            object.put(Constants.TESTS_ATHLETE, test.getAthlete());
            object.put(Constants.TESTS_SELECTIVE, test.getSelective());
            object.put(Constants.TESTS_TYPE, test.getType());
            object.put(Constants.TESTS_FIRST_VALUE, test.getFirstValue());
            object.put(Constants.TESTS_SECOND_VALUE, test.getSecondValue());
            object.put(Constants.TESTS_RATING, test.getRating());
            object.put(Constants.TESTS_WINGSPAN, test.getWingspan());
            object.put(Constants.TESTS_USER, test.getUser());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject createObjectSelective(Selective selective) {
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.SELECTIVES_TEAM, selective.getTeam());
            object.put(Constants.SELECTIVES_TITLE, selective.getTitle());
            object.put(Constants.SELECTIVES_CITY, selective.getCity());
            object.put(Constants.SELECTIVES_NEIGHBORHOOD, selective.getNeighborhood());
            object.put(Constants.SELECTIVES_POSTALCODE, selective.getPostalCode());
            object.put(Constants.SELECTIVES_STATE, selective.getState());
            object.put(Constants.SELECTIVES_NOTE, selective.getNotes());
            object.put(Constants.SELECTIVES_ADDRESS, selective.getAddress());
            object.put(Constants.SELECTIVES_CANSYNC, selective.getCanSync());

            JSONArray jsonDates = new JSONArray();
            jsonDates.put(convertStringInDate(AllActivities.hashInfoSelective.get("date")));
            if(AllActivities.hashInfoSelective.get("dateSecond") !=null && !(AllActivities.hashInfoSelective.get("dateSecond").equals("")))
                jsonDates.put(convertStringInDate(AllActivities.hashInfoSelective.get("dateSecond")));

            if(AllActivities.hashInfoSelective.get("dateThird") !=null && !(AllActivities.hashInfoSelective.get("dateThird").equals("")))
                jsonDates.put(convertStringInDate(AllActivities.hashInfoSelective.get("dateThird")));

            object.put(Constants.SELECTIVES_DATE, jsonDates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private static String convertStringInDate(String dateStrg){
        try {
            String day = dateStrg.substring(0, 2);
            String month = dateStrg.substring(3, 5);
            String year = dateStrg.substring(6, 10);

            String hour = dateStrg.substring(13, 15);
            String minut = dateStrg.substring(16, dateStrg.length());
            return year + "-" + month + '-' + day +" "+hour+":"+minut;
        }catch (Exception e){
            return  "";
        }

    }

    public static JSONObject createObjectTestsSelectives(String selective, ArrayList<String> testTypes) {
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.TESTS_SELECTIVE, selective);
            JSONArray jsonDates = new JSONArray();

            for(String test : testTypes){
                jsonDates.put(test.toString());
            }
            object.put(Constants.TESTTYPES_SELECTIVE, jsonDates);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject createObjectUserSelectives(String selective, String user, boolean isAdmin) {
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.USER_SELECTIVE_SELECTIVE, selective);
            object.put(Constants.USER_SELECTIVE_USER, user);
            object.put(Constants.USER_SELECTIVE_IS_ADMIN, isAdmin);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject queryObjectUserSelectives(String selective, String user) {
        JSONObject object = new JSONObject();
        try {
            JSONObject jsonQuery = new JSONObject();
            jsonQuery.put(Constants.USER_SELECTIVE_SELECTIVE, selective);
            jsonQuery.put(Constants.USER_SELECTIVE_USER, user);
            object.put("query", jsonQuery);
            JSONArray jsonDates = new JSONArray();
            jsonDates.put(" ");
            object.put("populate", jsonDates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject createObjectTeam(Team team) {
        JSONObject object = new JSONObject();
        try {
            object.put("isStarTeam", false);
            object.put(Constants.TEAM_PRESIDENTNAME, team.getPresidentName());
            object.put(Constants.TEAM_NAME, team.getName());
            object.put(Constants.TEAM_MODALITY, team.getModality());
            object.put(Constants.TEAM_EMAIL, team.getEmail());
            object.put(Constants.TEAM_CITY, team.getCity());
            object.put(Constants.TEAM_ADDRESS, team.getAddres());
            object.put(Constants.TEAM_SOCIAL_LINK, team.getSocialLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

}

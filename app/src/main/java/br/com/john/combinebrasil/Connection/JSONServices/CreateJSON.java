package br.com.john.combinebrasil.Connection.JSONServices;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Services;

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
            object.put(Constants.SELECTIVES_DATE, selective.getDate());
            object.put(Constants.SELECTIVES_TITLE, selective.getTitle());
            object.put(Constants.SELECTIVES_CITY, selective.getCity());
            object.put(Constants.SELECTIVES_NEIGHBORHOOD, selective.getNeighborhood());
            object.put(Constants.SELECTIVES_POSTALCODE, selective.getPostalCode());
            object.put(Constants.SELECTIVES_STATE, selective.getState());
            object.put(Constants.SELECTIVES_NOTE, selective.getNotes());
            object.put(Constants.SELECTIVES_ADDRESS, selective.getAddress());
            object.put(Constants.SELECTIVES_CODESELECTIVE, selective.getCodeSelective());
            object.put(Constants.SELECTIVES_CANSYNC, selective.getCanSync());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject createObjectTeam(Team team) {
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.TEAM_NAME, team.getName());
            object.put(Constants.TEAM_MODALITY, team.getModality());
            object.put(Constants.TEAM_EMAIL, team.getEmail());
            object.put(Constants.TEAM_CITY, team.getCity());
            object.put(Constants.TEAM_SOCIAL_LINK, team.getSocialLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}

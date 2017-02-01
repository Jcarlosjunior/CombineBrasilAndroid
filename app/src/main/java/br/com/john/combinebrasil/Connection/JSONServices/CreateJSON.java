package br.com.john.combinebrasil.Connection.JSONServices;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.john.combinebrasil.Classes.Tests;
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
}

package br.com.john.combinebrasil.Services;

import android.os.Environment;

import java.io.File;

/**
 * Created by GTAC on 17/10/2016.
 */
public class Constants {
    public static final boolean debug = true;

    public static final String URL = "http://kc.gtacsolutions.com/";

    public static final String login = "Login/CheckCredentials";

    public static final String user = "api/Users";


    public static final String PATH_DEFAULT = new File(Environment.getExternalStorageDirectory(), File.separator+ "Android" +
            File.separator+"data"+File.separator+  AllActivities.mainActivity.getApplicationContext().getPackageName()).toString();

    public static final String PATH_DATABASE = PATH_DEFAULT+ File.separator+"database";
    public static final String NAME_DATABASE = "Combine.db";
    public static final String TESTS = "Testes";
    public static final String USER = "User";

    /****************************************NAMES ACTIVITYS**********************************/

    public static final String CRONOMETER_ACTIVITY = "CronometerActivity";
    public static final String RESULTS_ACTIVITY = "ResultsActivity";

    //SHARED PREFERENCES
    public static final String TIMER_CHRONOMETER = "timer_chronometer",
            ID = "Id",
            LOGGED="Logged",
            TOKEN="Token",
            USERNAME="Username",
            NAME="Name",
            PASSWORD = "password",
            EMAIL ="Email",
            DESCRIPTION = "Description",
            TYPE = "Type";
    /*
    **************************CHAMADAS DO VOLLEY***********************************
    **/

    public static final String CALLED_LOGIN = "calledLogin";
    public static final String CALLED_GET_TESTS = "calledGetTests";
    public static final String CALLED_POST_TESTS = "calledPostTests";
    public static final String CALLED_GET_PLAYERS= "calledGetPlayers";
    public static final String CALLED_POST_PLAYERS = "calledPostPlayers";

    /*
    ***************************DATABASES********************************************
    */

    /*****************************PLAYERS TABLE**********************************/
    public static final String TABLE_PLAYERS = "Players";
    public static final String PLAYER_ID = "Id";
    public static final String PLAYER_NAME = "Name";
    public static final String PLAYER_AGE = "Age";
    public static final String PLAYER_ID_SELECTIVE = "IdSelective";
    public static final String PLAYER_DETAILS = "Details";

    /*****************************RESULTS TABLE**********************************/
    public static final String TABLE_RESULTS = "Result";
    public static final String RESULT_ID = "Id";
    public static final String RESULT_ID_SELECTIVE = "IdSelective";
    public static final String RESULT_ID_TEST = "IdTest";
    public static final String RESULT_ID_PLAYER = "IdPlayer";
    public static final String RESULT_STATUS = "Status";
    public static final String RESULT_FIRST_VALUE = "FirstValue";
    public static final String RESULT_SECOND_VALUE = "SecondValue";


    /*****************************TESTS TABLE**********************************/
    public static final String TABLE_TEST = "Tests";
    public static final String TEST_ID = "Id";
    public static final String TEST_NAME = "Name";
    public static final String TEST_TYPE = "Type";
    public static final String TEST_DESCRIPTION = "Description";
    public static final String TEST_ID_USER = "IdUser";
    public static final String TEST_ID_SELECTIVE = "IdSelective";
    public static final String TEST_CODE = "Code";

    /*****************************TYPES TABLE**********************************/
    public static final String TABLE_TYPE = "type";
    public static final String  TYPE_ID = "Id";
    public static final String  TYPE_NAME = "Name";
    public static final String  TYPE_DESCRIPTION = "Description";

    /*******************************USER TABLE ***********************************/
    public static final String TABLE_USER = "User";
    public static final String USER_ID = "Id";
    public static final String USER_NAME = "Name";
    public static final String USER_USERNAME = "Username";
    public static final String USER_PASSWORD = "Password";
    public static final String USER_EMAIL = "Email";
    public static final String USER_TOKEN = "Token";
}

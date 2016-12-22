package br.com.john.combinebrasil.Services;

import android.os.Environment;

import java.io.File;

/**
 * Created by GTAC on 17/10/2016.
 */
public class Constants {
    public static final boolean debug = true;

    public static final String URL = "https://combine-api.herokuapp.com/";

    public static final String login = "api/login";

    public static final String LOGIN_EMAIL = "email";
    public static final String LOGIN_ISADMIN = "isAdmin";
    public static final String LOGIN_CANWRITE = "canWrite";



    public static final String API_ATHLETES = "api/athletes";
    public static final String API_POSITIONS = "api/positions";
    public static final String API_TEAMS = "api/teams";
    public static final String API_TESTTYPES = "api/testTypes";
    public static final String API_SELECTIVEATHLETES = "api/selectiveAthletes";
    public static final String API_SELECTIVES = "api/selectives";
    public static final String API_TEAMUSERS = "api/teamUsers";
    public static final String API_TESTS = "api/tests";
    public static final String API_USERS = "api/Users";


    public static final String PATH_DEFAULT = new File(Environment.getExternalStorageDirectory(), File.separator+ "Android" +
            File.separator+"data"+File.separator+  AllActivities.mainActivity.getApplicationContext().getPackageName()).toString();

    public static final String PATH_DATABASE = PATH_DEFAULT+ File.separator+"database";
    public static final String NAME_DATABASE = "Combine.db";
    public static final String TESTS = "Testes";
    public static final String USER = "User";

    /****************************************NAMES ACTIVITYS**********************************/

    public static final String CRONOMETER_ACTIVITY = "CronometerActivity";
    public static final String RESULTS_ACTIVITY = "ResultsActivity";
    public static final String MAIN_ACTIVITY = "MainActivity";
    public static final String LOGIN_ACTIVITY = "LoginActivity";

    //SHARED PREFERENCES
    public static final String TIMER_CHRONOMETER = "timer_chronometer",
            ID = "Id",
            LOGGED="Logged";
    /*
    **************************CHAMADAS DO VOLLEY***********************************
    **/

    public static final String CALLED_LOGIN = "calledLogin";
    public static final String CALLED_GET_TESTS = "calledGetTests";
    public static final String CALLED_POST_TESTS = "calledPostTests";
    public static final String CALLED_GET_ATHLETES= "calledGetAthletes";
    public static final String CALLED_POST_ATHLETES = "calledPostAthletes";

    /*
    ***************************DATABASES********************************************
    */

    /*****************************PLAYERS TABLE**********************************/
    public static final String TABLE_ATHLETES = "Athletes";
    public static final String ATHLETES_ID = "_id";
    public static final String ATHLETES_NAME = "name";
    public static final String ATHLETES_BIRTHDAY = "birthday";
    public static final String ATHLETES_CPF = "cpf";
    public static final String ATHLETES_HEIGHT = "height";
    public static final String ATHLETES_WEIGHT = "weight";
    public static final String ATHLETES_CREATEDAT = "createdAt";
    public static final String ATHLETES_UPDATEAT = "updatedAt";

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
    public static final String  TABLE_TYPE = "type";
    public static final String  TYPE_ID = "Id";
    public static final String  TYPE_NAME = "Name";
    public static final String  TYPE_DESCRIPTION = "Description";

    /*******************************USER TABLE ***********************************/
    public static final String TABLE_USER = "User";
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "name";
    public static final String USER_PASSWORD = "password";
    public static final String USER_EMAIL = "email";
    public static final String USER_SALT = "salt";
    public static final String USER_ISADMIN = "isAdmin";
    public static final String USER_CANWRITE = "canWrite";
    public static final String USER_TOKEN = "token";

    /********************************POSITIONS TABLE ******************************/

    public static final String TABLE_POSITIONS = "Positions";
    public static final String POSITIONS_ID = "_id";
    public static final String POSITIONS_NAME = "name";
    public static final String POSITIONS_DESCRIPTIONS = "description";

    /***********************************SELECTIVES ATHLETES TABLE ******************/
    public static final String TABLE_SELECTIVEATHLETES = "SelectiveAthletes";
    public static final String SELECTIVEATHLETES_ID = "_id";
    public static final String SELECTIVEATHLETES_ATHLETE = "athlete";
    public static final String SELECTIVEATHLETES_SELECTIVE = "selective";
    public static final String SELECTIVEATHLETES_INSCRIPTIONNUMBER = "inscriptionNumber";
    public static final String SELECTIVEATHLETES_PRESENCE = "presence";

    /****************************** TEAM TABLE***************************************/

    public static final String TABLE_TEAM= "Team";
    public static final String TEAM_ID = "_id";
    public static final String TEAM_TYPE = "Type";
    public static final String TEAM_ATHLETE = "Athlete";
    public static final String TEAM_VALUE = "Value";
    public static final String TEAM_RATING = "Rating";

    /****************************** TEAM USERS TABLE***************************************/

    public static final String TABLE_TEAMUSERS = "TeamUsers";
    public static final String TEAMUSERS_ID = "_id";
    public static final String TEAMUSERS_USER = "User";
    public static final String TEAMUSERS_TEAM = "Team";

    /****************************** TEAM TYPES TABLE***************************************/

    public static final String TABLE_TESTTYPES = "TestTypes";
    public static final String TESTTYPES_ID = "_id";
    public static final String TESTTYPES_NAME = "Name";
    public static final String TESTTYPES_DESCRIPTION = "Description";
    public static final String TESTTYPES_ATTEMPTSLIMIT = "attemptsLimit";
    public static final String TESTTYPES_VISIBLETOREPORT = "visibleToReport";
}

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
    public static final String areas = "api/Areas";
    public static final String categories = "api/Categories";
    public static final String equipaments = "api/Equipaments";
    public static final String equipamentSystens = "api/EquipmentSystem";
    public static final String factories = "api/Factories";
    public static final String groups = "api/Groups";
    public static final String machines = "api/Machines";
    public static final String parameters = "api/Parameters";
    public static final String process = "api/Process";
    public static final String routeActivityDatas = "api/RouteActivityDatas";
    public static final String routesxTechnicians = "api/RoutesxTechnicians";
    public static final String routes = "api/Routes";
    public static final String routeXMachineActivities = "api/RouteXMachineActivities";
    public static final String userRoutes = "api/UserRoutes";


    public static final String PATH_DEFAULT = new File(Environment.getExternalStorageDirectory(), File.separator+ "Android" +
            File.separator+"data"+File.separator+  AllActivities.mainActivity.getApplicationContext().getPackageName()).toString();

    public static final String PATH_DATABASE = PATH_DEFAULT+ File.separator+"database";
    public static final String NAME_DATABASE = "CombineBrasil";
    public static final String TESTS = "Testes";
    public static final String USER = "User";

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
}

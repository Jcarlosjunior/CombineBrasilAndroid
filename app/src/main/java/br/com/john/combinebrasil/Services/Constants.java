package br.com.john.combinebrasil.Services;

import android.os.Environment;

import java.io.File;

/**
 * Created by GTAC on 17/10/2016.
 */
public class Constants {
    public static final boolean debug = false;

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
    public static final String NAME_DATABASE = "KimberlyClark";
    public static final String TABLES_ROUTES = "Routes";
    public static final String ID = "Id";
    public static final String CODE = "Code";
    public static final String DATE = "Date";
    public static final String PROCESS = "Process";
    public static final String SYSTEM = "System";
    public static final String MACHINE = "Machine";
    public static final String GROUP = "Group";
    public static final String STATUS = "Status";
    public static final String USER = "User";

    //SHARED PREFERENCES
    public static final String TIMER_CHRONOMETER = "timer_chronometer",
            LOGGED="Logged",
            TOKEN="Token",
            USERNAME="Username",
            NAME="Name",
            EMAIL ="Email";

    //API CONSTANTS
    public static final String CATEGORY = "Category";
    public static final String CATEGORY_NAME = "CategoryName";
    public static final String AREA = "Area";
    public static final String AREA_NAME = "AreaName";
    public static final String FACTORY = "Factory";
    public static final String FACTORY_NAME = "FactoryName";
    public static final String OBSERVATION = "Observation";
    public static final String PROCESS_NAME = "ProcessName";
    public static final String ROUTE = "Route";
    public static final String ROUTE_NAME = "RouteName";
    public static final String TIME_STAMP = "TimeStamp";
    public static final String TURN = "Turn";
}

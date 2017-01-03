package br.com.john.combinebrasil.Services;

import android.app.Activity;
import android.database.SQLException;
import android.util.Log;
import android.view.View;

import junit.framework.Test;

import java.io.IOException;
import java.util.ArrayList;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Classes.TeamUsers;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.MainActivity;

/**
 * Created by GTAC on 14/11/2016.
 */

public class SyncDatabase {

    private static Activity activity;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        SyncDatabase.activity = activity;
    }

    public SyncDatabase(){}

    public SyncDatabase(Activity activity) throws IOException {
        this.activity = activity;
        //initSyncDatabase();
    }

    public void initSyncDatabase() throws IOException {
        if (Services.isOnline(this.activity)) {
             MainActivity.textProgress.setText("Sincronizando atletas");
            callFunc(Constants.URL + Constants.API_ATHLETES, Constants.CALLED_GET_ATHLETES, false);
        }
    }

    private static  void callFunc(String url, String methodName, boolean isPost) {
        int methodType = isPost ? 1 : 0;
        Log.i("Sync DataBase", methodName + "\n\n "+url);
        Connection task = new Connection(url, methodType, methodName, false, activity);
        task.callByJsonStringRequest();
    }

    public static void hideProgress(String nameActivity){
        if(nameActivity.toString().equals(Constants.MAIN_ACTIVITY))
            MainActivity.linearProgress.setVisibility(View.GONE);
        else if(nameActivity.toString().equals(Constants.LOGIN_ACTIVITY))
            LoginActivity.linearProgress.setVisibility(View.GONE);
    }

    public static void athletesResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<Athletes> athletesList = des.getAthletes();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addAthletes(athletesList);
            db.close();
            //hideProgress(activity.getClass().getSimpleName());
            callFunc(Constants.URL + Constants.API_POSITIONS, Constants.CALLED_GET_POSITIONS,  false);
            MainActivity.textProgress.setText("Sincronizando Posições");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void positionsResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<Positions> positions = des.getPositions();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addPositions(positions);
            db.close();
            //hideProgress(activity.getClass().getSimpleName());
            callFunc(Constants.URL + Constants.API_SELECTIVEATHLETES, Constants.CALLED_GET_SELECTIVEATHLETES,  false);
            MainActivity.textProgress.setText("Sincronizando seletiva");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void selectiveAthletesResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<SelectiveAthletes> selectiveAthletes = des.getSelectiveAthletes();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addSelectivesAthletes(selectiveAthletes);
            db.close();
            //hideProgress(activity.getClass().getSimpleName());
            callFunc(Constants.URL + Constants.API_SELECTIVES, Constants.CALLED_GET_SELECTIVE,  false);
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void selectiveResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<Selective> selectives = des.getSelective();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addSelectives(selectives);
            db.close();
            callFunc(Constants.URL + Constants.API_TEAMUSERS, Constants.CALLED_GET_TEAMUSERS,  false);
            MainActivity.textProgress.setText("Sincronizando avaliadores");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void teamUsersResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<TeamUsers> teamUserses = des.getTeamUsers();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addTeamUsers(teamUserses);
            db.close();
            callFunc(Constants.URL + Constants.API_TEAMS, Constants.CALLED_GET_TEAM,  false);
            MainActivity.textProgress.setText("Sincronizando equipe");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void teamResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<Team> teams = des.getTeam();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addTeam(teams);
            db.close();
            callFunc(Constants.URL + Constants.API_TESTTYPES, Constants.CALLED_GET_TESTTYPES,  false);
            MainActivity.textProgress.setText("Sincronizando testes");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void testTypesResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<TestTypes> testTypes = des.getTestTypes();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addTestTypes(testTypes);
            db.close();
            callFunc(Constants.URL + Constants.API_TESTS, Constants.CALLED_GET_TESTS,  false);
            MainActivity.textProgress.setText("Sincronizando testes");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void testResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<Tests> test = des.getTest();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addTests(test);
            db.close();
            MainActivity.finishSync(activity);
            //hideProgress(activity.getClass().getSimpleName());
           // callFunc(Constants.URL + Constants.API_TESTS, Constants.CALLED_GET_TESTS,  false);
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }
}

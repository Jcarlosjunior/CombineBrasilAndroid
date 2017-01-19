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
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.SyncAthleteActivity;

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

    public static  void callFunc(String url, String methodName, boolean isPost, Activity act) {
        int methodType = isPost ? 1 : 0;
        Log.i("Sync DataBase", methodName + "\n\n "+url);
        Connection task = new Connection(url, methodType, methodName, false, act);
        task.callByJsonStringRequest();
    }

    public static void hideProgress(String nameActivity){
        if(nameActivity.toString().equals(Constants.MAIN_ACTIVITY))
            MainActivity.linearProgress.setVisibility(View.GONE);
        else if(nameActivity.toString().equals(Constants.LOGIN_ACTIVITY))
            LoginActivity.linearProgress.setVisibility(View.GONE);

    }

    public void initSyncDatabase() throws IOException {
        if (Services.isOnline(this.activity)) {
            MainActivity.textProgress.setText("Verificando seu Usuário");
            callFunc(Constants.URL + Constants.API_USERS+"?"+Constants.USER_EMAIL+"="+
                            SharedPreferencesAdapter.getValueStringSharedPreferences(activity,Constants.LOGIN_EMAIL),
                    Constants.CALLED_GET_USER,  false, activity);
        }
    }

    /**********************************************USER********************************************/
    public static void userResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        User user = des.getUsers();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addUser(user);
            db.close();
            MainActivity.textProgress.setText("Sincronizando Equipe");
            String url = Constants.URL + Constants.API_TEAMUSERS+"?"+Constants.TEAMUSERS_USER+"="+user.getId();
            callFunc(url, Constants.CALLED_GET_TEAMUSERS,  false, activity);

        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /*****************************************TEAM USER********************************************/
    public static void teamUsersResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        //ArrayList<TeamUsers> teamUserses = des.getTeamUsers();
        TeamUsers teamUser = des.getTeamUser();
        DatabaseHelper db = new DatabaseHelper(activity);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(activity, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addTeamUser(teamUser);
            db.close();
            MainActivity.textProgress.setText("Sincronizando Seletiva");
            String url = Constants.URL + Constants.API_SELECTIVES+"?"+Constants.SELECTIVES_TEAM+"="+ teamUser.getTeam();
            callFunc(url, Constants.CALLED_GET_SELECTIVE,  false, activity);

        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /******************************************SELECTIVE*******************************************/
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
            callFunc(Constants.URL + Constants.API_TEAMS, Constants.CALLED_GET_TEAM,  false, activity);
            MainActivity.textProgress.setText("Sincronizando avaliadores");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /**********************************************TEAM********************************************/
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
            ArrayList<Team> teamsAdd = new ArrayList<Team>();
            Selective item = new Selective();
            for(Team team : teams){
                item = db.getSelectiveFromTeam(team.getId());
                if(item!=null){
                    teamsAdd.add(team);
                    break;
                }
            }
            db.addTeam(teamsAdd);
            db.close();
            MainActivity.textProgress.setText("Sincronizando testes");
            String url = Constants.URL + Constants.API_SELECTIVEATHLETES+"?"+Constants.SELECTIVEATHLETES_SELECTIVE+"="+item.getId();
            callFunc(url,
                    Constants.CALLED_GET_SELECTIVEATHLETES,  false, activity);
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /*************************************SELECTIVE ATHLETES***************************************/
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
            callFunc(Constants.URL + Constants.API_ATHLETES, Constants.CALLED_GET_ATHLETES, false, activity);
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /*******************************************ATHLETES*******************************************/
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
            ArrayList<Athletes> athletesAdd = new ArrayList<Athletes>();
            for(int i=0; i<=athletesList.size()-1; i++){
                SelectiveAthletes item = db.getSelectiveAthletesFromAthlete(athletesList.get(i).getId());
                if(item!=null){
                    athletesList.get(i).setCode(item.getInscriptionNumber());
                    athletesAdd.add(athletesList.get(i));
                }

            }
            db.openDataBase();
            db.addAthletes(athletesAdd);
            db.close();
            callFunc(Constants.URL + Constants.API_POSITIONS, Constants.CALLED_GET_POSITIONS,  false, activity);
            MainActivity.textProgress.setText("Sincronizando Posições");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /******************************************POSITIONS*******************************************/
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
            callFunc(Constants.URL + Constants.API_TESTTYPES, Constants.CALLED_GET_TESTTYPES,  false, activity);
            MainActivity.textProgress.setText("Sincronizando seletiva");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /*****************************************TEST TYPES********************************************/
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
            callFunc(Constants.URL + Constants.API_TESTS, Constants.CALLED_GET_TESTS,  false, activity);
            MainActivity.textProgress.setText("Sincronizando testes");
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }

    /**********************************************TEST********************************************/
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
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }


    /***********************************************************************************************/
}

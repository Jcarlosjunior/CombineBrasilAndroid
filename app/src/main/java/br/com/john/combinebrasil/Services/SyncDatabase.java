package br.com.john.combinebrasil.Services;

import android.app.Activity;
import android.database.SQLException;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.Classes.Athletes;
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

    private void initSyncDatabase() throws IOException {
        if (Services.isOnline(activity)) {
            DatabaseHelper db = new DatabaseHelper(activity);
            db.createDataBase();
            MainActivity.textProgress.setText("Sincronizando atletas");
            callFunc(Constants.URL + Constants.API_ATHLETES, Constants.CALLED_GET_ATHLETES, false);
        }
    }

    private static  void callFunc(String url, String methodName, boolean isPost) {
        int methodType = isPost ? 1 : 0;
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
            hideProgress(activity.getClass().getSimpleName());
            //callFunc(Constants.URL + Constants.API_POSITIONS, "getPositions",  false);
        } catch (SQLException sqle) {
            Services.messageAlert(activity, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(activity.getClass().getSimpleName());
            throw sqle;
        }
    }
}

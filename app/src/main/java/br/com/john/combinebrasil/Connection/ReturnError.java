package br.com.john.combinebrasil.Connection;

/**
 * Created by GTAC on 18/10/2016.
 */
import android.app.Activity;
import android.util.Log;
import android.view.View;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.ChooseTeamSelectiveActivity;
import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.CreateSelectiveActivity;
import br.com.john.combinebrasil.CreateTeamActivity;
import br.com.john.combinebrasil.EnterSelectiveActivity;
import br.com.john.combinebrasil.HistoricSelectiveActivity;
import br.com.john.combinebrasil.LocalSelectiveActivity;
import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.MenuActivity;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.SyncDatabase;
import br.com.john.combinebrasil.SyncAthleteActivity;
import br.com.john.combinebrasil.TestSelectiveActivity;

public class ReturnError {
    public String message;

    private static ReturnError ourInstance = new ReturnError();

    public static ReturnError getInstance() {
        return ourInstance;
    }

    private ReturnError() {
    }

    public void goTo(String whoCalled, Activity activity, String message, int statusError) {
            if(whoCalled.equals(Constants.CALLED_POST_ATHLETES)){
                //CreateAccountAthlete.returnPostAthlete(activity, message, statusError);
            }
            else if(whoCalled.equals(Constants.CALLED_LOGIN)){
                LoginActivity.afterLogin(message, activity, statusError);
            }

            else if (whoCalled.equals(Constants.CALLED_GET_SELECTIVE)){
                if(activity.getClass().getSimpleName().equals("EnterSelectiveActivity"))
                    EnterSelectiveActivity.returnGetAllSelectives(activity, message, statusError);
                else if(activity.getClass().getSimpleName().equals("EnterSelectiveActivity"))
                    HistoricSelectiveActivity.returnGetAllSelectives(activity, message, statusError);
                else if(activity.getClass().getSimpleName().equals("MenuActivity"))
                    MenuActivity.returnGetSelectiveByCode(activity, message, statusError);
            }

            else if(whoCalled.equals("UPDATE_SELECTIVEATHLETE")) {
                if (activity.getClass().getSimpleName().equals("SyncAthleteActivity"))
                    SyncAthleteActivity.updateSelectiveAthlete(activity, "FAIL", message);
                //else if (activity.getClass().getSimpleName().equals(Constants.MAIN_ACTIVITY))
                    //MainActivity.updateSelectiveAthlete(activity, message);
            }

            else if(whoCalled.equals("UPDATE_ATHLETE")) {
                if (activity.getClass().getSimpleName().equals("SyncAthleteActivity"))
                    SyncAthleteActivity.updateAthlete(activity, message);
                //else if (activity.getClass().getSimpleName().equals(Constants.MAIN_ACTIVITY))
                    //MainActivity.updateAthlete(activity, message);
            }

            else if(whoCalled.equals("UpdateSelectiveAthletes")){
                if(activity.getClass().getSimpleName().equals("MainActivity"))
                    MainActivity.returnUpdateSelectiveAthletes(activity, message, statusError);
                if(activity.getClass().getSimpleName().equals("AthletesActivity"))
                    AthletesActivity.returnUpdateSelectiveAthletes(activity, message, statusError);
            }

            else if(whoCalled.equals("UpdateAthletes")){
                if(activity.getClass().getSimpleName().equals("MainActivity"))
                    MainActivity.returnUpdateAthletes(activity, message, statusError);
                if(activity.getClass().getSimpleName().equals("AthletesActivity"))
                    AthletesActivity.returnUpdateAthletes(activity, message, statusError);
            }

            else if (whoCalled.equals(Constants.CALLED_GET_TESTTYPES)) {
                if (activity.getClass().getSimpleName().equals("TestSelectiveActivity"))
                    TestSelectiveActivity.returnUpdateTests(activity, statusError, message);
                else if(activity.getClass().getSimpleName().equals("MainActivity"))
                    MainActivity.testResponse(activity, message, statusError);
            }
            else if(whoCalled.equals(Constants.CALLED_GET_CEP))
                if(activity.getClass().getSimpleName().equals("LocalSelectiveActivity"))
                    LocalSelectiveActivity.returnCEP(activity, message, statusError);
                else if(activity.getClass().getSimpleName().equals("CreateTeamActivity"))
                    CreateTeamActivity.returnCEP(activity, message, statusError);

            else if (whoCalled.equals(Constants.CALLED_GET_TEAM)) {
                if(activity.getClass().getSimpleName().equals("ChooseTeamSelectiveActivity"))
                    ChooseTeamSelectiveActivity.returnGetAllTeams(activity, message, statusError);
            }
    }
}
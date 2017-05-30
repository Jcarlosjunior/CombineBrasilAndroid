package br.com.john.combinebrasil.Connection;

/**
 * Created by GTAC on 18/10/2016.
 */

import android.app.Activity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.ChooseTeamSelectiveActivity;
import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.CreateSelectiveActivity;
import br.com.john.combinebrasil.CronometerOnlyOneActivity;
import br.com.john.combinebrasil.EnterSelectiveActivity;
import br.com.john.combinebrasil.HistoricSelectiveActivity;
import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.ResultsOnlyOneActivity;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.SyncDatabase;
import br.com.john.combinebrasil.SyncActivity;
import br.com.john.combinebrasil.SyncAthleteActivity;
import br.com.john.combinebrasil.TestSelectiveActivity;


public class ReturnResponse {
    public static String login;
    public static String password;

    private static ReturnResponse ourInstance = new ReturnResponse();

    public static ReturnResponse getInstance() {
        return ourInstance;
    }

    private ReturnResponse() {
    }

    //esse método é de retorno quando à sucesso na requisição
    //redireciona de volta para quem chamou
    //whocalled é importante para identificar quem  fez a requisição e para onde os dados irão
    public void goTo(String whoCalled, String response, boolean isList, Activity activity, int statuCode) {
        try {
            if (!response.equals(null) || response.length() <= 0) {
                if(whoCalled.equals("updateAthleteAccount")) {
                    CreateAccountAthlete.returnAccountAthlete(activity, response);
                }

                if(whoCalled.equals("UPDATE_TEST")) {
                    if(activity.getClass().getSimpleName().equals("SyncAthleteActivity"))
                        SyncAthleteActivity.returnUpdateSync(activity, response);
                    else if(activity.getClass().getSimpleName().equals("CronometerOnlyOneActivity"))
                        CronometerOnlyOneActivity.returnUpdateSync(activity, response);
                    else if(activity.getClass().getSimpleName().equals("ResultsOnlyOneActivity"))
                        ResultsOnlyOneActivity.returnUpdateSync(activity, response);
                }

                if (whoCalled.equals(Constants.CALLED_LOGIN)) {
                    LoginActivity.afterLogin(response, activity, statuCode);
                }
                else if (whoCalled.equals(Constants.CALLED_GET_USER)) {
                    SyncDatabase.userResponse(response);
                }
                else if (whoCalled.equals(Constants.CALLED_GET_TEAMUSERS)) {
                    SyncDatabase.teamUsersResponse(response);
                }
                else if (whoCalled.equals(Constants.CALLED_GET_SELECTIVE)){
                    if(activity.getClass().getSimpleName().equals("EnterSelectiveActivity"))
                        EnterSelectiveActivity.returnGetAllSelectives(activity, response, statuCode);
                    else if(activity.getClass().getSimpleName().equals("HistoricSelectiveActivity"))
                        HistoricSelectiveActivity.returnGetAllSelectives(activity, response, statuCode);
                    else {
                        SyncDatabase.selectiveResponse(response);
                    }
                }
                else if (whoCalled.equals(Constants.CALLED_GET_TEAM)) {
                    if(activity.getClass().getSimpleName().equals("ChooseTeamSelectiveActivity"))
                        ChooseTeamSelectiveActivity.returnGetAllTeams(activity, response, statuCode);
                    else
                        SyncDatabase.teamResponse(response);
                }
                else if (whoCalled.equals(Constants.CALLED_GET_SELECTIVEATHLETES)) {
                    SyncDatabase.selectiveAthletesResponse(response);
                }
                else if (whoCalled.equals(Constants.CALLED_GET_ATHLETES)) {
                    if (activity.getClass().getSimpleName().equals("SyncAthleteActivity"))
                        SyncAthleteActivity.athletesResponse(activity, response);
                    else
                        SyncDatabase.athletesResponse(response);
                }
                else if (whoCalled.equals(Constants.CALLED_GET_POSITIONS))
                    SyncDatabase.positionsResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_TESTTYPES)) {
                    if(activity.getClass().getSimpleName().equals("TestSelectiveActivity"))
                        TestSelectiveActivity.returnUpdateTests(activity, statuCode, response);
                    else if (activity.getClass().getSimpleName().equals("SyncAthleteActivity"))
                        SyncAthleteActivity.testResponse(activity, response);
                    else
                    SyncDatabase.testTypesResponse(response);
                }
                else if (whoCalled.equals(Constants.CALLED_GET_TESTS)){
                        SyncDatabase.testResponse(response);
                }
                else if(whoCalled.equals("UPDATE_SELECTIVEATHLETE")) {
                    if (activity.getClass().getSimpleName().equals("SyncAthleteActivity"))
                        SyncAthleteActivity.updateSelectiveAthlete(activity, "OK", response);
                    else if (activity.getClass().getSimpleName().equals(Constants.MAIN_ACTIVITY))
                        MainActivity.updateSelectiveAthlete(activity, response);
                    else if (activity.getClass().getSimpleName().equals(Constants.SYNC_ACTIVITY))
                        SyncActivity.updateSelectiveAthlete(activity, response);
                }

                else if(whoCalled.equals("UPDATE_ATHLETE")) {
                        if (activity.getClass().getSimpleName().equals("SyncAthleteActivity"))
                            SyncAthleteActivity.updateAthlete(activity, response);
                        else if (activity.getClass().getSimpleName().equals(Constants.MAIN_ACTIVITY))
                            MainActivity.updateAthlete(activity, response);
                        else if (activity.getClass().getSimpleName().equals(Constants.SYNC_ACTIVITY))
                            SyncActivity.updateAthlete(activity, response);
                    }
                else if(whoCalled.equals("UpdateSelectiveAthletes")){
                    if(activity.getClass().getSimpleName().equals("MainActivity"))
                        MainActivity.returnUpdateSelectiveAthletes(activity, response, statuCode);
                    else if(activity.getClass().getSimpleName().equals("AthletesActivity"))
                        AthletesActivity.returnUpdateSelectiveAthletes(activity, response, statuCode);
                }
                else if(whoCalled.equals("UpdateSelectiveAthletes")){
                    if(activity.getClass().getSimpleName().equals("MainActivity"))
                        MainActivity.returnUpdateSelectiveAthletes(activity, response, statuCode);
                    else if(activity.getClass().getSimpleName().equals("AthletesActivity"))
                        AthletesActivity.returnUpdateSelectiveAthletes(activity, response, statuCode);
                }
                else if(whoCalled.equals("UpdateAthletes")){
                    if(activity.getClass().getSimpleName().equals("MainActivity"))
                        MainActivity.returnUpdateAthletes(activity, response, statuCode);
                    else if(activity.getClass().getSimpleName().equals("AthletesActivity"))
                        AthletesActivity.returnUpdateAthletes(activity, response, statuCode);
                }

                else if(whoCalled.equals(Constants.CALLED_GET_CEP))
                    CreateSelectiveActivity.returnCEP(activity, response, statuCode);
            }
        } catch (Exception e) {
            Log.i("ERRO", e.toString());
        }
    }
}


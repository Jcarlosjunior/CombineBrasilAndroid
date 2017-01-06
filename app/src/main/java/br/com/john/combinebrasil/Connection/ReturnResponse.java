package br.com.john.combinebrasil.Connection;

/**
 * Created by GTAC on 18/10/2016.
 */

import android.app.Activity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.SyncDatabase;


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
                if (whoCalled.equals(Constants.CALLED_LOGIN))
                    LoginActivity.afterLogin(response, isList, activity, statuCode);
                else if (whoCalled.equals(Constants.CALLED_GET_USER))
                    SyncDatabase.userResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_TEAMUSERS))
                    SyncDatabase.teamUsersResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_SELECTIVE))
                    SyncDatabase.selectiveResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_TEAM))
                    SyncDatabase.teamResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_SELECTIVEATHLETES))
                    SyncDatabase.selectiveAthletesResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_ATHLETES))
                    SyncDatabase.athletesResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_POSITIONS))
                    SyncDatabase.positionsResponse(response);

                else if (whoCalled.equals(Constants.CALLED_GET_TESTTYPES))
                    SyncDatabase.testTypesResponse(response);
                else if (whoCalled.equals(Constants.CALLED_GET_TESTS))
                    SyncDatabase.testResponse(response);

            }
        } catch (Exception e) {
            Log.i("ERRO", e.toString());
        }
    }
}


package br.com.john.combinebrasil.Connection;

/**
 * Created by GTAC on 18/10/2016.
 */

import android.app.Activity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;


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
    public void goTo(String whoCalled, String response, boolean isList, Activity activity) {
        try {
            if (!response.equals(null) || response.length() <= 0) {
                if (whoCalled.equals("calledLogin")) {
                    //LoginActivity.afterLogin(response, isList, activity);

                } else if (whoCalled.equals("calledMachines")) {
                    //MainActivity.afterCalled(response, isList, activity);

                }else if (whoCalled.equals("")) {

                }
            }
        } catch (Exception e) {
            Log.i("ERRO", e.toString());
        }
    }
}

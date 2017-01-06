package br.com.john.combinebrasil.Connection;

/**
 * Created by GTAC on 18/10/2016.
 */
import android.app.Activity;
import android.util.Log;
import android.view.View;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.Services.Constants;

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
                LoginActivity.afterLogin(message, false, activity, statusError);
            }
            //esse método é de retorno caso tenha dado certo a requisição
            //redireciona de volta para quem chamou
            //whocalled é importante para identificar quem  fez a requisição
    }
}
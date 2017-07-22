package br.com.john.combinebrasil.Connection.Posts;

import br.com.john.combinebrasil.HistoricPlayersSelectiveActivity;
import br.com.john.combinebrasil.HistoricSelectiveActivity;
import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.MenuActivity;
import br.com.john.combinebrasil.Services.Constants;

/**
 * Created by GTAC on 20/07/2017.
 */

public class PostAsyncTask extends PostBase{
    @Override
    protected void onPostExecute(String status) {
        if (whoCalled.equals(Constants.CALLED_GET_ATHLETES)) {
            if (activity.getClass().getSimpleName().equals("HistoricPlayersSelectiveActivity"))
                HistoricPlayersSelectiveActivity.returnGetPlayers(activity, result, statusCode);
        }
        else if(whoCalled.equals(Constants.CALLED_GET_USER_SELECTIVE))
            HistoricSelectiveActivity.returnGetAllSelectives(activity, result, statusCode);
        else
            LoginActivity.afterLogin(result, activity, statusCode);
    }

}

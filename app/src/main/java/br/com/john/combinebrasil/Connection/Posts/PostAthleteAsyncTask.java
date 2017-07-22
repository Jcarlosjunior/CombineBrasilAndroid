package br.com.john.combinebrasil.Connection.Posts;

/**
 * Created by GTAC on 17/11/2016.
 */


import br.com.john.combinebrasil.CreateAccountAthleteActivity;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.SyncActivity;

public class PostAthleteAsyncTask extends PostBase {

    boolean isPlay;

    @Override
    protected void onPostExecute(String status) {
        if(activity.getClass().getSimpleName().equals(Constants.SYNC_ACTIVITY)){
            if (isPlay)
                SyncActivity.afterSendAthlete(activity, resp, result);
            else
                SyncActivity.afterSendSelectiveAthlete(activity, resp, result);
        }else {
            if (isPlay)
                CreateAccountAthleteActivity.afterSendAthlete(activity, resp, result);
            else
                CreateAccountAthleteActivity.afterSendSelectiveAthlete(activity, resp, result);
        }
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
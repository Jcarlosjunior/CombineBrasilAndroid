package br.com.john.combinebrasil.Connection.Posts;

/**
 * Created by GTAC on 06/01/2017.
 */


import br.com.john.combinebrasil.CronometerOnlyOneActivity;
import br.com.john.combinebrasil.ResultsOnlyOneActivity;
import br.com.john.combinebrasil.SyncActivity;
import br.com.john.combinebrasil.SyncAthleteActivity;

public class PostSync extends PostBase {

    boolean isAll, isSyncAcitivity=false;


    @Override
    protected void onPostExecute(String status) {
        if(activity.getClass().getSimpleName().equals("CronometerOnlyOneActivity")){
            CronometerOnlyOneActivity.returnPostTest(activity, resp, result);
        }
        else if(activity.getClass().getSimpleName().equals("ResultsOnlyOneActivity")){
            ResultsOnlyOneActivity.returnPostTest(activity, resp, result);
        }
        else {
            if (isSyncAcitivity) {
                SyncActivity.returnPostSync(activity, resp, result);
            } else {
                SyncAthleteActivity.returnPostTest(activity, resp, result);
            }
        }
    }

    public void setAll(boolean play) {
        isAll = isAll;
    }

    public void setSyncAcitivity(boolean syncAcitivity){
        this.isSyncAcitivity = syncAcitivity;
    }
}
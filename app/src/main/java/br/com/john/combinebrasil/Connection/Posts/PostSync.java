package br.com.john.combinebrasil.Connection.Posts;

/**
 * Created by GTAC on 06/01/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.CronometerOnlyOneActivity;
import br.com.john.combinebrasil.ResultsOnlyOneActivity;
import br.com.john.combinebrasil.Services.Constants;
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
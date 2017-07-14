package br.com.john.combinebrasil.Connection.Posts;

/**
 * Created by GTAC on 17/11/2016.
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
                CreateAccountAthlete.afterSendAthlete(activity, resp, result);
            else
                CreateAccountAthlete.afterSendSelectiveAthlete(activity, resp, result);
        }
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
package br.com.john.combinebrasil.Connection.Posts;

import android.app.Activity;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.RegisterActivity;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.SyncActivity;

/**
 * Created by GTAC on 17/03/2017.
 */

public class PostUsersAsyncTask extends AsyncTask<String, String, String> {
    private Activity activity;
    private JSONObject objPut;
    int statusCode=0;
    String resp = "";
    String result = "";

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
        HttpClient client;

        client = new DefaultHttpClient(httpParams);

        HttpPost post = new HttpPost(params[0]); //strings[0] == url

        post.setHeader("content-type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("authorization", Constants.AUTHENTICATION);

        StringEntity entity = null;
        try {
            entity = new StringEntity(objPut.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        post.setEntity(entity);
        HttpResponse response = null;
        try {
            response = client.execute(post);
            statusCode =response.getStatusLine().getStatusCode();
            if(statusCode==201 || statusCode ==200)
                resp="OK";
            else
                resp="FAIL";

            InputStream inputStream  = response.getEntity().getContent();

            result = convertInputStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            resp = "FAIL";
        }
        return resp;
    }

    protected void onProgressUpdate(String... progress) { }

    @Override
    protected void onPostExecute(String status) {
        RegisterActivity.afterRegisterUser(activity, resp, result);
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public void setObjPut(JSONObject objPut){
        this.objPut = objPut;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
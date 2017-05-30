package br.com.john.combinebrasil.Connection.Posts;

/**
 * Created by GTAC on 21/12/2016.
 */



import android.app.Activity;
import android.content.Context;
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

import br.com.john.combinebrasil.LoginActivity;
import br.com.john.combinebrasil.Services.Constants;

public class PostLogin extends AsyncTask<String, String, String> {

    Context context;
    String resp;
    private Activity activity;
    int status = 0;
    private JSONObject obj;
    private JSONObject objPut;
    int statusCode=0;
    String URL = "";
    int ret = 0;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
        HttpConnectionParams.setSoTimeout(httpParams, 15000);
        HttpClient client;

        client = new DefaultHttpClient(httpParams);

        HttpPost post = new HttpPost(params[0]); //strings[0] == url

        post.setHeader("content-type", "application/json");
        post.setHeader("authorization", Constants.AUTHENTICATION);

        StringEntity entity = null;
        try {
            entity = new StringEntity(getObjPut().toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        post.setEntity(entity);
        HttpResponse response = null;
        try {
            response = client.execute(post);
            statusCode =response.getStatusLine().getStatusCode();
            ret = statusCode;
            inputStream = response.getEntity().getContent();

            resp = convertInputStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            ret = 0;
        }

        return resp;
    }

    protected void onProgressUpdate(String... progress) { }

    @Override
    protected void onPostExecute(String status) {
        LoginActivity.afterLogin(resp, activity, ret);
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

    public JSONObject getObjPut(){
        return objPut;
    }
    public JSONObject getObj(){
        return obj;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private String checkIfNull(String string){
        if(string == null) {
            string = "";
        }
        return string;
    }
}
package br.com.john.combinebrasil.Connection.Posts;

/**
 * Created by GTAC on 20/03/2017.
 */
import android.app.Activity;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import br.com.john.combinebrasil.CreateSelectiveActivity;
import br.com.john.combinebrasil.InfoSelectiveCreateActivity;
import br.com.john.combinebrasil.RegisterActivity;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.TestSelectiveActivity;


/**
 * Created by GTAC on 17/03/2017.
 */

public class PostCreateSelective extends PostBase {

    @Override
    protected void onPostExecute(String status) {
        if(method == InfoSelectiveCreateActivity.METHOD_CREATE_SELECTVE)
            InfoSelectiveCreateActivity.returnCreateSelective(activity, resp, result);
        else if(method == InfoSelectiveCreateActivity.METHOD_TESTS_SELECTIVE)
            InfoSelectiveCreateActivity.returnCreateTestsSelective(activity, resp, result);
    }

}
package br.com.john.combinebrasil.Services;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by GTAC on 18/10/2016.
 */
public class Services {
    public static void message(String title, String message, Activity act) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setPositiveButton("Ok", null);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    public static boolean isOnline(Activity act) {
        ConnectivityManager cm =
                (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else{
            Toast.makeText(act,"É necessário uma conexão com a internet.", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}

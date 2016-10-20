package br.com.john.combinebrasil.Services;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 18/10/2016.
 */
public class Services {
    private static AlertDialog alerta;

    public static void message(String title, String message, Activity act) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setPositiveButton("Ok", null);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    public static void messageAlert(Activity act, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        View view = act.getLayoutInflater().inflate(R.layout.alert_message, null);
        builder.setView(view);

        alerta = builder.create();
        alerta.show();
        TextView textTitle = (TextView) view.findViewById(R.id.text_title_message);
        textTitle.setText(title);

        TextView textAlert = (TextView) view.findViewById(R.id.text_alert_message);
        textAlert.setText(message);
        Button nbutton = (Button) view.findViewById(R.id.button_ok_alert_message);
        nbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.hide();
            }
        });
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

    public static void changeColorEdit(EditText edit, String title, String mensagem, Activity act){
        edit.setBackground(act.getResources().getDrawable(R.drawable.background_edit_error));
        messageAlert(act, title, mensagem);
        //Services.message("Dados Inválidos", mensagem, this);
    }
}

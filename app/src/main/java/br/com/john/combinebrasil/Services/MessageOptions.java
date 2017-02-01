package br.com.john.combinebrasil.Services;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.CronometerActivity;
import br.com.john.combinebrasil.CronometerOnlyOneActivity;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.ResultsActivity;
import br.com.john.combinebrasil.ResultsOnlyOneActivity;
import br.com.john.combinebrasil.TimerActivity;

/**
 * Created by GTAC on 06/11/2016.
 */

public class MessageOptions {
    private static AlertDialog alerta;
    private static Activity act;
    private static String whoCalled;

    public MessageOptions (Activity act, String title, String message, String whoCalled){
        this.act = act;
        this.whoCalled = whoCalled;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        View view = act.getLayoutInflater().inflate(R.layout.alert_message_option, null);
        builder.setView(view);

        TextView textTitle = (TextView) view.findViewById(R.id.text_title_message_option);

        TextView textAlert = (TextView) view.findViewById(R.id.text_alert_message_option);

        Button negativeButton = (Button) view.findViewById(R.id.button_negative_message);
        Button positiveButton = (Button) view.findViewById(R.id.button_positive_message);
        LinearLayout linear = (LinearLayout) view.findViewById(R.id.linear_message_option);

        textAlert.setText(message);
        textTitle.setText(title);

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.hide();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.hide();
            }
        });

        positiveButton.setOnClickListener(positiveClicked);

        alerta = builder.create();
        alerta.show();
    }

    private View.OnClickListener positiveClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            methodPositive();
        }
    };

    private void methodPositive(){
        alerta.hide();
        if(act.getClass().getSimpleName().equals(Constants.CRONOMETER_ACTIVITY)){
            CronometerActivity.getMethodOutActivity(act, whoCalled);
        }
        else if(act.getClass().getSimpleName().equals(Constants.CRONOMETER_ONLY_ONE_ACTIVITY))
            CronometerOnlyOneActivity.getMethodOutActivity(act, whoCalled);

        else if(act.getClass().getSimpleName().equals(Constants.RESULTS_ACTIVITY)){
            ResultsActivity.getMethodOutResultsActivity(act, whoCalled);
        }
        else if(act.getClass().getSimpleName().equals(Constants.RESULTS_ONLY_ONE_ACTIVITY)){
            ResultsOnlyOneActivity.getMethodOutResultsActivity(act, whoCalled);
        }
        else if(act.getClass().getSimpleName().equals(Constants.MAIN_ACTIVITY)){
            MainActivity.returnMessageOptions(act, whoCalled);
        }
        else if(act.getClass().getSimpleName().equals(Constants.LOGIN_CREATEACCOUNTATHLETE)){
            CreateAccountAthlete.createAthleteOff(act);
        }
        else if(act.getClass().getSimpleName().equals(Constants.TIMER_ACTIVITY)){
            TimerActivity.returnOption(act, whoCalled);
        }
        else if(act.getClass().getSimpleName().equals("AthletesActivity"))
            AthletesActivity.returnOptions(act,whoCalled);
    }
}

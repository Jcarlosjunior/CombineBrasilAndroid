package br.com.john.combinebrasil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.john.combinebrasil.Services.CountDownTimer;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class CronometerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textFirstResult, textSecondValue, textCronometer;
    LinearLayout linearButtonPlay, linearFirstValue, linearSecondValue;
    ImageView imgIconButtonPlay, imgReset, imgPause;
    Button btnSave;

    private final CountDownTimer countDownTimer = new CountDownTimer();
    private boolean init = false, firstValueSave = false, secondValueSalve = false, isPause=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometer);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);

        linearButtonPlay = (LinearLayout) findViewById(R.id.linear_button_play);
        linearFirstValue = (LinearLayout) findViewById(R.id.linear_show_first_value);
        linearSecondValue = (LinearLayout) findViewById(R.id.linear_show_second_value);

        textFirstResult = (TextView) findViewById(R.id.text_first_value);
        textSecondValue = (TextView) findViewById(R.id.text_second_value);
        textCronometer = (TextView) findViewById(R.id.text_cronometer);

        imgIconButtonPlay = (ImageView) findViewById(R.id.image_icon_button_play);
        imgPause = (ImageView) findViewById(R.id.image_pause);
        imgReset = (ImageView) findViewById(R.id.image_reset);

        btnSave = (Button) findViewById(R.id.button_save_results);

        countDownTimer.setTextView(textCronometer);
        btnSave.setOnClickListener(clickedSave);
        imgPause.setOnClickListener(clickedPause);
        imgReset.setOnClickListener(clickedReset);
        linearButtonPlay.setOnClickListener(clickedPlayAndStop);
    }

    /*
    ************************** MÉTODOS DO CRONÔMETRO ***********************************
    */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void playCronometer(){
        init=true;
        isPause=false;
        countDownTimer.initCount();
        imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.stop));
        linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_red));
        imgPause.setVisibility(View.VISIBLE);
        imgReset.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void stopCronometer(){
        countDownTimer.stop();
        init=false;
        linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_red));
        imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
        imgPause.setVisibility(View.GONE);
        imgReset.setVisibility(View.GONE);
        imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
        linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_green));
        textCronometer.setText("00:00");

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void pauseCronometer(){
        isPause=true;
        countDownTimer.pause();
        linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_green));
        imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
        imgPause.setVisibility(View.INVISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void resetCrometer(){
        isPause = true;
        init=false;
        countDownTimer.stop();
        linearButtonPlay.setBackground(getDrawable(R.drawable.background_circle_green));
        imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
        imgPause.setVisibility(View.INVISIBLE);
        imgReset.setVisibility(View.INVISIBLE);
        textCronometer.setText("00:00");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkAndSaveRun(){
        if(firstValueSave==false){
            firstValueSave=true;
            textFirstResult.setText(textCronometer.getText());
            linearFirstValue.setVisibility(View.VISIBLE);
            stopCronometer();

        }
        else if(secondValueSalve==false){
            secondValueSalve=true;
            linearButtonPlay.setVisibility(View.GONE);
            textSecondValue.setText(textCronometer.getText());
            btnSave.setVisibility(View.VISIBLE);
            btnSave.setEnabled(true);
            stopCronometer();
        }
    }

    /*
    ********************************** MESSAGES *********************************
    */
    private void messageOption(String title, String message, String method){
        pauseCronometer();
        new MessageOptions(CronometerActivity.this, title, message, method);
    }

    public static void getMethodOutActivity(Activity act, String method){
        ((CronometerActivity)act).calledFunctions(method);
    }

    private void calledFunctions(String method){
        if(method.equals("exitCronometer")){
            try {
                stopCronometer();
            }catch(Exception e){}
            finish();
        }
        else if(method.equals("saveResult")){
            checkAndSaveRun();
        }
        else if(method.equals("resetCronometer")){
            resetCrometer();
        }
        else if(method.equals("saveAllResults")){
            Services.messageAlert(CronometerActivity.this, "Mensagem","Os resultados foram salvos!","DIALOGSAVECRONOMETER");
        }
    }

    /*
    ****************************** MÉTODOS DE CLICK ******************************************
    */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void clickePause(){
        if(!isPause){
            pauseCronometer();
        }
    }

    private View.OnClickListener clickedPlayAndStop = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(init==false){
                if(firstValueSave==false)
                    playCronometer();
                else if(secondValueSalve==false)
                    playCronometer();
            }
            else {
                if(!isPause) {
                    if (firstValueSave == false)
                        messageOption("Salvar","Deseja salvar o primeiro resultado?","saveResult");
                     else if (secondValueSalve == false)
                        messageOption("Salvar","Deseja salvar o segundo resultado?","saveResult");

                }
                else
                    playCronometer();
            }
        }
    };


    private View.OnClickListener clickedPause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickePause();
        }
    };

    private View.OnClickListener clickedReset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            messageOption("Mensagem","Deseja mesmo resetar a contagem", "resetCronometer");
        }
    };

    private View.OnClickListener clickedSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            messageOption("Salvar", "Deseja salvar os resultados?", "saveAllResults");
        }
    };

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitActivity();
        }
    };

    @Override
    public void onBackPressed(){
        exitActivity();
    }

    private void exitActivity() {
        if (init) {
            messageOption("Sair","Tem certeza que deseja sair e abandonar o teste?","exitCronometer");
        } else {
            if (countDownTimer.getPlay())
                stopCronometer();
            finish();
        }
    }

    public static void finished(Activity act){
        ((CronometerActivity)act).finish();
    }
}

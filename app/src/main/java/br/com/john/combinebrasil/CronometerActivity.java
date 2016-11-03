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


    private View.OnClickListener clickedPlayAndStop = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(init==false){
                if(firstValueSave==false){
                    countDownTimer.initCount();
                    imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.stop));
                    linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_red));
                    init=true;
                    imgPause.setVisibility(View.VISIBLE);
                    imgReset.setVisibility(View.VISIBLE);
                    isPause=false;
                }
                else if(secondValueSalve==false){
                    countDownTimer.initCount();
                    init=true;
                    imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.stop));
                    linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_red));
                    imgPause.setVisibility(View.VISIBLE);
                    imgReset.setVisibility(View.VISIBLE);
                    isPause=false;
                }
            }
            else {
                if(!isPause) {
                    if (firstValueSave == false) {
                        countDownTimer.pause();
                        message("Salvar", "Deseja salvar no primeiro resultado?", false);
                    } else if (secondValueSalve == false) {
                        countDownTimer.pause();
                        message("Salvar", "Deseja salvar no primeiro resultado?", false);
                    }
                }
                else{
                    countDownTimer.initCount();
                    imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.stop));
                    linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_red));
                    imgPause.setVisibility(View.VISIBLE);
                    isPause=false;
                }
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void clickePause(){
        if(!isPause){
            isPause=true;
            countDownTimer.pause();
            linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_green));
            imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
            imgPause.setVisibility(View.INVISIBLE);
            imgPause.setEnabled(false);
        }
        else{
            isPause=false;
            countDownTimer.initCount();
            linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_red));
            imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.stop));
            imgPause.setVisibility(View.VISIBLE);
            imgPause.setEnabled(true);
        }
    }

    private View.OnClickListener clickedPause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("Log","clickedPause");
            clickePause();
        }
    };

    private View.OnClickListener clickedReset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            countDownTimer.pause();
            message("Mensagem","Deseja mesmo resetar a contagem", true);
        }
    };

    public void message(String title, String message, boolean reset) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(reset==false)
            builder.setPositiveButton("Sim", dialogClickListener);
        else if(reset==true)
            builder.setPositiveButton("Sim", dialogResetClickListener);
        builder.setNegativeButton("Não", null);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    DialogInterface.OnClickListener dialogResetClickListener = new DialogInterface.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    countDownTimer.stop();
                    countDownTimer.initCount();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    checkAndSaveRun();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkAndSaveRun(){
        if(firstValueSave==false){
            imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
            linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_green));
            firstValueSave=true;
            init=false;
            imgPause.setVisibility(View.GONE);
            imgReset.setVisibility(View.GONE);
            textFirstResult.setText(textCronometer.getText());
            textCronometer.setText("00:00");
            linearFirstValue.setVisibility(View.VISIBLE);
            countDownTimer.stop();
        }
        else if(secondValueSalve==false){
            imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
            linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_green));
            secondValueSalve=true;
            imgPause.setVisibility(View.GONE);
            imgReset.setVisibility(View.GONE);
            textSecondValue.setText(textCronometer.getText());
            textCronometer.setText("00:00");
            linearSecondValue.setVisibility(View.VISIBLE);
            countDownTimer.stop();
            init = false;
            btnSave.setVisibility(View.VISIBLE);
            btnSave.setEnabled(true);
            linearButtonPlay.setVisibility(View.GONE);

        }
    }

    private View.OnClickListener clickedSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveResultMessage();
        }
    };

    private void saveResultMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Sim", dialogSave);
        builder.setNegativeButton("Não", null);
        builder.setMessage("Deseja salvar os resultados dos testes?");
        builder.setTitle("Salvar");
        builder.show();
    }

    DialogInterface.OnClickListener dialogSave = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Services.message("Mensagem","Resultados foram salvos!",CronometerActivity.this);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed(){
        exitActivity();
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitActivity();
        }
    };

    private void exitActivity() {
        if (init) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Sim", dialogExit);
            builder.setNegativeButton("Não", null);
            builder.setMessage("Tem certeza que deseja sair e abandonar o teste?");
            builder.setTitle("Sair");
            builder.create().show();
        } else {
            if (countDownTimer.getPlay())
                countDownTimer.stop();
            finish();
        }
    }

    DialogInterface.OnClickListener dialogExit = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        countDownTimer.stop();
                    }catch (Exception e){

                    }
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public static void finished(Activity act){
        ((CronometerActivity)act).finish();
    }
}

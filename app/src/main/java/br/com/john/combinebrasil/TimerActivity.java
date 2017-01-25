package br.com.john.combinebrasil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.media.Image;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.CountDownTimer;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MaskHeight;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;
import br.com.john.combinebrasil.Services.Timer;

public class TimerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textTimer, textNameAthlete, textInfoNameTest, textInfoDetailsTest, textInfoNameAthlete,
            textInfoDetailsAthlete, textResult;
    ImageView imgDelete, imgTestArrow, imgAthleteArrow;
    LinearLayout linearAlterTimer, linearTimerPlay, linearConfigureTimer, linearDeleteTest,
            linearVisibilityReset, linearReset, linearStop, linearValues, linearInfo, linearSetting;
    EditText editFirstResult;
    MaterialBetterSpinner spinnerMinutes, spinnerSeconds;

    Button btnCancel, btnAlterDone, btnSave;
    DatabaseHelper db;

    private final Timer timer = new Timer();
    private boolean init = false, arrowDownTest=false, arrowDownPlayer=false;

    String idAthlete = "";
    int position = 0;
    long minutes=0, seconds=0;
    float ratingValue = 0;
    RatingBar ratingTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        ImageView imgInfo = (ImageView) findViewById(R.id.img_create_account);
        imgInfo.setImageDrawable(TimerActivity.this.getDrawable(R.drawable.ic_info));
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo();
            }
        });

        linearSetting = (LinearLayout) findViewById(R.id.linear_setting);
        linearSetting.setVisibility(View.VISIBLE);
        linearSetting.setOnClickListener(configureTimer);

        linearDeleteTest = (LinearLayout) findViewById(R.id.linear_delete);
        imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(clickDelete);

        textTimer = (TextView) findViewById(R.id.text_timer);
        textNameAthlete = (TextView) findViewById(R.id.text_name_timer);

        editFirstResult = (EditText) findViewById(R.id.edit_first_result);

        btnCancel = (Button) findViewById(R.id.button_cancel) ;
        btnAlterDone = (Button) findViewById(R.id.button_configure_timer);
        btnSave = (Button) findViewById(R.id.button_save_results);

        btnCancel.setOnClickListener(clickCancelConfigure);
        btnAlterDone.setOnClickListener(clickAlterTimer);
        btnSave.setOnClickListener(clickSaveResults);

        linearAlterTimer = (LinearLayout) findViewById(R.id.linear_configure_timer);
        linearTimerPlay = (LinearLayout) findViewById(R.id.linear_button_play);
        linearVisibilityReset = (LinearLayout)findViewById(R.id.linear_visibility_reset);
        linearReset = (LinearLayout)findViewById(R.id.linear_reset);
        linearStop = (LinearLayout)findViewById(R.id.linear_button_stop);
        linearValues = (LinearLayout)findViewById(R.id.linear_values);
        //linearRating = (LinearLayout) findViewById(R.id.linear_rating);
        ratingTimer = (RatingBar) findViewById(R.id.rating_timer);
        //btnReady = (Button) findViewById(R.id.button_ready_timer);

        textInfoNameTest = (TextView) findViewById(R.id.text_info_name_test);
        textInfoNameAthlete = (TextView) findViewById(R.id.text_info_name_athlete);
        textInfoDetailsAthlete = (TextView) findViewById(R.id.text_info_details_athlete);
        textInfoDetailsTest = (TextView) findViewById(R.id.text_info_details_test);
        textResult = (TextView) findViewById(R.id.text_result);
        imgTestArrow = (ImageView)findViewById(R.id.img_test_arrow);
        imgAthleteArrow = (ImageView)findViewById(R.id.img_player_arrow);

        //textQualification = (TextView) findViewById(R.id.text_show_qualify_timer);

        linearStop.setOnClickListener(clickedStop);
        linearTimerPlay.setOnClickListener(clickPlay);
        linearReset.setOnClickListener(clickReset);
        linearValues.setVisibility(View.GONE);

        linearConfigureTimer = (LinearLayout) findViewById(R.id.linear_configure_timer);

        spinnerMinutes = (MaterialBetterSpinner) findViewById(R.id.spinner_minutes);
        spinnerSeconds = (MaterialBetterSpinner) findViewById(R.id.spinner_seconds);

        TextWatcher mask = MaskHeight.insert("###", editFirstResult);
        editFirstResult.addTextChangedListener(mask);

        timer.setTextView(textTimer);
        db = new DatabaseHelper(TimerActivity.this);
        db.openDataBase();

        setTimer();

        inflateSpinner();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAthlete = extras.getString("id_player");
            position = extras.getInt("position");
            verifyTest();
            db.openDataBase();
            Athletes athlete = db.getAthleteById(idAthlete);
            textNameAthlete.setText(athlete.getName());
        }
    }

    private void verifyTest(){
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        if(test != null){
            if(!Services.convertIntInBool(test.getSync())) {
                linearDeleteTest.setVisibility(View.VISIBLE);
            }
                if(test.getCanSync()) {
                    linearValues.setVisibility(View.VISIBLE);
                    linearVisibilityReset.setVisibility(View.INVISIBLE);
                    linearTimerPlay.setVisibility(View.INVISIBLE);
                    editFirstResult.setEnabled(false);
                    textTimer.setVisibility(View.GONE);
                    linearSetting.setVisibility(View.GONE);

                    editFirstResult.setText(String.valueOf(test.getFirstValue()));
                    textResult.setText("Resultado do atleta:");
                    btnSave.setText("VOLTAR PARA O TESTE");
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
        }
        else{
            linearVisibilityReset.setVisibility(View.GONE);
            linearTimerPlay.setVisibility(View.VISIBLE);
            textTimer.setVisibility(View.VISIBLE);
            linearDeleteTest.setVisibility(View.GONE);
            linearValues.setVisibility(View.GONE);
            linearSetting.setVisibility(View.VISIBLE);
            editFirstResult.setEnabled(true);
            textResult.setText("Insira o quantidade de repetições feitas");
            btnSave.setText("SALVAR RESULTADO");
            editFirstResult.setText("");
            btnSave.setOnClickListener(clickSaveResults);
            init = false;
        }
    }
    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitActivity();
        }
    };

    private void exitActivity(){
        if (init) {
            new MessageOptions(TimerActivity.this, "Mesnagem", "Deseja sair no meio do teste?", "exit");
        } else {
            finish();
        }
    }

    private View.OnClickListener clickedStop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pauseTimer();
        }
    };

    private View.OnClickListener clickPlay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playTimer();
        }
    };

    private View.OnClickListener clickReset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetTimer();
        }
    };

    private void playTimer(){
        linearValues.setVisibility(View.GONE);
        if(init==false)
            setTimer();
        timer.initCount(TimerActivity.this);
        init=true;
        linearStop.setVisibility(View.VISIBLE);
        linearTimerPlay.setVisibility(View.GONE);
        linearVisibilityReset.setVisibility(View.VISIBLE);
        linearDeleteTest.setVisibility(View.GONE);
        linearSetting.setVisibility(View.GONE);
    }

    private void pauseTimer(){
        linearSetting.setVisibility(View.GONE);
        linearValues.setVisibility(View.VISIBLE);
        timer.pause();
        init=true;
        linearStop.setVisibility(View.GONE);
        linearTimerPlay.setVisibility(View.VISIBLE);
        linearVisibilityReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        linearSetting.setVisibility(View.VISIBLE);
        linearValues.setVisibility(View.GONE);
        timer.setStop();
        timer.setValue(minutes, seconds);
        init=false;
        linearStop.setVisibility(View.GONE);
        linearTimerPlay.setVisibility(View.VISIBLE);
        linearVisibilityReset.setVisibility(View.GONE);
        linearDeleteTest.setVisibility(View.GONE);
        textTimer.setText(SharedPreferencesAdapter.getTimerDefault(TimerActivity.this));
    }

    public static void alertFinishTimer(Activity act){
        ((TimerActivity) act).alertFinishTimer();
    }
    private void alertFinishTimer(){
        init=false;
        this.runOnUiThread(new Runnable() {
            public void run() {
                Services.buildNotificationCommon(TimerActivity.this);
                Services.messageAlert(TimerActivity.this, "Mensagem","O timer foi concluído, insira o resultado do atleta.","timer");
                textTimer.setText(SharedPreferencesAdapter.getTimerDefault(TimerActivity.this));
                linearValues.setVisibility(View.VISIBLE);
                linearSetting.setVisibility(View.VISIBLE);
                Vibrator v = (Vibrator) TimerActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        });
    }

    private void inflateSpinner(){
        String [] adapter = new String[60];
        for (int i=0; i<=60-1;i++) {
            String value = "";
            if(i<=9)
                value = String.valueOf("0"+i);
            else
                value = String.valueOf(i);
            adapter[i] = value;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, adapter);
        spinnerMinutes.setAdapter(arrayAdapter);
        spinnerSeconds.setAdapter(arrayAdapter);
    }

    private View.OnClickListener configureTimer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showConfigureTimer();
        }
    };
    private void showConfigureTimer(){
        linearConfigureTimer.setVisibility(View.VISIBLE);
        spinnerMinutes.setText("00");
        spinnerSeconds.setText("00");
    }

    private View.OnClickListener clickDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MessageOptions(TimerActivity.this, "Deletar", "Deseja excluir os dados do teste atual?", "delete");
        }
    };

    private View.OnClickListener clickCancelConfigure = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearConfigureTimer.setVisibility(View.GONE);
        }
    };
    private View.OnClickListener clickAlterTimer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alterTime();
        }
    };

    private void alterTime(){
        long min = Long.parseLong(spinnerMinutes.getText().toString());
        long sec = Long.parseLong(spinnerSeconds.getText().toString());
        if(min == 0 && sec == 00){
            Services.messageAlert(TimerActivity.this, "Aviso","Tempo inválido, não pode-se começar o timer em 00:00.","");
        }
        else{
            SharedPreferencesAdapter.setTimerDefault(TimerActivity.this, spinnerMinutes.getText().toString()+":"+spinnerSeconds.getText().toString());
            linearConfigureTimer.setVisibility(View.GONE);
            textTimer.setText(SharedPreferencesAdapter.getTimerDefault(TimerActivity.this));
            timer.setValue(min, sec);
        }
    }

    private View.OnClickListener clickSaveResults = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!editFirstResult.getText().toString().isEmpty())
                saveResult();

        }
    };


    private void saveResult(){
        db.openDataBase();
        User user= db.getUser();
        Selective selective = db.getSelective();
        Tests test = new Tests(
                UUID.randomUUID().toString(),
                AllActivities.testSelected,
                idAthlete,
                selective.getId(),
                Long.parseLong(editFirstResult.getText().toString()),
                00,
                0,
                " ",
                user.getId(),
                Services.convertBoolInInt(false),
                true
        );
        db.addTest(test);
        linearDeleteTest.setVisibility(View.VISIBLE);
        linearValues.setVisibility(View.GONE);
        AthletesActivity.adapterTests.notifyItemChanged(position);
        Services.messageAlert(TimerActivity.this, "Mensagem","Teste salvo!","save");
    }
    private void deleteTest(){
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        db.openDataBase();
        db.deleteValue(Constants.TABLE_TESTS, test.getId());
        Services.messageAlert(TimerActivity.this, "Mensagem","Teste excluído!","deleteDone");
        verifyTest();
    }

    public static void returnOption (Activity act, String method){
        ((TimerActivity)act).returnOption(method);
    }

    private void returnOption(String whoCalled){
        if(whoCalled.equals("save")){
            AthletesActivity.adapterTests.notifyItemChanged(position);
            timer.setStop();
            this.finish();
        }
        else if(whoCalled.equals("delete")){
            deleteTest();
        }

        else if(whoCalled.equals("deleteDone")){
            verifyTest();
        }

        else if(whoCalled.equals("exit")){
            timer.setStop();
            finish();
        }
    }

    private void setTimer(){
        String time = "";
        try{
            time = SharedPreferencesAdapter.getTimerDefault(TimerActivity.this);
            if(time==null || time.equals("")){
                SharedPreferencesAdapter.setTimerDefault(TimerActivity.this, "01:00");
                time="01:00";
            }
        }catch (Exception e){
            SharedPreferencesAdapter.setTimerDefault(TimerActivity.this, "01:00");
            time="01:00";
        }
        try {
            minutes = Long.parseLong(time.substring(0, 2));
            seconds = Long.parseLong(time.substring(3));
        }catch (Exception e){
            minutes = 01;
            seconds= 00;
        }
        textTimer.setText(time);
        timer.setValue(minutes, seconds);
    }

    private void showInfo(){
        linearInfo = (LinearLayout) findViewById(R.id.linear_info);
        linearInfo.setVisibility(View.VISIBLE);
        DatabaseHelper db  = new DatabaseHelper(TimerActivity.this);
        db.openDataBase();
        TestTypes test = db.getTestTypeFromId(AllActivities.testSelected);
        if(test!=null) {
            textInfoNameTest.setText(test.getName());
            textInfoDetailsTest.setText(Html.fromHtml(test.getDescription()));
        }

        Athletes athlete = db.getAthleteById(idAthlete);

        if(athlete!=null){
            Positions positiom = db.getPositiomById(athlete.getDesirablePosition());
            String pos = "";
            if(positiom!=null){
                pos = positiom.getNAME();
            }
            textInfoNameAthlete.setText(athlete.getName());
            textInfoDetailsAthlete.setText("Nascimento: "+ Services.convertDate(athlete.getBirthday())+ "\n"+
                    "CPF: "+athlete.getCPF() +"\n"+
                    "Endereço: "+athlete.getAddress() +"\n"+
                    "Posição Desejada: "+pos +"\n"+
                    "Altura: "+String.format("%.2f", athlete.getHeight()).replace(".",",") +"\n"+
                    "Peso: "+String.format("%.0f",athlete.getWeight()).replace(".",",")+" Kg");
        }

        Button btnClose = (Button) findViewById(R.id.button_close_info);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearInfo.setVisibility(View.GONE);
            }
        });

        imgTestArrow.setOnClickListener(clickedImgArrowTest);
        imgAthleteArrow.setOnClickListener(clickedImgArrowPlayer);
    }

    private View.OnClickListener clickedImgArrowTest = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(arrowDownTest)
            {
                arrowDownTest=false;
                imgTestArrow.setImageDrawable(getDrawable(R.drawable.arrow_top));
                textInfoDetailsTest.setVisibility(View.VISIBLE);
            }
            else{
                arrowDownTest=true;
                imgTestArrow.setImageDrawable(getDrawable(R.drawable.arrow_down));
                textInfoDetailsTest.setVisibility(View.GONE);
            }
        }
    };

    private View.OnClickListener clickedImgArrowPlayer = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(arrowDownPlayer)
            {
                arrowDownPlayer=false;
                imgAthleteArrow.setImageDrawable(getDrawable(R.drawable.arrow_top));
                textInfoDetailsAthlete.setVisibility(View.VISIBLE);
            }
            else{
                arrowDownPlayer = true;
                imgAthleteArrow.setImageDrawable(getDrawable(R.drawable.arrow_down));
                textInfoDetailsAthlete.setVisibility(View.GONE);
            }

        }
    };
    /*private void showRating(){
        linearRating.setVisibility(View.VISIBLE);
        btnReady.setEnabled(false);
        ratingTimer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating > 0.0) {
                    btnReady.setEnabled(true);
                    btnReady.setAlpha(1f);
                    textQualification.setVisibility(View.VISIBLE);
                }
                else {
                    btnReady.setEnabled(false);
                    btnReady.setAlpha(.5f);
                }
                textQualification.setText(Services.verifyQualification(rating));
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TimerActivity.this,
                        String.valueOf(Services.verifyQualification(ratingTimer.getRating())),
                        Toast.LENGTH_SHORT).show();
                ratingValue = ratingTimer.getRating();
                saveResult();
                linearRating.setVisibility(View.GONE);
                Services.messageAlert(TimerActivity.this, "Mensagem","Os resultados foram salvos!","Save");
            }
        });
    }*/

    private void showInfoTestDone(){
        /*Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);

        linearInsert.setVisibility(View.GONE);
        linearResultDone.setVisibility(View.VISIBLE);
        txtFistDone.setText(Services.convertInTime(test.getFirstValue()));
        txtSecondDone.setText(Services.convertInTime(test.getSecondValue()));

        Athletes athlete = db.getAthleteById(idAthlete);
        txtNameResult.setText(athlete.getName());

        ratingDone.setRating(test.getRating());
        ratingDone.setEnabled(false);

        txtRating.setText(Services.verifyQualification(test.getRating()));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }


    /*private void exitActivity() {
        if (init) {
            messageOption("Sair","Tem certeza que deseja sair e abandonar o teste?","exitCronometer");
        } else {
            if (timer.getPlay())
                stopCronometer();
            finish();
        }
    }

    private void messageOption(String title, String message, String method){
        pauseCronometer();
        new MessageOptions(TimerActivity.this, title, message, method);
    }*/



}

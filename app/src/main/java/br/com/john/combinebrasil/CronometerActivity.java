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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.UUID;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.CountDownTimer;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class CronometerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textFirstResult, textSecondValue, textCronometer, textShowQualify, textInfoNameAthlete, textInfoNameTest, textInfoDetailsAthlete, textInfoDetailsTest;
    LinearLayout linearButtonPlay, linearFirstValue, linearSecondValue, linearRating, linearInfo;
    ImageView imgIconButtonPlay, imgReset, imgPause, imgSave, imgTestArrow, imgAthleteArrow;
    Button btnSave, btnReady, btnInconclusive;
    RatingBar ratingBar;
    private float ratingValue;
    String idAthlete = "";
    int position = 0, inconclusive=0;
    boolean isSaveInconclusive=false, arrowDownTest=false, arrowDownPlayer=false;

    LinearLayout linearInsert, linearResultDone;
    TextView txtFistDone, txtSecondDone, txtNameResult, txtRating;
    Button buttonBack;
    RatingBar ratingDone;

    private final CountDownTimer countDownTimer = new CountDownTimer();
    private boolean init = false, firstValueSave = false, secondValueSalve = false, isPause=false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometer);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        ImageView imgInfo = (ImageView) findViewById(R.id.img_create_account);
        imgInfo.setImageDrawable(CronometerActivity.this.getDrawable(R.drawable.ic_info));
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo();
            }
        });

        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);

        try {
            TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
            DatabaseHelper db = new DatabaseHelper(CronometerActivity.this);
            db.getTestTypeFromId(AllActivities.testSelected).getName();
            textTitle.setText(db.getTestTypeFromId(AllActivities.testSelected).getName());
        }catch(Exception e){}

        linearButtonPlay = (LinearLayout) findViewById(R.id.linear_button_play);
        linearFirstValue = (LinearLayout) findViewById(R.id.linear_show_first_value);
        linearSecondValue = (LinearLayout) findViewById(R.id.linear_show_second_value);
        linearRating = (LinearLayout) findViewById(R.id.linear_rating_cronometer);

        textFirstResult = (TextView) findViewById(R.id.text_first_value);
        textSecondValue = (TextView) findViewById(R.id.text_second_value);
        textCronometer = (TextView) findViewById(R.id.text_cronometer);
        textShowQualify = (TextView) findViewById(R.id.text_show_qualify_cronometer);
        final TextView textNamePlayer = (TextView) findViewById(R.id.text_name_player_cronometer);

        imgIconButtonPlay = (ImageView) findViewById(R.id.image_icon_button_play);
        imgPause = (ImageView) findViewById(R.id.image_pause);
        imgReset = (ImageView) findViewById(R.id.image_reset);
        imgSave = (ImageView) findViewById(R.id.image_save);
        imgTestArrow = (ImageView)findViewById(R.id.img_test_arrow);
        imgAthleteArrow = (ImageView)findViewById(R.id.img_player_arrow);

        btnSave = (Button) findViewById(R.id.button_save_results);
        btnReady = (Button) findViewById(R.id.button_ready_cronometer);
        btnInconclusive=(Button) findViewById(R.id.button_inconclusive_results);

        ratingBar = (RatingBar) findViewById(R.id.rating_cronometer);

        countDownTimer.setTextView(textCronometer);

        textShowQualify.setText("");
        textShowQualify.setVisibility(View.GONE);

        linearInsert = (LinearLayout) findViewById(R.id.linear_insert);
        linearResultDone = (LinearLayout) findViewById(R.id.linear_results_done);
        txtNameResult = (TextView) findViewById(R.id.txt_name_result);
        txtFistDone = (TextView) findViewById(R.id.txt_first_result_done);
        txtSecondDone = (TextView) findViewById(R.id.txt_second_result_done);
        ratingDone = (RatingBar) findViewById(R.id.rating_result_done);
        buttonBack = (Button) findViewById(R.id.button_back);
        txtRating = (TextView) findViewById(R.id.txt_rating_done);

        textInfoNameTest = (TextView) findViewById(R.id.text_info_name_test);
        textInfoNameAthlete = (TextView) findViewById(R.id.text_info_name_athlete);
        textInfoDetailsAthlete = (TextView) findViewById(R.id.text_info_details_athlete);
        textInfoDetailsTest = (TextView) findViewById(R.id.text_info_details_test);


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAthlete = extras.getString("id_player");
            position = extras.getInt("position");
            verifyTest();
            DatabaseHelper db  = new DatabaseHelper(CronometerActivity.this);
            db.openDataBase();
            Athletes athlete = db.getAthleteById(idAthlete);
            textNamePlayer.setText(athlete.getName());
        }
    }

    private void verifyTest(){
        DatabaseHelper db = new DatabaseHelper(CronometerActivity.this);
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        if(test != null){
            linearInsert.setVisibility(View.GONE);
            linearResultDone.setVisibility(View.VISIBLE);
            txtFistDone.setText(test.getFirstValue());
            txtSecondDone.setText(test.getSecondValue());

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
            });
        }
        else{
            btnSave.setOnClickListener(clickedSave);
            btnInconclusive.setOnClickListener(btnResetClickListener);
            imgPause.setOnClickListener(clickedPause);
            imgReset.setOnClickListener(clickedReset);
            imgSave.setOnClickListener(clickedSaveTime);
            linearButtonPlay.setOnClickListener(clickedPlayAndStop);

        }
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
        imgSave.setVisibility(View.GONE);
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
        imgSave.setVisibility(View.GONE);
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
        if(imgSave.getVisibility()==View.VISIBLE)
            imgPause.setVisibility(View.GONE);
        else
            imgPause.setVisibility(View.INVISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void resetCrometer(){
        inconclusive = inconclusive +1;
        if(!isSaveInconclusive)
            verifyInconclusive();
        isPause = true;
        init=false;
        countDownTimer.stop();
        linearButtonPlay.setBackground(getDrawable(R.drawable.background_circle_green));
        imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
        imgPause.setVisibility(View.INVISIBLE);
        imgReset.setVisibility(View.INVISIBLE);
        imgSave.setVisibility(View.GONE);
        textCronometer.setText("00:00");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkAndSaveRun(){
        if(inconclusive >=2)
            isSaveInconclusive=true;
        if(firstValueSave==false){
            firstValueSave=true;
            textFirstResult.setText(textCronometer.getText());
            linearFirstValue.setVisibility(View.VISIBLE);
            stopCronometer();
            inconclusive=0;

        }
        else if(secondValueSalve==false){
            secondValueSalve=true;
            inconclusive=0;
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
            showRating();
        }
    }

    private void showRating(){
        linearRating.setVisibility(View.VISIBLE);
        btnReady.setEnabled(false);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating > 0.0) {
                    btnReady.setEnabled(true);
                    btnReady.setAlpha(1f);
                    textShowQualify.setVisibility(View.VISIBLE);
                }
                else {
                    btnReady.setEnabled(false);
                    btnReady.setAlpha(.5f);
                }
                textShowQualify.setText(Services.verifyQualification(rating));
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CronometerActivity.this,
                        String.valueOf(Services.verifyQualification(ratingBar.getRating())),
                        Toast.LENGTH_SHORT).show();
                ratingValue = ratingBar.getRating();
                saveTest();
                linearRating.setVisibility(View.GONE);
                Services.messageAlert(CronometerActivity.this, "Mensagem","Os resultados foram salvos!","DIALOGSAVECRONOMETER");
            }
        });
    }

    private void saveTest(){
        DatabaseHelper db = new DatabaseHelper(CronometerActivity.this);
        db.openDataBase();
        User user= db.getUser();
        Tests test = new Tests(
                UUID.randomUUID().toString(),
                AllActivities.testSelected,
                idAthlete,
                textFirstResult.getText().toString(),
                textSecondValue.getText().toString(),
                ratingValue,
                " ",
                user.getId(),
                Services.convertBoolInInt(false)
                );
        db.addTest(test);
        AthletesActivity.adapterTests.notifyItemChanged(position);
    }

    private void verifyInconclusive(){
        if(inconclusive>=2){
            btnSave.setVisibility(View.GONE);
            btnInconclusive.setVisibility(View.VISIBLE);
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
                    imgSave.setVisibility(View.VISIBLE);
                    if (firstValueSave == false) {
                        messageOption("Salvar", "Deseja salvar o primeiro resultado?", "saveResult");
                        imgPause.setVisibility(View.GONE);
                    }
                     else if (secondValueSalve == false) {
                        messageOption("Salvar", "Deseja salvar o segundo resultado?", "saveResult");
                        imgPause.setVisibility(View.GONE);
                    }

                }
                else
                    playCronometer();
            }
        }
    };


    private View.OnClickListener clickedSaveTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (firstValueSave == false)
                messageOption("Salvar","Deseja salvar o primeiro resultado?","saveResult");
            else if (secondValueSalve == false)
                messageOption("Salvar","Deseja salvar o segundo resultado?","saveResult");
        }
    };
    private View.OnClickListener clickedPause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickePause();
            imgSave.setVisibility(View.VISIBLE);
            imgPause.setVisibility(View.GONE);
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

    private View.OnClickListener btnResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnInconclusive.setVisibility(View.GONE);
            btnSave.setVisibility(View.INVISIBLE);
            if (firstValueSave == false) {
                textFirstResult.setText("00:00");
                messageOption("Salvar", "Deseja salvar o primeiro resultado como inconclusivo?", "saveResult");
            }
            else if (secondValueSalve == false) {
                textSecondValue.setText("00:00");
                messageOption("Salvar", "Deseja salvar o segundo resultado como inconclusivo?", "saveResult");
            }
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

    private void showInfo(){
        linearInfo = (LinearLayout) findViewById(R.id.linear_info);
        linearInfo.setVisibility(View.VISIBLE);
        DatabaseHelper db  = new DatabaseHelper(CronometerActivity.this);
        db.openDataBase();
        TestTypes test = db.getTestTypeFromId(AllActivities.testSelected);
        if(test!=null) {
            textInfoNameTest.setText(test.getName());
            String testDetail = test.getDescription();
            testDetail = testDetail.replace(".",".\n");
            testDetail = testDetail.replace(";",";\n");
            testDetail = testDetail.replace("-","\n-");
            textInfoDetailsTest.setText(testDetail);
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
}

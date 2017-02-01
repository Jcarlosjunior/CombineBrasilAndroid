package br.com.john.combinebrasil;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.UUID;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.CreateJSON;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostSync;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.CountDownTimer;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class CronometerOnlyOneActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView textCronometer, textShowQualify, textInfoNameAthlete,
            textInfoNameTest, textInfoDetailsAthlete, textInfoDetailsTest, textRating, textProgress;

    LinearLayout linearButtonPlay, linearRating, linearInfo, deleteTest, linearProgress, linearVisibilityReset, linearReset,
        linearStop;
    ImageView imgIconButtonPlay, imgTestArrow, imgAthleteArrow, imgDelete;
    Button btnSave, btnReady, btnCancel;
    RatingBar ratingBar;
    private float ratingValue = 0;
    String idAthlete = "", nameTest="";
    int position = 0;
    private boolean init = false, isPause=false, arrowDownTest=false, arrowDownPlayer=false;

    LinearLayout linearInsert, linearResultDone;
    TextView txtFistDone, txtNameResult, txtRating;
    Button buttonBack;
    RatingBar ratingDone;

    private final CountDownTimer countDownTimer = new CountDownTimer();

    DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometer_only_one);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        ImageView imgInfo = (ImageView) findViewById(R.id.img_create_account);
        imgInfo.setImageDrawable(CronometerOnlyOneActivity.this.getDrawable(R.drawable.ic_info));
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo();
            }
        });

        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        final TextView textNamePlayer = (TextView) findViewById(R.id.text_name_player_cronometer);

        createElements();

        db = new DatabaseHelper(CronometerOnlyOneActivity.this);
        db.openDataBase();

        try {
            TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
            db.getTestTypeFromId(AllActivities.testSelected).getName();
            textTitle.setText(db.getTestTypeFromId(AllActivities.testSelected).getName());
            nameTest = db.getTestTypeFromId(AllActivities.testSelected).getName();
        }catch(Exception e){}


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAthlete = extras.getString("id_player");
            position = extras.getInt("position");
            verifyTest();
            db.openDataBase();
            Athletes athlete = db.getAthleteById(idAthlete);
            textNamePlayer.setText(athlete.getName());
        }
    }

    private void verifyTest(){
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        if(test != null){
            if(!Services.convertIntInBool(test.getSync())) {
                deleteTest.setVisibility(View.VISIBLE);
            }

            if(test.getCanSync()) {
                showInfoTestDone();
            }
            else {
                linearInsert.setVisibility(View.VISIBLE);
                linearResultDone.setVisibility(View.GONE);
            }
        }
        else{
            deleteTest.setVisibility(View.GONE);
            linearInsert.setVisibility(View.VISIBLE);
            linearResultDone.setVisibility(View.GONE);
            init = false;
            isPause=false;
            ratingValue = 0;
            imgIconButtonPlay.setVisibility(View.VISIBLE);
            linearButtonPlay.setVisibility(View.VISIBLE);
            enabledButtonAdd(false);
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
        linearStop.setVisibility(View.VISIBLE);
        linearButtonPlay.setVisibility(View.GONE);
        linearVisibilityReset.setVisibility(View.VISIBLE);

        enabledButtonAdd(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void stopCronometer(){
        countDownTimer.stop();
        init=false;
        //linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_red));
        //imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
       // imgIconButtonPlay.setImageDrawable(getDrawable(R.drawable.icon_play));
        //linearButtonPlay.setBackground(getDrawable(R.drawable.background_button_circle_green));

        linearStop.setVisibility(View.GONE);
        linearButtonPlay.setVisibility(View.VISIBLE);
        linearVisibilityReset.setVisibility(View.VISIBLE);
        textCronometer.setText("00:00");
        enabledButtonAdd(false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void pauseCronometer(){
        enabledButtonAdd(true);
        isPause=true;
        countDownTimer.pause();
        linearStop.setVisibility(View.GONE);
        linearButtonPlay.setVisibility(View.VISIBLE);
        linearVisibilityReset.setVisibility(View.VISIBLE);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void resetCrometer(){
        isPause = true;
        init=false;
        countDownTimer.stop();

        linearStop.setVisibility(View.GONE);
        linearButtonPlay.setVisibility(View.VISIBLE);
        linearVisibilityReset.setVisibility(View.GONE);

       textCronometer.setText("00:00");
        enabledButtonAdd(false);
    }

    private void checkAndSaveRun(){
        showRating();
    }

    /*
    ********************************** MESSAGES *********************************
    */
    private void messageOption(String title, String message, String method){
        pauseCronometer();
        new MessageOptions(CronometerOnlyOneActivity.this, title, message, method);
    }

    public static void getMethodOutActivity(Activity act, String method){
        ((CronometerOnlyOneActivity)act).calledFunctions(method);
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
            if(nameTest.toString().toLowerCase().equals("sprint 40 jardas")|nameTest.toString().toLowerCase().equals("sprint 40")){
                saveTest();
                linearRating.setVisibility(View.GONE);
            }else
                showRating();
        }
        else if(method.equals("deleteCronometer")){
            deleteTest();
        }
    }

    private void showRating(){
        pauseCronometer();
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
                Toast.makeText(CronometerOnlyOneActivity.this,
                        String.valueOf(Services.verifyQualification(ratingBar.getRating())),
                        Toast.LENGTH_SHORT).show();
                ratingValue = ratingBar.getRating();
                saveTest();
                linearRating.setVisibility(View.GONE);
            }
        });
    }

    private void saveTest(){
        db.openDataBase();
        User user= db.getUser();
        Selective selective = db.getSelective();
        Tests test = new Tests(
                UUID.randomUUID().toString(),
                AllActivities.testSelected,
                idAthlete,
                selective.getId(),
                Services.convertInMilliSeconds(textCronometer.getText().toString()),
                00,
                ratingValue,
                " ",
                user.getId(),
                Services.convertBoolInInt(false),
                false
        );
        db.addTest(test);
        deleteTest.setVisibility(View.VISIBLE);

        sync(test);
    }

    private void sync(Tests test){
        if(Services.isOnline(CronometerOnlyOneActivity.this)) {
            linearProgress.setVisibility(View.VISIBLE);
            db = new DatabaseHelper(CronometerOnlyOneActivity.this);
            Athletes athlete  = db.getAthleteById(test.getAthlete());
            if(athlete!=null){
                if(athlete.getSync()){
                    String url = Constants.URL + Constants.API_TESTS;
                        PostSync post = new PostSync();
                        post.setActivity(CronometerOnlyOneActivity.this);
                        post.setAll(false);
                        post.setObjPut(CreateJSON.createObject(test));
                        post.execute(url);
                }
            }
        }
        else
            Services.messageAlert(CronometerOnlyOneActivity.this, "Aviso","Para começar a sincronização, você precisa ter uma conexão com a internet.","");
    }

    public static void returnPostTest(Activity act, String resp, String result){
        ((CronometerOnlyOneActivity)act).returnPostTest(resp, result);
    }

    private void returnPostTest(String resp, String result){
        linearProgress.setVisibility(View.GONE);
        //result = result.replaceFirst("Value","");

        Log.i("ERROR", result);

            if(resp.equals("OK")) {
                DeserializerJsonElements des = new DeserializerJsonElements(result);
                Tests test = des.getTestObject();
                updateTest(test);
            }
            else{
                updateTestExist(result);
            }
    }

    private void updateTestExist(String result){
        try {
            linearProgress.setVisibility(View.VISIBLE);
            JSONObject json = new JSONObject(result);
            String detail = json.getString("detail");
            json = new JSONObject(detail);
            if(json.getInt("code") ==  11000){
                String update = json.getString("op");
                json = new JSONObject(update);
                String url = Constants.URL+Constants.API_TESTS+"?"+Constants.TESTS_ATHLETE+"="+
                        json.getString(Constants.TESTS_ATHLETE)+"&"+
                        Constants.TESTS_TYPE+"="+json.getString(Constants.TESTS_TYPE)+"&"+Constants.TESTS_SELECTIVE+"="+
                        json.getString(Constants.TESTS_SELECTIVE);
                Connection task = new Connection(url, 0, "UPDATE_TEST", false, CronometerOnlyOneActivity.this);
                task.callByJsonStringRequest();
            }
            else{
                linearProgress.setVisibility(View.GONE);
                Services.messageAlert(CronometerOnlyOneActivity.this, "Aviso","Erro ao tentar sincronizar teste.","");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void returnUpdateSync(Activity act, String response){
        ((CronometerOnlyOneActivity)act).returnUpdateSync(response);
    }

    private void returnUpdateSync(String response) {
        linearProgress.setVisibility(View.GONE);
        if (!response.equals("[]")) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            Tests test = des.getTestObjectTest();
            if (test != null) {
                updateTest(test);
            } else
                Services.messageAlert(CronometerOnlyOneActivity.this, "Aviso", "Erro ao tentar sincronizar teste.", "");
        }
        else
            Services.messageAlert(CronometerOnlyOneActivity.this, "Aviso", "Erro ao tentar sincronizar teste.", "");
    }

    private void updateTest(Tests test){
        countDownTimer.stop();
        DatabaseHelper db = new DatabaseHelper(CronometerOnlyOneActivity.this);
        db.updateTest(test);
        try {
            AthletesActivity.adapterTests.notifyItemChanged(position);
        }catch (Exception e){
            e.printStackTrace();
        }

        Services.messageAlert(CronometerOnlyOneActivity.this, "Mensagem","Os resultados foram salvos!","DIALOGSAVECRONOMETER");
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
            linearStop.setVisibility(View.VISIBLE);
            linearButtonPlay.setVisibility(View.GONE);
            linearVisibilityReset.setVisibility(View.VISIBLE);

            if(init==false){
                playCronometer();
            }
            else {
                if(!isPause) {
                       stopCronometer();

                }
                else
                    playCronometer();
            }
        }
    };


    private View.OnClickListener clickedSaveTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkAndSaveRun();
        }
    };
    private View.OnClickListener clickedPause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickePause();

        }
    };

    private View.OnClickListener clickedSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkAndSaveRun();
        }
    };

    private View.OnClickListener clickCancel = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            linearRating.setVisibility(View.GONE);
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
            resetCrometer();
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
        ((CronometerOnlyOneActivity)act).finish();
    }

    private void showInfo(){
        linearInfo = (LinearLayout) findViewById(R.id.linear_info);
        linearInfo.setVisibility(View.VISIBLE);
        DatabaseHelper db  = new DatabaseHelper(CronometerOnlyOneActivity.this);
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

    private void enabledButtonAdd(boolean enabled){
        if(enabled) {
            btnSave.setEnabled(true);
            btnSave.setAlpha(1f);
        }else {
            btnSave.setEnabled(false);
            btnSave.setAlpha(.5f);
        }
    }

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

    private void showInfoTestDone(){
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);

        linearInsert.setVisibility(View.GONE);
        linearResultDone.setVisibility(View.VISIBLE);
        txtFistDone.setText(Services.convertInTime(test.getFirstValue()));

        Athletes athlete = db.getAthleteById(idAthlete);
        txtNameResult.setText(athlete.getName());

        ratingDone.setRating(test.getRating());
        ratingDone.setEnabled(false);

        if(nameTest.toString().toLowerCase().equals("sprint 40 jardas")||
                nameTest.toString().toLowerCase().equals("sprint 40")){
            ratingDone.setVisibility(View.INVISIBLE);
            textRating.setVisibility(View.INVISIBLE);
            txtRating.setVisibility(View.INVISIBLE);
        }
        else{
            ratingDone.setVisibility(View.VISIBLE);
            textRating.setVisibility(View.VISIBLE);
            txtRating.setVisibility(View.VISIBLE);
        }


        txtRating.setText(Services.verifyQualification(test.getRating()));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createElements(){
        deleteTest = (LinearLayout) findViewById(R.id.linear_delete);
        imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(clickDelete);

        linearButtonPlay = (LinearLayout) findViewById(R.id.linear_button_play);
        linearVisibilityReset = (LinearLayout)findViewById(R.id.linear_visibility_reset);
        linearReset = (LinearLayout)findViewById(R.id.linear_reset);
        linearStop = (LinearLayout)findViewById(R.id.linear_button_stop);

        linearRating = (LinearLayout) findViewById(R.id.linear_rating_cronometer);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);

        textCronometer = (TextView) findViewById(R.id.text_cronometer);
        textShowQualify = (TextView) findViewById(R.id.text_show_qualify_cronometer);

        imgIconButtonPlay = (ImageView) findViewById(R.id.image_icon_button_play);
        imgTestArrow = (ImageView)findViewById(R.id.img_test_arrow);
        imgAthleteArrow = (ImageView)findViewById(R.id.img_player_arrow);

        btnSave = (Button) findViewById(R.id.button_save_results);
        btnReady = (Button) findViewById(R.id.button_ready_cronometer);
        btnCancel = (Button) findViewById(R.id.button_cancel);

        ratingBar = (RatingBar) findViewById(R.id.rating_cronometer);

        countDownTimer.setTextView(textCronometer);
        countDownTimer.setButton(btnSave);

        textShowQualify.setText("");
        textShowQualify.setVisibility(View.GONE);

        linearInsert = (LinearLayout) findViewById(R.id.linear_insert);
        linearResultDone = (LinearLayout) findViewById(R.id.linear_results_done);
        txtNameResult = (TextView) findViewById(R.id.txt_name_result);
        txtFistDone = (TextView) findViewById(R.id.txt_first_result_done);
        ratingDone = (RatingBar) findViewById(R.id.rating_result_done);
        buttonBack = (Button) findViewById(R.id.button_back);
        txtRating = (TextView) findViewById(R.id.txt_rating_done);
        textRating = (TextView) findViewById(R.id.text_rating);
        textProgress = (TextView) findViewById(R.id.text_progress);

        textInfoNameTest = (TextView) findViewById(R.id.text_info_name_test);
        textInfoNameAthlete = (TextView) findViewById(R.id.text_info_name_athlete);
        textInfoDetailsAthlete = (TextView) findViewById(R.id.text_info_details_athlete);
        textInfoDetailsTest = (TextView) findViewById(R.id.text_info_details_test);

        btnSave.setOnClickListener(clickedSave);
        btnCancel.setOnClickListener(clickCancel);

        linearStop.setOnClickListener(clickedPause);
        linearReset.setOnClickListener(btnResetClickListener);
        linearButtonPlay.setOnClickListener(clickedPlayAndStop);
    }

    private View.OnClickListener clickDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MessageOptions(CronometerOnlyOneActivity.this, "Deletar", "Deseja excluir os dados do teste atual?", "deleteCronometer");
        }
    };

    private void deleteTest(){
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        db.openDataBase();
        db.deleteValue(Constants.TABLE_TESTS, test.getId());
        Services.messageAlert(CronometerOnlyOneActivity.this, "Mensagem","Teste excluído!","");
        verifyTest();
    }
}
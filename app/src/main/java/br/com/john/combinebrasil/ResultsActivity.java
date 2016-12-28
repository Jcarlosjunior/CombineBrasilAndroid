package br.com.john.combinebrasil;

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

import java.util.ArrayList;
import java.util.UUID;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class ResultsActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button buttonAdd, btnReady;
    EditText editFirstResult, editSecondResult;
    int contResults=0;
    TextView textNamePlayer, textNameTest, textNameTestDetails, textDetailsTest, textNamePlayerDetails, textDetailsPlayer, textShowRating;
    ImageView imgArrowTest, imgArrowPlayer;
    boolean arrowDownTest=true,arrowDownPlayer=true;
    LinearLayout linearRating;
    RatingBar ratingBar;
    String idAthlete = "";
    float ratingValue;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);

        linearRating = (LinearLayout) findViewById(R.id.linear_rating_results);

        editFirstResult = (EditText) findViewById(R.id.edit_first_result);
        editSecondResult = (EditText) findViewById(R.id.edit_second_result);
        buttonAdd = (Button) findViewById(R.id.button_add_results);
        btnReady = (Button) findViewById(R.id.button_ready_results);

        textNamePlayer = (TextView) findViewById(R.id.text_name_player_result);
        textNameTest = (TextView) findViewById(R.id.text_name_test_result);
        textNameTestDetails = (TextView) findViewById(R.id.text_name_test_details);
        textDetailsTest = (TextView) findViewById(R.id.text_details_test);
        textNamePlayerDetails = (TextView) findViewById(R.id.text_name_player_details);
        textDetailsPlayer = (TextView) findViewById(R.id.text_details_player);
        textShowRating = (TextView) findViewById(R.id.text_show_qualify_results);

        imgArrowPlayer = (ImageView) findViewById(R.id.img_player_arrow);
        imgArrowTest = (ImageView) findViewById(R.id.img_test_arrow);
        imgArrowTest.setOnClickListener(clickedImgArrowTest);
        imgArrowPlayer.setOnClickListener(clickedImgArrowPlayer);

        ratingBar = (RatingBar) findViewById(R.id.rating_results);

        buttonAdd.setOnClickListener(clickSave);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            idAthlete = extras.getString("id_player");
            showInfoAthlete();
            checkAndSaveResults();
            verifyTest();
    }
}


    private void verifyTest(){
        DatabaseHelper db = new DatabaseHelper(ResultsActivity.this);
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        if(test != null){
            String firstValue = test.getValue().substring(0, 4);
            String secondValue = test.getValue().substring(5, test.getValue().length());
            editFirstResult.setText(firstValue);
            editSecondResult.setText(secondValue);
            editFirstResult.setEnabled(false);
            editSecondResult.setEnabled(false);
            buttonAdd.setVisibility(View.GONE);
            btnReady.setVisibility(View.GONE);
        }
        else{
            editFirstResult.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()>2)
                        enabledButtonAdd(true);
                    else
                        enabledButtonAdd(false);

                    if(s.length()==1){
                        editFirstResult.setText(s.toString() + ",");
                        editFirstResult.setSelection(s.length()+1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editSecondResult.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()>2)
                        enabledButtonAdd(true);
                    else
                        enabledButtonAdd(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void showInfoAthlete(){
        DatabaseHelper db  = new DatabaseHelper(ResultsActivity.this);
        db.openDataBase();
        Athletes athlete = db.getAthleteById(idAthlete);
        textNamePlayer.setText(athlete.getName());
        textNamePlayerDetails.setText(athlete.getName());

        TestTypes test = db.getTestTypeFromId(AllActivities.testSelected);
        if(test!=null) {
            textNameTest.setText(test.getName());
            textNameTestDetails.setText(test.getName());
            //textDetailsTest.setText(detailText);
        }
    }

    private View.OnClickListener clickedImgArrowTest = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(arrowDownTest)
            {
                arrowDownTest=false;
                imgArrowTest.setImageDrawable(getDrawable(R.drawable.arrow_top));
                textDetailsTest.setVisibility(View.VISIBLE);
            }
            else{
                arrowDownTest=true;
                imgArrowTest.setImageDrawable(getDrawable(R.drawable.arrow_down));
                textDetailsTest.setVisibility(View.GONE);
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
                imgArrowPlayer.setImageDrawable(getDrawable(R.drawable.arrow_top));
                textDetailsPlayer.setVisibility(View.VISIBLE);
            }
            else{
                arrowDownPlayer = true;
                imgArrowPlayer.setImageDrawable(getDrawable(R.drawable.arrow_down));
                textDetailsPlayer.setVisibility(View.GONE);
            }

        }
    };

    public static void getMethodOutResultsActivity(Activity act, String method){
        ((ResultsActivity)act).calledMethod(method);
    }

    private void calledMethod(String method){
        if(method.equals("checkAndSaveResults")){
            contResults = contResults + 1;
            checkAndSaveResults();
        }
        else if(method.equals("saveAllResults")){
            showRating();
        }
        else if(method.equals("exitActivity")){
            finish();
        }
    }

    private void showRating(){
        linearRating.setVisibility(View.VISIBLE);
        btnReady.setEnabled(false);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = rating;
                if(rating > 0.0) {
                    btnReady.setEnabled(true);
                    btnReady.setAlpha(1f);
                    textShowRating.setVisibility(View.VISIBLE);
                }
                else {
                    btnReady.setEnabled(false);
                    btnReady.setAlpha(.5f);
                }
                textShowRating.setText(Services.verifyQualification(rating));
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResultsActivity.this,
                        String.valueOf(Services.verifyQualification(ratingBar.getRating())),
                        Toast.LENGTH_SHORT).show();
                linearRating.setVisibility(View.GONE);
                saveTest();
                Services.messageAlert(ResultsActivity.this, "Mensagem","Os resultados foram salvos!", "DialogSaveResults");
            }
        });
    }

    private void saveTest(){
        DatabaseHelper db = new DatabaseHelper(ResultsActivity.this);
        db.openDataBase();
        String values = editFirstResult.getText().toString() + "|" + editSecondResult.getText().toString();
        Tests test = new Tests(
                UUID.randomUUID().toString(),
                AllActivities.testSelected,
                idAthlete,
                values,
                Services.verifyQualification(ratingValue));

        db.addTest(test);
    }

    private void checkAndSaveResults (){
        if(contResults==0){
            editFirstResult.setEnabled(true);
            editSecondResult.setEnabled(false);
            enabledButtonAdd(false);
        }
        else if(contResults ==1 ) {
            editFirstResult.setEnabled(false);
            editSecondResult.setEnabled(true);
            enabledButtonAdd(false);

        }
        else if(contResults==2){
            editFirstResult.setEnabled(false);
            editSecondResult.setEnabled(false);
            buttonAdd.setText("Salvar os resultados");
            buttonAdd.setOnClickListener(saveAll);
            enabledButtonAdd(true);
        }
    }

    private void enabledButtonAdd(boolean enabled){
        if(enabled) {
            buttonAdd.setEnabled(true);
            buttonAdd.setAlpha(1f);
        }else {
            buttonAdd.setEnabled(false);
            buttonAdd.setAlpha(.5f);
        }
    }

    private void messageOption(String title, String message, String method){
        new MessageOptions(ResultsActivity.this, title, message, method);
    }

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

    private void exitActivity(){
        if(contResults>0)
            messageOption("Sair", "Tem certeza que deseja sair e abandonar o teste?","exitActivity");
        else
            finish();
    }

    public View.OnClickListener clickSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            messageOption("Salvar", "Deseja salvar o resultado?", "checkAndSaveResults");
        }
    };

    private View.OnClickListener saveAll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            messageOption("Salvar", "Certeza que deseja salvar os resultados?","saveAllResults");
        }
    };

    public static void finished(Activity act){
        ((ResultsActivity)act).finish();
    }
}


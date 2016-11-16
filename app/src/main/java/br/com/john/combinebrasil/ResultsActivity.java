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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class ResultsActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button buttonAdd;
    EditText editFirstResult, editSecondResult;
    int contResults=0;
    TextView textNamePlayer, textNameTest, textNameTestDetails, textDetailsTest, textNamePlayerDetails, textDetailsPlayer;
    ImageView imgArrowTest, imgArrowPlayer;
    boolean arrowDownTest=true,arrowDownPlayer=true;

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
        editFirstResult = (EditText) findViewById(R.id.edit_first_result);
        editSecondResult = (EditText) findViewById(R.id.edit_second_result);
        buttonAdd = (Button) findViewById(R.id.button_add_results);

        textNamePlayer = (TextView) findViewById(R.id.text_name_player_result);
        textNameTest = (TextView) findViewById(R.id.text_name_test_result);
        textNameTestDetails = (TextView) findViewById(R.id.text_name_test_details);
        textDetailsTest = (TextView) findViewById(R.id.text_details_test);
        textNamePlayerDetails = (TextView) findViewById(R.id.text_name_player_details);
        textDetailsPlayer = (TextView) findViewById(R.id.text_details_player);

        imgArrowPlayer = (ImageView) findViewById(R.id.img_player_arrow);
        imgArrowTest = (ImageView) findViewById(R.id.img_test_arrow);
        imgArrowTest.setOnClickListener(clickedImgArrowTest);
        imgArrowPlayer.setOnClickListener(clickedImgArrowPlayer);

        buttonAdd.setOnClickListener(clickSave);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            checkAndSaveResults();

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
            Services.messageAlert(ResultsActivity.this, "Mensagem","Os resultados foram salvos!", "DialogSaveResults");
        }
        else if(method.equals("exitActivity")){
            finish();
        }
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


package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ResultsActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button buttonAdd;
    EditText editFirstResult, editSecondResult;
    int contResults=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        editFirstResult = (EditText) findViewById(R.id.edit_first_result);
        editSecondResult = (EditText) findViewById(R.id.edit_second_result);
        buttonAdd = (Button) findViewById(R.id.button_add_results);

        buttonAdd.setOnClickListener(clickSave);

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

    @Override
    public void onBackPressed(){
        finish();
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener clickSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            message("Salvar", "Deseja salvar o resultado?");
        }
    };

    public void message(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Sim", dialogClickListener);
        builder.setNegativeButton("Não", dialogClickListener);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    contResults= contResults+1;
                    checkAndSaveResults();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

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
}


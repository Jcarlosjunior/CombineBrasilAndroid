package br.com.john.combinebrasil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Services.DatabaseHelper;

public class DetailsAthletes extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_athletes);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            showInfoAthlete(extras.getString("id_player"));

        }


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

    private void showInfoAthlete(String id){
        DatabaseHelper db  = new DatabaseHelper(DetailsAthletes.this);
        db.openDataBase();
        Athletes athlete = db.getAthleteById(id);

        TextView text = (TextView) findViewById(R.id.text_details_athlete);

        text.setText("Nascimento: "+ athlete.getBirthday()+ "\n"+
                     "CPF: "+athlete.getCPF() +"\n"+
                     "Altura: "+athlete.getHeight() +"\n"+
                     "Peso: "+athlete.getWeight());



    }
}

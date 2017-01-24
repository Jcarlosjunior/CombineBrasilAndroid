package br.com.john.combinebrasil;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

public class DetailsAthletes extends AppCompatActivity {
    Toolbar toolbar;
    TextView textName;
    Button buttonEdit;
    Bundle extras;
    String idAthlete;

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

        textName = (TextView) findViewById(R.id.text_name_details);
        buttonEdit = (Button) findViewById(R.id.button_edit);
        buttonEdit.setOnClickListener(clickEditAthlete);

        DatabaseHelper db = new DatabaseHelper(DetailsAthletes.this);
        User user = db.getUser();
        if(user!=null){
            if(user.getIsAdmin())
                buttonEdit.setVisibility(View.VISIBLE);
            else
                buttonEdit.setVisibility(View.GONE);
        }
        extras = getIntent().getExtras();
        if(extras != null) {
            idAthlete = extras.getString("id_player");
            showInfoAthlete(idAthlete);
        }


    }
    @Override
    public void onRestart(){
        super.onRestart();
        showInfoAthlete(idAthlete);
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

        Positions positiom = db.getPositiomById(athlete.getDesirablePosition());

        textName.setText(athlete.getName());
        String pos = "";
        if(positiom!=null){
            pos = positiom.getNAME();
        }
        else
            pos = athlete.getDesirablePosition();
        text.setText("Nascimento: "+ Services.convertDate(athlete.getBirthday())+ "\n"+
                     "CPF: "+athlete.getCPF() +"\n"+
                     "Endereço: "+athlete.getAddress() +"\n"+
                     "Posição Desejada: "+pos +"\n"+
                     "Altura: "+String.format("%.2f", athlete.getHeight()).replace(".",",") +"\n"+
                     "Peso: "+String.format("%.0f",athlete.getWeight()).replace(".",",")+" Kg");
    }

    private View.OnClickListener clickEditAthlete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editAthlete();
        }
    };

    private void editAthlete(){
        Intent intent = new Intent(DetailsAthletes.this, CreateAccountAthlete.class);
        intent.putExtra("EditAthlete", true);
        intent.putExtra("id_player",extras.getString("id_player"));
        startActivity(intent);

    }
}

package br.com.john.combinebrasil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Services.DatabaseHelper;

public class HistoricPlayerActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String idAthlete, idSelective;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_player);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(clickedBack);
        LinearLayout btnMenu = (LinearLayout) findViewById(R.id.linear_menu_button);
        btnMenu.setVisibility(View.GONE);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setImageDrawable(this.getDrawable(R.drawable.ic_filter_list_white_24dp));
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText(R.string.players);

        Bundle extras = getIntent().getExtras();
        if(extras !=null){
            idAthlete = extras.getString("id_athlete");
            idSelective = extras.getString("id_selective");
            callInfoSelectivePlayer();
        }
    }

    private void callInfoSelectivePlayer(){
        DatabaseHelper db = new DatabaseHelper(this);
        Athletes athlete = db.getAthleteById(idAthlete);

    }

    private View.OnClickListener clickedBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}

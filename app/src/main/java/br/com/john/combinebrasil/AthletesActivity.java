package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterListAthletes;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.DatabaseHelper;

public class AthletesActivity extends AppCompatActivity {
    ListView listViewPlayers;
    Toolbar toolbar;
    ArrayList<Athletes> playersArrayList;
    private static Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        listViewPlayers = (ListView) findViewById(R.id.list_players);

        myContext = AthletesActivity.this;
        callInflateAthletes();
    }

    public static void afterSyncAthletes(Activity act){
        ((AthletesActivity) act).callInflateAthletes();
    }

    private void callInflateAthletes(){
        DatabaseHelper db = new DatabaseHelper(myContext);
        db.openDataBase();
        ArrayList<Athletes> athletesList = db.getAthletes();
        if(!(athletesList == null || athletesList.size()==0)){
            String[] values = new String[athletesList.size()];
            for(int i=0; i <=athletesList.size()-1; i++){
                values[i] = athletesList.get(i).getId();
            }
            inflateRecyclerView(athletesList, values);
        }
    }
    private void inflateRecyclerView(ArrayList<Athletes> testsArrayList, String[] values){
        AdapterListAthletes adapterTests = new AdapterListAthletes(this, values, testsArrayList);
        adapterTests.setActivity(this);
        listViewPlayers.setVisibility(View.VISIBLE);
        listViewPlayers.setAdapter(adapterTests);
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    }

    public static void onClickItemList(Activity activity, int positionArray){
        ((AthletesActivity) activity).validaClick(positionArray);
    }
    public void validaClick(int position){
        Intent i;
        if(AllActivities.type.equals("corrida"))
            i = new Intent(AthletesActivity.this, CronometerActivity.class);
        else
            i = new Intent(AthletesActivity.this, ResultsActivity.class);
        Athletes player  = playersArrayList.get(position);
        i.putExtra("id_player",player.getId());
        i.putExtra("name_player",player.getName());
        startActivity(i);
    }


}

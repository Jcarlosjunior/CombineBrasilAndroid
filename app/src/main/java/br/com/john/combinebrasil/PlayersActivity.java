package br.com.john.combinebrasil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterListPlayers;
import br.com.john.combinebrasil.Classes.Players;

public class PlayersActivity extends AppCompatActivity {
    ListView listViewPlayers;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        listViewPlayers = (ListView) findViewById(R.id.list_players);

        callAllFalseTests();
    }

    private void callAllFalseTests(){
        ArrayList<Players> testsArrayList = new ArrayList<Players>();
        String[] values = new String[12];
        for(int i=0; i<=11; i++){
            Players player = new Players();
            player.setId(String.valueOf(i));
            player.setName("Jogador "+i);
            player.setAge("Idade "+i);
            player.setDetails("descrição "+i);
            player.setIdSelective("");
            testsArrayList.add(player);
            values[i]="Player "+i;
        }

        inflateRecyclerView(testsArrayList, values);
    }

    private void inflateRecyclerView(ArrayList<Players> testsArrayList, String[] values){
        AdapterListPlayers adapterTests = new AdapterListPlayers(this, values, testsArrayList);
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


}

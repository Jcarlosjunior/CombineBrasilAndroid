package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterListAthletes;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerAthletes;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerAthletesInfo;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerSelectives;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAsyncTask;
import br.com.john.combinebrasil.Connection.Posts.PostLogin;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

public class HistoricPlayersSelectiveActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerPlayers;
    EditText editSearch;
    AdapterRecyclerAthletesInfo adapterListAthletes;
    String idSelective ="";
    ArrayList<Athletes> athletes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_players_selective);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(clickedBack);
        LinearLayout btnMenu = (LinearLayout) findViewById(R.id.linear_menu_button);
        btnMenu.setVisibility(View.GONE);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        linearAddAccount.setOnClickListener(clickedFilter);
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText(R.string.players);

        recyclerPlayers = (RecyclerView) findViewById(R.id.recycler_players_historic);
        editSearch = (EditText) findViewById(R.id.edit_search);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idSelective = Constants.debug ? "589e11e401bf3d0011a30962" : extras.getString("id_selective");
            callListPlayers();
        }
    }

    private void callListPlayers(){
        ConstraintLayout constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        TextView textView =(TextView)findViewById(R.id.text_progress);
        textView.setText("Verificando Atletas");

        if(Services.isOnline(HistoricPlayersSelectiveActivity.this)) {
            constraintProgress.setVisibility(View.VISIBLE);

            String url = Constants.URL + Constants.API_SELECTIVE_ATHLETES_SEARCH;
            PostAsyncTask post = new PostAsyncTask();
            post.setActivity(HistoricPlayersSelectiveActivity.this);
            post.setObjPut(querySearchData());
            post.setWhoCalled(Constants.CALLED_GET_ATHLETES);
            post.execute(url);
        }
        else{
            showNotConnection();
        }
    }

    private JSONObject querySearchData() {
        JSONObject object = new JSONObject();
        try {
            JSONObject jsonQuery = new JSONObject();
            jsonQuery.put(Constants.SELECTIVEATHLETES_SELECTIVE, idSelective);
            object.put("query", jsonQuery);
            JSONArray jsonDates = new JSONArray();
            jsonDates.put(Constants.SELECTIVEATHLETES_ATHLETE);
            object.put("populate", jsonDates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void returnGetPlayers(Activity act, String response, int status){
        ((HistoricPlayersSelectiveActivity)act).returnGetPlayers(response, status);
    }

    private void returnGetPlayers(String response, int status){
        ConstraintLayout constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraintProgress.setVisibility(View.GONE);
        if(status == 200 || status == 201){
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            athletes = des.getAthletesInSelective();
            if(athletes.size()>0){
                recordingSelectives(athletes);
            }
        }
    }

    private void recordingSelectives(ArrayList<Athletes> athletes){
        DatabaseHelper db = new DatabaseHelper(HistoricPlayersSelectiveActivity.this);
        try {
            db.createDataBase();
            if(athletes!=null) {
                db.openDataBase();
                db.deleteTable(Constants.TABLE_ATHLETES);
                db.addAthletes(athletes);
                inflateAtheltes(athletes);
            }
        } catch (Exception e) {}
    }

    private void inflateAtheltes(ArrayList<Athletes> athletes){
        if(!(athletes == null || athletes.size()==0)){
            String[] values = new String[athletes.size()];
            for(int i=0; i <=athletes.size()-1; i++){
                values[i] = athletes.get(i).getId();
            }
            inflateRecycler(athletes, values);
        }
    }

    private void inflateRecycler(ArrayList<Athletes> athletes, String[] ids){
        recyclerPlayers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterListAthletes = new AdapterRecyclerAthletesInfo(HistoricPlayersSelectiveActivity.this, athletes, ids);
        recyclerPlayers.setVisibility(View.VISIBLE);
        recyclerPlayers.setAdapter(adapterListAthletes);
    }

    private void showNotConnection(){
        ConstraintLayout constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraintProgress.setVisibility(View.GONE);
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        Button button = (Button) findViewById(R.id.btn_try_again_connect);
        constraint.setVisibility(View.VISIBLE);
        button.setOnClickListener(clickedTryAgain);
    }

    private View.OnClickListener clickedTryAgain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
            constraint.setVisibility(View.GONE);
            callListPlayers();
        }
    };

    private View.OnClickListener clickedBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener clickedFilter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showFilter();
        }
    };

    private void showFilter(){
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_filter);
        constraint.setVisibility(View.VISIBLE);
    }

    public static void onClickListItem(Activity act, int position){
        ((HistoricPlayersSelectiveActivity)act).onClickListItem(position);
    }

    private void onClickListItem(int position){
        Athletes athlete = athletes.get(position);
        Intent intent = new Intent(this, HistoricPlayerActivity.class);
        HistoricPlayerActivity.athlete = athlete;
        intent.putExtra("id_athlete", athlete.getId());
        intent.putExtra("id_selective", idSelective);
        startActivity(intent);
    }
}

package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerSync;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerSyncAthlete;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostSync;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

public class SyncAthleteActivity extends AppCompatActivity {
    private RecyclerView recyclerSync;
    private LinearLayout linearProgress;
    TextView textProgress;
    private static boolean syncAll = false;
    ArrayList<Athletes> athletes;
    ArrayList<Tests> tests;
    int positionNow = 0;
    long numAthletes = 0;
    AdapterRecyclerSyncAthlete adapterSync;
    String idTest, idAthlete;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_athlete);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);

        recyclerSync = (RecyclerView) findViewById(R.id.recycler_sync_athlete);
        recyclerSync.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);
        textProgress = (TextView) findViewById(R.id.text_progress);

        fab = (FloatingActionButton) findViewById(R.id.fab_upload_sync_athlete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSynAll();
            }
        });

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            idTest = extras.getString("testSelect");

            DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
            this.tests= db.getTestsFromType(idTest);

            numAthletes = db.getCountTable(Constants.TABLE_ATHLETES);

            TestTypes test = db.getTestTypeFromId(idTest);
            textTitle.setText(test.getName());

            callInflateList();

            if(numAthletes==db.getCountTestSync(idTest)){
                fab.setVisibility(View.GONE);
            }
        }
    }

    private void callInflateList() {
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);

        athletes = db.getAthletesByTests(idTest);
        try {
            if (athletes != null || athletes.size() > 0)
                inflateRecyclerView(athletes);
        }catch (Exception e){};
    }

    private void inflateRecyclerView(ArrayList<Athletes> athletes) {
        String[] values = new String[athletes.size()];
        for (int i = 0; i <= athletes.size() - 1; i++)
            values[i] = athletes.get(i).getId();
        adapterSync = new AdapterRecyclerSyncAthlete(SyncAthleteActivity.this, athletes, values, idTest);
        recyclerSync.setVisibility(View.VISIBLE);
        recyclerSync.setAdapter(adapterSync);
    }
    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public static void onCLickSyncAthlete(Activity act, int position){
        ((SyncAthleteActivity)act).onClickSyncAthlete(position);
    }
    private void onClickSyncAthlete(int position){
        positionNow = position;

        syncAll = false;
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        Tests test = db.getTestFromAthleteAndType(athletes.get(position).getId(), idTest);

        sync(test);
    }

    private void callSynAll(){
        if(Services.isOnline(SyncAthleteActivity.this)){
            syncAll();
        }
        else{
            Services.messageAlert(SyncAthleteActivity.this, "Aviso","Para começar a sincronização, você precisa estar conectado em uma rede Wi-Fi.","");
        }
    }

    private void syncAll(){
        syncAll = true;
        if(positionNow<numAthletes){
            sync(tests.get(positionNow));
        }
        else
            Services.messageAlert(SyncAthleteActivity.this, "Aviso","Todos os testes foram salvos!","");
    }

    private void sync(Tests test){
        linearProgress.setVisibility(View.VISIBLE);
        textProgress.setText("Sincronizando");

        if(Services.isOnline(SyncAthleteActivity.this)) {
            DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
            Athletes athlete  = db.getAthleteById(test.getAthlete());
            if(athlete!=null){
                if(athlete.getSync()){
                    String url = Constants.URL + Constants.API_TESTS;

                    if (!Services.convertIntInBool(test.getSync())) {
                        PostSync post = new PostSync();
                        post.setActivity(SyncAthleteActivity.this);
                        post.setAll(syncAll);
                        post.setObjPut(createObject(test));
                        post.execute(url);
                    } else {
                        positionNow = positionNow + 1;
                        syncAll();
                    }
                }
                else
                    callUpdateAthlete(athlete);

            }
        }
        else
            Services.messageAlert(SyncAthleteActivity.this, "Aviso","Para começar a sincronização, você precisa ter uma conexão com a internet.","");
    }

    private JSONObject createObject(Tests test) {
        JSONObject object = new JSONObject();

        try {
            object.put(Constants.TESTS_ATHLETE, test.getAthlete());
            object.put(Constants.TESTS_SELECTIVE, test.getSelective());
            object.put(Constants.TESTS_TYPE, test.getType());
            object.put(Constants.TESTS_FIRST_VALUE, test.getFirstValue());
            object.put(Constants.TESTS_SECOND_VALUE, test.getSecondValue());
            object.put(Constants.TESTS_RATING, test.getRating());
            object.put(Constants.TESTS_WINGSPAN, test.getWingspan());
            object.put(Constants.TESTS_USER, test.getUser());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void returnPostTest(Activity act, String resp, String result){
        ((SyncAthleteActivity)act).returnPostTest(resp, result);
    }

    private void returnPostTest(String resp, String result){
        linearProgress.setVisibility(View.GONE);
        result = result.replaceAll("Value","");

        Log.i("ERROR", result);

        if(syncAll){
            if(resp.equals("OK")) {
                DeserializerJsonElements des = new DeserializerJsonElements(result);
                Tests test = des.getTestObject();
                updateTest(test.getId());
                positionNow = positionNow + 1;
                callSynAll();
            }
        }
        else{
            if(resp.equals("OK")){
                DeserializerJsonElements des = new DeserializerJsonElements(result);
                Tests test = des.getTestObject();
                updateTest(test.getId());
            }
            else
                Services.messageAlert(SyncAthleteActivity.this, "Aviso","Erro ao tentar sincronizar teste.","");
        }
    }

    private void updateTest(String id){
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        db.updateSync(athletes.get(positionNow).getId(), idTest, id);

        adapterSync.notifyItemChanged(positionNow);

        if(numAthletes==db.getCountTestSync(idTest)){
            fab.setVisibility(View.GONE);
        }
    }

    /**************************************************UPDATE ATHLETE NOT SYNC******************************/
    private void callUpdateAthlete(Athletes athlete){
        if(Services.isOnline(SyncAthleteActivity.this)){
            updateAthlete(athlete);
        }
        else{
            Services.messageAlert(SyncAthleteActivity.this, "Aviso","Para começar a sincronização, você precisa estar conectado em uma rede Wi-Fi.","");
        }
    }

    private void updateAthlete(Athletes athlete){
        idAthlete = athlete.getId();
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        SelectiveAthletes selectiveAthletes = db.getSelectiveAthletesFromAthlete(athlete.getId());
        if(selectiveAthletes!=null){
            linearProgress.setVisibility(View.VISIBLE);
            String url = Constants.URL + Constants.API_SELECTIVEATHLETES+"?"+Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER+"="+selectiveAthletes.getInscriptionNumber();
            callFunc(url, "UPDATE_SELECTIVEATHLETE",  false);
        }
    }

    private void callFunc(String url, String methodName, boolean isPost) {
        int methodType = isPost ? 1 : 0;
        Log.i("Sync DataBase", methodName + "\n\n "+url);
        Connection task = new Connection(url, methodType, methodName, false, SyncAthleteActivity.this);
        task.callByJsonStringRequest();
    }

    public static void updateSelectiveAthlete(Activity act, String response){
        ((SyncAthleteActivity)act).updateSelectiveAthlete(response);
    }

    private void updateSelectiveAthlete(String response){
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        SelectiveAthletes selectiveAthlete = des.getSelectiveAthlete();

        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        db.updateSelectiveAthlete(selectiveAthlete);

        String url = Constants.URL + Constants.API_ATHLETES+"?"+Constants.ATHLETES_ID+"="+selectiveAthlete.getAthlete();
        callFunc(url, "UPDATE_ATHLETE",  false);
    }

    public static void updateAthlete(Activity act, String response){
        ((SyncAthleteActivity)act).updateAthlete(response);
    }

    private void updateAthlete(String response){
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        Athletes athlete = des.getAthlete();

        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        db.updateAthlete(athlete);
        db.updateTestsAthlete(idAthlete, athlete.getId());
        linearProgress.setVisibility(View.GONE);

        Tests test = db.getTestFromAthleteAndType(athletes.get(positionNow).getId(), idTest);
        sync(test);
    }


}

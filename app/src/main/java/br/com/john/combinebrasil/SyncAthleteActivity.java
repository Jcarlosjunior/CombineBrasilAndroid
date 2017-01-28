package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.database.SQLException;
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
import org.junit.Test;

import java.io.IOException;
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
import br.com.john.combinebrasil.Services.SyncDatabase;

public class SyncAthleteActivity extends AppCompatActivity {
    private RecyclerView recyclerSync;
    private LinearLayout linearProgress;
    TextView textProgress;
    private static boolean syncAll = false;
    ArrayList<Athletes> athletes;
    ArrayList<Tests> tests;
    int positionNow = 0;
    long numAthletes = 0;
    int positionSelected = 0;
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
            positionSelected = extras.getInt("positionSelected");

            DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
            this.tests= db.getTestsFromType(idTest);

            numAthletes = db.getCountTable(Constants.TABLE_ATHLETES);

            TestTypes test = db.getTestTypeFromId(idTest);
            textTitle.setText(test.getName());

            callInflateList();

            verifySync();
        }
    }

    private void verifySync(){
        int cont  = 0;
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        ArrayList<Tests> tests = db.getTestsFromType(idTest);
        for(Tests test : tests){
            if(!Services.convertIntInBool(test.getSync())) {
                cont = 1;
                fab.setVisibility(View.VISIBLE);
                break;
            }
        }
        if(cont == 0){
            fab.setVisibility(View.GONE);
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
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        if(positionNow<athletes.size()){
            sync(tests.get(positionNow));
        }
        else {
            Services.messageAlert(SyncAthleteActivity.this, "Aviso","Nada mais a sincronizar","");
            linearProgress.setVisibility(View.GONE);

            /*linearProgress.setVisibility(View.VISIBLE);
            Connection task = new Connection(Constants.URL + Constants.API_TESTS, 0, Constants.CALLED_GET_TESTS, false, SyncAthleteActivity.this);
            task.callByJsonStringRequest();*/

        }
    }

    public static void testResponse(Activity act,String response) {
        ((SyncAthleteActivity)act).testResponse(response);
    }
    private void testResponse(String response){
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<Tests> test = des.getTest();
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(SyncAthleteActivity.this, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
            db.addTests(test);
            db.close();
            Connection task = new Connection(Constants.URL + Constants.API_ATHLETES, 0, Constants.CALLED_GET_ATHLETES, false, SyncAthleteActivity.this);
            task.callByJsonStringRequest();
        } catch (SQLException sqle) {
            Services.messageAlert(SyncAthleteActivity.this, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(SyncAthleteActivity.this.getClass().getSimpleName());
            throw sqle;
        }
    }

    public static void athletesResponse(Activity act, String response){
        ((SyncAthleteActivity)act).athletesResponse(response);
    }
    private void athletesResponse(String response) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        ArrayList<Athletes> athletesList = des.getAthletes();
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            Services.messageAlert(SyncAthleteActivity.this, "Mensagem", "Unable to create database", "");
            throw new Error("Unable to create database");
        }
        try {
            ArrayList<Athletes> athletesAdd = new ArrayList<Athletes>();
            for(int i=0; i<=athletesList.size()-1; i++){
                SelectiveAthletes item = db.getSelectiveAthletesFromAthlete(athletesList.get(i).getId());
                if(item!=null){
                    athletesList.get(i).setCode(item.getInscriptionNumber());
                    athletesAdd.add(athletesList.get(i));
                }
            }
            db.openDataBase();
            db.addAthletes(athletesAdd);
            db.close();
            Services.messageAlert(SyncAthleteActivity.this, "Aviso", "Todos os testes foram salvos!", "");
            linearProgress.setVisibility(View.GONE);
        } catch (SQLException sqle) {
            Services.messageAlert(SyncAthleteActivity.this, "Mensagem", sqle.getMessage(), "");
            SyncDatabase.hideProgress(SyncAthleteActivity.this.getClass().getSimpleName());
            throw sqle;
        }
    }

    private void sync(Tests test){
        if(Services.isOnline(SyncAthleteActivity.this)) {
            linearProgress.setVisibility(View.VISIBLE);
            textProgress.setText("Sincronizando");
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
                        if(syncAll){
                            positionNow = positionNow + 1;
                            syncAll();
                        }
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
            else{
                updateTestExist(result);
            }

        }
        else{
            if(resp.equals("OK")){
                DeserializerJsonElements des = new DeserializerJsonElements(result);
                Tests test = des.getTestObject();
                updateTest(test.getId());
            }
            else
                updateTestExist(result);
        }
    }

    private void updateTestExist(String result){
        try {
            linearProgress.setVisibility(View.VISIBLE);
            JSONObject json = new JSONObject(result);
            String detail = json.getString("detail");
            json = new JSONObject(detail);
            if(json.getInt("code") ==  11000){
                String update = json.getString("op");
                json = new JSONObject(update);
                String url = Constants.URL+Constants.API_TESTS+"?"+Constants.TESTS_ATHLETE+"="+
                        json.getString(Constants.TESTS_ATHLETE)+"&"+
                        Constants.TESTS_TYPE+"="+json.getString(Constants.TESTS_TYPE);
                Connection task = new Connection(url, 0, "UPDATE_TEST", false, SyncAthleteActivity.this);
                task.callByJsonStringRequest();
            }
            else{
                linearProgress.setVisibility(View.GONE);
                Services.messageAlert(SyncAthleteActivity.this, "Aviso","Erro ao tentar sincronizar teste.","");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void returnUpdateSync(Activity act, String response){
        ((SyncAthleteActivity)act).returnUpdateSync(response);
    }

    private void returnUpdateSync(String response) {
        linearProgress.setVisibility(View.GONE);
        if (!response.equals("[]")) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            Tests test = des.getTestObjectTest();
            if (test != null) {
                updateTest(test.getId());
                if (syncAll) {
                    positionNow = positionNow + 1;
                    callSynAll();
                }
            } else
                Services.messageAlert(SyncAthleteActivity.this, "Aviso", "Erro ao tentar sincronizar teste.", "");
        }
        else
            Services.messageAlert(SyncAthleteActivity.this, "Aviso", "Erro ao tentar sincronizar teste.", "");

    }

    private void updateTest(String id){
        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
        db.updateSync(athletes.get(positionNow).getId(), idTest, id);

        adapterSync.notifyItemChanged(positionNow);
        try {
            SyncActivity.adapterTests.notifyItemChanged(positionSelected);
        }catch (Exception e){
            e.printStackTrace();
        }
        verifySync();
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
        if(selectiveAthletes==null){
            positionNow = positionNow+1;

            if(athletes.size()<positionNow) {
                Tests test = db.getTestFromAthleteAndType(athletes.get(positionNow).getId(), idTest);
                sync(test);
            }
            else{
                Services.messageAlert(SyncAthleteActivity.this, "Aviso","Nada mais a sincronizar","");
                linearProgress.setVisibility(View.GONE);
            }
        }
        else {
            linearProgress.setVisibility(View.VISIBLE);
            String url = Constants.URL + Constants.API_SELECTIVEATHLETES+"?"+Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER+"="+selectiveAthletes.getInscriptionNumber();
            SyncDatabase.callFunc(url, "UPDATE_SELECTIVEATHLETE",  false, SyncAthleteActivity.this);
        }
    }

    public static void updateSelectiveAthlete(Activity act, String status, String response){
        ((SyncAthleteActivity)act).updateSelectiveAthlete(status, response);
    }

    private void updateSelectiveAthlete(String status, String response){
        if(status.equals("OK")){
            if(!response.equals("[]")){
                DeserializerJsonElements des = new DeserializerJsonElements(response);
                SelectiveAthletes selectiveAthlete = des.getSelectiveAthlete();
                if(selectiveAthlete!=null){
                    DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);
                    db.updateSelectiveAthlete(selectiveAthlete);

                    String url = Constants.URL + Constants.API_ATHLETES + "?" + Constants.ATHLETES_ID + "=" + selectiveAthlete.getAthlete();
                    SyncDatabase.callFunc(url, "UPDATE_ATHLETE", false, SyncAthleteActivity.this);
                }
            }
        }
        else{
            if(syncAll){
                positionNow = positionNow + 1;
                syncAll();
            }
            else
                Services.messageAlert(SyncAthleteActivity.this, "Aviso","O atleta ainda não foi cadastrado na API.","");
        }

    }

    public static void updateAthlete(Activity act, String response){
        ((SyncAthleteActivity)act).updateAthlete(response);
    }

    private void updateAthlete(String response){
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        Athletes athlete = des.getAthlete();

        DatabaseHelper db = new DatabaseHelper(SyncAthleteActivity.this);

        SelectiveAthletes selectiveAthletes = db.getSelectiveAthletesFromAthlete(athlete.getId());
        athlete.setCode(selectiveAthletes.getInscriptionNumber());

        db.updateAthlete(athlete);
        db.updateTestsAthlete(idAthlete, athlete.getId());
        linearProgress.setVisibility(View.GONE);

        athletes.set(positionNow, athlete);
        Tests test = db.getTestFromAthleteAndType(athletes.get(positionNow).getId(), idTest);
        sync(test);
    }


}

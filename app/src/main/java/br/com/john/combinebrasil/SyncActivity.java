package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerSync;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAthleteAsyncTask;
import br.com.john.combinebrasil.Connection.Posts.PostSync;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class SyncActivity extends AppCompatActivity {
    private RecyclerView recyclerSync;
    LinearLayout linearProgress, linearAddAthlete;
    Button btnSaveAthletes;
    ImageView imgClose;
    TextView textSave;
    private static boolean isSync = false;
    ArrayList<TestTypes> testsInflate;
    ArrayList<Tests> tests;
    ArrayList<Athletes> athletes;
    String idAthleteChange;
    int positionNow = 0, positionSync, positionAthlete=0, positionSaveAthletes = 0;
    long numTests=0, numAthletes=0;
    public static AdapterRecyclerSync adapterTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText("Sincronizar os testes");

        linearAddAthlete = (LinearLayout) findViewById(R.id.linear_update_Athletes);
        btnSaveAthletes = (Button) findViewById(R.id.button_save_athlete);
        imgClose = (ImageView) findViewById(R.id.image_close) ;
        imgClose.setOnClickListener(closeAdd);
        textSave = (TextView) findViewById(R.id.text_message_save_athletes);

        btnSaveAthletes.setOnClickListener(saveAthletes);

        recyclerSync= (RecyclerView) findViewById(R.id.recycler_sync);
        recyclerSync.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_upload_sync);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSynAll();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        tests = new ArrayList<Tests>();

        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        numAthletes = db.getCountTable(Constants.TABLE_ATHLETES);

        callInflateList();

        verifyAthletes();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        verifyAthletes();
    }

    private void verifyAthletes() {
        linearAddAthlete.setVisibility(View.GONE);
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        User user = db.getUser();
        if(user.getIsAdmin()) {
            if (Services.isOnline(SyncActivity.this)) {
                ArrayList<Athletes> athletes = db.getAthletes();

                this.athletes = new ArrayList<Athletes>();
                if (athletes == null) {
                    linearAddAthlete.setVisibility(View.GONE);
                } else {
                    int cont = 0;
                    for (Athletes athlete : athletes) {
                        if (athlete.getSync() == false) {
                            cont = cont + 1;
                            this.athletes.add(athlete);
                        }
                    }
                    if (cont > 0) {
                        textSave.setText("Opa! Você cadastrou " + cont + " atletas na seletiva, porém você estava sem conexão com a internet, e por esse motivo, os atletas não foram salvos. Agora você está com conexão e pode salvo-los.");
                        linearAddAthlete.setVisibility(View.VISIBLE);
                    } else
                        linearAddAthlete.setVisibility(View.GONE);
                }
            }
            else
                linearAddAthlete.setVisibility(View.GONE);
        }
        else
            linearAddAthlete.setVisibility(View.GONE);
    }

    private View.OnClickListener saveAthletes = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveAthletes();
        }
    };

    private JSONObject createObject(Athletes athlete) {
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.ATHLETES_NAME, athlete.getName());
            object.put(Constants.ATHLETES_CPF, athlete.getCPF());
            object.put(Constants.ATHLETES_PHONE, athlete.getPhoneNumber());
            object.put(Constants.ATHLETES_DESIRABLE_POSITION, athlete.getDesirablePosition());
            object.put(Constants.ATHLETES_HEIGHT, athlete.getHeight());
            object.put(Constants.ATHLETES_WEIGHT, athlete.getWeight());
            object.put(Constants.ATHLETES_BIRTHDAY, athlete.getBirthday());
            object.put(Constants.ATHLETES_TERMSACCEPTED, athlete.getTermsAccepted());
            object.put(Constants.ATHLETES_EMAIL, athlete.getEmail());
            if(athlete.getAddress().toString().trim().isEmpty())
                object.put(Constants.ATHLETES_ADDRESS, " ");
            else
                object.put(Constants.ATHLETES_ADDRESS, athlete.getAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void saveAthletes(){
        if(positionSaveAthletes<athletes.size()) {
            String url = Constants.URL + Constants.API_ATHLETES;
            idAthleteChange = athletes.get(positionSaveAthletes).getId();
            linearProgress.setVisibility(View.VISIBLE);

            PostAthleteAsyncTask post = new PostAthleteAsyncTask();
            post.setActivity(SyncActivity.this);
            post.setObjPut(createObject(athletes.get(positionSaveAthletes)));
            post.setPlay(true);
            post.execute(url);
        }
        else {
            Services.messageAlert(SyncActivity.this, "Concluído", "Sem mais Atletas para salvar", "");
            linearAddAthlete.setVisibility(View.GONE);
        }
    }

    public static void afterSendAthlete(Activity act, String ret, String response){
        ((SyncActivity)act).afterPost(ret, response);
    }

    private void afterPost(String ret, String response){
        linearProgress.setVisibility(View.GONE);
        if(ret.equals("FAIL"))
            verifyErrorCreate(response);

        else if(ret.equals("OK"))
            saveAthlete(response);
    }

    private void verifyErrorCreate(String result){
        JSONObject json;
        try{
            json = new JSONObject(result);
            String detail = json.getString("detail");
            json = new JSONObject(detail);
            if(json.getInt("code") ==  11000)
                saveAthlete(json.getString("op"));
            else {
                Services.messageAlert(SyncActivity.this, "Mensagem", "Atleta não cadastrado\n" + result, "");
            }

        }catch(JSONException e){
            e.printStackTrace();
            try {
                json = new JSONObject(result);
                Services.messageAlert(SyncActivity.this, "Mensagem", "Atleta não cadastrado\n" + json.getString("message"), "");
            } catch (JSONException e1) {
                Services.messageAlert(SyncActivity.this, "Mensagem", "Atleta não cadastrado\n" + result, "");
                e1.printStackTrace();
            }
        }
    }

    private void saveAthlete(String response){
        linearProgress.setVisibility(View.GONE);

        DeserializerJsonElements des = new DeserializerJsonElements(response);
        Athletes athlete = des.getObjAthlete();
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        Athletes obj = db.getAthleteById(idAthleteChange);
        athlete.setCode(obj.getCode());
        db.updateAthlete(athlete);
        createCode(athlete.getId());
    }

    private void createCode(String idAthlete){
        linearProgress.setVisibility(View.VISIBLE);
        String url = Constants.URL + Constants.API_SELECTIVEATHLETES;

        PostAthleteAsyncTask post = new PostAthleteAsyncTask();
        post.setActivity(SyncActivity.this);
        post.setObjPut(createObjectSelectiveAthletes(idAthlete));
        post.setPlay(false);
        post.execute(url);
    }

    private JSONObject createObjectSelectiveAthletes(String athlete) {
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        SelectiveAthletes selectiveAthlete = db.getSelectiveAthletesFromAthlete(idAthleteChange);

        JSONObject object = new JSONObject();
        try {
            object.put(Constants.SELECTIVEATHLETES_ATHLETE, athlete);
            object.put(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER, selectiveAthlete.getInscriptionNumber());
            object.put(Constants.SELECTIVEATHLETES_SELECTIVE, selectiveAthlete.getSelective());
            object.put(Constants.SELECTIVEATHLETES_PRESENCE, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void afterSendSelectiveAthlete(Activity act, String response, String result){
        ((SyncActivity)act).afterSendSelectiveAthlete(response, result);
    }

    private void afterSendSelectiveAthlete(String response, String result){
        linearProgress.setVisibility(View.GONE);
        if(response.equals("FAIL")) {
            Services.messageAlert(SyncActivity.this, "Mensagem", "Atleta não cadastrado\n" + result, "");
        }
        else if(response.equals("OK")) {
            DeserializerJsonElements des = new DeserializerJsonElements(result);
            SelectiveAthletes item = des.getObjSelectiveAthlete();

            DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
            db.updateSelectiveAthlete(item);
            positionSaveAthletes = positionSaveAthletes +1;
            saveAthletes();
        }
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public void onBackPressed(){
        if(isSync){
            Services.messageAlert(SyncActivity.this, "Aviso", "Os testes estão sendo sincronizados, por favor, aguarde um momento.", "");
        }
        else
            finish();
    }

    private void callSynAll(){
        if(Services.isOnline(SyncActivity.this)){
            syncAll();
        }
        else{
            Services.messageAlert(SyncActivity.this, "Aviso","Para começar a sincronização, você precisa estar conectado em uma rede Wi-Fi.","");
        }
    }

    private void syncAll(){
        if(positionSync<numTests){
            sync();
        }
        else {
            Services.messageAlert(SyncActivity.this, "Aviso", "Todos os testes foram salvos!", "");
            linearProgress.setVisibility(View.GONE);
        }
    }

    private void sync(){
        linearProgress.setVisibility(View.VISIBLE);
        String url = Constants.URL+Constants.API_TESTS;

        if(Services.isOnline(SyncActivity.this)) {
            if (Services.convertIntInBool(tests.get(positionSync).getSync())) {
                positionSync = positionSync + 1;
                sync();
            } else {
                PostSync post = new PostSync();
                post.setActivity(SyncActivity.this);
                post.setAll(true);
                post.setSyncAcitivity(true);
                post.setObjPut(createObject(tests.get(positionSync)));
                post.execute(url);
            }
        }
        else
            Services.messageAlert(SyncActivity.this, "Aviso","Para começar a sincronização, você precisa ter uma conexão com a internet.","");

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

    public static void returnPostSync(Activity act, String resp, String result){
        ((SyncActivity)act).returnPostSync(resp,result);
    }
    private void  returnPostSync(String resp, String result){
        if(resp.equals("OK")){
            result = result.replaceAll("Value", "");
            DeserializerJsonElements des = new DeserializerJsonElements(result);
            Tests test = des.getTestObject();
            updateTest(test);
            positionSync = positionSync+1;
            callSynAll();
        }
        else
            Services.messageAlert(SyncActivity.this, "Aviso","Erro ao tentar sincronizar teste.","");
    }

    private void updateTest(Tests test){
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        db.updateSync(test.getAthlete(), test.getType(), test.getId());

        verifyUpdate();

        linearProgress.setVisibility(View.GONE);
    }

    private void verifyUpdate(){
        positionAthlete = positionAthlete +1;
        if(positionAthlete == numAthletes){
            adapterTests.notifyItemChanged(positionNow);
            positionNow=positionNow+1;
        }
    }

    private void callInflateList(){
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);

        ArrayList<TestTypes> testTypes = db.getTestsTypes();
        testsInflate = new ArrayList<TestTypes>();
        if(testTypes!=null){
            for(TestTypes test : testTypes){
                long numTestsDone = db.getCountTest(test.getId());
                if(numTestsDone > 0){
                    testsInflate.add(test);
                }
            }
            if (testsInflate!=null || testsInflate.size()>0)
                inflateRecyclerView(testsInflate);
        }
    }

    private void inflateRecyclerView(ArrayList<TestTypes> tests){
        String[] values = new String[tests.size()];
        for(int i=0; i<=tests.size()-1;i++)
            values[i] = tests.get(i).getId();
        adapterTests = new AdapterRecyclerSync(SyncActivity.this, tests, values);
        recyclerSync.setVisibility(View.VISIBLE);
        recyclerSync.setAdapter(adapterTests);
        auxSync();
    }

    private void auxSync(){
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        ArrayList<Tests> testsArrayList = new ArrayList<Tests>();
        for(TestTypes test : testsInflate){
            testsArrayList = db.getTestsFromType(test.getId());
            if(testsArrayList!=null)
            for(Tests obj : testsArrayList)
                tests.add(obj);
        }
        numTests = tests.size();
    }

    public static void onClickItemList(Activity act,int position, String id){
        ((SyncActivity)act).onClickItemList(position, id);
    }

    private void onClickItemList(int position, String id){
        Intent intent = new Intent(SyncActivity.this, SyncAthleteActivity.class);
        intent.putExtra("testSelect",id);
        intent.putExtra("positionSelected",position);
        startActivity(intent);
    }

    private View.OnClickListener closeAdd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearAddAthlete.setVisibility(View.GONE);
        }
    };


}

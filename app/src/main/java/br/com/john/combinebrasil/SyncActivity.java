package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerSync;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAthleteAsyncTask;
import br.com.john.combinebrasil.Connection.Posts.PostSync;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class SyncActivity extends AppCompatActivity {
    private RecyclerView recyclerSync;
    private static boolean isSync = false;
    ArrayList<TestTypes> testsInflate;
    ArrayList<Tests> tests;
    int positionNow = 0;
    long numAthletes=0;
    AdapterRecyclerSync adapterTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
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
        textTitle.setText("Sincronizar os testes");

        recyclerSync= (RecyclerView) findViewById(R.id.recycler_sync);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_upload_sync);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSynAll();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        numAthletes  = db.getCountTable(Constants.TABLE_ATHLETES);

        callInflateList();
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
        if(Services.isConnectectInWifi(SyncActivity.this)){
            syncAll();
        }
        else{
            Services.messageAlert(SyncActivity.this, "Aviso","Para começar a sincronização, você precisa estar conectado em uma rede Wi-Fi.","");
        }
    }

    private void syncAll(){
        TestTypes test = testsInflate.get(positionNow);
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        this.tests= db.getTestsFromType(test.getId());

        if(numAthletes<=positionNow){
            sync();
        }
        else
            Services.messageAlert(SyncActivity.this, "Aviso","Todos os testes foram salvos!","");


    }

    private void sync(){
        String url = Constants.URL+Constants.API_TESTS;

        PostSync post = new PostSync();
        post.setActivity(SyncActivity.this);
        post.setAll(true);
        post.setObjPut(createObject(tests.get(positionNow)));
        post.execute(url);
    }

    private JSONObject createObject(Tests test) {
        JSONObject object = new JSONObject();

        try {
             object.put(Constants.TESTS_ID, test.getId());
            object.put(Constants.TESTS_ATHLETE, test.getAthlete());
            object.put(Constants.TESTS_TYPE, test.getType());
            object.put(Constants.TESTS_FIRST_VALUE, test.getFirstValue());
            object.put(Constants.TESTS_SECOND_VALUE, test.getSecondValue());
            object.put(Constants.TESTS_RATING, test.getRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void returnPostSync(Activity act, String resp, String result){
        ((SyncActivity)act).returnPostSync(resp,result);
    }
    private void  returnPostSync(String resp, String result){
        DeserializerJsonElements des = new DeserializerJsonElements(result);
        Tests test = des.getTestObject();
        if(resp.equals("OK")){
            updateTest(test.getId());
            positionNow = positionNow+1;
            callSynAll();
        }
        else
            Services.messageAlert(SyncActivity.this, "Aviso","Erro ao tentar sincronizar teste.","");
    }

    private void updateTest(String idTest){
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        db.updateSync(idTest, true);

        adapterTests.notifyItemChanged(positionNow);
    }

    private void callInflateList(){
        DatabaseHelper db = new DatabaseHelper(SyncActivity.this);
        ArrayList<TestTypes> testTypes = db.getTestsTypes();
        testsInflate = new ArrayList<TestTypes>();
        if(testTypes!=null){
            for(TestTypes test : testTypes){
                long numTestsDone = db.getCountTest(test.getId());
                if(numTestsDone == numAthletes){
                    testsInflate.add(test);
                }
            }
            if (testsInflate!=null || testsInflate.size()>0)
                inflateRecyclerView(testsInflate);
        }
    }

    private void inflateRecyclerView(ArrayList<TestTypes> tests){
        Collections.sort(tests, new Comparator<TestTypes>() {
            public int compare(TestTypes v1, TestTypes v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        String[] values = new String[tests.size()];
        for(int i=0; i<=tests.size()-1;i++)
            values[i] = tests.get(i).getId();
        adapterTests = new AdapterRecyclerSync(SyncActivity.this, tests, values);
        recyclerSync.setVisibility(View.VISIBLE);
        recyclerSync.setAdapter(adapterTests);
    }

    public static void onClickItemList(Activity act,int position, String id){
        ((SyncActivity)act).onClickItemList(position, id);
    }

    private void onClickItemList(int position, String id){
        Intent intent = new Intent(SyncActivity.this, SyncAthleteActivity.class);
        intent.putExtra("testSelect",id);
        startActivity(intent);
    }

}

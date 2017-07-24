package br.com.john.combinebrasil;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerAthletesInfo;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerTests;
import br.com.john.combinebrasil.AdapterList.ExpandableRecycler.ChildItemTests;
import br.com.john.combinebrasil.AdapterList.ExpandableRecycler.ExpandableRecyclerViewAdapterTests;
import br.com.john.combinebrasil.AdapterList.ExpandableRecycler.GroupFatherTests;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAsyncTask;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Services;

public class HistoricRankingTestsActivity extends AppCompatActivity {
    ArrayList<TestTypes> testTypes;
    Toolbar toolbar;
    ArrayList<GroupFatherTests> groupFathers;
    ArrayList<ChildItemTests> childItens;
    ExpandableRecyclerViewAdapterTests adapterExpandable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_ranking_selective);

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
        if(extras!=null){
            callGetTestTypes(extras.getString("id_selective"));
        }
    }

    private void callGetTestTypes(String idSelective){
        if(Services.isOnline(this)){
            showProgress();
            getTestTypes(idSelective);
        }
    }

    private void getTestTypes(String idSelective) {
        String url = Constants.URL + Constants.API_SELECTIVE_TEST_TYPES_SEARCH;
        PostAsyncTask post = new PostAsyncTask();
        post.setObjPut(queryJson(idSelective));
        post.setWhoCalled(Constants.CALLED_GET_TEST_TYPES);
        post.setActivity(this);
        post.execute(url);
    }

    public static void returnGetTestTypes(Activity act, String response, int status){
        ((HistoricRankingTestsActivity)act).returnGetTestTypes(response, status);
    }

    private void returnGetTestTypes(String response, int status){
        hideProgress();
        if(status==200||status==201){
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            testTypes = des.getSelectiveTestType();
            callInflateRecycler();
        }
    }

    private void callInflateRecycler(){
        if(testTypes!=null && testTypes.size()>0){
            String[] values = new String[testTypes.size()];
            for(int i=0; i<=testTypes.size()-1; i++)
                values[i] = testTypes.get(i).getId();

            inflateRecycler(values);
        }
    }

    private void inflateRecycler(String[] values){
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_ranking);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(new GridLayoutManager(this, 2));//numero de colunas
        AdapterRecyclerTests adapter = new AdapterRecyclerTests(HistoricRankingTestsActivity.this, testTypes, values);
        recycler.setVisibility(View.VISIBLE);
        recycler.setAdapter(adapter);
    }

    private void inflateDataTests(ArrayList<Tests> tests){
        tests = orderAlphabeticTests(tests);
        groupFathers = new ArrayList<GroupFatherTests>();
        childItens = new ArrayList<ChildItemTests>();
        String type = tests.get(0).getType();
        for(Tests test : tests){
            if(test.equals(type))
                childItens.add(new ChildItemTests(test));
            else{
                type = test.getType();
                groupFathers.add(new GroupFatherTests(test.getType(), childItens));
            }
        }

        inflateExpandableRecycler();
    }

    private void inflateExpandableRecycler(){
        adapterExpandable = new ExpandableRecyclerViewAdapterTests(this, groupFathers);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_ranking);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setVisibility(View.VISIBLE);
        recycler.setAdapter(adapterExpandable);
    }

    private ArrayList<Tests> orderAlphabeticTests(ArrayList<Tests> tests){
        Collections.sort(tests, new Comparator<Tests>() {
            @Override
            public int compare(Tests o1, Tests o2) {
                return o1.getType().compareTo(o2.getType());
            }
        });

        return tests;
    }

    public static void clickItemRecycler(Activity act, int position){
        ((HistoricRankingTestsActivity)act).clickItemRecycler(position);
    }

    private void clickItemRecycler(int position){
        testTypes.get(position);

    }

    private JSONObject queryJson(String id){
        JSONObject json = new JSONObject();
        try{
            JSONObject jsonQuery = new JSONObject();
            jsonQuery.put(Constants.SELECTIVEATHLETES_SELECTIVE, id);
            json.put("query", jsonQuery);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("testType");
            json.put("populate", jsonArray);
        }catch(JSONException ex){
            Log.i("JSONException", ex.getMessage());
        }
        return json;
    }

    private void showProgress(){
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraint.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraint.setVisibility(View.GONE);
    }

    private View.OnClickListener clickedBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}

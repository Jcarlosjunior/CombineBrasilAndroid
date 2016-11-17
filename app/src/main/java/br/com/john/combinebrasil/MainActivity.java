package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.IOException;
import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerTests;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SyncDatabase;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    public static LinearLayout linearProgress;
    public static TextView textProgress;
    Toolbar toolbar;
    ArrayList<Tests> testsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout linearBacktoolbar = (LinearLayout) findViewById(R.id.linear_back_button);
        linearBacktoolbar.setVisibility(View.GONE);

        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setOnClickListener(clickAddAccount);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress_tests);
        textProgress = (TextView) findViewById(R.id.text_progress);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int totalItemCount = llm.getItemCount();
                Log.i("totalRecycler", totalItemCount+"");
                //CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();
                Log.i("recyclerLastFind", llm.findLastCompletelyVisibleItemPosition()+"");

                if (totalItemCount == llm.findLastCompletelyVisibleItemPosition()+1) {
                    // List<Car> listAux = ((CestasFragment) getActivity()).getSetCarList(10);
                    Log.i("recycler", "last");
                    /*for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }*/
                }
            }
        });

        try {
            linearProgress.setVisibility(View.VISIBLE);
            SyncDatabase sync = new SyncDatabase(MainActivity.this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Constants.debug)
            callAllFalseTests();
        else
            callAllTests();
    }

    private void callAllFalseTests(){
        testsArrayList = new ArrayList<Tests>();
        String[] values = new String[6];
        for(int i=0; i<=5; i++){
            Tests test = new Tests();
            test.setId(String.valueOf(i));
            test.setName("Teste "+i);
            test.setType("Tipo "+i);
            test.setDescription("descrição "+i);
            testsArrayList.add(test);
            values[i]="Teste "+i;
        }
        inflateRecyclerView(testsArrayList, values);
    }

    private void callAllTests() {
            if (Services.isOnline(this)) {
                linearProgress.setVisibility(View.VISIBLE);
                String url = Constants.URL + Constants.login;
                Connection task = new Connection(url, Request.Method.GET, Constants.CALLED_GET_TESTS, true, this);
                task.callByJsonStringRequest();
            }
            else
                Services.messageAlert(this, "Aviso", "Sem conexão com a internet", "");
    }

    public static void afterCalled(String response, boolean isList, Activity activity) {
        ((MainActivity) activity).ShowTests(response, isList);
    }

    public void ShowTests(String response, boolean isList){
        linearProgress.setVisibility(View.GONE);
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        Tests tests;

        if (!isList)
            tests = des.getObjectTest();
        else{
            ArrayList<Tests> testsArrayList = des.getListTests();
            String[] values = new String[testsArrayList.size()];
            for(int i=0; i<=testsArrayList.size()-1; i++){
                values[i] = String.valueOf(testsArrayList.get(i).getName());
            }
            inflateRecyclerView(testsArrayList, values);
        }

        if (!response.equals("")) {
            //Guarda em memória do cel
        }
    }

    private void inflateRecyclerView(ArrayList<Tests> testsArrayList, String[] values){
        AdapterRecyclerTests adapterTests = new AdapterRecyclerTests(this,testsArrayList, values);
        adapterTests.setHomeActivity(this);
        linearProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//numero de colunas
        mRecyclerView.setAdapter(adapterTests);
    }

    public static void onClickItemList(Activity activity, int positionArray, String id){
        ((MainActivity) activity).validaClick(positionArray);
    }
    public void validaClick(int position){
        if(position==2 || position ==0)
            AllActivities.type="corrida";
        else
            AllActivities.type="";
        Intent i = new Intent(MainActivity.this, AthletesActivity.class);
        Tests test = testsArrayList.get(position);
        i.putExtra("id_test", test.getId());
        i.putExtra("name_test", test.getName());
        i.putExtra("details_test", test.getDescription());
        startActivity(i);
    }

    private View.OnClickListener clickAddAccount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, CreateAccountAthlete.class);
            startActivity(i);
        }
    };
}

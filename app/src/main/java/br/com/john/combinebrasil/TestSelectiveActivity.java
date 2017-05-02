package br.com.john.combinebrasil;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerChooseTestSelective;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

public class TestSelectiveActivity extends AppCompatActivity {
    public static final String messageSelectiveCreate = "SELECTIVE_CREATE_OK";
    ConstraintLayout linearProgress;
    LinearLayout linearNotConnection;
    TextView textProgress;
    Toolbar toolbar;
    RecyclerView recyclerViewTests;
    private int positionRemove = 0;

    ArrayList<TestTypes> tests;
    private static ArrayList<TestTypes> testsChoose;
    AdapterRecyclerChooseTestSelective adapterRecyclerTests;

    LinearLayout linearDelete;
    FloatingActionButton buttonDone;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_selective);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        linearDelete = (LinearLayout)findViewById(R.id.linear_delete);
        linearDelete.setOnClickListener(clickedRemoveTests);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        recyclerViewTests = (RecyclerView) findViewById(R.id.recycler_tests_selective);

        linearProgress = (ConstraintLayout) findViewById(R.id.linear_progress);
        textProgress = (TextView) findViewById(R.id.text_progress);

        buttonDone = (FloatingActionButton) findViewById(R.id.fab_upload_done);
        buttonDone.setOnClickListener(clickDoneTests);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordination_tests);

        tests = new ArrayList<TestTypes>();
        testsChoose = new ArrayList<TestTypes>();

        callUpdateTests();
    }

    private void callUpdateTests(){
        if(Services.isOnline(this)){
            updateTests();
        }
        else
            linearNotConnection.setVisibility(View.VISIBLE);
    }

    private void updateTests(){
        linearProgress.setVisibility(View.VISIBLE);
        String url = Constants.URL + Constants.API_TESTTYPES;
        Connection task = new Connection(url, 0, Constants.CALLED_GET_TESTTYPES, false, TestSelectiveActivity.this);
        task.callByJsonStringRequest();
    }

    public static void returnUpdateTests(Activity act, int status, String result){
        ((TestSelectiveActivity)act).returnUpdateTests(status, result);
    }
    private void returnUpdateTests(int status, String result){
        linearProgress.setVisibility(View.GONE);
        if(status == 200 || status == 201) {
            verifyResult(result);
        }
    }
    private void verifyResult(String result){
        DeserializerJsonElements des = new DeserializerJsonElements(result);
        tests = des.getTestTypes();
        try{
            if (tests!=null)
                recordingTests();
        }catch (Exception e){}
    }

    private void recordingTests(){
        DatabaseHelper db = new DatabaseHelper(TestSelectiveActivity.this);
        try {
            db.createDataBase();
            db.openDataBase();
            db.addTestTypes(tests);
            inflateTests();
        } catch (Exception e) {}
    }

    private void inflateTests(){
        if(!(tests == null || tests.size()==0)){
            String[] values = new String[tests.size()];
            for(int i=0; i <=tests.size()-1; i++){
                values[i] = tests.get(i).getId();
            }
            inflateRecyclerView(values);
        }
    }

    private void inflateRecyclerView(String[] values){
        recyclerViewTests.setHasFixedSize(true);
        recyclerViewTests.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTests.setLayoutManager(new GridLayoutManager(this, 2));
        adapterRecyclerTests = new AdapterRecyclerChooseTestSelective(TestSelectiveActivity.this, tests, values);
        recyclerViewTests.setAdapter(adapterRecyclerTests);
    }

    public static boolean testExist(Activity act, String id){
        return ((TestSelectiveActivity) act).findTestChoose(id);
    }
    private boolean findTestChoose(String id){
        boolean ret = false;
        for (int position=0; position<testsChoose.size(); position++) {
            if (testsChoose.get(position).getId().equals(id)) {
                positionRemove = position;
                ret = true;
            }
        }
        return ret;
    }

    public static void clickTestChoose(Activity act, String id, int position){
        ((TestSelectiveActivity) act).clickTestChoose(id, position);
    }
    private void clickTestChoose(String id, int position){
        boolean ret = findTestChoose(id);
        if(!ret)
            testsChoose.add(tests.get(position));
        else
            testsChoose.remove(positionRemove);

        showOrHideRemove();

        adapterRecyclerTests.notifyItemChanged(position);
    }

    private void showOrHideRemove(){
        if(!testsChoose.isEmpty()){
            linearDelete.setVisibility(View.VISIBLE);
            buttonDone.setVisibility(View.VISIBLE);
        }
        else{
            linearDelete.setVisibility(View.GONE);
            buttonDone.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener clickedRemoveTests= new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            removeAllTests();
        }
    };
    private void removeAllTests(){
        int sizeTest = testsChoose.size();
        for(int i = 0; i<=sizeTest-1; i++){
            boolean ret = findTestChoose(testsChoose.get(0).getId());
            if(ret){
                int position = tests.indexOf(testsChoose.get(0));
                testsChoose.remove(0);
                adapterRecyclerTests.notifyItemChanged(position);
            }
        }
        showOrHideRemove();
    }

    private View.OnClickListener clickDoneTests = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            doneTests();
        }
    };

    private void doneTests(){
        Services.messageAlert(this, "Mensagem","Testes adicionado a seletiva", messageSelectiveCreate);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Adicionado", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void returnClickableAlert(Activity act, String whoCalled){
        ((TestSelectiveActivity)act).returnClickableAlert(whoCalled);
    }
    private void returnClickableAlert(String whoCalled){
        if(whoCalled.equals(messageSelectiveCreate))
            finishAfterCreateSelective();
    }

    private void finishAfterCreateSelective(){
        CreateSelectiveActivity.finishActity();
        this.finish();
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TestSelectiveActivity.this.finish();
        }
    };
}

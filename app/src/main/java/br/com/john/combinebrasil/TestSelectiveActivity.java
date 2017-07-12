package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

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
    ConstraintLayout linearProgress, linearNotConnection;
    public static ConstraintLayout constraintInfoTest;
    TextView textProgress;
    Toolbar toolbar;
    RecyclerView recyclerViewTests;
    public static Button btnNextPass;
    Button btnTryAgain;
    ArrayList<TestTypes> tests;
    AdapterRecyclerChooseTestSelective adapterRecyclerTests;
    public static LinearLayout linearDelete;
    private static Activity act;

    HashMap<String, String> hashMapSelective;

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

        linearNotConnection = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        linearProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        textProgress = (TextView) findViewById(R.id.text_progress);
        constraintInfoTest = (ConstraintLayout) findViewById(R.id.constraint_info_test);
        btnTryAgain = (Button) findViewById(R.id.btn_try_again_connect);
        btnTryAgain.setOnClickListener(clickListenerTryAgain);
        btnNextPass = (Button) findViewById(R.id.button_next_pass);
        btnNextPass.setOnClickListener(clickDoneTests);

        hashMapSelective = AllActivities.hashInfoSelective;

        tests = new ArrayList<TestTypes>();

        act = TestSelectiveActivity.this;
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
        recyclerViewTests.setLayoutManager(new GridLayoutManager(this, 1));
        adapterRecyclerTests = new AdapterRecyclerChooseTestSelective(TestSelectiveActivity.this, tests, values);
        adapterRecyclerTests.valuesID = new String[values.length];
        recyclerViewTests.setAdapter(adapterRecyclerTests);
    }

    private void showOrHideRemove(){
        for (TestTypes testType : adapterRecyclerTests.list){
            if(testType.isSelected()){
                TestSelectiveActivity.linearDelete.setVisibility(View.VISIBLE);
                TestSelectiveActivity.btnNextPass.setVisibility(View.VISIBLE);
            }
            else{
                TestSelectiveActivity.linearDelete.setVisibility(View.GONE);
                TestSelectiveActivity.btnNextPass.setVisibility(View.GONE);
            }
        }
    }

    private View.OnClickListener clickedRemoveTests= new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            removeAllTests();
        }
    };

    private void removeAllTests(){
        int count =0;
        for (TestTypes test : adapterRecyclerTests.list){
            if(test.isSelected()) {
                test.setSelected(false);
                adapterRecyclerTests.notifyItemChanged(count);
            }
            count++;
        }
        showOrHideRemove();
    }

    private View.OnClickListener clickDoneTests = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            doneTests();
        }
    };

    private void doneTests() {
        createSelective();
    }

    private void createSelective(){
        Intent i = new Intent(TestSelectiveActivity.this, InfoSelectiveCreateActivity.class);

        ArrayList<String> testsChoose = new ArrayList<>();

        for (TestTypes test :  adapterRecyclerTests.list) {
            if(test.isSelected())
                testsChoose.add(test.getId());
        }
        i.putStringArrayListExtra("testsChoose", testsChoose);
        startActivity(i);
    }

    private View.OnClickListener clickListenerTryAgain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callUpdateTests();
        }
    };

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TestSelectiveActivity.this.finish();
        }
    };

    public static void returnClickableAlert(Activity act, String whoCalled){
        ((TestSelectiveActivity)act).returnClickableAlert(whoCalled);
    }

    private void returnClickableAlert(String whoCalled){
        if(whoCalled.equals(messageSelectiveCreate))
            finishAfterCreateSelective();
    }

    private void finishAfterCreateSelective(){
        CreateSelectiveActivity.finishActity();
        ChooseTeamSelectiveActivity.finishActity();
        this.finish();
    }

    public static void finishOhterActivity(){
        ((TestSelectiveActivity)act).finish();
    }

    public static void showInfoTest(Activity act, int position){
        ((TestSelectiveActivity)act).showInfoTest(position);
    }

    private void showInfoTest(int position){
        ConstraintLayout constraintInfoTest = (ConstraintLayout) findViewById(R.id.constraint_info_test);
        TextView textTitle = (TextView) findViewById(R.id.text_title_test);
        TextView textDescription = (TextView) findViewById(R.id.text_description_test);
        Button btnClose = (Button) findViewById(R.id.btn_close);

        TestTypes test = tests.get(position);

        constraintInfoTest.setVisibility(View.VISIBLE);
        textTitle.setText(test.getName());
        textDescription.setText(Html.fromHtml(test.getDescription()));
        btnClose.setOnClickListener(clickedBtnClose);
    }

    private View.OnClickListener clickedBtnClose = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConstraintLayout constraintInfoTest = (ConstraintLayout) findViewById(R.id.constraint_info_test);
            constraintInfoTest.setVisibility(View.GONE);
        }
    };
}

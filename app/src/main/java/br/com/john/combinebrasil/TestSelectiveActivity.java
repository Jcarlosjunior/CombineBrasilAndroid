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
import android.widget.ScrollView;
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
    TextView textProgress, textTestRequired, textTestRecommended, textTestAdditional;
    Toolbar toolbar;
    RecyclerView recyclerViewTestsRequired, recyclerViewRecommended, recyclerViewAdditional;
    public static Button btnNextPass, btnCloseMessage;
    Button btnTryAgain;
    ArrayList<TestTypes> tests;
    ArrayList<TestTypes> testsRequireds;
    ArrayList<TestTypes> testsRecommended;
    ArrayList<TestTypes> testsAdditional;
    AdapterRecyclerChooseTestSelective adapterRecyclerTestsRequired, adapterRecyclerTestsRecommended, adapterRecyclerTestsAdditional;
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

        recyclerViewTestsRequired = (RecyclerView) findViewById(R.id.recycler_tests_selective);
        recyclerViewRecommended = (RecyclerView) findViewById(R.id.recycler_tests_recommended);
        recyclerViewAdditional = (RecyclerView) findViewById(R.id.recycler_tests_additional);

        linearNotConnection = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        linearProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        textProgress = (TextView) findViewById(R.id.text_progress);
        textTestRequired = (TextView) findViewById(R.id.text_tests_required);
        textTestRecommended = (TextView) findViewById(R.id.text_tests_recommended);
        textTestAdditional = (TextView) findViewById(R.id.text_tests_additional);
        constraintInfoTest = (ConstraintLayout) findViewById(R.id.constraint_info_test);
        btnTryAgain = (Button) findViewById(R.id.btn_try_again_connect);
        btnTryAgain.setOnClickListener(clickListenerTryAgain);
        btnNextPass = (Button) findViewById(R.id.button_next_pass);
        btnNextPass.setOnClickListener(clickDoneTests);
        btnNextPass.setVisibility(View.GONE);
        btnCloseMessage=(Button) findViewById(R.id.btn_close_message_tests);
        btnCloseMessage.setOnClickListener(clickMessageDefault);

        textTestRequired.setOnClickListener(clickTestsRequireds);
        textTestRecommended.setOnClickListener(clickTestsRecommended);
        textTestAdditional.setOnClickListener(clickTestsAdditional);

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
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_message_test_default);
        constraint.setVisibility(View.VISIBLE);
        linearProgress.setVisibility(View.GONE);
        if(status == 200 || status == 201) {
            verifyResult(result);
        }
    }

    private void verifyResult(String result){
        DeserializerJsonElements des = new DeserializerJsonElements(result);
        tests = des.getTestTypes();
        try{
            if (tests!=null){
                recordingTests();
            }

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
        testsRequireds = new ArrayList<>();
        testsRecommended = new ArrayList<>();
        testsAdditional = new ArrayList<>();

        if(!(tests == null || tests.size()==0)){
            for(int i=0; i <=tests.size()-1; i++){
                tests.get(i).setDefaultTest(i<=7 ? true : false);
                if(tests.get(i).isDefaultTest())
                    tests.get(i).setSelected(true);
                else
                    tests.get(i).setSelected(false);

                if(i%2 > 0)
                    tests.get(i).setIconImageURL("http://jeremybeynon.com/wp-content/uploads/2014/12/nfl-logo.jpg");
                else
                    tests.get(i).setIconImageURL("http://pontepretagorilas.com.br/wp-content/uploads/2017/04/timthumb-1-128x128.png");

                if(tests.get(i).isMainTest()&&tests.get(i).isRequiredToReport() && tests.get(i).getSiblingTestType().isEmpty())
                    testsRequireds.add(tests.get(i));
                else if(tests.get(i).isMainTest()&&tests.get(i).isRequiredToReport() && !tests.get(i).getSiblingTestType().isEmpty())
                    testsRecommended.add(tests.get(i));
                else
                    testsAdditional.add(tests.get(i));
            }

            String valuesRequireds[] = new String[testsRequireds.size()];
            for(int i=0; i<=testsRequireds.size()-1;i++)
                valuesRequireds[i] = testsRequireds.get(i).getId();
            String valuesRecommended[] = new String[testsRecommended.size()];
            for(int i=0; i<=testsRecommended.size()-1;i++)
                valuesRecommended[i] = testsRecommended.get(i).getId();
            String valuesAdditional[] = new String[testsAdditional.size()];
            for(int i=0; i<=testsAdditional.size()-1;i++)
                valuesAdditional[i] = testsAdditional.get(i).getId();
            inflateRecyclerViewRequired(valuesRequireds);
            inflateRecyclerViewRecommended(valuesRecommended);
            inflateRecyclerViewAdditional(valuesAdditional);
            adapterRecyclerTestsRequired.showOrHideRemove();
        }
    }

    private void inflateRecyclerViewRequired(String[] values){
        recyclerViewTestsRequired.setHasFixedSize(true);
        recyclerViewTestsRequired.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTestsRequired.setLayoutManager(new GridLayoutManager(this, 1));
        adapterRecyclerTestsRequired = new AdapterRecyclerChooseTestSelective(TestSelectiveActivity.this, testsRequireds, values);
        adapterRecyclerTestsRequired.valuesID = new String[values.length];
        recyclerViewTestsRequired.setAdapter(adapterRecyclerTestsRequired);
    }

    private void inflateRecyclerViewRecommended(String[] values){
        recyclerViewRecommended.setHasFixedSize(true);
        recyclerViewRecommended.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecommended.setLayoutManager(new GridLayoutManager(this, 1));
        adapterRecyclerTestsRecommended = new AdapterRecyclerChooseTestSelective(TestSelectiveActivity.this, testsRecommended, values);
        adapterRecyclerTestsRecommended.valuesID = new String[values.length];
        recyclerViewRecommended.setAdapter(adapterRecyclerTestsRecommended);
    }
    private void inflateRecyclerViewAdditional(String[] values){
        recyclerViewRecommended.setHasFixedSize(true);
        recyclerViewRecommended.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecommended.setLayoutManager(new GridLayoutManager(this, 1));
        adapterRecyclerTestsAdditional = new AdapterRecyclerChooseTestSelective(TestSelectiveActivity.this, testsAdditional, values);
        adapterRecyclerTestsAdditional.valuesID = new String[values.length];
        recyclerViewRecommended.setAdapter(adapterRecyclerTestsAdditional);
    }



    private View.OnClickListener clickedRemoveTests= new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            removeAllTests();
        }
    };

    private void removeAllTests(){
        int count =0;
        for (TestTypes test : adapterRecyclerTestsRequired.list){
            if(test.isSelected()) {
                test.setSelected(false);
                adapterRecyclerTestsRequired.notifyItemChanged(count);
            }
            count++;
        }
        TestSelectiveActivity.linearDelete.setVisibility(View.GONE);
        TestSelectiveActivity.btnNextPass.setVisibility(View.GONE);
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

        for (TestTypes test :  adapterRecyclerTestsRequired.list) {
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
        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView5);
        scroll.setScrollY(0);

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

    private View.OnClickListener clickMessageDefault = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeMessageTestDefault();
        }
    };

    private void closeMessageTestDefault(){
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_message_test_default);
        constraint.setVisibility(View.GONE);
    }

    private View.OnClickListener clickTestsRequireds = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(recyclerViewTestsRequired.getVisibility() == View.VISIBLE)
                recyclerViewTestsRequired.setVisibility(View.GONE);
            else
                recyclerViewTestsRequired.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener clickTestsRecommended = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(recyclerViewRecommended.getVisibility() == View.VISIBLE)
                recyclerViewRecommended.setVisibility(View.GONE);
            else
                recyclerViewRecommended.setVisibility(View.VISIBLE);
        }
    };
    private View.OnClickListener clickTestsAdditional = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(recyclerViewAdditional.getVisibility() == View.VISIBLE)
                recyclerViewAdditional.setVisibility(View.GONE);
            else
                recyclerViewAdditional.setVisibility(View.VISIBLE);
        }
    };

}

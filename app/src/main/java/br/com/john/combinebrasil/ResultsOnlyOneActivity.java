package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.UUID;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Results;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.CreateJSON;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostSync;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MaskHeight;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class ResultsOnlyOneActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button buttonAdd, btnReady, buttonWingspan;
    EditText editFirstResult, editWingspan;
    int position=0;
    TextView textNamePlayer, textNameTest, textNameTestDetails, textDetailsTest, textNamePlayerDetails,
            textDetailsPlayer, textShowRating;
    ImageView imgArrowTest, imgArrowPlayer, imgDelete;
    boolean arrowDownTest=true,arrowDownPlayer=true;
    LinearLayout linearRating, linearWingSpan, deleteTest, linearProgress;
    RatingBar ratingBar;
    String idAthlete = "", wingspan=" ";
    float ratingValue = 0;

    LinearLayout linearInsert, linearResultDone;
    TextView txtFistDone, txtSecondDone, txtNameResult, txtRating;
    Button buttonBack;
    RatingBar ratingDone;
    DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_only_one);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);

        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        db = new DatabaseHelper(ResultsOnlyOneActivity.this);
        TestTypes test = db.getTestTypeFromId(AllActivities.testSelected);
        textTitle.setText(test.getName());
        deleteTest = (LinearLayout) findViewById(R.id.linear_delete);
        imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(clickDelete);

        linearInsert = (LinearLayout) findViewById(R.id.linear_insert);
        linearResultDone = (LinearLayout) findViewById(R.id.linear_results_done);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);
        txtNameResult = (TextView) findViewById(R.id.txt_name_result);
        txtFistDone = (TextView) findViewById(R.id.txt_first_result_done);
        txtSecondDone = (TextView) findViewById(R.id.txt_second_result_done);
        ratingDone = (RatingBar) findViewById(R.id.rating_result_done);
        buttonBack = (Button) findViewById(R.id.button_back);
        txtRating = (TextView) findViewById(R.id.txt_rating_done);

        editWingspan = (EditText) findViewById(R.id.edit_wingspan);
        buttonWingspan = (Button) findViewById(R.id.button_wingspan);
        buttonWingspan.setOnClickListener(clickedWingspan);
        linearWingSpan = (LinearLayout) findViewById(R.id.linear_wingspan);

        linearRating = (LinearLayout) findViewById(R.id.linear_rating_results);

        editFirstResult = (EditText) findViewById(R.id.edit_first_result);
        buttonAdd = (Button) findViewById(R.id.button_add_results);
        btnReady = (Button) findViewById(R.id.button_ready_results);

        textNamePlayer = (TextView) findViewById(R.id.text_name_player_result);
        textNameTest = (TextView) findViewById(R.id.text_name_test_result);
        textNameTestDetails = (TextView) findViewById(R.id.text_name_test_details);
        textDetailsTest = (TextView) findViewById(R.id.text_details_test);
        textNamePlayerDetails = (TextView) findViewById(R.id.text_name_player_details);
        textDetailsPlayer = (TextView) findViewById(R.id.text_details_player);
        textShowRating = (TextView) findViewById(R.id.text_show_qualify_results);

        imgArrowPlayer = (ImageView) findViewById(R.id.img_player_arrow);
        imgArrowTest = (ImageView) findViewById(R.id.img_test_arrow);
        imgArrowTest.setOnClickListener(clickedImgArrowTest);
        imgArrowPlayer.setOnClickListener(clickedImgArrowPlayer);

        ratingBar = (RatingBar) findViewById(R.id.rating_results);

        buttonAdd.setOnClickListener(clickSave);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            idAthlete = extras.getString("id_player");
            position = extras.getInt("position");
            showInfoAthlete();
            verifyTest();
        }

        TextWatcher mask = MaskHeight.insert("#,##", editWingspan);
        editWingspan.addTextChangedListener(mask);
        mask = MaskHeight.insert("#,##", editFirstResult);
        editFirstResult.addTextChangedListener(mask);
    }

    private void verifyTest(){
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        if(test != null){
            if(!Services.convertIntInBool(test.getSync())) {
                deleteTest.setVisibility(View.VISIBLE);
            }
            else
                deleteTest.setVisibility(View.GONE);
            if(test.getCanSync()) {
                showInfoTestDone();
            }
            else {
                if(test!=null) {
                    editFirstResult.setText(Services.convertCentimetersinMeters(test.getFirstValue()));
                    editFirstResult.setEnabled(false);
                }
                linearInsert.setVisibility(View.VISIBLE);
                linearResultDone.setVisibility(View.GONE);
            }
        }
        else{
            linearInsert.setVisibility(View.VISIBLE);
            linearResultDone.setVisibility(View.GONE);
            editFirstResult.setText("");
            editFirstResult.setEnabled(true);
            deleteTest.setVisibility(View.GONE);
            TestTypes testypes = db.getTestTypeFromId(AllActivities.testSelected);

            if(testypes.getName().toLowerCase().equals("salto vertical")) {
                linearInsert.setVisibility(View.GONE);
                linearWingSpan.setVisibility(View.VISIBLE);
            }
            editFirstResult.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()>2)
                        enabledButtonAdd(true);
                    else
                        enabledButtonAdd(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void showInfoTestDone(){
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);

        linearInsert.setVisibility(View.GONE);
        linearResultDone.setVisibility(View.VISIBLE);
        txtFistDone.setText(Services.convertCentimetersinMeters(test.getFirstValue()));
        txtSecondDone.setText(Services.convertCentimetersinMeters(test.getSecondValue()));

        Athletes athlete = db.getAthleteById(idAthlete);
        txtNameResult.setText(athlete.getName());

        ratingDone.setRating(test.getRating());
        ratingDone.setEnabled(false);

        txtRating.setText(Services.verifyQualification(test.getRating()));

        buttonBack.setOnClickListener(btnBackClickListener);
    }

    private void showInfoAthlete(){
        DatabaseHelper db  = new DatabaseHelper(ResultsOnlyOneActivity.this);
        db.openDataBase();
        Athletes athlete = db.getAthleteById(idAthlete);
        textNamePlayer.setText(athlete.getName());
        textNamePlayerDetails.setText(athlete.getName());

        TestTypes test = db.getTestTypeFromId(AllActivities.testSelected);
        if(test!=null) {
            textNameTest.setText(test.getName());
            textNameTestDetails.setText(test.getName());
            textDetailsTest.setText(Html.fromHtml(test.getDescription()));
        }
    }


    private View.OnClickListener clickedImgArrowTest = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(arrowDownTest)
            {
                arrowDownTest=false;
                imgArrowTest.setImageDrawable(getDrawable(R.drawable.arrow_top));
                textDetailsTest.setVisibility(View.VISIBLE);
            }
            else{
                arrowDownTest=true;
                imgArrowTest.setImageDrawable(getDrawable(R.drawable.arrow_down));
                textDetailsTest.setVisibility(View.GONE);
            }
        }
    };

    private View.OnClickListener clickedImgArrowPlayer = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(arrowDownPlayer)
            {
                arrowDownPlayer=false;
                imgArrowPlayer.setImageDrawable(getDrawable(R.drawable.arrow_top));
                textDetailsPlayer.setVisibility(View.VISIBLE);
            }
            else{
                arrowDownPlayer = true;
                imgArrowPlayer.setImageDrawable(getDrawable(R.drawable.arrow_down));
                textDetailsPlayer.setVisibility(View.GONE);
            }

        }
    };

    public static void getMethodOutResultsActivity(Activity act, String method){
        ((ResultsOnlyOneActivity)act).calledMethod(method);
    }

    private void calledMethod(String method){
        if(method.equals("checkAndSaveResults")){
            checkAndSaveResults();
        }
        else if(method.equals("saveAllResults")){
            updateTest();
            Services.messageAlert(ResultsOnlyOneActivity.this, "Mensagem","Os resultados foram salvos!", "DialogSaveResults");
        }
        else if(method.equals("exitActivity")){
            finish();
        }
        else if(method.equals("deleteResults")){
            deleteTest();
        }
    }

    private void deleteTest(){
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
        db.openDataBase();
        db.deleteValue(Constants.TABLE_TESTS, test.getId());
        Services.messageAlert(ResultsOnlyOneActivity.this, "Mensagem","Teste excluído!","");
        verifyTest();
    }

    private void saveTest(){
        db.openDataBase();
        User user= db.getUser();
        Selective selective = db.getSelective();
        Tests test = new Tests(
                UUID.randomUUID().toString(),
                AllActivities.testSelected,
                idAthlete,
                selective.getId(),
                Services.convertMetersinCentimeters(editFirstResult.getText().toString()),
                0,
                ratingValue,
                wingspan,
                user.getId(),
                Services.convertBoolInInt(false),
                false);
        db.addTest(test);
        sync(test);
        deleteTest.setVisibility(View.VISIBLE);
        AthletesActivity.adapterTests.notifyItemChanged(position);
    }

    private void sync(Tests test){
        if(Services.isOnline(ResultsOnlyOneActivity.this)) {
            linearProgress.setVisibility(View.VISIBLE);
            db = new DatabaseHelper(ResultsOnlyOneActivity.this);
            Athletes athlete  = db.getAthleteById(test.getAthlete());
            if(athlete!=null){
                if(athlete.getSync()){
                    String url = Constants.URL + Constants.API_TESTS;
                    PostSync post = new PostSync();
                    post.setActivity(ResultsOnlyOneActivity.this);
                    post.setAll(false);
                    post.setObjPut(CreateJSON.createObject(test));
                    post.execute(url);
                }
            }
        }
        else
            Services.messageAlert(ResultsOnlyOneActivity.this, "Aviso","Para começar a sincronização, você precisa ter uma conexão com a internet.","");
    }

    public static void returnPostTest(Activity act, String resp, String result){
        ((ResultsOnlyOneActivity)act).returnPostTest(resp, result);
    }

    private void returnPostTest(String resp, String result){
        linearProgress.setVisibility(View.GONE);
        //result = result.replaceFirst("Value","");

        Log.i("ERROR", result);

        if(resp.equals("OK")) {
            DeserializerJsonElements des = new DeserializerJsonElements(result);
            Tests test = des.getTestObject();
            updateTest(test);
        }
        else{
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
                        Constants.TESTS_TYPE+"="+json.getString(Constants.TESTS_TYPE)+"&"+Constants.TESTS_SELECTIVE+"="+
                        json.getString(Constants.TESTS_SELECTIVE);
                Connection task = new Connection(url, 0, "UPDATE_TEST", false, ResultsOnlyOneActivity.this);
                task.callByJsonStringRequest();
            }
            else{
                linearProgress.setVisibility(View.GONE);
                Services.messageAlert(ResultsOnlyOneActivity.this, "Aviso","Erro ao tentar sincronizar teste.","");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void returnUpdateSync(Activity act, String response){
        ((ResultsOnlyOneActivity)act).returnUpdateSync(response);
    }

    private void returnUpdateSync(String response) {
        linearProgress.setVisibility(View.GONE);
        if (!response.equals("[]")) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            Tests test = des.getTestObjectTest();
            if (test != null) {
                updateTest(test);
            } else
                Services.messageAlert(ResultsOnlyOneActivity.this, "Aviso", "Erro ao tentar sincronizar teste.", "");
        }
        else
            Services.messageAlert(ResultsOnlyOneActivity.this, "Aviso", "Erro ao tentar sincronizar teste.", "");
    }

    private void updateTest(Tests test){
        DatabaseHelper db = new DatabaseHelper(ResultsOnlyOneActivity.this);
        db.updateTest(test);
        try {
            AthletesActivity.adapterTests.notifyItemChanged(position);
        }catch (Exception e){
            e.printStackTrace();
        }

        Services.messageAlert(ResultsOnlyOneActivity.this, "Mensagem","Os resultados foram salvos!","DIALOGSAVERESULTS");
    }

    private void updateTest(){
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(idAthlete, AllActivities.testSelected);
         AthletesActivity.adapterTests.notifyItemChanged(position);
    }

    private void checkAndSaveResults (){
            saveTest();
            editFirstResult.setEnabled(false);
            buttonAdd.setText("Salvar os resultados");
            buttonAdd.setOnClickListener(saveAll);
            enabledButtonAdd(true);
    }

    private void enabledButtonAdd(boolean enabled){
        if(enabled) {
            buttonAdd.setEnabled(true);
            buttonAdd.setAlpha(1f);
        }else {
            buttonAdd.setEnabled(false);
            buttonAdd.setAlpha(.5f);
        }
    }

    private void messageOption(String title, String message, String method){
        new MessageOptions(ResultsOnlyOneActivity.this, title, message, method);
    }

    @Override
    public void onBackPressed(){
        exitActivity();
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitActivity();
        }
    };

    private void exitActivity(){
        finish();
    }

    public View.OnClickListener clickSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int first = Integer.parseInt(editFirstResult.getText().toString().substring(0,1));
            if(first>=4){
                Services.changeColorEditBorderError(editFirstResult, ResultsOnlyOneActivity.this);
                Services.messageAlert(ResultsOnlyOneActivity.this, "Aviso","Ops, o salto está muito alto, verifique e tente novamente.","");
            }
            else
                messageOption("Salvar", "Deseja salvar o resultado?", "checkAndSaveResults");
        }
    };

    private View.OnClickListener saveAll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            messageOption("Salvar", "Certeza que deseja salvar os resultados?","saveAllResults");
        }
    };

    public static void finished(Activity act){
        ((ResultsOnlyOneActivity)act).finish();
    }

    private View.OnClickListener clickedWingspan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView extError = (TextView) findViewById(R.id.text_error_wingspan);
            if (editWingspan.getText().toString().trim().length()>3){
                wingspan = editWingspan.getText().toString();
                linearWingSpan.setVisibility(View.GONE);
                extError.setVisibility(View.INVISIBLE);
                linearInsert.setVisibility(View.VISIBLE);
            }
            else {
                extError.setVisibility(View.VISIBLE);
                Services.changeColorEditBorderError(editWingspan, ResultsOnlyOneActivity.this);
            }
        }
    };

    private View.OnClickListener clickDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MessageOptions(ResultsOnlyOneActivity.this, "Deletar", "Deseja excluir os dados do teste atual?", "deleteResults");
        }
    };
}
package br.com.john.combinebrasil;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerChooseTestSelective;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerTestInfo;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Connection.JSONServices.CreateJSON;
import br.com.john.combinebrasil.Connection.Posts.PostCreateSelective;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

import static br.com.john.combinebrasil.TestSelectiveActivity.messageSelectiveCreate;

public class InfoSelectiveCreateActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textTeamInfo, textDetailsInfo, textObservationInfo, textTestsInfo, textNameSelective,
            textDateSelective, textLocalSelective, textObservationsDetails, textPrivacy, textTermsPrivacy, textProgress;
    ImageView imgTeamChoose, imgshowMoreTeam, imgShowMoreObservations, imgShowMoreDetails, imgShowMoreTests;
    ConstraintLayout constraintDetailsInfo, constraintProgress, constraintNotConnection, constraintPrivacy;
    Button btnFinish, btnCancel, btnAccepted, btnTryAgain;
    RecyclerView recyclerTests;
    AdapterRecyclerTestInfo adapterRecyclerTests;
    boolean isAcceptedPrivacy;
    HashMap<String, String> hashMapSelective;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_selective_create);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        textTeamInfo = (TextView) findViewById(R.id.text_team_info);
        textDetailsInfo = (TextView) findViewById(R.id.text_detalhes_info);
        textObservationInfo = (TextView) findViewById(R.id.text_observacoes_info);
        textTestsInfo = (TextView) findViewById(R.id.text_testes_info);
        textNameSelective = (TextView) findViewById(R.id.text_name_selective_details);
        textDateSelective = (TextView) findViewById(R.id.text_date_selective_details);
        textLocalSelective = (TextView) findViewById(R.id.text_local_selective_details);
        textPrivacy = (TextView) findViewById(R.id.text_politica_privacidade);
        textTermsPrivacy = (TextView) findViewById(R.id.text_terms_privacy);
        textObservationsDetails = (TextView) findViewById(R.id.text_observations_details);
        textProgress = (TextView) findViewById(R.id.text_progress);

        imgTeamChoose = (ImageView) findViewById(R.id.img_team_choose);
        imgshowMoreTeam = (ImageView) findViewById(R.id.img_show_more_team);
        imgShowMoreObservations = (ImageView) findViewById(R.id.img_show_more_observacoes);
        imgShowMoreDetails = (ImageView) findViewById(R.id.img_show_more_detalhes);
        imgShowMoreTests = (ImageView) findViewById(R.id.img_show_more_testes);

        constraintDetailsInfo = (ConstraintLayout) findViewById(R.id.constraint_details);
        constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraintPrivacy = (ConstraintLayout) findViewById(R.id.constraint_privacy);
        constraintNotConnection = (ConstraintLayout) findViewById(R.id.constraint_not_connection);

        btnFinish = (Button) findViewById(R.id.btn_create_selective);
        btnCancel = (Button) findViewById(R.id.btn_cancel_confirm);
        btnAccepted = (Button) findViewById(R.id.btn_accepted_confirm);
        btnTryAgain = (Button) findViewById(R.id.btn_try_again_connect);

        recyclerTests=(RecyclerView) findViewById(R.id.recycler_tests_info);

        textTeamInfo.setOnClickListener(btnShowMoteTeam);
        textDetailsInfo.setOnClickListener(btnShowMoteDetails);
        textObservationInfo.setOnClickListener(btnShowMoteObservation);
        textTestsInfo.setOnClickListener(btnShowMoteTests);

        btnTryAgain.setOnClickListener(btnClickedTryAgain);
        btnAccepted.setOnClickListener(btnAcceptedClickListener);
        btnCancel.setOnClickListener(btnCancelClickListener);
        btnFinish.setOnClickListener(btnCreateSelectiveClickListener);
        textPrivacy.setOnClickListener(textClickedPrivacy);

        hashMapSelective = AllActivities.hashInfoSelective;

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            ArrayList<String> testChooses = extras.getStringArrayList("testsChoose");
            getTestsChoosesinBD(testChooses);
        }
    }

    private void getTestsChoosesinBD(ArrayList<String> testChooses){
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<TestTypes> tests = new ArrayList<TestTypes>();
        for (String id : testChooses ){
            db.openDataBase();
            tests.add(db.getTestTypeFromId(id));
            db.close();
        }
        inflateListTests(tests);
    }

    private void inflateListTests(ArrayList<TestTypes> tests){
        String[] values = new String[tests.size()];
        for(int i=0; i<=tests.size()-1; i++)
            values[i] = tests.get(i).getId();

        recyclerTests.setHasFixedSize(true);
        recyclerTests.setItemAnimator(new DefaultItemAnimator());
        recyclerTests.setLayoutManager(new GridLayoutManager(this, 1));
        adapterRecyclerTests = new AdapterRecyclerTestInfo(InfoSelectiveCreateActivity.this, tests, values);
        recyclerTests.setAdapter(adapterRecyclerTests);
    }

    private View.OnClickListener btnShowMoteTeam = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTeamInfo();
        }
    };
    private View.OnClickListener btnShowMoteDetails = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDetails();
        }
    };
    private View.OnClickListener btnShowMoteObservation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {showObservations();
        }
    };
    private View.OnClickListener btnShowMoteTests = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTestsInfo();
        }
    };

    private void showTeamInfo(){
        if(imgTeamChoose.getVisibility()==View.GONE) {
            imgshowMoreTeam.setImageDrawable(this.getDrawable(R.drawable.ic_show_less_line));
            imgTeamChoose.setVisibility(View.VISIBLE);
        }
        else{
            imgshowMoreTeam.setImageDrawable(this.getDrawable(R.drawable.ic_show_more_line));
            imgTeamChoose.setVisibility(View.GONE);
        }
    }

    private void showDetails(){
        if(constraintDetailsInfo.getVisibility()==View.GONE) {
            imgShowMoreDetails.setImageDrawable(this.getDrawable(R.drawable.ic_show_less_line));
            constraintDetailsInfo.setVisibility(View.VISIBLE);
        }
        else{
            imgShowMoreDetails.setImageDrawable(this.getDrawable(R.drawable.ic_show_more_line));
            constraintDetailsInfo.setVisibility(View.GONE);
        }
    }

    private void showObservations(){
        if(textObservationsDetails.getVisibility()==View.GONE) {
            imgShowMoreObservations.setImageDrawable(this.getDrawable(R.drawable.ic_show_less_line));
            textObservationsDetails.setVisibility(View.VISIBLE);
        }
        else{
            imgShowMoreObservations.setImageDrawable(this.getDrawable(R.drawable.ic_show_more_line));
            textObservationsDetails.setVisibility(View.GONE);
        }
    }

    private void showTestsInfo(){
        if(recyclerTests.getVisibility()==View.GONE) {
            imgShowMoreTests.setImageDrawable(this.getDrawable(R.drawable.ic_show_less_line));
            recyclerTests.setVisibility(View.VISIBLE);
        }
        else{
            imgShowMoreTests.setImageDrawable(this.getDrawable(R.drawable.ic_show_more_line));
            recyclerTests.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {InfoSelectiveCreateActivity.this.finish();}
    };

    private View.OnClickListener btnCreateSelectiveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callCreateSelective();
        }
    };

    private View.OnClickListener btnCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            constraintPrivacy.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener btnAcceptedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isAcceptedPrivacy = true;
            constraintPrivacy.setVisibility(View.GONE);
            textPrivacy.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener textClickedPrivacy = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            constraintPrivacy.setVisibility(View.VISIBLE);
        }
    };


    /************************* CRIAÇÃO DE SELETIVA ***********/
    private void callCreateSelective(){
        if(isAcceptedPrivacy){
            if(Services.isOnline(this)){
                constraintProgress.setVisibility(View.VISIBLE);
                textProgress.setText("Criando sua seletiva");
                createSelective();
            }
            constraintNotConnection.setVisibility(View.VISIBLE);
        }
    }

    private void createSelective(){
        String url = Constants.URL + Constants.API_SELECTIVES;
        PostCreateSelective post = new PostCreateSelective();
        post.setActivity(InfoSelectiveCreateActivity.this);
        post.setObjPut(CreateJSON.createObjectSelective(createObjectSelective()));
        post.execute(url);
    }

     public static void returnCreateSelective(Activity act, String response, String result){
        ((InfoSelectiveCreateActivity)act).returnCreateSelective(response, result);
    }

    private void returnCreateSelective(String response, String result){
        constraintProgress.setVisibility(View.GONE);
        if(response.toUpperCase().equals("OK")){
            try {
                JSONObject json = new JSONObject(result);
                String code = json.getString(Constants.SELECTIVES_CODESELECTIVE);
                Services.messageAlert(this, "Seletiva criada!!", "Parabéns a sua seletiva foi criada", "createSelective");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static void returnClickableAlert(Activity act, String whoCalled){
        ((InfoSelectiveCreateActivity)act).returnClickableAlert(whoCalled);
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

    private Selective createObjectSelective() {
        String date = Services.convertDate(hashMapSelective.get("date"));
        Selective selective = new Selective();
        selective.setTeam(hashMapSelective.get("team"));
        selective.setTitle(hashMapSelective.get("title"));
        selective.setCity(hashMapSelective.get("city"));
        selective.setDate(date);
        selective.setNeighborhood(hashMapSelective.get("neighborhood"));
        selective.setPostalCode(hashMapSelective.get("cep"));
        selective.setState(hashMapSelective.get("state"));
        selective.setStreet(hashMapSelective.get("street"));
        selective.setNotes(hashMapSelective.get("notes"));
        selective.setAddress(returnAddress());
        selective.setCanSync(true);
        selective.setCodeSelective("");
        selective.setCodeSelective(returnCodeSelective());
        return selective;
    }

    private String returnAddress(){
        return hashMapSelective.get("cep")+" ("+hashMapSelective.get("neighborhood")+
                " - "+hashMapSelective.get("city")+
                ", "+hashMapSelective.get("street")+
                " - "+hashMapSelective.get("state")+
                ") - "+hashMapSelective.get("complement");
    }

    private String returnCodeSelective(){
        DatabaseHelper db = new DatabaseHelper(this);
        db.openDataBase();
        return  String.format(hashMapSelective.get("title").toString().substring(0,2)
                +db.getNameTeamByIdTeam(hashMapSelective.get("team").toString()).toString().substring(0,2)
                +hashMapSelective.get("date").toString().substring(0,2)).trim().toUpperCase();
    }

    private View.OnClickListener btnClickedTryAgain = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            callCreateSelective();
        }
    };

}

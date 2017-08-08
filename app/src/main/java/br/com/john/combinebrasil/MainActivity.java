package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAsyncTask;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.AppSectionsPagerAdapter;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.NavigationTestsDrawer;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;

public class MainActivity extends AppCompatActivity {
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPagerHome;
    public static ConstraintLayout constraintProgress, constraintNoConnection;
    public static TextView textProgress;
    public static final int METHOD_ATHLETE_SELECTIVE_GET=1231;

    Toolbar toolbar;
    NavigationTestsDrawer navigationDrawer;

    ArrayList<SelectiveAthletes> sele=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolbar);

        AllActivities.mainActivity = MainActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        DatabaseHelper db = new DatabaseHelper(this);
        User user = db.getUser();
        boolean isAdmin = db.getSelective().getUser()!=null ? db.getSelective().getUser().equals(user.getId()) : false;
        navigationDrawer = new NavigationTestsDrawer(savedInstanceState, this, toolbar, user, isAdmin);
        navigationDrawer.createNavigationAccess();

        LinearLayout linearBacktoolbar = (LinearLayout) findViewById(R.id.linear_back_button);
        linearBacktoolbar.setVisibility(View.GONE);

        LinearLayout linearAdd = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAdd.setOnClickListener(clickAddAccount);

        constraintNoConnection = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        textProgress = (TextView) findViewById(R.id.text_progress);

        Fragment fragments[] = new Fragment[]{new TestsFragment(),
                new PlayersFragment()};

        String tabIcons[] = {getResources().getString(R.string.tests),
                getResources().getString(R.string.players)};

        String tabTitles[] = new String[]{"Tab1", "Tab2"};

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        mAppSectionsPagerAdapter.setTabIcons(tabIcons);
        mAppSectionsPagerAdapter.setTabTitles(tabTitles);
        mAppSectionsPagerAdapter.setFragments(fragments);

        mViewPagerHome = (ViewPager) findViewById(R.id.pager);
        mViewPagerHome.setOffscreenPageLimit(2);
        mViewPagerHome.setAdapter(mAppSectionsPagerAdapter);

        final PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        tabs.setViewPager(mViewPagerHome);
        tabs.setSmoothScrollingEnabled(true);
        tabs.setHorizontalFadingEdgeEnabled(true);
        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }
        });

        if (mAppSectionsPagerAdapter != null) {
            for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            }
        }
        if(!verifyDate()) {
           this.callTests();
        }

    }
    private void callTests(){
        if(Services.isOnline(MainActivity.this)) {
            constraintProgress.setVisibility(View.VISIBLE);
            textProgress.setText("Verificando testes...");
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            Selective sel = db.getSelective();
            String url = Constants.URL + Constants.API_SELECTIVE_TESTTYPES+"?"+Constants.TESTS_SELECTIVE+"="+sel.getId();
            Connection task = new Connection(url, 0, Constants.CALLED_GET_TESTTYPES,  false, MainActivity.this);
            task.callByJsonStringRequest();
        }
        else
            constraintNoConnection.setVisibility(View.VISIBLE);
    }

    public static void testResponse(Activity act,String response, int status) {
        ((MainActivity)act).testResponse(response, status);
    }

    private void testResponse(String response, int status){
        constraintProgress.setVisibility(View.GONE);
        if(status == 200 || status ==201){
            try {
                AllActivities.isSync = false;
                DeserializerJsonElements des = new DeserializerJsonElements(response);
                ArrayList<TestTypes> tests = des.getSelectiveTestType();
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                db.openDataBase();
                db.deleteTable(Constants.TABLE_TESTTYPES);
                db.addTestTypes(tests);
                db.close();
                MainActivity.finishSync(MainActivity.this);
            } catch (SQLException sqle) {
                Services.messageAlert(MainActivity.this, "Mensagem", sqle.getMessage(), "");
                throw sqle;
            }
        }
    }

    public static void finishSync(Activity act){
        ((MainActivity)act).finishSync();
    }

    private void finishSync(){
        TestsFragment.callInflateTests();
        callPlayersSelective();
    }

    private void callPlayersSelective(){
        constraintProgress.setVisibility(View.VISIBLE);
        textProgress.setText("Verificando atletas...");
        String url = Constants.URL+Constants.API_SELECTIVE_ATHLETES_SEARCH;
        PostAsyncTask post = new PostAsyncTask();
        post.setActivity(this);
        post.setContext(this);
        post.setMethod(METHOD_ATHLETE_SELECTIVE_GET);
        post.setWhoCalled("METHOD_ATHLETE_SELECTIVE_GET");
        post.setObjPut(createObjAthleteSelectiveGet());
        post.execute(url);
    }

    private JSONObject createObjAthleteSelectiveGet(){
        JSONObject jsonObject = new JSONObject();
        try{
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            Selective sel = db.getSelective();

            JSONObject query = new JSONObject();
            query.put("selective", sel.getId());
            jsonObject.put("query", query);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put("athlete");

            jsonObject.put("populate", jsonArray);

        }catch (Exception ex){
            Log.i("Exception", ex.getMessage());
        }

        return jsonObject;
    }

    public static void returnGetAthleteSelective(Activity act, String response, int status){
        ((MainActivity)act).returnGetAthleteSelective(response, status);
    }
    private void returnGetAthleteSelective(String result, int status){
        constraintProgress.setVisibility(View.GONE);
        if(status == 200 || status ==201) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i=0; i<=jsonArray.length()-1;i++){

                    String strAthlete = jsonArray.getJSONObject(i).getString("athlete");
                    JSONObject jsonObject = new JSONObject(strAthlete);
                    jsonObject.put(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER,
                            jsonArray.getJSONObject(i).getString(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER));

                    DatabaseHelper db = new DatabaseHelper(this);
                    DeserializerJsonElements des = new DeserializerJsonElements(jsonObject.toString());
                    db.addAthlete(des.getObjAthlete());
                }

                PlayersFragment.callInflateAthletes();
            } catch (Exception ex) {
                Log.i("GetAthleteSelective", ex.getMessage());
            }
        }
    }





    /************************** CLICK LIST **********************************/
    public static void onClickItemList(Activity activity, int positionArray, String id){
        ((MainActivity)activity).validaClick(positionArray, id);
    }

    public void validaClick(int position, String id){
        Intent i = new Intent(MainActivity.this, AthletesActivity.class);
        TestTypes test = TestsFragment.testsArrayList.get(position);
        AllActivities.testSelected = id;
        i.putExtra("id_test", test.getId());
        startActivity(i);
    }

    public static void onClickItemList(Activity activity, int positionArray){
        ((MainActivity)activity).validaClickAthlete(positionArray);
    }
    public void validaClickAthlete(int position){
        Intent i = new Intent(MainActivity.this, DetailsAthletes.class);
        Athletes player  = PlayersFragment.playersArrayList.get(position);
        i.putExtra("id_player",player.getId());
        startActivity(i);
    }

    private View.OnClickListener clickAddAccount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, CreateAccountAthleteActivity.class);
            startActivity(i);
        }
    };

    @Override
    public void onBackPressed() {
            int position = mViewPagerHome.getCurrentItem();
            if (position == 0) {
                MainActivity.this.finish();
            } else {
                mViewPagerHome.setCurrentItem(0);
            }

    }

    @Override
    public void onRestart(){
        super.onRestart();
        PlayersFragment.callInflateAthletes();
        TestsFragment.callInflateTests();
    }

    public static void returnMessageOptions(Activity act, String whoCalled){
        ((MainActivity)act).returnMessageOptions(whoCalled);
    }

    private void returnMessageOptions(String whoCalled){
        if(whoCalled.equals("update")){
            updateAthletes();
        }
        else if(whoCalled.equals("exit")){
            exit();
        }
        else if(whoCalled.equals("exit_selective")){
            exit_selective();
        }
    }

    private void exit(){
        SharedPreferencesAdapter.cleanAllShared(this);
        this.deleteDatabase(Constants.NAME_DATABASE);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        this.finish();
    }

    private void exit_selective(){
        SharedPreferencesAdapter.clearData(this, Constants.SELECTIVES_ID_ENTER);
        SharedPreferencesAdapter.clearData(this, Constants.SELECTIVES_CODESELECTIVE);
        SharedPreferencesAdapter.setEnterSelectiveSharedPreferences(this,false);
        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteTable(Constants.TABLE_SELECTIVES);
        db.deleteTable(Constants.TABLE_ATHLETES);
        db.deleteTable(Constants.TABLE_TESTTYPES);
        db.deleteTable(Constants.TABLE_TESTS);
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        this.finish();
    }

    private void updateAthletes(){
        if(Services.isOnline(MainActivity.this)){
            constraintProgress.setVisibility(View.VISIBLE);
            textProgress.setText("Atualziando Atletas");
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            Selective selective = db.getSelective();
            String url = Constants.URL+Constants.API_SELECTIVEATHLETES+"?"+Constants.SELECTIVEATHLETES_SELECTIVE+"="+
                    selective.getId();
            Connection task =new Connection(url, 0,"UpdateSelectiveAthletes",false, MainActivity.this);
            task.callByJsonStringRequest();
        }
        else
            Services.messageAlert(MainActivity.this, "Aviso","è necessário conexão com a internet para atualizar.","");
    }

    public static void returnUpdateSelectiveAthletes(Activity activity, String response, int statusCode){
        ((MainActivity)activity).returnUpdateSelectiveAthletes(response, statusCode);
    }
    private void returnUpdateSelectiveAthletes(String response, int statusCode) {
        if (statusCode == 200 || statusCode == 201) {
            constraintProgress.setVisibility(View.GONE);
            if (response.trim().equals("[]") || response.isEmpty()) {
                Services.messageAlert(MainActivity.this, "Mensagem", "Nenhum atleta para atualizar", "");
            } else {
                DeserializerJsonElements des = new DeserializerJsonElements(response);
                sele = des.getSelectiveAthletes();
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                db.openDataBase();
                db.addSelectivesAthletes(sele);
                String url = Constants.URL+Constants.API_ATHLETES;
                constraintProgress.setVisibility(View.VISIBLE);
                textProgress.setText("Atualziando Atletas");
                Connection task =new Connection(url, 0,"UpdateAthletes",false, MainActivity.this);
                task.callByJsonStringRequest();
            }
        }
        else
            Services.messageAlert(MainActivity.this, "Mensagem", "Erro ao tentar atualziar", "");

    }

    public static void returnUpdateAthletes(Activity act, String response, int status){
        ((MainActivity)act).returnUpdateAthletes(response, status);
    }
    private void returnUpdateAthletes(String response, int status){
        if (status == 200 || status == 201) {
            if (response.trim().equals("[]") || response.isEmpty()) {
                Services.messageAlert(MainActivity.this, "Mensagem", "Nenhum atleta para atualizar", "");
            } else {
                DeserializerJsonElements des = new DeserializerJsonElements(response);
                ArrayList<Athletes> athletes = des.getAthletes();
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                db.openDataBase();
                for(Athletes athlete : athletes){
                    Athletes obj = db.getAthleteById(athlete.getId());
                    if(obj==null){
                        SelectiveAthletes sel = db.getSelectiveAthletesFromAthlete(athlete.getId());
                        if(sel!=null){
                            athlete.setCode(sel.getInscriptionNumber());
                            db.addAthlete(athlete);
                        }
                    }

                    else if(obj.getCode().isEmpty()){
                        SelectiveAthletes sel = db.getSelectiveAthletesFromAthlete(athlete.getId());
                        if(sel!=null){
                            athlete.setCode(sel.getInscriptionNumber());
                            db.updateAthlete(athlete);
                        }
                    }
                }

                constraintProgress.setVisibility(View.GONE);
                Services.messageAlert(MainActivity.this, "Mensagem","Todos os atletas foram atualizados","");
                PlayersFragment.callInflateAthletes();
            }
        }
        else
            Services.messageAlert(MainActivity.this, "Mensagem", "Erro ao tentar atualziar", "");
    }
/*
    private View.OnClickListener closeAddAthlete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearAddAthlete.setVisibility(View.GONE);
            editCodeUser.setText("");
            editNameAthlete.setText("");
            editCodeAthlete.setText("");
        }
    };

    private View.OnClickListener clickAddAthlete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(verifyForm())
                callCreateAthlete(true);
        }
    };


        private void callCreateAthlete(boolean check){
            if(check){
                if(Services.isOnline(MainActivity.this)){
                    constraintProgress.setVisibility(View.VISIBLE);
                    String url = Constants.URL + Constants.API_SELECTIVEATHLETES+"?"+Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER+"="+editCodeAthlete.getText().toString().toUpperCase();
                    SyncDatabase.callFunc(url, "UPDATE_SELECTIVEATHLETE",  false, MainActivity.this);
                }
                else
                    createAthleteOff();
            }
            else
                createAthleteOff();
        }

        public static void updateSelectiveAthlete(Activity act, String response){
            ((MainActivity)act).updateSelectiveAthlete(response);
        }

        private void updateSelectiveAthlete(String response){
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            SelectiveAthletes selectiveAthlete = des.getSelectiveAthlete();

            if (response.equals("[]")) {
                callCreateAthlete(false);
            }
            else{
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                SelectiveAthletes sel = db.getSelectiveAthletesFromId(selectiveAthlete.getId());
                if (sel == null) {
                    db.addSelectiveAthlete(selectiveAthlete);

                    String url = Constants.URL + Constants.API_ATHLETES+"?"+Constants.ATHLETES_ID+"="+selectiveAthlete.getAthlete();
                    SyncDatabase.callFunc(url, "UPDATE_ATHLETE",  false, MainActivity.this);
                }
                else {
                    constraintProgress.setVisibility(View.GONE);
                    linearAddAthlete.setVisibility(View.GONE);
                    Services.messageAlert(MainActivity.this, "Mensagem", "O código informado, já esta cadastrado a um atleta.", "");
                }
            }

        }

        public static void updateAthlete(Activity act, String response){
            ((MainActivity)act).updateAthlete(response);
        }

        private void updateAthlete(String response){
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            Athletes athlete = des.getAthlete();

            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            SelectiveAthletes selectiveAthletes = db.getSelectiveAthletesFromAthlete(athlete.getId());
            athlete.setCode(selectiveAthletes.getInscriptionNumber());
            db.addAthlete(athlete);

            linearProgress.setVisibility(View.GONE);
            linearAddAthlete.setVisibility(View.GONE);
            PlayersFragment.callInflateAthletes();
            Services.messageAlert(MainActivity.this, "Mensagem", "Atleta foi cadastrado", "");
        }



        private void createAthleteOff(){
            try{
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);

                Athletes athlete = createAthlete();
                SelectiveAthletes selectiveAthlete = createSelectiveAthletes(athlete);
                athlete.setCode(selectiveAthlete.getInscriptionNumber());

                db.addAthlete(athlete);
                db.addSelectiveAthlete(selectiveAthlete);

                linearAddAthlete.setVisibility(View.GONE);
                linearProgress.setVisibility(View.GONE);
                Services.messageAlert(MainActivity.this, "Aviso","Atleta temporário criado. Para finalizar cadastro, o atleta deverá ser sincronizado.","");
                clearForm();
                PlayersFragment.callInflateAthletes();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private Athletes createAthlete(){
            Athletes athletes;
            try {
                athletes = new Athletes(
                        UUID.randomUUID().toString(),
                        editNameAthlete.getText().toString(),
                        " ",
                        " ",
                        " ",
                        " ",
                        0,
                        0,
                        " ",
                        " ",
                        editCodeAthlete.getText().toString(),
                        " ",
                        " ",
                        false,
                        true
                );
            }
            catch (Exception e){
                return null;
            }
            return athletes;

        }

        private SelectiveAthletes createSelectiveAthletes(Athletes athlete){
            SelectiveAthletes selectiveAthlete;
            try {
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                Selective selective = db.getSelective();

                selectiveAthlete = new SelectiveAthletes(
                        UUID.randomUUID().toString(),
                        athlete.getId(),
                        selective.getId(),
                        editCodeAthlete.getText().toString().toUpperCase(),
                        true
                );
            }catch (Exception e){
                return  null;
            }

            return selectiveAthlete;
        }

    private boolean verifyForm(){
        boolean ver = true;
        if(!valida(editNameAthlete))
            ver = false;
        if(!valida(editCodeAthlete))
            ver = false;
        if(!validaCodeSelective(editCodeUser))
            ver = false;
        if(!ver)
            Services.messageAlert(this, "Alerta","Dados inválidos, por favor, verifique para continuar.","");
        return ver;
    }

    public boolean validaCodeSelective(EditText edit) {
        boolean ver = false;
        if(getString(edit).length()>=3) {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            Selective selective = db.getSelective();
            if(selective!=null){
                if(selective.getCodeSelective().toLowerCase().equals(edit.getText().toString().toLowerCase())){
                    Services.changeColorEditBorder(edit, this);
                    ver = true;
                }
                else
                    Services.changeColorEditBorderError(edit, this);
            }
        }
        else
            Services.changeColorEditBorderError(edit, this);
        return ver;
    }

    public boolean valida(EditText edit) {
        boolean ver = false;
        if(getString(edit).length()>=3) {
            Services.changeColorEditBorder(edit, this);
            ver = true;
        }
        else
            Services.changeColorEditBorderError(edit, this);
        return ver;
    }

    private String getString(EditText edit){
        return edit.getText().toString().trim().equals("") ? "" : edit.getText().toString();
    }

    private void clearForm(){
        editNameAthlete.setText("");
        editCodeUser.setText("");
        editCodeAthlete.setText("");
    }*/

    private boolean verifyDate(){
        boolean ret = false;
        try {
            String dateLogin = SharedPreferencesAdapter.getValueStringSharedPreferences(MainActivity.this, Constants.DATE_LOGIN);
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
            String currentDate = df.format(c.getTime());

            Date date1 = df.parse(dateLogin);
            Date date2 = df.parse(currentDate);
            long diff = date2.getTime() - date1.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)>=3){
                ret = true;
                Services.messageAlert(MainActivity.this, "Mensagem","A sua versão de teste expirou.","exit");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }


}

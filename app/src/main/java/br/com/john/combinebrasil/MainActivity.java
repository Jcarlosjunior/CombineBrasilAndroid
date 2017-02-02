package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.AppSectionsPagerAdapter;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.NavigationDrawer;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;
import br.com.john.combinebrasil.Services.SyncDatabase;

public class MainActivity extends AppCompatActivity {
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPagerHome;
    public static LinearLayout linearProgress;
    public static TextView textProgress;
    private LinearLayout linearAddAthlete;
    private EditText editCodeUser, editCodeAthlete, editNameAthlete;
    private Button btnAdd, btnCancel;
    Toolbar toolbar;
    NavigationDrawer navigationDrawer;

    ArrayList<SelectiveAthletes>sele=null;

    ImageView imgCreateAthlete, imgUpdate, imgUpload, imgHelp, imgExit;
    LinearLayout linearMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolbar);

        AllActivities.mainActivity = MainActivity.this;

        /*navigationDrawer = new NavigationDrawer(savedInstanceState, toolbar, true);
        navigationDrawer.createNavigationAccess();*/

        LinearLayout linearBacktoolbar = (LinearLayout) findViewById(R.id.linear_back_button);
        linearBacktoolbar.setVisibility(View.GONE);

        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setOnClickListener(clickAddAccount);

        linearAddAthlete = (LinearLayout) findViewById(R.id.linear_add_athlete);
        linearAddAthlete.setVisibility(View.GONE);

        editCodeAthlete = (EditText) findViewById(R.id.edit_code_athlete);
        editCodeUser = (EditText) findViewById(R.id.edit_code_selective);
        editNameAthlete = (EditText) findViewById(R.id.edit_name_add);

        btnAdd = (Button) findViewById(R.id.button_add_athlete);
        btnCancel = (Button) findViewById(R.id.button_cancel);
        btnAdd.setOnClickListener(clickAddAthlete);
        btnCancel.setOnClickListener(closeAddAthlete);

        final LinearLayout btnMenu = (LinearLayout) findViewById(R.id.linear_menu_button);
        btnMenu.setVisibility(View.VISIBLE);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.VISIBLE);
                //NavigationDrawer.navigationDrawerLeft.openDrawer();
            }
        });

        createMenu();

        linearProgress = (LinearLayout) findViewById(R.id.linear_progress_tests);
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
        if(AllActivities.isSync)
            this.syncAll();
        AllActivities.isSync = false;

    }

    public static void callSync(Activity act){
        ((MainActivity)act).syncAll();
    }
    private void syncAll(){
        if(Services.isOnline(MainActivity.this)) {
            linearProgress.setVisibility(View.VISIBLE);
            try {
                SyncDatabase syncDatabase = new SyncDatabase(MainActivity.this);
                syncDatabase.initSyncDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Services.messageAlert(MainActivity.this, "Aviso","Você precisa estar conectado à internet para atualizar.","");
    }

    public static void finishSync(Activity act){
        ((MainActivity)act).finishSync();
    }
    private void finishSync(){
        linearProgress.setVisibility(View.GONE);
        PlayersFragment.callInflateAthletes();
        TestsFragment.callInflateTests();
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
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            User user = db.getUser();
            if(user!=null){
                if(user.getIsAdmin()){
                    Intent i = new Intent(MainActivity.this, CreateAccountAthlete.class);
                    startActivity(i);
                }
                else{
                    clearForm();
                    linearAddAthlete.setVisibility(View.VISIBLE);
                }

            }
        }
    };

    @Override
    public void onBackPressed() {
        /*if (NavigationDrawer.navigationDrawerLeft.isDrawerOpen())
            NavigationDrawer.navigationDrawerLeft.closeDrawer();*/

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

    Intent intent = null;
    private void createMenu(){
        linearMenu = (LinearLayout) findViewById(R.id.linear_menu);
        imgCreateAthlete = (ImageView) findViewById(R.id.img_create_menu);
        imgUpdate = (ImageView) findViewById(R.id.img_update_menu);
        imgUpload = (ImageView) findViewById(R.id.img_upload_menu);
        imgHelp = (ImageView) findViewById(R.id.img_help_menu);
        imgExit = (ImageView) findViewById(R.id.img_exit_menu);

        linearMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
            }
        });

        imgCreateAthlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                User user = db.getUser();
                if(user!=null){
                    if(user.getIsAdmin()){
                        Intent i = new Intent(MainActivity.this, CreateAccountAthlete.class);
                        startActivity(i);
                    }
                    else{
                        clearForm();
                        linearAddAthlete.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
                new MessageOptions(MainActivity.this, "Aualizar", "Deseja atualizar os atletas?", "update");
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
                intent = new Intent(MainActivity.this, SyncActivity.class);
                startActivity(intent);
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
                intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
                new MessageOptions(MainActivity.this, "Logout", "Ao sair todos os dados serão excluídos do aplicativo, deseja realmente sair?", "exit");
            }
        });
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
    }

    private void exit(){
        SharedPreferencesAdapter.cleanAllShared(this);
        this.deleteDatabase(Constants.NAME_DATABASE);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        this.finish();
    }

    private void updateAthletes(){
        if(Services.isOnline(MainActivity.this)){
            linearProgress.setVisibility(View.VISIBLE);
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
            linearProgress.setVisibility(View.GONE);
            if (response.trim().equals("[]") || response.isEmpty()) {
                Services.messageAlert(MainActivity.this, "Mensagem", "Nenhum atleta para atualizar", "");
            } else {
                DeserializerJsonElements des = new DeserializerJsonElements(response);
                sele = des.getSelectiveAthletes();
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                db.openDataBase();
                db.addSelectivesAthletes(sele);
                String url = Constants.URL+Constants.API_ATHLETES;
                linearProgress.setVisibility(View.VISIBLE);
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

                linearProgress.setVisibility(View.GONE);
                Services.messageAlert(MainActivity.this, "Mensagem","Todos os atletas foram atualizados","");
                PlayersFragment.callInflateAthletes();
            }
        }
        else
            Services.messageAlert(MainActivity.this, "Mensagem", "Erro ao tentar atualziar", "");
    }

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
                linearProgress.setVisibility(View.VISIBLE);
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
                linearProgress.setVisibility(View.GONE);
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
    }


}

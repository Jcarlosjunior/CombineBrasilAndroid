package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.AppSectionsPagerAdapter;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.NavigationDrawer;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;
import br.com.john.combinebrasil.Services.SyncDatabase;

public class MainActivity extends AppCompatActivity {
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPagerHome;
    public static LinearLayout linearProgress;
    public static TextView textProgress;
    Toolbar toolbar;
    NavigationDrawer navigationDrawer;

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
        linearProgress.setVisibility(View.VISIBLE);
        try {
            SyncDatabase syncDatabase = new SyncDatabase(MainActivity.this);
            syncDatabase.initSyncDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Intent i = new Intent(MainActivity.this, CreateAccountAthlete.class);
            startActivity(i);
        }
    };

    @Override
    public void onBackPressed() {
        if (NavigationDrawer.navigationDrawerLeft.isDrawerOpen())
            NavigationDrawer.navigationDrawerLeft.closeDrawer();
        else {
            int position = mViewPagerHome.getCurrentItem();
            if (position == 0) {
                MainActivity.this.finish();
            } else {
                mViewPagerHome.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        PlayersFragment.callInflateAthletes();
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
                intent = new Intent(MainActivity.this, CreateAccountAthlete.class);
                startActivity(intent);
            }
        });

        imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
                new MessageOptions(MainActivity.this, "Aualizar", "Deseja atualizar todos dados do aplicativo?", "update");
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
                intent = new Intent(MainActivity.this, SyncAthleteActivity.class);
                startActivity(intent);
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMenu.setVisibility(View.GONE);
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
            linearProgress.setVisibility(View.VISIBLE);
            syncAll();
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


}

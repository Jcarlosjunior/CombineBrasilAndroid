package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.AppSectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPagerHome;
    public static LinearLayout linearProgress;
    public static TextView textProgress;
    Toolbar toolbar;
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

        linearProgress = (LinearLayout) findViewById(R.id.linear_progress_tests);
        textProgress = (TextView) findViewById(R.id.text_progress);

        AllActivities.mainActivity = MainActivity.this;

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
    }

    public static void afterCalled(String response, boolean isList, Activity activity) {
        TestsFragment.ShowTests(response, isList);
    }

    /************************** CLICK LIST **********************************/
    public static void onClickItemList(Activity activity, int positionArray, String id){
        ((MainActivity)activity).validaClick(positionArray);
    }

    public void validaClick(int position){
        if(position==2 || position ==0)
            AllActivities.type="corrida";
        else
            AllActivities.type="";
        Intent i = new Intent(MainActivity.this, AthletesActivity.class);
        Tests test = TestsFragment.testsArrayList.get(position);
        i.putExtra("id_test", test.getId());
        i.putExtra("name_test", test.getName());
        i.putExtra("details_test", test.getDescription());
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
        int position = mViewPagerHome.getCurrentItem();
        if (position == 0) {
            MainActivity.this.finish();
        } else {
            mViewPagerHome.setCurrentItem(0);
        }
    }
}

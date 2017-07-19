package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.john.combinebrasil.AdapterList.AdapterCoverflow;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

public class ChooseTeamSelectiveActivity extends AppCompatActivity {
    Button btnAddTeam, btnNextPass;
    ImageView imgPrevious, imgNext;
    Toolbar toolbar;
    ViewPager pager;
    ArrayList<Team> teams;
    public static Activity act;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team_selective);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnClickListenter);

        LinearLayout btnMenu = (LinearLayout) findViewById(R.id.linear_menu_button);
        btnMenu.setVisibility(View.GONE);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText(R.string.create_selective);
        act = ChooseTeamSelectiveActivity.this;
        btnAddTeam= (Button) findViewById(R.id.button_add_team);
        btnNextPass = (Button) findViewById(R.id.button_next_pass);
        imgPrevious = (ImageView) findViewById(R.id.img_previous);
        imgNext = (ImageView) findViewById(R.id.img_next);
        btnAddTeam.setOnClickListener(clickAddTeamListener);
        btnNextPass.setOnClickListener(clickNextPassListener);
        imgPrevious.setOnClickListener(btnPreviousListener);
        imgNext.setOnClickListener(btnNextListener);

        teams = new ArrayList<Team>();

        PagerContainer mContainer = (PagerContainer) findViewById(R.id.pager_container);
        pager = mContainer.getViewPager();
        mContainer.setBackgroundColor(Color.argb(255,255,255,255));

        boolean showRotate = getIntent().getBooleanExtra("showRotate",true);

        if(showRotate){
            new CoverFlow.Builder()
                    .with(pager)
                    .scale(0.6f)
                    .pagerMargin(0.3f)
                    .spaceSize(0.7f)
                    .rotationY(0f)
                    .build();
        }
        getTeams();
    }

    private void getTeams(){
        if(Services.isOnline(ChooseTeamSelectiveActivity.this)) {
            hideNotConnect();
            showProgress(getString(R.string.update_teams));
            String url = Constants.URL + Constants.API_TEAMS+"?isStarTeam=true";
            Connection task = new Connection(url, 0, Constants.CALLED_GET_TEAM, false, ChooseTeamSelectiveActivity.this);
            task.callByJsonStringRequest();
        }
        else
            showNotConnect();
    }

    public static void returnGetAllTeams(Activity act, String response, int status){
        ((ChooseTeamSelectiveActivity)act).returnGetAllTeams(response, status);
    }

    private void returnGetAllTeams(String response, int status){
        hideProgress();
        if(status == 200 || status == 201) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            ArrayList<Team> teams = des.getTeam();

            try{
                if (teams!=null) {
                    recordingTeams(teams);
                    inflateTeam(teams);
                }
            }catch (Exception e){Log.i("Exception: ", e.getMessage());}
        }
        else
            Services.messageAlert(this, "Aviso",response,"hide");
    }

    private void recordingTeams(ArrayList<Team> teams){
        DatabaseHelper db = new DatabaseHelper(ChooseTeamSelectiveActivity.this);
        try {
            db.createDataBase();
            if(teams!=null) {
                db.openDataBase();
                db.addTeam(teams);
            }
        } catch (Exception e) {}
    }

    private void inflateTeam(ArrayList<Team> teams){
        this.teams = teams;
        PagerAdapter adapter = new AdapterCoverflow(ChooseTeamSelectiveActivity. this, teams);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setClipChildren(false);
    }

    private View.OnClickListener btnPreviousListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getItem(+1)>0)
                pager.setCurrentItem(getItem(-1), true);
        }
    };

    private View.OnClickListener btnNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getItem(+1)<teams.size())
                pager.setCurrentItem(getItem(+1), true);
        }
    };

    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

    private View.OnClickListener btnClickListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener clickAddTeamListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChooseTeamSelectiveActivity.this, CreateTeamActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener clickNextPassListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nextPass();
        }
    };

    private void nextPass(){
        Intent intent = new Intent(ChooseTeamSelectiveActivity.this, LocalSelectiveActivity.class);
        AllActivities.hashInfoSelective = new HashMap<String, String>();
        AllActivities.hashInfoSelective.put("team", teams.get(getItem(0)).getId());
        startActivity(intent);
    }

    private void showNotConnect(){
        TextView textMessage = (TextView) findViewById(R.id.txt_message_not_connect);
        textMessage.setText(Html.fromHtml(getString(R.string.no_connection)));
        ConstraintLayout constraintNoConnect = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        constraintNoConnect.setVisibility(View.VISIBLE);
        Button btnTryAgain =(Button) findViewById(R.id.btn_try_again_connect);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTeams();
            }
        });
    }
    private void hideNotConnect(){
        ConstraintLayout constraintNoConnect = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        constraintNoConnect.setVisibility(View.GONE);
    }

    private void showProgress(String message){
        ConstraintLayout constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        TextView textProgress = (TextView) findViewById(R.id.text_progress);
        textProgress.setText(message);
        constraintProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress (){
        ConstraintLayout constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraintProgress.setVisibility(View.GONE);
    }


    public static void finishActity (){
        ((ChooseTeamSelectiveActivity)act).finish();
    }
}
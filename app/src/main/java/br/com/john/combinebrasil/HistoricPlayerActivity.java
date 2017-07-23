package br.com.john.combinebrasil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerResultsTest;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerTestInfo;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerTests;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Connection.JSONServices.CreateJSON;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAsyncTask;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.ImageConverter;
import br.com.john.combinebrasil.Services.ImageLoadedCallback;
import br.com.john.combinebrasil.Services.Services;

public class HistoricPlayerActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String idAthlete, idSelective;
    TextView textNamePlayer, textEmailPlayer, textPhonePlayer, textBirthdayPlayer, textAddressPlayer, textHeightPlayer, textWeightPlayer;
    ImageView imagePlayer;
    RecyclerView recyclerTests, recyclerPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_player);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(clickedBack);
        LinearLayout btnMenu = (LinearLayout) findViewById(R.id.linear_menu_button);
        btnMenu.setVisibility(View.GONE);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setImageDrawable(this.getDrawable(R.drawable.ic_filter_list_white_24dp));
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText(R.string.players);

        imagePlayer = (ImageView) findViewById(R.id.image_player);

        textNamePlayer=(TextView)findViewById(R.id.text_player);
        textEmailPlayer=(TextView)findViewById(R.id.text_email_player);
        textPhonePlayer=(TextView)findViewById(R.id.text_phone_player);
        textBirthdayPlayer=(TextView)findViewById(R.id.text_birthday_player);
        textAddressPlayer=(TextView)findViewById(R.id.text_local_player);
        textHeightPlayer=(TextView)findViewById(R.id.text_player_height);
        textWeightPlayer=(TextView)findViewById(R.id.text_player_weight);

        recyclerTests=(RecyclerView)findViewById(R.id.recycler_player_tests);
        recyclerPositions=(RecyclerView)findViewById(R.id.recycler_position);

        Bundle extras = getIntent().getExtras();
        if(extras !=null){
            idAthlete = extras.getString("id_athlete");
            idSelective = extras.getString("id_selective");
            callInfoSelectivePlayer();
            callResultsAthletes();
        }
    }

    private void callInfoSelectivePlayer(){
        DatabaseHelper db = new DatabaseHelper(this);
        Athletes athlete = db.getAthleteById(idAthlete);
        showInfoAthlete(athlete);
    }

    private void showInfoAthlete(Athletes athlete){
        textNamePlayer.setText(athlete.getName());
        textAddressPlayer.setText("Mora "+athlete.getAddress());
        textEmailPlayer.setText(athlete.getEmail());
        textPhonePlayer.setText(athlete.getPhoneNumber());
        textBirthdayPlayer.setText("Nascido em "+returnDateTime(athlete.getBirthday()));
        textHeightPlayer.setText(String.valueOf(athlete.getHeight()).replace(".",",") +" metros");
        textWeightPlayer.setText(String.valueOf(athlete.getWeight()).replace(".",",")+" Kg");

        showImageAthlete((athlete.getURLImage()!=null&&!athlete.getURLImage().isEmpty())?athlete.getURLImage():"https://scontent.fsjk2-1.fna.fbcdn.net/v/t1.0-9/17553779_1335695839811094_3378390160014516983_n.jpg?oh=81e4c17d65ebcab4b7e3142e662ddc9e&oe=59F8FAD6");
    }

    private String returnDateTime(String dateTime){
        dateTime = dateTime.replace("[","").replace("]","");
        String year = dateTime.substring(1, 5);
        String month = dateTime.substring(6, 8);
        String day = dateTime.substring(9, 11);

        return day + "/" + month + "/" + year;
    }

    private void showImageAthlete(String urlImage){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_image);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(urlImage)
                .into(imagePlayer,  new ImageLoadedCallback(progressBar) {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            roundedImage();
                        }
                    }
                    @Override
                    public void onError(){
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void roundedImage(){
        Bitmap bitmap = ((BitmapDrawable)imagePlayer.getDrawable()).getBitmap();
        bitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 200);
        imagePlayer.setImageBitmap(bitmap);
    }

    private void callResultsAthletes(){
        if(Services.isOnline(this)){
            showProgress("Verificando resultados do atleta...   ");
            PostAsyncTask postAsyncTask = new PostAsyncTask();
            String url = Constants.URL+Constants.API_RESULTS_SELECTIVE_ATHLETES_SEARCH;
            postAsyncTask.setActivity(this);
            postAsyncTask.setObjPut(querySearchData());
            postAsyncTask.setWhoCalled(Constants.CALLED_RESULTS_ATHLETE);
            postAsyncTask.execute(url);
        }
        else
            showNotConnection();
    }

    public static void returnGetResultsSelectiveAthlete(Activity act, String result, int status){
        ((HistoricPlayerActivity)act).returnGetResultsSelectiveAthlete(result, status);
    }

    private void returnGetResultsSelectiveAthlete(String result, int status){
        hideProgress();
        if(status == 200 || status == 201){
            DeserializerJsonElements des = new DeserializerJsonElements(result);
            ArrayList<Tests> tests = des.getTest();

        }
    }

    private void initInflateTests(ArrayList<Tests> tests){
        if(tests!=null && tests.size()>0){
            String[] values = new String[tests.size()];
            for(int i=0; i<=tests.size()-1; i++){
                values[i] = tests.get(i).getId();
            }
            inflateTests(tests, values);
        }
    }

    private void inflateTests(ArrayList<Tests> tests, String[] values){
        AdapterRecyclerResultsTest adapter = new AdapterRecyclerResultsTest(this, tests, values);
        recyclerTests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerTests.setVisibility(View.VISIBLE);
        recyclerTests.setAdapter(adapter);
    }

    private JSONObject querySearchData() {
        JSONObject object = new JSONObject();
        try {
            JSONObject jsonQuery = new JSONObject();
            jsonQuery.put(Constants.SELECTIVEATHLETES_SELECTIVE, idSelective);
            object.put("query", jsonQuery);
            JSONArray jsonDates = new JSONArray();
            jsonDates.put(Constants.SELECTIVEATHLETES_ATHLETE);
            object.put("populate", jsonDates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void showProgress(String message){
        ConstraintLayout constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        TextView textProgress = (TextView) findViewById(R.id.text_progress);
        textProgress.setText(message);
        constraintProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        ConstraintLayout constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraintProgress.setVisibility(View.GONE);
    }

    private void showNotConnection(){
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        Button button = (Button) findViewById(R.id.btn_try_again_connect);
        constraintLayout.setVisibility(View.VISIBLE);
        button.setOnClickListener(clickTryAgain);
    }

    private View.OnClickListener clickTryAgain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callResultsAthletes();
            hideNotConnection();
        }
    };
    private void hideNotConnection(){
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        constraintLayout.setVisibility(View.GONE);
    }

    private View.OnClickListener clickedBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}

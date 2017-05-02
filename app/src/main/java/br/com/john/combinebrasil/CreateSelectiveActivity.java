package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.CEP;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.CreateJSON;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostCreateSelective;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.Services;

public class CreateSelectiveActivity extends AppCompatActivity{
    private CoordinatorLayout coordinatorLayout;
    MaterialBetterSpinner spinnerDay, spinnerMonth, spinnerYear;
    Spinner spinnerTeam;
    EditText editTitle, editCep, editComplement, editCity, editNeighborhood, editState,editStreet, editNotes;
    LinearLayout linearProgress;
    Button btnCreateSelective;
    ImageView imageAddTeam;
    TextView textProgress;
    Toolbar toolbar;
    ArrayList<Team> teams;
    int posSelectedTeam = 0;
    public static Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_selective);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        act = this;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_create_selective);
        textProgress = (TextView) findViewById(R.id.text_progress);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);

        spinnerTeam = (Spinner) findViewById(R.id.spinner_team);
        spinnerDay = (MaterialBetterSpinner) findViewById(R.id.spinner_day_selective);
        spinnerMonth = (MaterialBetterSpinner) findViewById(R.id.spinner_month_selective);
        spinnerYear = (MaterialBetterSpinner) findViewById(R.id.spinner_year_selective);

        editTitle = (EditText) findViewById(R.id.edit_title);
        editCep = (EditText) findViewById(R.id.edit_cep);
        editComplement = (EditText) findViewById(R.id.edit_complement);
        editCity = (EditText) findViewById(R.id.edit_city);
        editNeighborhood = (EditText) findViewById(R.id.edit_neighborhood);
        editState = (EditText) findViewById(R.id.edit_state);
        editStreet = (EditText) findViewById(R.id.edit_street);
        editNotes = (EditText) findViewById(R.id.edit_notes);

        imageAddTeam = (ImageView) findViewById(R.id.image_add_team);

        btnCreateSelective = (Button) findViewById(R.id.btn_create_selective);

        btnCreateSelective.setOnClickListener(clickCreateSelective);
        imageAddTeam.setOnClickListener(clickAddTeam);

        Mask maskCpf = new Mask("#####-###", editCep);
        editCep.addTextChangedListener(maskCpf);

        editCep.addTextChangedListener(textListenerCep);

        spinnerDay.setAdapter(Services.inflateSpinnerDay(CreateSelectiveActivity.this));
        spinnerMonth.setAdapter(Services.inflateSpinnerMonth(CreateSelectiveActivity.this));
        spinnerYear.setAdapter(Services.inflateSpinnerYear(CreateSelectiveActivity.this));

        getAllTeams();
        //spinnerTeam.setOnItemSelectedListener(clickedSpinerTeam);



    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateSelectiveActivity.this.finish();
        }
    };

    private void getAllTeams(){
        if(Services.isOnline(CreateSelectiveActivity.this)) {
            linearProgress.setVisibility(View.VISIBLE);
            textProgress.setText("Atualizando equipes");

            String url = Constants.URL + Constants.API_TEAMS;
            Connection task = new Connection(url, 0, Constants.CALLED_GET_TEAM, false, CreateSelectiveActivity.this);
            task.callByJsonStringRequest();
        }
    }

    public static void returnGetAllTeams(Activity act, String response, int status){
        ((CreateSelectiveActivity)act).returnGetAllTeams(response, status);
    }
    private void returnGetAllTeams(String response, int status){
        linearProgress.setVisibility(View.GONE);
        if(status == 200 || status == 201) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            ArrayList<Team> teams = des.getTeam();
            try{
                if (teams!=null)
                    recordingTeams(teams);
            }catch (Exception e){}
        }
    }
    private void recordingTeams(ArrayList<Team> teams){
        DatabaseHelper db = new DatabaseHelper(CreateSelectiveActivity.this);
        try {
            db.createDataBase();
            if(teams!=null) {
                db.openDataBase();
                db.addTeam(teams);
                inflateTeam(teams);
            }
        } catch (Exception e) {}
    }

    private void inflateTeam(ArrayList<Team> team){
        this.teams = team;
        String[] arrayTeams = new String[team.size()];
        for(int x = 0; x<=team.size()-1;x++ ){
            arrayTeams[x] = team.get(x).getName();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateSelectiveActivity.this, android.R.layout.simple_dropdown_item_1line, arrayTeams);
        spinnerTeam.setAdapter(arrayAdapter);
        spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posSelectedTeam = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private TextWatcher textListenerCep = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().length()==9){
                searchAddress(editCep.getText().toString());
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void searchAddress(String cep){
        showLoadingAddress();
        String url = Constants.URLCep+cep+"/json/";
        Connection task = new Connection(url, 0, Constants.CALLED_GET_CEP, false, CreateSelectiveActivity.this);
        task.callByJsonStringRequest();
    }

    private void showLoadingAddress(){
        editCity.setText("Localizando cidade...");
        editState.setText("Localizando...");
        editStreet.setText("Localizando rua...");
        editNeighborhood.setText("localizando bairro...");
    }

    public static void returnCEP(Activity act, String response, int statusCode){
        ((CreateSelectiveActivity)act).returnCEP(response, statusCode);
    }

    private void returnCEP(String response, int statusCode){
        if(statusCode == 200 || statusCode == 201) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            CEP cep = des.getCep();
            showAddress(cep);
        }
        else
            showAddressNotFound();
    }

    private void showAddress(CEP cep){
        editStreet.setText(cep.getStreet());
        editCity.setText(cep.getCity());
        editNeighborhood.setText(cep.getNeighborhood());
        editState.setText(cep.getState());
    }

    private void showAddressNotFound(){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Endereço não encontrado", Snackbar.LENGTH_SHORT);
        snackbar.show();

        editCity.setText("");
        editState.setText("");
        editStreet.setText("");
        editNeighborhood.setText("");
    }

    private View.OnClickListener clickAddTeam = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CreateSelectiveActivity.this, CreateTeamActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener clickCreateSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(CreateSelectiveActivity.this, TestSelectiveActivity.class);
            //i.putExtra("id_selective",teams.get(posSelectedTeam).getId());
            startActivity(i);

            /*if(verifyFields()) {
                if(validaSpinners())
                    createSelective();
            }*/
        }
    };

    private void createSelective(){
        if(Services.isOnline(CreateSelectiveActivity.this)){
            linearProgress.setVisibility(View.VISIBLE);
            textProgress.setText("Criando sua seletiva");

            String url = Constants.URL + Constants.API_SELECTIVES;

            PostCreateSelective post = new PostCreateSelective();
            post.setActivity(CreateSelectiveActivity.this);
            post.setObjPut(CreateJSON.createObjectSelective(createObjectSelective()));
            post.execute(url);
        }
    }

    public static void returnCreateSelective(Activity act, String response, String result){
        ((CreateSelectiveActivity)act).returnCreateSelective(response, result);
    }
    private void returnCreateSelective(String response, String result){
        linearProgress.setVisibility(View.GONE);
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

    private Selective createObjectSelective() {
        String date = spinnerYear.getText().toString() + "-" + Services.convertMonthInNumber(spinnerMonth.getText().toString()) + "-" + spinnerDay.getText().toString();

        Selective selective = new Selective();
        selective.setTeam(teams.get(posSelectedTeam).getId());
        selective.setTitle(editTitle.getText().toString());
        selective.setCity(editCity.getText().toString());
        selective.setDate(date);
        selective.setNeighborhood(editNeighborhood.getText().toString());
        selective.setPostalCode(editCep.getText().toString());
        selective.setState(editState.getText().toString());
        selective.setStreet(editStreet.getText().toString());
        selective.setNotes(editNotes.getText().toString());
        selective.setAddress(returnAddress());
        selective.setCanSync(true);
        selective.setCodeSelective("");
        selective.setCodeSelective(String.format(editTitle.getText().toString().substring(0,2)+teams.get(posSelectedTeam).getName().toString().substring(0,2)+spinnerDay.getText().toString()).trim().toUpperCase());
        return selective;
    }

    private String returnAddress(){
        return editCep.getText().toString()+" ("+editNeighborhood.getText().toString()+" - "+editCity.getText().toString()+", "+editStreet.getText().toString()+" - "+editState.getText().toString()+") - "+editComplement.getText().toString();
    }

    private boolean verifyFields(){
        boolean ver = true;
        if(!validaEdit(editTitle))
            ver = false;
        if(!validaEdit(editCep))
            ver = false;
        if(!validaEdit(editCity))
            ver = false;
        if(!validaEdit(editNeighborhood))
            ver = false;
        if(!validaEdit(editStreet))
            ver = false;
        if(!validaEdit(editState))
            ver = false;
        if(!ver)
            Services.messageAlert(this, "Alerta","Dados inválidos, por favor, verifique para continuar.","");
        return ver;
    }

    private boolean validaSpinners(){
        boolean ver = false;
        if(spinnerDay.getText().toString().equals(""))
            Services.messageAlert(CreateSelectiveActivity.this, "Alerta","Dia da seletiva inválido","");
        else if(spinnerMonth.getText().toString().equals(""))
            Services.messageAlert(CreateSelectiveActivity.this, "Alerta","Mês da seletiva inválido","");
        else if(spinnerYear.getText().toString().equals(""))
            Services.messageAlert(CreateSelectiveActivity.this, "Alerta","Mês da seletiva inválido","");
        //else if(idTeam.toString().equals(""))
          //  Services.messageAlert(CreateSelectiveActivity.this, "Alerta","Você deve selecionar um time","");
        else
            ver=true;
        return ver;
    }

    public boolean validaEdit(EditText edit) {
        boolean ver = false;
        if(getString(edit).length()>=2) {
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

    public static void returnMessage(Activity act, String whoCalled){
        ((CreateSelectiveActivity)act).returnMessage(whoCalled);
    }
    private void returnMessage(String whoCalled){
        Intent i;
        if(whoCalled.equals("createSelective")){
            i = new Intent(CreateSelectiveActivity.this, TestSelectiveActivity.class);
            i.putExtra("id_selective",teams.get(posSelectedTeam).getId());
            startActivity(i);
        }
    }

    public static void finishActity (){
        ((CreateSelectiveActivity)act).finish();
    }
}

package br.com.john.combinebrasil;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Connection.JSONServices.CreateJSON;
import br.com.john.combinebrasil.Connection.Posts.PostCreateSelective;
import br.com.john.combinebrasil.Connection.Posts.PostTeamAsyncTask;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Services;

public class CreateTeamActivity extends AppCompatActivity {
    Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private EditText editTeam, editCity, editEmail, editSocialLink;
    private MaterialBetterSpinner spinnerModality;
    private LinearLayout linearProgress;
    private TextView textProgress;
    private Button btnCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_create_team);
        spinnerModality =(MaterialBetterSpinner) findViewById(R.id.spinner_modality);
        editTeam = (EditText) findViewById(R.id.edit_name_team);
        editEmail = (EditText) findViewById(R.id.edit_email_team);
        editCity = (EditText) findViewById(R.id.edit_city_team);
        editSocialLink = (EditText) findViewById(R.id.edit_social_link);
        btnCreate = (Button) findViewById(R.id.btn_create_team);
        btnCreate.setOnClickListener(clickCreateTeam);

        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);
        textProgress = (TextView) findViewById(R.id.text_progress);

        spinnerModality.setAdapter(inflateModality());
    }
    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateTeamActivity.this.finish();
        }
    };

    private View.OnClickListener clickCreateTeam = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(verifyFields()){
                if(!spinnerModality.getText().toString().equals("")){
                    createTeam();
                }
                else
                    Services.messageAlert(CreateTeamActivity.this, "Alerta","Selecione uma modalidade","");
            }
        }
    };

    private void createTeam(){
        if(Services.isOnline(CreateTeamActivity.this)){
            linearProgress.setVisibility(View.VISIBLE);
            textProgress.setText("Criando seu time...");

            String url = Constants.URL + Constants.API_TEAMS;

            PostTeamAsyncTask post = new PostTeamAsyncTask();
            post.setActivity(CreateTeamActivity.this);
            post.setObjPut(CreateJSON.createObjectTeam(createObjectTeam()));
            post.execute(url);
        }
    }

    public static void returnCreateTeam(Activity act, String resp, String result){
        ((CreateTeamActivity)act).returnCreateTeam(resp, result);
    }

    private void returnCreateTeam(String resp, String result){
        linearProgress.setVisibility(View.GONE);
        if(resp.toUpperCase().equals("OK")){
            try {
                JSONObject json = new JSONObject(result);
                Services.messageAlert(CreateTeamActivity.this, "Mensagem","Equipe criada com sucesso","exitTeam");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            verifyError(result);
    }

    public static void returnMessage(Activity act, String methodCalled){
        ((CreateTeamActivity)act).returnMessage(methodCalled);
    }

    private void returnMessage(String methodCalled){
        if(methodCalled.toLowerCase().equals("exitteam"))
            CreateTeamActivity.this.finish();
    }

    private void verifyError(String result){
        JSONObject json;
        try{
            json = new JSONObject(result);
            Services.messageAlert(CreateTeamActivity.this, "Erro", json.getString("message"),"");
        }catch (JSONException e){
        }
    }

    private Team createObjectTeam() {
        Team team = new Team();
        team.setName(editTeam.getText().toString());
        team.setEmail(editEmail.getText().toString());
        team.setCity(editCity.getText().toString());
        team.setModality(spinnerModality.getText().toString());

        return team;
    }

    private boolean verifyFields(){
        boolean ver = true;
        if(!validaEdit(editTeam))
            ver = false;
        if(!Services.isValidEmail(editEmail, this))
            ver = false;
        if(!validaEdit(editCity))
            ver = false;
        if(!validaEdit(editSocialLink))
            ver = false;
        return ver;
    }

    public boolean validaEdit(EditText edit) {
        boolean ver = false;
        if(getString(edit).length()>=5) {
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

    private ArrayAdapter<String> inflateModality(){
        String[] spinnerMonthValues  = {"Flag X5", "Flag X8", "Full Pad", "No Pad"};
        ArrayAdapter<String> arrayModality = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerMonthValues);
        return arrayModality;
    }
}

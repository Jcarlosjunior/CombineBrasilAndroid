package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.john.combinebrasil.Classes.Login;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAthleteAsyncTask;
import br.com.john.combinebrasil.Connection.Posts.PostLogin;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;
import br.com.john.combinebrasil.Services.SyncDatabase;

public class LoginActivity extends Activity {

    EditText editPassword, editLogin;
    Button btnLogin;
    public static LinearLayout linearProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editPassword = (EditText) findViewById(R.id.edit_password);
        editLogin = (EditText) findViewById(R.id.edit_login);
        btnLogin = (Button) findViewById(R.id.btn_entrar);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress_login);

        btnLogin.setOnLongClickListener(onLongClickListener);
        btnLogin.setOnClickListener(onClickLoginListener);

        DatabaseHelper db = new DatabaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inflateDatabase(){
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            editLogin.setText("teste@teste.com.br");
            editPassword.setText("teste");
            return true;
        }
    };

    public View.OnClickListener onClickLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
               callLogin();
        }
    };

    private void callLogin() {
        if (verifyLogin()) {
            if (Services.isOnline(this)) {
                linearProgress.setVisibility(View.VISIBLE);
                String url = Constants.URL + Constants.login;
                PostLogin post = new PostLogin();
                post.setActivity(LoginActivity.this);
                post.setObjPut(loginData());
                post.execute(url);


               // Connection task = new Connection(Constants.URL + Constants.login, Request.Method.POST, Constants.CALLED_LOGIN, false, this, loginData());
                //Connection task = new Connection(url, Request.Method.GET, Constants.CALLED_GET_ATHLETES, false, this);
               //task.callByJsonStringRequest();
            }
            else
                Services.messageAlert(this,"Aviso", "Sem conexão com a internet","hide");
        }
    }

        public static void afterLogin(String response, boolean isList, Activity activity, int statusCode) {
            if(statusCode == 200)
                ((LoginActivity) activity).validaLogin(response, isList);
            else
                ((LoginActivity) activity).errorLogin(response, statusCode);
        }

    public void validaLogin(String response, boolean isList) {
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        User user = des.getObjectsUser();
        DatabaseHelper db = new DatabaseHelper(LoginActivity.this);
        db.addUser(user);

        SyncDatabase syncDatabase = new SyncDatabase();
        syncDatabase.setActivity(LoginActivity.this);
        SyncDatabase.athletesResponse(response);

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
        linearProgress.setVisibility(View.GONE);
        /*DeserializerJsonElements des = new DeserializerJsonElements(response);
        User user = new User();

        if (!isList)
            user = des.getObjectsUser();

        linearProgress.setVisibility(View.GONE);
        if (!response.equals("")) {
            //Guarda em memória do cel
            SharedPreferencesAdapter.setValueStringSharedPreferences(this, Constants.TOKEN, user.getToken());
            SharedPreferencesAdapter.setValueStringSharedPreferences(this, Constants.USERNAME, user.getUsername());
            SharedPreferencesAdapter.setValueStringSharedPreferences(this, Constants.NAME, user.getName());
        }
        Intent i = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(i);
        LoginActivity.this.finish();*/

    }

    private void errorLogin(String response, int statusCode){
        linearProgress.setVisibility(View.GONE);
        String message = "";
        if(statusCode == 400) {
            message = "";
            JSONObject json = null;
            try {
                json = new JSONObject(response);
                message = json.optString("message", "Usuário ou senha inválida");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Services.messageAlert(LoginActivity.this, "Aviso", message, "");
    }

    private JSONObject loginData() {
        JSONObject object = new JSONObject();
        try {
            object.put("email",  editLogin.getText().toString());
            object.put("password", editPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private boolean verifyLogin(){
        boolean ver = false;
        if(validateEmail(editLogin)) {
            if (validatePassword(editPassword)) {
                ver = true;
            }
        }
        return ver;
    }

    public boolean validateEmail(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=3)
            ver = true;
        else
            Services.changeColorEdit(edit, this.getString(R.string.erro_dados_invalidos), "Insira um usuário com válido", this);
        return ver;
    }

    public boolean validatePassword(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=3)
            ver = true;
        else
            Services.changeColorEdit(edit,this.getString(R.string.erro_dados_invalidos), "Insira uma senha válida", this);
       return ver;
    }

    private String getString(EditText edit){
        return edit.getText().toString().trim().equals("") ? "" : edit.getText().toString();
    }
}

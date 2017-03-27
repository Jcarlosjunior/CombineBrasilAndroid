package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostLogin;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;

public class LoginActivity extends Activity {

    EditText editPassword, editLogin;
    Button btnLogin;
    TextView textRegister;
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
        textRegister = (TextView) findViewById(R.id.text_register);

        if(Constants.debug)
        btnLogin.setOnLongClickListener(onLongClickListener);

        btnLogin.setOnClickListener(onClickLoginListener);
        textRegister.setOnClickListener(onClickRegister);

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

    private View.OnClickListener onClickRegister = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(mainIntent);
        }
    };

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
        linearProgress.setVisibility(View.GONE);
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        User user = des.getLogin();

        SharedPreferencesAdapter.setLoggedSharedPreferences(LoginActivity.this, true);
        SharedPreferencesAdapter.setValueStringSharedPreferences(LoginActivity.this, Constants.LOGIN_EMAIL, user.getEmail());

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
        String formattedDate = df.format(c.getTime());

        SharedPreferencesAdapter.setValueStringSharedPreferences(LoginActivity.this, Constants.DATE_LOGIN, formattedDate);

        Intent mainIntent = new Intent(LoginActivity.this, MenuActivity.class);
        AllActivities.isSync = true;
        startActivity(mainIntent);
        finish();
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

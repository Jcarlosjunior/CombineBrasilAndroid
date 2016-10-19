package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;

import java.util.HashMap;

import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;

public class LoginActivity extends Activity {

    EditText editPassword, editLogin;
    Button btnLogin;
    LinearLayout linearProgress;

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
    }

    public View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            editLogin.setText("Combine");
            editPassword.setText("combine123");
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
                Connection task = new Connection(url, Request.Method.POST, "calledLogin", false, this, loginData());
                task.callByJsonStringRequest();
            }
            else
                Services.message("Aviso", "Sem conexão com a internet", this);
        }
    }

    public static void afterLogin(String response, boolean isList, Activity activity) {
        ((LoginActivity) activity).validaLogin(response, isList);
    }

    public void validaLogin(String response, boolean isList) {
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
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        LoginActivity.this.finish();*/
    }

    private HashMap<String, String> loginData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Username", editLogin.getText().toString());
        params.put("Password", editPassword.getText().toString());
        return params;
    }

    public boolean validateEmail(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=3){
            ver = true;
        }
        else
            changeColorEdit(edit, "Insira um usuário com válido");
        return ver;
    }

    public boolean validatePassword(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=3){
            ver = true;
        }
        else
            changeColorEdit(edit, "Insira uma senha válida");
       return ver;
    }

    public void changeColorEdit(EditText edit, String mensagem){
        edit.setBackground(getResources().getDrawable(R.drawable.background_edit_error));
        Services.messageAlert(this, "DAdos Inválidos", mensagem);
        //Services.message("Dados Inválidos", mensagem, this);
    }

    private String getString(EditText edit){
        return edit.getText().toString().trim().equals("") ? "" : edit.getText().toString();
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

}

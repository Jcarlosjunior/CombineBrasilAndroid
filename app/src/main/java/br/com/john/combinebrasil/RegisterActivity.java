package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAthleteAsyncTask;
import br.com.john.combinebrasil.Connection.Posts.PostUsersAsyncTask;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;

public class RegisterActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editUser, editEmail, editPassword, editConfirmPassword;
    Button btnRegister, btnRegisterFacebook;
    ConstraintLayout linearProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText(R.string.register);

        editUser = (EditText) findViewById(R.id.edit_user);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        linearProgress = (ConstraintLayout) findViewById(R.id.linear_progress_login);
        btnRegisterFacebook = (Button) findViewById(R.id.btn_register_facebook);
        btnRegister.setOnClickListener(clickRegister);
        editConfirmPassword.addTextChangedListener(textConfirm);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            boolean loginFacebook = extras.getBoolean("loginFacebook");
            if(loginFacebook)
                createUserByFacebook(extras);
        }
    }

    private void createUserByFacebook(Bundle extras){
        editPassword.setText(extras.getString("pswd"));
        editConfirmPassword.setText(extras.getString("pswd"));
        editEmail.setText(extras.getString("email"));
        editUser.setText(extras.getString("name"));

        editPassword.setEnabled(false);
        editConfirmPassword.setEnabled(false);
        btnRegisterFacebook.setVisibility(View.GONE);
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener clickRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(verifyForm())
                if(verifyPassword()){
                callRegister();
            }
        }
    };

    private void callRegister(){
        if(Services.isOnline(this)){
            linearProgress.setVisibility(View.VISIBLE);
            registerUser();
        }
    }

    private void registerUser(){
        String url = Constants.URL + Constants.API_USERS;
        PostUsersAsyncTask post = new PostUsersAsyncTask();
        post.setActivity(RegisterActivity.this);
        post.setObjPut(createObject());
        post.execute(url);
    }

    public static void afterRegisterUser(Activity act, String response, String result){
        ((RegisterActivity)act).afterRegisterUser(response, result);
    }

    private void afterRegisterUser(String response, String result){
        linearProgress.setVisibility(View.GONE);
        if(response.equals("OK")){
            Services.messageAlert(RegisterActivity.this, "Mensagem", "Usuário foi criado com sucesso.", "REGISTER_USER");
        }
        else
            verifyErrorRegister(result);
    }

    private void verifyErrorRegister(String result){
        JSONObject json;
        try{
            json = new JSONObject(result);
            String detail = json.getString("detail");
            json = new JSONObject(detail);
            if(json.getInt("code") ==  11000)
                saveUser(json.getString("op"));
            else {
                Services.messageAlert(RegisterActivity.this, "Mensagem", "Usuário não cadastrado\n" + result, "");
            }
        }catch(JSONException e){
            e.printStackTrace();
            try {
                json = new JSONObject(result);
                if(json.getString("message").toString().toLowerCase().equals("email must be unique"))
                    Services.messageAlert(RegisterActivity.this, "Mensagem", "E-mail já está cadastrado.", "");
                else
                    Services.messageAlert(RegisterActivity.this, "Mensagem", "Usuário não cadastrado\n" + json.getString("message"), "");
            } catch (JSONException e1) {
                Services.messageAlert(RegisterActivity.this, "Mensagem", "Usuário não cadastrado\n" + result, "");
                e1.printStackTrace();
            }
        }
    }
    private void saveUser(String response){
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        User user = des.getObjectsUser();

        SharedPreferencesAdapter.setLoggedSharedPreferences(RegisterActivity.this, true);
        SharedPreferencesAdapter.setValueStringSharedPreferences(RegisterActivity.this, Constants.LOGIN_EMAIL, user.getEmail());

        getTimeLogin();

        callMainActivity();
    }

    private void getTimeLogin(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
        String formattedDate = df.format(c.getTime());

        SharedPreferencesAdapter.setValueStringSharedPreferences(RegisterActivity.this, Constants.DATE_LOGIN, formattedDate);

    }

    private void callMainActivity(){
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        AllActivities.isSync = true;
        startActivity(mainIntent);
        finish();
    }

    private JSONObject createObject()
    {
        JSONObject object = new JSONObject();
        try {
            try {
                object.put(Constants.USER_EMAIL, editEmail.getText().toString().toLowerCase());
                object.put(Constants.USER_NAME, editUser.getText().toString());
                object.put(Constants.USER_PASSWORD, editPassword.getText().toString());
                object.put(Constants.USER_ISADMIN, true);
                object.put(Constants.USER_CANWRITE, true);
            }
            catch(JSONException e){}
        }catch (Exception e){}
        return object;
    }
    private boolean verifyForm()
    {
        boolean ver = true;
        if (!isValidEmail(editEmail))
            ver = false;
        if (!validaEdit(editUser))
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

    public final boolean isValidEmail(EditText edit) {
        CharSequence target = edit.getText();
        boolean ret = false;
        if (TextUtils.isEmpty(target)) {
            Services.changeColorEditBorderError(edit, this);
            ret =  false;
        } else {
            ret =  android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            if(!ret)
                Services.changeColorEditBorderError(edit, this);
            else
                Services.changeColorEditBorder(edit, this);
        }

        return ret;
    }

    private String getString(EditText edit){
        return edit.getText().toString().trim().equals("") ? "" : edit.getText().toString();
    }

    private TextWatcher textConfirm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>3) {
                if (!editPassword.getText().toString().isEmpty()) {
                    verifyPassword();
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private boolean verifyPassword(){
        boolean verify = editPassword.getText().toString().equals(editConfirmPassword.getText().toString()) ? true : false;
        if(!verify)
            Services.changeColorEditBorderError(editConfirmPassword, this);
        else
            Services.changeColorEditBorder(editConfirmPassword,this);
        return verify;
    }

    public static void returnMessage(Activity act, String whoCalled){
        if(whoCalled.equals("REGISTER_USER")){
            ((RegisterActivity) act).goToHome();
        }
    }

    private void goToHome()
    {
        Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
        startActivity(intent);
        this.finish();
    }
}

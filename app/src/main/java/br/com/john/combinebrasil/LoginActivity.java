package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostLogin;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;


public class LoginActivity extends Activity {

    EditText editPassword, editLogin;
    Button btnLogin, btnFacebook;
    LoginButton loginButton;
    TextView textRegister;
    CallbackManager callbackManager;
    public static android.support.constraint.ConstraintLayout linearProgress;
    String id, name, email, gender, birthday;
    boolean isLoginFb = false;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editLogin = (EditText) findViewById(R.id.edit_login);
        btnLogin = (Button) findViewById(R.id.btn_entrar);
        btnFacebook = (Button) findViewById(R.id.btn_enter_facebook);
        loginButton = (LoginButton) findViewById(R.id.login_facebook);
        linearProgress = (android.support.constraint.ConstraintLayout) findViewById(R.id.linear_progress_login);
        textRegister = (TextView) findViewById(R.id.txt_register);

        if(Constants.debug)
        btnLogin.setOnLongClickListener(onLongClickListener);
        btnLogin.setOnClickListener(onClickLoginListener);
        btnFacebook.setOnClickListener(clickLoginFacebook);
        textRegister.setOnClickListener(onClickRegister);

        DatabaseHelper db = new DatabaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
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
            editLogin.setText("jonathansps22@hotmail.com");
            editPassword.setText("1582318081780553");
            return true;
        }
    };

    public View.OnClickListener onClickLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isLoginFb = false;
            if (verifyLogin())
               callLogin(editLogin.getText().toString(), editPassword.getText().toString());
        }
    };

    private boolean verifyLogin(){
        boolean ver = false;
        if(validateEmail(editLogin)) {
            if (validatePassword(editPassword)) {
                ver = true;
            }
        }
        return ver;
    }

    private void callLogin(String user, String pswd) {
            if (Services.isOnline(this)) {
                linearProgress.setVisibility(View.VISIBLE);
                String url = Constants.URL + Constants.login;
                PostLogin post = new PostLogin();
                post.setActivity(LoginActivity.this);
                post.setObjPut(loginData(user, pswd));
                post.execute(url);
            }
            else
                Services.messageAlert(this,"Aviso", "Sem conexão com a internet","hide");
        }


    private JSONObject loginData(String email, String pswd) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("password", pswd);
            Log.i("password", pswd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void afterLogin(String response, Activity activity, int statusCode) {
            if(statusCode == 200)
                ((LoginActivity) activity).validaLogin(response);
            else
                ((LoginActivity) activity).errorLogin(response, statusCode);
        }

    public void validaLogin(String response) {
        linearProgress.setVisibility(View.GONE);
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        user = des.getLogin();

        callGetDataUser(user);
    }

    private void callGetDataUser(User user){
        SharedPreferencesAdapter.setLoggedSharedPreferences(LoginActivity.this, true);
        SharedPreferencesAdapter.setValueStringSharedPreferences(LoginActivity.this, Constants.LOGIN_EMAIL, user.getEmail());
        SharedPreferencesAdapter.setValueStringSharedPreferences(LoginActivity.this, Constants.USER_TOKEN, user.getToken());

        linearProgress.setVisibility(View.VISIBLE);
        String url = Constants.URL+Constants.API_USERS+"?"+Constants.USER_EMAIL+"="+user.getEmail();
        Connection connection = new Connection(url, 0, Constants.CALLED_GET_USER, false, LoginActivity.this);
        connection.callByJsonStringRequest();
    }

    public static void returnGetDataUser(Activity act, String response, int status){
        ((LoginActivity)act).returnGetDataUser(response, status);
    }

    private void returnGetDataUser(String response, int status){
        linearProgress.setVisibility(View.GONE);
        if(status==200||status==201){
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            User user = des.getUsers();

            this.user.setName(user.getName());
            this.user.setId(user.getId());

            saveUser(this.user);

            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            AllActivities.isSync = true;
            startActivity(intent);
            finish();
        }
    }

    private void saveUser(User user){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
        String formattedDate = df.format(c.getTime());

        SharedPreferencesAdapter.setValueStringSharedPreferences(LoginActivity.this, Constants.DATE_LOGIN, formattedDate);

        DatabaseHelper db = new DatabaseHelper(this);
        db.openDataBase();
        db.addUser(user);
    }

    private void errorLogin(String response, int statusCode){
        linearProgress.setVisibility(View.GONE);
        String message = "";
        if(statusCode == 400) {
            JSONObject json = null;
            try {
                json = new JSONObject(response);
                message = json.optString("message", "Usuário ou senha inválida");
                if(!isLoginFb)
                    Services.messageAlert(LoginActivity.this, "Aviso", message, "");
                else
                    Services.messageAlert(LoginActivity.this, "Aviso", "Seu usuário não existe, crie seu usuário para continuar.", "LOGIN_FACBOOK");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void callRegisterWithFacebook(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("loginFacebook", true);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        intent.putExtra("pswd", id);
        intent.putExtra("birthday", birthday);
        startActivity(intent);
    }//1582318081780553


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

    private View.OnClickListener clickLoginFacebook = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isLoginFb = true;
            loginFacebook();
        }
    };

    private void loginFacebook() {
        List<String> permissionNeeds = Arrays.asList("email",
                "user_birthday", "public_profile", "user_birthday", "user_photos");
        loginButton.setReadPermissions(permissionNeeds);
        try {
            loginButton.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.i("LoginFacebook", "onSuccess");

                            String accessToken = loginResult.getAccessToken().getToken();
                            Log.i("LoginFacebook", accessToken);

                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.i("LoginFacebook", response.toString());
                                            successLoginFacebook(object);
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender, birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            Log.v("LoginFacebook", "onCancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.v("LoginFacebook", "Exception "+exception.getCause().toString());
                        }
                    });
            loginButton.performClick();

        } catch (FacebookException e) {
            Log.i("LoginFacebook", "Exception "+e.getMessage().toString());
        }
    }

    private void successLoginFacebook(JSONObject object){
        try {
            id = object.getString("id");
            try {
                URL profile_pic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                SharedPreferencesAdapter.setValueStringSharedPreferences(this, "profile_pic", profile_pic.toString());
                Log.i("profile_pic", profile_pic + "");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            name = object.getString("name");
            email = object.getString("email");
            gender = object.getString("gender");
            birthday = object.getString("birthday");
            callLogin(email, id);
            Log.i("LoginFacebook: ", "name: "+name+", email: "+email+", gender: "+gender+", birthday: "+birthday);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void returnMessage(Activity act, String whoCalled){
        if(whoCalled.equals("LOGIN_FACBOOK")){
            ((LoginActivity) act).callRegisterWithFacebook();
        }
    }
}

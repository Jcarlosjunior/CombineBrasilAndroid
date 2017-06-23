package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.NavigationDrawer;
import br.com.john.combinebrasil.Services.Services;

public class MenuActivity extends AppCompatActivity {
    private ImageView linearCreateSelective;
    private ImageView linearEnterSelective;
    private ImageView linearHistoricSelective;
    Toolbar toolbar;
    NavigationDrawer navigationDrawer;
    EditText editCode;
    Button btnConfirmCode;
    ConstraintLayout constraintDialogEnterSelective, constraintNotConnection, constraintProgress;
    private static Selective selective;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setVisibility(View.GONE);
        LinearLayout btnMenu = (LinearLayout) findViewById(R.id.linear_menu_button);
        btnMenu.setVisibility(View.GONE);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText(R.string.register);

        linearCreateSelective = (ImageView) findViewById(R.id.linear_create_selective);
        linearCreateSelective.setOnClickListener(clickCreateSelective);

        linearEnterSelective = (ImageView) findViewById(R.id.linear_enter_selective);
        linearEnterSelective.setOnClickListener(clickEnterSelective);

        linearHistoricSelective = (ImageView) findViewById(R.id.linear_historic_selective);
        linearHistoricSelective.setOnClickListener(clickHistoricSelective);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigationDrawer = new NavigationDrawer(savedInstanceState, this, toolbar, true);
        navigationDrawer.createNavigationAccess();

        editCode = (EditText) findViewById(R.id.edit_code);
        btnConfirmCode = (Button) findViewById(R.id.btn_confirm_code);
        btnConfirmCode.setOnClickListener(clickConfirmEnterSelective);
        btnConfirmCode.setOnLongClickListener(clickLongConfirmSelective);
        constraintDialogEnterSelective =(ConstraintLayout) findViewById(R.id.constraint_dialog_code);
        constraintNotConnection = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        constraintProgress = (ConstraintLayout) findViewById(R.id.constraint_progress);
        constraintDialogEnterSelective.setOnClickListener(clickHideEnterSelective);
        enabledOrDisabledBtn(btnConfirmCode, false);
        editCode.addTextChangedListener(textWatcher);
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>=4)
                enabledOrDisabledBtn(btnConfirmCode, true);
            else
                enabledOrDisabledBtn(btnConfirmCode, false);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void enabledOrDisabledBtn(Button btn, boolean isEnabled){
        if(isEnabled){
            btn.setEnabled(true);
            btn.setAlpha(1);
        }
        else{
            btn.setEnabled(false);
            btn.setAlpha(0.5f);
        }
    }

    private View.OnClickListener clickCreateSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, ChooseTeamSelectiveActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener clickEnterSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            constraintDialogEnterSelective.setVisibility(View.VISIBLE);
            showSoft(editCode);
        }
    };

    private View.OnClickListener clickHistoricSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, HistoricSelectiveActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener clickHideEnterSelective = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            hideSoft();
            constraintDialogEnterSelective.setVisibility(View.GONE);
        }
    };
    private View.OnClickListener clickConfirmEnterSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callEnterSelective();
        }
    };
    private View.OnLongClickListener clickLongConfirmSelective = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(Constants.debug)
                editCode.setText("SELCOMBINE");
            return true;
        }
    };

    private void callEnterSelective(){
        if(Services.isOnline(this)){
            hideNotConnect();
            hideSoft();
            showProgress(getString(R.string.verify_selective));
            String url = Constants.URL + Constants.API_SELECTIVES+"?"+Constants.SELECTIVES_CODESELECTIVE+"="+editCode.getText().toString().toUpperCase();
            Connection task = new Connection(url, 0, Constants.CALLED_GET_SELECTIVE, false, MenuActivity.this);
            task.callByJsonStringRequest();
        }
        else
            showToNoConnect();
    }

    public static void returnGetSelectiveByCode(Activity act, String response, int status){
        ((MenuActivity)act).returnGetSelectiveByCode(response, status);
    }

    private void returnGetSelectiveByCode(String response, int status){
        hideProgress();
        if(status == 200 || status == 201) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            Selective selective = des.getSelective();
                try{
                    if (selective!=null) {
                        this.selective = selective;
                        DatabaseHelper db = new DatabaseHelper(MenuActivity.this);
                        db.deleteTable(Constants.TABLE_SELECTIVES);
                        db.addSelective(selective);
                        Services.messageAlert(this, "Mensagem","Parabéns, você acaba de entrar na "+selective.getTitle(),"CODE_OK");
                }
            }catch (Exception e){
                Log.i("Exception: ", e.getMessage());}
        }else
            Services.messageAlert(MenuActivity.this, "Aviso", "O código inserido não existe.", "hide");
    }

    private void openSelective(Selective selective){
        AllActivities.isSync = true;
        Intent i = new Intent(MenuActivity.this, MainActivity.class);
        i.putExtra("id_selective",selective.getId());
        startActivity(i);
        this.finish();
    }

    private void hideSoft(){
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showSoft(EditText edit){
        edit.setFocusable(true);
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void showToNoConnect(){
        TextView textMessage = (TextView) findViewById(R.id.txt_message_not_connect);
        textMessage.setText(Html.fromHtml(getString(R.string.no_connection)));
        ConstraintLayout constraintNoConnect = (ConstraintLayout) findViewById(R.id.constraint_not_connection);
        constraintNoConnect.setVisibility(View.VISIBLE);
        Button btnTryAgain =(Button) findViewById(R.id.btn_try_again_connect);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEnterSelective();
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

    public static void returnMessageAlert(Activity act, String whoCalled){
        if(whoCalled.equals("CODE_OK"))
            ((MenuActivity)act).openSelective(selective);

    }

    @Override
    public void onBackPressed(){
        if(constraintDialogEnterSelective.getVisibility()==View.VISIBLE)
            constraintDialogEnterSelective.setVisibility(View.GONE);
        else this.finish();
    }
}

package br.com.john.combinebrasil;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.john.combinebrasil.Services.NavigationDrawer;

public class MenuActivity extends AppCompatActivity {
    private ImageView linearCreateSelective;
    private ImageView linearEnterSelective;
    private ImageView linearHistoricSelective;
    Toolbar toolbar;
    NavigationDrawer navigationDrawer;
    EditText editCode;
    Button btnConfirmCode;
    ConstraintLayout constraintDialogEnterSelective;
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
        constraintDialogEnterSelective =(ConstraintLayout) findViewById(R.id.constraint_dialog_code);
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
            constraintDialogEnterSelective.setVisibility(View.GONE);
        }
    };
}

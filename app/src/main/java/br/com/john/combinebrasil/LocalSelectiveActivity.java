package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashMap;

import br.com.john.combinebrasil.Classes.CEP;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.Services;

public class LocalSelectiveActivity extends AppCompatActivity {
    EditText editCep, editComplement, editCity, editNeighborhood, editState,editStreet;
    TextView textProgress;
    Toolbar toolbar;
    Button btnCreateSelective;
    private static Activity act;
    String idTeamSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_selective);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        editState = (EditText)  findViewById(R.id.edit_state);
        editCep = (EditText) findViewById(R.id.edit_cep);
        editComplement = (EditText) findViewById(R.id.edit_complement);
        editCity = (EditText) findViewById(R.id.edit_city);
        editNeighborhood = (EditText) findViewById(R.id.edit_neighborhood);
        editStreet = (EditText) findViewById(R.id.edit_street);
        btnCreateSelective = (Button) findViewById(R.id.btn_create_selective);

        btnCreateSelective.setOnClickListener(clickCreateSelective);
        btnCreateSelective.setOnLongClickListener(clickLongCreateSelective);
        Mask maskCpf = new Mask("#####-###", editCep);
        editCep.addTextChangedListener(maskCpf);

        editCep.addTextChangedListener(textListenerCep);

        act = LocalSelectiveActivity.this;

        final View activityRootView = findViewById(R.id.activity_local_selective);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(act, 200)) { // if more than 200 dp, it's probably a keyboard...
                   btnCreateSelective.setVisibility(View.GONE);
                }
                else
                    btnCreateSelective.setVisibility(View.VISIBLE);
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private View.OnLongClickListener clickLongCreateSelective = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(Constants.debug) {
                editCep.setText("12246-260");
                editComplement.setText("Em frente ao forum");
            }
            return true;
        }
    };

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LocalSelectiveActivity.this.finish();
        }
    };

    private boolean verifyFields(){
        boolean ver = true;
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
        Connection task = new Connection(url, 0, Constants.CALLED_GET_CEP, false, LocalSelectiveActivity.this);
        task.callByJsonStringRequest();
    }

    private void showLoadingAddress(){
        editCity.setText("Localizando cidade...");
        editState.setText("Localizando...");
        editStreet.setText("Localizando rua...");
        editNeighborhood.setText("localizando bairro...");
    }

    public static void returnCEP(Activity act, String response, int statusCode){
        ((LocalSelectiveActivity)act).returnCEP(response, statusCode);
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
        editCity.setText("");
        editState.setText("");
        editStreet.setText("");
        editNeighborhood.setText("");
    }

    public static void finishActity (){
        ((LocalSelectiveActivity)act).finish();
    }

    private View.OnClickListener clickCreateSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(verifyFields()) {
                callChooseTestsSelective();
            }
        }
    };

    private void callChooseTestsSelective(){
        Intent i;
        i = new Intent(LocalSelectiveActivity.this, CreateSelectiveActivity.class);
        AllActivities.hashInfoSelective.put("cep",editCep.getText().toString());
        AllActivities.hashInfoSelective.put("street",editStreet.getText().toString());
        AllActivities.hashInfoSelective.put("neighborhood",editNeighborhood.getText().toString());
        AllActivities.hashInfoSelective.put("state",editState.getText().toString());
        AllActivities.hashInfoSelective.put("city",editCity.getText().toString());
        AllActivities.hashInfoSelective.put("complement",editComplement.getText().toString());
        startActivity(i);
    }
}

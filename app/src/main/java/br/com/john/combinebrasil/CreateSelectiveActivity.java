package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
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

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.CEP;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.CreateJSON;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostCreateSelective;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.Services;

public class CreateSelectiveActivity extends AppCompatActivity{
    EditText editTitle, editCep, editComplement, editCity, editNeighborhood, editState,editStreet, editNotes;
    TextView textDate, textSecondDate, textThirdDate;
    LinearLayout linearProgress;
    Button btnCreateSelective, btnCancel, btnConfirm;
    ImageView imgAddDate, imgAddSecondDate, imgAddThirdDate;
    TextView textProgress;
    Toolbar toolbar;
    String idTeamSelected;
    public static Activity act;
    public static HashMap<String, String> hashInfoSelective;
    MaterialCalendarView calendarDates;
    ConstraintLayout constraintCalendar;
    private int dateClicked = 0;
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
        textProgress = (TextView) findViewById(R.id.text_progress);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);

        textDate = (TextView) findViewById(R.id.txt_date);
        textSecondDate = (TextView) findViewById(R.id.txt_date_second);
        textThirdDate = (TextView) findViewById(R.id.txt_date_third);
        imgAddDate = (ImageView) findViewById(R.id.img_add_date);
        imgAddSecondDate = (ImageView) findViewById(R.id.img_add_second_date);
        imgAddThirdDate = (ImageView) findViewById(R.id.img_add_third_date);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editState = (EditText)  findViewById(R.id.edit_state);
        editCep = (EditText) findViewById(R.id.edit_cep);
        editComplement = (EditText) findViewById(R.id.edit_complement);
        editCity = (EditText) findViewById(R.id.edit_city);
        editNeighborhood = (EditText) findViewById(R.id.edit_neighborhood);
        editStreet = (EditText) findViewById(R.id.edit_street);
        editNotes = (EditText) findViewById(R.id.edit_notes);
        btnCreateSelective = (Button) findViewById(R.id.btn_create_selective);
        constraintCalendar = (ConstraintLayout) findViewById(R.id.constraint_calendar);
        btnCancel = (Button) findViewById(R.id.btn_cancel_date);
        btnConfirm = (Button) findViewById(R.id.btn_confirm_date);

        calendarDates = (MaterialCalendarView) findViewById(R.id.calendar_dates);
        textDate.setOnClickListener(clickedDateListener);
        textSecondDate.setOnClickListener(clickedSecondDateListener);
        textThirdDate.setOnClickListener(clickedThirdDateListener);
        imgAddDate.setOnClickListener(clickedAddDate);
        imgAddSecondDate.setOnClickListener(clickedAddSecondDate);
        imgAddThirdDate.setOnClickListener(clickedAddThirdDate);
        constraintCalendar.setOnClickListener(clickedCancelDate);
        btnCancel.setOnClickListener(clickedCancelDate);
        btnConfirm.setOnClickListener(clickedConfirmDate);
        btnCreateSelective.setOnClickListener(clickCreateSelective);
        btnCreateSelective.setOnLongClickListener(clickLongCreateSelective);

        Mask maskCpf = new Mask("#####-###", editCep);
        editCep.addTextChangedListener(maskCpf);

        editCep.addTextChangedListener(textListenerCep);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idTeamSelected = extras.getString("team_choose");
        }
    }

    private View.OnLongClickListener clickLongCreateSelective = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(Constants.debug) {
                editTitle.setText("Seletiva Teste");
                editCep.setText("12246-260");
                editComplement.setText("Em frente ao forum");
                editNotes.setText("ir de chuteira, camisa preta.");
                textDate.setText("30/05/2017");
            }
            return true;
        }
    };

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateSelectiveActivity.this.finish();
        }
    };
    private View.OnClickListener clickCreateSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //createSelective();
            if(verifyFields()) {
                callChooseTestsSelective();
            }
        }
    };

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

    private void callChooseTestsSelective(){
        Intent i;
        i = new Intent(CreateSelectiveActivity.this, TestSelectiveActivity.class);
        hashInfoSelective = new HashMap<String, String>();
        hashInfoSelective.put("team",idTeamSelected);
        hashInfoSelective.put("date", textDate.getText().toString());
        hashInfoSelective.put("title",editTitle.getText().toString());
        hashInfoSelective.put("cep",editCep.getText().toString());
        hashInfoSelective.put("street",editStreet.getText().toString());
        hashInfoSelective.put("neighborhood",editNeighborhood.getText().toString());
        hashInfoSelective.put("state",editState.getText().toString());
        hashInfoSelective.put("city",editCity.getText().toString());
        hashInfoSelective.put("complement",editComplement.getText().toString());
        hashInfoSelective.put("notes",editNotes.getText().toString());
        startActivity(i);

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
        //Snackbar snackbar = Snackbar
        //        .make(coordinatorLayout, "Endereço não encontrado", Snackbar.LENGTH_SHORT);
        //snackbar.show();

        editCity.setText("");
        editState.setText("");
        editStreet.setText("");
        editNeighborhood.setText("");
    }

    public static void finishActity (){
        ((CreateSelectiveActivity)act).finish();
    }

    private View.OnClickListener clickedDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDate(1);
        }
    };

    private View.OnClickListener clickedSecondDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDate(2);
        }
    };

    private View.OnClickListener clickedThirdDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDate(3);
        }
    };

    private void showDate(int date){
        dateClicked = date;
        constraintCalendar.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener clickedConfirmDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            clickConfirmDate();
        }
    };
    private void clickConfirmDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        CalendarDay calendarSelected = calendarDates.getSelectedDate();
        constraintCalendar.setVisibility(View.GONE);
        if(dateClicked==1) {
            Date date = calendarSelected.getDate();
            textDate.setText(convertDateCalendar(formatter.format(date)));
        }
        else if(dateClicked==2) {
            Date date = calendarSelected.getDate();
            textSecondDate.setText(convertDateCalendar(formatter.format(date)));
        }
        else {
            Date date = calendarSelected.getDate();
            textThirdDate.setText(convertDateCalendar(formatter.format(date)));
        }
    }

    private View.OnClickListener clickedCancelDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            hideCalendar();
        }
    };

    private void hideCalendar(){
        constraintCalendar.setVisibility(View.GONE);
    }

    private View.OnClickListener clickedAddDate = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            clickAddDate();
        }
    };
    private void clickAddDate(){
        showDate(textSecondDate, imgAddSecondDate);
        imgAddDate.setVisibility(View.INVISIBLE);
        imgAddSecondDate.setImageDrawable(getDrawable(R.drawable.ic_less));
    }

    private View.OnClickListener clickedAddSecondDate = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            clickAddSecondDate();
        }
    };

    private void clickAddSecondDate(){
        if(!textSecondDate.getText().toString().isEmpty()){
            hideDate(textSecondDate, imgAddSecondDate);
            imgAddDate.setVisibility(View.VISIBLE);
            imgAddSecondDate.setImageDrawable(getDrawable(R.drawable.ic_add));
        }
        else{
            showDate(textThirdDate, imgAddThirdDate);
            imgAddSecondDate.setVisibility(View.INVISIBLE);
            imgAddThirdDate.setImageDrawable(getDrawable(R.drawable.ic_less));
        }
    }

    private View.OnClickListener clickedAddThirdDate = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            clickAddSThirdDate();
        }
    };

    private void clickAddSThirdDate(){
            hideDate(textThirdDate, imgAddThirdDate);
            imgAddSecondDate.setVisibility(View.VISIBLE);
            imgAddSecondDate.setImageDrawable(getDrawable(R.drawable.ic_add));
    }

    private String convertDateCalendar(String date){
        String month =  date.substring(0,2);
        String day = date.substring(3,5);
        String year = date.substring(6,date.length());
        return day + "/"+month+"/"+year;
    }

    private void showDate(TextView text, ImageView img){
        text.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);
    }
    private void hideDate(TextView text, ImageView img){
        text.setVisibility(View.GONE);
        img.setVisibility(View.GONE);
    }


}

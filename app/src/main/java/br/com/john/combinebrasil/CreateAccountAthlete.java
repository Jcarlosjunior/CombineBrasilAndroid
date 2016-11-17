package br.com.john.combinebrasil;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.Services;

public class CreateAccountAthlete extends AppCompatActivity {
    MaterialBetterSpinner spinnerDay, spinnerMonth, spinnerYear;
    Toolbar toolbar;
    EditText editTextName, editTextCPF, editTextHeight, editTextWeihgt;
    private Button buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_athlete);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        editTextName = (EditText) findViewById(R.id.edit_name_add);
        editTextCPF = (EditText) findViewById(R.id.edit_cpf_add);
        editTextHeight = (EditText) findViewById(R.id.edit_height_add);
        editTextWeihgt = (EditText) findViewById(R.id.edit_weight_add);
        buttonAdd = (Button) findViewById(R.id.button_add_athlete);

        spinnerDay  = (MaterialBetterSpinner) findViewById(R.id.spinner_day_birthday_add);
        spinnerMonth = (MaterialBetterSpinner) findViewById(R.id.spinner_month_birthday_add);
        spinnerYear = (MaterialBetterSpinner) findViewById(R.id.spinner_year_birthday_add);

        inflateSpinnerDay();

        Mask maskCpf = new Mask("###.###.###-##", editTextCPF);
        editTextCPF.addTextChangedListener(maskCpf);

        buttonAdd.setOnClickListener(addAthleteClicked);
    }

    private View.OnClickListener addAthleteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callAddAthlete();
        }
    };
    private void callAddAthlete(){
        if(Services.isOnline(this)){
            LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
            linearProgress.setVisibility(View.VISIBLE);
            String url = Constants.URL + Constants.API_ATHLETES;
            Connection task = new Connection(url, Request.Method.POST, Constants.CALLED_POST_ATHLETES, false, CreateAccountAthlete.this, postData());
            task.callByJsonStringRequest();
        }
    }

    public static void returnPostAthlete(Activity act, String response){
        ((CreateAccountAthlete) act).afterPost(response);
    }
    private void afterPost(String response){
        LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
        linearProgress.setVisibility(View.GONE);
        int codeResponse;
        try {
            JSONObject json = new JSONObject(response);
            codeResponse = Integer.parseInt(json.getString("statusCode"));
            if(codeResponse==400)
                Services.messageAlert(CreateAccountAthlete.this, "Menasgem", "Atleta não cadastrado\n"+json.getString("error"), "" );
            else if(codeResponse==401)
                Services.messageAlert(CreateAccountAthlete.this, "Menasgem", "Atleta não cadastrado\n"+json.getString("error"), "" );
            else if(codeResponse==200)
                Services.messageAlert(CreateAccountAthlete.this, "Salvo", "Atleta cadastrado com sucesso", "POSTATHLETE" );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void finished(Activity act){
        ((CreateAccountAthlete)act).finish();
    }

    private HashMap<String, String> postData() {
        HashMap<String, String> params = new HashMap<>();
        double height = Double.parseDouble(editTextHeight.getText().toString().replaceAll(",","."));
        double weight = Double.parseDouble(editTextWeihgt.getText().toString().replaceAll(",","."));
        String birthday = spinnerYear.getText().toString()+"-"+chooseMonth(spinnerMonth.getText().toString())+"-"+spinnerDay.getText().toString();
        params.put(Constants.ATHLETES_NAME, editTextName.getText().toString());
        params.put(Constants.ATHLETES_CPF, editTextCPF.getText().toString());
        params.put(Constants.ATHLETES_HEIGHT, String.valueOf(height));
        params.put(Constants.ATHLETES_WEIGHT, String.valueOf(weight));
        params.put(Constants.ATHLETES_BIRTHDAY, birthday);
        return params;
    }

    private String chooseMonth(String month){
        if(month.equalsIgnoreCase("janeiro"))
            return "01";
        else if(month.equalsIgnoreCase("fevereiro"))
            return "02";
        else if(month.equalsIgnoreCase("Março"))
            return "03";
        else if(month.equalsIgnoreCase("abril"))
            return "04";
        else if(month.equalsIgnoreCase("maio"))
            return "05";
        else if(month.equalsIgnoreCase("junho"))
            return "06";
        else if(month.equalsIgnoreCase("julho"))
            return "07";
        else if(month.equalsIgnoreCase("agosto"))
            return "08";
        else if(month.equalsIgnoreCase("setembro"))
            return "09";
        else if(month.equalsIgnoreCase("outubro"))
            return "10";
        else if(month.equalsIgnoreCase("novembro"))
            return "11";
        else if(month.equalsIgnoreCase("dezembro"))
            return "12";
        else
            return "";
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateAccountAthlete.this.finish();
        }
    };

    private void inflateSpinnerDay(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        String[] spinnerDayValues = new String[31];
        String[] spinnerMonthValues  = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro","Novembro","Dezembro"};
        String[] spinnerYearsValues = new String[200];
        for(int i=0; i<=30;i++){
            spinnerDayValues[i]=String.valueOf(i+1);
        }
        for(int x=199; x>=0; x--){
            spinnerYearsValues[x]=String.valueOf(year-x);
        }

        ArrayAdapter<String> arrayAdapterDay = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerDayValues);
        ArrayAdapter<String> arrayAdapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerMonthValues);
        ArrayAdapter<String> arrayAdapterYear = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerYearsValues);

        spinnerDay.setAdapter(arrayAdapterDay);
        spinnerMonth.setAdapter(arrayAdapterMonth   );
        spinnerYear.setAdapter(arrayAdapterYear);
    }
}

package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
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

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAthleteAsyncTask;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.Services;

public class CreateAccountAthlete extends AppCompatActivity {
    MaterialBetterSpinner spinnerDay, spinnerMonth, spinnerYear;
    Toolbar toolbar;
    EditText editTextName, editTextCPF, editTextPosition, editTextAddress, editTextHeight, editTextWeihgt;
    private Button buttonAdd;
    Athletes athlete;
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
        editTextAddress = (EditText) findViewById(R.id.edit_address_add);
        editTextPosition = (EditText) findViewById(R.id.edit_position_add);
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
       if(verifyForm()) {
           if(validaBirthday()) {
               if (Services.isOnline(this)) {
                   LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
                   linearProgress.setVisibility(View.VISIBLE);
                   String url = Constants.URL + Constants.API_ATHLETES;

                   PostAthleteAsyncTask post = new PostAthleteAsyncTask();
                   post.setActivity(CreateAccountAthlete.this);
                   post.setObjPut(createObject());
                   post.execute(url);
               }
           }
        }
    }

    private void createAthlete(String id){
        double height = Double.parseDouble(editTextHeight.getText().toString().replaceAll(",","."));
        double weight = Double.parseDouble(editTextWeihgt.getText().toString().replaceAll(",","."));
        String birthday = spinnerYear.getText().toString()+"-"+
                chooseMonth(spinnerMonth.getText().toString())+"-"+spinnerDay.getText().toString();

        athlete = new Athletes(
                id,
                editTextName.getText().toString(),
                birthday,
                editTextCPF.getText().toString(),
                editTextAddress.getText().toString(),
                editTextPosition.getText().toString(),
                height,
                weight,
                "",
                "",
                ""
        );

    }
    private JSONObject createObject() {
        JSONObject object = new JSONObject();
        try {
            double height = Double.parseDouble(editTextHeight.getText().toString().replaceAll(",","."));
            double weight = Double.parseDouble(editTextWeihgt.getText().toString().replaceAll(",","."));
            String birthday = spinnerYear.getText().toString()+"-"+chooseMonth(spinnerMonth.getText().toString())+"-"+spinnerDay.getText().toString();
            object.put(Constants.ATHLETES_NAME, editTextName.getText().toString());
            object.put(Constants.ATHLETES_CPF, editTextCPF.getText().toString());
            object.put(Constants.ATHLETES_ADDRESS, editTextAddress.getText().toString());
            object.put(Constants.ATHLETES_DESIRABLE_POSITION, editTextPosition.getText().toString());
            object.put(Constants.ATHLETES_HEIGHT, height);
            object.put(Constants.ATHLETES_WEIGHT, weight);
            object.put(Constants.ATHLETES_BIRTHDAY, birthday);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private boolean verifyForm(){
        boolean ver = true;
        if(!validaName(editTextName))
            ver = false;
        if (!validaCPF(editTextCPF))
            ver = false;
        if (!validaName(editTextAddress))
            ver = false;
        if(!validateText(editTextHeight, "Altura inválido."))
            ver = false;
        if(!validateText(editTextWeihgt,"Peso inválido"))
            ver = false;

        if(!ver)
            Services.messageAlert(this, "Alerta","Dados inválidos, por favor, verifique para continuar.","");
        return ver;
    }
    public boolean validaName(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=5) {
            Services.changeColorEditBorder(edit, this);
            ver = true;
        }
        else
            Services.changeColorEditBorderError(edit, this);
        return ver;
    }

    public boolean validaCPF(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=14) {
            Services.changeColorEditBorder(edit, this);
            ver = true;
        }
        else
           Services.changeColorEditBorderError(edit, this);
        return ver;
    }

    private boolean validaBirthday(){
        boolean ver = false;
        if(spinnerDay.getText().toString().equals(""))
            Services.messageAlert(CreateAccountAthlete.this, "Alerta","Dia de nascimento inválido","");
        else if(spinnerMonth.getText().toString().equals(""))
            Services.messageAlert(CreateAccountAthlete.this, "Alerta","Mês de nascimento inválido","");
        else if(spinnerYear.getText().toString().equals(""))
            Services.messageAlert(CreateAccountAthlete.this, "Alerta","Mês de nascimento inválido","");
        else
            ver=true;
        return ver;
    }
    private boolean validateText(EditText edit, String msg){
        boolean ver = false;
        if(getString(edit).length()>=2){
            int cont=0;
            for(int i=0; i<= edit.length()-1; i++)
            {
                String s = String.valueOf(edit.getText().toString().charAt(i));
                if(s.equals(",") || s.equals("."))
                    cont = cont+1;
            }
            if(cont<=1) {
                Services.changeColorEditBorder(edit, this);
                ver = true;
            }
            else
                Services.changeColorEditBorderError(edit, this);
        }
        else
            Services.changeColorEditBorderError(edit, this);
        return ver;
    }
    private String getString(EditText edit){
        return edit.getText().toString().trim().equals("") ? "" : edit.getText().toString();
    }

    public static void returnPostAthlete(Activity act, String response, int status){
        //((CreateAccountAthlete) act).afterPost(response);
    }
    public static void afterSendAthlete(Activity act, String ret, String response){
        ((CreateAccountAthlete)act).afterPost(ret, response);
    }

    private void afterPost(String ret, String response){
        LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
        linearProgress.setVisibility(View.GONE);
            if(ret.equals("FAIL"))
                Services.messageAlert(CreateAccountAthlete.this, "Menasgem", "Atleta não cadastrado\n"+response, "" );
            else if(ret.equals("OK"))
                saveAthlete(response);
    }

    private void saveAthlete(String response){
        DeserializerJsonElements des = new DeserializerJsonElements(response);
        Athletes athlete = des.getAthlete();
        DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
        long ret = db.addAthlete(athlete);
        if(ret!=0){
            Services.messageAlert(CreateAccountAthlete.this, "Salvo", "Atleta cadastrado com sucesso", "POSTATHLETE");
        }
    }

    private void clearForm(){
        editTextName.setText("");
        editTextCPF.setText("");
        editTextAddress.setText("");
        editTextWeihgt.setText("");
        editTextHeight.setText("");
        editTextPosition.setText("");
        spinnerMonth.setSelection(0);
        spinnerYear.setSelection(0);
        spinnerDay.setSelection(0);
    }

    public static void finished(Activity act){
        ((CreateAccountAthlete)act).finished();
    }
    public void finished(){
        clearForm();
    }

    private HashMap<String, String> postData() {
        Object obj = new Object();
        obj = "fasdfasdf";
        obj = 3;

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
            int num = i+1;
            if(num<10)
                spinnerDayValues[i] = String.valueOf("0"+num);
            else
                spinnerDayValues[i] = String.valueOf(num);
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

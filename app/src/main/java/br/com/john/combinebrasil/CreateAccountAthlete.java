package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAthleteAsyncTask;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.MaskHeight;
import br.com.john.combinebrasil.Services.Services;

public class CreateAccountAthlete extends AppCompatActivity {
    MaterialBetterSpinner spinnerDay, spinnerMonth, spinnerYear, spinnerPosition;
    Toolbar toolbar;
    EditText editTextName, editTextCPF, editTextPhone, editTextHeight, editTextWeihgt, editEmail, editAddress;
    private Button buttonAdd;
    Athletes athlete;
    ArrayList<Positions> positions;
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
        editTextPhone = (EditText) findViewById(R.id.edit_phone_add);
        editTextHeight = (EditText) findViewById(R.id.edit_height_add);
        editTextWeihgt = (EditText) findViewById(R.id.edit_weight_add);
        editEmail = (EditText) findViewById(R.id.edit_email_add);
        editAddress = (EditText) findViewById(R.id.edit_address_add);

        buttonAdd = (Button) findViewById(R.id.button_add_athlete);

        spinnerDay  = (MaterialBetterSpinner) findViewById(R.id.spinner_day_birthday_add);
        spinnerMonth = (MaterialBetterSpinner) findViewById(R.id.spinner_month_birthday_add);
        spinnerYear = (MaterialBetterSpinner) findViewById(R.id.spinner_year_birthday_add);
        spinnerPosition = (MaterialBetterSpinner) findViewById(R.id.spinner_positions_add);

        inflateSpinnerDay();
        inflateSpinnerPosition();

        Mask maskCpf = new Mask("###.###.###-##", editTextCPF);
        editTextCPF.addTextChangedListener(maskCpf);

        Mask maskPhone = new Mask("(##)#####-####", editTextPhone);
        editTextPhone.addTextChangedListener(maskPhone);

        TextWatcher mask = MaskHeight.insert("#,##", editTextHeight);
        editTextHeight.addTextChangedListener(mask);
        mask = MaskHeight.insert("##",editTextWeihgt);
        editTextWeihgt.addTextChangedListener(mask);

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
                   post.setPlay(true);
                   post.execute(url);
               }
           }
        }
    }

    private JSONObject createObject() {
        JSONObject object = new JSONObject();
        try {
            int position = getIndex(spinnerPosition);

            try {
                double height = Double.parseDouble(editTextHeight.getText().toString().replaceAll(",", "."));
                double weight = Double.parseDouble(editTextWeihgt.getText().toString().replaceAll(",", "."));
                String birthday = spinnerYear.getText().toString() + "-" + chooseMonth(spinnerMonth.getText().toString()) + "-" + spinnerDay.getText().toString();
                object.put(Constants.ATHLETES_NAME, editTextName.getText().toString());
                object.put(Constants.ATHLETES_CPF, editTextCPF.getText().toString());
                object.put(Constants.ATHLETES_PHONE, editTextPhone.getText().toString());
                object.put(Constants.ATHLETES_DESIRABLE_POSITION, positions.get(position).getID());
                object.put(Constants.ATHLETES_HEIGHT, height);
                object.put(Constants.ATHLETES_WEIGHT, weight);
                object.put(Constants.ATHLETES_BIRTHDAY, birthday);
                object.put(Constants.ATHLETES_EMAIL, editEmail.getText().toString());
                if(editAddress.getText().toString().trim().isEmpty())
                    object.put(Constants.ATHLETES_ADDRESS, " ");
                else
                    object.put(Constants.ATHLETES_ADDRESS, editAddress.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return object;
    }

    int index = 0;
    private int getIndex(MaterialBetterSpinner spinner) {
        try {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    String seluniversity = (String) arg0.getSelectedItem();
                    index = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }catch (Exception e){
            index = 0;
        }

        return index;
    }

    private String getString(EditText edit){
        return edit.getText().toString().trim().equals("") ? "" : edit.getText().toString();
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
        LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
        linearProgress.setVisibility(View.GONE);

        DeserializerJsonElements des = new DeserializerJsonElements(response);
        athlete = des.getAthlete();
        createCode(athlete.getId());
    }

    private void createCode(String idAthlete){
        String url = Constants.URL + Constants.API_SELECTIVEATHLETES;

        PostAthleteAsyncTask post = new PostAthleteAsyncTask();
        post.setActivity(CreateAccountAthlete.this);
        post.setObjPut(createObjectSelectiveAthletes(idAthlete));
        post.setPlay(false);
        post.execute(url);
    }

    private JSONObject createObjectSelectiveAthletes(String athlete) {
        DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
        Selective selective = db.getSelective();
        ArrayList<SelectiveAthletes> selectivesAthletes = db.getSelectivesAthletes();

        String numCode="";
        if(selectivesAthletes!=null)
            numCode = "01";
        else{
            int num = selectivesAthletes.size()+1;
            numCode = String.valueOf(num);
            if(num<9)
                numCode = "0"+num;
        }

        String nameSelective = selective.getTitle().toString().toUpperCase().substring(0,2)+"-"+numCode;

        JSONObject object = new JSONObject();
        try {
            object.put(Constants.SELECTIVEATHLETES_ATHLETE, athlete);
            object.put(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER, nameSelective);
            object.put(Constants.SELECTIVEATHLETES_SELECTIVE, selective.getId());
            object.put(Constants.SELECTIVEATHLETES_PRESENCE, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void afterSendSelectiveAthlete(Activity act, String response, String result){
        ((CreateAccountAthlete)act).afterSendSelectiveAthlete(response, result);
    }

    private void afterSendSelectiveAthlete(String response, String result){
        LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
        linearProgress.setVisibility(View.GONE);
        if(response.equals("FAIL"))
            Services.messageAlert(CreateAccountAthlete.this, "Menasgem", "Atleta não cadastrado\n"+result, "" );
        else if(response.equals("OK")) {
            DeserializerJsonElements des = new DeserializerJsonElements(result);
            SelectiveAthletes item = des.getSelectiveAthlete();

            DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
            db.addSelectiveAthlete(item);
            athlete.setCode(item.getInscriptionNumber());
            long ret = db.addAthlete(athlete);
            if(ret!=0){
                Services.messageAlert(CreateAccountAthlete.this, "Salvo", "Atleta cadastrado com sucesso", "POSTATHLETE");
            }
        }

    }

    private void clearForm(){
        editTextName.setText("");
        editTextCPF.setText("");
        editAddress.setText("");
        editEmail.setText("");
        editTextPhone.setText("");
        editTextWeihgt.setText("");
        editTextHeight.setText("");
        spinnerPosition.setText("");
        spinnerMonth.setText("");
        spinnerYear.setText("");
        spinnerDay.setText("");
    }

    public static void finished(Activity act){
        ((CreateAccountAthlete)act).finished();
    }

    public void finished(){
        clearForm();
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

    private void inflateSpinnerPosition(){
        DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
        positions = db.getPositions();

        if(positions!=null){
            String [] adapter = new String[positions.size()];
            for(int i =0;i<=positions.size()-1;i++){
                adapter[i] = positions.get(i).getNAME() +" ("+positions.get(i).getDESCRIPTION()+")";
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, adapter);
            spinnerPosition.setAdapter(arrayAdapter);

        }
    }

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
        spinnerMonth.setAdapter(arrayAdapterMonth);
        spinnerYear.setAdapter(arrayAdapterYear);
    }

    private boolean verifyForm(){
        boolean ver = true;
        if(!validaName(editTextName))
            ver = false;
        if(!isValidEmail(editEmail))
            ver = false;
        if (!validaCPF(editTextCPF))
            ver = false;
        if (!validaPhone(editTextPhone))
            ver = false;
        if(!validateHeight(editTextHeight))
            ver = false;
        if(!validateWeight(editTextWeihgt))
            ver = false;

        if(!ver)
            Services.messageAlert(this, "Alerta","Dados inválidos, por favor, verifique para continuar.","");
        return ver;
    }

    public boolean validaName(EditText edit) {
        boolean ver = false;
        if(getString(edit).length()>=5) {
            Services.changeColorEditBorder(edit, this);
            ver = true;
        }
        else
            Services.changeColorEditBorderError(edit, this);
        return ver;
    }

    public boolean validaPhone(EditText edit) {
        boolean ver = false;
        if(getString(edit).length()>=12) {
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
        else if(spinnerPosition.getText().toString().equals(""))
            Services.messageAlert(CreateAccountAthlete.this, "Alerta","Selecione uma posição desejada","");
        else
            ver=true;
        return ver;
    }

    private boolean validateHeight(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=3){
            Services.changeColorEditBorder(edit, this);
            ver = true;
        }
        else
            Services.changeColorEditBorderError(edit, this);
        return ver;
    }

    private boolean validateWeight(EditText edit){
        boolean ver = false;
        if(getString(edit).length()==2){
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
}

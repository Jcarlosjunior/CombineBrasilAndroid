package br.com.john.combinebrasil;

import android.app.Activity;
import android.app.Service;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Connection.Posts.PostAthleteAsyncTask;
import br.com.john.combinebrasil.Connection.Posts.PutAthlete;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.MaskHeight;
import br.com.john.combinebrasil.Services.MessageOptions;
import br.com.john.combinebrasil.Services.Services;

public class CreateAccountAthlete extends AppCompatActivity {
    MaterialBetterSpinner spinnerDay, spinnerMonth, spinnerYear, spinnerPosition;
    Toolbar toolbar;
    EditText editTextName, editTextCPF, editTextPhone, editTextHeight, editTextWeihgt, editEmail, editAddress;
    private Button buttonAdd;
    Athletes athlete;
    ArrayList<Positions> positions;

    TextView textTerms, bodyTextTerms;
    CheckBox checkTerms;
    Button btnClose;
    LinearLayout linearTerms;
    boolean checked = false, editAthlete = false;
    String idAthlete;
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

        textTerms = (TextView) findViewById(R.id.text_terms);
        checkTerms = (CheckBox) findViewById(R.id.check_terms);
        btnClose = (Button) findViewById(R.id.button_close);
        linearTerms = (LinearLayout) findViewById(R.id.linear_terms);

        textTerms.setOnClickListener(clickOpenTerms);
        btnClose.setOnClickListener(closeTerms);

        bodyTextTerms = (TextView) findViewById(R.id.text_terms_body);
        bodyTextTerms.setText(Html.fromHtml(Constants.TERMS_TEXT));

        textTerms.setVisibility(View.VISIBLE);

        checkTerms.setChecked(false);
        checkTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTerms.isChecked()) {
                    checked = true;
                    textTerms.setVisibility(View.GONE);
                }
                else {
                    checked = false;
                    textTerms.setVisibility(View.VISIBLE);
                }
            }
        });

        inflateSpinnerDay();
        inflateSpinnerPosition();

        Mask maskCpf = new Mask("###.###.###-##", editTextCPF);
        editTextCPF.addTextChangedListener(maskCpf);

        Mask maskPhone = new Mask("(##)#####-####", editTextPhone);
        editTextPhone.addTextChangedListener(maskPhone);

        TextWatcher mask = MaskHeight.insert("#,##", editTextHeight);
        editTextHeight.addTextChangedListener(mask);
        mask = MaskHeight.insert("###",editTextWeihgt);
        editTextWeihgt.addTextChangedListener(mask);

        buttonAdd.setOnClickListener(addAthleteClicked);

        if(Constants.debug)
            buttonAdd.setOnLongClickListener(longClick);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            editAthlete = extras.getBoolean("EditAthlete");
            if(editAthlete) {
                idAthlete = extras.getString("id_player");
                editAthlete();
            }
        }
    }

    private void editAthlete(){
        DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
        Athletes athlete = db.getAthleteById(idAthlete);

        checked = true;
        textTerms.setVisibility(View.GONE);
        editTextName.setText(athlete.getName());
        editTextCPF.setText(athlete.getCPF());
        editAddress.setText(athlete.getAddress());
        editEmail.setText(athlete.getEmail());
        editTextPhone.setText(athlete.getPhoneNumber());
        editTextHeight.setText(String.format("%.2f", athlete.getHeight()).replace(".",","));
        editTextWeihgt.setText(String.format("%.0f",athlete.getWeight()).replace(".",","));
        Positions position = db.getPositiomById(athlete.getDesirablePosition());
        if(position!=null)
            spinnerPosition.setText(position.getNAME());
        else
            spinnerPosition.setText("");
        String birthday = Services.convertDate(athlete.getBirthday());
        if(!birthday.isEmpty()){
            String day = birthday.substring(0,2);
            String month =Services.chooseMonth(birthday.substring(3,5));
            String year = birthday.substring(6);

            spinnerDay.setText(day);
            spinnerMonth.setText(month);
            spinnerYear.setText(year);
        }
        else{
            spinnerDay.setText("");
            spinnerMonth.setText("");
            spinnerYear.setText("");
        }
    }

    private View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            editTextName.setText("Atleta");
            editTextCPF.setText("43242343243");
            editAddress.setText("Rua do Atleta");
            editEmail.setText("atleta@atletinha.com");
            editTextPhone.setText("12988888888");

            editTextHeight.setText("190");
            editTextWeihgt.setText("90");

            return true;
        }
    };

    private View.OnClickListener addAthleteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callAddAthlete();
        }
    };

    private void callAddAthlete(){
       if(verifyForm()) {
           if(validaBirthday()) {
               if (checked == true) {
                   if (Services.isOnline(this)) {
                       LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
                       linearProgress.setVisibility(View.VISIBLE);
                       if(editAthlete){
                           String url = Constants.URL + Constants.API_ATHLETES+"/"+idAthlete;
                           PutAthlete post = new PutAthlete();
                           post.setActivity(CreateAccountAthlete.this);
                           post.setObjPut(createObject());
                           post.setPlay(true);
                           post.execute(url);
                       }else {
                           String url = Constants.URL + Constants.API_ATHLETES;
                           PostAthleteAsyncTask post = new PostAthleteAsyncTask();
                           post.setActivity(CreateAccountAthlete.this);
                           post.setObjPut(createObject());
                           post.setPlay(true);
                           post.execute(url);
                       }
                   } else
                       new MessageOptions(CreateAccountAthlete.this, "Mensagem", "Você está offline, deseja salvar o usuário offline? Você deverá sincronizar com os outros avaliadores.","createUserOff");

               }
           }
           else
               Services.messageAlert(CreateAccountAthlete.this, "Aviso", "Para continuar, é necessário que o atleta aceite os termos de uso.", "");
       }
    }

    public static void createAthleteOff (Activity act){
        ((CreateAccountAthlete)act).createAthleteOff();
    }

    private void createAthleteOff(){
        try{
            Athletes athlete = createAthlete();
            SelectiveAthletes selectiveAthlete = createSelectiveAthletes(athlete);
            athlete.setCode(selectiveAthlete.getInscriptionNumber());

            DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
            long res = db.addAthlete(athlete);
            if (res == 0) {
                Athletes obj = db.getAthleteByValue(Constants.ATHLETES_CPF, athlete.getCPF());
                if(obj != null)
                    Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "CPF do usuário já esta cadastrado.", "");
                else{
                    obj = db.getAthleteByValue(Constants.ATHLETES_EMAIL, athlete.getEmail());
                    if(obj != null)
                        Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "E-mail do usuário já esta cadastrado.", "");
                    else{
                        obj = db.getAthleteByValue(Constants.ATHLETES_CODE, athlete.getCode());
                        if(obj != null)
                            Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "Código já cadastrado.", "");
                    }
                    Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "Erro ao cadastrar!", "");
                }
            }

            db.addSelectiveAthlete(selectiveAthlete);

            Services.messageAlert(CreateAccountAthlete.this, "Concluído", "Atleta criado, o código do atleta é "+selectiveAthlete.getInscriptionNumber()+". Informe aos outros avaliadores.", "");
            clearForm();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private SelectiveAthletes createSelectiveAthletes(Athletes athlete){
        SelectiveAthletes selectiveAthlete;
        try {
            DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
            Selective selective = db.getSelective();
            ArrayList<SelectiveAthletes> selectivesAthletes = db.getSelectivesAthletes();
            String numCode = "";
            if (selectivesAthletes == null)
                numCode = "01";
            else {
                int num;
                try {
                    num = selectivesAthletes.size() + 1;
                } catch (NullPointerException e) {
                    num = 1;
                }
                numCode = String.valueOf(num);
                if (num < 9)
                    numCode = "0" + num;
            }

            String nameSelective = selective.getTitle().toString().toUpperCase().substring(0, 2) + "-" + numCode;


            selectiveAthlete = new SelectiveAthletes(
                    UUID.randomUUID().toString(),
                    athlete.getId(),
                    selective.getId(),
                    nameSelective,
                    true
            );
        }catch (Exception e){
            return  null;
        }

        return selectiveAthlete;
    }

    private Athletes createAthlete(){
        Athletes athletes;
        try {
            String address = " ";
            String birthday = spinnerYear.getText().toString() + "-" + chooseMonth(spinnerMonth.getText()
                    .toString()) + "-" + spinnerDay.getText().toString();

            double height = Double.parseDouble(editTextHeight.getText().toString().replaceAll(",", "."));
            double weight = Double.parseDouble(editTextWeihgt.getText().toString().replaceAll(",", "."));

            if (!(editAddress.getText().toString().trim().isEmpty()))
                address = editAddress.getText().toString();

            athletes = new Athletes(
                    UUID.randomUUID().toString(),
                    editTextName.getText().toString(),
                    birthday,
                    editTextCPF.getText().toString(),
                    address,
                    getIdPosition(spinnerPosition),
                    height,
                    weight,
                    " ",
                    " ",
                    " ",
                    editEmail.getText().toString(),
                    editTextPhone.getText().toString(),
                    false,
                    true
                    );
        }
        catch (Exception e){
            return null;
        }
        return athletes;

    }

    private JSONObject createObject() {
        JSONObject object = new JSONObject();
        try {

            try {
                double height = Double.parseDouble(editTextHeight.getText().toString().replaceAll(",", "."));
                double weight = Double.parseDouble(editTextWeihgt.getText().toString().replaceAll(",", "."));
                String birthday = spinnerYear.getText().toString() + "-" + chooseMonth(spinnerMonth.getText().toString()) + "-" + spinnerDay.getText().toString();
                object.put(Constants.ATHLETES_NAME, editTextName.getText().toString());
                object.put(Constants.ATHLETES_CPF, editTextCPF.getText().toString());
                object.put(Constants.ATHLETES_PHONE, editTextPhone.getText().toString());
                object.put(Constants.ATHLETES_DESIRABLE_POSITION, getIdPosition(spinnerPosition));
                object.put(Constants.ATHLETES_HEIGHT, height);
                object.put(Constants.ATHLETES_WEIGHT, weight);
                object.put(Constants.ATHLETES_BIRTHDAY, birthday);
                object.put(Constants.ATHLETES_TERMSACCEPTED, true);
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


    private String getIdPosition(MaterialBetterSpinner spinner) {
        String positionSelected = "";
        try {
            positionSelected = spinner.getText().toString();
            if(!positionSelected.trim().equals("")){
                for(Positions position : positions){
                    if(positionSelected.trim().equals(position.getNAME())){
                        positionSelected = position.getID();
                        return position.getID();
                    }
                }
            }
        }catch (Exception e){
            return "";
        }
        return positionSelected;
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
                verifyErrorCreate(response);

            else if(ret.equals("OK")) {
                if (!editAthlete)
                    saveAthlete(response);
                else
                    updateAthlete(response);
            }
    }

    private void verifyErrorCreate(String result){
        JSONObject json;
        try{
            json = new JSONObject(result);
            String detail = json.getString("detail");
            json = new JSONObject(detail);
            if(json.getInt("code") ==  11000)
                saveAthlete(json.getString("op"));
            else
                Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "Atleta não cadastrado\n" + result, "");

        }catch(JSONException e){
            e.printStackTrace();
            try {
                json = new JSONObject(result);
                Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "Atleta não cadastrado\n" + json.getString("message"), "");
            } catch (JSONException e1) {
                Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "Atleta não cadastrado\n" + result, "");
                e1.printStackTrace();
            }
        }
    }

    private void updateAthlete(String response){
        String url = Constants.URL + Constants.API_ATHLETES+"?"+Constants.ATHLETES_ID+"="+idAthlete;
        Connection task = new Connection(url, 0, "updateAthleteAccount",false, CreateAccountAthlete.this);
        task.callByJsonStringRequest();

        LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
        linearProgress.setVisibility(View.VISIBLE);
    }
    public static void returnAccountAthlete(Activity act, String response){
        ((CreateAccountAthlete)act).returnAccountAthlete(response);
    }
    private void returnAccountAthlete(String response){

        LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
        linearProgress.setVisibility(View.GONE);

        DeserializerJsonElements des = new DeserializerJsonElements(response);
        athlete = des.getAthlete();
        if(athlete!=null){
            DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
            SelectiveAthletes seletive = db.getSelectiveAthletesFromAthlete(athlete.getId());
            athlete.setCode(seletive.getInscriptionNumber());
            db.updateAthlete(athlete);
        }

        Services.messageAlert(CreateAccountAthlete.this, "Mensagem","Atleta autalizado","updateAthelete");
    }

    private void saveAthlete(String response){
        LinearLayout linearProgress = (LinearLayout) findViewById(R.id.linear_progress_add);
        linearProgress.setVisibility(View.GONE);

        DeserializerJsonElements des = new DeserializerJsonElements(response);
        athlete = des.getObjAthlete();
        DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);

        try {
            if (!db.existAthleteByCPF(athlete.getCPF()))
                createCode(athlete.getId());
            else
                Services.messageAlert(CreateAccountAthlete.this, "Aviso", "Ops, esse atleta já esta cadastrado na seletiva", "");
        }catch (Exception e){
            Services.messageAlert(CreateAccountAthlete.this, "Aviso", "Ops, esse atleta já esta cadastrado na seletiva", "");
        }
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
        if(selectivesAthletes==null)
            numCode = "01";
        else{
            int num;
            try {
                num = selectivesAthletes.size() + 1;
            }catch (NullPointerException e){
                num = 1;
            }
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
        if(response.equals("FAIL")) {
            Services.messageAlert(CreateAccountAthlete.this, "Mensagem", "Atleta não cadastrado\n" + result, "");
        }
        else if(response.equals("OK")) {
            DeserializerJsonElements des = new DeserializerJsonElements(result);
            SelectiveAthletes item = des.getObjSelectiveAthlete();

            DatabaseHelper db = new DatabaseHelper(CreateAccountAthlete.this);
            db.addSelectiveAthlete(item);
            athlete.setCode(item.getInscriptionNumber());
            long ret = db.addAthlete(athlete);
            if(ret!=0){
                Services.messageAlert(CreateAccountAthlete.this, "Salvo", "Atleta cadastrado com sucesso. O código do atleta é "+athlete.getCode(), "POSTATHLETE");
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
        textTerms.setVisibility(View.VISIBLE);
        checkTerms.setChecked(false);
        checked = false;
    }

    public static void update(Activity act){
        ((CreateAccountAthlete)act).update();
    }
    private void update(){
        this.finish();
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
        positions = new ArrayList<Positions>();
        positions = db.getPositions();

        if(positions!=null){
            String [] adapter = new String[positions.size()];
            for (int i=0; i<=positions.size()-1;i++)
                adapter[i] = positions.get(i).getNAME();
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
            int first = Integer.parseInt(edit.getText().toString().substring(0,1));
            if(first>=3){
                Services.changeColorEditBorderError(edit, this);
            }
            else {
                Services.changeColorEditBorder(edit, this);
                ver = true;
            }
        }
        else
            Services.changeColorEditBorderError(edit, this);
        return ver;
    }

    private boolean validateWeight(EditText edit){
        boolean ver = false;
        if(getString(edit).length()>=2){
            int height = Integer.parseInt(getString(edit));
            if(height>300)
                Services.changeColorEditBorderError(edit, this);
            else {
                Services.changeColorEditBorder(edit, this);
                ver = true;
            }
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

    private View.OnClickListener clickOpenTerms = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearTerms.setVisibility(View.VISIBLE);
        }
    };

    private  View.OnClickListener closeTerms = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearTerms.setVisibility(View.GONE);
        }
    };
}

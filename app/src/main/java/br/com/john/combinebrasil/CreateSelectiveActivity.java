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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.shawnlin.numberpicker.NumberPicker;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.Mask;
import br.com.john.combinebrasil.Services.MaskHeight;
import br.com.john.combinebrasil.Services.Services;

public class CreateSelectiveActivity extends AppCompatActivity{
    Switch switchPay;
    EditText editTitle, editNotes, editPrice;
    TextView textDate, textSecondDate, textThirdDate;
    LinearLayout linearProgress;
    Button btnCreateSelective, btnCancelDate, btnConfirmDate, btnConfirmTime, btnCancelTime;
    ImageView imgAddDate, imgAddSecondDate, imgAddThirdDate;
    TextView textProgress;
    Toolbar toolbar;
    NumberPicker nPHour, nPminute;

    public static Activity act;
    MaterialCalendarView calendarDates;
    ConstraintLayout constraintCalendar, constraintTime;
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

        act = CreateSelectiveActivity.this;
        textProgress = (TextView) findViewById(R.id.text_progress);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);

        textDate = (TextView) findViewById(R.id.txt_date);
        textSecondDate = (TextView) findViewById(R.id.txt_date_second);
        textThirdDate = (TextView) findViewById(R.id.txt_date_third);
        imgAddDate = (ImageView) findViewById(R.id.img_add_date);
        imgAddSecondDate = (ImageView) findViewById(R.id.img_add_second_date);
        imgAddThirdDate = (ImageView) findViewById(R.id.img_add_third_date);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editPrice = (EditText) findViewById(R.id.edit_selective_price);
        switchPay = (Switch) findViewById(R.id.switch_pay);

        editNotes = (EditText) findViewById(R.id.edit_notes);
        btnCreateSelective = (Button) findViewById(R.id.btn_create_selective);
        constraintCalendar = (ConstraintLayout) findViewById(R.id.constraint_calendar);
        constraintTime = (ConstraintLayout) findViewById(R.id.constraint_time);
        btnCancelDate = (Button) findViewById(R.id.btn_cancel_date);
        btnConfirmDate = (Button) findViewById(R.id.btn_confirm_date);
        btnConfirmTime = (Button) findViewById(R.id.btn_confirm_time);
        btnCancelTime = (Button) findViewById(R.id.btn_cancel_time);

        nPHour = (NumberPicker) findViewById(R.id.number_picker_hour);
        nPminute = (NumberPicker) findViewById(R.id.number_picker_min);
        nPHour.setFormatter("%02d");
        nPminute.setFormatter("%02d");

        calendarDates = (MaterialCalendarView) findViewById(R.id.calendar_dates);
        textDate.setOnClickListener(clickedDateListener);
        textSecondDate.setOnClickListener(clickedSecondDateListener);
        textThirdDate.setOnClickListener(clickedThirdDateListener);
        imgAddSecondDate.setOnClickListener(clickedAddSecondDate);
        imgAddThirdDate.setOnClickListener(clickedAddThirdDate);
        btnCancelDate.setOnClickListener(clickedCancelDate);
        btnConfirmDate.setOnClickListener(clickedConfirmDate);
        btnCreateSelective.setOnClickListener(clickCreateSelective);
        btnCancelTime.setOnClickListener(clickCloseTime);
        btnConfirmTime.setOnClickListener(clickConfirmTime);
        btnCreateSelective.setOnLongClickListener(longCreateSelective);
        textSecondDate.addTextChangedListener(textChangeSecond);

        switchPay.setOnCheckedChangeListener(changedListenerSwitchPay);

        TextWatcher mask = MaskHeight.insert("##,##", editPrice);
        editPrice.addTextChangedListener(mask);

        final View activityRootView = findViewById(R.id.activity_create_selective);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(act, 200)) { // if more than 200 dp, it's probably a keyboard...
                    btnCreateSelective.setVisibility(View.INVISIBLE);
                }
                else
                    btnCreateSelective.setVisibility(View.VISIBLE);
            }
        });
        imgAddDate.setVisibility(View.INVISIBLE);

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateSelectiveActivity.this.finish();
        }
    };
    private View.OnClickListener clickCreateSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(verifyFields()) {
                callChooseTestsSelective();
            }
        }
    };

    private boolean verifyFields(){
        boolean ver = true;
        if(!validaEdit(editTitle))
            ver = false;
        if(!validaDate(textDate))
            ver = false;
        if(!ver)
            Services.messageAlert(this, "Alerta","Dados inválidos, por favor, verifique para continuar.","");
        return ver;
    }

    public boolean validaDate(TextView date) {
        boolean ver = false;
        if(!(date.getText().length()==0)) {
            date.setBackground(act.getResources().getDrawable(R.drawable.background_edit));
            ver = true;
        }
        else
            date.setBackground(act.getResources().getDrawable(R.drawable.background_edit_border_error));
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
        AllActivities.hashInfoSelective.put("date", textDate.getText().toString());
        if(!(textSecondDate.getText().equals("")))
             AllActivities.hashInfoSelective.put("dateSecond", textSecondDate.getText().toString());
        if(!(textThirdDate.getText().equals("")))
            AllActivities.hashInfoSelective.put("dateThird", textThirdDate.getText().toString());
        AllActivities.hashInfoSelective.put("title",editTitle.getText().toString());
        AllActivities.hashInfoSelective.put("notes",editNotes.getText().toString());
        AllActivities.hashInfoSelective.put("price",switchPay.isChecked() ? editPrice.getText().toString() : "");
        startActivity(i);

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
        Calendar c = Calendar.getInstance();
        switch (date){
            case 1 :
                if(textDate.getText().length()==0)
                    calendarDates.setSelectedDate(c);
                else{
                    Date d = Services.convertStringInDate(textDate.getText().toString(), "yyyy/MM/dd",'/');
                    c.setTime(d);
                    calendarDates.setSelectedDate(c);
                }
                break;
            case 2 :
                if(textSecondDate.getText().length()==0)
                    calendarDates.setSelectedDate(c);
                else{
                    Date d = Services.convertStringInDate(textSecondDate.getText().toString(), "yyyy/MM/dd",'/');
                    c.setTime(d);
                    calendarDates.setSelectedDate(c);
                }
                break;
            case 3 :
                if(textThirdDate.getText().length()==0)
                    calendarDates.setSelectedDate(c);
                else{
                    Date d = Services.convertStringInDate(textThirdDate.getText().toString(), "yyyy/MM/dd",'/');
                    c.setTime(d);
                    calendarDates.setSelectedDate(c);
                }
                break;
        }
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
        Date date = calendarSelected.getDate();
        if(dateClicked==1) {
            SimpleDateFormat dtForm = new SimpleDateFormat("dd/MM/yyyy");
            Date current = Calendar.getInstance().getTime();
            String reportDate = dtForm.format(current);
            if(verifyDate(reportDate, convertDateCalendar(formatter.format(date)))) {
                textDate.setText(convertDateCalendar(formatter.format(date)));
                imgAddDate.setVisibility(View.VISIBLE);
                imgAddDate.setOnClickListener(clickedAddDate);

                showTimeChoose();
                //constraintCalendar.setVisibility(View.GONE);
            }
            else
                Toast.makeText(this, "A data selecionada deve ser maior que a data atual.", Toast.LENGTH_SHORT).show();
        }
        else if(dateClicked==2) {
            if(verifyDate(textDate.getText().toString(), convertDateCalendar(formatter.format(date)))) {
                textSecondDate.setText(convertDateCalendar(formatter.format(date)));
                constraintTime.setVisibility(View.VISIBLE);
                showTimeChoose();
                //constraintCalendar.setVisibility(View.GONE);
            }
            else
                Toast.makeText(this, "A segunda deve ser maior que a primeira selecionada.", Toast.LENGTH_SHORT).show();
        }
        else {
            if(verifyDate(textSecondDate.getText().toString(), convertDateCalendar(formatter.format(date)))) {
                textThirdDate.setText(convertDateCalendar(formatter.format(date)));
                constraintTime.setVisibility(View.VISIBLE);
                showTimeChoose();
                //constraintCalendar.setVisibility(View.GONE);
            }
            else
                Toast.makeText(this, "A terceira deve ser maior que a segunda selecionada.", Toast.LENGTH_SHORT).show();

        }
    }

    private void showTimeChoose(){
        nPHour.setValue(0);
        nPminute.setValue(0);

        constraintTime.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener clickCloseTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            constraintTime.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener clickConfirmTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            confirmTime();
        }
    };

    private void confirmTime(){
        String hour = String.format("%02d", nPHour.getValue());
        String minute = String.format("%02d", nPminute.getValue());

        switch (dateClicked){
            case 1 : textDate.setText(textDate.getText().toString()+" - "+ hour+":"+minute);
                break;
            case 2 : textSecondDate.setText(textSecondDate.getText().toString()+" - "+ hour+":"+minute);
                break;
            case 3 : textThirdDate.setText(textThirdDate.getText().toString()+" - "+ hour+":"+minute);
        }

        constraintTime.setVisibility(View.GONE);
        constraintCalendar.setVisibility(View.GONE);
    }

    private boolean verifyDate(String dtPrincipal,String dtValid){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateP = sdf.parse(dtPrincipal);

            Date strDate = sdf.parse(dtValid);
            if (strDate.after(dateP)) {
               return true;
            }
            else
                return false;
        }catch (Exception ex){
            ex.getStackTrace();
            return false;
        }
    }

    private View.OnClickListener clickedCancelDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            constraintCalendar.setVisibility(View.GONE);
        }
    };


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
        String text = textSecondDate.getText().toString();
        if(text.equals("")){
            textSecondDate.setText("");
            hideDate(textSecondDate, imgAddSecondDate);
            showDate(textDate, imgAddDate);
            imgAddDate.setImageDrawable(getDrawable(R.drawable.ic_add));
        }
        else{
            showDate(textThirdDate, imgAddThirdDate);
            imgAddSecondDate.setVisibility(View.INVISIBLE);
            imgAddThirdDate.setImageDrawable(getDrawable(R.drawable.ic_less));
        }
    }

    private TextWatcher textChangeSecond = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>6){
                imgAddSecondDate.setImageDrawable(getDrawable(R.drawable.ic_add));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private View.OnClickListener clickedAddThirdDate = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            clickAddSThirdDate();
        }
    };

    private void clickAddSThirdDate(){
        textThirdDate.setText("");
        hideDate(textThirdDate, imgAddThirdDate);
        imgAddSecondDate.setVisibility(View.VISIBLE);
        imgAddSecondDate.setImageDrawable(getDrawable(R.drawable.ic_add));
    }

    private String convertDateCalendar(String date){
        String month =  date.substring(0,2);
        String day = date.substring(3,5);
        String year = date.substring(6, 10);
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

    private View.OnLongClickListener longCreateSelective= new View.OnLongClickListener(){

        @Override
        public boolean onLongClick(View v) {
            if(Constants.debug) {
                editTitle.setText("Seletiva Teste");
                editNotes.setText("levar tenis, short preto");
            }
            return true;
        }
    };

    private Switch.OnCheckedChangeListener changedListenerSwitchPay = new Switch.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                editPrice.setVisibility(View.VISIBLE);
            else
                editPrice.setVisibility(View.GONE);
        }
    };

    public static void finishOhterActivity(){
        ((CreateSelectiveActivity)act).finish();
    }

}

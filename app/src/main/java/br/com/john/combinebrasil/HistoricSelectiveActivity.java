package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerSelectives;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

import static android.view.View.GONE;

public class HistoricSelectiveActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerSelectives;
    ConstraintLayout linearProgress, linearSearchNull, constraintInfoSelective, constraintFilters, constraintCalendar;
    EditText editSearch;
    ImageView imageClose, imageChangeDates, imageCloseFilter, imageRemoveFilters;
    Button btnApplyFilter, btnDateInit, btnDateEnd, btnAdmin, btnAvaliator, btnCancel, btnConfirm;
    SeekBar seekBarDays;
    TextView textTitle, textTeamSelective, textDateSelective, textAddressSelective, textInfoSelective, textAdminSelective;
    LinearLayout linearFilter;
    MaterialCalendarView calendarDates;

    AdapterRecyclerSelectives adapterSelectives;

    ArrayList<Selective> selectives;

    Date dateInit, dateEnd;
    int daysFilter = 0;
    boolean admin = false, avaliator = false, isFilter= false, isDateSeek = false, isDateInit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_selective);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        linearFilter = (LinearLayout) findViewById(R.id.linear_add_account);
        ImageView imageFilter = (ImageView) findViewById(R.id.img_create_account);
        imageFilter.setImageDrawable(this.getDrawable(R.drawable.ic_filter_list_white_24dp));
        imageFilter.setOnClickListener(clickFilter);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        editSearch = (EditText) findViewById(R.id.edit_search);

        linearSearchNull = (ConstraintLayout) findViewById(R.id.linear_search_null);
        linearProgress = (ConstraintLayout) findViewById(R.id.linear_progress);
        constraintInfoSelective = (ConstraintLayout) findViewById(R.id.constraint_info_selective);
        constraintFilters = (ConstraintLayout) findViewById(R.id.constraint_filters_selective);
        constraintCalendar = (ConstraintLayout) findViewById(R.id.constraint_calendar);

        imageClose = (ImageView) findViewById(R.id.image_close_info);
        imageChangeDates = (ImageView) findViewById(R.id.img_change_days_filter);
        imageRemoveFilters = (ImageView) findViewById(R.id.image_remove_all_filters);
        imageCloseFilter = (ImageView) findViewById(R.id.image_close_filter);

        textTitle = (TextView) findViewById(R.id.text_tittle_selective);
        textTeamSelective = (TextView) findViewById(R.id.team_name_selective);
        textDateSelective = (TextView) findViewById(R.id.text_date_selective);
        textInfoSelective = (TextView) findViewById(R.id.text_info_selective);
        textAddressSelective = (TextView) findViewById(R.id.text_address_selective);
        textAdminSelective = (TextView) findViewById(R.id.text_admin_selctive);

        btnApplyFilter = (Button) findViewById(R.id.button_apply_filter);
        btnAdmin = (Button)findViewById(R.id.btn_admin_mode_filter);
        btnAvaliator = (Button) findViewById(R.id.btn_avaliator_mode_filter);
        btnDateInit = (Button) findViewById(R.id.btn_date_init);
        btnDateEnd = (Button) findViewById(R.id.btn_date_end);
        btnCancel = (Button) findViewById(R.id.btn_cancel_date);
        btnConfirm = (Button) findViewById(R.id.btn_confirm_date);

        seekBarDays = (SeekBar) findViewById(R.id.seekbar_filter_days);

        calendarDates = (MaterialCalendarView) findViewById(R.id.calendar_filter_selectives);

        recyclerSelectives = (RecyclerView) findViewById(R.id.recycler_historic_selective);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                    searchSelective(s.toString());
                else
                    inflateSelective(selectives);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                }
            }
        });
        imageClose.setOnClickListener(clickCloseInfo);
        imageCloseFilter.setOnClickListener(cickledCloseFilter);
        imageRemoveFilters.setOnClickListener(clickedRemoveAllFilters);
        imageChangeDates.setOnClickListener(clickedChangeDates);
        btnAdmin.setOnClickListener(clickedAdminFilter);
        btnAvaliator.setOnClickListener(clickedAvaliador);
        btnDateInit.setOnClickListener(clickedDateInit);
        btnDateEnd.setOnClickListener(clickedDateEnd);
        btnCancel.setOnClickListener(clickedCancelDate);
        btnConfirm.setOnClickListener(clickedConfirmDate);
        btnApplyFilter.setOnClickListener(clickedApplyFilter);
        seekBarDays.setOnSeekBarChangeListener(seekBarChange);

        getAllSelectives();
        removeAllFilters();
    }

    private void getAllSelectives(){
        if(Services.isOnline(HistoricSelectiveActivity.this)) {
            linearProgress.setVisibility(View.VISIBLE);

            String url = Constants.URL + Constants.API_SELECTIVES;
            Connection task = new Connection(url, 0, Constants.CALLED_GET_SELECTIVE, false, HistoricSelectiveActivity.this);
            task.callByJsonStringRequest();
        }
    }

    public static void returnGetAllSelectives(Activity act, String response, int status){
        ((HistoricSelectiveActivity)act).returnGetAllSelectives(response, status);
    }

    private void returnGetAllSelectives(String response, int status){
        linearProgress.setVisibility(GONE);
        if(status == 200 || status == 201) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            selectives = des.getSelective();
            try{
                if (selectives!=null)
                    recordingSelectives(selectives);
            }catch (Exception e){}
        }
    }

    private void recordingSelectives(ArrayList<Selective> selectives){
        DatabaseHelper db = new DatabaseHelper(HistoricSelectiveActivity.this);
        try {
            db.createDataBase();
            if(selectives!=null) {
                db.openDataBase();
                db.addSelectives(selectives);
                inflateSelective(selectives);
            }
        } catch (Exception e) {}
    }

    private void inflateSelective(ArrayList<Selective> selectives){
        if(!(selectives == null || selectives.size()==0)){
            String[] values = new String[selectives.size()];
            for(int i=0; i <=selectives.size()-1; i++){
                values[i] = selectives.get(i).getId();
            }
            inflateRecyclerView(selectives, values);
        }
    }

    private void inflateRecyclerView(ArrayList<Selective> arraySelectives, String[] values){
        recyclerSelectives.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterSelectives = new AdapterRecyclerSelectives(HistoricSelectiveActivity.this, arraySelectives, values);
        recyclerSelectives.setVisibility(View.VISIBLE);
        recyclerSelectives.setAdapter(adapterSelectives);
    }

    private void searchSelective(String search){
        DatabaseHelper db = new DatabaseHelper(HistoricSelectiveActivity.this);
        db.openDataBase();
        ArrayList<Selective> selectiveList = db.searchSelective(search);
        if(selectiveList!= null && selectiveList.size()>0) {
            inflateSelective(selectiveList);
            recyclerSelectives.setVisibility(View.VISIBLE);
            linearSearchNull.setVisibility(GONE);
        }
        else{
            recyclerSelectives.setVisibility(GONE);
            linearSearchNull.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HistoricSelectiveActivity.this.finish();
        }
    };

    private View.OnClickListener clickFilter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            constraintFilters.setVisibility(View.VISIBLE);
        }
    };

    public static void onClickSelective(Activity act, int position){
        ((HistoricSelectiveActivity) act).onClickSelective(position);
    }
    private void onClickSelective(int position){
        constraintInfoSelective.setVisibility(View.VISIBLE);
        showInfoSelective(selectives.get(position));
    }

    private void showInfoSelective(Selective selective){
        DatabaseHelper db = new DatabaseHelper(this);
        db.openDataBase();
        textTitle.setText(selective.getTitle());
        textDateSelective.setText(Services.convertDate(selective.getDate()));
        textTeamSelective.setText(db.getNameTeamByIdTeam(selective.getTeam()));
        textAddressSelective.setText(selective.getAddress());
        textAdminSelective.setText("Jonathan Marques");
        textInfoSelective.setText("A seletiva já ocorreu. Foram inscritos 160 pessoas na seletiva.");
    }

    private View.OnClickListener clickCloseInfo = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            constraintInfoSelective.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener cickledCloseFilter = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            constraintFilters.setVisibility(View.GONE);
        }
    };
    private View.OnClickListener clickedRemoveAllFilters = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            removeAllFilters();
        }
    };

    private void removeAllFilters(){
        dateEnd = null;
        dateInit = null;
        daysFilter = 0;
        admin = false;
        avaliator = false;

        noSelectedButtonFilter(btnAdmin);
        noSelectedButtonFilter(btnAvaliator);
        seekBarDays.setProgress(0);
        btnDateInit.setText(this.getString(R.string.date_init));
        btnDateEnd.setText(this.getString(R.string.date_end));
        imageRemoveFilters.setAlpha(0.5f);
    }

    private void verifyFilter(){
        boolean ver = false;
        if(admin)
            ver = true;
        if(avaliator)
            ver = true;
        if(dateInit != null)
            ver = true;
        if(dateEnd != null)
            ver = true;
        if(daysFilter>0)
            ver = true;
        if(ver)
            imageRemoveFilters.setAlpha(1f);
        else
            imageRemoveFilters.setAlpha(0.5f);
    }

    private void noSelectedButtonFilter(Button button){
        button.setBackground(this.getDrawable(R.drawable.background_button_white));
    }
    private void selectedButtonFilter(Button button){
        button.setBackground(this.getDrawable(R.drawable.background_button_blue));
    }
    private View.OnClickListener clickedDateInit = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            clickDateInit();
        }
    };
    private void clickDateInit(){
        isDateInit = true;
        Calendar c = Calendar.getInstance();
        if(dateInit==null)
            calendarDates.setSelectedDate(c.getTime());
        else
            calendarDates.setSelectedDate(dateInit);
        constraintCalendar.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener clickedDateEnd = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            clickDateEnd();
        }
    };
    private void clickDateEnd(){
        isDateInit = false;
        Calendar c = Calendar.getInstance();
        if(dateEnd==null)
            calendarDates.setSelectedDate(c.getTime());
        else
            calendarDates.setSelectedDate(dateEnd);
        constraintCalendar.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener clickedChangeDates = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showButtonsDates();
        }
    };
    private void showButtonsDates(){
        if(isDateSeek){
            isDateSeek=false;
            seekBarDays.setVisibility(View.GONE);
            btnDateEnd.setVisibility(View.VISIBLE);
            btnDateInit.setVisibility(View.VISIBLE);
        }
        else {
            isDateSeek = true;
            seekBarDays.setVisibility(View.VISIBLE);
            btnDateEnd.setVisibility(View.GONE);
            btnDateInit.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener clickedAdminFilter = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            clickAdmin();
        }
    };
    private void clickAdmin(){
        if(admin){
            admin = false;
            noSelectedButtonFilter(btnAdmin);
        }
        else{
            admin = true;
            selectedButtonFilter(btnAdmin);
        }
        verifyFilter();
    }

    private View.OnClickListener clickedAvaliador = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            clickedAvaliator();
        }
    };
    private void clickedAvaliator(){
        if(avaliator){
            avaliator = false;
            noSelectedButtonFilter(btnAvaliator);
        }
        else{
            avaliator = true;
            selectedButtonFilter(btnAvaliator);
        }
        verifyFilter();
    }

    private View.OnClickListener clickedCancelDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            constraintCalendar.setVisibility(View.GONE);
        }
    };

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
        if(isDateInit) {
            dateInit = calendarSelected.getDate();
            btnDateInit.setText(convertDateCalendar(formatter.format(dateInit)));
        }
        else {
            dateEnd = calendarSelected.getDate();
            btnDateEnd.setText(formatter.format(dateEnd));
        }
    }

    private String convertDateCalendar(String date){
        String month =  date.substring(0,2);
        String day = date.substring(3,5);
        String year = date.substring(6,date.length());
        return day + "/"+month+"/"+year;
    }

    private SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            daysFilter = progress;
            verifyFilter();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private View.OnClickListener clickedApplyFilter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            applyFilter();
        }
    };
    private void applyFilter(){
        if(isDateSeek){

        }

    }


}

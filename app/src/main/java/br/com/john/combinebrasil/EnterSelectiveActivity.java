package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.com.john.combinebrasil.AdapterList.AdapterRecyclerAthletes;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerSelectives;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Connection.Connection;
import br.com.john.combinebrasil.Connection.JSONServices.DeserializerJsonElements;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

public class EnterSelectiveActivity extends AppCompatActivity {
    private final int CHOOSE_NAME=0, CHOOSE_TEAM=1, CHOOSE_DATE=2;
    Toolbar toolbar;
    LinearLayout linearSearchNull, linearProgress, linearOrder;
    RecyclerView recyclerSelectives;
    EditText editSearch;
    ImageView imageOrder;
    private TextView textOptionName, textOptionTeam, textOptionDate;
    private Button btnCancel;
    AdapterRecyclerSelectives adapterSelectives;
    ArrayList<Selective> selectives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_selective);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        recyclerSelectives = (RecyclerView) findViewById(R.id.list_selectives);
        recyclerSelectives.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerSelectives.setVisibility(View.VISIBLE);

        linearSearchNull = (LinearLayout) findViewById(R.id.linear_search_null);
        linearSearchNull.setVisibility(View.GONE);
        linearProgress = (LinearLayout) findViewById(R.id.linear_progress);
        linearOrder = (LinearLayout)findViewById(R.id.linear_order_by);
        linearOrder.setVisibility(View.GONE);
        editSearch = (EditText)findViewById(R.id.edit_search);
        editSearch.setOnTouchListener(editTouch);
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
        imageOrder = (ImageView) findViewById(R.id.img_order);
        textOptionName = (TextView) findViewById(R.id.text_option_order_name);
        textOptionDate = (TextView) findViewById(R.id.text_option_order_date);
        textOptionTeam = (TextView) findViewById(R.id.text_option_order_team);
        btnCancel = (Button) findViewById(R.id.btn_cancel_order);

        textOptionName.setOnClickListener(clickedOrderName);
        textOptionDate.setOnClickListener(clickedOrderDate);
        textOptionTeam.setOnClickListener(clickedOrderTeam);
        btnCancel.setOnClickListener(hideOptionsOrder);
        getAllSelectives();
    }

    public static void onClickItemList(Activity activity, int positionArray){
        ((EnterSelectiveActivity) activity).onClickItemList(positionArray);
    }

    public void onClickItemList(int position){
        Selective player  = selectives.get(position);
        Intent i;
        i = new Intent(EnterSelectiveActivity.this, SelectiveDetailsActivity.class);
        i.putExtra("id_selective",player.getId());
        i.putExtra("position",position);
        startActivity(i);
    }

    private void getAllSelectives(){
        if(Services.isOnline(EnterSelectiveActivity.this)) {
            linearProgress.setVisibility(View.VISIBLE);

            String url = Constants.URL + Constants.API_SELECTIVES;
            Connection task = new Connection(url, 0, Constants.CALLED_GET_SELECTIVE, false, EnterSelectiveActivity.this);
            task.callByJsonStringRequest();
        }
    }

    public static void returnGetAllSelectives(Activity act, String response, int status){
        ((EnterSelectiveActivity)act).returnGetAllSelectives(response, status);
    }

    private void returnGetAllSelectives(String response, int status){
        linearProgress.setVisibility(View.GONE);
        if(status == 200 || status == 201) {
            DeserializerJsonElements des = new DeserializerJsonElements(response);
            selectives = des.getSelective();
            try{
                if (selectives!=null)
                    recordingTeams(selectives);
            }catch (Exception e){}
        }
    }

    private void recordingTeams(ArrayList<Selective> selectives){
        DatabaseHelper db = new DatabaseHelper(EnterSelectiveActivity.this);
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
        adapterSelectives = new AdapterRecyclerSelectives(EnterSelectiveActivity.this, arraySelectives, values);
        recyclerSelectives.setVisibility(View.VISIBLE);
        recyclerSelectives.setAdapter(adapterSelectives);
    }

    private void searchSelective(String search){
        DatabaseHelper db = new DatabaseHelper(EnterSelectiveActivity.this);
        db.openDataBase();
        ArrayList<Selective> selectiveList = db.searchSelective(search);
        if(selectiveList!= null && selectiveList.size()>0) {
            inflateSelective(selectiveList);
            recyclerSelectives.setVisibility(View.VISIBLE);
            linearSearchNull.setVisibility(View.GONE);
        }
        else{
            recyclerSelectives.setVisibility(View.GONE);
            linearSearchNull.setVisibility(View.VISIBLE);
        }
    }


    /******************** BUSCAS E ORDENAÇÃO ***********************/
    private View.OnTouchListener editTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return editTouchMotionEvent(event);
        }

    };

    private boolean editTouchMotionEvent(MotionEvent event){
        boolean ret = false;
        editSearch.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        imageOrder.setVisibility(View.GONE);
        editSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, R.drawable.close, 0);

        final int DRAWABLE_RIGHT = 2;

        try {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editSearch.getRight() - editSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    editSearch.setText("");
                    hideKeyboard();
                    imageOrder.setVisibility(View.VISIBLE);
                    editSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
                    editSearch.setCursorVisible(false);
                    editSearch.clearFocus();
                    ret = true;
                    callInflateSelectives();
                }
            }
            ret = false;
        } catch (Exception e) {
            Log.i("Clicked", e.getMessage());
        }
        return ret;
    }

    private void callInflateSelectives(){
        DatabaseHelper db = new DatabaseHelper(this);
        db.openDataBase();
        selectives = db.getSelectives();
        if(selectives!=null) {
            recyclerSelectives.setVisibility(View.VISIBLE);
            linearSearchNull.setVisibility(View.GONE);
            orderName();
            inflateSelective(selectives);
        }

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void orderName (){
        try {
            if (!(selectives == null || selectives.size() == 0)) {

                Collections.sort(selectives, new Comparator<Selective>() {
                    public int compare(Selective v1, Selective v2) {
                        return v1.getTitle().toLowerCase().compareTo(v2.getTitle().toLowerCase());
                    }
                });

                inflateSelective(selectives);
                //textOptionName.setTextColor(ContextCompat.getColor(EnterSelectiveActivity.this, R.color.colorPrimary));
                //textOptionCode.setTextColor(ContextCompat.getColor(EnterSelectiveActivity.this, R.color.black));
            }
        }catch (Exception e){}
    }

    private void orderTeam(){
        try {
            if(!(selectives == null || selectives.size()==0)) {
                Collections.sort(selectives, new Comparator<Selective>() {
                    public int compare(Selective v1, Selective v2) {
                        return v1.getTeam().toLowerCase().compareTo(v2.getTeam().toLowerCase());
                    }
                });

                inflateSelective(selectives);
                //textOptionCode.setTextColor(ContextCompat.getColor(AthletesActivity.this, R.color.colorPrimary));
                //textOptionName.setTextColor(ContextCompat.getColor(AthletesActivity.this, R.color.black));
            }
        }catch (Exception e){
            Log.i("Error ",e.getMessage());
        }
    }

    private void orderDate(){
        try {
            if(!(selectives == null || selectives.size()==0)) {
                Collections.sort(selectives, new Comparator<Selective>() {
                    public int compare(Selective v1, Selective v2) {
                        return v1.getDate().toLowerCase().compareTo(v2.getDate().toLowerCase());
                    }
                });

                inflateSelective(selectives);
                //textOptionCode.setTextColor(ContextCompat.getColor(AthletesActivity.this, R.color.colorPrimary));
                //textOptionName.setTextColor(ContextCompat.getColor(AthletesActivity.this, R.color.black));
            }
        }catch (Exception e){
            Log.i("Error ",e.getMessage());
        }
    }

    private View.OnClickListener clickedOrderTeam = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            clickInOrderChoose(CHOOSE_TEAM);
        }
    };
    private View.OnClickListener clickedOrderName = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            clickInOrderChoose(CHOOSE_NAME);
        }
    };
    private View.OnClickListener clickedOrderDate = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            clickInOrderChoose(CHOOSE_DATE);
        }
    };

    private void clickInOrderChoose(int choose){
        if(choose==CHOOSE_TEAM)
            orderTeam();
        else if(choose==CHOOSE_NAME)
            orderName();
        else if(choose == CHOOSE_DATE)
            orderDate();
        linearOrder.setVisibility(View.GONE);
    }

    private View.OnClickListener hideOptionsOrder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearOrder.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EnterSelectiveActivity.this.finish();
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    }

}

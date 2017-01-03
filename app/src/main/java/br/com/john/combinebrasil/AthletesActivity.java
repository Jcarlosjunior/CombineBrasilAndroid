package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.com.john.combinebrasil.AdapterList.AdapterListAthletes;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerAthletes;
import br.com.john.combinebrasil.AdapterList.AdapterRecyclerTests;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.DatabaseHelper;

public class AthletesActivity extends AppCompatActivity {
    public static RecyclerView listViewPlayers;
    public static AdapterRecyclerAthletes adapterTests;
    public LinearLayoutManager mLayoutManager;
    Toolbar toolbar;
    ArrayList<Athletes> athletesArrayList;
    private static Context myContext;
    private TextView textOptionName, textOptionCode;
    private EditText editSearch;
    private ImageView imgOrder;
    private Button btnCancel;
    private LinearLayout linearOrder, linearNotSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);

        listViewPlayers = (RecyclerView) findViewById(R.id.list_players);
        listViewPlayers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        linearOrder = (LinearLayout) findViewById(R.id.linear_order_by);
        linearNotSearch = (LinearLayout) findViewById(R.id.linear_search_null);

        editSearch = (EditText) findViewById(R.id.edit_search);
        textOptionName = (TextView) findViewById(R.id.text_option_order_name);
        textOptionCode = (TextView) findViewById(R.id.text_option_order_code);

        btnCancel= (Button) findViewById(R.id.btn_cancel_order);

        imgOrder = (ImageView) findViewById(R.id.img_order);

        imgOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearOrder.setVisibility(View.VISIBLE);
            }
        });
        linearOrder.setOnClickListener(hideOptionsOrder);

        myContext = AthletesActivity.this;

        hideKeyboard();

        textOptionCode.setOnClickListener(clickedOrderCode);
        textOptionName.setOnClickListener(clickedOrderName);
        btnCancel.setOnClickListener(hideOptionsOrder);

        editSearch.setOnTouchListener(editTouch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                    searchPlayer(s.toString());
                else
                    showList(athletesArrayList);
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

        callInflateAthletes();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        callInflateAthletes();
    }

    private void callInflateAthletes(){
        DatabaseHelper db = new DatabaseHelper(myContext);
        db.openDataBase();
        athletesArrayList = db.getAthletes();
        showList(athletesArrayList);
    }

    private void showList(ArrayList<Athletes> arrayAthletes){
        if(!(arrayAthletes == null || arrayAthletes.size()==0)){
            String[] values = new String[arrayAthletes.size()];
            for(int i=0; i <=arrayAthletes.size()-1; i++){
                values[i] = arrayAthletes.get(i).getId();
            }
            inflateRecyclerView(arrayAthletes, values);
        }
    }

    private void inflateRecyclerView(ArrayList<Athletes> athletesArrayList, String[] values){
        Collections.sort(athletesArrayList, new Comparator<Athletes>() {
            public int compare(Athletes v1, Athletes v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        adapterTests = new AdapterRecyclerAthletes(AthletesActivity.this, athletesArrayList, values);

        listViewPlayers.setVisibility(View.VISIBLE);
        listViewPlayers.setAdapter(adapterTests);
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    }

    public static void onClickItemList(Activity activity, int positionArray){
        ((AthletesActivity) activity).onClickItemList(positionArray);
    }

    public void onClickItemList(int position){
        Intent i;
        if(AllActivities.type.equals("corrida"))
            i = new Intent(AthletesActivity.this, CronometerActivity.class);
        else
            i = new Intent(AthletesActivity.this, ResultsActivity.class);
        Athletes player  = athletesArrayList.get(position);
        i.putExtra("id_player",player.getId());
        i.putExtra("position",position);
        startActivity(i);
    }

    /******************** BUSCAS E ORDENAÇÃO ***********************/
    private View.OnTouchListener editTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            editSearch.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            imgOrder.setVisibility(View.GONE);
            editSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, R.drawable.close, 0);

            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            try {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editSearch.getRight() - editSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        editSearch.setText("");
                        hideKeyboard();
                        imgOrder.setVisibility(View.VISIBLE);
                        editSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
                        editSearch.setCursorVisible(false);
                        editSearch.clearFocus();
                        ret = true;
                    }
                }
                ret = false;
            } catch (Exception e) {
                Log.i("Clicked", e.getMessage());
            }
            return ret;
        }

    };

    private View.OnClickListener clickedOrderCode = new View.OnClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
                orderCode();
            linearOrder.setVisibility(View.GONE);
        }
    };
    private View.OnClickListener clickedOrderName = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            orderName();
            linearOrder.setVisibility(View.GONE);
        }
    };
    private View.OnClickListener hideOptionsOrder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearOrder.setVisibility(View.GONE);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void orderName (){
        if(!(athletesArrayList == null || athletesArrayList.size()==0)) {

            Collections.sort(athletesArrayList, new Comparator<Athletes>() {
            public int compare(Athletes v1, Athletes v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });

            showList(athletesArrayList);
            textOptionName.setTextColor(getColor(R.color.primary));
            textOptionCode.setTextColor(getColor(R.color.black));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void orderCode(){
        try {
            if(!(athletesArrayList == null || athletesArrayList.size()==0)) {
                Collections.sort(athletesArrayList, new Comparator<Athletes>() {
                    public int compare(Athletes v1, Athletes v2) {
                        return v1.getCode().compareTo(v2.getCode());
                    }
                });

                showList(athletesArrayList);
                textOptionCode.setTextColor(getColor(R.color.primary));
                textOptionName.setTextColor(getColor(R.color.black));
            }
        }catch (Exception e){

        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchPlayer(String search){
        DatabaseHelper db = new DatabaseHelper(AthletesActivity.this);
        db.openDataBase();
        ArrayList<Athletes> athletesList = db.searchAthletes(search);
        if(athletesList!= null && athletesList.size()>0)
            showList(athletesList);
        else{
            listViewPlayers.setVisibility(View.GONE);
            linearNotSearch.setVisibility(View.VISIBLE);
        }
    }

}

package br.com.john.combinebrasil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

public class SelectiveDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    Bundle extras;
    String idSelective;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selective_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(btnBackClickListener);

        extras = getIntent().getExtras();
        if(extras != null) {
            idSelective = extras.getString("id_selective");
            showInfoSelective(idSelective);
        }

    }

    private void showInfoSelective(String id){
        DatabaseHelper db  = new DatabaseHelper(SelectiveDetailsActivity.this);
        db.openDataBase();
        Selective selective  = db.getSelectiveFromId(id);
        TextView textName = (TextView) findViewById(R.id.text_name_selective);
        TextView textDate = (TextView) findViewById(R.id.text_date_selective);
        TextView textDetails = (TextView) findViewById(R.id.text_details_selective);

        textName.setText(selective.getTitle());
        textDate.setText(Services.convertDate(selective.getDate()));

        textDetails.setText("Time: "+ selective.getTeam()+ "\n"+
                "Endereço: "+selective.getAddress() +"\n"+
                "Observações: "+selective.getNotes());
    }

    private View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SelectiveDetailsActivity.this.finish();
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    }
}

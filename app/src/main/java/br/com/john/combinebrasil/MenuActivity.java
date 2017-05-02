package br.com.john.combinebrasil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {
    private ImageView linearCreateSelective;
    private ImageView linearEnterSelective;
    private ImageView linearHistoricSelective;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        linearCreateSelective = (ImageView) findViewById(R.id.linear_create_selective);
        linearCreateSelective.setOnClickListener(clickCreateSelective);

        linearEnterSelective = (ImageView) findViewById(R.id.linear_enter_selective);
        linearEnterSelective.setOnClickListener(clickEnterSelective);

        linearHistoricSelective = (ImageView) findViewById(R.id.linear_historic_selective);
        linearHistoricSelective.setOnClickListener(clickHistoricSelective);
    }

    private View.OnClickListener clickCreateSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, CreateSelectiveActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener clickEnterSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, EnterSelectiveActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener clickHistoricSelective = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, HistoricSelectiveActivity.class);
            startActivity(intent);
        }
    };
}

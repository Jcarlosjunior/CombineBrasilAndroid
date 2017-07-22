package br.com.john.combinebrasil;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;

public class SelectiveCreatedSuccessActivity extends AppCompatActivity {
    String codeSelective;
    ImageView imageShowCreated;
    TextView textMessageCreatedSelective, textCode;
    ConstraintLayout constraintCreateSelective;
    Button btnExitShow;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selective_created_success);

        imageShowCreated = (ImageView) findViewById(R.id.img_created_selective);
        constraintCreateSelective = (ConstraintLayout) findViewById(R.id.constraint_created_selective);
        btnExitShow = (Button) findViewById(R.id.btn_exit_created_selective);
        textCode = (TextView) findViewById(R.id.text_code_selective);
        TextView myTextView=(TextView)findViewById(R.id.text_title_success);
        Typeface typeFace= Typeface.createFromAsset(getAssets(),"Freshman.ttf");
        myTextView.setTypeface(typeFace);
        textCode.setTypeface(typeFace);
        textCode.setOnClickListener(clickedSharedWhatsApp);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            textCode.setText(codeSelective = extras.getString("code"));
            id = extras.getString("id_selective");
            SharedPreferencesAdapter.setEnterSelectiveSharedPreferences(this, true);
            SharedPreferencesAdapter.setValueStringSharedPreferences(this, Constants.SELECTIVES_CODESELECTIVE, textCode.getText().toString());
            SharedPreferencesAdapter.setValueStringSharedPreferences(this, Constants.SELECTIVES_ID_ENTER, id);
        }
        clockwise(myTextView);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        constraintCreateSelective.startAnimation(anim);
        btnExitShow.setOnClickListener(clickedBackToMenu);
    }

    @Override
    public void onBackPressed(){
    }

    private View.OnClickListener clickedBackToMenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickToMain();
        }
    };

    private void clickToMain(){
        InfoSelectiveCreateActivity.finishOhterActivity();
        TestSelectiveActivity.finishOhterActivity();
        CreateSelectiveActivity.finishOhterActivity();
        LocalSelectiveActivity.finishActity();
        ChooseTeamSelectiveActivity.finishActity();
        MenuActivity.finishActity();

        Intent i = new Intent(SelectiveCreatedSuccessActivity.this, MainActivity.class);
        i.putExtra("id_selective",id);
        AllActivities.isSync = true;
        startActivity(i);
        finish();
    }

    private void clockwise(TextView text){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);
        text.startAnimation(animation);
    }

    private View.OnClickListener clickedSharedWhatsApp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharedWhatsApp();
        }
    };

    private void sharedWhatsApp(){
        Intent whatsappIntent = new Intent(android.content.Intent.ACTION_SEND);
        whatsappIntent.setType("image/*");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, textCode.getText().toString());
       // whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file)); //add image path
        //(Intent.createChooser(share, "Share image using"));
        try {
            this.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

}

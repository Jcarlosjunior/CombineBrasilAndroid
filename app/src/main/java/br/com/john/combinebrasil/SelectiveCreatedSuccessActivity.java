package br.com.john.combinebrasil;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectiveCreatedSuccessActivity extends AppCompatActivity {
    String codeSelective;
    ImageView imageShowCreated;
    TextView textMessageCreatedSelective, textCode;
    ConstraintLayout constraintCreateSelective;
    Button btnExitShow;
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

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            textCode.setText(codeSelective = extras.getString("code"));
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
            InfoSelectiveCreateActivity.finishOhterActivity();
            TestSelectiveActivity.finishOhterActivity();
            CreateSelectiveActivity.finishOhterActivity();
            LocalSelectiveActivity.finishActity();
            ChooseTeamSelectiveActivity.finishActity();
            finish();
        }
    };

    private void clockwise(TextView text){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);
        text.startAnimation(animation);
    }

}

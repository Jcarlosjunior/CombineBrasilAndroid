package br.com.john.combinebrasil;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent;
                if(SharedPreferencesAdapter.getLoggedSharedPreferences(SplashScreenActivity.this))
                   mainIntent = new Intent(SplashScreenActivity.this, MenuActivity.class);
                else
                    mainIntent = new Intent(SplashScreenActivity.this, IntroActivity.class);
                startActivity(mainIntent);
                finish();

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

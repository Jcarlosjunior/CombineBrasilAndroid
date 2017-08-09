package br.com.john.combinebrasil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.SharedPreferencesAdapter;

public class IntroActivity extends AppIntro {
    int num=0;
    int[] color;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllActivities.introActivity = IntroActivity.this;
        //setDepthAnimation();
        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        ////addSlide(secondFragment);
       // addSlide(thirdFragment);
       // addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Testes protocolados", "Você tem na sua mão, testes de alto padrão que conseguirão avaliar o crescimento e qualificação dos atletas", R.drawable.tests_protocol, Color.parseColor("#3E50B4")));
        addSlide(AppIntroFragment.newInstance("Verifique os dados de atletas", "Não perca tempo, apenas verifique no app",  R.drawable.icon_person, Color.parseColor("#FF5722")));
        addSlide(AppIntroFragment.newInstance("Ache fácilmente o atleta na fila", "Encontre rapidammente os atelteas que irão realizar sua seletiva", R.drawable.icon_search_name_code, Color.parseColor("#673AB7")));
        addSlide(AppIntroFragment.newInstance("Avalie a perfomance", "Além do resultado do teste para o atleta, avalie a perfomance de como ele executou o teste", R.drawable.icon_qualification, Color.parseColor("#00BCD4")));
        addSlide(AppIntroFragment.newInstance("Dados armazenados na núvem", "Salve com seguranças os dados dos testes com conexão de internet", R.drawable.icon_sync_all, Color.parseColor("#F44336")));
        addSlide(AppIntroFragment.newInstance("Relatórios prontos", "Sua seletivas se tornam relatórios na palma da sua mão", R.drawable.icon_report, Color.parseColor("#4CAF50")));
        num=5;
        color = new int[6];
        color[0] = Color.parseColor("#3E50B4");
        color[1] = Color.parseColor("#FF5722");
        color[2] = Color.parseColor("#673AB7");
        color[3] = Color.parseColor("#00BCD4");
        color[4] = Color.parseColor("#F44336");
        color[5] = Color.parseColor("#4CAF50");

        setColorTransitionsEnabled(true);
        showSkipButton(true);
        setDoneText("Pronto");
        if(SharedPreferencesAdapter.getLoggedSharedPreferences(IntroActivity.this))
            setSkipText("Finalizar");
        else
            setSkipText("Fazer Login");
        setProgressButtonEnabled(true);
        setImageNextButton(this.getDrawable(R.drawable.ic_navigate_next_white));

        setVibrate(true);
        setVibrateIntensity(30);
    }
    private void loadMainActivity(){
        Intent intent = null;
        if(SharedPreferencesAdapter.getLoggedSharedPreferences(IntroActivity.this))
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v){
        loadMainActivity();
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    @Override
    protected void onPageSelected(int position) {
        super.onPageSelected(position);
        changeColor(color[position]);
    }

    private void changeColor(int color){
        setBarColor(color);
        setSeparatorColor(color);
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


    public class ParallaxPageTransformer implements ViewPager.PageTransformer {

        public void transformPage(View view, float position) {

            int pageWidth = view.getWidth();


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(1);

            } else if (position <= 1) { // [-1,1]

                view.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(1);
            }


        }
    }
}

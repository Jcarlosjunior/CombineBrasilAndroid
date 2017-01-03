package br.com.john.combinebrasil.Services;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.SyncActivity;

/**
 * Created by GTAC on 05/12/2016.
 */

public class NavigationDrawer {
    Bundle savedInstanceState;
    Toolbar mToolbar;
    public static Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    private static boolean userIsAdmin;


    public NavigationDrawer(Bundle savedInstanceState, Toolbar mToolbar, boolean userIsAdmin){
        this.savedInstanceState = savedInstanceState;
        this.mToolbar = mToolbar;
        this.userIsAdmin = userIsAdmin;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createNavigationAccess(){
        navigationDrawerLeft = new Drawer()
                .withActivity(AllActivities.mainActivity)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(-1)
                .withActionBarDrawerToggleAnimated(false)
                .withActionBarDrawerToggle(false)
                .withSliderBackgroundDrawable(AllActivities.mainActivity.getResources().getDrawable(R.color.white))
                .build();

        navigationDrawerLeft.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                getClickInNavigation(i);
            }
        });
        addItensNavigationAccess();
    }


    private void addItensNavigationAccess(){
        if(this.userIsAdmin) {
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(AllActivities.mainActivity.getResources().getString(R.string.create))
                    .withTextColor(AllActivities.mainActivity.getResources().getColor(R.color.colorPrimary))
                    .withSelectedColor(AllActivities.mainActivity.getResources().getColor(R.color.white))
                    .withSelectedTextColor(AllActivities.mainActivity.getResources().getColor(R.color.black)));
        }
        else {
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(AllActivities.mainActivity.getResources().getString(R.string.create))
                    .withTextColor(AllActivities.mainActivity.getResources().getColor(R.color.greyDark))
                    .withSelectedColor(AllActivities.mainActivity.getResources().getColor(R.color.white))
                    .withSelectedTextColor(AllActivities.mainActivity.getResources().getColor(R.color.greyDark)));
        }
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(AllActivities.mainActivity.getResources().getString(R.string.tests_athletes))
                .withTextColor(AllActivities.mainActivity.getResources().getColor(R.color.colorPrimary))
                .withSelectedColor(AllActivities.mainActivity.getResources().getColor(R.color.white))
                .withSelectedTextColor(AllActivities.mainActivity.getResources().getColor(R.color.black)));

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(AllActivities.mainActivity.getResources().getString(R.string.sync))
                .withTextColor(AllActivities.mainActivity.getResources().getColor(R.color.colorPrimary))
                .withSelectedColor(AllActivities.mainActivity.getResources().getColor(R.color.white))
                .withSelectedTextColor(AllActivities.mainActivity.getResources().getColor(R.color.black)));

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(AllActivities.mainActivity.getResources().getString(R.string.about))
                .withTextColor(AllActivities.mainActivity.getResources().getColor(R.color.colorPrimary))
                .withSelectedColor(AllActivities.mainActivity.getResources().getColor(R.color.white))
                .withSelectedTextColor(AllActivities.mainActivity.getResources().getColor(R.color.black)));

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(AllActivities.mainActivity.getResources().getString(R.string.exit))
                .withTextColor(AllActivities.mainActivity.getResources().getColor(R.color.colorPrimary))
                .withSelectedColor(AllActivities.mainActivity.getResources().getColor(R.color.white))
                .withSelectedTextColor(AllActivities.mainActivity.getResources().getColor(R.color.black)));
    }
    private void getClickInNavigation(int position){
        Intent intent;
        switch (position){
            case 0://Cadastrar Atletas
                if(this.userIsAdmin) {
                    intent =  new Intent(AllActivities.mainActivity, CreateAccountAthlete.class);
                    AllActivities.mainActivity.startActivity(intent);
                }

                break;
            case 1: // Atletas e Testes


                break;
            case 2: //Sincronizar
                intent =  new Intent(AllActivities.mainActivity, SyncActivity.class);
                AllActivities.mainActivity.startActivity(intent);
                break;

            case 3:

                break;
            //Sobre o Nostramamma
            case 4:
                break;
            //Facebook
            case 5:

                break;
            default:
        }
    }

    public void changeActivity(){
        Intent i = new Intent(AllActivities.mainActivity, CreateAccountAthlete.class);
        AllActivities.mainActivity.startActivity(i);
        AllActivities.mainActivity.finish();
    }

    private int getCorretcDrawerIcon(int position, boolean isSelecetd){
        switch(position){
            case 0:
                //return( isSelecetd ? R.drawable.cesta_media : R.drawable.cesta_media );
            case 1:
               // return( isSelecetd ? R.drawable.cesta_media : R.drawable.cesta_media );
            case 2:
              //  return( isSelecetd ? R.drawable.cesta_media : R.drawable.cesta_media );
            case 3:
               // return( isSelecetd ? R.drawable.cesta_media : R.drawable.cesta_media );
        }
        return(0);
    }
}
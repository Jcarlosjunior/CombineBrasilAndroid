package br.com.john.combinebrasil.Services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.CreateAccountAthleteActivity;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.MenuActivity;
import br.com.john.combinebrasil.MenuHistoricSelectiveActivity;
import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 19/07/2017.
 */


public class NavigationTestsDrawer {
    Activity mActivity;
    Bundle savedInstanceState;
    Toolbar mToolbar;
    private User user;
    private boolean isAdmin;

    public NavigationTestsDrawer(Bundle savedInstanceState, Activity mActivity, Toolbar mToolbar, User user, boolean isAdmin){
        this.savedInstanceState = savedInstanceState;
        this.mActivity = mActivity;
        this.mToolbar = mToolbar;
        this.user = user;
        this.isAdmin = isAdmin;
    }

    public void createNavigationAccess() {
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.home).withIcon(FontAwesome.Icon.faw_home);
        SecondaryDrawerItem itemPerfil = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.meu_perfil).withIcon(FontAwesome.Icon.faw_user);
        SecondaryDrawerItem itemInfoSeletive = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.info_seletiva).withIcon(FontAwesome.Icon.faw_cogs);
        SecondaryDrawerItem itemCadastrarAtleta = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.cadastrar_atleta).withIcon(FontAwesome.Icon.faw_user_plus);
        SecondaryDrawerItem itemHelp = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.help).withIcon(FontAwesome.Icon.faw_question_circle);
        SecondaryDrawerItem itemExitSelective = new SecondaryDrawerItem().withIdentifier(6).withName(R.string.exit_selective).withIcon(FontAwesome.Icon.faw_question_circle);

        PrimaryDrawerItem itemExit = new PrimaryDrawerItem().withName(R.string.exit).withIdentifier(7)
                .withTextColorRes(R.color.color_primary).withIcon(FontAwesome.Icon.faw_sign_out)
                .withIconColorRes(R.color.color_primary).withSelectedTextColorRes(R.color.black)
                .withSelectedIconColorRes(R.color.black).withSelectedBackgroundAnimated(true);


        String profile_pic = SharedPreferencesAdapter.getValueStringSharedPreferences(mActivity, "profile_pic");
        ImageView view = new ImageView(mActivity);
        view.setDrawingCacheEnabled(true);
        Uri uri = Uri.parse("https://cdn2.iconfinder.com/data/icons/ios-7-icons/50/user_male2-512.png");

        Glide.with(mActivity)
                .load(profile_pic.equals("")?uri:profile_pic)
                .thumbnail(0.5f)
                .into(view);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withHeaderBackground(R.drawable.item_menu_criar_avaliacao)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(user!=null?user.getName():"")
                                .withEmail(user!=null?user.getEmail():"")
                                .withIcon(Uri.parse(profile_pic))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        return false;
                    }
                })
                .build();

        view = headerResult.getHeaderBackgroundView();
        Glide.with(mActivity).load("http://www.culturamix.com/wp-content/gallery/bola-de-futebol-americano-2/Bola-de-Futebol-Americano-3.jpg").into(view);
        Drawer result = new DrawerBuilder()
                .withActivity(mActivity)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        itemHome,
                        itemPerfil,
                        itemInfoSeletive,
                        itemCadastrarAtleta,
                        itemHelp,
                        itemExitSelective
                )
                .withStickyFooterDivider(true)
                .addStickyDrawerItems(itemExit)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        clickItemMenu(position, drawerItem);
                        return false;
                    }
                })
                .build();

        result.openDrawer();
        result.closeDrawer();
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        result.getDrawerLayout();
    }

    private void clickItemMenu(int position, IDrawerItem drawerItem){
        Intent intent;
        switch (position) {
            case 1://Home
                intent = new Intent(mActivity, MainActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case 2://Meu Perfil
               // intent = new Intent(mActivity, PerfilUserActivity.class);
             //   mActivity.startActivity(intent);
                break;
            case 3://Informação Seletiva
                intent = new Intent(mActivity, MenuHistoricSelectiveActivity.class);
                DatabaseHelper db = new DatabaseHelper(mActivity);
                MenuHistoricSelectiveActivity.SELECTIVE_CLICKED =  db.getSelective();
                mActivity.startActivity(intent);
                break;
            case 4://Cadastrar Usuário
                intent = new Intent(mActivity, CreateAccountAthleteActivity.class);
                mActivity.startActivity(intent);
                break;
            case 5://Ajuda
            //    intent = new Intent(mActivity, HelpActivity.class);
            //    mActivity.startActivity(intent);
            //    mActivity.finish();
                break;
            case 6://Sair da Seletiva
                new MessageOptions(mActivity, "Fechar Seletiva", "Deseja realmente sair da seletiva?", "exit_selective");
                break;
            case -1://Sair
                new MessageOptions(mActivity, "Logout", "Ao sair todos os dados serão excluídos do aplicativo, deseja realmente sair?", "exit");
                break;

        }
    }
}


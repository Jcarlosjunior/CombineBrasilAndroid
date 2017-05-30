package br.com.john.combinebrasil.Services;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.MenuActivity;
import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 05/12/2016.
 */

public class NavigationDrawer {
    Activity mActivity;
    Bundle savedInstanceState;
    Toolbar mToolbar;
    private static boolean userIsAdmin;

    public NavigationDrawer(Bundle savedInstanceState, Activity mActivity, Toolbar mToolbar, boolean userIsAdmin){
        this.savedInstanceState = savedInstanceState;
        this.mActivity = mActivity;
        this.mToolbar = mToolbar;
        this.userIsAdmin = userIsAdmin;
    }

    public void createNavigationAccess() {
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(0).withName(R.string.home).withIcon(FontAwesome.Icon.faw_home);
        SecondaryDrawerItem itemPerfil = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.meu_perfil).withIcon(FontAwesome.Icon.faw_user);
        SecondaryDrawerItem itemAddRegister = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.cadastrar_atleta).withIcon(FontAwesome.Icon.faw_user_plus);
        SecondaryDrawerItem itemUpdate = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.update).withIcon(FontAwesome.Icon.faw_refresh);
        SecondaryDrawerItem itemHelp = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.help).withIcon(FontAwesome.Icon.faw_question_circle);

        PrimaryDrawerItem itemExit = new PrimaryDrawerItem().withName(R.string.exit).withIdentifier(5)
                .withTextColorRes(R.color.color_primary).withIcon(FontAwesome.Icon.faw_sign_out)
                .withIconColorRes(R.color.color_primary).withSelectedTextColorRes(R.color.black)
                .withSelectedIconColorRes(R.color.black).withSelectedBackgroundAnimated(true);
        
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withHeaderBackground(R.drawable.item_menu_criar_avaliacao)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://maxcdn.icons8.com/Share/icon/ios7/Cinema//anonymous_mask1600.png")
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        return false;
                    }
                })
                .build();
        ImageView view = headerResult.getHeaderBackgroundView();
        Glide.with(mActivity).load("https://www.tnwinc.com/wp-content/uploads/2013/11/bullying-and-harassment-at-work-dolphins-700x300.jpg").into(view);
        Drawer result = new DrawerBuilder()
                .withActivity(mActivity)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        itemHome,
                        itemPerfil,
                        itemAddRegister,
                        itemUpdate,
                        itemHelp
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
            case 0://Home
                if (this.userIsAdmin) {
                    intent = new Intent(mActivity, MenuActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
                break;
            case 1://Meu Perfil
                if (this.userIsAdmin) {
                    intent = new Intent(mActivity, MenuActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
                break;
            case 2://Cadastrar Atleta
                if (this.userIsAdmin) {
                    intent = new Intent(mActivity, CreateAccountAthlete.class);
                    mActivity.startActivity(intent);
                }
                break;
            case 3://Atualizar
                if (this.userIsAdmin) {
                    intent = new Intent(mActivity, MenuActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
                break;
            case 4://Ajuda
                if (this.userIsAdmin) {
                    intent = new Intent(mActivity, MenuActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
                break;
            case -1://Ajuda
                new MessageOptions(mActivity, "Logout", "Ao sair todos os dados serão excluídos do aplicativo, deseja realmente sair?", "exit");
            break;
        }
    }

    private void loadImage(ImageView view){
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            /*
            @Override
            public Drawable placeholder(Context ctx) {
            return super.placeholder(ctx);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
            return super.placeholder(ctx, tag);
            }
            */
        });
    }
}


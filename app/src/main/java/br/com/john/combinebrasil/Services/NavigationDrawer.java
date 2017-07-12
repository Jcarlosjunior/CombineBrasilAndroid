package br.com.john.combinebrasil.Services;


import android.app.Activity;
import android.content.Context;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
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
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.squareup.picasso.Picasso;

import java.net.URI;

import br.com.john.combinebrasil.Classes.User;
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
    private User user;

    public NavigationDrawer(Bundle savedInstanceState, Activity mActivity, Toolbar mToolbar, boolean userIsAdmin, User user){
        this.savedInstanceState = savedInstanceState;
        this.mActivity = mActivity;
        this.mToolbar = mToolbar;
        this.userIsAdmin = userIsAdmin;
        this.user = user;
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


        String profile_pic = SharedPreferencesAdapter.getValueStringSharedPreferences(mActivity, "profile_pic");
        ImageView view = new ImageView(mActivity);
        view.setDrawingCacheEnabled(true);
        Uri uri = Uri.parse("https://cdn2.iconfinder.com/data/icons/ios-7-icons/50/user_male2-512.png");

        Glide.with(mActivity)
                .load(profile_pic.equals("")?uri:profile_pic)
                .thumbnail(0.5f)
                .into(view);

        Bitmap bitmapIcon = view.getDrawingCache();

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

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }
}


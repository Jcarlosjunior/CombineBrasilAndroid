package br.com.john.combinebrasil.Services;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by GTAC on 19/11/2016.
 */

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
    public String tabIcons[];

    public AppSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private String tabTitles[];

    private Fragment fragments[];


    public String[] getTabIcons() {
        return tabIcons;
    }

    public void setTabIcons(String[] tabIcons) {
        this.tabIcons = tabIcons;
    }

    public String[] getTabTitles() {
        return tabTitles;
    }

    public Fragment[] getFragments() {
        return fragments;
    }

    public void setFragments(Fragment[] fragments) {
        this.fragments = fragments;
    }




    public void setTabTitles(String[] strings) {
        tabTitles = strings;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabIcons[position];
    }

    /*@Override
    public int getPageIconResId(int i) {
        return tabIcons[i];
    }*/

}
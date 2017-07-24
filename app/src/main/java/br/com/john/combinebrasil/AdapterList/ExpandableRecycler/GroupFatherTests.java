package br.com.john.combinebrasil.AdapterList.ExpandableRecycler;

import android.annotation.SuppressLint;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import br.com.john.combinebrasil.Classes.Tests;

/**
 * Created by GTAC on 24/07/2017.
 */
@SuppressLint("ParcelCreator")
public class GroupFatherTests extends ExpandableGroup<ChildItemTests> {

    public GroupFatherTests(String title, List<ChildItemTests> items) {
        super(title, items);
    }
}
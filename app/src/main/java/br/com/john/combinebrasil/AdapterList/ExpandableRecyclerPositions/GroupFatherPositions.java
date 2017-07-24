package br.com.john.combinebrasil.AdapterList.ExpandableRecyclerPositions;

import android.annotation.SuppressLint;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by GTAC on 24/07/2017.
 */
@SuppressLint("ParcelCreator")
public class GroupFatherPositions extends ExpandableGroup<ChildItemPositions> {

    public GroupFatherPositions(String title, List<ChildItemPositions> items) {
        super(title, items);
    }
}
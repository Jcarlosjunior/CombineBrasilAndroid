package br.com.john.combinebrasil.AdapterList.ExpandableRecyclerPositions;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 24/07/2017.
 */

public class ExpandableRecyclerViewAdapterPositions extends ExpandableRecyclerViewAdapter<GroupFatherViewHolder, ChildItemViewHolder> {

    private Activity activity;

    public ExpandableRecyclerViewAdapterPositions(Activity activity, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.activity = activity;
    }

    @Override
    public GroupFatherViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_group_view_holder, parent, false);

        return new GroupFatherViewHolder(view, activity);
    }

    @Override
    public ChildItemViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_position_selective, parent, false);

        return new ChildItemViewHolder(view, activity);
    }

    @Override
    public void onBindChildViewHolder(ChildItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ChildItemPositions phone = ((GroupFatherPositions) group).getItems().get(childIndex);
        holder.onBind(phone,group);
    }

    @Override
    public void onBindGroupViewHolder(GroupFatherViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGroupName(group);
    }
}
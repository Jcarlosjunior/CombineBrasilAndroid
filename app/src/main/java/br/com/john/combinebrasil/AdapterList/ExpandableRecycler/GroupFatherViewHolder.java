package br.com.john.combinebrasil.AdapterList.ExpandableRecycler;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 24/07/2017.
 */

public class GroupFatherViewHolder extends GroupViewHolder {

    private TextView txtTitleName;

    public GroupFatherViewHolder(View itemView) {
        super(itemView);

        txtTitleName = (TextView) itemView.findViewById(R.id.txt_title_group);
    }

    @Override
    public void expand() {
        txtTitleName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
        Log.i("Adapter", "expand");
    }

    @Override
    public void collapse() {
        Log.i("Adapter", "collapse");
        txtTitleName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
    }

    public void setGroupName(ExpandableGroup group) {
        txtTitleName.setText(group.getTitle());
    }
}
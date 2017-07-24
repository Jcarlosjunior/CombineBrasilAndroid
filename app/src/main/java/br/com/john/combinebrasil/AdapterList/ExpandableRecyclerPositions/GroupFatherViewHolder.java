package br.com.john.combinebrasil.AdapterList.ExpandableRecyclerPositions;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
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
    private ConstraintLayout constraintBg;
    Activity act;

    public GroupFatherViewHolder(View itemView, Activity act) {
        super(itemView);

        this.act = act;
        txtTitleName = (TextView) itemView.findViewById(R.id.txt_title_group);
        constraintBg = (ConstraintLayout) itemView.findViewById(R.id.constraint_group_view_holder);
    }

    @Override
    public void expand() {
        txtTitleName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
        constraintBg.setBackground(act.getResources().getDrawable(R.drawable.background_border_line));
        Log.i("Adapter", "expand");
    }

    @Override
    public void collapse() {
        Log.i("Adapter", "collapse");
        txtTitleName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
        constraintBg.setBackground(act.getResources().getDrawable(R.drawable.background_border));
    }

    public void setGroupName(ExpandableGroup group) {
        txtTitleName.setText(group.getTitle());
    }
}
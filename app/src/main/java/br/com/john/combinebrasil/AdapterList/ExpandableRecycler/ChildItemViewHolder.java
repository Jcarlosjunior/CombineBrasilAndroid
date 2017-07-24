package br.com.john.combinebrasil.AdapterList.ExpandableRecycler;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 24/07/2017.
 */

public class ChildItemViewHolder  extends ChildViewHolder {

    private TextView txtTitleName;
    private TextView txtResult;
    private ImageView imageIcon;
    private Activity Act;

    public ChildItemViewHolder(View itemView, Activity act) {
        super(itemView);
        imageIcon = (ImageView) itemView.findViewById(R.id.icon);
        txtTitleName = (TextView) itemView.findViewById(R.id.text_name_test);
        txtResult = (TextView) itemView.findViewById(R.id.text_result_test);
        Act = act;
    }

    public void onBind(ChildItemTests test, ExpandableGroup group) {
        txtTitleName.setText(test.getAthlete());
        txtResult.setText(String.valueOf(test.getFirstValue()));

        Picasso.with(Act)
                .load("http://cdn.bleacherreport.net/images/team_logos/328x328/nfl.png")
                .into(imageIcon);
    }
}
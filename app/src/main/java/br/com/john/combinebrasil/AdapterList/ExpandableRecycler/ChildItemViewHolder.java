package br.com.john.combinebrasil.AdapterList.ExpandableRecycler;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.Services;

/**
 * Created by GTAC on 24/07/2017.
 */

public class ChildItemViewHolder  extends ChildViewHolder {

    private TextView txtTitleName;
    private TextView txtResult;
    private TextView txtPosition;
    private ImageView imageIcon;
    private Activity Act;

    public ChildItemViewHolder(View itemView, Activity act) {
        super(itemView);
        imageIcon = (ImageView) itemView.findViewById(R.id.icon);
        txtTitleName = (TextView) itemView.findViewById(R.id.text_name_test);
        txtResult = (TextView) itemView.findViewById(R.id.text_result_test);
        txtPosition = (TextView) itemView.findViewById(R.id.text_positions_test);
        Act = act;
    }

    public void onBind(ChildItemTests test, ExpandableGroup group) {
        txtTitleName.setText(test.getNameAthlete());
        txtPosition.setText(test.getPositions());

        Picasso.with(Act)
                .load(test.getImageURL())
                .into(imageIcon);

        if(test.getValueType().equals("tempo"))
            txtResult.setText(Services.convertInTime(test.getFirstResult()));
        else if(test.getValueType().equals("medida em metros")||test.getValueType().equals("metros"))
            txtResult.setText(Services.convertCentimetersinMeters(test.getFirstResult()));
        else
            txtResult.setText(String.valueOf(test.getFirstResult()));
    }
}
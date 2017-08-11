package br.com.john.combinebrasil.AdapterList.ExpandableRecyclerPositions;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 24/07/2017.
 */

public class ChildItemViewHolder  extends ChildViewHolder {
    private TextView txtTitleName;
    private TextView txtOtherPositions;
    private TextView txtPosition;
    private Activity Act;

    public ChildItemViewHolder(View itemView, Activity act) {
        super(itemView);
        txtPosition = (TextView) itemView.findViewById(R.id.text_position);
        txtTitleName = (TextView) itemView.findViewById(R.id.text_name_test);
        txtOtherPositions = (TextView) itemView.findViewById(R.id.text_other_positiions);
        Act = act;
    }

    public void onBind(ChildItemPositions position, ExpandableGroup group) {
        txtTitleName.setText(position.getName());
        txtOtherPositions.setText(String.valueOf(position.getPosition()));
        txtPosition.setText(position.getDescription());
    }
}
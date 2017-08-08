package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 05/08/17.
 */

public class AdapterRecyclerPositions extends RecyclerView.Adapter<AdapterRecyclerPositions.ViewHolder> {
    private String[] values;
    public static String[] valuesID;
    public static ArrayList<Positions> list;
    private Activity act;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerPositions(Activity act, ArrayList<Positions> list, String[] values) {
        super();
        this.list = list;
        this.values = values;
        this.act = act;
        valuesID = new String[list.size()];
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtName;
        public TextView txtStatus;
        private ConstraintLayout constraintItem;

        public ViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.text_name_position);
            constraintItem = (ConstraintLayout) v.findViewById(R.id.constraint_list);
            txtStatus = (TextView) v.findViewById(R.id.text_status);
        }
    }

    public void add(int position, String item) {
        notifyItemInserted(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerPositions.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_positions, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(list.get(holder.getPosition()).getNAME());

        holder.txtStatus.setText(position+1 +"ª Posição indicada");
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}

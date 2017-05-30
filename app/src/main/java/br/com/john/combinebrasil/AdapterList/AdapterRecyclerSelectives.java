package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.EnterSelectiveActivity;
import br.com.john.combinebrasil.HistoricSelectiveActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

/**
 * Created by GTAC on 18/04/2017.
 */

public class AdapterRecyclerSelectives extends RecyclerView.Adapter<AdapterRecyclerSelectives.ViewHolder> {
    private String[] values;
    private ArrayList<Selective> list;
    private Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerSelectives(Activity act, ArrayList<Selective> list, String[] values) {
        super();
        this.list = list;
        this.values = values;
        this.activity = act;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ConstraintLayout linearBackground;
        TextView textNameSelective;
        TextView textTeamName;
        TextView textDate;
        ImageView imgView;
        public ViewHolder(View v) {
            super(v);
            linearBackground = (ConstraintLayout) v.findViewById(R.id.constraint_background_list);
            textNameSelective = (TextView) v.findViewById(R.id.text_name_selective_list);
            textTeamName = (TextView) v.findViewById(R.id.text_team_name_list);
            textDate = (TextView) v.findViewById(R.id.text_date_list);
            imgView = (ImageView) v.findViewById(R.id.image_list);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerSelectives.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_selective, parent, false);
        // set the view's size, margins, paddings and layout parameters
        AdapterRecyclerSelectives.ViewHolder vh = new AdapterRecyclerSelectives.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(AdapterRecyclerSelectives.ViewHolder holder, final int position) {
        holder.linearBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });
        holder.textNameSelective.setText(list.get(position).getTitle());
        holder.textTeamName.setText(list.get(position).getTeam());
        holder.textDate.setText(Services.convertDate(list.get(position).getDate()));

        DatabaseHelper db = new DatabaseHelper(activity);

        holder.textTeamName.setText(db.getNameTeamByIdTeam(list.get(position).getTeam()));

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    private void click(int position){
        if(activity.getClass().getSimpleName().equals("EnterSelectiveActivity"))
            EnterSelectiveActivity.onClickItemList(activity, position);
        else if (activity.getClass().getSimpleName().equals("HistoricSelectiveActivity"))
            HistoricSelectiveActivity.onClickSelective(activity, position);
    }

    //o metodo abaixo serve para remover um elemento da lista ao clicar no mesmo
    /*public void add(int position, String item) {
        //notifyItemInserted(position);
    }

    public void remove(String item) {
        //int position = myTitle.indexOf(item);
        //myTitle.remove(position);
        //notifyItemRemoved(position);
    }*/


}

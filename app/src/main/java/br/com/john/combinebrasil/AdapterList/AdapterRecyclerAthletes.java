package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;

/**
 * Created by GTAC on 02/01/2017.
 */


public class AdapterRecyclerAthletes extends RecyclerView.Adapter<AdapterRecyclerAthletes.ViewHolder> {
    private String[] values;
    private ArrayList<Athletes> list;
    private Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerAthletes(Activity act, ArrayList<Athletes> list, String[] values) {
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
        TextView textNamePlayer;
        TextView textFirstResult;
        TextView textSecondResult;
        TextView textCode;
        ImageView imgAthlete;
        public ViewHolder(View v) {
            super(v);
            linearBackground = (ConstraintLayout) v.findViewById(R.id.linear_list_players);
            textNamePlayer = (TextView) v.findViewById(R.id.text_name_player_list);
            textFirstResult = (TextView) v.findViewById(R.id.text_first_result_list);
            textSecondResult = (TextView) v.findViewById(R.id.text_second_result_list);
            textCode = (TextView) v.findViewById(R.id.text_code_list);
            imgAthlete = (ImageView) v.findViewById(R.id.image_athlete);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerAthletes.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_players, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.linearBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });
        holder.textNamePlayer.setText(list.get(position).getName());
        holder.textFirstResult.setText("");
        holder.textSecondResult.setText("");
        holder.textSecondResult.setVisibility(View.GONE);
        holder.textCode.setText(list.get(position).getCode());
        Picasso.with(activity).load(list.get(position).getURLImage()).into(holder.imgAthlete);

        DatabaseHelper db = new DatabaseHelper(activity);
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(list.get(position).getId(), AllActivities.testSelected);

        if(test!=null) {
            TestTypes type = db.getTestTypeFromId(test.getType());

            if (type.getValueType().toLowerCase().equals("corrida") || type.getValueType().toLowerCase().equals("tempo")){
                holder.textFirstResult.setText(Services.convertInTime(test.getFirstValue()));
                holder.textSecondResult.setText(Services.convertInTime(test.getSecondValue()));
            }
            else if(type.getValueType().toLowerCase().equals("repeticao")|| type.getValueType().toLowerCase().equals("repeticao por tempo"))
            {
                holder.textFirstResult.setText(String.valueOf(test.getFirstValue()));
                holder.textSecondResult.setVisibility(View.GONE);
            }
            else{
                holder.textFirstResult.setText(Services.convertCentimetersinMeters(test.getFirstValue()));
                holder.textSecondResult.setText(Services.convertCentimetersinMeters(test.getSecondValue()));
            }
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    private void click(int position){
            AthletesActivity.onClickItemList(activity, position);
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
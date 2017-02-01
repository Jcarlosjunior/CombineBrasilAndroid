package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        LinearLayout linearBackground;
        LinearLayout linearStatus;
        TextView textNamePlayer;
        TextView textFirstResult;
        TextView textSecondResult;
        TextView textCode;
        ImageView imgStatus;
        TextView textStatus;
        public ViewHolder(View v) {
            super(v);
            linearBackground = (LinearLayout) v.findViewById(R.id.linear_list_players);
            linearStatus = (LinearLayout) v.findViewById(R.id.linear_img_status);
           textNamePlayer = (TextView) v.findViewById(R.id.text_name_player_list);
            textFirstResult = (TextView) v.findViewById(R.id.text_first_result_list);
            textSecondResult = (TextView) v.findViewById(R.id.text_second_result_list);
            textCode = (TextView) v.findViewById(R.id.text_code_list);
            imgStatus = (ImageView) v.findViewById(R.id.img_status_players);
            textStatus = (TextView) v.findViewById(R.id.text_status);
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
        holder.textStatus.setVisibility(View.GONE);

        DatabaseHelper db = new DatabaseHelper(activity);
        db.openDataBase();
        Tests test = db.getTestFromAthleteAndType(list.get(position).getId(), AllActivities.testSelected);

        if(test!=null) {
            TestTypes type = db.getTestTypeFromId(test.getType());
            if(test.getCanSync()) {
                holder.linearStatus.setVisibility(View.VISIBLE);
                holder.textStatus.setVisibility(View.VISIBLE);
                if(Services.convertIntInBool(test.getSync()))
                    holder.textStatus.setText("Exercício já sincronizado");
                else
                    holder.textStatus.setText("Exercício pronto pra ser sincronizado");
            }
            else {
                holder.linearStatus.setVisibility(View.GONE);
            }

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
        else
            holder.linearStatus.setVisibility(View.GONE);
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
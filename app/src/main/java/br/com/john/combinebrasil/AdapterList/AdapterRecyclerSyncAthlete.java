package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
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

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.SyncAthleteActivity;

/**
 * Created by GTAC on 07/01/2017.
 */

public class AdapterRecyclerSyncAthlete  extends RecyclerView.Adapter<AdapterRecyclerSyncAthlete.ViewHolder> {
    private String[] values;
    private ArrayList<Athletes> list;
    private Activity activity;
    private String idTest;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerSyncAthlete(Activity act, ArrayList<Athletes> list, String[] values, String idTest) {
        super();
        this.list = list;
        this.values = values;
        this.activity = act;
        this.idTest = idTest;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LinearLayout linearBackground;
        TextView txtName;
        TextView txtCode;
        TextView txtFirstResult;
        TextView txtSecondResult;
        ImageView imgStatus;
        ImageView imgSync;

        public ViewHolder(View v) {
            super(v);
            linearBackground = (LinearLayout) v.findViewById(R.id.linear_sync_athlete);
            txtName = (TextView) v.findViewById(R.id.text_name_sync);
            txtCode = (TextView) v.findViewById(R.id.text_code_sync);
            txtFirstResult = (TextView) v.findViewById(R.id.text_first_sync);
            txtSecondResult = (TextView) v.findViewById(R.id.text_second_sync);
            imgStatus = (ImageView) v.findViewById(R.id.img_status_sync);
            imgSync = (ImageView) v.findViewById(R.id.img_sync);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerSyncAthlete.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_sync_athlete, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DatabaseHelper db = new DatabaseHelper(activity);
        Tests test = db.getTestFromAthleteAndType(list.get(position).getId(), idTest);

        holder.txtName.setText(list.get(position).getName());
        holder.txtCode.setText(list.get(position).getCode());

        if(test!=null) {
            TestTypes type = db.getTestTypeFromId(test.getType());
            if (type.getValueType().toLowerCase().equals("corrida") || type.getValueType().toLowerCase().equals("tempo")) {
                holder.txtFirstResult.setText(Services.convertInTime(test.getFirstValue()));
                holder.txtSecondResult.setText(Services.convertInTime(test.getSecondValue()));
            }
            else if(type.getValueType().toLowerCase().equals("repeticao")|| type.getValueType().toLowerCase().equals("repeticao por tempo"))
            {
                holder.txtFirstResult.setText(String.valueOf(test.getFirstValue()));
                holder.txtSecondResult.setVisibility(View.GONE);
            }
            else {
                holder.txtFirstResult.setText(Services.convertCentimetersinMeters(test.getFirstValue()));
                holder.txtSecondResult.setText(Services.convertCentimetersinMeters(test.getSecondValue()));
            }
        }

        if (Services.convertIntInBool(test.getSync())){
            holder.imgSync.setVisibility(View.GONE);
            holder.imgStatus.setImageDrawable(activity.getDrawable(R.drawable.check));

        }
        else {
            holder.imgSync.setVisibility(View.VISIBLE);
            holder.imgStatus.setVisibility(View.GONE);
        }

        holder.imgSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SyncAthleteActivity.onCLickSyncAthlete(activity, position);
            }
        });

        holder.linearBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("clique no item da lista", "true");
                Log.i("Elemento clicado", list.get(position).getId());
                //SyncAthleteActivity.onClickItemList(activity, position, list.get(position).getId());
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    private boolean verifyStatus(String athlete, String type) {
        DatabaseHelper db = new DatabaseHelper(activity);
        Tests test = db.getTestFromAthleteAndType(athlete, type);
        return Services.convertIntInBool(test.getSync());
    }
}
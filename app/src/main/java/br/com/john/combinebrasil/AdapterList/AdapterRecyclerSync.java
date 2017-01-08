package br.com.john.combinebrasil.AdapterList;

/**
 * Created by GTAC on 06/01/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Test;

import java.util.ArrayList;

import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.AllActivities;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.SyncActivity;

public class AdapterRecyclerSync  extends RecyclerView.Adapter<AdapterRecyclerSync.ViewHolder> {
    private String[] values;
    private ArrayList<TestTypes> list;
    private Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerSync(Activity act, ArrayList<TestTypes> list, String[] values) {
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
        TextView txtName;
        TextView txtStatus;
        public ViewHolder(View v) {
            super(v);
            linearBackground = (LinearLayout) v.findViewById(R.id.linear_background_list);
            txtName = (TextView) v.findViewById(R.id.text_test_list);
            txtStatus = (TextView) v.findViewById(R.id.text_status);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerSync.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_sync, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtName.setText(list.get(position).getName());
        if(verifyStatus(list.get(position).getId()) == Constants.STATUS_NOT_INIT)
            holder.txtStatus.setText("Nenhum teste do exercício foi sincronizado.");
        else if(verifyStatus(list.get(position).getId()) == Constants.STATUS_DONE)
            holder.txtStatus.setText("O exercício já está atualizado!");
        else if(verifyStatus(list.get(position).getId()) == Constants.STATUS_PENDING)
            holder.txtStatus.setText("Faltam atualizar alguns atletas.");


        holder.linearBackground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("clique no item da lista", "true");
                Log.i("Elemento clicado", list.get(position).getId());
                SyncActivity.onClickItemList(activity, position, list.get(position).getId());
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    private int verifyStatus(String id){
        DatabaseHelper db = new DatabaseHelper(activity);
        long tests = db.getCountTest(id);
        long testsSync = db.getCountTetsSync(id);
        if (testsSync==0)
            return Constants.STATUS_NOT_INIT;
        else if(tests==testsSync)
            return Constants.STATUS_DONE;
        else if(testsSync<tests)
            return Constants.STATUS_PENDING;
        else
            return Constants.STATUS_NOT_INIT;
    }
}
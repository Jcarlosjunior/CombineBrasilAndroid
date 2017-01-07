package br.com.john.combinebrasil.AdapterList;

/**
 * Created by GTAC on 06/01/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.Services;
import br.com.john.combinebrasil.SyncActivity;

public class AdapterRecyclerSync extends RecyclerView.Adapter<AdapterRecyclerSync.ViewHolder> {
    private final int STATUS_NOT_INIT=0, STATUS_DONE = 1, STATUS_PENDING=2;


    private String[] values;
    private ArrayList<TestTypes> list;
    private Activity act;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerSync(Activity act, ArrayList<TestTypes> list, String[] values) {
        this.list = list;
        this.values = values;
        this.act = act;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtTest;
        public TextView txtStatus;
        public LinearLayout listItem;

        public ViewHolder(View v) {
            super(v);
            txtTest = (TextView) v.findViewById(R.id.text_test_list);
            txtStatus = (TextView) v.findViewById(R.id.text_status);
            listItem = (LinearLayout)v.findViewById(R.id.linear_list);
        }
    }

    public void add(int position, String item) {
        // myTitle.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        //int position = myTitle.indexOf(item);
        //myTitle.remove(position);
        //notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerSync.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_sync, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtTest.setText(list.get(position).getName());
        if(verifyStatus(list.get(position).getId()) == STATUS_NOT_INIT)
            holder.txtStatus.setText("O exercício ainda não atualizado.");
        else if(verifyStatus(list.get(position).getId()) == STATUS_DONE)
            holder.txtStatus.setText("O exercício já está atualizado!");
        else if(verifyStatus(list.get(position).getId()) == STATUS_PENDING)
            holder.txtStatus.setText("Faltam atualizar alguns atletas.");


        holder.listItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("clique no item da lista", "true");
                Log.i("Elemento clicado", list.get(position).getId());
                SyncActivity.onClickItemList(act, position, list.get(position).getId());
            }
        });
    }

    private int verifyStatus(String id){
        DatabaseHelper db = new DatabaseHelper(act);
        long tests = db.getCountTest(id);
        long testsSync = db.getCountTetsSync(id);
        if (testsSync==0)
            return STATUS_NOT_INIT;
        else if(tests==testsSync)
            return STATUS_DONE;
        else if(testsSync<tests)
            return STATUS_PENDING;
        else
            return STATUS_NOT_INIT;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.AthletesActivity;
import br.com.john.combinebrasil.R;

/**
 * Created by GTAC on 24/10/2016.
 */

public class AdapterListAthletes extends ArrayAdapter<String> {
    private final Context context;
    private final String[] Values;
    private ArrayList<Athletes> list;
    private Activity activity;

    ListView listView;

    public AdapterListAthletes(Context context, String[] values, ArrayList<Athletes> list) {
        super(context, R.layout.layout_list_players, values);
        this.context = context;
        Values = values;
        this.list = list;
    }
    static class ViewHolder {
        LinearLayout linearBackground;
        TextView textNamePlayer;
        TextView textFirstResult;
        TextView textSecondResult;
        ImageView imgStatus;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_players, null);
            viewHolder = new ViewHolder();
            viewHolder.linearBackground = (LinearLayout) convertView.findViewById(R.id.linear_list_players);
            viewHolder.textNamePlayer = (TextView) convertView.findViewById(R.id.text_name_player_list);
            viewHolder.textFirstResult = (TextView) convertView.findViewById(R.id.text_first_result_list);
            viewHolder.textSecondResult = (TextView) convertView.findViewById(R.id.text_second_result_list);
            viewHolder.imgStatus = (ImageView) convertView.findViewById(R.id.img_status_players);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.linearBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });
        viewHolder.textNamePlayer.setText(list.get(position).getName());
        viewHolder.textFirstResult.setText("Primeiro resultado");
        viewHolder.textSecondResult.setText("Segundo resultado");

        return convertView;
    }

    public void click(int position){
        //DetailsActivitiesActivity.linearInfoActivity.setVisibility(View.VISIBLE);
        AthletesActivity.onClickItemList(activity, position);
    }

    public  void setActivity(Activity activity)
    {
        this.activity = activity;
    }
}

package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import junit.framework.Test;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.Constants;
import br.com.john.combinebrasil.TestSelectiveActivity;

/**
 * Created by GTAC on 24/04/2017.
 */


public class AdapterRecyclerResultsTest extends RecyclerView.Adapter<AdapterRecyclerResultsTest.ViewHolder> {
    private String[] values;
    public static String[] valuesID;
    public static ArrayList<Tests> list;
    private Activity act;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerResultsTest(Activity act, ArrayList<Tests> list, String[] values) {
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
        public TextView txtResult;
        public ImageView imageIcon;
        private ConstraintLayout constraintItem;

        public ViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.text_name_test);
            imageIcon = (ImageView) v.findViewById(R.id.icon);
            constraintItem = (ConstraintLayout) v.findViewById(R.id.constraint_list);
            txtResult = (TextView) v.findViewById(R.id.text_result_test);
        }
    }

    public void add(int position, String item) {
        notifyItemInserted(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerResultsTest.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_test_choose, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(list.get(holder.getPosition()).getType());

        holder.txtResult.setText(String.valueOf(list.get(position).getFirstValue()));

        Picasso.with(act)
                .load("http://icons.iconarchive.com/icons/icons8/windows-8/256/Sports-Running-icon.png")
                .into(holder.imageIcon);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}
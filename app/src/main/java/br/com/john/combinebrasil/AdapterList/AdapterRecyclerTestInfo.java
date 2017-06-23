package br.com.john.combinebrasil.AdapterList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.TestTypes;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.Services.Constants;

/**
 * Created by GTAC on 22/06/2017.
 */


public class AdapterRecyclerTestInfo extends RecyclerView.Adapter<AdapterRecyclerTestInfo.ViewHolder> {
    private String[] values;
    private ArrayList<TestTypes> list;
    private Activity act;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerTestInfo(Activity act, ArrayList<TestTypes> list, String[] values) {
        super();
        this.list = list;
        this.values = values;
        this.act = act;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtName;
        public ImageView imageIcon;

        public ViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.text_name_test);
            imageIcon = (ImageView) v.findViewById(R.id.icon);
        }
    }

    public void add(int position, String item) {
        notifyItemInserted(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerTestInfo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_test_info, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        chosseIcon(list.get(position).getIconImageURL(), holder.imageIcon, false);
        holder.txtName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    private void chosseIcon(String id, ImageView img, boolean clicked){
        int color = clicked == true? Constants.colorBlue : Constants.colorWhite;

        if(id.equals("icon_w_five"))
            img.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable) act.getResources()
                    .getDrawable(R.drawable.icon_w_five)).getBitmap(), color));
        else if(id.equals("icon_w_twenty"))
            img.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable) act.getResources()
                    .getDrawable(R.drawable.icon_w_twenty)).getBitmap(), color));
        else if(id.equals("icon_back_pedal"))
            img.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable) act.getResources()
                    .getDrawable(R.drawable.icon_back_pedal)).getBitmap(), color));
        else if(id.equals("icon_sprint_40"))
            img.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable) act.getResources()
                    .getDrawable(R.drawable.icon_sprint_40)).getBitmap(), color));
        else if(id.equals("icon_t_route"))
            img.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable) act.getResources()
                    .getDrawable(R.drawable.icon_t_route)).getBitmap(), color));
        else if(id.equals("icon_pocket_move"))
            img.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable) act.getResources()
                    .getDrawable(R.drawable.icon_pocket_move)).getBitmap(), color));
        else if(id.equals("icon_salto_horizontal"))
            img.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable) act.getResources()
                    .getDrawable(R.drawable.icon_salto_horizontal)).getBitmap(), color));
    }

    private Bitmap getRoundedCornerBitmap(Bitmap pBitmap, int color) {

        int width = 400;
        int height = pBitmap.getHeight() * 400 / pBitmap.getWidth();

        Bitmap bitmap = Bitmap.createScaledBitmap(pBitmap, width, height, true);

        int heightDiff = (width - height) / 4;
        height = width;
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(2, 2, width, height);

        final RectF rectF = new RectF(rect);
        final float roundPx = width / 4;

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, 2, heightDiff, paint);

        return output;
    }

}
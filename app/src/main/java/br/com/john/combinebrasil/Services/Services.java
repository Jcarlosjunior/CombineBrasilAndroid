package br.com.john.combinebrasil.Services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import br.com.john.combinebrasil.CronometerActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.ResultsActivity;

/**
 * Created by GTAC on 18/10/2016.
 */
public class Services {
    private static Activity activity = null;
    private static AlertDialog alerta;

    public static void message(String title, String message, Activity act) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setPositiveButton("Ok", null);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    public static void messageSaveResults(Activity act) {
        activity = act;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setPositiveButton("Ok", dialogSave);
        builder.setMessage("Resultados foram salvos!");
        builder.setTitle("Mensagem");
        builder.create().show();
    }

    private static DialogInterface.OnClickListener dialogSave = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if(activity.getClass().getSimpleName().equals("CronometerActivity"))
                        CronometerActivity.finished(activity);
                    else if(activity.getClass().getSimpleName().equals("ResultsActivity"))
                        ResultsActivity.finished(activity);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public static void messageAlert(Activity act, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        View view = act.getLayoutInflater().inflate(R.layout.alert_message, null);
        builder.setView(view);

        alerta = builder.create();
        alerta.show();
        TextView textTitle = (TextView) view.findViewById(R.id.text_title_message);
        textTitle.setText(title);

        TextView textAlert = (TextView) view.findViewById(R.id.text_alert_message);
        textAlert.setText(message);
        Button nbutton = (Button) view.findViewById(R.id.button_ok_alert_message);
        nbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.hide();
            }
        });
    }

    public static boolean isOnline(Activity act) {
        ConnectivityManager cm =
                (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else{
            Toast.makeText(act,"É necessário uma conexão com a internet.", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public static void changeColorEdit(EditText edit, String title, String mensagem, Activity act){
        edit.setBackground(act.getResources().getDrawable(R.drawable.background_edit_error));
        messageAlert(act, title, mensagem);
        //Services.message("Dados Inválidos", mensagem, this);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap pBitmap) {

        int width = 220;
        int height = pBitmap.getHeight() * 220 / pBitmap.getWidth();


        Bitmap bitmap = Bitmap.createScaledBitmap(pBitmap, width, height, true);

        int heightDiff = (width - height) / 4;
        height = width;
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffffffff;
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

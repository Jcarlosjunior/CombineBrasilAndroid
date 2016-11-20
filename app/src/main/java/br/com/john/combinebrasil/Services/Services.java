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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.CronometerActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.ResultsActivity;

/**
 * Created by GTAC on 18/10/2016.
 */
public class Services {
    private static Activity activity = null;
    private static AlertDialog alerta;
    private static String whoCalled="";

    public static void messageAlert(Activity act, String title, String message, String whoCalled){
        Services.whoCalled = whoCalled;
        Services.activity=act;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        View view = act.getLayoutInflater().inflate(R.layout.alert_message, null);
        builder.setView(view);

        TextView textTitle = (TextView) view.findViewById(R.id.text_title_message);
        textTitle.setText(title);

        TextView textAlert = (TextView) view.findViewById(R.id.text_alert_message);
        textAlert.setText(message);
        Button nbutton = (Button) view.findViewById(R.id.button_ok_alert_message);
        LinearLayout linear = (LinearLayout) view.findViewById(R.id.linear_message_ok);

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.hide();
            }
        });
        nbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOkAlert();
            }
        });
        alerta = builder.create();
        alerta.show();
    }

    private static void clickedOkAlert(){
        if(whoCalled.toUpperCase().equals("HIDE") || whoCalled.equals(""))
            alerta.hide();
        else if(whoCalled.toUpperCase().equals("DIALOGSAVECRONOMETER")){
            alerta.hide();
            CronometerActivity.finished(activity);
        }
        else if(whoCalled.toUpperCase().equals("DIALOGSAVERESULTS")){
            alerta.hide();
            ResultsActivity.finished(activity);
        }
        else if(whoCalled.toUpperCase().equals("POSTATHLETE")){
            alerta.hide();
            CreateAccountAthlete.finished(activity);
        }
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
        messageAlert(act, title, mensagem, "hide");
    }
    public static void changeColorEditBorderError(EditText edit, Activity act){
        edit.setBackground(act.getResources().getDrawable(R.drawable.background_edit_border_error));
        edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.alert_circle, 0);

    }
    public static void changeColorEditBorder(EditText edit, Activity act){
        edit.setBackground(act.getResources().getDrawable(R.drawable.background_edit_border));
        edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

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

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static String mask(String format, String text){
        String maskedText="";
        int i =0;
        for (char m : format.toCharArray()) {
            if (m != '#') {
                maskedText += m;
                continue;
            }
            try {
                maskedText += text.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return maskedText;
    }
}

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
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import br.com.john.combinebrasil.CreateAccountAthlete;
import br.com.john.combinebrasil.CronometerActivity;
import br.com.john.combinebrasil.CronometerOnlyOneActivity;
import br.com.john.combinebrasil.MainActivity;
import br.com.john.combinebrasil.R;
import br.com.john.combinebrasil.ResultsActivity;
import br.com.john.combinebrasil.ResultsOnlyOneActivity;
import br.com.john.combinebrasil.TimerActivity;

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
        alerta.hide();
        if(whoCalled.toUpperCase().equals("HIDE") || whoCalled.equals(""))
            alerta.hide();
        else if(whoCalled.toUpperCase().equals("DIALOGSAVECRONOMETER")){
            if(activity.getClass().getSimpleName().equals("CronometerActivity"))
                CronometerActivity.finished(activity);
            else
                CronometerOnlyOneActivity.finished(activity);
        }
        else if(whoCalled.toUpperCase().equals("DIALOGSAVERESULTS")){
            if(activity.getClass().getSimpleName().equals("ResultsActivity"))
                ResultsActivity.finished(activity);
            else if(activity.getClass().getSimpleName().equals("ResultsOnlyOneActivity"))
                ResultsOnlyOneActivity.finished(activity);
        }
        else if(whoCalled.toUpperCase().equals("POSTATHLETE")){
            CreateAccountAthlete.finished(activity);
        }
        else if (whoCalled.equals("updateAthelete"))
            CreateAccountAthlete.update(activity);
        else if(activity.getClass().getSimpleName().equals(Constants.TIMER_ACTIVITY))
            TimerActivity.returnOption(activity, whoCalled);
        else if (whoCalled.equals("exit")){
            MainActivity.returnMessageOptions(activity, whoCalled);
        }
    }

    public static boolean isOnline(Activity act) {
        ConnectivityManager cm =
                (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
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

        int width = 400;
        int height = pBitmap.getHeight() * 400 / pBitmap.getWidth();


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

    public static String verifyQualification(float rating){
        int num = (int)rating;
        String ret = "";
        switch(num){
            case 1: ret = "Muito baixo";
                break;
            case 2: ret = "Baixo";
                break;
            case 3: ret = "Medio";
                break;
            case 4: ret = "Alto";
                break;
            case 5: ret = "Muito Alto";
                break;
        }
        return ret;
    }

    public static boolean convertIntInBool(int value){
        return (value == 1) ? true : false;
    }
    public  static int convertBoolInInt(boolean value){
        return (value == true) ? 1 : 0;
    }

    public static boolean isConnectectInWifi(Activity act){
        boolean ret = false;
        ConnectivityManager connManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            ret = true;
        }
        return ret;
    }

    public static String convertDate(String date){
        String dateNow = "";
        try {
            for (int i = 0; i <= date.length() - 1; i++) {
                String c = date.substring(i, i + 1);
                if (c.equals("T")) {
                    break;
                } else
                    dateNow = dateNow.concat(c);
            }
            String year = dateNow.substring(0, 4);
            String month = dateNow.substring(5, 7);
            String day = dateNow.substring(8, 10);
            return day + "/" + month + "/" + year;
        }catch (Exception e){
            return date;
        }
    }

    public static long convertMetersinCentimeters(String meters){
        return Long.parseLong(meters.replace(",",""));
    }

    public static String convertCentimetersinMeters(long centimeters){
        String ret = "";
        try{
            String value = String.valueOf(centimeters);

            if(value.length()>=3){
                String met="", cent="";
                met = value.substring(0,1);
                cent = value.substring(1);
                ret = met+","+cent;
            }
            else{
                ret = "0,"+value;
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }

        return ret;
    }

    public static String convertInTime(long millis){
       SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SS");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        String time = formatter.format(calendar.getTime()).toString();

        try {
            String min = time.substring(0,2);
            if(min.equals("00")){
                time = time.substring(3);
            }
        }catch(Exception e){
            e.printStackTrace();
            return "00:00";
        }

        return time;
        //return minutes+":"+seconds+":"+mil;
    }

    public static long convertInMinute(long mili ){
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mili);
        String time = formatter.format(calendar.getTime()).toString();
        String min = time.substring(0,2);
        return Long.parseLong(min);
    }

    public static long convertInSecond(long mili){

        return (mili / 1000) % 60;
    }

    public static long convertInMilliSeconds(String time){
        long milisseconds = 0;
        int cont =0;
        try {
            for (int i = 0; i <= time.length() - 1; i++) {
                if (time.charAt(i) == ':')
                    cont = cont + 1;
            }
        }catch(Exception e){
            return milisseconds;
        }
        if(cont == 1) {
            long sec = Integer.parseInt(time.substring(0, 2));
            long mil = Integer.parseInt(time.substring(3));

            long t = (sec * 1000) + (mil*10);

            milisseconds = t;
        }

        if(cont == 2) {
            try {
                long min = Integer.parseInt(time.substring(0, 2));
                String ti = time.substring(6);
                long sec = Integer.parseInt(time.substring(3, 5));
                long mil = Integer.parseInt(time.substring(6));

                long t = ((min * 60L)*1000) + (sec * 1000) + (mil * 10);

                milisseconds = t;
            }catch (Exception e){
                return milisseconds;
            }
        }

        return milisseconds;
    }


    public static NotificationCompat.Builder buildNotificationCommon(final Activity _context) {
        Log.i("NOTIFICATION","SOUNDS");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context)
                .setWhen(System.currentTimeMillis());
        //Vibration
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Ton
        builder.setSound(notification);

        return builder;
    }

    public static String chooseMonth(String month){
        if(month.equalsIgnoreCase("01") ||month.equalsIgnoreCase("1"))
            return "Janeiro";
        else if(month.equalsIgnoreCase("02")||month.equalsIgnoreCase("2"))
            return "Fevereiro";
        else if(month.equalsIgnoreCase("03")||month.equalsIgnoreCase("3"))
            return "Março";
        else if(month.equalsIgnoreCase("04")||month.equalsIgnoreCase("4"))
            return "Abril";
        else if(month.equalsIgnoreCase("05")||month.equalsIgnoreCase("5"))
            return "Maio";
        else if(month.equalsIgnoreCase("06")||month.equalsIgnoreCase("6"))
            return "Junho";
        else if(month.equalsIgnoreCase("07")||month.equalsIgnoreCase("7"))
            return "Julho";
        else if(month.equalsIgnoreCase("08")||month.equalsIgnoreCase("8"))
            return "Agosto";
        else if(month.equalsIgnoreCase("09")||month.equalsIgnoreCase("9"))
            return "Setembro";
        else if(month.equalsIgnoreCase("10"))
            return "Outubro";
        else if(month.equalsIgnoreCase("11"))
            return "Novembro";
        else if(month.equalsIgnoreCase("12"))
            return "Dezembro";
        else
            return "";
    }
}

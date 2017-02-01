package br.com.john.combinebrasil.Services;

/**
 * Created by GTAC on 01/11/2016.
 */

import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class CountDownTimer {
    private static Thread timerThread = null;
    private static boolean play;
    private static final Date date = new Date();
    private static long milisecondCounter=00,secondCounter=00, minuteCounter=00, hourCounter=00;
    private static String timeCount = "";
    private static TextView textView;
    private static Button btnSave;

    public CountDownTimer(){

    }

    public void initCount(){
        play = true;
        timeUpdate();
    }
    public void timeUpdate() {
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(play){
                    Date newDate = new Date();
                    if(((newDate.getTime()) - date.getTime()) > 99) {
                        milisecondCounter = milisecondCounter + 1;
                        mHandlerUpdateMili.post(mUpdateMili);
                        if (milisecondCounter > 99) {
                            secondCounter = secondCounter + 1;
                            mHandlerUpdateSec.post(mUpdateSec);
                            milisecondCounter=0;
                            if (secondCounter > 59) {
                                minuteCounter = minuteCounter + 1;
                                mHandlerUpdateMinute.post(mUpdateMinute);
                                secondCounter = 0;
                            }
                        }
                    }
                    try{
                        timerThread.sleep(9);
                    }catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        });
        timerThread.start();
    }
    final Handler mHandlerUpdateMili = new Handler();
    final Runnable mUpdateMili = new Runnable() {
        public void run() {
            timeCount = getTimeCount();
            textView.setText(timeCount);
        }
    };
    final Handler mHandlerUpdateSec = new Handler();
    final Runnable mUpdateSec = new Runnable() {
        public void run() {
            timeCount = getTimeCount();
        }
    };
    final Handler mHandlerUpdateMinute = new Handler();
    final Runnable mUpdateMinute= new Runnable() {
        public void run() {
            timeCount = getTimeCount();
        }
    };
    final Handler mHandlerUpdateHour = new Handler();
    final Runnable mUpdateHour = new Runnable() {
        public void run() {
            timeCount = getTimeCount();
        }
    };

    public void stop(){
        timerThread.interrupt();
        milisecondCounter =00;
        hourCounter =00;
        minuteCounter=00;
        secondCounter=00;
        textView.setText("00:00");
        enabledButtonAdd(false);
        play=false;
        //SharedPreferencesAdapter.setTimeChronometer(AllActivitys.mainActivity, getTimeCount());
    }

    public void pause(){
        timerThread.interrupt();
        play=false;
    }

    public static String getTimeCount(){
        String minute=String.valueOf(minuteCounter),
                second=String.valueOf(secondCounter),
                milis=String.valueOf(milisecondCounter);

        if(minuteCounter<10)
            minute = String.valueOf("0"+minuteCounter);
        if(secondCounter<10)
            second = String.valueOf("0"+secondCounter);
        if(milisecondCounter<10)
            milis = String.valueOf("0"+milisecondCounter);

        if(minuteCounter>0) {

            enabledButtonAdd(true);
            return String.valueOf(minute + ":" + second + ":" + milis);
        }
        else {
            if(minuteCounter<=0&&secondCounter<2)
                enabledButtonAdd(false);
            else
                enabledButtonAdd(true);
            return String.valueOf(second + ":" + milis);
        }
    }

    public void setTimeCount(String time){
        try{
            String[] timeArray = time.split(":");
            minuteCounter = Long.parseLong(timeArray[0]);
            secondCounter = Long.parseLong(timeArray[1]);
            milisecondCounter = Long.parseLong(timeArray[1]);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void setPlay(boolean play){
        CountDownTimer.play = play;
    }

    public boolean getPlay(){
        return play;
    }

    public void setTextView(TextView txt){
        textView = txt;
    }
    public void setButton(Button btn){
        btnSave = btn;
    }

    public static void enabledButtonAdd(boolean enabled){
        if(btnSave!=null) {
            if (enabled) {
                btnSave.setEnabled(true);
                btnSave.setAlpha(1f);
            } else {
                btnSave.setEnabled(false);
                btnSave.setAlpha(.5f);
            }
        }
    }
}

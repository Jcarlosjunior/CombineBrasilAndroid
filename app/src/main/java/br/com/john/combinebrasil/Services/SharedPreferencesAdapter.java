package br.com.john.combinebrasil.Services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GTAC on 17/10/2016.
 */
public class SharedPreferencesAdapter {
    private static final String MY_PREFERENCES = "Combine_Brasil";

    private static SharedPreferences shared;
    private static SharedPreferences.Editor editor;

    public static boolean clearData(Context ctx , String key){
        shared = ctx.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return shared.edit().remove(key).commit();
    }

    public static void cleanAllShared(Context ctx){
        shared = ctx.getSharedPreferences(MY_PREFERENCES, ctx.MODE_PRIVATE);
        shared.edit().clear().commit();
    }

    public static void setLoggedSharedPreferences (Context ctx, boolean logged){
        editor = ctx.getSharedPreferences(MY_PREFERENCES, ctx.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.LOGGED, logged);
        editor.commit();
    }

    public static boolean getLoggedSharedPreferences(Context ctx){
        shared = ctx.getSharedPreferences(MY_PREFERENCES, ctx.MODE_PRIVATE);
        boolean logged = shared.getBoolean(Constants.LOGGED, false);
        return logged;
    }

    public static void setValueStringSharedPreferences (Context ctx,String key, String value){
        editor = ctx.getSharedPreferences(MY_PREFERENCES, ctx.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getValueStringSharedPreferences(Context ctx, String key){
        shared = ctx.getSharedPreferences(MY_PREFERENCES, ctx.MODE_PRIVATE);
        String value = shared.getString(key, "");
        return value;
    }

    public static void setTimerDefault (Context ctx, String value){
        editor = ctx.getSharedPreferences(MY_PREFERENCES, ctx.MODE_PRIVATE).edit();
        editor.putString(Constants.TIMER, value);
        editor.commit();
    }

    public static String getTimerDefault(Context ctx){
        shared = ctx.getSharedPreferences(MY_PREFERENCES, ctx.MODE_PRIVATE);
        String value = shared.getString(Constants.TIMER, "");
        return value;
    }
}

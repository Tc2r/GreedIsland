package com.tc2r.greedisland.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.tc2r.greedisland.R;



/**
 * Created by nudennie.white on 3/26/18.
 */

public class PlayerInfo extends AppCompatActivity {
    private static final PlayerInfo ourInstance = new PlayerInfo();

    public static PlayerInfo getInstance() {
        return ourInstance;
    }

    private PlayerInfo() {
    }

    public String Get_Pref_Theme_Key(Context context){
        return context.getApplicationContext().getResources().getString(R.string.pref_theme_selection_key);
    }

    public String Get_Pref_First_Run_Key(Context context){
        return context.getApplicationContext().getResources().getString(R.string.pref_initiate_key);
    }

    public String Get_Pref_First_Tut_Key(Context context){
        return context.getApplicationContext().getResources().getString(R.string.pref_first_time_tut_key);
    }


    public Boolean GetFirstRun(Context context, Boolean defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Get_Pref_First_Run_Key(context), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Get_Pref_First_Run_Key(context), defaultValue);
    }
    public void SetFirstRun(Context context, Boolean setRun){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Get_Pref_First_Run_Key(context), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Get_Pref_First_Run_Key(context), setRun);
        sharedPreferences.edit().commit();
    }

    public Boolean GetTutRan(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Get_Pref_First_Tut_Key(context), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Get_Pref_First_Tut_Key(context), false);
    }
    public void SetTutRan(Boolean setRun){
        SharedPreferences sharedPreferences = getSharedPreferences(PlayerInfo.getInstance().Get_Pref_First_Tut_Key(getApplicationContext()), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(getString(R.string.pref_first_time_tut_key), setRun);
        sharedPreferences.edit().commit();
    }
}

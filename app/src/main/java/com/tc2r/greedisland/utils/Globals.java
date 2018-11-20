package com.tc2r.greedisland.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.tc2r.greedisland.R;

/**
 * Created by Tc2r on 2/4/2017.
 * <p>
 * Description:
 */

public class Globals {
    private static Globals instance;
    private static String USERNAME;
    private static int ACTION_TOKENS;


    private Globals() {
    }

    public static int getActionTokens() {
        return ACTION_TOKENS;
    }

    public static void setActionTokens(int actionTokens) {
        ACTION_TOKENS = actionTokens;
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

    @SuppressWarnings("isNetworkAvailable")
    public static boolean isNetworkAvailable(Context context) {

        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) {


                    return true;
                }
            }

        } catch (Exception e) {

            return false;
        }
        return false;
    }

    public static void ChangeTheme(Context context) {
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        String customTheme = setting.getString(context.getString(R.string.pref_theme_selection_key), context.getString(R.string.pref_theme_default_selection));
        switch (customTheme) {
            case "Sunlight":
                context.setTheme(R.style.AppTheme_Sunlight);
                break;
            case "Bouquets":
                context.setTheme(R.style.AppTheme_Bouquets);
                break;
            case "Red Wedding":
                context.setTheme(R.style.AppTheme_RedWedding);
                break;
            case "Royal Flush":
                context.setTheme(R.style.AppTheme_RoyalF);
                break;
            case "Birds n Berries":
                context.setTheme(R.style.AppTheme_BirdBerries);
                break;
            case "Blue Berry":
                context.setTheme(R.style.AppTheme_BlueBerry);
                break;
            case "Cinnamon":
                context.setTheme(R.style.AppTheme_Cinnamon);
                break;
            case "Day n Night":
                context.setTheme(R.style.AppTheme_Night);
                break;
            case "Earthly":
                context.setTheme(R.style.AppTheme_Earth);
                break;
            case "Forest":
                context.setTheme(R.style.AppTheme_Forest);
                break;
            case "Fresh Greens":
                context.setTheme(R.style.AppTheme_Greens);
                break;
            case "Fresh n Energetic":
                context.setTheme(R.style.AppTheme_Fresh);
                break;
            case "Icy Blue":
                context.setTheme(R.style.AppTheme_Icy);
                break;
            case "Ocean":
                context.setTheme(R.style.AppTheme_Ocean);
                break;
            case "Play Green/blues":
                context.setTheme(R.style.AppTheme_GrnBlu);
                break;
            case "Primary":
                context.setTheme(R.style.AppTheme_Prime);
                break;
            case "Rain":
                context.setTheme(R.style.AppTheme_Rain);
                break;
            case "Tropical":
                context.setTheme(R.style.AppTheme_Tropical);
                break;
            default:
                context.setTheme(R.style.AppTheme_Greens);
                break;
        }
    }

    public String getUsername() {
        return USERNAME;
    }

    public void setUsername(String username) {
        Globals.USERNAME = username;
    }
}

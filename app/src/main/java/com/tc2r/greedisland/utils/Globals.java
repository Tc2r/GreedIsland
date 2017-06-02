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

		int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
						ConnectivityManager.TYPE_WIFI};
		try {
			ConnectivityManager connectivityManager =
							(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			for (int networkType : networkTypes) {
				NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
				if (activeNetworkInfo != null &&
								activeNetworkInfo.getType() == networkType) {


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
		String customTheme = setting.getString("Theme_Preference", "Fresh Greens");
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
				////Log.d("Test", "Birds n Berries");
				context.setTheme(R.style.AppTheme_BirdBerries);
				break;
			case "Blue Berry":
				////Log.d("Test", "Blue Berry!");
				context.setTheme(R.style.AppTheme_BlueBerry);
				break;
			case "Cinnamon":
				////Log.d("Test", "Cinnamon!");
				context.setTheme(R.style.AppTheme_Cinnamon);
				break;
			case "Day n Night":
				////Log.d("Test", "Day n Night");
				context.setTheme(R.style.AppTheme_Night);
				break;
			case "Earthly":
				////Log.d("Test", "Earthly!");
				context.setTheme(R.style.AppTheme_Earth);
				break;
			case "Forest":
				////Log.d("Test", "Forest!");
				context.setTheme(R.style.AppTheme_Forest);
				break;
			case "Fresh Greens":
				////Log.d("Test", "GREENS!");
				context.setTheme(R.style.AppTheme_Greens);
				break;
			case "Fresh n Energetic":
				////Log.d("Test", "Fresh n Energetic");
				context.setTheme(R.style.AppTheme_Fresh);
				break;
			case "Icy Blue":
				////Log.d("Test", "Icy!");
				context.setTheme(R.style.AppTheme_Icy);
				break;
			case "Ocean":
				////Log.d("Test", "Ocean");
				context.setTheme(R.style.AppTheme_Ocean);
				break;
			case "Play Green/blues":
				////Log.d("Test", "Play Green/blues");
				context.setTheme(R.style.AppTheme_GrnBlu);
				break;
			case "Primary":
				////Log.d("Test", "Primary");
				context.setTheme(R.style.AppTheme_Prime);
				break;
			case "Rain":
				////Log.d("Test", "Rain!");
				context.setTheme(R.style.AppTheme_Rain);
				break;
			case "Tropical":
				////Log.d("Test", "Tropical");
				context.setTheme(R.style.AppTheme_Tropical);
				break;
			default:
				////Log.d("Test", "Default");
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

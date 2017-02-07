package com.tc2r.greedisland.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.tc2r.greedisland.MainActivity;
import com.tc2r.greedisland.R;

import java.lang.ref.WeakReference;
import java.util.Random;


public class Splash2 extends AppCompatActivity {

	private final int SPLASH_DISPLAY_LENGTH = 2000;
	private Handler handler = new Handler();

	// 1. Create a static nested class that extends Runnable to start the main Activity

	public static class StartSplashRunnable implements Runnable {
		private WeakReference<Activity> mActivity;


		public StartSplashRunnable(Activity mActivity) {
			this.mActivity = new WeakReference<Activity>(mActivity);



		}

		@Override
		public void run() {

			// check that the reference is valid and execute the code
			if (mActivity.get() != null) {
				Activity activity = mActivity.get();
				Intent intent = new Intent(activity, MainActivity.class);
				activity.startActivity(intent);
				activity.finish();
				activity = null;
				intent = null;

			}
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		CheckTheme();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash2);
		ImageView splash = (ImageView) findViewById(R.id.splashScreen);
		Random rand = new Random();
		int showSplash = rand.nextInt(5)+1;
		showSplash = 5;
		switch (showSplash){
			case 1:
				splash.setImageResource(R.drawable.splash);
				break;
			case 2:
				splash.setImageResource(R.drawable.splash2);
				break;

			case 3:
				splash.setImageResource(R.drawable.splash3);
				break;

			case 4:
				splash.setImageResource(R.drawable.splash4);
				break;
			case 5:
				splash.setImageResource(R.drawable.splash5);
				break;

		}

		handler.postDelayed(new StartSplashRunnable(this), SPLASH_DISPLAY_LENGTH);
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		handler = null;
		Runtime.getRuntime().gc();
		super.onDestroy();
	}

	private void CheckTheme() {
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplication());
		String customTheme = setting.getString("Theme_Preference", "Fresh Greens");
		switch (customTheme) {
			case "Sunlight":
				setTheme(R.style.AppTheme_Sunlight);
				break;
			case "Bouquets":
				setTheme(R.style.AppTheme_Bouquets);
				break;
			case "Red Wedding":
				setTheme(R.style.AppTheme_RedWedding);
				break;
			case "Royal Flush":
				setTheme(R.style.AppTheme_RoyalF);
				break;
			case "Birds n Berries":
				//Log.wtf("Test", "Birds n Berries");
				setTheme(R.style.AppTheme_BirdBerries);
				break;
			case "Blue Berry":
				//Log.wtf("Test", "Blue Berry!");
				setTheme(R.style.AppTheme_BlueBerry);
				break;
			case "Cinnamon":
				//Log.wtf("Test", "Cinnamon!");
				setTheme(R.style.AppTheme_Cinnamon);
				break;
			case "Day n Night":
				//Log.wtf("Test", "Day n Night");
				setTheme(R.style.AppTheme_Night);
				break;
			case "Earthly":
				//Log.wtf("Test", "Earthly!");
				setTheme(R.style.AppTheme_Earth);
				break;
			case "Forest":
				//Log.wtf("Test", "Forest!");
				setTheme(R.style.AppTheme_Forest);
				break;
			case "Fresh Greens":
				//Log.wtf("Test", "GREENS!");
				setTheme(R.style.AppTheme_Greens);
				break;
			case "Fresh n Energetic":
				//Log.wtf("Test", "Fresh n Energetic");
				setTheme(R.style.AppTheme_Fresh);
				break;
			case "Icy Blue":
				//Log.wtf("Test", "Icy!");
				setTheme(R.style.AppTheme_Icy);
				break;
			case "Ocean":
				//Log.wtf("Test", "Ocean");
				setTheme(R.style.AppTheme_Ocean);
				break;
			case "Play Green/blues":
				//Log.wtf("Test", "Play Green/blues");
				setTheme(R.style.AppTheme_GrnBlu);
				break;
			case "Primary":
				//Log.wtf("Test", "Primary");
				setTheme(R.style.AppTheme_Prime);
				break;
			case "Rain":
				//Log.wtf("Test", "Rain!");
				setTheme(R.style.AppTheme_Rain);
				break;
			case "Tropical":
				//Log.wtf("Test", "Tropical");
				setTheme(R.style.AppTheme_Tropical);
				break;
			default:
				//Log.wtf("Test", "Default");
				setTheme(R.style.AppTheme_Greens);
				break;
		}

	}
}

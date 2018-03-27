package com.tc2r.greedisland.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.tc2r.greedisland.MainActivity;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.utils.Globals;

import java.lang.ref.WeakReference;
import java.util.Random;


public class Splash2 extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	// Declare final variables
	private static final int SPLASH_DISPLAY_LENGTH = 1500;

	// Declare variables
	private Handler handler = new Handler();
	private SharedPreferences setting;


	// 1. Create a static nested class that extends Runnable to start the main Activity

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setting = PreferenceManager.getDefaultSharedPreferences(Splash2.this);
		setting.registerOnSharedPreferenceChangeListener(this);
		onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash2);

		ImageView splash = (ImageView) findViewById(R.id.splashScreen);
		Random rand = new Random();
		int showSplash = rand.nextInt(5)+1;
		switch (showSplash){
			case 1:
				splash.setImageResource(R.drawable.splash1);
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
	protected void onStop() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		View root = findViewById(R.id.activity_splash2);
		setContentView(new View(this));
		unbindDrawables(root);
		System.gc();
		super.onStop();
	}

	@Override
	public void onDestroy() {

		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		super.onDestroy();
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		//Toast.makeText(this, "CHANGE", Toast.LENGTH_SHORT).show();
		if (key.equals(getString(R.string.pref_theme_selection_key))) {
			Globals.ChangeTheme(this);
		}
	}

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
				intent.putExtra("init", true);
				activity.startActivity(intent);
				activity.finish();
				intent = null;
				activity = null;

			}
		}
	}
}

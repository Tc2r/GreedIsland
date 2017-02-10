package com.tc2r.greedisland.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.tc2r.greedisland.R;

import java.lang.ref.WeakReference;
import java.util.Random;

public class Splash1 extends AppCompatActivity {
	private final int SPLASH_DISPLAY_LENGTH = 1500;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash1);

		ImageView splash = (ImageView) findViewById(R.id.splashScreen);
		Random rand = new Random();
		int showSplash = rand.nextInt(3) + 1;
		switch (showSplash) {
			case 1:
				splash.setImageResource(R.drawable.logo_joy);
				break;
			case 2:
			case 3:
				splash.setImageResource(R.drawable.mysplash);
				break;
		}
		handler.postDelayed(new StartSplashRunnable(this), SPLASH_DISPLAY_LENGTH);
	}

	@Override
	public void onDestroy() {

		handler.removeCallbacksAndMessages(null);
		handler = null;
		super.onDestroy();
	}

	public static class StartSplashRunnable implements Runnable {
		private WeakReference<Activity> mActivity;

		private StartSplashRunnable(Activity activity) {
			mActivity = new WeakReference(activity);
		}


		@Override
		public void run() {


			if (mActivity.get() != null) {
				Activity activity = mActivity.get();
				Intent intent = new Intent(activity, Splash2.class);
				activity.startActivity(intent);
				activity.finish();
				activity = null;
				intent = null;

			}
		}
	}
}

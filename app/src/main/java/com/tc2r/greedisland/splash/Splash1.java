package com.tc2r.greedisland.splash;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.tc2r.greedisland.R;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Displays a splash screen when app is opened, as well as playing a sound clip.
 */
public class Splash1 extends AppCompatActivity {
    // Declare final variables.
    private static final int SPLASH_DISPLAY_LENGTH = 1800;

    // Declare variables.
    private Handler handler = new Handler();
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash1);

        mp = MediaPlayer.create(this, R.raw.tc_splash_intro);
        mp.setVolume(50, 50);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();

        ImageView splash = (ImageView) findViewById(R.id.splashScreen);

        // 25% chance to use JoyStation logo, 75% chance to use Tc2r Splash.
        Random rand = new Random();
        int showSplash = rand.nextInt(4) + 1;
        rand = null;
        switch (showSplash) {
            case 1:
                splash.setImageResource(R.drawable.logo_joy);
                break;
            case 2:
            case 3:
            case 4:
                splash.setImageResource(R.drawable.tc2r_splash);
                break;
        }
        handler.postDelayed(new StartSplashRunnable(this), SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onStop() {
        // unbind heavy objects, call garbage collection.
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        View root = findViewById(R.id.activity_splash1);
        setContentView(new View(this));
        if (root != null) {
            unbindDrawables(root);
        }
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    // unbind all drawables from view objects.
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
                intent = null;
                activity.finish();
                activity = null;


            }
        }
    }
}

package com.tc2r.greedisland.utils;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by Tc2r on 2/21/2017.
 * <p>
 * Description:
 */

public class AnimationCardReceived implements Interpolator {


    public static void ReceiveCard(final RelativeLayout layout, final View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_PARENT, -0.2f, Animation.RELATIVE_TO_SELF, 3.0f);

        slide.setInterpolator(new AnticipateOvershootInterpolator());
        slide.setStartOffset(400);


        final Animation shrink = new ScaleAnimation(1, .2f, 1, .2f, layout.getWidth() / 2, layout.getHeight());

        shrink.setDuration(400);
        slide.setDuration(700);

        slide.setFillAfter(false);
        slide.setFillEnabled(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //layout.startAnimation(shrink);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                layout.removeAllViews();

                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
                //layout.setVisibility(View.GONE);
                //layout.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(shrink);
        animationSet.addAnimation(slide);

        layout.startAnimation(animationSet);


    }

    @Override
    public float getInterpolation(float input) {
        return 0;
    }
}

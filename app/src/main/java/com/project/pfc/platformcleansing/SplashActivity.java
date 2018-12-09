package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
   final String TAG = "Animation";
    FrameLayout mFrame;
    ImageView mShelter;
    ImageView mBunker;
    int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // SplashActivity full screen
        setContentView(R.layout.activity_splash);
        mFrame = (FrameLayout) findViewById(R.id.activity_splash);
        mShelter = (ImageView) findViewById(R.id.shelter);
        mBunker = (ImageView) findViewById(R.id.bunker);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;

        startShelterFrameAnimation();
        startBunkerTweenAnimation();
    }

    private void startShelterFrameAnimation() {
        mShelter.setBackgroundResource(R.drawable.frame_anim);
        AnimationDrawable platformAnim = (AnimationDrawable) mShelter.getBackground();
        platformAnim.start();
    }

    private void startBunkerTweenAnimation() {
        Animation foamCleansing_anim = AnimationUtils.loadAnimation(this, R.anim.bunker);
        mBunker.startAnimation(foamCleansing_anim);
        foamCleansing_anim.setAnimationListener(animationListener);
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.i(TAG, "onAnimationEnd");
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };
}

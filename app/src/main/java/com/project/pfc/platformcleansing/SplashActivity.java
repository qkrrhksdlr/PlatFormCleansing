package com.project.pfc.platformcleansing;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
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
            if(!PermissionsStateCheck.permissionState(getApplicationContext(), PermissionsStateCheck.permission_location) ||
                    !PermissionsStateCheck.permissionState(getApplicationContext(), PermissionsStateCheck.permission_write_external)){
                dialog();
            } else {
                finish();
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };

    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한요청");
        builder.setMessage("저희 어플의 모든 기능을 이용하기 위해선 다음 권한을 동의 해주셔야 합니다.\n 요청권한 : 현재위치, 외부저장소");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), ListActivity.class));
                    }
                });
        builder.show();
    }
}

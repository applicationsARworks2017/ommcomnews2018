package com.lipl.ommcom.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.lipl.ommcom.R;
import com.lipl.ommcom.util.AnimationUtil;
import com.lipl.ommcom.util.AnimatorPath;
import com.lipl.ommcom.util.PathEvaluator;
import com.lipl.ommcom.util.Util;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class SplashScreen extends AppCompatActivity {
    private RelativeLayout layoutLogoImg;
    private ObjectAnimator anim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        init();
        intro();

    }
    private void intro(){
        AnimationUtil.fadeIn(layoutLogoImg, 2000, 0);
        // playAudio();
    }
    private void init(){
        layoutLogoImg = (RelativeLayout) findViewById(R.id.layoutLogoImg);
        int yPosition= -(Util.getScreenHeight() / 2);
        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.lineTo(0, yPosition);
        anim = ObjectAnimator.ofObject(this, "buttonLoc",
                new PathEvaluator(), path.getPoints().toArray());
        anim.setDuration(2000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                AnimationUtil.zoomOut(layoutLogoImg, 2000, 0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
               /* setToolbarVisible();
                AnimationUtil.slideInOfDifferentToolbarElements(toolbar);
                AnimationUtil.slideDown(mToolbarBottomBorder);
                displayContent();*/
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                intent.putExtra("isFromNotification", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}

package com.lipl.ommcom.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Android Luminous on 4/13/2016.
 */
public class AnimationUtil {

    /**
     * View will disappear in given duration with given time delay
     * */
    public static ObjectAnimator fadeOut(View v, long duration, long delay){
        v.setAlpha(1);
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,View.ALPHA,0);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * View will appear in given duration with given time delay
     * */
    public static ObjectAnimator fadeIn(View v, long duration, long delay){
        v.setAlpha(0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,View.ALPHA,1);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;

    }

    /**
     * View will zoom in in given duration with given time delay
     * */
    public static ObjectAnimator zoomIn(View v, long duration, long delay){
        v.setScaleX(0);
        v.setScaleY(0);

        PropertyValuesHolder propx = PropertyValuesHolder.ofFloat("scaleX", 1);
        PropertyValuesHolder propy = PropertyValuesHolder.ofFloat("scaleY", 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propx, propy);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * View will zoom out given duration with given time delay
     * */
    public static ObjectAnimator zoomOut(View v, long duration, long delay){
        PropertyValuesHolder propx = PropertyValuesHolder.ofFloat(View.SCALE_X, 0);
        PropertyValuesHolder propy = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propx, propy);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    public static ObjectAnimator zoomOutBK(View v, long duration, long delay){
        PropertyValuesHolder propx = PropertyValuesHolder.ofFloat(View.SCALE_X, 0);
        PropertyValuesHolder propy = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propx, propy);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * View will slide in from top given duration with given time delay
     * */
    public static ObjectAnimator slideInFromTop(View v, long duration, long delay){
        v.setTranslationY(-300);
        v.setAlpha(0f);
        PropertyValuesHolder propA = PropertyValuesHolder.ofFloat(View.ALPHA, 1);
        PropertyValuesHolder propY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propA, propY);
        animator.setStartDelay(delay );
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * View will slide in from bottom given duration with given time delay
     * */
    public static ObjectAnimator slideInFromBottom(View v, long duration, long delay){
        v.setTranslationY(300);
        v.setAlpha(0.5f);
        PropertyValuesHolder propA = PropertyValuesHolder.ofFloat(View.ALPHA, 1);
        PropertyValuesHolder propY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propA, propY);
        animator.setStartDelay(delay );
        animator.setDuration(duration);
        animator.start();
        return animator;

    }

    /**
     * View will slide in from left given duration with given time delay
     * */
    public static ObjectAnimator slideInFromLeft(View v, long duration, long delay){
        v.setTranslationX(-(Util.getScreenWidth()));
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,View.TRANSLATION_X,0);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    public static ObjectAnimator slideInFromLeftBK(View v, long duration, long delay){
        v.setTranslationX(0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,View.TRANSLATION_X,(Util.getScreenWidth()));
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * View will slide in from right given duration with given time delay
     * */
    public static ObjectAnimator slideInFromRight(View v, long duration, long delay){
        v.setTranslationX((Util.getScreenWidth()));
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,View.TRANSLATION_X,0);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;

    }

    public static ObjectAnimator slideInFromRightBK(View v, long duration, long delay){
        v.setTranslationX(0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,View.TRANSLATION_X,-(Util.getScreenWidth()));
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * View will slide in and zoom given duration with given time delay
     * */
    public static ObjectAnimator slideInAndZoom(View v, long duration, long delay, float x, float y){
        v.setScaleX(0);
        v.setScaleY(0);
        v.setTranslationX(x);
        v.setTranslationY(y);

        PropertyValuesHolder propx = PropertyValuesHolder.ofFloat(View.SCALE_X, 1);
        PropertyValuesHolder propy = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1);
        PropertyValuesHolder trX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0);
        PropertyValuesHolder trY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propx, propy, trX, trY);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;

    }

    /**
     * View will rotate in given duration with given time delay
     * */
    public static ObjectAnimator rotate(View v, long duration, long delay){
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,View.ROTATION,360);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        return animator;
    }

    /**
     * View will rotate and reveal in given duration with given time delay
     * */
    public static ObjectAnimator rotateAndFadeIn(View v, long duration, long delay){
        v.setAlpha(0f);
        PropertyValuesHolder propA = PropertyValuesHolder.ofFloat(View.ALPHA, 1);
        PropertyValuesHolder propY = PropertyValuesHolder.ofFloat(View.ROTATION, 360);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propA, propY);
        animator.setStartDelay(delay );
        animator.setDuration(duration);
        animator.start();
        return animator;

    }

    /**
     * View will rotate and zoom in given duration with given time delay
     * */
    public static ObjectAnimator rotateAndZoom(View v, long duration, long delay){
        v.setScaleX(0);
        v.setScaleY(0);

        PropertyValuesHolder propx = PropertyValuesHolder.ofFloat(View.SCALE_X, 1);
        PropertyValuesHolder propy = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1);
        PropertyValuesHolder rotate = PropertyValuesHolder.ofFloat(View.ROTATION, 360);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propx, propy, rotate);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.start();
        return animator;

    }

    public static void move(final View view, int positionX, int positionY, int duration){
        view.animate()
            .translationX(positionX)
            .translationY(positionY)
            .setDuration(duration);
    }

    public static void playAnimationsSequentially(AnimatorSet animatorSet){
        animatorSet.playSequentially(animatorSet.getChildAnimations());
    }

    /**
     * Parallax slide in of different toolbar elements
     * */
    public static void slideInOfDifferentToolbarElements(Toolbar toolbar){
        int noOfChild = toolbar.getChildCount();
        View view;
        toolbar.setAlpha(0);
        toolbar.setTranslationY(-300);
        toolbar.animate().setDuration(500).translationY(0).alpha(1);

        /* For loop animates toolbar's child elements to give a nice parallax effect */
        for(int i = 0; i < noOfChild; i++ ){
            view = toolbar.getChildAt(i);
            view.setTranslationY(-300);
            view.animate().setStartDelay(0).setDuration(500).translationY(0);
        }
    }

    /**
     * Slide down from top to position of a view
     * */
    public static void slideDown(View view){
        view.setAlpha(0);
        view.setTranslationY(-300);
        view.animate().setDuration(500).translationY(0).alpha(1);
    }
}

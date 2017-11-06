package com.example.hmyd.mytestandroid_studio.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author kongdy
 * @date 2017/11/3 15:06
 * @describe TODO
 **/

public class MyTransition extends Transition {

    private final static String TOP = "mTop";
    private final static String LEFT = "mLeft";
    private final static String WIDTH = "mWidth";
    private final static String HEIGHT = "mHeight";

    private final static int DEFAULT_NULL_SIZE = 100;

    private final static int NO_POSITION = -1;

    private final static long DEFAULT_DURATION = 600;

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (null != view) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (viewGroup.getChildCount() > 0) {
                    return;
                }
            }
            transitionValues.values.put(TOP, view.getTop());
            transitionValues.values.put(LEFT, view.getLeft());
            transitionValues.values.put(WIDTH, view.getWidth());
            transitionValues.values.put(HEIGHT, view.getHeight());
        } else {
            transitionValues.values.put(TOP, DEFAULT_NULL_SIZE);
            transitionValues.values.put(LEFT, DEFAULT_NULL_SIZE);
            transitionValues.values.put(WIDTH, DEFAULT_NULL_SIZE);
            transitionValues.values.put(HEIGHT, DEFAULT_NULL_SIZE);
        }
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (null != view) {

            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (viewGroup.getChildCount() > 0) {
                    return;
                }
            }

            Rect rect = new Rect();
            view.getHitRect(rect);

            final int screenWidth = getScreenWidth(view.getContext());
            final int screenHeight = getScreenHeight(view.getContext());

            if (rect.top <= 0 || rect.top >= screenHeight) {
                transitionValues.values.put(TOP, NO_POSITION);
            } else {
                transitionValues.values.put(TOP, rect.top);
            }

            if (rect.left <= 0 || rect.left >= screenWidth) {
                transitionValues.values.put(LEFT, NO_POSITION);
            } else {
                transitionValues.values.put(LEFT, rect.left);
            }
        } else {
            transitionValues.values.put(TOP, NO_POSITION);
            transitionValues.values.put(LEFT, NO_POSITION);
        }
    }

    private int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {

        if (startValues == null || endValues == null)
            return new AnimatorSet();

        final View endView = endValues.view;

        if(startValues.values.size() <= 0 || endValues.values.size() <= 0)
            return new AnimatorSet();

        final int startTop = (int) startValues.values.get(TOP);
        final int startLeft = (int) startValues.values.get(LEFT);
        final int endTop = (int) endValues.values.get(TOP);
        final int endLeft = (int) endValues.values.get(LEFT);
        final int endWidth = (int) endValues.values.get(WIDTH);
        final int endHeight = (int) endValues.values.get(HEIGHT);

        final int screenWidth = getScreenWidth(endView.getContext());
        final int screenHeight = getScreenHeight(endView.getContext());

        int widthFactor = endLeft <= screenWidth / 2 ? -1 : 1;

        int[] positionArray = new int[4];

        positionArray[1] = endLeft;
        positionArray[3] = endTop;

        if (startLeft == NO_POSITION) {
            positionArray[0] = screenWidth + (endWidth * widthFactor);
        } else {
            positionArray[0] = startLeft;
        }

        if (startTop == NO_POSITION) {
            positionArray[2] = screenHeight + endHeight;
        } else {
            positionArray[2] = startTop;
        }

        if (getDuration() < 0)
            setDuration(DEFAULT_DURATION);

        ViewCompat.setTranslationX(endView, positionArray[0]);
        ViewCompat.setTranslationY(endView, positionArray[2]);
        endView.requestLayout();

        ValueAnimator leftPositionAnimator = ValueAnimator.ofInt(positionArray[0], positionArray[1]);
        leftPositionAnimator.setDuration(getDuration());
        leftPositionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int current = (int) animation.getCurrentPlayTime();
                ViewCompat.setTranslationX(endView, current);
            }
        });

        ValueAnimator topPositionAnimator = ValueAnimator.ofInt(positionArray[2], positionArray[3]);
        topPositionAnimator.setDuration(getDuration());
        topPositionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int current = (int) animation.getCurrentPlayTime();
                ViewCompat.setTranslationY(endView, current);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftPositionAnimator, topPositionAnimator);

        return animatorSet;
    }
}

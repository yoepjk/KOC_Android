package com.example.jong.test.Util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandCollapseAnimation extends Animation {
    private ViewGroup animatedViewGroup;
    private int endHeight;
    private int type;

    public ExpandCollapseAnimation(ViewGroup viewGroup, int duration, int type) {
        setDuration(duration);
        animatedViewGroup = viewGroup;
        endHeight = animatedViewGroup.getLayoutParams().height;
        this.type = type;
        if(type == 0) {
            animatedViewGroup.getLayoutParams().height = 0;
            animatedViewGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if(type == 0) {
                animatedViewGroup.getLayoutParams().height = (int) (endHeight * interpolatedTime);
            } else {
                animatedViewGroup.getLayoutParams().height = endHeight - (int) (endHeight * interpolatedTime);
            }
            animatedViewGroup.requestLayout();
        } else {
            if(type == 0) {
                animatedViewGroup.getLayoutParams().height = endHeight;
                animatedViewGroup.requestLayout();
            } else {
                animatedViewGroup.getLayoutParams().height = 0;
                animatedViewGroup.setVisibility(View.GONE);
                animatedViewGroup.requestLayout();
                animatedViewGroup.getLayoutParams().height = endHeight;
            }
        }
    }
}

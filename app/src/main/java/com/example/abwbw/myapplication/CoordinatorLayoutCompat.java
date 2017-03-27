package com.example.abwbw.myapplication;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @autor wangbinwei
 * @since 2017/3/27 下午4:19
 */

public class CoordinatorLayoutCompat extends CoordinatorLayout {
    public CoordinatorLayoutCompat(Context context) {
        super(context);
    }

    public CoordinatorLayoutCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinatorLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.i("abwbw16","onStartNestedScroll");
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.i("abwbw16","onStopNestedScroll");
        super.onStopNestedScroll(target);
    }
}

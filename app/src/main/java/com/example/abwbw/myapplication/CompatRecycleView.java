package com.example.abwbw.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by abwbw on 2017/3/27.
 */

public class CompatRecycleView extends RecyclerView {
    public CompatRecycleView(Context context) {
        super(context);
    }

    public CompatRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}

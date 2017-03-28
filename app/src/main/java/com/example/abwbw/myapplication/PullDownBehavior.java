package com.example.abwbw.myapplication;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * 下拉拉伸行为
 * @autor wangbinwei
 * @since 2017/3/27 上午11:11
 */

public class PullDownBehavior extends CoordinatorLayout.Behavior<View> {
    private Rect normal = new Rect();// 矩形空白

    private View target;

    public PullDownBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        final View headView = coordinatorLayout.findViewById(R.id.head_nsv);
        Log.i("abwbw56","onNestedScroll :" + dyConsumed + " " + dyUnconsumed);
        this.target = target;
        fixHeadLoaction(child);

        if(isNeedAnimation() && !isOverHeight(child) && headView == target){
            Log.i("abwbw56","need pull down isNeedAnimation");
            layout(child, dyUnconsumed);
        }  else if(needPullDown(dyUnconsumed)){
            // 执行下拉操作
            Log.i("abwbw56","need pull down");
            layout(child, dyUnconsumed);
        }  else

        if(isNeedAnimation() && !isOverHeight(child) && target instanceof RecyclerView) {
            Log.i("abwbw56","need pull down isNeedAnimation RecyclerView");
            layout(child, dyConsumed);
            ((RecyclerView) target).requestDisallowInterceptTouchEvent(false);
        }else {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }
    }

    private void fixHeadLoaction(View childView){
        if(isOverHeight(childView) && !normal.isEmpty()){
            childView.layout(normal.left, normal.top, normal.right, normal.bottom);
        }
    }

    private boolean isOverHeight(View childView){
        return childView.getTop() <= 0;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        if(isNeedAnimation()){
            animation(child);
        }
        this.target = null;
    }

    public void layout(View child, int y){
        if (normal.isEmpty()) {
            // 填充矩形，目的：就是告诉this:我现在已经有了，你松开的时候记得要执行回归动画.
            normal.set(child.getLeft(), child.getTop(),
                    child.getRight(), child.getBottom());
        }
        // 移动布局
        child.layout(child.getLeft(), child.getTop() - y / 2,
                child.getRight(), child.getBottom() - y / 2);
    }

    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    public void animation(View child) {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, child.getTop(),
                normal.top);
        ta.setInterpolator(new DecelerateInterpolator());
        ta.setDuration(150);
        child.startAnimation(ta);
        // 设置回到正常的布局位置
        child.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();// 清空矩形
    }

    public boolean needPullDown(int dyUnConsumed){
        return dyUnConsumed < 0;
    }
}

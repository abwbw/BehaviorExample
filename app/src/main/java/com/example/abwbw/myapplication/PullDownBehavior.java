package com.example.abwbw.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 下拉拉伸行为
 * @autor wangbinwei
 * @since 2017/3/27 上午11:11
 */

public class PullDownBehavior extends CoordinatorLayout.Behavior<View> {
    private Rect normal = new Rect();// 矩形空白
    private boolean needShrink;
    private int originHeight = -1;
    private Animator animator;

    public PullDownBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        final View headView = coordinatorLayout.findViewById(R.id.head_nsv);
        if(needShrink){
            Log.i("abwbw56","need shrink");

            animation2(headView);
        } else if(needPullDown(dyUnconsumed)){
            animation3(headView);
            // 执行下拉操作
            Log.i("abwbw56","need pull down");
            pullDown(child, dyUnconsumed);
        } else {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if(needShrink(target, dy)){
            needShrink = true;
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
            needShrink = false;
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    private boolean needShrink(View target, int dyUnconsumed){
        if(target instanceof RecyclerView){

            boolean canUp = ((RecyclerView) target).canScrollVertically(-1);
            if( !canUp && dyUnconsumed > 0){
                Log.i("abwbw56","need shrink 2 : dyUnconsumed " + dyUnconsumed);
                return true;

            }
        }
        return false;
    }

    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibleItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) - itemHeight - firstItemBottom;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        if(isNeedAnimation()){
            animation(child);
        }
    }

    public void pullDown(View child, int y){
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
        ta.setDuration(300);
        child.startAnimation(ta);
        // 设置回到正常的布局位置
        child.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();// 清空矩形
    }

    public void animation2(final View child) {
        if(originHeight == -1) {
            if(animator != null && (animator.isStarted() || animator.isRunning())){
                animator.end();
            }
            originHeight = child.getHeight();
            ValueAnimator animator = ValueAnimator.ofInt(child.getHeight(), 0);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final ViewGroup.LayoutParams lp = child.getLayoutParams();
                    lp.height = (int) animation.getAnimatedValue();
                    child.setLayoutParams(lp);
                }
            });

            animator.start();
            this.animator = animator;
        }
        // 设置回到正常的布局位置

//        normal.setEmpty();// 清空矩形
    }

    public void animation3(final View child){
        if(originHeight != -1) {
            if(animator != null && (animator.isStarted() || animator.isRunning())){
                animator.end();
            }
            ValueAnimator animator = ValueAnimator.ofInt(0, originHeight);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final ViewGroup.LayoutParams lp = child.getLayoutParams();
                    lp.height = (int) animation.getAnimatedValue();
                    child.setLayoutParams(lp);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    originHeight = -1;
                }
            });
            animator.start();
            this.animator = animator;
        }
    }

    public boolean needPullDown(int dyUnConsumed){
        return dyUnConsumed < 0;
    }
}

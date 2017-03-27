package com.example.abwbw.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 实现了可以有下拉弹回的ScrollView的自定义View
 */
public class SpringScrollView extends NestedScrollView {
    private static final int DEFAULT_TOUCH_ACTION = -1;

    private View mPullDwonView;// 孩子

    private float y;// 坐标

    private boolean isInitY;

    private Rect normal = new Rect();// 矩形空白

    public SpringScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /***
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mPullDwonView = getChildAt(0);// 获取其孩子
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mPullDwonView != null) {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /***
     * 触摸事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();// 获取点击y坐标
                isInitY = true;
                Log.i("abwbw32","down y:" + y);
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                isInitY = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isInitY){
                    y = ev.getY();
                    isInitY = true;
                }
                final float preY = y;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY);// 获取滑动距离

                y = nowY;
                Log.i("abwbw32","move preY:" + preY + " nowY:" + nowY + " deltaY:" + deltaY);
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isUp(deltaY) && isNeedMove()) {
                    if (normal.isEmpty()) {
                        // 填充矩形，目的：就是告诉this:我现在已经有了，你松开的时候记得要执行回归动画.
                        normal.set(mPullDwonView.getLeft(), mPullDwonView.getTop(),
                                mPullDwonView.getRight(), mPullDwonView.getBottom());
                    }
                    // 移动布局
                    mPullDwonView.layout(mPullDwonView.getLeft(), mPullDwonView.getTop() - deltaY / 2,
                            mPullDwonView.getRight(), mPullDwonView.getBottom() - deltaY / 2);

                    Log.i("abwbw32","move layout");
                }
                break;

            default:
                break;
        }
    }


    public boolean isUp(int moveY){
        return moveY < 0;
    }

    @Override
    public boolean startNestedScroll(int axes) {
        Log.i("abwbw32","startNestedScroll");
        return super.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        Log.i("abwbw32","stopNestedScroll");
        super.stopNestedScroll();
    }

    /***
     * 开启动画移动
     */
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, mPullDwonView.getTop(),
                normal.top);
        ta.setDuration(300);
        mPullDwonView.startAnimation(ta);
        // 设置回到正常的布局位置
        mPullDwonView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();// 清空矩形
    }

    /***
     * 是否需要开启动画
     * <p>
     * 如果矩形不为空，返回true，否则返回false.
     *
     * @return
     */
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * 是否需要移动布局 mPullDwonView.getMeasuredHeight():获取的是控件的高度
     * getHeight()：获取的是当前控件在屏幕中显示的高度
     *
     * @return
     */
    public boolean isNeedMove() {
        int offset = mPullDwonView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0是顶部，后面那个是底部
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

}
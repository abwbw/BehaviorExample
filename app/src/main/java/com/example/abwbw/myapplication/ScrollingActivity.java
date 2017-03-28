package com.example.abwbw.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    private ViewPager mContentVp;
    private NestedScrollView mHeadNsv;

    private ValueAnimator mHeadAnim;

    private Toolbar mToolbar;

    private int mHeadHeight;
    private int mToolbarHeight;

    private boolean mIsShrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        findView();
        initView();
    }

    private void findView(){
        mContentVp = (ViewPager) findViewById(R.id.content_vp);
        mHeadNsv = (NestedScrollView) findViewById(R.id.head_nsv);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initView(){
        mContentVp.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        setSupportActionBar(mToolbar);
        mHeadNsv.post(new Runnable() {
            @Override
            public void run() {
                mHeadHeight = mHeadNsv.getHeight();
            }
        });

        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                mToolbarHeight = mToolbar.getHeight();
            }
        });
    }

    private void shrinkHeadView(){
        if(mIsShrink){
            return;
        }

        if(mHeadAnim != null && (mHeadAnim.isRunning() || mHeadAnim.isStarted())){
            mHeadAnim.cancel();
        }

        mHeadAnim = ValueAnimator.ofInt(mHeadHeight, mToolbarHeight);

        mHeadAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final ViewGroup.LayoutParams lp = mHeadNsv.getLayoutParams();
                lp.height = (int) animation.getAnimatedValue();
                mHeadNsv.setLayoutParams(lp);

            }
        });

        mHeadAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsShrink = true;

            }
        });

        mHeadAnim.setDuration(300);

        mHeadAnim.start();


    }

    private void expandHeadView(){
        if(!mIsShrink){
            return;
        }

        if(mHeadAnim != null && (mHeadAnim.isRunning() || mHeadAnim.isStarted())){
            mHeadAnim.cancel();
        }

        mHeadAnim = ValueAnimator.ofInt(mToolbarHeight, mHeadHeight);

        mHeadAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final ViewGroup.LayoutParams lp = mHeadNsv.getLayoutParams();
                lp.height = (int) animation.getAnimatedValue();
                mHeadNsv.setLayoutParams(lp);
            }
        });

        mHeadAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsShrink = false;

            }
        });

        mHeadAnim.setDuration(300);

        mHeadAnim.start();


    }

    private class FragmentAdapter extends FragmentPagerAdapter implements ContentFragment.ScrollListener{

        private List<ContentFragment> mFraments = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm){
            super(fm);
            mFraments.add(new ContentFragment());
            mFraments.add(new ContentFragment());
            mFraments.add(new ContentFragment());

            ContentFragment.ScrollObserver.getObserver().setListener(this);
        }

        @Override
        public Fragment getItem(int position) {
            return mFraments.get(position);
        }

        @Override
        public int getCount() {
            return mFraments.size();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

            final int fistPosition = manager.findFirstVisibleItemPosition();

            if(fistPosition > 0){
//                shrinkHeadView();
                Log.i("abwbw58","shrinkHeadView");
            } else if(fistPosition == 0){
//                expandHeadView();
                Log.i("abwbw58","expandHeadView");
            }
        }
    }

}

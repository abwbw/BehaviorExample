package com.example.abwbw.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    private ViewPager mContentVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        findView();
        initView();
    }

    private void findView(){
        mContentVp = (ViewPager) findViewById(R.id.content_vp);
    }


    private void initView(){
        mContentVp.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
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
            Log.i("abwbw57","onScrolled:" + dy);


        }
    }

}

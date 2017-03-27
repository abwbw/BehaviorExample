package com.example.abwbw.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor wangbinwei
 * @since 2017/3/27 下午12:30
 */

public class ContentFragment extends Fragment {
    private RecyclerView mContentRv;
    private List<ContentData> mContentData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_fragment, container, false);

        mContentRv = (RecyclerView) contentView.findViewById(R.id.content_rv);
        mContentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentRv.setAdapter(new ContentAdapter());
        initData();

        return contentView;
    }

    private void initData(){
        for(int i=0,size = 100;i<size;i++){
            mContentData.add(new ContentData("this is " + i));
        }
    }

    private class ContentData{
        private String text;

        public ContentData(String text){
            this.text = text;
        }

        public String getText(){
            return text;
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder{
        private TextView contentTv;

        public ContentHolder(View itemView) {
            super(itemView);
            contentTv = (TextView) itemView.findViewById(R.id.content_tv);
        }

        public void update(ContentData data){
            contentTv.setText(data.getText());
        }


    }

    private class ContentAdapter extends RecyclerView.Adapter<ContentHolder>{

        @Override
        public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View layout = LayoutInflater.from(getContext()).inflate(R.layout.view_holder_layout, parent, false);
            return new ContentHolder(layout);
        }

        @Override
        public void onBindViewHolder(ContentHolder holder, int position) {
            holder.update(mContentData.get(position));
        }

        @Override
        public int getItemCount() {
            return mContentData.size();
        }
    }
}

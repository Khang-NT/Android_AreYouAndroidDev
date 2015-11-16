package com.hasbrain.areyouandroiddev;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;
import android.widget.ListView;

import com.hasbrain.areyouandroiddev.model.ListPostAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

public class PostListActivity extends BasePostListActivity {

    private ListPostAdapter mAdapter;
    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_list;
    }

    @Override
    protected void assignView() {
        super.assignView();
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        mAdapter = new ListPostAdapter(this, isLandscape, this);
    }

    @Override
    protected void initView() {
        super.initView();
        if (!isLandscape) {
            ListView mListView = (ListView) findViewById(R.id.list);
            mListView.setAdapter(mAdapter);
        } else {
            GridView gridView = (GridView) findViewById(R.id.gridview);
            gridView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void displayPostList(List<RedditPost> postList) {
        super.displayPostList(postList);
        mAdapter.setPostList(postList);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        final List<RedditPost> backup = mAdapter.getPostList();
        mAdapter.setPostList(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setPostList(backup);
                mRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}

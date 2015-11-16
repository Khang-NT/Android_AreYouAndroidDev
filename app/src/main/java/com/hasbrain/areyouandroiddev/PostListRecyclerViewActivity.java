package com.hasbrain.areyouandroiddev;

import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hasbrain.areyouandroiddev.adapter.PostListRecyclerAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

/**
 * Created by Khang (khang.neon.1997@gmail.com) on 16/11/2015/11.
 */
public class PostListRecyclerViewActivity extends BasePostListActivity {
    private static final String TAG = "PostListRecyclerViewActivity";

    private PostListRecyclerAdapter mAdapter;
    private boolean isLandscape;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_list_recyclerview;
    }

    @Override
    protected void assignView() {
        super.assignView();
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mAdapter = new PostListRecyclerAdapter(this, isLandscape);
    }

    @Override
    protected void initView() {
        super.initView();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(isLandscape ?
                new GridLayoutManager(this, 3) : new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
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

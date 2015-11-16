package com.hasbrain.areyouandroiddev;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hasbrain.areyouandroiddev.model.ExpandableRecyclerAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Khang (khang.neon.1997@gmail.com) on 16/11/2015.
 */
public class PostListInSectionRecyclerActivity extends BasePostListActivity {
    private static final String STICKY_HEADER = "Sticky posts", NORMAL_HEADER = "Normal posts", BOTTOM = "bottom";

    private ExpandableRecyclerAdapter mAdapter;

    @Override
    protected void assignView() {
        super.assignView();
        List<String> headerNames = new ArrayList<>();
        headerNames.add(STICKY_HEADER);
        headerNames.add(NORMAL_HEADER);
        headerNames.add(BOTTOM);
        mAdapter = new ExpandableRecyclerAdapter(headerNames, null, this);
    }

    @Override
    protected void initView() {
        super.initView();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_in_section_recyclerview;
    }

    @Override
    protected void displayPostList(List<RedditPost> postList) {
        List<RedditPost> stickyPosts = new ArrayList<>(),
                normalPosts = new ArrayList<>();
        for (RedditPost post : postList) {
            if (post.isStickyPost())
                stickyPosts.add(post);
            else
                normalPosts.add(post);
        }
        HashMap<String, List<RedditPost>> dataList = mAdapter.getListDataChild();
        dataList.put(STICKY_HEADER, stickyPosts);
        dataList.put(NORMAL_HEADER, normalPosts);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}

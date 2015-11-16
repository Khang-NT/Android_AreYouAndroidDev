package com.hasbrain.areyouandroiddev;

import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.hasbrain.areyouandroiddev.adapter.ListPostInSectionAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/9/15.
 */
public class PostInSectionActivity extends BasePostListActivity {
    private static final String STICKY_HEADER = "Sticky posts", NORMAL_HEADER = "Normal posts", BOTTOM = "bottom";

    private ListPostInSectionAdapter mAdapter;

    @Override
    protected void assignView() {
        super.assignView();
        List<String> headerNames = new ArrayList<>();
        headerNames.add(STICKY_HEADER);
        headerNames.add(NORMAL_HEADER);
        headerNames.add(BOTTOM);
        mAdapter = new ListPostInSectionAdapter(this, headerNames, null, this);
    }

    @Override
    protected void initView() {
        super.initView();
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.elv_explandablelist);
        expandableListView.setAdapter(mAdapter);
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
    protected int getLayoutResource() {
        return R.layout.activity_post_in_section;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_in_section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, PostListInSectionRecyclerActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}

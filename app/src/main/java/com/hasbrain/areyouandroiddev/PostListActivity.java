package com.hasbrain.areyouandroiddev;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import com.hasbrain.areyouandroiddev.adapter.ListPostAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

public class PostListActivity extends BasePostListActivity {

    private ListPostAdapter mAdapter;
    private boolean isLandscape;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.postlistinsection:
                intent = new Intent(this, PostInSectionActivity.class);
                startActivity(intent);
                break;
            case R.id.use_recyclerview:
                intent = new Intent(this, PostListRecyclerViewActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

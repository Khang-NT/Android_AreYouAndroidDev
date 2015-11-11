package com.hasbrain.areyouandroiddev;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.FileBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.ListPostAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PostListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ListPostAdapter.OnItemClick {

    public static final String DATA_JSON_FILE_NAME = "data.json";
    private FeedDataStore feedDataStore;

    SwipeRefreshLayout mSwipeRefresh;
    ListView mListView;
    ListPostAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        InitViews();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        Gson gson = gsonBuilder.create();
        InputStream is = null;
        try {
            is = getAssets().open(DATA_JSON_FILE_NAME);
            feedDataStore = new FileBasedFeedDataStore(gson, is);
            feedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
                @Override
                public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                    displayPostList(postList);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void displayPostList(List<RedditPost> postList) {
        mAdapter.setPostList(postList);
    }

    protected int getLayoutResource() {
        return R.layout.activity_post_list;
    }

    protected void InitViews() {
        mListView = (ListView) findViewById(R.id.list);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mAdapter = new ListPostAdapter(this);

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.YELLOW);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClick(this);
    }

    @Override
    public void onRefresh() {
        final List<RedditPost> backup = mAdapter.getPostList();
        mAdapter.setPostList(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setPostList(backup);
                mSwipeRefresh.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onItemClick(int position, String url) {
        Toast.makeText(PostListActivity.this, "Go to url: " + url, Toast.LENGTH_SHORT).show();
        Intent postactivity = new Intent(this, PostViewActivity.class);
        postactivity.putExtra("URL", url);
        startActivity(postactivity);
    }
}

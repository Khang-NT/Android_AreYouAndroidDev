package com.hasbrain.areyouandroiddev;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.FileBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.OnItemClick;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BasePostListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClick {

    public static final String DATA_JSON_FILE_NAME = "data.json";

    protected boolean isLandscape;
    protected SwipeRefreshLayout mRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(getLayoutResource());

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        assignView();
        initView();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        Gson gson = gsonBuilder.create();
        InputStream is = null;
        try {
            is = getAssets().open(DATA_JSON_FILE_NAME);
            FeedDataStore feedDataStore = new FileBasedFeedDataStore(gson, is);
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

    }

    protected int getLayoutResource() {
        return R.layout.activity_post_list;
    }

    protected void assignView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
    }

    protected void initView() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onItemClick(String url) {
        Toast.makeText(this, "Go to url: " + url, Toast.LENGTH_SHORT).show();
        Intent postactivity = new Intent(this, PostViewActivity.class);
        postactivity.putExtra("URL", url);
        startActivity(postactivity);
    }


    @Override
    public void onRefresh() {

    }
}

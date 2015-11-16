package com.hasbrain.areyouandroiddev;

import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/9/15.
 */
public class PostInSectionActivity extends BasePostListActivity {

    @Override
    protected void displayPostList(List<RedditPost> postList) {
        //TODO: Display posts in sections.
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_in_section;
    }
}

package com.hasbrain.areyouandroiddev.adapter;

import android.view.View;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.Utils;

public class ViewHolder {
    private TextView score, author, title, comment;

    public ViewHolder(View root) {
        score = (TextView) root.findViewById(R.id.tv_score);
        author = (TextView) root.findViewById(R.id.tv_author_subreddit);
        title = (TextView) root.findViewById(R.id.tv_title);
        comment = (TextView) root.findViewById(R.id.tv_comment_domain_createdutc);
    }

    public void bindData(RedditPost data) {
        Utils.bindData(data, score, author, title, comment);
    }
}
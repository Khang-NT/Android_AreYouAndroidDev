package com.hasbrain.areyouandroiddev.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.OnItemClick;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.Utils;

import java.util.List;

/**
 * Created by Khang (khang.neon.1997@gmail.com) on 16/11/2015.
 */
public class PostListRecyclerAdapter extends RecyclerView.Adapter<PostListRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "PostListRecyclerAdapter";
    private static final int TYPE_COUNT = 2, DEFAULT_ITEM = 0, BOTTOM_ITEM = 1;

    private OnItemClick callBack;
    private List<RedditPost> mPostList;
    private boolean isLandscape;
    private int lastAnimatePosition = -1;

    public PostListRecyclerAdapter(OnItemClick callBack, boolean isLandscape) {
        this.callBack = callBack;
        this.isLandscape = isLandscape;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                viewType == DEFAULT_ITEM ? R.layout.list_item : R.layout.bottom_item,
                parent, false
        );
        v.findViewById(R.id.view_clickable).setOnClickListener(this);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RedditPost data = getItem(position);
        if (data != null) {
            holder.findTextView(R.id.tv_score).setText(data.getScore() + "");
            holder.findTextView(R.id.tv_author_subreddit).setText(Utils.buildAuthorAndSubredditText(
                    data.getAuthor(),
                    data.getSubreddit(),
                    isLandscape
            ));
            holder.findTextView(R.id.tv_title).setText(Utils.buildTitleText(
                    data.getTitle(),
                    data.isStickyPost(),
                    isLandscape
            ));
            holder.findTextView(R.id.tv_comment_domain_createdutc).setText(Utils.buildCommentDomainCreatedtimeText(
                    data.getCommentCount(),
                    data.getDomain(),
                    data.getCreatedUTC()
            ));
        }

        holder.findViewById(R.id.view_clickable).setTag(position);

        if (!isLandscape && position > lastAnimatePosition && position < 20) {
            lastAnimatePosition = position;
            holder.findViewById(R.id.container).startAnimation(
                    AnimationUtils.loadAnimation(holder.root.getContext(), R.anim.slide_in)
            );
        }
    }

    public RedditPost getItem(int position) {
        return mPostList == null ||
                position == getItemCount() - 1 ?
                null : mPostList.get(position);
    }

    @Override
    public int getItemCount() {
        return mPostList == null ? 0 : mPostList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? BOTTOM_ITEM : DEFAULT_ITEM;
    }

    public void setPostList(List<RedditPost> mPostList) {
        this.mPostList = mPostList;
        lastAnimatePosition = -1;
        notifyDataSetChanged();
    }

    public List<RedditPost> getPostList() {
        return mPostList;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        final RedditPost data = getItem(position);
        final String url = (data == null) ?
                v.getContext().getString(R.string.def_url) : data.getUrl();
        callBack.onItemClick(url);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        public ViewHolder(View v) {
            super(v);
            this.root = v;
        }

        public TextView findTextView(int id) {
            View v = findViewById(id);
            return v == null ?
                    null : (TextView) v;
        }

        public View findViewById(int id) {
            return root.findViewById(id);
        }
    }
}

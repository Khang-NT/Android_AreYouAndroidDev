package com.hasbrain.areyouandroiddev.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;

import java.util.List;

public class ListPostAdapter extends ArrayAdapter<RedditPost> {
    private static final String TAG = "ListPostAdapter";
    private static final int TYPE_COUNT = 2, DEFAULT_ITEM = 0, BOTTOM_ITEM = 1;

    LayoutInflater inflater;
    List<RedditPost> mPostList;
    boolean[] animationStates;
    boolean isLandscape;

    public ListPostAdapter(Context context, boolean isLandscape) {
        this(context, isLandscape, null);
    }

    public ListPostAdapter(Context context, boolean isLandscape, List<RedditPost> postList) {
        super(context, 0);
        this.isLandscape = isLandscape;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setPostList(postList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowview = convertView;
        final RedditPost data = getItem(position);

        int type = getItemViewType(position);
        if (rowview == null) {
            rowview = inflater.inflate(type == DEFAULT_ITEM ? R.layout.list_item : R.layout.bottom_item,
                    null);
            rowview.findViewById(R.id.view_clickable).setOnClickListener(mViewClickListener);

            if (type == DEFAULT_ITEM) {
                final ViewHolder viewHolder = new ViewHolder(
                        (TextView) rowview.findViewById(R.id.tv_score),
                        (TextView) rowview.findViewById(R.id.tv_author_subreddit),
                        (TextView) rowview.findViewById(R.id.tv_title),
                        (TextView) rowview.findViewById(R.id.tv_comment_domain_createdutc)
                );

                rowview.setTag(viewHolder);
            }

            int sumHeight = sumChildrenHeight(parent);

            if (!isLandscape && !animationStates[position] && sumHeight <= parent.getHeight()) {
                animationStates[position] = true;
                rowview.animate().alpha(0).translationYBy(80f).setDuration(0).start();
                rowview.animate().alpha(1).translationYBy(-80f).setDuration(800)
                        .setInterpolator(new DecelerateInterpolator())
                        .setStartDelay(position == getCount() - 1 ?
                                0 : position * 200)
                        .start();
            }
        }

        if (type == DEFAULT_ITEM && data != null) {
            ViewHolder viewHolder = (ViewHolder) rowview.getTag();
            viewHolder.score.setText(data.getScore() + "");
            viewHolder.author.setText(Utils.buildAuthorAndSubredditText(
                    data.getAuthor(),
                    data.getSubreddit(),
                    isLandscape
            ));
            viewHolder.title.setText(Utils.buildTitleText(
                    data.getTitle(),
                    data.isStickyPost(),
                    isLandscape
            ));
            viewHolder.comment.setText(Utils.buildCommentDomainCreatedtimeText(
                    data.getCommentCount(),
                    data.getDomain(),
                    data.getCreatedUTC()
            ));
        }

        rowview.findViewById(R.id.view_clickable).setTag(position);

        return rowview;
    }

    @Override
    public RedditPost getItem(int position) {
        return mPostList == null ||
                position == getCount() - 1 ?
                null : mPostList.get(position);
    }

    @Override
    public int getCount() {
        return mPostList == null ? 0 : mPostList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() - 1 ? BOTTOM_ITEM : DEFAULT_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    public void setPostList(List<RedditPost> mPostList) {
        this.mPostList = mPostList;
        int count = getCount();
        animationStates = new boolean[count];
        notifyDataSetChanged();
    }

    public List<RedditPost> getPostList() {
        return mPostList;
    }


    private class ViewHolder {
        TextView score, author, title, comment;

        public ViewHolder(TextView core, TextView author, TextView title, TextView comment) {
            this.score = core;
            this.author = author;
            this.title = title;
            this.comment = comment;
        }
    }

    int sumChildrenHeight(ViewGroup v) {
        int total = 0;
        for (int i = 0; i < v.getChildCount(); i++)
            total += v.getChildAt(i).getHeight();
        return total;
    }

    private View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClick != null) {  //// CALLBACK
                int position = (int) v.getTag();
                final RedditPost data = getItem(position);
                final String url = (data == null) ?
                        inflater.getContext().getString(R.string.def_url) : data.getUrl();
                mOnItemClick.onItemClick(url);
            }
        }
    };

    OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

}

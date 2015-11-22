package com.hasbrain.areyouandroiddev.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;

import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.OnItemClick;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

public class ListPostAdapter extends ArrayAdapter<RedditPost> implements View.OnClickListener {
    private static final String TAG = "ListPostAdapter";
    private static final int TYPE_COUNT = 2, DEFAULT_ITEM = 0, BOTTOM_ITEM = 1;

    LayoutInflater inflater;
    List<RedditPost> mPostList;
    boolean[] animationStates;
    boolean isLandscape;
    OnItemClick callback;

    public ListPostAdapter(Context context, boolean isLandscape, OnItemClick callback) {
        this(context, isLandscape, callback, null);
    }

    public ListPostAdapter(Context context, boolean isLandscape, @NonNull OnItemClick callback, List<RedditPost> postList) {
        super(context, 0);
        this.isLandscape = isLandscape;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback = callback;
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
            rowview.findViewById(R.id.view_clickable).setOnClickListener(this);

            if (type == DEFAULT_ITEM) {
                final ViewHolder viewHolder = new ViewHolder(rowview);

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
            viewHolder.bindData(data);
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


    int sumChildrenHeight(ViewGroup v) {
        int total = 0;
        for (int i = 0; i < v.getChildCount(); i++)
            total += v.getChildAt(i).getHeight();
        return total;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        final RedditPost data = getItem(position);
        final String url = (data == null) ?
                inflater.getContext().getString(R.string.def_url) : data.getUrl();
        callback.onItemClick(url);
    }
}

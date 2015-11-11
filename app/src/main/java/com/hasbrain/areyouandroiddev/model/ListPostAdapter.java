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
    private static final int TYPECOUNT = 2, DEFAULT_ITEM = 0, BOTTOM_ITEM = 1;

    LayoutInflater inflater;
    List<RedditPost> mPostList;
    boolean[] animationStates;

    public ListPostAdapter(Context context) {
        this(context, null);
    }

    public ListPostAdapter(Context context, List<RedditPost> postList) {
        super(context, 0);
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
                TextView tv = (TextView) rowview.findViewById(R.id.tv_content);
                tv.setText(Utils.HtmlFactory(data));
                rowview.setTag(tv);
            }

            int ttH = totalHeight(parent);

            if (!animationStates[position] && ttH <= parent.getHeight()) {
                animationStates[position] = true;
                rowview.animate().alpha(0).translationYBy(80f).setDuration(0).start();
                rowview.animate().alpha(1).translationYBy(-80f).setDuration(800)
                        .setInterpolator(new DecelerateInterpolator())
                        .setStartDelay(position == getCount() - 1 ?
                                0 : position * 200)
                        .start();
            }
        } else if (type == DEFAULT_ITEM) {
            TextView tv = (TextView) rowview.getTag();
            tv.setText(Utils.HtmlFactory(data));
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
        return TYPECOUNT;
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

    int totalHeight(ViewGroup v) {
        int total = 0;
        for (int i = 0; i < v.getChildCount(); i++)
            total += v.getChildAt(i).getHeight();
        return total;
    }

    private View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClick != null) {
                int position = (int) v.getTag();
                final RedditPost data = getItem(position);
                final String url = data == null ? inflater.getContext().getString(R.string.def_url) : data.getUrl();
                mOnItemClick.onItemClick(position, url);
            }
        }
    };

    OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    public interface OnItemClick {
        void onItemClick(int position, String url);
    }
}

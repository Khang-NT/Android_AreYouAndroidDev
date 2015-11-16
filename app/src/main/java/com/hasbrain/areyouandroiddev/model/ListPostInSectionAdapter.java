package com.hasbrain.areyouandroiddev.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;

import java.util.HashMap;
import java.util.List;


public class ListPostInSectionAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private static final String TAG = "ListPostInSectionAdapter";
    private static final int GROUP_TYPE_COUNT = 2, TYPE_DEFAULT = 0, TYPE_BOTTOM_VIEW = 1;

    private Context context;
    private List<String> groupName; // header titles
    private HashMap<String, List<RedditPost>> listDataChild;
    private OnItemClick callback;

    public ListPostInSectionAdapter(Context context, List<String> groupName, HashMap<String, List<RedditPost>> listDataChild,
                                    @NonNull OnItemClick callback) {
        this.context = context;
        this.groupName = groupName;
        this.listDataChild = listDataChild;
        this.callback = callback;
    }


    @Override
    public int getGroupCount() {
        return groupName.size();
    }

    @Override
    public int getGroupTypeCount() {
        return GROUP_TYPE_COUNT;
    }

    @Override
    public int getGroupType(int groupPosition) {
        return groupPosition == groupName.size() - 1 ?
                TYPE_BOTTOM_VIEW : TYPE_DEFAULT;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<RedditPost> posts = listDataChild.get(groupName.get(groupPosition));
        return posts == null ? 0 : posts.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupName.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<RedditPost> posts = listDataChild.get(groupName.get(groupPosition));
        return posts == null ?
                null : posts.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        int type = getGroupType(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type == TYPE_DEFAULT)
                convertView = infalInflater.inflate(R.layout.list_group, null);
            else {
                convertView = infalInflater.inflate(R.layout.bottom_item, null);
                convertView.findViewById(R.id.view_clickable).setTag(new int[]{groupPosition, 0});
                convertView.findViewById(R.id.view_clickable).setOnClickListener(this);
            }
        }
        if (type == TYPE_DEFAULT) {
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final RedditPost data = (RedditPost) getChild(groupPosition, childPosition);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, null);
            convertView.findViewById(R.id.view_clickable).setOnClickListener(this);

            viewHolder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.tv_score),
                    (TextView) convertView.findViewById(R.id.tv_author_subreddit),
                    (TextView) convertView.findViewById(R.id.tv_title),
                    (TextView) convertView.findViewById(R.id.tv_comment_domain_createdutc)
            );
            convertView.setTag(viewHolder);
        }

        if (viewHolder == null)
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.score.setText(data.getScore() + "");
        viewHolder.author.setText(Utils.buildAuthorAndSubredditText(
                data.getAuthor(),
                data.getSubreddit(),
                false
        ));
        viewHolder.title.setText(Utils.buildTitleText(
                data.getTitle(),
                data.isStickyPost(),
                false
        ));
        viewHolder.comment.setText(Utils.buildCommentDomainCreatedtimeText(
                data.getCommentCount(),
                data.getDomain(),
                data.getCreatedUTC()
        ));
        convertView.findViewById(R.id.view_clickable).setTag(new int[]{groupPosition, childPosition});
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public HashMap<String, List<RedditPost>> getListDataChild() {
        return listDataChild;
    }

    @Override
    public void onClick(View v) {
        int[] post_index = (int[]) v.getTag();
        final RedditPost data = (RedditPost) getChild(post_index[0], post_index[1]);
        final String url = (data == null) ?
                context.getString(R.string.def_url) : data.getUrl();
        callback.onItemClick(url);
    }
}

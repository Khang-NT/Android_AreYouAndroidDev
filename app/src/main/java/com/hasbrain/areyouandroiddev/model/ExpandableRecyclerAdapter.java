package com.hasbrain.areyouandroiddev.model;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hasbrain.areyouandroiddev.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Khang (khang.neon.1997@gmail.com) on 16/11/2015.
 */
public class ExpandableRecyclerAdapter extends BaseExpandableRecyclerAdapter {
    private static final String TAG = "ExpandableRecyclerAdapter";
    private static final int GROUPTYPE_DEFAULT = 0, GROUPTYPE_BOTTOM_VIEW = 1, ITEMTYPEDEFAULT = 2;
    private OnItemClick callback;
    private HashMap<String, List<RedditPost>> listDataChild;
    private List<String> groupName;

    public ExpandableRecyclerAdapter(@NonNull List<String> groupName, HashMap<String, List<RedditPost>> listDataChild,
                                     @NonNull OnItemClick callback) {
        this.groupName = groupName;
        if (listDataChild != null)
            this.listDataChild = listDataChild;
        else
            this.listDataChild = new HashMap<>();
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateView(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root;
        switch (viewType) {
            case GROUPTYPE_DEFAULT:
                root = inflater.inflate(R.layout.list_group, parent, false);
                break;
            case GROUPTYPE_BOTTOM_VIEW:
                root = inflater.inflate(R.layout.bottom_item, parent, false);
                break;
            default:
                root = inflater.inflate(R.layout.list_item, parent, false);
        }
        return new ViewHolder(root);
    }

    @Override
    public void onBindGroup(ViewHolder viewHolder, int groupId) {
        if (groupId < getGroupCount() - 1) {
            viewHolder.findTextView(R.id.lblListHeader).setText(
                    groupName.get(groupId)
            );
        }
    }

    @Override
    protected void onBindChild(ViewHolder holder, int groupId, int childId) {
        RedditPost data = listDataChild.get(groupName.get(groupId)).get(childId);
        if (data != null) {
            holder.findTextView(R.id.tv_score).setText(data.getScore() + "");
            holder.findTextView(R.id.tv_author_subreddit).setText(Utils.buildAuthorAndSubredditText(
                    data.getAuthor(),
                    data.getSubreddit(),
                    false
            ));
            holder.findTextView(R.id.tv_title).setText(Utils.buildTitleText(
                    data.getTitle(),
                    data.isStickyPost(),
                    false
            ));
            holder.findTextView(R.id.tv_comment_domain_createdutc).setText(Utils.buildCommentDomainCreatedtimeText(
                    data.getCommentCount(),
                    data.getDomain(),
                    data.getCreatedUTC()
            ));
        }
    }

    @Override
    public int getGroupType(int groupId) {
        return groupId == getGroupCount() - 1 ? GROUPTYPE_BOTTOM_VIEW : GROUPTYPE_DEFAULT;
    }

    @Override
    public int getChildType(int groupId, int childId) {
        return ITEMTYPEDEFAULT;
    }

    @Override
    public int getIDViewOnHeaderClickable(int groupId) {
        return R.id.view_clickable;
    }

    @Override
    public int getIDViewOnItemClickable(int groupId, int childId) {
        return R.id.view_clickable;
    }

    @Override
    public int getGroupCount() {
        return groupName.size();
    }

    @Override
    public int getChildCount(int groupId) {
        List<RedditPost> posts = listDataChild.get(groupName.get(groupId));
        return posts == null ? 0 : posts.size();
    }

    @Override
    protected void onGroupCollapsed(View groupView, int groupId) {
        super.onGroupCollapsed(groupView, groupId);
        if (groupId != getGroupCount() - 1)
            groupView.findViewById(R.id.devider).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onGroupExpanded(View groupView, int groupId) {
        super.onGroupExpanded(groupView, groupId);
        if (groupId != getGroupCount() - 1)
            groupView.findViewById(R.id.devider).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onGroupItemClicked(View itemView, int groupId, int childId) {
        super.onGroupItemClicked(itemView, groupId, childId);
        RedditPost data = listDataChild.get(groupName.get(groupId)).get(childId);
        if (data != null && callback != null)
            callback.onItemClick(data.getUrl());
    }

    public HashMap<String, List<RedditPost>> getListDataChild() {
        return listDataChild;
    }
}

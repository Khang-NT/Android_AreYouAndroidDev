package com.hasbrain.areyouandroiddev.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Khang (khang.neon.1997@gmail.com) on 16/11/2015.
 */
public abstract class BaseExpandableRecyclerAdapter extends RecyclerView.Adapter<BaseExpandableRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "BaseExpandableRecyclerAdapter";

    HashMap<Integer, Boolean> isExpanded;


    public BaseExpandableRecyclerAdapter() {
        isExpanded = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateView(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int[] groupAndChildIndex = convertPositonToGroupIndex(position);

        if (groupAndChildIndex[1] == -1)
            onBindGroup(holder, groupAndChildIndex[0]);
        else
            onBindChild(holder, groupAndChildIndex[0], groupAndChildIndex[1]);


        int IdViewClickable = groupAndChildIndex[1] == -1 ?
                getIDViewOnHeaderClickable(groupAndChildIndex[0]) :
                getIDViewOnItemClickable(groupAndChildIndex[0], groupAndChildIndex[1]);
        if (IdViewClickable != 0) {
            holder.findViewById(IdViewClickable).setOnClickListener(this);
            holder.findViewById(IdViewClickable).setTag(groupAndChildIndex);
        }
    }

    @Override
    public int getItemCount() {
        int groupcount = getGroupCount();
        int childcount = 0;
        for (int i = 0; i < groupcount; i++)
            childcount += isExpanded(i) ? getChildCount(i) : 0;
        return groupcount + childcount;
    }

    @Override
    public int getItemViewType(int position) {
        int[] groupAndChildIndex = convertPositonToGroupIndex(position);
        if (groupAndChildIndex[1] == -1)
            return getGroupType(groupAndChildIndex[0]);
        else
            return getChildType(groupAndChildIndex[0], groupAndChildIndex[1]);
    }

    @Override
    public void onClick(View v) {
        int[] groupAndChildIndex = (int[]) v.getTag();
        int groupId = groupAndChildIndex[0], childId = groupAndChildIndex[1];
        if (childId == -1) {
            boolean expand = !isExpanded(groupId);
            isExpanded.put(groupId, expand);

            if (expand) {
                if (getChildCount(groupId) > 0)
                    notifyItemRangeInserted(getGroupFirstIndex(groupId) + 1, getChildCount(groupId));
                onGroupExpanded(v, groupId);
            } else {
                if (getChildCount(groupId) > 0)
                    notifyItemRangeRemoved(getGroupFirstIndex(groupId) + 1, getChildCount(groupId));
                onGroupCollapsed(v, groupId);
            }
        } else {
            onGroupItemClicked(v, groupId, childId);
        }
    }

    private int[] convertPositonToGroupIndex(int position) {
        int groupId = 0, childId = -1, tmp = 0;
        while (true) {
            if (position == tmp) break;
            int childcount = isExpanded(groupId) ? getChildCount(groupId) : 0;
            if (tmp + childcount >= position) {
                childId = position - tmp - 1;
                break;
            }
            groupId++;
            tmp += 1 + childcount;
        }
        return new int[]{groupId, childId};
    }

    private int getGroupFirstIndex(int groupId) {
        if (groupId == 0)
            return 0;
        else
            return getGroupFirstIndex(groupId - 1) + 1
                    + (isExpanded(groupId - 1) ? getChildCount(groupId - 1) : 0);
    }

    public final boolean isExpanded(int groupId) {
        if (isExpanded.containsKey(groupId))
            return isExpanded.get(groupId);
        else
            return true;
    }


    //Abstract Method

    public abstract ViewHolder onCreateView(ViewGroup parent, int viewType);

    public abstract void onBindGroup(ViewHolder viewHolder, int groupId);

    protected abstract void onBindChild(ViewHolder viewHolder, int groupId, int childId);

    /*
        Both Group's type and Child's type must unique together
    */
    public abstract int getGroupType(int groupId);

    /*
        Both Group's type and Child's type must unique together
    */
    public abstract int getChildType(int groupId, int childId);

    public abstract int getIDViewOnHeaderClickable(int groupId);

    public abstract int getIDViewOnItemClickable(int groupId, int childId);

    public abstract int getGroupCount();

    public abstract int getChildCount(int groupId);

    protected void onGroupExpanded(View groupView, int groupId) {
    }

    protected void onGroupCollapsed(View groupView, int groupId) {
    }

    protected void onGroupItemClicked(View itemView, int groupId, int childId) {
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

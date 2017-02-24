package mj.wt.wtapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.bean.PhotoInfo;

/**
 * Created by wantao on 2017/2/24.
 */

public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groupArray;
    private List<List<PhotoInfo>> childArray;

    public ExpandAdapter(Context context, List<String> groupArray, List<List<PhotoInfo>> childArray) {
        this.context = context;
        this.groupArray = groupArray;
        this.childArray = childArray;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
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
        GroupHolder holder;
        if (convertView==null)
        {
            holder=new GroupHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.expand_groupview,null);
            holder.ivDrop= (ImageView) convertView.findViewById(R.id.group_drop_iv);
            holder.tvName= (TextView) convertView.findViewById(R.id.group_name);
            convertView.setTag(holder);
        }else {
            holder= (GroupHolder) convertView.getTag();
        }
        if (isExpanded)
            holder.ivDrop.setImageResource(R.mipmap.demand_drop_up);
        else
        holder.ivDrop.setImageResource(R.mipmap.demand_drop_down);
        holder.tvName.setText(groupArray.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView==null)
        {
            holder=new ChildHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.expand_childview,null);
            holder.tvNumber= (TextView) convertView.findViewById(R.id.child_number);
            holder.tvName= (TextView) convertView.findViewById(R.id.child_name);
            convertView.setTag(holder);
        }else {
            holder= (ChildHolder) convertView.getTag();
        }
        holder.tvName.setText(childArray.get(groupPosition).get(childPosition).getName());
        holder.tvNumber.setText(childArray.get(groupPosition).get(childPosition).getNumber());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
     public class GroupHolder
    {
        ImageView ivDrop;
        TextView tvName;
    }
    public class ChildHolder
    {
        TextView tvName;
        TextView tvNumber;
    }


}

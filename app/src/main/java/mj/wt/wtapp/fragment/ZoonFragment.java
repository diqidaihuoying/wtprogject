package mj.wt.wtapp.fragment;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basic.BaseFragment;
import com.example.basic.utils.SnackbarUtil;

import mj.wt.wtapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoonFragment extends BaseFragment {
    private ListView listView;
    private LinearLayout zoonView;

   @Override
    public int getLayoutResId() {
        return R.layout.fragment_zoon;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView(View parentView) {
        super.initView(parentView);
        listView= (ListView) parentView.findViewById(R.id.listview);
        listView.setAdapter(new ListAdapter());
        zoonView= (LinearLayout) parentView.findViewById(R.id.linear_zoon);
        zoonView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v==zoonView)
        {
            Toast.makeText(activity,"空间动态",Toast.LENGTH_SHORT).show();
        }
    }
    class ListAdapter extends  BaseAdapter
    {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null)
            {
                convertView= LayoutInflater.from(activity).inflate(R.layout.zoonlist_item,null);
                holder=new ViewHolder();
                holder.ivIcon= (ImageView) convertView.findViewById(R.id.item_icon);
                holder.tvName= (TextView) convertView.findViewById(R.id.item_name);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            if (position==0)
            {
                holder.ivIcon.setImageResource(R.mipmap.ic_category_5);
                holder.tvName.setText("购物商城");
            }else if (position==1)
            {
                holder.ivIcon.setImageResource(R.mipmap.ic_category_3);
                holder.tvName.setText("美女直播");
            }else if (position==2)
            {
                holder.ivIcon.setImageResource(R.mipmap.ic_category_7);
                holder.tvName.setText("内涵段子");
            }else if (position==3)
            {
                holder.ivIcon.setImageResource(R.mipmap.ic_category_2);
                holder.tvName.setText("书籍世界");
            }
            else if (position==4)
            {
                holder.ivIcon.setImageResource(R.mipmap.ic_category_6);
                holder.tvName.setText("实时新闻");
            }
            return convertView;
        }
    }
    class ViewHolder
    {
        ImageView ivIcon;
        TextView  tvName;
    }
}

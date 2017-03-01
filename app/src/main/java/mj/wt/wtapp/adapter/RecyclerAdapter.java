package mj.wt.wtapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import mj.wt.wtapp.R;

/**
 * Created by wantao on 2017/2/28.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static final int VOICE_TYPE = 1;
    private static final int VIDEO_TYPE = 2;
    private static final int PICTURE_TYPE = 3;
    private Context context;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.zone_item, null);
        MyViewHolder holder = new MyViewHolder(view);
        View layout = null;
        if (viewType == VOICE_TYPE)
            layout = LayoutInflater.from(context).inflate(R.layout.zone_voice_layout, null);
        else if (viewType == VIDEO_TYPE)
            layout = LayoutInflater.from(context).inflate(R.layout.zone_video_layout, null);
        else
            layout = LayoutInflater.from(context).inflate(R.layout.zone_picture_layout, null);
        holder.reLayout.addView(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position % 3 == 0) {
            return VOICE_TYPE;
        } else if (position % 3 == 1) {
            return VIDEO_TYPE;
        } else {
            return PICTURE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivHead;
        TextView tvName;
        TextView tvTime;
        TextView tvContent;
        RelativeLayout reLayout;
        LinearLayout linearLikeView;
        TextView tvLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivHead = (SimpleDraweeView) itemView.findViewById(R.id.iv_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            reLayout = (RelativeLayout) itemView.findViewById(R.id.layout_content);
            linearLikeView = (LinearLayout) itemView.findViewById(R.id.line_likelist);
            tvLike = (TextView) itemView.findViewById(R.id.tv_likelist);
        }
    }

}

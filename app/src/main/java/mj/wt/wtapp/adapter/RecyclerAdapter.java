package mj.wt.wtapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basic.utils.DateUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mj.wt.wtapp.ActionHelp;
import mj.wt.wtapp.R;
import mj.wt.wtapp.bean.ZoneBigPicture;

/**
 * Created by wantao on 2017/2/28.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private static final int FIRST_ITEM = 0;
    private static final int VOICE_TYPE = 1;
    private static final int VIDEO_TYPE = 2;
    private static final int PICTURE_TYPE_BIG = 3;
    private static final int PICTURE_TYPE_MIDDLE = 4;
    private static final int PICTURE_TYPE_SMALL = 5;
    private Context context;
    private LinkedList<String> likeNames = new LinkedList<>();


    private List<ZoneBigPicture.DataBean.ItemsBean> bigPictures;
    //数据源
    public RecyclerAdapter(Context context, List<ZoneBigPicture.DataBean.ItemsBean> bigPictures) {
        this.context = context;
        this.bigPictures = bigPictures;
        ActionHelp.getLikesNames(likeNames);//初始化点赞列表
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        MyViewHolder holder = null;
        View layout;
        if (viewType == FIRST_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.zone_firstitem, null);
            holder = new MyViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.zone_item, null);
            holder = new MyViewHolder(view);
            if (viewType == VOICE_TYPE)
                layout = LayoutInflater.from(context).inflate(R.layout.zone_voice_layout, null);
            else if (viewType == VIDEO_TYPE)
                layout = LayoutInflater.from(context).inflate(R.layout.zone_video_layout, null);
            else {
                layout = LayoutInflater.from(context).inflate(R.layout.zone_picture_layout, null);
                holder.gridView = (GridView) layout.findViewById(R.id.gridview);
            }
            holder.reLayout.addView(layout);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (holder.ivHead != null) {
            holder.ivHead.setImageURI(bigPictures.get(position).getCover_image_url());
        }
        if (holder.tvContent != null) {
            holder.tvContent.setText(bigPictures.get(position).getIntroduction());
        }
        if (holder.gridView != null) {

            List<String> urls = new ArrayList<>();
            if (getItemViewType(position) == PICTURE_TYPE_BIG) {
                urls.add(bigPictures.get(position).getCover_image_url());
                PictureAdapter adapter = new PictureAdapter(urls, context, PICTURE_TYPE_BIG);
                holder.gridView.setNumColumns(1);
                holder.gridView.setAdapter(adapter);
            } else if (getItemViewType(position) == PICTURE_TYPE_MIDDLE) {
                for (int i = 0; i < 4; i++) {
                    urls.add(bigPictures.get(position).getCover_image_url());
                }
                PictureAdapter adapter = new PictureAdapter(urls, context, PICTURE_TYPE_MIDDLE);
                holder.gridView.setNumColumns(2);
                holder.gridView.setAdapter(adapter);
            } else {
                for (int i = 0; i < 6; i++) {
                    urls.add(bigPictures.get(position).getCover_image_url());
                }
                PictureAdapter adapter = new PictureAdapter(urls, context, PICTURE_TYPE_SMALL);
                holder.gridView.setNumColumns(3);
                holder.gridView.setAdapter(adapter);
            }
        }
        if (holder.tvLike != null) {
            holder.tvLike.setText(getLikes(), TextView.BufferType.SPANNABLE);
            getEachWord(holder.tvLike);
            holder.tvLike.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tvLike.setHighlightColor(context.getResources().getColor(R.color.gray_light));

        }
        if (holder.ivLike != null) {
            if (holder.ivLike.getTag()==null)
            {
                holder.ivLike.setTag(1);
            }
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((int) holder.ivLike.getTag() == 1) {
                        likeNames.addFirst("16岁少女被十几个老师轮流布置作业");
                        holder.ivLike.setImageResource(R.mipmap.favorite_pressed_c);
                        holder.ivLike.setTag(0);
                        Toast.makeText(context,likeNames.get(0)+"觉得很赞",Toast.LENGTH_SHORT).show();
                    } else {
                        likeNames.remove(0);
                        holder.ivLike.setImageResource(R.mipmap.post_like);
                        holder.ivLike.setTag(1);
                    }
                    RecyclerAdapter.this.notifyDataSetChanged();
                }

            });
        }
        if (holder.listView!=null)
        {
            TalkAdapter adapter=new TalkAdapter(context);
            holder.listView.setAdapter(adapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return FIRST_ITEM;
        else if (position == 1)
            return VOICE_TYPE;
        else if (position == 2)
            return VIDEO_TYPE;
        else if (position % 3 == 0)
            return PICTURE_TYPE_BIG;
        else if (position % 3 == 1)
            return PICTURE_TYPE_MIDDLE;
        else
            return PICTURE_TYPE_SMALL;
    }

    @Override
    public int getItemCount() {
        return bigPictures.size();
    }

    public String getLikes() {
        String likes="";
        for (int i = 0; i < likeNames.size(); i++) {
            if (i != likeNames.size() - 1)
                likes += likeNames.get(i) + "丶";
            else {
                likes += likeNames.get(i) + " 等" + likeNames.size() + "人觉得很赞";
            }
        }
        return likes;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBg;
        ImageView ivLike;
        SimpleDraweeView ivHead;
        TextView tvName;
        TextView tvTime;
        TextView tvContent;
        RelativeLayout reLayout;
        LinearLayout linearLikeView;
        TextView tvLike;
        GridView gridView;
        GridView listView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivHead = (SimpleDraweeView) itemView.findViewById(R.id.iv_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            reLayout = (RelativeLayout) itemView.findViewById(R.id.layout_content);
            linearLikeView = (LinearLayout) itemView.findViewById(R.id.line_likelist);
            tvLike = (TextView) itemView.findViewById(R.id.tv_likelist);
            ivBg = (ImageView) itemView.findViewById(R.id.zoon_iv);
            ivLike= (ImageView) itemView.findViewById(R.id.iv_like);
            listView= (GridView) itemView.findViewById(R.id.listview);
        }
    }

    public void getEachWord(TextView textView) {
        Spannable spans = (Spannable) textView.getText();
        int start = 0;
        int end = 0;
        for (int i = 0; i < likeNames.size(); i++) {
            ClickableSpan clickSpan = getClickableSpan();
            if (i == 0)
                end += likeNames.get(i).length();
            else
                end += likeNames.get(i).length() + 1;
            spans.setSpan(clickSpan, start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String s = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }

        };
    }
}

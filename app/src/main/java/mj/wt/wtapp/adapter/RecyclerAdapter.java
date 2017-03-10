package mj.wt.wtapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mj.wt.wtapp.greendao.SongDbManager;
import mj.wt.wtapp.http.ActionHelp;
import mj.wt.wtapp.R;
import mj.wt.wtapp.bean.Song;
import mj.wt.wtapp.bean.TalkInfo;
import mj.wt.wtapp.bean.ZoneBigPicture;
import mj.wt.wtapp.utils.MusicUtils;
import mj.wt.wtapp.utils.ParesJsonUtil;
import mj.wt.wtapp.utils.SoftKeyBoardUtil;

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
    //点赞数据
    private LinkedList<String> likeNames = new LinkedList<>();
    //评论数据
    private LinkedList<TalkInfo.DataBean.ItemsBean> list = new LinkedList<>();
    private String currentUserName = "";
    private PopupWindow popupWindow;
    private EditText editText;
    private TextView sendTv;
    //图片数据源
    private List<ZoneBigPicture.DataBean.ItemsBean> bigPictures;
    //音乐资源
    private boolean musicIsPrepare=false;
    private MediaPlayer mediaPlayer;
    private List<Song> musicData;
    private AnimationDrawable animationDrawable;
    private Animation tVPaoMaDeng=new TranslateAnimation(0,-400f,0.0f,0.0f);



    public RecyclerAdapter(Context context, List<ZoneBigPicture.DataBean.ItemsBean> bigPictures) {
        this.context = context;
        this.bigPictures = bigPictures;

        ActionHelp.getLikesNames(likeNames);//初始化点赞列表
        //解析回复列表本地数据
        TalkInfo.DataBean dataBean = ParesJsonUtil.handleCitiesResponse(context);
        currentUserName = dataBean.getUsername();
        list.addAll(dataBean.getItems());
        //获取本地音乐
        musicData = MusicUtils.getMusicData(context);
        mediaPlayer = new MediaPlayer();

        //设置跑马灯动画效果
        tVPaoMaDeng.setDuration(10000);
        tVPaoMaDeng.setRepeatCount(500);
        tVPaoMaDeng.setRepeatMode(1);

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
            if (viewType == VOICE_TYPE) {
                layout = LayoutInflater.from(context).inflate(R.layout.zone_voice_layout, null);
                holder.voiceView = (RelativeLayout) layout.findViewById(R.id.voiceview);
                holder.musicName = (TextView) layout.findViewById(R.id.music_name);
                holder.musicIv = (ImageView) layout.findViewById(R.id.music_iv);
            } else if (viewType == VIDEO_TYPE)
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
            if (holder.ivLike.getTag() == null) {
                holder.ivLike.setTag(1);
            }
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((int) holder.ivLike.getTag() == 1) {
                        likeNames.addFirst(currentUserName);
                        holder.ivLike.setImageResource(R.mipmap.favorite_pressed_c);
                        holder.ivLike.setTag(0);
                        Toast.makeText(context, likeNames.get(0) + "觉得很赞", Toast.LENGTH_SHORT).show();
                    } else {
                        likeNames.remove(0);
                        holder.ivLike.setImageResource(R.mipmap.post_like);
                        holder.ivLike.setTag(1);
                    }
                    RecyclerAdapter.this.notifyDataSetChanged();
                }

            });
        }
        if (holder.listView != null) {
            TalkAdapter adapter = new TalkAdapter(context, list, currentUserName);
            adapter.setRecyclerAdapter(this);
            holder.listView.setAdapter(adapter);
        }
        if (holder.ivComment != null) {
            holder.ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    normalShowPopWindow(v);
                }
            });
        }
        if (holder.tvComment != null) {
            holder.tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    normalShowPopWindow(v);
                }
            });
        }
        if (holder.musicName != null) {
            holder.musicName.setText(musicData.get(position).singer + "-" + musicData.get(position).song);
        }
        if (holder.musicIv != null) {
            animationDrawable = (AnimationDrawable) holder.musicIv.getDrawable();
        }
        if (holder.voiceView != null) {
            perpareMusic(animationDrawable,musicData.get(position).path,position);
            holder.voiceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        Song song= SongDbManager.getInstance(context).querySong(position);
                        song.setProgress(mediaPlayer.getCurrentPosition());//更新播放进度
                        SongDbManager.getInstance(context).updateSong(song);
                        mediaPlayer.pause();
                        holder.musicName.getAnimation().cancel();
                        animationDrawable.stop();
                    } else {
                        if (musicIsPrepare) {
                            Song song= SongDbManager.getInstance(context).querySong(position);
                            if (song!=null) {
                                mediaPlayer.seekTo((int) song.progress);
                                mediaPlayer.start();
                                holder.musicName.startAnimation(tVPaoMaDeng);
                                animationDrawable.start();
                            }
                        }else {
                            Toast.makeText(context,"歌曲加载中",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


    private void perpareMusic(AnimationDrawable animationDrawable,String path,int position) {
        animationDrawable.stop();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicIsPrepare=false;
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                musicIsPrepare=true;//开始音频  
            }
        });
        //设置id,重复插入会导致报错
        Song song= SongDbManager.getInstance(context).querySong(position);
        if (song==null) {
            musicData.get(position).setId((long) position);
            SongDbManager.getInstance(context).insetSong(musicData.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return FIRST_ITEM;
        else if (position % 5 == 1)
            return VOICE_TYPE;
        else if (position % 5 == 2)
            return VIDEO_TYPE;
        else if (position % 5 == 3)
            return PICTURE_TYPE_BIG;
        else if (position % 5 == 4)
            return PICTURE_TYPE_MIDDLE;
        else
            return PICTURE_TYPE_SMALL;
    }

    @Override
    public int getItemCount() {
        return bigPictures.size();
    }

    public String getLikes() {
        String likes = "";
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
        ImageView ivComment;
        SimpleDraweeView ivHead;
        TextView tvName;
        TextView tvTime;
        TextView tvContent;
        TextView tvComment;
        RelativeLayout reLayout;
        LinearLayout linearLikeView;
        TextView tvLike;
        GridView gridView;
        ListView listView;
        //音乐控件
        RelativeLayout voiceView;
        TextView musicName;
        ImageView musicIv;

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
            ivLike = (ImageView) itemView.findViewById(R.id.iv_like);
            listView = (ListView) itemView.findViewById(R.id.listview);
            ivComment = (ImageView) itemView.findViewById(R.id.iv_comment);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);

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

    //普通评论
    public void normalShowPopWindow(View v) {
        if (popupWindow == null) {
            createPopWindow();//创建PopWindow
        }
        editText.setHint("评论");
        final EditText finalEditText = editText;
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = finalEditText.getText().toString();
                if (!s.equals("")) {
                    finalEditText.setText("");
                    popupWindow.dismiss();
                    TalkInfo.DataBean.ItemsBean itemsBean = new TalkInfo.DataBean.ItemsBean();
                    itemsBean.setMsg_from(currentUserName);
                    itemsBean.setMsg_to(currentUserName);
                    itemsBean.setMsg_content(s);
                    list.add(list.size(), itemsBean);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setPopWindowLocation(v);//设置popwindow显示位置

    }

    //列表评论
    public void specilShowPopWindow(View v, final int position) {
        if (popupWindow == null) {
            createPopWindow();//创建PopWindow
        }
        if (list.get(position).getMsg_from().equals(currentUserName))
            editText.setHint("回复" + list.get(position).getMsg_to() + ": ");
        else
            editText.setHint("回复" + list.get(position).getMsg_from() + ": ");
        final EditText finalEditText = editText;
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = finalEditText.getText().toString();
                if (!s.equals("")) {
                    finalEditText.setText("");
                    popupWindow.dismiss();
                    TalkInfo.DataBean.ItemsBean itemsBean = new TalkInfo.DataBean.ItemsBean();
                    itemsBean.setMsg_from(currentUserName);
                    if (list.get(position).getMsg_from().equals(currentUserName))
                        itemsBean.setMsg_to(list.get(position).getMsg_to());
                    else
                        itemsBean.setMsg_to(list.get(position).getMsg_from());
                    itemsBean.setMsg_content(s);
                    list.add(position + 1, itemsBean);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setPopWindowLocation(v);
    }


    private void createPopWindow() {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        final View popupWindowView = View.inflate(context, R.layout.talk_popupwindow, null);
        editText = (EditText) popupWindowView.findViewById(R.id.et_comment);
        sendTv = (TextView) popupWindowView.findViewById(R.id.tv_send);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);//设置之后在软键盘的上面，否则在底部，软键盘没有顶上去,而且点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

    }

    private void setPopWindowLocation(View v) {
        // 这里是位置显示方式,在屏幕的左侧
        popupWindow.showAtLocation((View) v.getParent(), Gravity.BOTTOM, 0, 0);
        //键盘弹出来
        SoftKeyBoardUtil.showKeyboard((Activity) context, true);
    }

}

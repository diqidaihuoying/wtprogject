package mj.wt.wtapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.dou361.ijkplayer.widget.PlayerView;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.util.DensityUtil;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mj.wt.wtapp.http.ActionHelp;
import mj.wt.wtapp.R;
import mj.wt.wtapp.adapter.RecyclerAdapter;
import mj.wt.wtapp.bean.ZoneBigPicture;
import mj.wt.wtapp.http.ObjectCallback;
import mj.wt.wtapp.utils.MediaUtils;
import okhttp3.Call;

public class ZoonActivity extends AppCompatActivity {


    private RelativeLayout zoonBar;
    private int height;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private List<ZoneBigPicture.DataBean.ItemsBean> bigPictures;

    private MediaPlayer mediaPlayer;//音频播放

    private PlayerView player;//视频播放

    private PowerManager.WakeLock wakeLock;//常量设置




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_zoon);
        height = DensityUtil.dip2px(this, 140);
        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();

        initData();
        initView();
    }

    private void initData() {
        bigPictures = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        adapter = new RecyclerAdapter(this, bigPictures, mediaPlayer);
        adapter.setVideoViewClickListener(new RecyclerAdapter.VideoViewClickListener() {
            @Override
            public void click() {
                player.startPlay();
            }
        });
        getNetWorkData();//获取网络数据
    }


    private void initView() {
        zoonBar = (RelativeLayout) findViewById(R.id.zoon_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int y = getScollYDistance();
                if (y <= 0) {   //设置标题的背景颜色
                    zoonBar.setBackgroundColor(Color.argb((int) 0, 0, 191, 255));
                } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / height;
                    float alpha = (255 * scale);
                    zoonBar.setBackgroundColor(Color.argb((int) alpha, 0, 191, 255));
                } else {    //滑动到banner下面设置普通颜色
                    zoonBar.setBackgroundColor(Color.argb((int) 255, 0, 191, 255));
                }
                if (player!=null)
                    player.pausePlay();
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    public int getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


    public void getNetWorkData() {
        ActionHelp.getBigPictureInfo(this, 20, 0, new ObjectCallback<ZoneBigPicture.DataBean>() {
            @Override
            protected void onSuccessed(ZoneBigPicture.DataBean response) {
                bigPictures.clear();
                bigPictures.addAll(response.getItems());
                adapter.notifyDataSetChanged();
            }

            @Override
            public Type getType() {
                return new TypeToken<ZoneBigPicture.DataBean>() {
                }.getType();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(this, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        /**demo的内容，暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(this, false);
        /**demo的内容，激活设备常亮状态*/
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            zoonBar.setVisibility(View.VISIBLE);
        }else {
            zoonBar.setVisibility(View.GONE);
        }
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        /**demo的内容，恢复设备亮度状态*/
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    public void setPlayer(PlayerView player) {
        this.player = player;
    }
}



package com.dou361.ijkplayer.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.ijkplayer.R;
import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.listener.OnControlPanelVisibilityChangeListener;
import com.dou361.ijkplayer.listener.OnPlayerBackListener;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class PlayerView {

    /**
     * 打印日志的TAG
     */
    private static final String TAG = PlayerView.class.getSimpleName();
    /**
     * 全局上下文
     */
    private final Context mContext;
    /**
     * 依附的容器Activity
     */
    private final Activity mActivity;
    /**
     * Activity界面的中布局的查询器
     */
    private final LayoutQuery query;
    /**
     * 原生的Ijkplayer
     */
    private final IjkVideoView videoView;
    /**
     * 播放器整个界面
     */
    private final View rl_box;
    /**
     * 播放器顶部控制bar
     */
    private final View ll_topbar;
    /**
     * 播放器底部控制bar
     */
    private final View ll_bottombar;
    /**
     * 播放器封面，播放前的封面或者缩列图
     */
    private final ImageView iv_trumb;
    /**
     * 视频返回按钮
     */
    private final ImageView iv_back;
    /**
     * 视频中间的播放按钮
     */
    private final ImageView iv_player;
    /**
     * 视频全屏按钮
     */
    private final ImageView iv_fullscreen;
    private String currentUrl;

    /**
     * 视频加载速度
     */
    private final TextView tv_speed;
    /**
     * 视频播放进度条
     */
    private SeekBar seekBar;

    /**
     * 码流列表
     */
    private List<VideoijkBean> listVideos = new ArrayList<VideoijkBean>();

    /**
     * 当前状态
     */
    private int status = PlayStateParams.STATE_IDLE;
    /**
     * 当前播放位置
     */
    private int currentPosition;
    /**
     * 滑动进度条得到的新位置，和当前播放位置是有区别的,newPosition =0也会调用设置的，故初始化值为-1
     */
    private long newPosition = -1;
    /**
     * 视频旋转的角度，默认只有0,90.270分别对应向上、向左、向右三个方向
     */
    private int rotation = 0;
    /**
     * 视频显示比例,默认保持原视频的大小
     */
    private int currentShowType = PlayStateParams.fitparent;
    /**
     * 播放总时长
     */
    private long duration;

    /**
     * 获取当前设备的宽度
     */
    private final int screenWidthPixels;
    /**
     * 记录播放器竖屏时的高度
     */
    private final int initHeight;


    /**
     * 第三方so是否支持，默认不支持，true为支持
     */
    private boolean playerSupport;

    /**
     * 是否在拖动进度条中，默认为停止拖动，true为在拖动中，false为停止拖动
     */
    private boolean isDragging;
    /**
     * 播放的时候是否需要网络提示，默认显示网络提示，true为显示网络提示，false不显示网络提示
     */
    private boolean isGNetWork = true;




    /**
     * 是否是竖屏，默认为竖屏，true为竖屏，false为横屏
     */
    private boolean isPortrait = true;



    /**
     * 同步进度
     */
    private static final int MESSAGE_SHOW_PROGRESS = 1;
    /**
     * 设置新位置
     */
    private static final int MESSAGE_SEEK_NEW_POSITION = 3;

    /**
     * 重新播放
     */
    private static final int MESSAGE_RESTART_PLAY = 5;
    /**
     * 隐藏状态栏
     */
    private static final int MESSAGE_HIDE_UI = 6;
    /**
     * 显示状态栏
     */
    private static final int MESSAGE_SHOW_UI = 7;
    /**
     * 消息处理
     */
    @SuppressWarnings("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**滑动完成，设置播放进度*/
                case MESSAGE_SEEK_NEW_POSITION:
                    if (newPosition >= 0) {
                        videoView.seekTo((int) newPosition);
                        newPosition = -1;
                    }
                    break;
                /**滑动中，同步播放进度*/
                case MESSAGE_SHOW_PROGRESS:
                    long pos = syncProgress();
                    if (!isDragging) {
                        msg = obtainMessage(MESSAGE_SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));

                    }
                    break;
                case MESSAGE_HIDE_UI:
                    showOperatorPan(false);
                    break;
                case MESSAGE_SHOW_UI:
                    showOperatorPan(true);
                    break;
            }
        }
    };
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    /**========================================视频的监听方法==============================================*/

    /**
     * Activity界面方向监听
     */
    private final OrientationEventListener orientationEventListener;
    /**
     * 控制面板显示或隐藏监听
     */
    private OnControlPanelVisibilityChangeListener onControlPanelVisibilityChangeListener;

    /**
     * 视频的返回键监听
     */
    private OnPlayerBackListener mPlayerBack;
    /**
     * 视频播放时信息回调
     */
    private IMediaPlayer.OnInfoListener onInfoListener;

    /**
     * 点击事件监听
     */
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.app_video_fullscreen) {
                /**视频全屏切换*/
                toggleFullScreen();
            } else if (v.getId() == R.id.play_icon) {
                /**视频播放和暂停*/
                    showOperatorPan(true);
                if (videoView.isPlaying()) {
                    pausePlay();
                } else {
                    if (isGNetWork && (NetworkUtils.getNetworkType(mContext) == 4 || NetworkUtils.getNetworkType(mContext) == 5 || NetworkUtils.getNetworkType(mContext) == 6)) {
                        query.id(R.id.app_video_netTie).visible();
                    } else {
                        query.id(R.id.app_video_netTie).gone();
                        startPlay();
                    }
                    mHandler.removeMessages(MESSAGE_HIDE_UI);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_UI, 3000);
                }
            } else if (v.getId() == R.id.app_video_finish) {
                /**返回*/
                if (!isPortrait) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    if (mPlayerBack != null) {
                        mPlayerBack.onPlayerBack();
                    } else {
                        mActivity.finish();
                    }
                }
            } else if (v.getId() == R.id.app_video_netTie_icon) {
                /**使用移动网络提示继续播放*/
                isGNetWork = false;
                startPlay();
            }
        }


    };

    /**
     * 更新播放按钮ui
     */
    private void updatePlayUi() {
       if (status == PlayStateParams.STATE_PLAYING) {
            iv_player.setVisibility(View.GONE);
            iv_player.setImageResource(R.drawable.simple_player_center_pause);
        } else {
            iv_player.setVisibility(View.VISIBLE);
            iv_player.setImageResource(R.drawable.simple_player_center_play);
        }
    }

    /**
     * 进度条滑动监听
     */
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        /**数值的改变*/
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                /**不是用户拖动的，自动播放滑动的情况*/
                return;
            } else {
                long duration = getDuration();
                int position = (int) ((duration * progress * 1.0) / 1000);
                String time = generateTime(position);
                query.id(R.id.app_video_currentTime_full).text(time);
            }

        }

        /**开始拖动*/
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isDragging = true;
            mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
        }

        /**停止拖动*/
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            long duration = getDuration();
            videoView.seekTo((int) ((duration * seekBar.getProgress() * 1.0) / 1000));
            mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
            isDragging = false;
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
        }
    };


    /**
     * 保留旧的调用方法
     */
    public PlayerView(Activity activity) {
        this(activity, null);
    }

    /**
     * 新的调用方法，适用非Activity中使用PlayerView，例如fragment、holder中使用
     */
    public PlayerView(Activity activity, View rootView) {
        this.mActivity = activity;
        this.mContext = activity;
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            playerSupport = true;
        } catch (Throwable e) {
            Log.e(TAG, "loadLibraries error", e);
        }
        screenWidthPixels = mContext.getResources().getDisplayMetrics().widthPixels;


        if (rootView == null) {
            query = new LayoutQuery(mActivity);
            rl_box = mActivity.findViewById(R.id.app_video_box);
            videoView = (IjkVideoView) mActivity.findViewById(R.id.video_view);
            ll_topbar = mActivity.findViewById(R.id.app_video_top_box);
            ll_bottombar = mActivity.findViewById(R.id.ll_bottom_bar);
            iv_trumb = (ImageView) mActivity.findViewById(R.id.iv_trumb);
            iv_back = (ImageView) mActivity.findViewById(R.id.app_video_finish);
            iv_player = (ImageView) mActivity.findViewById(R.id.play_icon);
            iv_fullscreen = (ImageView) mActivity.findViewById(R.id.app_video_fullscreen);
            tv_speed = (TextView) mActivity.findViewById(R.id.app_video_speed);
            seekBar = (SeekBar) mActivity.findViewById(R.id.app_video_seekBar);
        } else {
            query = new LayoutQuery(mActivity, rootView);
            rl_box = rootView.findViewById(R.id.app_video_box);
            videoView = (IjkVideoView) rootView.findViewById(R.id.video_view);
            ll_topbar = rootView.findViewById(R.id.app_video_top_box);
            ll_bottombar = rootView.findViewById(R.id.ll_bottom_bar);
            iv_trumb = (ImageView) rootView.findViewById(R.id.iv_trumb);
            iv_back = (ImageView) rootView.findViewById(R.id.app_video_finish);
            iv_player = (ImageView) rootView.findViewById(R.id.play_icon);
            iv_fullscreen = (ImageView) rootView.findViewById(R.id.app_video_fullscreen);
            tv_speed = (TextView) rootView.findViewById(R.id.app_video_speed);
            seekBar = (SeekBar) rootView.findViewById(R.id.app_video_seekBar);
        }
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(mSeekListener);
        iv_player.setOnClickListener(onClickListener);
        iv_fullscreen.setOnClickListener(onClickListener);
        iv_back.setOnClickListener(onClickListener);
        query.id(R.id.app_video_netTie_icon).clicked(onClickListener);
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if (tv_speed != null) {
                        tv_speed.setText(getFormatSize(extra));
                }
                statusChange(what);
                if (onInfoListener != null) {
                    onInfoListener.onInfo(mp, what, extra);
                }
                getCurrentPosition();
                return true;
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(mContext, new PlayerGestureListener());
        rl_box.setClickable(true);
        rl_box.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                }
                if (gestureDetector.onTouchEvent(motionEvent))
                    return true;
                // 处理手势结束
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });

        orientationEventListener = new OrientationEventListener(mActivity) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 0 && orientation <= 30 || orientation >= 330 || (orientation >= 150 && orientation <= 210)) {
                    //竖屏
                    if (isPortrait) {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        orientationEventListener.disable();
                    }
                } else if ((orientation >= 90 && orientation <= 120) || (orientation >= 240 && orientation <= 300)) {
                    if (!isPortrait) {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        orientationEventListener.disable();
                    }
                }
            }
        };

        isPortrait = (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initHeight = rl_box.getLayoutParams().height;

        if (!playerSupport) {
            Toast.makeText(mContext, R.string.not_support, Toast.LENGTH_SHORT).show();
        } else {
            query.id(R.id.ll_bg).visible();
        }
    }

    public PlayerView onPause() {
        getCurrentPosition();
        videoView.onPause();
        return this;
    }

    public PlayerView onResume() {
        videoView.onResume();
        videoView.seekTo(currentPosition);
        return this;
    }

    public PlayerView onDestroy() {
        orientationEventListener.disable();
        mHandler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
        videoView.stopPlayback();
        return this;
    }

    public PlayerView onConfigurationChanged(final Configuration newConfig) {
        isPortrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        doOnConfigurationChanged(isPortrait);
        return this;
    }

    public boolean onBackPressed() {
        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return false;
    }





    /**
     * 设置播放区域拉伸类型
     */
    public PlayerView setScaleType(int showType) {
        currentShowType = showType;
        videoView.setAspectRatio(currentShowType);
        return this;
    }

    /**
     * 设置播放地址
     * 包括视频清晰度列表
     * 对应地址列表
     */
    public PlayerView setPlaySource(List<VideoijkBean> list) {
        listVideos.clear();
        if (list != null && list.size() > 0) {
            listVideos.addAll(list);
        }
        return this;
    }

    /**
     * 设置播放地址
     * 单个视频VideoijkBean
     */
    public PlayerView setPlaySource(VideoijkBean videoijkBean) {
        listVideos.clear();
        if (videoijkBean != null) {
            listVideos.add(videoijkBean);
        }
        return this;
    }

    /**
     * 设置播放地址
     * 单个视频地址时
     * 带流名称
     */
    public PlayerView setPlaySource(String stream, String url) {
        VideoijkBean mVideoijkBean = new VideoijkBean();
        mVideoijkBean.setStream(stream);
        mVideoijkBean.setUrl(url);
        setPlaySource(mVideoijkBean);
        currentUrl = url;
        currentPosition=0;
        return this;
    }

    /**
     * 设置播放地址
     * 单个视频地址时
     */
    public PlayerView setPlaySource(String url) {
        setPlaySource("标清", url);
        return this;
    }


    /**
     * 开始播放
     */
    public PlayerView startPlay() {
        status = PlayStateParams.STATE_PREPARING;
        if (playerSupport) {
            //换源之后声音可播，画面卡住，主要是渲染问题，目前只是提供了软解方式，后期提供设置方式
                videoView.setRender(videoView.RENDER_TEXTURE_VIEW);
                videoView.setVideoPath(currentUrl);
                videoView.seekTo(currentPosition);
                videoView.start();
            } else {
                Toast.makeText(mContext, R.string.not_support, Toast.LENGTH_SHORT).show();
            }
        updatePlayUi();
        return this;
    }


    /**
     * 设置视频名称
     */
    public PlayerView setTitle(String title) {
        query.id(R.id.app_video_title).text(title);
        return this;
    }

    /**
     * 暂停播放
     */
    public PlayerView pausePlay() {
        status = PlayStateParams.STATE_PAUSED;
        getCurrentPosition();
        videoView.pause();
        updatePlayUi();
        return this;
    }

    /**
     * 停止播放
     */
    public PlayerView stopPlay() {
        videoView.stopPlayback();
        return this;
    }

    /**
     * 设置播放位置
     */
    public PlayerView seekTo(int playtime) {
        videoView.seekTo(playtime);
        return this;
    }

    /**
     * 获取当前播放位置
     */
    public int getCurrentPosition() {
        currentPosition = videoView.getCurrentPosition();
        return currentPosition;
    }

    /**
     * 获取视频播放总时长
     */
    public long getDuration() {
        duration = videoView.getDuration();
        return duration;
    }

    /**
     * 设置2/3/4/5G和WiFi网络类型提示，
     *
     * @param isGNetWork true为进行2/3/4/5G网络类型提示
     *                   false 不进行网络类型提示
     */
    public PlayerView setNetWorkTypeTie(boolean isGNetWork) {
        this.isGNetWork = isGNetWork;
        return this;
    }


    /**
     * 显示或隐藏操作面板 ,true为显示，false为隐藏
     */
    public PlayerView showOperatorPan(boolean flag) {
        ll_topbar.setVisibility(flag ? View.VISIBLE : View.GONE);
        ll_bottombar.setVisibility(flag ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 全屏切换
     */
    public PlayerView toggleFullScreen() {
        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        updateFullScreenButton();
        return this;
    }

    /**
     * 获取界面方向
     */
    public int getScreenOrientation() {
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }


    /**
     * 状态改变同步UI
     */
    private void statusChange(int newStatus) {
        if (newStatus == PlayStateParams.STATE_COMPLETED) {
            status = PlayStateParams.STATE_COMPLETED;
            currentPosition = 0;
            showOperatorPan(true);
        } else if (newStatus == PlayStateParams.STATE_PREPARING
                || newStatus == PlayStateParams.MEDIA_INFO_BUFFERING_START) {
            status = PlayStateParams.STATE_PREPARING;
            query.id(R.id.app_video_loading).visible();
            /**视频缓冲*/
        } else {
            if (status == PlayStateParams.STATE_PAUSED) {
                status = PlayStateParams.STATE_PAUSED;
            } else {
                status = PlayStateParams.STATE_PLAYING;
            }
            /**视频缓冲结束后隐藏缩列图*/
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**延迟0.5秒隐藏视频封面隐藏*/
                    query.id(R.id.ll_bg).gone();
                    query.id(R.id.app_video_loading).gone();
                    mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
                    mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
                }
            }, 500);
            updatePlayUi();
        }
    }


    /**
     * 界面方向改变是刷新界面
     */
    private void doOnConfigurationChanged(final boolean portrait) {
        if (videoView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    tryFullScreen(!portrait);
                    if (portrait) {
                        query.id(R.id.app_video_box).height(initHeight, false);
                    } else {
                        int heightPixels = mActivity.getResources().getDisplayMetrics().heightPixels;
                        int widthPixels = mActivity.getResources().getDisplayMetrics().widthPixels;
                        query.id(R.id.app_video_box).height(Math.min(heightPixels, widthPixels), false);
                    }
                    updateFullScreenButton();
                }
            });
            orientationEventListener.enable();
        }
    }


    /**
     * 设置界面方向
     */
    private void setFullScreen(boolean fullScreen) {
        if (mActivity != null) {

            WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
            if (fullScreen) {
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                mActivity.getWindow().setAttributes(attrs);
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } else {
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mActivity.getWindow().setAttributes(attrs);
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }

    }

    /**
     * 设置界面方向带隐藏actionbar
     */
    private void tryFullScreen(boolean fullScreen) {
        if (mActivity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide();
                } else {
                    supportActionBar.show();
                }
            }
        }
        setFullScreen(fullScreen);
    }


    /**
     * 同步进度
     */
    private long syncProgress() {
        if (isDragging) {
            return 0;
        }
        long position = videoView.getCurrentPosition();
        long duration = videoView.getDuration();
        if (seekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                seekBar.setProgress((int) pos);
            }
            int percent = videoView.getBufferPercentage();
            seekBar.setSecondaryProgress(percent * 10);
        }
        query.id(R.id.app_video_currentTime_full).text(generateTime(position));
        query.id(R.id.app_video_endTime_full).text(generateTime(duration));

        return position;
    }

    /**
     * 时长格式化显示
     */
    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 下载速度格式化显示
     */
    private String getFormatSize(int size) {
        long fileSize = (long) size;
        String showSize = "";
        if (fileSize >= 0 && fileSize < 1024) {
            showSize = fileSize + "Kb/s";
        } else if (fileSize >= 1024 && fileSize < (1024 * 1024)) {
            showSize = Long.toString(fileSize / 1024) + "KB/s";
        } else if (fileSize >= (1024 * 1024) && fileSize < (1024 * 1024 * 1024)) {
            showSize = Long.toString(fileSize / (1024 * 1024)) + "MB/s";
        }
        return showSize;
    }

    /**
     * 更新全屏和半屏按钮
     */
    private void updateFullScreenButton() {
        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            iv_fullscreen.setImageResource(R.drawable.simple_player_icon_fullscreen_shrink);
        } else {
            iv_fullscreen.setImageResource(R.drawable.simple_player_icon_fullscreen_stretch);
        }
    }


    /**
     * 播放器的手势监听
     */
    public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            /**视频视窗双击事件*/
            toggleFullScreen();
            return true;
        }

        /**
         * 单击
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            /**视频视窗单击事件*/
            showOperatorPan(true);
            mHandler.removeMessages(MESSAGE_HIDE_UI);
            mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_UI,3000);
            return true;
        }
    }

}

package mj.wt.wtapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.hyphenate.util.DensityUtil;

import mj.wt.wtapp.Myappliaction;

/**
 * Created by wantao on 2017/3/3.
 */

public class PictureSizeUtil {
    // 屏幕宽
    public static final int SCREEN_WIDTH = getScreenWidth(Myappliaction.context);
    // 10dp->px
    public static final int DP_PX_10 = DensityUtil.dip2px(Myappliaction.context, 10);
    /**
     * 获得屏幕高度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 3张图片显示 690*380
     *
     * @return
     */
    public static RelativeLayout.LayoutParams smallLayoutParams() {
        int width = (SCREEN_WIDTH - 4 * DP_PX_10) / 3; // 左右和中间间距
        int height = (width * 380) / 690;
        return new RelativeLayout.LayoutParams(width, height);
    }
    /**
     * 2张图片显示 690*380
     *
     * @return
     */
    public static RelativeLayout.LayoutParams middleLayoutParams() {
        int width = (SCREEN_WIDTH - 3 * DP_PX_10) / 2; // 左右和中间间距
        int height = (width * 380) / 690;
        return new RelativeLayout.LayoutParams(width, height);
    }
    /**
     * 1张图片显示 690*380
     *
     * @return
     */
    public static RelativeLayout.LayoutParams bigLayoutParams() {
        int width = SCREEN_WIDTH - 2 * DP_PX_10;
        int height = (width * 380) / 690;
        return new RelativeLayout.LayoutParams(width, height);
    }


}

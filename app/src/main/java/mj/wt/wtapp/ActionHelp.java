package mj.wt.wtapp;

import android.app.Activity;

import com.classic.okhttp.OkHttpUtils;
import com.classic.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wantao on 2017/3/2.
 */

public class ActionHelp {

    public static final String Zone_BIG_PICTURE="http://api.liwushuo.com/v2/channels/101/items?";

    /*
      空间页面获取大图信息
     limit 一次请求的个数
     offset 页数
     */
    public static void getBigPictureInfo(Activity activity,int limit,int offset,Callback callback)
    {
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("limit", limit+"");
        paramsMap.put("offset",offset+"");
        paramsMap.put("gender", 1+"");
        paramsMap.put("generation", 2 + "");
        OkHttpUtils.get().url(Zone_BIG_PICTURE).tag(activity).headers(null).build().execute(callback);
    }


}

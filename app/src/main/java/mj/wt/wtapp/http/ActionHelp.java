package mj.wt.wtapp.http;

import android.app.Activity;

import com.classic.okhttp.OkHttpUtils;
import com.classic.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static void getLikesNames(List<String> likeNames)
    {
        likeNames.add("LoL无极剑圣开山鼻祖万涛");
        likeNames.add("宇宙疯狂输出全场Carry肖雨");
        likeNames.add("银河系最怂比 永远不上 肖亮");
        likeNames.add("除了赏金猎人有点输出 其它都是坑 肖顺");
        likeNames.add("万年坑货赵信大野肖勇");
        likeNames.add("神预测:全场输出最低 5123肖栋");
    }



}

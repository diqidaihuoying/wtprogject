package mj.wt.wtapp.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import mj.wt.wtapp.bean.TalkInfo;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by wantao on 2017/3/7.
 */

public class ParesJsonUtil {
    //读取assets中的文件
    private static InputStream readFromAssets(Context context) {
        InputStream inputStream=null;
        try {
            inputStream = context.getAssets().open("zone_talk.txt");//此处为要加载的json文件名称
        } catch (Exception e) {
        }
        return  inputStream;
    }
    //将传入的is一行一行解析读取出来出来
    private static  String readTextFromSDcard(Context context) throws Exception {
        InputStreamReader reader = new InputStreamReader(readFromAssets(context),"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }
    //把读取出来的json数据进行解析
    public static TalkInfo.DataBean handleCitiesResponse(Context context) {
        String str=null;
        try {
             str=readTextFromSDcard(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TalkInfo obj = new Gson().fromJson(str, TalkInfo.class);
        return obj.getData();
    }
}

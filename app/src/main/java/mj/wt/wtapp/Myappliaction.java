package mj.wt.wtapp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import mj.wt.wtapp.utils.FrescoHelper;

/**
 * Created by asus on 2016/9/27.
 */
public class Myappliaction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化图片加载fresco
        Fresco.initialize(getApplicationContext(), FrescoHelper.getImagePipelineConfig(getApplicationContext()));
    }
}

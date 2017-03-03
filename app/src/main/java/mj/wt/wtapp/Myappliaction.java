package mj.wt.wtapp;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import mj.wt.wtapp.huanxin.utils.PreferenceManager;
import mj.wt.wtapp.utils.FrescoHelper;

/**
 * Created by asus on 2016/9/27.
 */
public class Myappliaction extends Application {
    public static Context context;
    @Override
        public void onCreate() {
        super.onCreate();
        context=this;
        // 初始化图片加载fresco
        Fresco.initialize(getApplicationContext(), FrescoHelper.getImagePipelineConfig(getApplicationContext()));

        //initialize preference manager
        PreferenceManager.init(this);

        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }


}

package mj.wt.wtapp.main;

import android.content.Intent;


import com.example.basic.BaseSplashActivity;
import com.hyphenate.chat.EMClient;

import java.util.List;

import mj.wt.wtapp.R;

public class SplashActivity extends BaseSplashActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int sleepTime = 2000;

    /*public static boolean isFristEnter = true;
    private SharedPreferencesUtil sp;*/

    @Override
    protected void setSplashResources(List<SplashImgResource> resources) {
        resources.add(new SplashImgResource(R.mipmap.splash,1500,100f,true));
    }

    @Override
    protected Class<?> nextActivity() {
        return null;
    }

    @Override
    protected void toNext() {
        /*sp = new SharedPreferencesUtil(this, TAG);
        isFristEnter = sp.getBooleanValue("isFristEnter", true);
        if (isFristEnter) {//第一次进入引导页
            isFristEnter = false;
            sp.putBooleanValue("isFristEnter", isFristEnter);
            startActivity(new Intent(this,MainActivity.class));
        } else {
            startActivity(new Intent(this,MainActivity.class));
        }*/
        new Thread(new Runnable() {
            public void run() {
                if (EMClient.getInstance().isLoggedInBefore()) {
                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //enter main screen
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();
    }
}

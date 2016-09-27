package mj.wt.wtapp.main;

import android.content.Intent;


import com.example.basic.BaseSplashActivity;

import java.util.List;

import mj.wt.wtapp.R;

public class SplashActivity extends BaseSplashActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
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
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}

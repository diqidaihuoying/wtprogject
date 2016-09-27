package mj.wt.wtapp.base;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.basic.BaseActivity;

import mj.wt.wtapp.R;

public abstract class TActivity extends BaseActivity {
    protected TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        if (tvTitle != null) {
            tvTitle.setText(setToolTitle());
        }
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.mipmap.icon_common_toolbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }
    // 设置标题
    protected String setToolTitle() {
        return "";
    }

    @Override public void onBackPressed() {
        if (currentFragment != null && currentFragment.isVisible()) {
            if (!currentFragment.onBackPress()) {
                return;
            }
        }
        super.onBackPressed();
    }

}

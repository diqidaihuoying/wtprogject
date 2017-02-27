package mj.wt.wtapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import mj.wt.wtapp.R;
import mj.wt.wtapp.widget.ZoonScrollView;

public class ZoonActivity extends AppCompatActivity implements ZoonScrollView.ZoonScrollViewListener{


    private ZoonScrollView scrollView;
    private RelativeLayout zoonBar;
    private ImageView zoonImage;
    private int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_zoon);
        initView();
        zoonImage.setFocusable(true);
        zoonImage.setFocusableInTouchMode(true);
        zoonImage.requestFocus();
        initListeners();
    }


    private void initView() {
        scrollView= (ZoonScrollView) findViewById(R.id.zoonscrollview);
        zoonBar= (RelativeLayout) findViewById(R.id.zoon_bar);
        zoonImage= (ImageView) findViewById(R.id.zoon_iv);
    }
    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = zoonImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                zoonBar.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = zoonImage.getHeight();
                scrollView.setListener(ZoonActivity.this);
            }
        });
    }

    @Override
    public void onScrollChanged(ZoonScrollView scrollView, int x, int y, int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            zoonBar.setBackgroundColor(Color.argb((int) 0, 0,191,255));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            zoonBar.setBackgroundColor(Color.argb((int) alpha, 0,191,255));
        } else {    //滑动到banner下面设置普通颜色
            zoonBar.setBackgroundColor(Color.argb((int) 255, 0,191,255));
        }
    }
}

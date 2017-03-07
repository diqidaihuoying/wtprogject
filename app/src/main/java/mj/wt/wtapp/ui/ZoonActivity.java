package mj.wt.wtapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.hyphenate.util.DensityUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mj.wt.wtapp.ActionHelp;
import mj.wt.wtapp.R;
import mj.wt.wtapp.adapter.RecyclerAdapter;
import mj.wt.wtapp.bean.ZoneBigPicture;
import mj.wt.wtapp.http.ObjectCallback;
import mj.wt.wtapp.utils.StatusBarUtil;
import okhttp3.Call;

public class ZoonActivity extends AppCompatActivity {


    private RelativeLayout zoonBar;
    private int height;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private List<ZoneBigPicture.DataBean.ItemsBean> bigPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_zoon);
        height= DensityUtil.dip2px(this,140);
        initData();
        initView();
    }

    private void initData() {
        bigPictures = new ArrayList<>();
        adapter = new RecyclerAdapter(this,bigPictures);
        getNetWorkData();//获取网络数据
    }


    private void initView() {
        zoonBar= (RelativeLayout) findViewById(R.id.zoon_bar);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int y=getScollYDistance();
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


        });
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    public int getScollYDistance() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


    public void getNetWorkData() {
        ActionHelp.getBigPictureInfo(this, 20, 0, new ObjectCallback<ZoneBigPicture.DataBean> (){
            @Override
            protected void onSuccessed(ZoneBigPicture.DataBean response) {
                bigPictures.clear();
                bigPictures.addAll(response.getItems());
                adapter.notifyDataSetChanged();
            }

            @Override
            public Type getType() {
                return new TypeToken<ZoneBigPicture.DataBean>() {
                }.getType();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });

    }
}



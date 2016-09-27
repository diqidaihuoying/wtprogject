package mj.wt.wtapp.main;

import android.net.Uri;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.view.ViewHelper;

import mj.wt.wtapp.R;
import mj.wt.wtapp.base.TActivity;
import mj.wt.wtapp.widget.dragview.adapter.SwipeListAdapter;
import mj.wt.wtapp.widget.dragview.utils.Utils;
import mj.wt.wtapp.widget.dragview.view.DragLayout;
import mj.wt.wtapp.widget.dragview.view.DragRelativeLayout;


public class MainActivity extends TActivity {
    private DragLayout mDragLayout;
    private ImageView mHeader;
    private SimpleDraweeView mLeftHeader;
    private TextView addFriendTv;
    private SwipeListAdapter adapter;
    private ListView mainListView;
    private ListView leftListView;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        initLeftContent();
        initMainContent();
    }

    private void initMainContent() {
        mDragLayout = (DragLayout) findViewById(R.id.main_draglayout);
        mDragLayout.setDragListener(mDragListener);
        DragRelativeLayout mMainView = (DragRelativeLayout) findViewById(R.id.main_dragrelativelayout);
        mMainView.setDragLayout(mDragLayout);
        mHeader = (ImageView) findViewById(R.id.main_head_iv);
        mHeader.setOnClickListener(this);
        addFriendTv = (TextView) findViewById(R.id.main_add_tv);
        addFriendTv.setOnClickListener(this);
        mHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDragLayout.switchScaleEnable();
                return true;
            }
        });
        mainListView= (ListView) findViewById(R.id.main_listview);
        adapter = new SwipeListAdapter(MainActivity.this);
        mainListView.setAdapter(adapter);
        mDragLayout.setAdapterInterface(adapter);
        mainListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    adapter.closeAllLayout();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initLeftContent() {
        mLeftHeader= (SimpleDraweeView) findViewById(R.id.main_left_head);
        mLeftHeader.setImageResource(R.mipmap.icon_head);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[]{"万涛","马晶"});
        leftListView= (ListView) findViewById(R.id.main_left_listview);
        leftListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.main_head_iv:
                mDragLayout.open(true);
                break;
            case R.id.main_add_tv:
              Utils.showToast(this,"添加好友");
                break;
            default:
                break;
        }
    }

    private DragLayout.OnDragListener mDragListener = new DragLayout.OnDragListener() {
        @Override
        public void onOpen() {
        }
        @Override
        public void onClose() {

        }
        @Override
        public void onDrag(final float percent) {
            /*主界面左上角头像渐渐消失*/
            ViewHelper.setAlpha(mHeader, 1 - percent);
        }
        @Override
        public void onStartOpen(DragLayout.Direction direction) {
            Utils.showToast(getApplicationContext(), "onStartOpen: " + direction.toString());
        }
    };
}

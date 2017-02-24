package mj.wt.wtapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.base.TActivity;
import mj.wt.wtapp.bean.PhotoInfo;
import mj.wt.wtapp.fragment.ContactFragment;
import mj.wt.wtapp.fragment.NewsFragment;
import mj.wt.wtapp.fragment.ZoonFragment;
import mj.wt.wtapp.widget.dragview.view.DragLayout;
import mj.wt.wtapp.widget.dragview.view.DragRelativeLayout;


public class MainActivity extends TActivity {
    public DragLayout mDragLayout;
    private ImageView mHeader;
    private ImageView mLeftHeader;
    private TextView addFriendTv,titleTv;
    private ListView leftListView;
    private ArrayList<String> fragmentTags;
    private static int currIndex = 0;
    private static final String FRAGMENT_TAGS = "fragmentTags";
    private static final String CURR_INDEX = "currIndex";
    private FragmentManager fragmentManager;

    public List<PhotoInfo> getPhotoInfos() {
        return photoInfos;
    }

    public void setPhotoInfos(List<PhotoInfo> photoInfos) {
        this.photoInfos = photoInfos;
    }

    private List<PhotoInfo> photoInfos;

    private EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    private void refreshUIWithMessage() {
        if (currIndex == 0) {
            // refresh conversation list
            NewsFragment fragmentByTag = (NewsFragment) fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
            if (fragmentByTag!= null) {
                fragmentByTag.refresh();
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            initTabData();
        } else {
            initFromSavedInstantsState(savedInstanceState);
        }
        initTabViews();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    private void initTabViews() {
        initLeftContent();
        initMainContent();
        RadioGroup group = (RadioGroup) findViewById(R.id.main_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_news:
                        titleTv.setText("消息");
                        addFriendTv.setVisibility(View.INVISIBLE);
                        currIndex = 0;
                        break;
                    case R.id.foot_bar_contact:
                        titleTv.setText("联系人");
                        addFriendTv.setVisibility(View.VISIBLE);
                        currIndex = 1;
                        break;
                    case R.id.foot_bar_zoon:
                        titleTv.setText("动态");
                        addFriendTv.setVisibility(View.INVISIBLE);
                        currIndex = 2;
                        break;
                    default:
                        break;
                }
                showFragment();
            }
        });
        showFragment();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
        outState.putStringArrayList(FRAGMENT_TAGS, fragmentTags);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initFromSavedInstantsState(savedInstanceState);
    }


    private void initFromSavedInstantsState(Bundle savedInstanceState) {
        currIndex = savedInstanceState.getInt(CURR_INDEX);
        fragmentTags = savedInstanceState.getStringArrayList(FRAGMENT_TAGS);
        showFragment();
    }

    private void showFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if(fragment == null) {
            fragment = instantFragment(currIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            if (i == currIndex) continue;
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if(f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.main_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }
    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                return new NewsFragment();
            case 1:
                return new ContactFragment();
            case 2:
                return new ZoonFragment();
            default:
                return null;
        }
    }

    private void initTabData() {
        currIndex = 0;
        fragmentTags = new ArrayList<>(Arrays.asList("NewsFragment", "ContactFragment", "ZoonFragment"));
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
        titleTv= (TextView) findViewById(R.id.main_head_title);
    }

    private void initLeftContent() {
        mLeftHeader= (ImageView) findViewById(R.id.main_left_head);
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
        }
    };


    @Override
    protected void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        super.onDestroy();
    }
}

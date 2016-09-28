package mj.wt.wtapp.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.basic.BaseFragment;

import mj.wt.wtapp.R;
import mj.wt.wtapp.main.MainActivity;
import mj.wt.wtapp.widget.dragview.adapter.SwipeListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment {

    private ListView listview;
    private SwipeListAdapter adapter;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }

    @Override
    public void initView(View parentView) {
        super.initView(parentView);
        listview= (ListView)parentView. findViewById(R.id.newsfragment_listview);
        adapter = new SwipeListAdapter(activity);
        listview.setAdapter(adapter);
        ((MainActivity)activity).mDragLayout.setAdapterInterface(adapter);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
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
}

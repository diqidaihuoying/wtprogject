package mj.wt.wtapp.fragment;


import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.basic.BaseFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import mj.wt.wtapp.R;
import mj.wt.wtapp.main.MainActivity;
import mj.wt.wtapp.widget.dragview.adapter.SwipeListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment {

    private ListView listview;
    private SwipeListAdapter adapter;
    private  List<EMConversation> list=new ArrayList<>();

    @Override
    public void initData() {
        super.initData();
        list.clear();
        list.addAll(loadConversationList());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }

    @Override
    public void initView(View parentView) {
        super.initView(parentView);
        listview= (ListView)parentView. findViewById(R.id.newsfragment_listview);
        adapter = new SwipeListAdapter(activity,list);
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



    private List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }
    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    public  void  refresh()
    {
        list.clear();
        list.addAll(loadConversationList());
        adapter.notifyDataSetChanged();
    }
}

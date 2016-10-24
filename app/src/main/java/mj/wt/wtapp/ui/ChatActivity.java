package mj.wt.wtapp.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableFuture;

import mj.wt.wtapp.adapter.ChatAdapter;
import mj.wt.wtapp.R;
import mj.wt.wtapp.base.TActivity;
import mj.wt.wtapp.utils.EaseConstant;

public class ChatActivity extends TActivity implements SwipeRefreshLayout.OnRefreshListener{

    private TextView title;
    private ImageView back;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private String toChatUsername;
    private List<EMMessage> list=new ArrayList<>();
    private ChatAdapter adapter;

    private boolean isloading;
    private boolean haveMoreData = true;
    private int pagesize = 20;
    private EMConversation conversation;

    private EditText editText;
    private Button btSend;

    private InputMethodManager imm;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initData() {
        super.initData();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        toChatUsername=getIntent().getStringExtra("toChatUsername");
        conversation=EMClient.getInstance().chatManager().getConversation(toChatUsername, EMConversation.EMConversationType.Chat, true);
        conversation.markAllMessagesAsRead();
        List<EMMessage> allMessages = conversation.getAllMessages();
        if (allMessages!=null)
        {
            list.addAll(allMessages);
        }
        adapter=new ChatAdapter(activity,list);
       imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @Override
    public void initView() {
        super.initView();
        title= (TextView) findViewById(R.id.toobar_title);
        title.setText(toChatUsername);
        back= (ImageView) findViewById(R.id.toobar_back);
        listView= (ListView) findViewById(R.id.chat_listview);
        listView.setAdapter(adapter);
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        back.setOnClickListener(this);
        refreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);

        editText= (EditText) findViewById(R.id.chat_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(editText.getText().toString()))
                {
                    btSend.setSelected(false);
                }else
                {
                    btSend.setSelected(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btSend= (Button) findViewById(R.id.chat_send);
        btSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v==back)
        {
            finish();
        }else  if (v==btSend)
        {
            String content= editText.getText().toString();
            if (!TextUtils.isEmpty(content))
            {
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
                message.setChatType(EMMessage.ChatType.Chat);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                editText.setText("");
                list.add(message);
                adapter.notifyDataSetChanged();
                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            try {
                                    list.addAll(conversation.loadMoreMsgFromDB(((EMMessage)adapter.getItem(0)).getMsgId(),
                                            pagesize));
                                adapter.notifyDataSetChanged();
                            } catch (Exception e1) {
                                refreshLayout.setRefreshing(false);
                                return;
                            }
                            if (list.size() > 0) {
                                listView.setSelection(list.size() - 1);
                                if (list.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(activity, "沒有更多的消息了",
                                    Toast.LENGTH_SHORT).show();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                }, 600);
            }

    private EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            list.addAll(messages);
            adapter.notifyDataSetChanged();
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
            Log.e("tag","");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }
}

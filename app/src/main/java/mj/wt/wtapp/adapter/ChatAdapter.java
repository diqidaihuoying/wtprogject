package mj.wt.wtapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.utils.MsgParseUtil;

/**
 * Created by wantao on 2016/10/13.
 */

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<EMMessage> list;

    public ChatAdapter(Context context, List<EMMessage> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list==null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        EMMessage message= (EMMessage) getItem(position);
        if (convertView==null)
        {
            if (message.direct() == EMMessage.Direct.RECEIVE )
            {
                convertView = View.inflate(context, R.layout.ease_row_received_message,null);
            }else if (message.direct() == EMMessage.Direct.SEND)
            {
                convertView = View.inflate(context, R.layout.ease_row_sent_message,null);
            }
            viewHolder=new ViewHolder();
            findViewByid(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //设置时间搓
        setBaseTime(viewHolder,position,message);

        //设置用户头像
        viewHolder.headIv.setImageResource(R.mipmap.icon_head);

        //设置已读
        if (viewHolder.tvRead != null) {
            if (message.isAcked()) {
                viewHolder.tvRead.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvRead.setVisibility(View.INVISIBLE);
            }
        }

        //设置消息内容
        if (viewHolder.content != null)
            viewHolder.content .setText(MsgParseUtil.getMessageDigest(message, context));

        return convertView;
    }



    private void setBaseTime(ViewHolder viewHolder,int position,EMMessage message) {
        TextView timestamp=viewHolder.sendTime;
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage)getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void findViewByid(ViewHolder viewHolder,View convertView) {
        viewHolder.sendTime= (TextView) convertView.findViewById(R.id.timestamp);
        viewHolder.headIv= (ImageView) convertView.findViewById(R.id.iv_userhead);
        viewHolder.content= (TextView) convertView.findViewById(R.id.tv_chatcontent);
        viewHolder.msgStatu= (ImageView) convertView.findViewById(R.id.msg_status);
        viewHolder.tvRead= (TextView) convertView.findViewById(R.id.tv_ack);
        viewHolder.progressBar= (ProgressBar) convertView.findViewById(R.id.progress_bar);
    }




    public class ViewHolder
    {
        public ImageView headIv;
        public TextView content;
        public TextView sendTime;
        public ImageView msgStatu;
        public TextView tvRead;
        public ProgressBar progressBar;
    }

}

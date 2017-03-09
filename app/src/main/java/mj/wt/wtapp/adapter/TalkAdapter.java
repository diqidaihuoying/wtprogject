package mj.wt.wtapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.print.PageRange;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.bean.TalkInfo;
import mj.wt.wtapp.utils.ParesJsonUtil;
import mj.wt.wtapp.utils.SoftKeyBoardUtil;

import static android.R.id.edit;

/**
 * Created by wantao on 2017/3/7.
 */

public class TalkAdapter extends BaseAdapter {

    private LinkedList<TalkInfo.DataBean.ItemsBean> list;
    private Context context;
    private String currentUserName;

    private RecyclerAdapter adapter;
    public TalkAdapter(Context context,LinkedList<TalkInfo.DataBean.ItemsBean> list,String currentUserName) {
        this.context=context;
        this.currentUserName=currentUserName;
        this.list=list;

    }

    public void setRecyclerAdapter(RecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TalkHolder holder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.talk_item, null);
            holder = new TalkHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (TalkHolder) convertView.getTag();
        }
        if (list.get(position).getMsg_to().equals(currentUserName)) {
            //设置单人文本 如：万涛 ：你好
            holder.tv.setText(getSingleText(position), TextView.BufferType.SPANNABLE);
            getSingleEachWord(holder.tv,list.get(position).getMsg_from().length());
            holder.tv.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tv.setHighlightColor(context.getResources().getColor(R.color.gray_light));
        }else
        {  //设置双人文本 如：万涛 回复 肖雨：你好
            holder.tv.setText(getDoubleText(position), TextView.BufferType.SPANNABLE);
            getDoubleEachWord(holder.tv,list.get(position).getMsg_from().length(),list.get(position).getMsg_to().length());
            holder.tv.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tv.setHighlightColor(context.getResources().getColor(R.color.gray_light));
        }
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter!=null)
                    adapter.specilShowPopWindow(v,position);
            }
        });
        return convertView;
    }


    public String getSingleText(int position) {
        String str=list.get(position).getMsg_from()+"： "+list.get(position).getMsg_content();
        return str;
    }
    private String getDoubleText(int position) {
        String str=list.get(position).getMsg_from()+" 回复 "+list.get(position).getMsg_to()+": "+list.get(position).getMsg_content();
        return str;
    }
    public void getSingleEachWord(TextView textView,int end) {
        Spannable spans = (Spannable) textView.getText();
        int start = 0;
        ClickableSpan clickSpan = getClickableSpan();
        spans.setSpan(clickSpan, start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    private void getDoubleEachWord(TextView tv, int fromLength,int toLength) {
        Spannable spans = (Spannable) tv.getText();
        ClickableSpan clickSpanFrom = getClickableSpan();//发送者
        spans.setSpan(clickSpanFrom, 0, fromLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickSpanTo = getClickableSpan();//接受者
        spans.setSpan(clickSpanTo, fromLength+4, fromLength+4+toLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String s = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.color_007aff));
                ds.setUnderlineText(false);
            }

        };
    }

    class TalkHolder
    {
        TextView tv;
    }
}

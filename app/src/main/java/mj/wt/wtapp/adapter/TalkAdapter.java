package mj.wt.wtapp.adapter;

import android.content.Context;
import android.print.PageRange;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.bean.TalkInfo;
import mj.wt.wtapp.utils.ParesJsonUtil;

/**
 * Created by wantao on 2017/3/7.
 */

public class TalkAdapter extends BaseAdapter {

    private List<TalkInfo.DataBean.ItemsBean> list=new ArrayList<>();
    private Context context;
    private String currentUserName="";
    private int text;

    public TalkAdapter(Context context) {
        this.context=context;
        TalkInfo.DataBean dataBean = ParesJsonUtil.handleCitiesResponse(context);
        currentUserName=dataBean.getUsername();
        list.addAll(dataBean.getItems());

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        ClickableSpan clickSpan = getClickableSpan();
        spans.setSpan(clickSpan, 0, fromLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spans.setSpan(clickSpan, fromLength+4, fromLength+4+toLength,
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

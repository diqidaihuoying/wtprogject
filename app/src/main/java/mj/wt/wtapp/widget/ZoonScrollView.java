package mj.wt.wtapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by wantao on 2017/2/27.
 */

public class ZoonScrollView extends ScrollView {

    private ZoonScrollViewListener listener;

    public void setListener(ZoonScrollViewListener listener) {
        this.listener = listener;
    }

    public ZoonScrollView(Context context) {
        super(context);
    }

    public ZoonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoonScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener!=null)
        {
            listener.onScrollChanged(this,l,t,oldl,oldt);
        }
    }

    public interface  ZoonScrollViewListener
    {
        void onScrollChanged(ZoonScrollView scrollView, int x, int y,
                             int oldx, int oldy);
    }

}

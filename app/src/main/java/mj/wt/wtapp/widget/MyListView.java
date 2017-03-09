package mj.wt.wtapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义ListView
 * 解决ScrollView和ListView一起使用的问题，显示的时候ListView不能完全正确的显示
 * @author 刘宾  
 * @date 2014年7月11日 下午2:57:17
 */
public class MyListView extends ListView{

	public MyListView(Context context) {
	    super(context);
    }

	public MyListView(Context context, AttributeSet attrs) {
	    super(context, attrs);
    }

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
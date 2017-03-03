package mj.wt.wtapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.utils.PictureSizeUtil;

import static mj.wt.wtapp.R.id.picture_item;

/**
 * Created by wantao on 2017/3/3.
 */

public class PictureAdapter extends BaseAdapter {
    private List<String> urls;
    private Context context;
    private static final int PICTURE_TYPE_BIG = 3;
    private static final int PICTURE_TYPE_MIDDLE=4;
    private static final int PICTURE_TYPE_SMALL=5;
    private int sizeType;
    public PictureAdapter(List<String> urls,Context context,int sizeType) {
        this.urls = urls;
        this.context=context;
        this.sizeType=sizeType;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.zone_picture_item,null);
        SimpleDraweeView simpleDraweeView= (SimpleDraweeView) view.findViewById(picture_item);
        if (sizeType==PICTURE_TYPE_BIG)
        {
            simpleDraweeView.setLayoutParams(PictureSizeUtil.bigLayoutParams());
        }else if (sizeType==PICTURE_TYPE_MIDDLE)
        {
            simpleDraweeView.setLayoutParams(PictureSizeUtil.middleLayoutParams());
        }else
        {
            simpleDraweeView.setLayoutParams(PictureSizeUtil.smallLayoutParams());
        }
        simpleDraweeView.setImageURI(urls.get(position));
        return view;
    }
}

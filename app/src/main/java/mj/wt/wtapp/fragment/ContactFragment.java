package mj.wt.wtapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.basic.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.adapter.ExpandAdapter;
import mj.wt.wtapp.bean.PhotoInfo;
import mj.wt.wtapp.ui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment {


    private ExpandableListView expandableListView;
    private List<String> groupArray;
    private List<List<PhotoInfo>> childArray;
    private ExpandAdapter expandAdapter;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initData() {
        super.initData();
        groupArray = new ArrayList<>();
        groupArray.add("我的好友");
        groupArray.add("手机通讯录");
        childArray=new ArrayList<>();
        childArray.add(((MainActivity)activity).getPhotoInfos());
        childArray.add(getPhoneNumberFromMobile());
        expandAdapter=new ExpandAdapter(activity,groupArray,childArray);
    }

    @Override
    public void initView(View parentView) {
        super.initView(parentView);
        expandableListView= (ExpandableListView) parentView.findViewById(R.id.expandlistview);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(expandAdapter);
    }

    public List<PhotoInfo> getPhoneNumberFromMobile() {
        // TODO Auto-generated constructor stub
        List list = new ArrayList<PhotoInfo>();
        Cursor cursor = activity.
                getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            PhotoInfo phoneInfo = new PhotoInfo(name, number);
            list.add(phoneInfo);
        }
        return  list;
    }
}

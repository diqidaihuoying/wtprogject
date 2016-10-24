package mj.wt.wtapp.widget.dragview.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import mj.wt.wtapp.R;
import mj.wt.wtapp.utils.MsgParseUtil;
import mj.wt.wtapp.widget.dragview.listnenr.GooViewListener;
import mj.wt.wtapp.widget.dragview.view.SwipeLayout;

public class SwipeListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<EMConversation> list;
	HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();
	private  ItemClickListener itemClickListener;
	public SwipeListAdapter(Context mContext,List<EMConversation> list) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
		this.list=list;
	}

	public void setItemClickListener(ItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
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
		return list==null?0:position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		if (convertView != null) {
			mHolder = (ViewHolder) convertView.getTag();
		}else {
			convertView =  mInflater.inflate(R.layout.list_item_swipe, null);
			mHolder = ViewHolder.fromValues(convertView);
			convertView.setTag(mHolder);
		}

		// get conversation
		EMConversation conversation = (EMConversation) getItem(position);
		String username = conversation.getUserName();
		//设置用户名子
		if (conversation.getType() == EMConversation.EMConversationType.Chat) {
			mHolder.mName.setText(username);
		}
		//设置头像
		mHolder.mImage.setImageResource(R.mipmap.icon_head);
		//设置消息红点
		if (conversation.getUnreadMsgCount() > 0) {
			// show unread message count
			mHolder.mReminder.setText(String.valueOf(conversation.getUnreadMsgCount()));
			mHolder.mReminder.setVisibility(View.VISIBLE);
		} else {
			mHolder.mReminder.setVisibility(View.INVISIBLE);
		}
		if (conversation.getAllMsgCount() != 0) {
			// show the content of latest message
			EMMessage lastMessage = conversation.getLastMessage();
			//设置最新一条消息的时间
			mHolder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
			//设置最新的一条消息
			mHolder.message.setText(MsgParseUtil.getMessageDigest(lastMessage, mContext));
		}

		SwipeLayout view = (SwipeLayout) convertView;
		view.close(false, false);
		view.getFrontView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (itemClickListener!=null)
					itemClickListener.itemclick(position);
			}
		});
		view.setSwipeListener(mSwipeListener);
		mHolder.mButtonCall.setTag(position);
		mHolder.mButtonCall.setOnClickListener(onActionClick);
		mHolder.mButtonDel.setTag(position);
		mHolder.mButtonDel.setOnClickListener(onActionClick);
		mHolder.mReminder.setTag(position);
		GooViewListener mGooListener = new GooViewListener(mContext, mHolder.mReminder) {
				@Override
				public void onDisappear(PointF mDragCenter) {
					super.onDisappear(mDragCenter);
					notifyDataSetChanged();
				}
				@Override
				public void onReset(boolean isOutOfRange) {
					super.onReset(isOutOfRange);
					notifyDataSetChanged();
				}
			};
		mHolder.mReminder.setOnTouchListener(mGooListener);
		return view;
	}

	OnClickListener onActionClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer p = (Integer) v.getTag();
			int id = v.getId();
			if (id == R.id.bt_call) {
				closeAllLayout();
			} else if (id == R.id.bt_delete) {
				closeAllLayout();
			}
		}
	};

	SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener() {
		@Override
		public void onOpen(SwipeLayout swipeLayout) {
			mUnClosedLayouts.add(swipeLayout);
		}

		@Override
		public void onClose(SwipeLayout swipeLayout) {
			mUnClosedLayouts.remove(swipeLayout);
		}

		@Override
		public void onStartClose(SwipeLayout swipeLayout) {
		}

		@Override
		public void onStartOpen(SwipeLayout swipeLayout) {
			closeAllLayout();
			mUnClosedLayouts.add(swipeLayout);
		}
	};

	public int getUnClosedCount(){
		return mUnClosedLayouts.size();
	}
	
	public void closeAllLayout() {
		if(mUnClosedLayouts.size() == 0)
			return;
		for (SwipeLayout l : mUnClosedLayouts) {
			l.close(true, false);
		}
		mUnClosedLayouts.clear();
	}
	
	static class ViewHolder {
		public ImageView mImage;
		public Button mButtonCall;
		public Button mButtonDel;
		public TextView mReminder;
		public TextView mName;
		public TextView message;
		public  TextView time;
		private ViewHolder(ImageView mImage, Button mButtonCall,
				Button mButtonDel, TextView mReminder, TextView mName,TextView message,TextView time) {
			super();
			this.mImage = mImage;
			this.mButtonCall = mButtonCall;
			this.mButtonDel = mButtonDel;
			this.mReminder = mReminder;
			this.mName = mName;
			this.message=message;
			this.time=time;
		}

		public static ViewHolder fromValues(View view) {
			return new ViewHolder(
				(ImageView) view.findViewById(R.id.iv_head),
				(Button) view.findViewById(R.id.bt_call),
				(Button) view.findViewById(R.id.bt_delete),
				(TextView) view.findViewById(R.id.point),
				(TextView) view.findViewById(R.id.tv_name),(TextView)view.findViewById(R.id.tv_message),(TextView)view.findViewById(R.id.tv_time));
		}
	}
	public interface ItemClickListener
	{
		void itemclick(int position);
	}

}
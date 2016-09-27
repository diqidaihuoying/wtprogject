package mj.wt.wtapp.widget.dragview.interfaces;

import mj.wt.wtapp.widget.dragview.view.SwipeLayout;

public interface SwipeLayoutInterface {

	SwipeLayout.Status getCurrentStatus();
	
	void close();
	
	void open();
}

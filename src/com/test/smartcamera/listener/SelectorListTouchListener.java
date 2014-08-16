package com.test.smartcamera.listener;

import android.view.MotionEvent;
import android.view.View;

import com.test.smartcamera.gui.SelectorList;

public class SelectorListTouchListener implements View.OnTouchListener
{
	private SelectorList mList = null;

	public SelectorListTouchListener(SelectorList aList)
	{
		mList = aList;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch (event.getActionMasked())
		{
//		case MotionEvent.ACTION_DOWN:
//			Log.d(this.getClass().getName(), "ACTION_DOWN");
//			break;
		case MotionEvent.ACTION_UP:
//			Log.d(this.getClass().getName(), "ACTION_UP");
			if(mList.getAnnotationVisibility() == View.VISIBLE)
			{//click
				mList.setAnnotationVisibility(View.GONE);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
//			Log.d(this.getClass().getName(), "ACTION_CANCEL");
			if(mList.getAnnotationVisibility() == View.GONE)
			{//scroll
				mList.setAnnotationVisibility(View.VISIBLE);
			}
		break;
		}
		return true;
	}
}

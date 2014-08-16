package com.test.smartcamera.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class TouchableLinearLayout extends LinearLayout
{
	private View.OnTouchListener mTouchListener = null;
	
	public TouchableLinearLayout(Context aContext){super(aContext);}
	public TouchableLinearLayout(Context context, AttributeSet attrs){super(context, attrs);}
//	public TouchableLinearLayout(Context context, AttributeSet attrs, int defStyle){super(context, attrs, defStyle);}
	
	@Override
	public void setOnTouchListener (View.OnTouchListener aListener)
	{
		mTouchListener = aListener;
	}
	
	@Override
	public boolean onInterceptTouchEvent (MotionEvent ev)
	{
		if(mTouchListener != null)
		{
			mTouchListener.onTouch(this, ev);
		}
	    return false;
	}
}

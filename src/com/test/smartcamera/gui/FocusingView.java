package com.test.smartcamera.gui;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.test.smartcamera.DelayedEvent;
import com.test.smartcamera.R;
import com.test.smartcamera.resources.ApplicationConstants;

public class FocusingView 
	extends ImageView 
	implements Camera.AutoFocusCallback
{
	protected boolean mHoldState = false;

	protected DelayedRestore mDelayedRestore = new DelayedRestore();
		
	protected class DelayedRestore extends DelayedEvent
	{
		@Override
		public void run()
		{
			restoreState();
		}
	}
	
	public FocusingView(Context context){super(context);}
	public FocusingView(Context context, AttributeSet attrs){super(context, attrs);}
	public FocusingView(Context context, AttributeSet attrs, int defStyle){super(context, attrs, defStyle);}
	
	public void onPause()
	{
		restoreState();
		mDelayedRestore.onPause();
	}
	
	public void restoreState()
	{
		setImageResource(R.drawable.ic_action_locate);
		mHoldState = false;
	}
	
	public void holdState()
	{
		mHoldState = true;
	}
	
	@Override
	public void onAutoFocus(boolean success, Camera camera)
	{
		if(success == true)
		{
			//Log.d(this.getClass().getName(), "Focused");
			setImageResource(R.drawable.ic_action_photo);
			if(mHoldState == false)
			{//auto restore state
				mDelayedRestore.startEvent(ApplicationConstants.const_restore_dealy);
			}
		}
	}
}

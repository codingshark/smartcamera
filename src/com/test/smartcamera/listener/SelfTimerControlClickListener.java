package com.test.smartcamera.listener;

import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;
import com.test.smartcamera.eng.Log;

public class SelfTimerControlClickListener extends OnClickListenerBase
{
	@Override
	public void onClick(View anItem)
	{
		Log.d(this.getClass().getName(), "Clicked: " + anItem.getId());
		
		switch(anItem.getId())
		{
		case R.id.btn_self_timer_cancel:
			ApplicationContext.mSessionHandler.popAndPlaySession();
			break;
//		case R.id.btn_switch_cam://cannot switch cam in this session
		default:
			Log.e(this.getClass().getName(), "Invide control signal: " + anItem.getId());
			break;
		}
	}
}

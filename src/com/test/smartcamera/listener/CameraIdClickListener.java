package com.test.smartcamera.listener;

import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.eng.Log;

public class CameraIdClickListener extends OnClickListenerBase
{
	@Override
	public void onClick(View anItem)
	{
		Log.d(this.getClass().getName(), "clicked: " + anItem.getId());
		ApplicationContext.getCurrentSession().switchCamera(anItem.getId());
	}
}

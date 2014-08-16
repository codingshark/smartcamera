package com.test.smartcamera.listener;

import com.test.smartcamera.eng.Log;

import android.view.View;

import com.test.smartcamera.ApplicationContext;

public class FeatureSelectorListClickListener extends OnClickListenerBase
{
	@Override
	public void onClick(View anItem)
	{
		Log.d(this.getClass().getName(), "clicked: " + anItem.getId());
		ApplicationContext.getCurrentSession().showDetailedSettingList(anItem.getId());
	}
}

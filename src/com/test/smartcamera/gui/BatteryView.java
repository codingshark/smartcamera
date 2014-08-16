package com.test.smartcamera.gui;

import android.view.ViewGroup;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.BatteryMonitor;
import com.test.smartcamera.BatteryMonitor.BatteryInfoListener;
import com.test.smartcamera.R;

public class BatteryView extends StatusPanelTextView
implements BatteryInfoListener
{
	private static final String mTitle = ApplicationContext.getContext().getResources().getString(R.string.battery_view_title);
	@Override
	public void onBatteryInfo(float batteryPct)
	{
		mTextView.setText(mTitle + batteryPct + "%");
	}
	
	@Override
	public void addThisView(ViewGroup aGroup)
	{
		super.addThisView(aGroup);
		mTextView.setText(mTitle + BatteryMonitor.getBatteryPercentage(ApplicationContext.getContext()) + "%");
		ApplicationContext.mBatteryMonitor.registerBatteryInfoListener(this);
	}
	
	@Override
	public void removeThisView(ViewGroup aGroup)
	{
		super.removeThisView(aGroup);
		ApplicationContext.mBatteryMonitor.unregisterBatteryInfoListener(this);
	}
}

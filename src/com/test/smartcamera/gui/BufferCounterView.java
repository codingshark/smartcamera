package com.test.smartcamera.gui;

import android.view.ViewGroup;
import android.widget.TextView;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;

public class BufferCounterView implements StatusViewItem
{
	private MeterView mBufferMeter = null;
	private TextView mPhotoCntView = null;
	
	@Override
	public void addThisView(ViewGroup aGroup)
	{
		mBufferMeter = new MeterView();
		mBufferMeter.infateView(ApplicationContext.getContext(), R.layout.meter_view, R.string.buffer_counter_view_str_buffer);
		mBufferMeter.setProgress(0);
		aGroup.addView(mBufferMeter.getView());
		
		mPhotoCntView = new TextView(ApplicationContext.getContext());
		mPhotoCntView.setText(Integer.toString(0));
		aGroup.addView(mPhotoCntView);
	}
	
	@Override
	public void removeThisView(ViewGroup aGroup)
	{
		aGroup.removeView(mBufferMeter.getView());
		mBufferMeter = null;
		
		aGroup.removeView(mPhotoCntView);
		mPhotoCntView = null;
	}
	
	public void updateView(int aPhotoCnt, int aProgress)
	{
		mPhotoCntView.setText(Integer.toString(aPhotoCnt));
		mBufferMeter.setProgress(aProgress);
	}
}

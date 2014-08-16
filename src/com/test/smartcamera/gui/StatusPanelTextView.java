package com.test.smartcamera.gui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;

public class StatusPanelTextView implements StatusViewItem
{
	protected TextView mTextView = null;
	
	@Override
	public void addThisView(ViewGroup aGroup)
	{
		LayoutInflater vInflater = LayoutInflater.from(ApplicationContext.getContext());
		mTextView = (TextView)vInflater.inflate(R.layout.status_panel_text_view, null, false);
		aGroup.addView(mTextView);
	}
	
	@Override
	public void removeThisView(ViewGroup aGroup)
	{
		aGroup.removeView(mTextView);
	}
	
	public TextView getTextView()
	{
		return mTextView;
	}
}

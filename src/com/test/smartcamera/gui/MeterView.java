package com.test.smartcamera.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.smartcamera.R;
import com.test.smartcamera.resources.ApplicationConstants;

public class MeterView
{
	private ProgressBar mProgressBar = null;
	private TextView mTextView = null;
	private LinearLayout mLinearLayout = null;
	
	public void infateView(Context aContext, int aViewRes, int aTitleRes)
	{
		LayoutInflater vInflater = LayoutInflater.from(aContext);
		mLinearLayout = (LinearLayout)vInflater.inflate(aViewRes, null, false);
		mProgressBar = (ProgressBar)mLinearLayout.findViewById(R.id.meter_view_progressbar);
		mTextView = (TextView)mLinearLayout.findViewById(R.id.meter_view_progress_text);
		
		mTextView.setText("0%");
		
		mProgressBar.setMax(ApplicationConstants.const_max_progress);
		
		TextView vTitleText = (TextView)mLinearLayout.findViewById(R.id.meter_view_title);
		vTitleText.setText(aTitleRes);
	}
	
	public LinearLayout getView()
	{
		return mLinearLayout;
	}
	
	public void setProgress(int aProgress)
	{
		if(mProgressBar.getProgress() != aProgress)
		{
			mProgressBar.setProgress(aProgress);
			mTextView.setText((aProgress * 100)/ApplicationConstants.const_max_progress + "%");
		}
	}
}

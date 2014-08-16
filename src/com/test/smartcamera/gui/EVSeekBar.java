package com.test.smartcamera.gui;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.ListsHandler;
import com.test.smartcamera.R;
import com.test.smartcamera.eng.Log;

public class EVSeekBar extends SeekBarView
{
	protected float mStep = 0;
	
	public EVSeekBar()
	{
		mStep = ApplicationContext.getFeatureHandler().getExposureCompensationStep();
	}
	
	@Override
	protected void handleProgress(int aProgressValue)
	{
		Log.d(this.getClass().getName(), Integer.toString(aProgressValue));
		
		if(aProgressValue >= 0)
		{
			mProgressText.setText(String.format("+%.2f",
					(float)(((float)aProgressValue)*((float)mStep))));
		}else
		{
			mProgressText.setText(String.format("%.2f",
					(float)(((float)aProgressValue)*((float)mStep))));
		}
		ApplicationContext.getPreferenceHandler().setIntegerParameter(ListsHandler.list_exposure_compensation_options, 0, aProgressValue);
	}
	
	@Override
	protected int getDefaultProgress()
	{
		return (mMaxValue - mMinValue) /2;
	}
	
	@Override
	protected int getProgressDrawableRes()
	{
		return R.drawable.ev_seekbar;
	}
	
	@Override
	protected int getDefaultValueStringRes()
	{
		return R.string.str_default_exposure_compensation_value;
	}
}

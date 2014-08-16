package com.test.smartcamera.gui;

import java.util.List;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.ListsHandler;
import com.test.smartcamera.R;
import com.test.smartcamera.eng.Log;
import com.test.smartcamera.resources.ApplicationConstants;
import com.test.smartcamera.resources.DefaultValues;

public class ZoomSeekBar extends SeekBarView
{
	protected List<Integer> mZoomRatios = null;

	public ZoomSeekBar()
	{
		mZoomRatios = ApplicationContext.getFeatureHandler().getZoomRatios();
	}
	
	@Override
	protected void handleProgress(int aProgressValue)
	{
		Log.d(this.getClass().getName(), Integer.toString(aProgressValue));

		mProgressText.setText(Float.toString(
				(float)(((float)mZoomRatios.get(aProgressValue))/((float)ApplicationConstants.const_zoom_ratio)))
				+ "X");
		
		ApplicationContext.getPreferenceHandler().setIntegerParameter(ListsHandler.list_zoom_options, 0, aProgressValue);
	}
	
	@Override
	protected int getProgressDrawableRes()
	{
		return R.drawable.zoom_seekbar;
	}

	@Override
	protected int getDefaultProgress()
	{
		return DefaultValues.default_zoom_seekbar_min_value;
	}
	
	@Override
	protected int getDefaultValueStringRes()
	{
		return R.string.str_default_zoom_value;
	}
}

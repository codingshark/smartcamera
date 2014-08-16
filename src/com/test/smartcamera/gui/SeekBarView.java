package com.test.smartcamera.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;
import com.test.smartcamera.resources.DefaultValues;

public abstract class SeekBarView extends MountableGroupView
	implements SeekBar.OnSeekBarChangeListener
{
	protected TextView mProgressText = null;

//	protected int mNameRes = ApplicationConstants.null_res_id;
	protected int mMinValue = DefaultValues.default_seekbar_min_value;
	protected int mMaxValue = DefaultValues.default_seekbar_max_value;
	protected LinearLayout mRoot = null;
	
	public void onCreate(int aNameRes, int aMaxValue, int aMinValue)
	{
//		mNameRes = aNameRes;
		mMaxValue = aMaxValue;
		mMinValue = aMinValue;
		
//		mRoot = (LinearLayout)anActivity.findViewById(R.id.seekbar_view);
		mRoot = (LinearLayout)LayoutInflater.from(ApplicationContext.getContext()).inflate(R.layout.seekbar_view, null, false);
		
		mProgressText = (TextView)mRoot.findViewById(R.id.view_seekbar_value);
		mProgressText.setText(getDefaultValueStringRes());
		
		TextView vNameText = (TextView)mRoot.findViewById(R.id.view_seekbar_name);
		vNameText.setText(aNameRes);
		
		SeekBar vSeekBar = (SeekBar)mRoot.findViewById(R.id.view_seekbar);
		vSeekBar.setProgressDrawable(ApplicationContext.getContext().getResources().getDrawable(getProgressDrawableRes()));
		vSeekBar.setMax(mMaxValue - mMinValue);
		vSeekBar.setProgress(getDefaultProgress());
		vSeekBar.setOnSeekBarChangeListener(this);
	}
	
	@Override
	public void mountThis(int aPlaceHolderId)
	{
		super.mountThis(R.id.seekbar_placeholder);
	}
	
	@Override
	protected View getRootAsView()
	{
		return mRoot;
	}

	public void onStartTrackingTouch (SeekBar seekBar){}
	public void onStopTrackingTouch (SeekBar seekBar){}
	public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser)
	{
		handleProgress(progress + mMinValue);
	}

	protected abstract void handleProgress(int aProgressValue);
	protected abstract int getProgressDrawableRes();
	protected abstract int getDefaultProgress();
	protected abstract int getDefaultValueStringRes();
}

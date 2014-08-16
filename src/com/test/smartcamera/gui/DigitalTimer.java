package com.test.smartcamera.gui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.DelayedEvent;
import com.test.smartcamera.resources.ApplicationConstants;

public class DigitalTimer extends DelayedEvent
implements StatusViewItem
{
	private TextView mTextView = null;

	private long mStartTime = 0L;//in milliseconds
	private long mCountDownMilliSec = 0L;
	
	@Override
	public void addThisView(ViewGroup aGroup)
	{
		aGroup.addView(mTextView);
	}
	
	@Override
	public void removeThisView(ViewGroup aGroup)
	{
		aGroup.removeView(mTextView);
	}
	
	@Override
	public void run()
	{
		long vDiffTime = System.currentTimeMillis() - mStartTime;
		scheduleNextEvent(vDiffTime);
		showTime(vDiffTime);
	}
	
	public void start()
	{
		mStartTime = System.currentTimeMillis();
		showTime(0L);
		scheduleNextEvent(0L);
	}
	
	public void setCountDownMilliSec(long aCountDownMilliSec)
	{
		mCountDownMilliSec = aCountDownMilliSec;
	}
	
	public void infateView(int aTextViewRes,
							long aCountDownMilliSec)
	{
		LayoutInflater vInflater = LayoutInflater.from(ApplicationContext.getContext());
		mTextView = (TextView)vInflater.inflate(aTextViewRes, null, false);
		setCountDownMilliSec(aCountDownMilliSec);
	}

	private void showTime(long aMilliSec)
	{
		int secs = (int) (aMilliSec / 1000);
		
		if(mCountDownMilliSec != 0)
		{//count down stop watch
			secs = (int)(mCountDownMilliSec / 1000) - secs;
		}
		
		int mins = secs / 60;
		int hours = mins / 60;
		secs = secs % 60;
		mins = mins % 60;
		
		mTextView.setText(hours + ":"
		+ String.format("%02d", mins) + ":"
		+ String.format("%02d", secs));
	}
	
	private void scheduleNextEvent(long aMilliSec)
	{
		if(mCountDownMilliSec == 0)
		{//this is a timer
			mTimerHandler.postDelayed(this, ApplicationConstants.const_period);
		}else
		{//this is a count-down stopwatch
			if(mCountDownMilliSec != aMilliSec)
			{
				mTimerHandler.postDelayed(this, ApplicationConstants.const_period);
			}else
			{
				mTimerHandler.removeCallbacks(this);
			}
		}
	}
}
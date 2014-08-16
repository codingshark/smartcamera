package com.test.smartcamera;

import android.os.Handler;


public abstract class DelayedEvent implements Runnable
{
	protected Handler mTimerHandler = new Handler();
	
	public abstract void run();
	
	public void startEvent(int aDelay)
	{
		if(aDelay != 0)
		{
			mTimerHandler.postDelayed(this, aDelay);
		}else
		{
			run();
		}
	}
	
	public void onPause()
	{
		mTimerHandler.removeCallbacks(this);
	}
}

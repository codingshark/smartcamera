package com.test.smartcamera;

import java.util.LinkedList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.test.smartcamera.eng.Log;

public class BatteryMonitor extends BroadcastReceiver
{
	LinkedList<BatteryInfoListener> mListenerList = new LinkedList<BatteryInfoListener>();
	boolean mReceiverRegistered = false;
	
	public interface BatteryInfoListener
	{
		public void onBatteryInfo(float batteryPct);
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(listIsEmpty() == false)
		{
			float batteryPct = getBatteryPct(intent);
			Log.d(this.getClass().getName(), "Battery: " + batteryPct);
			for(BatteryInfoListener vListener: mListenerList)
			{
				vListener.onBatteryInfo(batteryPct);
			}
		}
	}
	private static float getBatteryPct(Intent anIntent)
	{
		int level = anIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = anIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		float batteryPct = level / (float)scale;
		return batteryPct;
	}
	
	private void registerThis()
	{
		if(listIsEmpty() == false)
		{
			IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			ApplicationContext.getContext().registerReceiver(this, intentfilter);
			mReceiverRegistered = true;
		}
	}
	private void unregisterThis()
	{
		if(listIsEmpty() == false)
		{
			ApplicationContext.getContext().unregisterReceiver(this);
			mReceiverRegistered = false;
		}
	}
	private boolean listIsEmpty()
	{//test emptiness, however
		//size() is expensive, use this way instead
		return mListenerList.peekFirst() == null;
	}
	public void onResume()
	{
		registerThis();
	}
	public void onPause()
	{
		unregisterThis();
	}
	
	public void registerBatteryInfoListener(BatteryInfoListener aListener)
	{
		mListenerList.add(aListener);
		if(mReceiverRegistered == false)
		{
			registerThis();
		}
	}
	public void unregisterBatteryInfoListener(BatteryInfoListener aListener)
	{
		mListenerList.remove(aListener);
		if(listIsEmpty() == true)
		{
			unregisterThis();
		}
	}
	public static float getBatteryPercentage(Context aContext)
	{
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = aContext.registerReceiver(null, ifilter);
		return getBatteryPct(batteryStatus);
	}
}


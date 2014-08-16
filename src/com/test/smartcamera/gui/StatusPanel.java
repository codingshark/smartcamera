package com.test.smartcamera.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.FileFactory;
import com.test.smartcamera.R;
import com.test.smartcamera.eng.Log;

public class StatusPanel extends MountableGroupView
{
	//------------------
	public static final int view_buffer_counter = 0;
	public static final int view_self_timer = 1;
	public static final int view_video_timer = 2;
	public static final int view_battery = 3;
	public static final int view_img_flash_mem_usage = 4;
	//TODO add new view id here
	
	private static final int VIEW_TYPE_COUNTER = 5;//TODO increase this after new view declared
	//------------------
	private StatusViewItem mViewList [] = new StatusViewItem[VIEW_TYPE_COUNTER];
	private LinearLayout mRoot = null;
	
	public void onCreate()
	{
		mRoot = (LinearLayout)LayoutInflater.from(ApplicationContext.getContext()).inflate(R.layout.status_panel_view, null, false);
	}
	
	public void showView(int aViewId)
	{
		//create buffered buffered view
		if(mViewList[aViewId] == null)
		{
			switch(aViewId)
			{
			case view_buffer_counter:
				mViewList[view_buffer_counter] = new BufferCounterView();
				break;
			case view_self_timer:
				{
					DigitalTimer vDigitalTimer = new DigitalTimer();
					vDigitalTimer.infateView(R.layout.status_panel_text_view, ApplicationContext.mShutterController.getShutterDelay());
					mViewList[view_self_timer] = vDigitalTimer;
				}
				break;
			case view_video_timer:
				{
					DigitalTimer vDigitalTimer = new DigitalTimer();
					vDigitalTimer.infateView(R.layout.status_panel_text_view, 0L);
					mViewList[view_video_timer] = vDigitalTimer;
				}
				break;
			case view_battery:
				mViewList[view_battery] = new BatteryView();
				break;
			case view_img_flash_mem_usage:
				mViewList[view_img_flash_mem_usage] = new FlashMemUsageView(FileFactory.file_type_image);
				break;
			//TODO add new views here
			default:
				Log.e(this.getClass().getName(), "Unknown view id: " + aViewId);
				break;
			}
		mViewList[aViewId].addThisView(mRoot);
		}else
		{//update state
			if(aViewId == view_self_timer)
			{
				((DigitalTimer)mViewList[view_self_timer]).setCountDownMilliSec(ApplicationContext.mShutterController.getShutterDelay());
			}
		}
	}
	public StatusViewItem getView(int aViewId)
	{
		return mViewList[aViewId];
	}
	public void removeView(int aViewId)
	{
		assert(mViewList[aViewId] != null);
		mViewList[aViewId].removeThisView(mRoot);
		mViewList[aViewId] = null;
	}
	
	@Override
	public void mountThis(int aPlaceHolderId)
	{
		super.mountThis(R.id.status_panel_placeholder);
	}
	
	@Override
	protected View getRootAsView()
	{
		return mRoot;
	}
	
	public void onResume()
	{//TODO add life cycle aware views
		if(mViewList[view_self_timer] != null)
		{
			DigitalTimer vDigitalTimer = (DigitalTimer)mViewList[view_self_timer];
			vDigitalTimer.start();
		}
		if(mViewList[view_video_timer] != null)
		{
			DigitalTimer vDigitalTimer = (DigitalTimer)mViewList[view_video_timer];
			vDigitalTimer.start();
		}
	}
	public void onPause()
	{//TODO add life cycle aware views
		if(mViewList[view_self_timer] != null)
		{
			DigitalTimer vDigitalTimer = (DigitalTimer)mViewList[view_self_timer];
			vDigitalTimer.onPause();
		}
		if(mViewList[view_video_timer] != null)
		{
			DigitalTimer vDigitalTimer = (DigitalTimer)mViewList[view_video_timer];
			vDigitalTimer.onPause();
		}
		if(mViewList[view_battery] != null)
		{//if not removed view_battery will keep monitor battery status
			removeView(view_battery);
		}
	}
	
}

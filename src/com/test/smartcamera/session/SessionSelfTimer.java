package com.test.smartcamera.session;

import android.view.WindowManager;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.DelayedEvent;
import com.test.smartcamera.ListsHandler;
import com.test.smartcamera.gui.StatusPanel;

public class SessionSelfTimer extends AbstractSession
{
	private ShutterTimer mShutterTimer = new ShutterTimer();

	class ShutterTimer extends DelayedEvent
	{
		@Override
		public void run()
		{
			ApplicationContext.mShutterController.releaseShutter();
			ApplicationContext.mSessionHandler.popAndPlaySession();
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		ApplicationContext.mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mShutterTimer.startEvent(ApplicationContext.mShutterController.getShutterDelay());
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		ApplicationContext.mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mShutterTimer.onPause();
		ApplicationContext.mShutterController.setShutterDelay(0);
	}
	
	@Override
	protected void getSettingLists()
	{
		//mFeatureList = null;
		mControlList = ApplicationContext.mListsHandler.getList(ListsHandler.list_self_timer_session_control);
	}
	
	@Override
	protected void getStatusPanel()
	{
		mStatusPanel.showView(StatusPanel.view_self_timer);
	}
}

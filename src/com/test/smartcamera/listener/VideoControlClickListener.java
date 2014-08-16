package com.test.smartcamera.listener;

import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;
import com.test.smartcamera.eng.Log;
import com.test.smartcamera.session.SessionVideo;

public class VideoControlClickListener extends OnClickListenerBase
{
	@Override
	public void onClick(View anItem)
	{
		Log.d(this.getClass().getName(), "Clicked: " + anItem.getId());
		
		ApplicationContext.getCurrentSession().detailedSettingGone();
		
		switch(anItem.getId())
		{
		case R.id.btn_video_snap:
			ApplicationContext.mShutterController.takeSnapShot();
			break;
		case R.id.btn_video_stop://Actually this is STOP recording video
			if(((SessionVideo)ApplicationContext.mSessionHandler.getCurrentSession()).canStopNow() == true)
			{
				ApplicationContext.mSessionHandler.popAndPlaySession();
			}
			break;
//		case R.id.btn_switch_cam://cannot switch cam in this session
		default:
			Log.e(this.getClass().getName(), "Invide control signal: " + anItem.getId());
			break;
		}
	}
}

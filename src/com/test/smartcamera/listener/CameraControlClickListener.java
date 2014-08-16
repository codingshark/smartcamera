package com.test.smartcamera.listener;

import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;
import com.test.smartcamera.SessionHandler;
import com.test.smartcamera.eng.Log;

public class CameraControlClickListener extends OnClickListenerBase
{
	@Override
	public void onClick(View anItem)
	{
		Log.d(this.getClass().getName(), "Clicked: " + anItem.getId());
		
		ApplicationContext.getCurrentSession().detailedSettingGone();
		
		switch(anItem.getId())
		{
		case R.id.btn_take_picture:
			ApplicationContext.mShutterController.takePicture();
			break;
		case R.id.btn_record_video:
			ApplicationContext.mSessionHandler.pushAndPlaySession(SessionHandler.session_im_video_rec);
			break;
		case R.id.btn_switch_cam:
			ApplicationContext.getCurrentSession().showDetailedSettingList(R.id.btn_switch_cam);
			break;
		default:
			Log.e(this.getClass().getName(), "Invide control signal: " + anItem.getId());
			break;
		}
	}
}

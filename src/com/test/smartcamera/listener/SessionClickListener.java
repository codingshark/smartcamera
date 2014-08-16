package com.test.smartcamera.listener;

import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;
import com.test.smartcamera.SessionHandler;
import com.test.smartcamera.activity.GalleryActivity;
import com.test.smartcamera.activity.SysSettingsActivity;
import com.test.smartcamera.eng.Log;


public class SessionClickListener extends OnClickListenerBase
{
	@Override
	public void onClick(View anItem)
	{
		Log.d(this.getClass().getName(), "clicked: " + anItem.getId());
		switch(anItem.getId())
		{
		case R.id.btn_session_professional:
			ApplicationContext.mSessionHandler.playSession(SessionHandler.session_ex_prophoto);
			break;
		case R.id.btn_session_auto:
			ApplicationContext.mSessionHandler.playSession(SessionHandler.session_ex_auto);
			break;
		case R.id.btn_session_settings:
			ApplicationContext.gotoActivity(SysSettingsActivity.class);
			break;
		case R.id.btn_session_gallery:
			ApplicationContext.gotoActivity(GalleryActivity.class);
			break;
		default:
			Log.e(this.getClass().getName(), "Invide session mode: " + anItem.getId());
			break;
		}
	}
}

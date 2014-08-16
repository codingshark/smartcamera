package com.test.smartcamera.listener;

import android.view.KeyEvent;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.eng.Log;

public class KeyEventHandler
{	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(event.getRepeatCount() == 0)Log.d(this.getClass().getName(), "Key Down: " + keyCode);
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_FOCUS:
		//Do Focus
			if(event.getRepeatCount() == 0)
			{
				ApplicationContext.mCameraHolder.startFocus();
				ApplicationContext.mFocusingView.holdState();
				ApplicationContext.getCurrentSession().detailedSettingGone();
			}
		return true;
		case KeyEvent.KEYCODE_CAMERA:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		//Do Snap or continuous shoot
			if(event.getRepeatCount() == 0)
			{
				ApplicationContext.getCurrentSession().detailedSettingGone();
				ApplicationContext.mShutterController.mShutterButtonPressed = true;
				ApplicationContext.mShutterController.takePicture();
			}
		return true;
		}
		return false;//return false if the event is not handled
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		Log.d(this.getClass().getName(), "Key Up: " + keyCode);
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_FOCUS:
		//restore focusing view state
			ApplicationContext.mFocusingView.restoreState();
		return true;
		case KeyEvent.KEYCODE_CAMERA:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		//stop continuous shooting
			ApplicationContext.mShutterController.mShutterButtonPressed = false;
		//TODO
		return true;
		}
		return false;//return false if the event is not handled
	}
}

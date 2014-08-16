package com.test.smartcamera.listener;

import android.view.MotionEvent;
import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.ListsHandler;
import com.test.smartcamera.eng.Log;
import com.test.smartcamera.resources.ApplicationConstants;
import com.test.smartcamera.resources.DefaultValues;

public class CameraPreviewGestureListener implements View.OnTouchListener
{
	private int mZooming = DefaultValues.default_zoom_seekbar_min_value;
	
	private int mMaxPointerNumEver = 0;
	private float mDistance = 0;
	private int mfilterCnt = 0;//filter out some noise at the beginning of the move event
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
//		Log.d(this.getClass().getName(), event.toString());
		//Log.d(this.getClass().getName(), "idx: " + event.getActionIndex() + ", id: " + event.getPointerId(event.getActionIndex()));
		
		// get masked (not specific to a pointer) action
		int maskedAction = event.getActionMasked();
		switch (maskedAction)
		{
		case MotionEvent.ACTION_DOWN:
//			Log.d(this.getClass().getName(), "ACTION_DOWN");
			++mMaxPointerNumEver;
			mfilterCnt = 0;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
//			Log.d(this.getClass().getName(), "ACTION_POINTER_DOWN");
			++mMaxPointerNumEver;
			if(event.getPointerCount() == 2)
			{
				mDistance = Math.abs(event.getX(1)-event.getX(0)) + Math.abs(event.getY(1)-event.getY(0));
			}
		break;
		case MotionEvent.ACTION_MOVE:// a pointer was moved
			//Log.d(this.getClass().getName(), "getHistorySize: " + event.getHistorySize());
			//Log.d(this.getClass().getName(), "getPointerCount: " + event.getPointerCount());
			if(event.getPointerCount() == 2)
			{
				float vDistanceDiff = Math.abs(event.getX(1)-event.getX(0)) + Math.abs(event.getY(1)-event.getY(0)) - mDistance;
				if(vDistanceDiff > 0)
				{
					++mfilterCnt;//filter out some noise at the beginning of the move event
				}else
				{
					--mfilterCnt;
				}
				if(Math.abs(mfilterCnt) >= ApplicationConstants.const_filter_length)
				{
					onScale(vDistanceDiff);
					--mfilterCnt;//Decrease the sensitivity a little bit
				}
				mDistance = vDistanceDiff + mDistance;
				//Log.d(this.getClass().getName(), "ACTION_MOVE");
			}
		break;
		case MotionEvent.ACTION_UP:
//			Log.d(this.getClass().getName(), "ACTION_UP");
			if(mMaxPointerNumEver == 1)
			{
				onClick();
			}
			mMaxPointerNumEver = 0;
			break;
		case MotionEvent.ACTION_POINTER_UP:
//			Log.d(this.getClass().getName(), "ACTION_POINTER_UP");
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.d(this.getClass().getName(), "ACTION_CANCEL");
			mMaxPointerNumEver = 0;
		break;
		}
		return true;
	}
	
	protected void onClick()
	{
		Log.d(this.getClass().getName(), "Click");
		ApplicationContext.mCameraHolder.startFocus();
		ApplicationContext.getCurrentSession().detailedSettingGone();
	}
	
	protected void onScale(float aDiff)
	{
		//Log.d(this.getClass().getName(), "onScale diff: " + aDiff);
		if(aDiff > 0)
		{
			++mZooming;
			if(mZooming < ApplicationContext.getFeatureHandler().getMaxZoom())
			{
				ApplicationContext.getPreferenceHandler().setIntegerParameter(ListsHandler.list_zoom_options, 0, mZooming);
			}else
			{
				--mZooming;
			}
		}else
		{
			--mZooming;
			if(mZooming >= DefaultValues.default_zoom_seekbar_min_value)
			{
				ApplicationContext.getPreferenceHandler().setIntegerParameter(ListsHandler.list_zoom_options, 0, mZooming);
			}else
			{
				++mZooming;
			}
		}
	}
}

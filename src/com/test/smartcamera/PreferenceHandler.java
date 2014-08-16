package com.test.smartcamera;

import android.hardware.Camera;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.CheckableInterface;
import com.test.smartcamera.resources.ApplicationConstants;
import com.test.smartcamera.resources.DefaultValues;

public class PreferenceHandler
{
	public void setStringParameter(int aListId, int anIndex, String aParameter)
	{
		Log.d(this.getClass().getName(), "setParameter ListId: " + aListId + "Param: " + aParameter);

		if(ApplicationContext.mShutterController.cameraBusy() == true)
		{//cancel parameter setting while camera is busy.
		//camera is busy during bursting and picture processing
			return;
		}
		
		Camera.Parameters param = ApplicationContext.mCameraHolder.getCameraParameters();
		switch(aListId)
		{
		case ListsHandler.list_antibanding_options:
			param.setAntibanding(aParameter);
			break;
		case ListsHandler.list_flash_options:
			param.setFlashMode(aParameter);
			break;
		case ListsHandler.list_color_effect_options:
			param.setColorEffect(aParameter);
			break;
		case ListsHandler.list_focus_options:
			param.setFocusMode(aParameter);
			break;
		case ListsHandler.list_scene_options:
			param.setSceneMode(aParameter);
			break;
		case ListsHandler.list_white_balance_options:
			param.setWhiteBalance(aParameter);
			break;
		//TODO add new parameter settings here
		default:
			Log.e(this.getClass().getName(), "Invalide List Id: " + aListId);
		}
		ApplicationContext.mCameraHolder.setCameraParameters(param);
		
		checkList(aListId, anIndex);
	}
	
	public void setCameraSizeParameter(int aListId, int anIndex, Camera.Size aParameter)
	{
		Log.d(this.getClass().getName(), "setParameter ListId: " + aListId + "Param: " + aParameter);

		if(ApplicationContext.mShutterController.cameraBusy() == true)
		{//cancel parameter setting while camera is busy.
		//camera is busy during bursting and picture processing
			return;
		}
		
		switch(aListId)
		{
		case ListsHandler.list_picture_size_options:
			Camera.Parameters param = ApplicationContext.mCameraHolder.getCameraParameters();
			param.setPictureSize(aParameter.width, aParameter.height);
			ApplicationContext.mCameraHolder.setCameraParameters(param);
			break;
		case ListsHandler.list_video_size_options:
			ApplicationContext.mCameraHolder.setVideoSize(aParameter.width, aParameter.height);
			break;
		//TODO add new parameter settings here
		default:
			Log.e(this.getClass().getName(), "Invalide List Id: " + aListId);
		}
		checkList(aListId, anIndex);
	}
	
	public void setIntegerParameter(int aListId, int anIndex, int aParameter)
	{
		Log.d(this.getClass().getName(), "setParameter ListId: " + aListId + "Param: " + aParameter);

		if(ApplicationContext.mShutterController.cameraBusy() == true)
		{//cancel parameter setting while camera is busy.
		//camera is busy during bursting and picture processing
			return;
		}
		
		switch(aListId)
		{
		case ListsHandler.list_exposure_compensation_options:
		{
			Camera.Parameters param = ApplicationContext.mCameraHolder.getCameraParameters();
			param.setExposureCompensation(aParameter);
			ApplicationContext.mCameraHolder.setCameraParameters(param);
		}
			break;
		case ListsHandler.list_zoom_options:
		{
			Camera.Parameters param = ApplicationContext.mCameraHolder.getCameraParameters();
			param.setZoom(aParameter);
			ApplicationContext.mCameraHolder.setCameraParameters(param);
		}
			break;
		case ListsHandler.list_burst_mode_options:
			burstModeHandler(aParameter);
			checkList(aListId, anIndex);
			break;
		case ListsHandler.list_self_timer_options:
			selfTimerHandler(aParameter);
			checkList(aListId, anIndex);
			break;
		//TODO add new parameter settings here
		default:
			Log.e(this.getClass().getName(), "Invalide List Id: " + aListId);
		}
	}

	private void checkList(int aListId, int anIndex)
	{
		CheckableInterface vCheckable = (CheckableInterface)ApplicationContext.mListsHandler.getList(aListId);
		vCheckable.mutuallyExclusiveCheck(anIndex);
	}
	
	private void burstModeHandler(int anId)
	{
		switch(anId)
		{
		case R.id.btn_burst_single_frame:
			ApplicationContext.mShutterController.setBurstMode(ShutterController.BURST_MODE_OFF);
			break;
		case R.id.btn_burst_high_resolution:
			ApplicationContext.mShutterController.setBurstMode(ShutterController.HIGH_RESOLUTION_BURST);
			break;
		case R.id.btn_burst_high_speed:
			ApplicationContext.mShutterController.setBurstMode(ShutterController.HIGH_SPEED_BURST);
			break;
		default:
			Log.e(this.getClass().getName(), "Invide burst mode: " + anId);
			break;
		}
	}
	
	private void selfTimerHandler(int anId)
	{
		switch(anId)
		{
		case R.id.btn_self_timer_off:
			ApplicationContext.mShutterController.setShutterDelay(0);
			break;
		case R.id.btn_self_timer_two_seconds:
			ApplicationContext.mShutterController.setShutterDelay(ApplicationConstants.const_two_seconds_delay);
			break;
		case R.id.btn_self_timer_ten_seconds:
			ApplicationContext.mShutterController.setShutterDelay(ApplicationConstants.const_ten_seconds_delay);
			break;
		case R.id.btn_self_timer_thirty_seconds:
			ApplicationContext.mShutterController.setShutterDelay(ApplicationConstants.const_thirty_seconds_delay);
			break;
		default:
			Log.e(this.getClass().getName(), "Invide self timer: " + anId);
			break;
		}
	}
	
	public static int getCameraIdPreference()
	{
		return DefaultValues.default_camera_id;
	}
	
	public static int getSelfTimerPreference()
	{
		return DefaultValues.default_self_timer_option;
	}
	
	public static int getBurstPreference()
	{
		return DefaultValues.default_burst_option;
	}
	
	public String getFlashPreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultFlashOption();
	}
	
	public String getAntibandingPreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultAntibandingOption();
	}
	
	public String getColorEffectPreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultColorEffectOption();
	}
	
	public String getFocusPreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultFocusOption();
	}
	
	public String getScenePreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultSceneOption();
	}
	
	public String getWhiteBalancePreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultWhiteBalanceOption();
	}
	
	public Camera.Size getPictureSizePreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultPictureSizeOption();
	}
	
	public Camera.Size getVideoSizePreference()
	{
		return ApplicationContext.getFeatureHandler().getDefaultVideoSizeOption();
	}
}

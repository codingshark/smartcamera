package com.test.smartcamera;

import java.util.List;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Build;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.EntryArrayIntInt;
import com.test.smartcamera.gui.EntryArrayIntString;
import com.test.smartcamera.gui.EntryArrayStringCameraSize;
import com.test.smartcamera.gui.EntryArrayStringInt;
import com.test.smartcamera.gui.ListEntry;
import com.test.smartcamera.resources.DefaultValues;

public class FeatureHandler
{
	public Camera.Parameters mCamParameters = null;

	private static final int mCameraOnlyFeatureNum = 3;//TODO update this number, after number feature is added
	private static final int mVideoCameraFeatureNum = 7;//TODO update this number, after number feature is added
	private static final int mSecondaryCameraFeatureNum = 2;//TODO update this number, after number feature is added
	private static final int mSessionNum = 4;//TODO update this, after session changes
	private static final int mCamSessionControlNum = 3;//TODO update this number, when new control added
	private static final int mVideoSessionControlNum = 2;//TODO update this number, when new control added
	private static final int mSelfTimerSessionControlNum = 1;//TODO update this number, when new control added
	private static final int mSelfTimerOptionNum = 4;//TODO update this number, if more option added
	private static final int mBurstOptionNum = 3;//TODO update this number, if more option added
	
	public FeatureHandler(Camera.Parameters aParameters)
	{
		assert(mCamParameters != null);
		mCamParameters = aParameters;
	}
	
	public EntryArrayIntString getAntibandingArray()
	{
		List<String> vList = mCamParameters.getSupportedAntibanding();
		if(vList != null)
		{
			EntryArrayIntString vArray = new EntryArrayIntString();
			vArray.ensureCapacity(vList.size());
			prepareAntibandingArray(vArray, vList);
			return vArray;
		}
		return null; 
	}
	public EntryArrayIntString getFlashArray()
	{
		List<String> vList = mCamParameters.getSupportedFlashModes();
		if(vList != null)
		{
			EntryArrayIntString vArray = new EntryArrayIntString();
			vArray.ensureCapacity(vList.size());
			prepareFlashArray(vArray, vList);
			return vArray;
		}
		return null;
	}
	public EntryArrayIntString getColorEffectArray()
	{
		List<String> vList = mCamParameters.getSupportedColorEffects();
		if(vList != null)
		{
			EntryArrayIntString vArray = new EntryArrayIntString();
			vArray.ensureCapacity(vList.size());
			prepareColorEffectArray(vArray, vList);
			return vArray;
		}
		return null;
	}
	public EntryArrayIntString getFocusArray()
	{
		List<String> vList = mCamParameters.getSupportedFocusModes();
		if(vList != null)
		{
			EntryArrayIntString vArray = new EntryArrayIntString();
			vArray.ensureCapacity(vList.size());
			prepareFocusArray(vArray, vList);
			return vArray;
		}
		return null;
	}
	public EntryArrayIntString getSceneArray()
	{
		List<String> vList = mCamParameters.getSupportedSceneModes();
		if(vList != null)
		{
			EntryArrayIntString vArray = new EntryArrayIntString();
			vArray.ensureCapacity(vList.size());
			prepareSceneArray(vArray, vList);
			return vArray;
		}
		return null;
	}
	public EntryArrayIntString getWhiteBalanceArray()
	{
		List<String> vList = mCamParameters.getSupportedWhiteBalance();
		if(vList != null)
		{
			EntryArrayIntString vArray = new EntryArrayIntString();
			vArray.ensureCapacity(vList.size());
			prepareWhiteBalanceArray(vArray, vList);
			return vArray;
		}
		return null;
	}
	
	public EntryArrayStringCameraSize getPictureSizeArray()
	{
		List<Camera.Size> vList = mCamParameters.getSupportedPictureSizes();
		if(vList != null)
		{
			EntryArrayStringCameraSize vArray = new EntryArrayStringCameraSize();
			vArray.ensureCapacity(vList.size());
			prepareCameraSizeArray(vArray, vList);
			return vArray;
		}
		return null;
	}
	
	/*	@TargetApi(14)
	public void getMeteringAreas()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )//API 14
		{
			Log.d(this.getClass().getName(), "Max metering: " + mCamParameters.getMaxNumMeteringAreas());
			if(mCamParameters.getMeteringAreas() != null)
			{
			Log.d(this.getClass().getName(), "Metering Areas: " + mCamParameters.getMeteringAreas().toString());
			}
		}
	}*/
	
	@TargetApi(11)
	public EntryArrayStringCameraSize getVideoSizeArray()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)//API 11
		{
			List<Camera.Size> vList = mCamParameters.getSupportedVideoSizes();
			if(vList != null)
			{
				EntryArrayStringCameraSize vArray = new EntryArrayStringCameraSize();
				vArray.ensureCapacity(vList.size());
				prepareCameraSizeArray(vArray, vList);
				return vArray;
			}
			return null;
		}
		return null;
	}

	public static EntryArrayStringInt getCameraIdArray()
	{
		EntryArrayStringInt vArray = new EntryArrayStringInt();
		vArray.ensureCapacity(Camera.getNumberOfCameras());
		prepareCameraIdArray(vArray);
		return vArray;
	}
	
	public EntryArrayIntInt getCameraFeatureSelectorArray()
	{
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mCameraOnlyFeatureNum + mVideoCameraFeatureNum + mSecondaryCameraFeatureNum);
		prepareCameraFeatureArray(vArray);
		
		assert(vArray.size() == mCameraOnlyFeatureNum + mVideoCameraFeatureNum + mSecondaryCameraFeatureNum);
		return vArray;
	}
	
	public EntryArrayIntInt getVideoFeatureSelectorArray()
	{
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mVideoCameraFeatureNum);
		prepareVideoFeatureArray(vArray);
		
		assert(vArray.size() == mVideoCameraFeatureNum);
		return vArray;
	}
	
	public static EntryArrayIntInt getSessionArray()
	{
		//Camera ID (Camera.Parameters) independent
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mSessionNum);
		
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.session_auto, R.id.btn_session_auto));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.session_professional, R.id.btn_session_professional));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.session_settings, R.id.btn_session_settings));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.session_gallery, R.id.btn_session_gallery));
		
		assert(vArray.size() == mSessionNum);
		return vArray;
	}
	
	public static EntryArrayIntInt getCamSessionControlArray()
	{
		//Camera ID (Camera.Parameters) independent
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mCamSessionControlNum);
		
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_photo, R.string.cam_session_control_take_picture, R.id.btn_take_picture));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_video, R.string.cam_session_control_record_view, R.id.btn_record_video));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.cam_session_control_switch_camera, R.id.btn_switch_cam));
		
		assert(vArray.size() == mCamSessionControlNum);
		return vArray;
	}
	
	protected static EntryArrayIntInt getVideoSessionControlArray()
	{
		//Camera ID (Camera.Parameters) independent
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mVideoSessionControlNum);
				
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.vid_session_control_snap, R.id.btn_video_snap));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.vid_session_control_stop, R.id.btn_video_stop));

		assert(vArray.size() == mVideoSessionControlNum);
		return vArray;
	}

	public static EntryArrayIntInt getSelfTimerSessionControlArray()
	{
		//Camera ID (Camera.Parameters) independent
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mSelfTimerSessionControlNum);
						
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.self_timer_session_control_cancel, R.id.btn_self_timer_cancel));

		assert(vArray.size() == mSelfTimerSessionControlNum);
		return vArray;
	}

	public static EntryArrayIntInt getSelfTimerArray()
	{
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mSelfTimerOptionNum);
						
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_self_timer_off, R.id.btn_self_timer_off));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_self_timer_two_seconds, R.id.btn_self_timer_two_seconds));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_self_timer_ten_seconds, R.id.btn_self_timer_ten_seconds));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_self_timer_thirty_seconds, R.id.btn_self_timer_thirty_seconds));
		
		assert(vArray.size() == mSelfTimerOptionNum);
		return vArray;
	}
	
	public static EntryArrayIntInt getBurstArray()
	{
		EntryArrayIntInt vArray = new EntryArrayIntInt();
		vArray.ensureCapacity(mBurstOptionNum);
						
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_burst_single_frame, R.id.btn_burst_single_frame));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_burst_high_resolution, R.id.btn_burst_high_resolution));
		vArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_burst_high_speed, R.id.btn_burst_high_speed));
		
		assert(vArray.size() == mBurstOptionNum);
		return vArray;
	}
	
	public List<Integer> getZoomRatios()
	{
		if(mCamParameters.isZoomSupported() == true)
		{//zoom supported
			return mCamParameters.getZoomRatios();
		}
		return null;
	}
	
	@TargetApi(14)
	protected boolean isVideoSnapshotSupported()
	{
		//Camera ID (Camera.Parameters) independent
		return ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			&& (mCamParameters.isVideoSnapshotSupported() == true));
	}

	public int getMaxZoom()
	{
		if(mCamParameters.isZoomSupported() == true)
		{
			return mCamParameters.getMaxZoom();
		}
		return DefaultValues.default_max_zoom;
	}
	
	public int getMaxExposureCompensation()
	{
		return mCamParameters.getMaxExposureCompensation();
	}
	
	public int getMinExposureCompensation()
	{
		return mCamParameters.getMinExposureCompensation();
	}
	
	public float getExposureCompensationStep()
	{
		return mCamParameters.getExposureCompensationStep();
	}

	private void prepareAntibandingArray(EntryArrayIntString anArray, List<String> aDataList)
	{
		if(aDataList != null)
		{
			for(String str: aDataList)
			{
				if(str.equals(Camera.Parameters.ANTIBANDING_AUTO))
				{
					anArray.add(new ListEntry<Integer, String>
						(R.drawable.ic_action_delete, R.string.menu_antibanding_auto, Camera.Parameters.ANTIBANDING_AUTO));
				}else if(str.equals(Camera.Parameters.ANTIBANDING_OFF))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_delete, R.string.menu_antibanding_off, Camera.Parameters.ANTIBANDING_OFF));
				}else if(str.equals(Camera.Parameters.ANTIBANDING_50HZ))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_delete, R.string.menu_antibanding_50hz, Camera.Parameters.ANTIBANDING_50HZ));
				}else if(str.equals(Camera.Parameters.ANTIBANDING_60HZ))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_delete, R.string.menu_antibanding_60hz, Camera.Parameters.ANTIBANDING_60HZ));
				}else
				{
					Log.e(this.getClass().getName(), "Unhandled antibanding mode: " + str);
				}
			}
		}
	}

	private void prepareFlashArray(EntryArrayIntString anArray, List<String> aDataList)
	{
		if(aDataList != null)
		{
			for(String str: aDataList)
			{
				if(str.equals(Camera.Parameters.FLASH_MODE_OFF))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_flash_off, Camera.Parameters.FLASH_MODE_OFF));
				}else if(str.equals(Camera.Parameters.FLASH_MODE_AUTO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_flash_auto, Camera.Parameters.FLASH_MODE_AUTO));
				}else if(str.equals(Camera.Parameters.FLASH_MODE_ON))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_flash_on, Camera.Parameters.FLASH_MODE_ON));
				}else if(str.equals(Camera.Parameters.FLASH_MODE_RED_EYE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_flash_red_eye, Camera.Parameters.FLASH_MODE_RED_EYE));
				}else if(str.equals(Camera.Parameters.FLASH_MODE_TORCH))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_flash_torch, Camera.Parameters.FLASH_MODE_TORCH));
				}else
				{
					Log.e(this.getClass().getName(), "Unhandled Flash mode: " + str);
				}
			}
		}
	}
	
	private void prepareColorEffectArray(EntryArrayIntString anArray, List<String> aDataList)
	{
		if(aDataList != null)
		{
			for(String str: aDataList)
			{
				if(str.equals(Camera.Parameters.EFFECT_NONE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_none, Camera.Parameters.EFFECT_NONE));
				}else if(str.equals(Camera.Parameters.EFFECT_MONO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_mono, Camera.Parameters.EFFECT_MONO));
				}else if(str.equals(Camera.Parameters.EFFECT_NEGATIVE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_negative, Camera.Parameters.EFFECT_NEGATIVE));
				}else if(str.equals(Camera.Parameters.EFFECT_SOLARIZE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_solarize, Camera.Parameters.EFFECT_SOLARIZE));
				}else if(str.equals(Camera.Parameters.EFFECT_SEPIA))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_sepia, Camera.Parameters.EFFECT_SEPIA));
				}else if(str.equals(Camera.Parameters.EFFECT_POSTERIZE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_posterize, Camera.Parameters.EFFECT_POSTERIZE));
				}else if(str.equals(Camera.Parameters.EFFECT_WHITEBOARD))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_whiteboard, Camera.Parameters.EFFECT_WHITEBOARD));
				}else if(str.equals(Camera.Parameters.EFFECT_BLACKBOARD))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_blackboard, Camera.Parameters.EFFECT_BLACKBOARD));
				}else if(str.equals(Camera.Parameters.EFFECT_AQUA))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_color_effect_aqua, Camera.Parameters.EFFECT_AQUA));
				}else
				{
					Log.e(this.getClass().getName(), "Unhandled color effect mode: " + str);
				}
			}
		}
	}
	
	private void prepareFocusArray(EntryArrayIntString anArray, List<String> aDataList)
	{
		if(aDataList != null)
		{
			for(String str: aDataList)
			{
				if(str.equals(Camera.Parameters.FOCUS_MODE_AUTO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_focus_auto, Camera.Parameters.FOCUS_MODE_AUTO));
				}else if(str.equals(Camera.Parameters.FOCUS_MODE_INFINITY))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_focus_infinity, Camera.Parameters.FOCUS_MODE_INFINITY));
				}else if(str.equals(Camera.Parameters.FOCUS_MODE_MACRO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_focus_macro, Camera.Parameters.FOCUS_MODE_MACRO));
				}else if(str.equals(Camera.Parameters.FOCUS_MODE_FIXED))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_focus_fixed, Camera.Parameters.FOCUS_MODE_FIXED));
				}else if(str.equals(Camera.Parameters.FOCUS_MODE_EDOF))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_focus_edof, Camera.Parameters.FOCUS_MODE_EDOF));
				}else if(str.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_focus_continuous_video, Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO));
				}else if(str.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_focus_continuous_picture, Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE));
				}else
				{
					Log.e(this.getClass().getName(), "Unhandled Focus mode: " + str);
				}
			}
		}
	}
	
	private void prepareSceneArray(EntryArrayIntString anArray, List<String> aDataList)
	{
		if(aDataList != null)
		{
			for(String str: aDataList)
			{
				if(str.equals(Camera.Parameters.SCENE_MODE_AUTO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_auto, Camera.Parameters.SCENE_MODE_AUTO));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_ACTION))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_action, Camera.Parameters.SCENE_MODE_ACTION));
				}
				else if(str.equals(Camera.Parameters.SCENE_MODE_PORTRAIT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_portrait, Camera.Parameters.SCENE_MODE_PORTRAIT));
				}
				else if(str.equals(Camera.Parameters.SCENE_MODE_LANDSCAPE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_landscape, Camera.Parameters.SCENE_MODE_LANDSCAPE));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_NIGHT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_night, Camera.Parameters.SCENE_MODE_NIGHT));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_NIGHT_PORTRAIT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_night_portrait, Camera.Parameters.SCENE_MODE_NIGHT_PORTRAIT));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_THEATRE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_theatre, Camera.Parameters.SCENE_MODE_THEATRE));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_BEACH))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_beach, Camera.Parameters.SCENE_MODE_BEACH));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_SNOW))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_snow, Camera.Parameters.SCENE_MODE_SNOW));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_SUNSET))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_sunset, Camera.Parameters.SCENE_MODE_SUNSET));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_STEADYPHOTO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_steady_photo, Camera.Parameters.SCENE_MODE_STEADYPHOTO));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_FIREWORKS))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_fireworks, Camera.Parameters.SCENE_MODE_FIREWORKS));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_SPORTS))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_sports, Camera.Parameters.SCENE_MODE_SPORTS));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_PARTY))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_party, Camera.Parameters.SCENE_MODE_PARTY));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_CANDLELIGHT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_candlelight, Camera.Parameters.SCENE_MODE_CANDLELIGHT));
				}else if(str.equals(Camera.Parameters.SCENE_MODE_BARCODE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_scene_barcode, Camera.Parameters.SCENE_MODE_BARCODE));
				}else
				{
					Log.e(this.getClass().getName(), "Unhandled Scene mode: " + str);
				}
			}
		}
	}
	
	private void prepareWhiteBalanceArray(EntryArrayIntString anArray, List<String> aDataList)
	{
		if(aDataList != null)
		{
			for(String str: aDataList)
			{
				if(str.equals(Camera.Parameters.WHITE_BALANCE_AUTO))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_auto, Camera.Parameters.WHITE_BALANCE_AUTO));
				}else if(str.equals(Camera.Parameters.WHITE_BALANCE_INCANDESCENT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_incandescent, Camera.Parameters.WHITE_BALANCE_INCANDESCENT));
				}else if(str.equals(Camera.Parameters.WHITE_BALANCE_FLUORESCENT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_fluorescent, Camera.Parameters.WHITE_BALANCE_FLUORESCENT));
				}else if(str.equals(Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_warm_fluorescent, Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT));
				}else if(str.equals(Camera.Parameters.WHITE_BALANCE_DAYLIGHT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_daylight, Camera.Parameters.WHITE_BALANCE_DAYLIGHT));
				}else if(str.equals(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_cloudy_daylight, Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT));
				}else if(str.equals(Camera.Parameters.WHITE_BALANCE_TWILIGHT))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_twilight, Camera.Parameters.WHITE_BALANCE_TWILIGHT));
				}else if(str.equals(Camera.Parameters.WHITE_BALANCE_SHADE))
				{
					anArray.add(new ListEntry<Integer, String>
					(R.drawable.ic_action_locate, R.string.menu_white_balance_shade, Camera.Parameters.WHITE_BALANCE_SHADE));
				}else
				{
					Log.e(this.getClass().getName(), "Unhandled white balance mode: " + str);
				}
			}
		}
	}
	
	private void prepareCameraSizeArray(EntryArrayStringCameraSize anArray, List<Camera.Size> aDataList)
	{
		if(aDataList != null)
		{
			for(Camera.Size vSize: aDataList)
			{
				anArray.add(new ListEntry<String, Camera.Size>
				(findCameraSizeIconRes(vSize), composeCameraSizeString(vSize), vSize));
			}
		}
	}
	
	private static String composeCameraSizeString(Camera.Size aData)
	{
		String suffix = "";
		float ratio = (float)(((float)aData.width) / ((float)aData.height));
		if((ratio > 1.75) && ((ratio - 1.75) < 0.1))
		{
			suffix = "(16:9)";
		}else if((ratio > 1.58) && ((ratio - 1.58) < 0.1))
		{
			suffix = "(16:10)";
		}else if((ratio > 1.48) && ((ratio - 1.48) < 0.1))
		{
			suffix = "(3:2)";
		}else if((ratio > 1.31) && ((ratio - 1.31) < 0.1))
		{
			suffix = "(4:3)";
		}else if((ratio > 1.20) && ((ratio - 1.20) < 0.1))
		{
			suffix = "(11:9)";
		}else if((ratio > 0.98) && ((ratio - 0.98) < 0.1))
		{
			suffix = "(1:1)";
		}
		return Integer.toString(aData.width) + "x" + Integer.toString(aData.height) + suffix;
	}

	private static int findCameraSizeIconRes(Camera.Size aData)
	{
		int result = DefaultValues.default_camera_size_icon;
		//TODO
		return result;
	}
	
	private static void prepareCameraIdArray(EntryArrayStringInt anArray)
	{
		int vNumberOfCameras = Camera.getNumberOfCameras();
		for(int i=0; i<vNumberOfCameras; ++i)
		{
			anArray.add(new ListEntry<String, Integer>
			(findCameraIdIconRes(i), composeCameraIdString(i), i));
		}
	}
	
	private static String composeCameraIdString(Integer aData)
	{
		Resources vRes = ApplicationContext.getContext().getResources();
		String vStrCamera = vRes.getString(R.string.str_camera_id);
		String vCamerIdStr = vStrCamera + " " + aData.toString();
		
		Camera.CameraInfo vCamInfo=new Camera.CameraInfo();
		Camera.getCameraInfo(aData.intValue(), vCamInfo);
		
		if (vCamInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
		{
			vCamerIdStr = vCamerIdStr + " " + vRes.getString(R.string.str_camera_back);
		}else if (vCamInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
		{
			vCamerIdStr = vCamerIdStr + " " + vRes.getString(R.string.str_camera_front);
		}else
		{
			Log.e(FeatureHandler.class.getName(), "Invalid CameraInfo facing");
		}
		return vCamerIdStr;
	}

	private static int findCameraIdIconRes(Integer aData)
	{
		int result = DefaultValues.default_camer_id_icon;
		//TODO
		return result;
	}
	
	private void prepareCameraFeatureArray(EntryArrayIntInt anArray)
	{
		prepareVideoCameraFeatures(anArray);
		prepareCameraOnlyFeatures(anArray);
		prepareSecondaryCameraFeatures(anArray);
	}
	
	protected void prepareSecondaryCameraFeatures(EntryArrayIntInt anArray)
	{
		anArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_title_self_timer, R.id.btn_self_timer_option));
		anArray.add(new ListEntry<Integer, Integer>
		(R.drawable.ic_action_delete, R.string.menu_title_burst_mode, R.id.btn_burst_option));
	}
	
	@TargetApi(11)
	protected void prepareCameraOnlyFeatures(EntryArrayIntInt anArray)
	{
		if(mCamParameters.getSupportedPictureSizes() != null)
		{
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_picture_size, R.id.btn_picture_size_option));
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)//API 11
		{
			if(mCamParameters.getSupportedVideoSizes() != null)
			{
				anArray.add(new ListEntry<Integer, Integer>
				(R.drawable.ic_action_delete, R.string.menu_title_video_size, R.id.btn_video_size_option));
			}
		}
		if(mCamParameters.getSupportedFocusModes() != null)
		{
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_focus, R.id.btn_focus_option));
		}
	}
	
	protected void prepareVideoCameraFeatures(EntryArrayIntInt anArray)
	{
		//TODO this part might be very sensitive to device
		if((mCamParameters.getMaxExposureCompensation() != 0) ||
			(mCamParameters.getMinExposureCompensation() != 0))
		{//exposure compensation supported
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_exposure_compensation, R.id.btn_exposure_compensation_option));
		}
		if(mCamParameters.isZoomSupported() == true)
		{//zoom supported
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_zoom, R.id.btn_zoom_option));
		}
		if(mCamParameters.getSupportedAntibanding() != null)
		{
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_antibanding, R.id.btn_antibanding_option));
		}
		if(mCamParameters.getSupportedColorEffects() != null)
		{
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_color_effect, R.id.btn_color_effect_option));
		}
		if(mCamParameters.getSupportedFlashModes() != null)
		{
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_flash, R.id.btn_flash_option));
		}
		if(mCamParameters.getSupportedSceneModes() != null)
		{
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_scene, R.id.btn_scene_option));
		}
		if(mCamParameters.getSupportedWhiteBalance() != null)
		{
			anArray.add(new ListEntry<Integer, Integer>
			(R.drawable.ic_action_delete, R.string.menu_title_white_balance, R.id.btn_white_balance_option));
		}
	}

	private void prepareVideoFeatureArray(EntryArrayIntInt anArray)
	{
		prepareVideoCameraFeatures(anArray);
	}
	
	public String getDefaultAntibandingOption()
	{
		return mCamParameters.getAntibanding();
	}
	public String getDefaultFlashOption()
	{
		return mCamParameters.getFlashMode();
	}
	
	public String getDefaultColorEffectOption()
	{
		return mCamParameters.getColorEffect();
	}
	
	public String getDefaultFocusOption()
	{
		return mCamParameters.getFocusMode();
	}
	
	public String getDefaultSceneOption()
	{
		return mCamParameters.getSceneMode();
	}
	
	public String getDefaultWhiteBalanceOption()
	{
		return mCamParameters.getWhiteBalance();
	}
	
	public Camera.Size getDefaultPictureSizeOption()
	{
		return mCamParameters.getPictureSize();
	}
	
	@TargetApi(11)
	public Camera.Size getDefaultVideoSizeOption()
	{
		return mCamParameters.getSupportedVideoSizes().get(0);
	}
}

/*
The following features have not been handled

//Gets the supported jpeg thumbnail sizes.
List<Camera.Size> 	getSupportedJpegThumbnailSizes()

//Gets the supported picture formats.
List<Integer> 	getSupportedPictureFormats()

//Gets the supported preview formats.
List<Integer> 	getSupportedPreviewFormats()

//Gets the supported preview fps (frame-per-second) ranges.
List<int[]> 	getSupportedPreviewFpsRange()

//Gets the supported preview sizes.
List<Camera.Size> 	getSupportedPreviewSizes()

//Gets the supported video frame sizes that can be used by MediaRecorder.
List<Camera.Size> 	getSupportedVideoSizes()
*/

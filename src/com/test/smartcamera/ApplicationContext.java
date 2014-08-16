package com.test.smartcamera;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Surface;
import android.view.View;

import com.test.smartcamera.gui.CameraPreview;
import com.test.smartcamera.gui.FocusingView;
import com.test.smartcamera.gui.MountableGroupView;
import com.test.smartcamera.listener.CameraPreviewGestureListener;
import com.test.smartcamera.resources.DefaultValues;
import com.test.smartcamera.session.AbstractSession;

public class ApplicationContext
{	
	public static final StorageManager mStorageManager = new StorageManager();
	public static final CameraHolder mCameraHolder = new CameraHolder();
	public static FragmentActivity mActivity = null;
	public static final ShutterController mShutterController = new ShutterController();
	public static int mCameraId = DefaultValues.default_camera_id;
	public static FocusingView mFocusingView = null;
	public static final SessionHandler mSessionHandler = new SessionHandler();
	public static CameraPreview mCameraPreview = null;
	public static final ListsHandler mListsHandler = new ListsHandler();
	public static final BatteryMonitor mBatteryMonitor = new BatteryMonitor();
	
	private static final FeatureHandler [] mCameraFeatureList = new FeatureHandler[CameraHolder.getNumberOfCameras()];
	private static final PreferenceHandler [] mPreferenceHandler = new PreferenceHandler[CameraHolder.getNumberOfCameras()];
	
	//session menu
	public static MountableGroupView mSessionList = null;
	
	private ApplicationContext(){}
	
	public static void onCreate(FragmentActivity anActivity, PreferencesSerializer anPreferencesSerializer)
	{
		assert(anActivity != null);
		mActivity = anActivity;
		
		mFocusingView = (FocusingView)mActivity.findViewById(R.id.focusing_view);

		if(mCameraHolder.onCreate(
				getContext(),
				DefaultValues.default_camera_id,
				mFocusingView) == false)
		{
			DialogueHandler.show(DialogueHandler.CAMERA_ACCESS_ERROR);
//			mActivity.finish();
//			return;
		}

		try
		{
			mStorageManager.onCreate(anPreferencesSerializer);
		}catch(Exception e)
		{
			DialogueHandler.show(DialogueHandler.ALBUM_ACCESS_ERROR);
//			mActivity.finish();
		}
        
		mSessionHandler.onCreate();
        //mCamcorderFeatures.onCreate();
        
        //handle session selection menu
      	mSessionList = mListsHandler.getList(ListsHandler.list_sessions);

        mCameraPreview = (CameraPreview)mActivity.findViewById(R.id.camera_preview);
        mCameraPreview.onCreate(mCameraHolder, new CameraPreviewGestureListener());
        
        mShutterController.onCreate();
	}
	
	public static void onResume()
	{
		mCameraHolder.onResume(mCameraPreview.getHolder());
		mSessionHandler.onResume();
		mBatteryMonitor.onResume();
		
		mSessionList.mountThis(R.id.id_session_list_placeholder);
      	mSessionList.setVisibility(View.VISIBLE);
	}
	
	public static void onPause()
	{
		mCameraHolder.onPause();
		mFocusingView.onPause();
		mSessionHandler.onPause();
		mShutterController.onPause();
		mBatteryMonitor.onPause();
		
		mSessionList.unmountThis();
	}
	public static void onStop(PreferencesSerializer anPreferencesSerializer)
	{
		 mStorageManager.onStop(anPreferencesSerializer);
//		 mCameraHolder.onStop();
		 mSessionHandler.onStop();
	}
	
	public static void switchCamera(int aCameraId)
	{
		if(aCameraId != mCameraId)
		{
			mCameraHolder.onPause();//release previous camera
			if(mCameraHolder.openCamera(aCameraId) == false)
			{//cannot open new camera
				mCameraHolder.openCamera(mCameraId);//restore previous camera
				return;
			}
			mCameraId = aCameraId;
			//TODO //notice and update mCameraPreview
			mCameraHolder.startPreview(mCameraPreview.getHolder());
			mSessionList.setVisibility(View.VISIBLE);//showDetailedSettingList makes mSessionList invisible
			
			mShutterController.onSnapFormatChange();
		}
	}

	public static Context getContext()
	{
		return mActivity.getApplicationContext();
	}
	
	public static FeatureHandler getFeatureHandler()
	{
		if(mCameraFeatureList[mCameraId] == null)
		{
			mCameraFeatureList[mCameraId] = new FeatureHandler(mCameraHolder.getCameraParameters());
		}
		return mCameraFeatureList[mCameraId];
	}
	
	public static PreferenceHandler getPreferenceHandler()
	{
		if(mPreferenceHandler[mCameraId] == null)
		{
			mPreferenceHandler[mCameraId] = new PreferenceHandler();
		}
		return mPreferenceHandler[mCameraId];
	}
	
	public static void gotoActivity(Class<?> cls)
	{
		Intent intent = new Intent(getContext(), cls);
		mActivity.startActivity(intent);
	}

	public static Surface getCameraPreviewSurface()
	{
		return mCameraPreview.getHolder().getSurface();
	}

	public static AbstractSession getCurrentSession()
	{
		return mSessionHandler.getCurrentSession();
	}
}

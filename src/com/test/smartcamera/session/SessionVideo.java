package com.test.smartcamera.session;

import java.io.File;

import android.view.WindowManager;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.DialogueHandler;
import com.test.smartcamera.FileFactory;
import com.test.smartcamera.ListsHandler;
import com.test.smartcamera.StorageManager;
import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.StatusPanel;
import com.test.smartcamera.resources.ApplicationConstants;


public class SessionVideo extends AbstractSession
{
	private long mVideoSessionStartTime = 0L; 
	
	protected void startVideoRecording()
	{
		mVideoSessionStartTime = System.currentTimeMillis();
		//Start video recording
		File vAlbum = FileFactory.getFolderHandler(FileFactory.file_type_video);
		if(vAlbum == null)
		{
			Log.e(this.getClass().getName(), "cannot access album");
			DialogueHandler.show(DialogueHandler.VIDEO_RECORDING_FILE_ERROR);
			ApplicationContext.mSessionHandler.popAndPlaySession();
		}
		if(vAlbum.getUsableSpace() < ApplicationConstants.const_flash_space_reservation)
		{
			Log.e(this.getClass().getName(), "cannot access album");
			DialogueHandler.show(DialogueHandler.LOW_FREE_SPACE);
			ApplicationContext.mSessionHandler.popAndPlaySession();
		}
		File vVideoFile = FileFactory.getNewFileHandler(vAlbum, FileFactory.file_type_video);
		if(vVideoFile == null)
		{//Error Happens
			Log.e(this.getClass().getName(), "cannot open file for video recording");
			DialogueHandler.show(DialogueHandler.VIDEO_RECORDING_FILE_ERROR);
			ApplicationContext.mSessionHandler.popAndPlaySession();
		}else
		{
			Log.d(this.getClass().getName(), "Start video recording");
			if(ApplicationContext.mCameraHolder.startVideoRecording(vVideoFile,
					StorageManager.getStorageMediaType(),
					ApplicationContext.getCameraPreviewSurface()) == false)
			{
				Log.e(this.getClass().getName(), "Cannot start video recorder");
				DialogueHandler.show(DialogueHandler.MEDIA_RECORDER_ERROR);
				ApplicationContext.mSessionHandler.popAndPlaySession();
			}
			//Keep the screen on
			ApplicationContext.mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	protected void stopVideoRecording()
	{
		if (ApplicationContext.mCameraHolder.isRecording() == true)
		{
			ApplicationContext.mCameraHolder.StopVideoRecording();
			// inform the user that recording has stopped
//            setCaptureButtonText("Capture");//TODO
			Log.d(this.getClass().getName(), "Stop video recording");
			ApplicationContext.mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}else
		{
			Log.e(this.getClass().getName(), "In video session but the camera is not recording");
		}
	}
	
	//prevent the user stop video recording immediately after
	//starting this session. Stopping just starting recording
	//will make the recording fail (RuntimeException while MediaRecorder.stop).
	//http://developer.android.com/reference/android/media/MediaRecorder.html#stop%28%29
	public boolean canStopNow()
	{
		return (System.currentTimeMillis() - mVideoSessionStartTime) 
					> ApplicationConstants.const_min_video_recording_time;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		startVideoRecording();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		stopVideoRecording();
	}
	
	@Override
	protected void getSettingLists()
	{
		mFeatureList = ApplicationContext.mListsHandler.getList(ListsHandler.list_video_feature);
		mControlList = ApplicationContext.mListsHandler.getList(ListsHandler.list_video_session_control);
	}
	
	@Override
	protected void getStatusPanel()
	{
		mStatusPanel.showView(StatusPanel.view_video_timer);
		mStatusPanel.showView(StatusPanel.view_battery);
	}
}

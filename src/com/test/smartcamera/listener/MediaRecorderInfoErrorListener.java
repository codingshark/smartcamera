package com.test.smartcamera.listener;

import android.media.MediaRecorder;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.DialogueHandler;
import com.test.smartcamera.eng.Log;

public class MediaRecorderInfoErrorListener
	implements MediaRecorder.OnErrorListener,
	MediaRecorder.OnInfoListener
{
	@Override
	public void onError(MediaRecorder mr, int what, int extra)
	{//ref:http://developer.android.com/reference/android/media/MediaRecorder.OnErrorListener.html
		//TODO
		switch(what)
		{
		case MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN:
			Log.e(this.getClass().getName(), "MEDIA_RECORDER_ERROR_UNKNOWN");
			//must be in video session, pop this session to stop video recording
			ApplicationContext.mSessionHandler.popAndPlaySession();
			DialogueHandler.show(DialogueHandler.MEDIA_RECORDER_ERROR);
			break;
		case MediaRecorder.MEDIA_ERROR_SERVER_DIED:
			Log.e(this.getClass().getName(), "MEDIA_ERROR_SERVER_DIED");
			//must be in video session, pop this session to stop video recording
			ApplicationContext.mSessionHandler.popAndPlaySession();
			DialogueHandler.show(DialogueHandler.MEDIA_RECORDER_ERROR);
			break;
		default:
			Log.e(this.getClass().getName(), "Unhandled media recorder error: " + what);
		}
	}
	
	@Override
	public void onInfo(MediaRecorder mr, int what, int extra)
	{
		switch(what)
		{
		case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
			Log.e(this.getClass().getName(), "MEDIA_RECORDER_INFO_UNKNOWN");
			//must be in video session, pop this session to stop video recording
			ApplicationContext.mSessionHandler.popAndPlaySession();
			DialogueHandler.show(DialogueHandler.MEDIA_RECORDER_ERROR);
			break;
		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
			Log.d(this.getClass().getName(), "MEDIA_RECORDER_INFO_MAX_DURATION_REACHED");
			//must be in video session, pop this session to stop video recording
			ApplicationContext.mSessionHandler.popAndPlaySession();
			DialogueHandler.show(DialogueHandler.MAX_DURATION_REACHED);
			break;
		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
			Log.d(this.getClass().getName(), "MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED");
			//must be in video session, pop this session to stop video recording
			ApplicationContext.mSessionHandler.popAndPlaySession();
			DialogueHandler.show(DialogueHandler.MAX_FILE_SIZE_REACHED);
			break;
		default:
			Log.e(this.getClass().getName(), "Unhandled media recorder info: " + what);
		}
	}
}

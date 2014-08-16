package com.test.smartcamera;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.ErrorNoticeDialog;
import com.test.smartcamera.resources.ApplicationConstants;

public class DialogueHandler
{
	public static final int LOW_FREE_SPACE = 0;
	public static final int MAX_FILE_SIZE_REACHED	= 1;
	public static final int MAX_DURATION_REACHED	= 2;
	public static final int MEDIA_RECORDER_ERROR	= 3;
	public static final int SAVE_PHOTO_ERROR	= 4;
	public static final int ALBUM_ACCESS_ERROR = 5;
	public static final int CAMERA_ACCESS_ERROR = 6;
	public static final int PHOTO_PROCESSING_ERROR = 7;
	public static final int VIDEO_RECORDING_FILE_ERROR = 8;
	public static final int LOW_PHOTO_ALBUM_MEM = 9;
	public static final int LOW_VIDEO_ALBUM_MEM = 10;
	
	public static void show(int aDialogType)
	{
		switch(aDialogType)
		{
		case LOW_FREE_SPACE:
			showErrorNotice(R.string.dialog_low_free_memory);
			break;
		case MAX_FILE_SIZE_REACHED:
			showErrorNotice(R.string.dialog_max_file_size_reached);
			break;
		case MAX_DURATION_REACHED:
			showErrorNotice(R.string.dialog_max_duration_reached);
			break;
		case MEDIA_RECORDER_ERROR:
			showErrorNotice(R.string.dialog_media_recorder_error);
			break;
		case SAVE_PHOTO_ERROR:
			showErrorNotice(R.string.dialog_save_photo_error);
			break;
		case ALBUM_ACCESS_ERROR:
			showErrorNotice(R.string.dialog_album_access_error);
			break;
		case CAMERA_ACCESS_ERROR:
			showErrorNotice(R.string.dialog_camera_access_error);
			break;
		case PHOTO_PROCESSING_ERROR:
			showErrorNotice(R.string.dialog_photo_processing_error);
			break;
		case VIDEO_RECORDING_FILE_ERROR:
			showErrorNotice(R.string.dialog_video_recording_file_error);
			break;
		case LOW_PHOTO_ALBUM_MEM:
			showErrorNotice(R.string.dialog_low_photo_album_memory);
			break;
		case LOW_VIDEO_ALBUM_MEM:
			showErrorNotice(R.string.dialog_low_video_album_memory);
			break;
		default:
			Log.e(DialogueHandler.class.getName(), "Invalid dialog type: " + aDialogType);
			break;
		}
	}
	
	private static void showErrorNotice(int aMsgRes)
	{
		ErrorNoticeDialog vDialog = new ErrorNoticeDialog();
		vDialog.setMsg(aMsgRes);
		vDialog.show(ApplicationContext.mActivity.getSupportFragmentManager(), ApplicationConstants.tag_err_dialog);
	}
}

package com.test.smartcamera;

import java.io.File;

import android.os.AsyncTask;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.FlashMemUsageView;
import com.test.smartcamera.gui.StatusPanel;
import com.test.smartcamera.resources.ApplicationConstants;

public class PhotoSaveTask extends AsyncTask<Integer, Integer, Integer>
{
	public static final int PHOTO_SAVE_SUCCEED = 0;
	public static final int PHOTO_SAVE_FILE_WRITING_ERROR = 1;
	public static final int PHOTO_SAVE_CANNOT_GET_FILE_HANDLER = 2;
	public static final int PHOTO_SAVE_FLASH_FULL = 3;
	public static final int PHOTO_SAVE_DATA_PROCESSING_FAILED = 4;
	public static final int PHOTO_SAVE_CANNOT_ACCESS_ALBUM = 5;
	
	private File mFile = null;
	private byte[] mData = null;
	private PhotoHandler.PhotoSavingCallback mPhotoSavingCallback = null;
	
	public PhotoSaveTask(PhotoHandler.PhotoSavingCallback aCallback)
	{
		assert(aCallback != null);
		mPhotoSavingCallback = aCallback;
	}
	
	public interface AsyncImageProcessor
	{//this method could be accessed concurrently by it is not synchronized
		//use it carefully
		public byte[] processImage(int anImgFormat, byte[] aData);
	}
	
	@Override
    protected Integer doInBackground(Integer... data)
	{
		onProgressUpdate(1);//fetch data
		while(mData != null)
		{
			File vParentFolder = FileFactory.getFolderHandler(FileFactory.file_type_image);
			if(vParentFolder == null)
			{
				Log.e(this.getClass().getName(), "cannot access image folder");
				return PHOTO_SAVE_CANNOT_ACCESS_ALBUM;
			}
			if(vParentFolder.getUsableSpace() < (mData.length + ApplicationConstants.const_flash_space_reservation))
			{
				Log.e(this.getClass().getName(), "Not enough free space" + vParentFolder.getPath());
				return PHOTO_SAVE_FLASH_FULL;
			}
			mFile = FileFactory.getNewFileHandler(vParentFolder, FileFactory.file_type_image);
			if(mFile == null)
			{
				Log.e(this.getClass().getName(), "cannot open file for data storage");
				return PHOTO_SAVE_CANNOT_GET_FILE_HANDLER;
			}
			if(mPhotoSavingCallback.getImageProcessor() != null)
			{
				mData = mPhotoSavingCallback.getImageProcessor().processImage(mPhotoSavingCallback.getDataFormat(), mData); 
				if(mData == null)
				{
					Log.e(this.getClass().getName(), "data preprocess failed");
					return PHOTO_SAVE_DATA_PROCESSING_FAILED;
				}
			}
			if(PhotoSavingUtils.writeFile(mFile, mData) == false)
			{
				Log.e(this.getClass().getName(), "photo writing failed");
				return PHOTO_SAVE_FILE_WRITING_ERROR;
			}
			onProgressUpdate(0);//fetch data
		}
		return PHOTO_SAVE_SUCCEED;
	}

	@Override
	protected void onProgressUpdate(Integer... isEntry)
	{
		if(isEntry[0] == 0)
		{//not the first element
			PhotoSavingUtils.broadcastImageURI(mFile);//broadcast last photo
		}
		mData = mPhotoSavingCallback.fetchData();
	}

	@Override
	protected void onPostExecute(Integer result)
	{
		mPhotoSavingCallback.finishSavingTask();
		FlashMemUsageView vFlashMemUsageView = (FlashMemUsageView)ApplicationContext.getCurrentSession().mStatusPanel.getView(StatusPanel.view_img_flash_mem_usage);
		if(vFlashMemUsageView != null)
		{
			vFlashMemUsageView.updateFlashUsage();
		}
		switch(result.intValue())
		{
		case PhotoSaveTask.PHOTO_SAVE_SUCCEED://onProgressUpdate has broadcasted the new photo
			break;
		case PhotoSaveTask.PHOTO_SAVE_CANNOT_ACCESS_ALBUM:
		case PhotoSaveTask.PHOTO_SAVE_FILE_WRITING_ERROR:
		case PhotoSaveTask.PHOTO_SAVE_CANNOT_GET_FILE_HANDLER:
			DialogueHandler.show(DialogueHandler.SAVE_PHOTO_ERROR);
			//abort following shutter events, if there's any
			mPhotoSavingCallback.abortHandling();
			break;
		case PhotoSaveTask.PHOTO_SAVE_FLASH_FULL:
			DialogueHandler.show(DialogueHandler.LOW_FREE_SPACE);
			//abort following shutter events, if there's any
			mPhotoSavingCallback.abortHandling();
			break;
		case PhotoSaveTask.PHOTO_SAVE_DATA_PROCESSING_FAILED:
			DialogueHandler.show(DialogueHandler.PHOTO_PROCESSING_ERROR);
			//abort following shutter events, if there's any
			mPhotoSavingCallback.abortHandling();
			break;
		default:
			break;
		}
	}
}

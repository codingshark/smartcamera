package com.test.smartcamera;

import java.io.File;
import java.util.LinkedList;

import android.graphics.ImageFormat;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.resources.ApplicationConstants;

public class PhotoHandler
{
	private LinkedList<byte[]> mPhotoBuffer = new LinkedList<byte[]>();
	private boolean mPhotoSavingInProgress = false;
	private PhotoSaveTask mPhotoSavingTask = null;
	private PhotoSaveTask.AsyncImageProcessor mImageProcessor = null;
	private PhotoSavingCallback mPhotoSavingCallback = new PhotoSavingCallback();
	private int mImgFormat = ImageFormat.JPEG;
	
	public PhotoHandler(
			PhotoSaveTask.AsyncImageProcessor aImageProcessor,
			int aImgFormat)
	{
		mImageProcessor = aImageProcessor;
		mImgFormat = aImgFormat;
	}
	
	public int getImgFormat()
	{
		return mImgFormat;
	}
	
	public class PhotoSavingCallback
	{
		public PhotoSaveTask.AsyncImageProcessor getImageProcessor()
		{
			return mImageProcessor;
		}
		public int getDataFormat()
		{
			return mImgFormat;
		}
		
		public void abortHandling()
		{
			Log.e(this.getClass().getName(), "abortShutterEvents");
			mPhotoBuffer.clear();
			ApplicationContext.mShutterController.abortShutterEvents();
		}
		
		public byte[] fetchData()
		{
			return mPhotoBuffer.pollFirst();
		}
		
		public void finishSavingTask()
		{
			mPhotoSavingInProgress = false;
		}
	}
	
	public void handleData(byte[] aData)
	{
		mPhotoBuffer.add(aData);
		if(mPhotoSavingInProgress == false)
		{
			mPhotoSavingInProgress = true;
			mPhotoSavingTask = new PhotoSaveTask(mPhotoSavingCallback);
			mPhotoSavingTask.execute(0);
		}
	}

	public void processSavingTask()
	{//save remaining photos in the buffer to the disk
		byte[] aPhoto = mPhotoBuffer.pollFirst();
		File vFile = null;
		File vAlbum = null;
		while(aPhoto != null)
		{
			vAlbum = FileFactory.getFolderHandler(FileFactory.file_type_image);
			if(vAlbum == null)
			{
				Log.e(this.getClass().getName(), "cannot access album");
				mPhotoSavingCallback.abortHandling();
				DialogueHandler.show(DialogueHandler.SAVE_PHOTO_ERROR);
				return;
			}
			if(vAlbum.getUsableSpace() < (aPhoto.length + ApplicationConstants.const_flash_space_reservation))
			{
				Log.e(this.getClass().getName(), "Not enough free space" + vAlbum.getPath());
				mPhotoSavingCallback.abortHandling();
				DialogueHandler.show(DialogueHandler.LOW_FREE_SPACE);
				return;
			}
			vFile = FileFactory.getNewFileHandler(vAlbum, FileFactory.file_type_image);
			if(vFile == null)
			{
				Log.e(this.getClass().getName(), "cannot open file for photo storage");
				mPhotoSavingCallback.abortHandling();
				DialogueHandler.show(DialogueHandler.SAVE_PHOTO_ERROR);
				return;
			}
			if(mImageProcessor != null)
			{
				aPhoto = mImageProcessor.processImage(getImgFormat(), aPhoto); 
				if(aPhoto == null)
				{
					Log.e(this.getClass().getName(), "data preprocess failed");
					mPhotoSavingCallback.abortHandling();
					DialogueHandler.show(DialogueHandler.PHOTO_PROCESSING_ERROR);
					return;
				}
			}
			if(PhotoSavingUtils.writeFile(vFile, aPhoto) == false)
			{
				Log.e(this.getClass().getName(), "photo data writing failed");
				mPhotoSavingCallback.abortHandling();
				DialogueHandler.show(DialogueHandler.SAVE_PHOTO_ERROR);
				return;
			}
			PhotoSavingUtils.broadcastImageURI(vFile);//broadcast last photo
			aPhoto = mPhotoBuffer.pollFirst();
		}
	}
}

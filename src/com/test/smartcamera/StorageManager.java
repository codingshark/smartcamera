package com.test.smartcamera;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.resources.ApplicationConstants;
import com.test.smartcamera.resources.DefaultValues;

public class StorageManager extends SetupWork
{	
	public static final int media_type_internal = 0;
	public static final int media_type_external = 1;
		
	public static final int file_name_rule_counter = 0;
	public static final int file_name_rule_date = 1;
	
	private static int mStorageMediaType = DefaultValues.default_storage_media_type;
	private static int mPhotoCnt = 0;
	private static int mFileNameRule = DefaultValues.defualt_file_name_rule;
	
	//run once after the application is installed
	//Setup basic preferences
	@Override
	protected boolean onSetup()
	{
		mPhotoCnt = 0;
		mFileNameRule = DefaultValues.defualt_file_name_rule;
		if(isExternalStorageWritable() == true)
		{
			mStorageMediaType = media_type_external;
		}else
		{
			mStorageMediaType = media_type_internal;
		}
		return true;
	}
	
	@Override
	protected void getPreferences(PreferencesSerializer aSerializer)
	{
		mStorageMediaType =
				aSerializer.getInt(ApplicationConstants.key_storage_media_type, 
								DefaultValues.default_storage_media_type);
		//Log.d(this.getClass().getName(), getStorageMediaType().name());
		
		mFileNameRule = 
				aSerializer.getInt(ApplicationConstants.key_file_name_rule, 
								DefaultValues.defualt_file_name_rule);
		//Log.d(this.getClass().getName(), getFileNameRule().name());
		
		mPhotoCnt = 
				aSerializer.getInt(ApplicationConstants.key_photo_cnt, DefaultValues.default_photo_cnt);
		//Log.d(this.getClass().getName(), Integer.toString(getPhotoCnt()));
	}
	
	@Override
	protected void putPreferences(PreferencesSerializer aSerializer)
	{
		aSerializer.putInt(ApplicationConstants.key_storage_media_type, DefaultValues.default_storage_media_type);
		//Log.d(this.getClass().getName(), getStorageMediaType().name());
		aSerializer.putInt(ApplicationConstants.key_file_name_rule, DefaultValues.defualt_file_name_rule);
		//Log.d(this.getClass().getName(), getFileNameRule().name());
		aSerializer.putInt(ApplicationConstants.key_photo_cnt, 0);
		//Log.d(this.getClass().getName(), Integer.toString(getPhotoCnt()));
	}
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	@Override
	protected void onStart() throws Exception
	{
		createAlbumDirectories();
		checkFreeStorageSize();
	}
	
	private void checkFreeStorageSize() throws IOException
	{
		File vImgAlbum = FileFactory.getFolderHandler(FileFactory.file_type_image);
		if(vImgAlbum == null)
		{
			Log.e(this.getClass().getName(), "Cannot access image album");
			throw new IOException("Cannot access image album");
			//TODO we could try to recover from this error
			//the user may just pulled out his SD card 
		}
		if(vImgAlbum.getUsableSpace() < ApplicationConstants.const_flash_space_warning_size)
		{
			DialogueHandler.show(DialogueHandler.LOW_PHOTO_ALBUM_MEM);
		}
		
		File vVidAlbum = FileFactory.getFolderHandler(FileFactory.file_type_video);
		if(vVidAlbum == null)
		{
			Log.e(this.getClass().getName(), "Cannot access video album");
			throw new IOException("Cannot access video album");
			//TODO we could try to recover from this error
			//the user may just pulled out his SD card 
		}
		if(vVidAlbum.getParent() != vImgAlbum.getParent())
		{
			if(vVidAlbum.getUsableSpace() < ApplicationConstants.const_flash_space_warning_size)
			{
				DialogueHandler.show(DialogueHandler.LOW_VIDEO_ALBUM_MEM);
			}
		}
	}
	
	private void createDir(File aDir) throws IOException
	{
		// Make sure the Pictures directory exists.
		aDir.mkdirs();
    	if(aDir.isDirectory() == false)
    	{
    		Log.e(this.getClass().getName(), "Cannot create directory: " + aDir.getPath());
    		throw new IOException("Cannot create dir: " + aDir.getPath());
    	}
	}
	
	private void createAlbumDirectories() throws IOException
	{
		switch(mStorageMediaType)
		{
		case media_type_internal:
			createDir(new File(ApplicationContext.getContext().getFilesDir(), ApplicationConstants.image_album_name));
			createDir(new File(ApplicationContext.getContext().getFilesDir(), ApplicationConstants.video_album_name));
			break;
		case media_type_external:
			createDir(new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES),
					ApplicationConstants.image_album_name));
			createDir(new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_MOVIES),
					ApplicationConstants.video_album_name));
			break;
		default:
			Log.e(this.getClass().getName(), "createAlbumDirectories invalid media_type: " + mStorageMediaType);
			break;
		}
	}
	
	public static int getFileNameRule()
	{
		return mFileNameRule;
	}
	public static int getStorageMediaType()
	{
		return mStorageMediaType;
	}
	public static synchronized int getPhotoCnt()
	{
		return mPhotoCnt;
	}
	public static synchronized void incPhotoCnt()
	{
		++mPhotoCnt;
	}
}

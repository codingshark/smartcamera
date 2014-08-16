package com.test.smartcamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.resources.ApplicationConstants;

public class FileFactory
{
	public static final int file_type_image = 0;
	public static final int file_type_video = 1;
	
	private FileFactory(){}
	
	//use "XXXL" + number + "th/st/nd/rd"
	private static String createSuffix(int number)
	{
		String result = ApplicationConstants.const_duplicate_file_suffix + number;
		switch(number % 10)
		{
		case 1:
			result = result + "st" + ApplicationConstants.const_duplicate_file_suffix_2;
			break;
		case 2:
			result = result + "nd" + ApplicationConstants.const_duplicate_file_suffix_2;
			break;
		case 3:
			result = result + "rd" + ApplicationConstants.const_duplicate_file_suffix_2;
			break;
		default:
			result = result + "th" + ApplicationConstants.const_duplicate_file_suffix_2;
			break;
		}
		return result;
	}
	
	private static String makeFileName(int suffixCnt, int aType)
	{
		String fileName = "";
		switch(aType)
		{
		case file_type_image:
			fileName = ApplicationConstants.image_file_prefix;
			break;
		case file_type_video:
			fileName = ApplicationConstants.video_file_prefix;
			break;
		default:
			Log.e(FileFactory.class.getName(), "Invalid file type: " + aType);
		}
		switch(StorageManager.getFileNameRule())
		{
		case StorageManager.file_name_rule_counter:
			fileName = fileName + StorageManager.getPhotoCnt();
			StorageManager.incPhotoCnt();
			break;
		case StorageManager.file_name_rule_date:
			fileName = fileName + new SimpleDateFormat(ApplicationConstants.const_date_format/*, Locale.US*/).format(new Date());
			break;
		default:
			Log.e(FileFactory.class.getName(), "Invalid naming rule: " + StorageManager.getFileNameRule());
		}

		if(suffixCnt != 0)
		{
			fileName = fileName + createSuffix(suffixCnt);
		}
		
		switch(aType)
		{
		case file_type_image:
			fileName = fileName + ApplicationConstants.jpeg_file_extension;
			break;
		case file_type_video:
			fileName = fileName + ApplicationConstants.mp4_file_extension;
			break;
		default:
			Log.e(FileFactory.class.getName(), "Invalid file type: " + aType);
		}
		return fileName;
	}

	private static File getDirFromInternalStorage(int aType)
	{
		File path = null;
		File vDir = null;
		synchronized(FileFactory.class)
		{
			vDir = ApplicationContext.getContext().getFilesDir();
		}
		switch(aType)
		{
		case file_type_image:
			path = new File(vDir, ApplicationConstants.image_album_name);
			break;
		case file_type_video:
			path = new File(vDir, ApplicationConstants.video_album_name);
			break;
		default:
			Log.e(FileFactory.class.getName(), "invalid File type: " + aType);
				break;
		}
		return path;
	}
	
	private static File getDirFromExternalSorage(int aType)
	{
		File path = null;
		switch(aType)
		{
		case file_type_image:
			path = new File(Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES),
						ApplicationConstants.image_album_name);
			break;
		case file_type_video:
			path = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_MOVIES),
					ApplicationConstants.video_album_name);
			break;
		default:
			Log.e(FileFactory.class.getName(), "invalid File type: " + aType);
			break;
		}
		return path;
	}
	
	public static File getFolderHandler(int aType)
	{
		File path = null;
		switch(StorageManager.getStorageMediaType())
		{
		case StorageManager.media_type_internal:
			path = getDirFromInternalStorage(aType);
			break;
		case StorageManager.media_type_external:
			if(StorageManager.isExternalStorageWritable() == false)
			{//external storage is not ready, try internal storage
				path = getDirFromInternalStorage(aType);
			}else
			{
				path = getDirFromExternalSorage(aType);
			}
			break;
		default:
			Log.e(FileFactory.class.getName(), "invalid StorageMediaType: " + StorageManager.getStorageMediaType());
		}
		assert(path != null);
		
		// Make sure the Pictures directory exists.
    	if(path.isDirectory() == false)
    	{
    		path.mkdirs();
    		if(path.isDirectory() == false)
    		{
    			Log.e(FileFactory.class.getName(), "Cannot create directory: " + path.getPath());
    			return null;
    		}
    	}
    	return path;
	}
	
	public static File getNewFileHandler(File aParentFolder, int aType)
	{
		File file = null;
	    int trials = 0;
	    do{
	    	file = new File(aParentFolder, makeFileName(trials, aType));
	    	++trials;
	    }while(file.exists());
	    
		return file;
	}
	
	public static float showDiskFreeSpacePct(int aType)
	{
		File vDir = FileFactory.getFolderHandler(aType);
		if(vDir != null)
		{
			int total = (int)(vDir.getTotalSpace()>>ApplicationConstants.const_space_mb_shift_factor);
			int usable = (int)(vDir.getUsableSpace()>>ApplicationConstants.const_space_mb_shift_factor);
			float percent = (usable*100)/total;
			Log.d(FileFactory.class.getName(), "Disk Usable pct: " + percent);
			return (float)(percent) / (float)(100);
		}
		return 0;
	}
	
	public static long showDiskFreeSpaceMB(int aType)
	{
		File vDir = FileFactory.getFolderHandler(aType);
		if(vDir != null)
		{
			return vDir.getUsableSpace()>>ApplicationConstants.const_space_mb_shift_factor;
		}
		return 0;
	}
}

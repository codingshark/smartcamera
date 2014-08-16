package com.test.smartcamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.net.Uri;

import com.test.smartcamera.eng.Log;

public class PhotoSavingUtils
{
	private PhotoSavingUtils(){}
	
	protected static boolean writeFile(File aFile, byte[] jpeg)
	{
		try
		{
			FileOutputStream outstream = new FileOutputStream(aFile.getPath());
			outstream.write(jpeg);
			outstream.close();
			return true;
		}catch(FileNotFoundException e)
		{
			Log.e(PhotoSavingUtils.class.getName(), aFile.getPath(), e);
			return false;
		}catch (IOException e)
		{
			Log.e(PhotoSavingUtils.class.getName(), aFile.getPath(), e);
			return false;
		}
	}
	
	protected static void broadcastImageURI(File aFile)
	{
		Uri aUri = MediaURIHandler.getMediaURI(
				aFile,
				System.currentTimeMillis(),
				0,//anOrientation, //TODO
				0,//data.length,//TODO
				null,//location //TODO
				FileFactory.file_type_image);
		
//		Log.d(this.getClass().getName(), aUri.toString());
		if(aUri != null)
		{
			MediaURIHandler.BroadcastURI(aUri, FileFactory.file_type_image);
		}
	}
}

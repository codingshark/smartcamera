package com.test.smartcamera;

import java.io.File;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;

import com.test.smartcamera.eng.Log;

public class MediaURIHandler
{
	private static final Uri EXTERNAL_IMG_URI = Images.Media.EXTERNAL_CONTENT_URI;
	private static final Uri INTERNAL_IMG_URI = Images.Media.EXTERNAL_CONTENT_URI;

	private static final Uri EXTERNAL_VID_URI = Video.Media.EXTERNAL_CONTENT_URI;
	private static final Uri INTERNAL_VID_URI = Video.Media.EXTERNAL_CONTENT_URI;
	
	private MediaURIHandler(){}
	
	// Tell the media scanner about the new file so that it is
    // immediately available to the user.
	public static Uri getMediaURI(
			File file,
			long aTime,
			int anOrientation,
			long aSize,
			Location aLocation,
			int aFileType)
	{
		ContentResolver contentResolver = ApplicationContext.getContext().getContentResolver();
			
		ContentValues values = new ContentValues(9);

		int aStorageType = StorageManager.getStorageMediaType();
		
		String tile = file.getName();
		if (tile.indexOf(".") > 0)//check avoids turning hidden files like ".profile" into ""
			tile = tile.substring(0, tile.lastIndexOf("."));
			//Log.d(MediaURIHandler.class.getName(), "New file title: " + tile);

        switch(aFileType)
		{
		case FileFactory.file_type_image:
			values.put(Images.Media.TITLE, tile);
	        // That filename is what will be handed to Gmail when a user shares a
	        // photo. Gmail gets the name of the picture attachment from the
	        // "DISPLAY_NAME" field.
	        values.put(Images.Media.DISPLAY_NAME, file.getName());
	        values.put(Images.Media.DATE_TAKEN, aTime);
	        values.put(Images.Media.ORIENTATION, anOrientation);
	        values.put(Images.Media.DATA, file.getPath());
	        values.put(Images.Media.SIZE, aSize);
	        if (aLocation != null) {
	            values.put(Images.Media.LATITUDE, aLocation.getLatitude());
	            values.put(Images.Media.LONGITUDE, aLocation.getLongitude());
	        }
			values.put(Images.Media.MIME_TYPE, "image/jpeg");
			if(aStorageType == StorageManager.media_type_external)
	        {
	        	return contentResolver.insert(EXTERNAL_IMG_URI, values);
	        }else if(aStorageType == StorageManager.media_type_internal)
	        {
	        	return contentResolver.insert(INTERNAL_IMG_URI, values);
	        }else
	        {
	        	Log.e(MediaURIHandler.class.getName(), "Invalid StorageManager.StorageMediaType type: " + aStorageType);
	        }
			break;
		case FileFactory.file_type_video:
			values.put(Video.Media.TITLE, tile);
	        // That filename is what will be handed to Gmail when a user shares a
	        // photo. Gmail gets the name of the picture attachment from the
	        // "DISPLAY_NAME" field.
	        values.put(Video.Media.DISPLAY_NAME, file.getName());
	        values.put(Video.Media.DATE_TAKEN, aTime);
	        values.put(Video.Media.DATA, file.getPath());
	        values.put(Video.Media.SIZE, aSize);
	        if (aLocation != null) {
	            values.put(Images.Media.LATITUDE, aLocation.getLatitude());
	            values.put(Images.Media.LONGITUDE, aLocation.getLongitude());
	        }
			values.put(Images.Media.MIME_TYPE, "video/mp4");
			if(aStorageType == StorageManager.media_type_external)
	        {
	        	return contentResolver.insert(EXTERNAL_VID_URI, values);
	        }else if(aStorageType == StorageManager.media_type_internal)
	        {
	        	return contentResolver.insert(INTERNAL_VID_URI, values);
	        }else
	        {
	        	Log.e(MediaURIHandler.class.getName(), "Invalid StorageManager.StorageMediaType type: " + aStorageType);
	        }
			break;
		default:
			Log.e(MediaURIHandler.class.getName(), "Invalid file type: " + aFileType);
			break;
		}

        return null;
	}
	
	public static void BroadcastURI(Uri aUri, int aType)
	{
		switch(aType)
		{
		case FileFactory.file_type_image:
			ApplicationContext.getContext().sendBroadcast(new Intent(
	                "com.android.camera.NEW_PICTURE", aUri));
			break;
		case FileFactory.file_type_video:
			ApplicationContext.getContext().sendBroadcast(new Intent(
					"android.hardware.action.NEW_VIDEO", aUri));
			break;
		default:
			Log.e(MediaURIHandler.class.getName(), "Invalid file type: " + aType);
			break;
		}
		//Log.d(MediaURIHandler.class.getName(), aUri.toString());
	}
}

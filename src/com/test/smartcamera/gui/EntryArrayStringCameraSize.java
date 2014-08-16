package com.test.smartcamera.gui;

import com.test.smartcamera.resources.ApplicationConstants;

import android.hardware.Camera;

public class EntryArrayStringCameraSize extends EntryArray<String, Camera.Size>
{
	@Override
	public int getDataType()
	{
		return ENTRY_CAMERA_SIZE_DATA;
	}
	
	@Override
	public boolean isCustomizedEntryText()
	{
		return true;
	}
	
	@Override
	public String getTextString(ListEntry<String, Camera.Size> anEntry)
	{
		return anEntry.mText;
	}
	
	@Override
	public int getTextRes(ListEntry<String, Camera.Size> anEntry)
	{
		return ApplicationConstants.null_res_id;
	}
}

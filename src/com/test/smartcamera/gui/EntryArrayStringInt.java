package com.test.smartcamera.gui;

import com.test.smartcamera.resources.ApplicationConstants;

public class EntryArrayStringInt extends EntryArray<String, Integer>
{
	@Override
	public int getDataType()
	{
		return ENTRY_INTEGER_DATA;
	}
	
	@Override
	public boolean isCustomizedEntryText()
	{
		return true;
	}
	
	@Override
	public String getTextString(ListEntry<String, Integer> anEntry)
	{
		return anEntry.mText;
	}
	
	@Override
	public int getTextRes(ListEntry<String, Integer> anEntry)
	{
		return ApplicationConstants.null_res_id;
	}
}

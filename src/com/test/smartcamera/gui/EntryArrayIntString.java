package com.test.smartcamera.gui;

public class EntryArrayIntString extends EntryArray<Integer, String>
{
	@Override
	public int getDataType()
	{
		return ENTRY_STRING_DATA;
	}
	
	@Override
	public boolean isCustomizedEntryText()
	{
		return false;
	}
	
	@Override
	public String getTextString(ListEntry<Integer, String> anEntry)
	{
		return null;
	}
	
	@Override
	public int getTextRes(ListEntry<Integer, String> anEntry)
	{
		return anEntry.mText;
	}
}

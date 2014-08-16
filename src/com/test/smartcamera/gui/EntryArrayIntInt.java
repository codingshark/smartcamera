package com.test.smartcamera.gui;


public class EntryArrayIntInt extends EntryArray<Integer, Integer>
{
	@Override
	public int getDataType()
	{
		return ENTRY_INTEGER_DATA;
	}
	
	@Override
	public boolean isCustomizedEntryText()
	{
		return false;
	}
	
	@Override
	public String getTextString(ListEntry<Integer, Integer> anEntry)
	{
		return null;
	}
	
	@Override
	public int getTextRes(ListEntry<Integer, Integer> anEntry)
	{
		return anEntry.mText;
	}
}

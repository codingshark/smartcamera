package com.test.smartcamera.gui;

import java.util.ArrayList;

public abstract class EntryArray <S, T> extends ArrayList<ListEntry<S, T> >
{
	public static final int ENTRY_STRING_DATA = 0;
	public static final int ENTRY_CAMERA_SIZE_DATA = 1;
	public static final int ENTRY_INTEGER_DATA = 2;
	
	public abstract int getDataType();
	public abstract boolean isCustomizedEntryText();
	public abstract String getTextString(ListEntry<S, T> anEntry);
	public abstract int getTextRes(ListEntry<S, T> anEntry);
	public boolean compareData(T dataA, T dataB)
	{
		return dataA.equals(dataB);
	}
}

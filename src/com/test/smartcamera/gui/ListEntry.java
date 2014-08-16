package com.test.smartcamera.gui;

import android.view.View;

public class ListEntry <S, T>
{
	protected S mText;
	protected int mIconRes = 0;
	protected T mData;
	protected View mView = null;
	
	public ListEntry(int anIconRes, S aStr, T aData)
	{
		mText = aStr;
		mIconRes = anIconRes;
		mData = aData;
	}
}

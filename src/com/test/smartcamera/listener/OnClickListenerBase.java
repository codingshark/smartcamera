package com.test.smartcamera.listener;

import android.view.View;

public abstract class OnClickListenerBase implements View.OnClickListener
{
	protected int mListId = -1;
	public void bindWithList(int aListId)
	{
		mListId = aListId;
	}
}

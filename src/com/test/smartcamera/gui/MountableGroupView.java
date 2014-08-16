package com.test.smartcamera.gui;

import android.view.View;
import android.view.ViewGroup;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.eng.Log;

public abstract class MountableGroupView
{
	//A short explanation: The layout variable is not static,
	//so it is very possible that the layout variable you use
	//to call removeView() is already another one and so it 
	//is not working for the child. To prevent that you should 
	//try to always access the parent by calling getParent() and 
	//not only by using a member variable that might already be 
	//overridden by a new object.
	//Or we should use:
	//final ViewParent parent = mViewGroup.getParent();
	//if (parent != null && parent instanceof ViewGroup)
	//{((ViewGroup) parent).removeView(mViewGroup);}
	//ref:http://stackoverflow.com/questions/4984499/illegalstateexception-in-android
	protected ViewGroup mParentViewGroup = null;//used for mount and unmount

	public void setVisibility(int visibility)
	{
		getRootAsView().setVisibility(visibility);
	}

	public void mountThis(int aPlaceHolderId)
	{
		if(mParentViewGroup == null)
		{
			mParentViewGroup = (ViewGroup)ApplicationContext.mActivity.findViewById(aPlaceHolderId);
		}
		mParentViewGroup.removeAllViews();
		mParentViewGroup.addView(getRootAsView());
	}

	public void unmountThis()
	{
		if(mParentViewGroup != null)
		{
			mParentViewGroup.removeAllViews();
			mParentViewGroup = null;
		}
	}
	
	protected abstract View getRootAsView(); 
}

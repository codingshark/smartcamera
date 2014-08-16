package com.test.smartcamera.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.smartcamera.ApplicationContext;

public abstract class SelectionList <S, T> extends MountableGroupView
{
	protected ViewGroup mRoot = null;
	protected View.OnClickListener mClickListener = null;
	protected LayoutInflater mInflater = null;
	protected EntryArray<S, T> mEntryArray = null;
	
	@Override
	protected View getRootAsView()
	{
		return mRoot;
	}
	
	public final void findGroupView(int aLayoutId,
			View.OnClickListener aListener,
			EntryArray<S, T> anEntryArray,
			T aPreference)
	{
		mClickListener = aListener;
		mInflater = LayoutInflater.from(ApplicationContext.getContext());
		mRoot = (ViewGroup)ApplicationContext.mActivity.findViewById(aLayoutId);
		mRoot.removeAllViews();
		mEntryArray = anEntryArray;
		createEntries(aPreference);
	}

	public final void infateGroupView(int aLayoutId,
				View.OnClickListener aListener,
				EntryArray<S, T> anEntryArray,
				T aPreference)
	{
		mClickListener = aListener;
		mInflater = LayoutInflater.from(ApplicationContext.getContext());
		mRoot = (ViewGroup)mInflater.inflate(aLayoutId, null, false);
		mEntryArray = anEntryArray;
		createEntries(aPreference);
	}
	
	public T getData(int anIdx)
	{
		return mEntryArray.get(anIdx).mData;
	}
	
	public int getDataType()
	{
		return mEntryArray.getDataType();
	}
	
	protected abstract void createEntries(T aPreference);
}

package com.test.smartcamera.gui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.R;

public class SelectorList extends SelectionList<Integer, Integer>
{
	private int mEntryOrientation = LinearLayout.HORIZONTAL;
	private int mAnnotationVisibility = View.GONE;
	
	public void installOnTouchListener(View.OnTouchListener aListener)
	{
		if(aListener != null)
		{
			mRoot.setOnTouchListener(aListener);
		}
	}
	
	public void setLayoutOrientation(int anOrientation)
	{
		((LinearLayout)mRoot).setOrientation(anOrientation);
	}
	
	public void setEntryOrientation(int anEntryOrientation)
	{
		mEntryOrientation = anEntryOrientation;
	}
	
	public void setAnnotationVisibility(int aVisibility)
	{
		if(mAnnotationVisibility != aVisibility)
		{
			mAnnotationVisibility = aVisibility;
			for(ListEntry<Integer, Integer> vEntry : mEntryArray)
			{
				vEntry.mView.findViewById(R.id.view_feature_name).setVisibility(aVisibility);
			}
			if(aVisibility == View.VISIBLE)
			{
				mRoot.setBackgroundColor(ApplicationContext.getContext().getResources().getColor(R.color.translucent_dark));
			}else
			{
				mRoot.setBackgroundColor(ApplicationContext.getContext().getResources().getColor(R.color.transparent));
			}
		}
	}
	
	public int getAnnotationVisibility()
	{
		return mAnnotationVisibility;
	}
	
	@Override
	protected void createEntries(Integer aPreference)
	{
		for(ListEntry<Integer, Integer> vEntry : mEntryArray)
		{
			mRoot.addView(createRow(vEntry.mIconRes, vEntry.mText, vEntry));
		}
	}
	
	private View createRow(	int iconRes,
								int textRes,
								ListEntry<Integer, Integer> anEntry)
	{
		LinearLayout aLayout = (LinearLayout)mInflater.inflate(R.layout.feature_list_entry, null, false);
		aLayout.setId(anEntry.mData);
		aLayout.setOrientation(mEntryOrientation);
		if(mClickListener != null)
		{
			aLayout.setOnClickListener(mClickListener);
		}

		createButton(aLayout, iconRes);
		createText(aLayout, textRes);
		anEntry.mView = aLayout;
		return aLayout;
	}
	
	private View createButton(ViewGroup aParent, int iconRes)
	{
		ImageButton button = (ImageButton)aParent.findViewById(R.id.view_feature_button);
		button.setImageResource(iconRes);
		return button;
	}
	
	private View createText(ViewGroup aParent, int textRes)
	{
		TextView text = (TextView)aParent.findViewById(R.id.view_feature_name);
		text.setText(textRes);
		return text;
	}
}

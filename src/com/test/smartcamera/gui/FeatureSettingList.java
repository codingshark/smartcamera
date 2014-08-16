package com.test.smartcamera.gui;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.test.smartcamera.R;
import com.test.smartcamera.resources.ApplicationConstants;

public class FeatureSettingList<S, T> extends SelectionList<S, T>
implements CheckableInterface
{
	private int mCheckedRowId = 0;
	private int mTitleId = 0;
	
	public FeatureSettingList(int aTitleId)
	{
		mTitleId = aTitleId;
	}
	
	@Override
	protected void createEntries(T aPreference)
	{
		mRoot.addView(createTitle(mTitleId));
		
		int vRowId = 0;
		if(mEntryArray.isCustomizedEntryText() == true)
		{
			for(ListEntry<S, T> vEntry : mEntryArray)
			{
				mRoot.addView(createRow(
							vEntry.mIconRes,
							ApplicationConstants.null_res_id,
							mEntryArray.getTextString(vEntry),
							vRowId,
							vEntry));
				if(mEntryArray.compareData(aPreference, vEntry.mData) == true)
				{
					mutuallyExclusiveCheck(vRowId);
				}
				++vRowId;
			}
		}else
		{
			for(ListEntry<S, T> vEntry : mEntryArray)
			{
				mRoot.addView(createRow(
						vEntry.mIconRes,
						mEntryArray.getTextRes(vEntry),
						null,
						vRowId,
						vEntry));
			if(mEntryArray.compareData(aPreference, vEntry.mData) == true)
			{
				mutuallyExclusiveCheck(vRowId);
			}
			++vRowId;
			}
		}
	}
	
	@Override
	public void mutuallyExclusiveCheck(int aRowId)
	{
		RadioButton vRadioButton = (RadioButton)mEntryArray.get(mCheckedRowId).mView;
		vRadioButton.setChecked(false);//uncheck
		
		vRadioButton = (RadioButton)mEntryArray.get(aRowId).mView;
		vRadioButton.setChecked(true);//check
		
		mCheckedRowId = aRowId;
	}
	
	private View createTitle(int titleTextRes)
	{
		FrameLayout titleFrame = (FrameLayout)mInflater.inflate(R.layout.detailed_setting_title, null, false);
		TextView titleText= (TextView)titleFrame.findViewById(R.id.detailed_setting_title_text);
		titleText.setText(titleTextRes);
		return titleFrame;
	}
	
	private View createRow(int iconRes,
							int textRes,
							String textStr,
							int aRowId,
							ListEntry<S, T> anEntry)
	{
		TableRow row = (TableRow)mInflater.inflate(R.layout.detailed_setting_row, null, false);
		row.setId(aRowId);
		if(mClickListener != null)
		{
			row.setOnClickListener(mClickListener);
		}
		
		ImageView rowIcon= (ImageView)row.findViewById(R.id.detailed_setting_row_icon);
		rowIcon.setImageResource(iconRes);
		TextView rowText= (TextView)row.findViewById(R.id.detailed_setting_row_text);
		if(textRes != ApplicationConstants.null_res_id)
		{
			rowText.setText(textRes);
		}else if(textStr != null)
		{
			rowText.setText(textStr);
		}
		RadioButton rowButton= (RadioButton)row.findViewById(R.id.detailed_setting_row_button);
		rowButton.setClickable(false);
		anEntry.mView = rowButton;
		return row;
	}
}

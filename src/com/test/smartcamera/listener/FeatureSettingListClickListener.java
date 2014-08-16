package com.test.smartcamera.listener;

import android.hardware.Camera;
import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.EntryArray;
import com.test.smartcamera.gui.SelectionList;

public class FeatureSettingListClickListener extends OnClickListenerBase
{
	@Override
	public void onClick(View anItem)
	{
		SelectionList aList = (SelectionList)ApplicationContext.mListsHandler.getList(mListId);
		switch(aList.getDataType())
		{
			case EntryArray.ENTRY_STRING_DATA:
				ApplicationContext.getPreferenceHandler().
				setStringParameter(mListId, anItem.getId(), ((SelectionList<?, String>)aList).getData(anItem.getId()));
				break;
			case EntryArray.ENTRY_CAMERA_SIZE_DATA:
				ApplicationContext.getPreferenceHandler().
				setCameraSizeParameter(mListId, anItem.getId(), ((SelectionList<?, Camera.Size>)aList).getData(anItem.getId()));
				break;
			case EntryArray.ENTRY_INTEGER_DATA:
				ApplicationContext.getPreferenceHandler().
				setIntegerParameter(mListId, anItem.getId(), ((SelectionList<?, Integer>)aList).getData(anItem.getId()));
				break;
			default:
				Log.e(this.getClass().getName(), "Invalid data type: " + aList.getDataType());
				break;
		}
	}
}

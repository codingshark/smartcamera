package com.test.smartcamera.gui;

import android.view.ViewGroup;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.FileFactory;
import com.test.smartcamera.R;
import com.test.smartcamera.resources.DefaultValues;

public class FlashMemUsageView extends StatusPanelTextView
{
	private static final String mTitle = ApplicationContext.getContext().getResources().getString(R.string.flash_usage_view_title);
	public int mDirType = DefaultValues.default_dir_type;
	
	public FlashMemUsageView(int aType)
	{
		mDirType = aType;
	}
	
	@Override
	public void addThisView(ViewGroup aGroup)
	{
		super.addThisView(aGroup);
		updateFlashUsage();
	}
	
	public void updateFlashUsage()
	{
		long vFreeSpace = FileFactory.showDiskFreeSpaceMB(mDirType);
		if(vFreeSpace > 1024)
		{//Unit in GB
			vFreeSpace = vFreeSpace >> 10;
			mTextView.setText(mTitle + vFreeSpace + "GB");
		}else
		{//Unit in MB
			mTextView.setText(mTitle + vFreeSpace + "MB");
		}
	}
}

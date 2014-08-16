package com.test.smartcamera.session;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.ListsHandler;
import com.test.smartcamera.gui.StatusPanel;

public class SessionProPhoto extends AbstractSession
{
	@Override
	protected void getSettingLists()
	{
		 mFeatureList = ApplicationContext.mListsHandler.getList(ListsHandler.list_camera_feature);
		 mControlList = ApplicationContext.mListsHandler.getList(ListsHandler.list_camera_session_control);
	}
	
	@Override
	protected void getStatusPanel()
	{
		mStatusPanel.showView(StatusPanel.view_img_flash_mem_usage);
	}
}

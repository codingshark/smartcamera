package com.test.smartcamera.session;

import android.view.View;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.ListsHandler;
import com.test.smartcamera.R;
import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.MountableGroupView;
import com.test.smartcamera.gui.SelectorList;
import com.test.smartcamera.gui.StatusPanel;


public abstract class AbstractSession
{
	protected boolean mCreated = false;
	protected MountableGroupView mCurrentDetailedSettingList = null;
	
	protected MountableGroupView mControlList = null;
	protected MountableGroupView mFeatureList = null;
	
	public StatusPanel mStatusPanel = new StatusPanel();

	protected static final int VISIBILITY_UNCHANGED = 0x0;
	public static final int FEATURE_GONE = 0x1;
	public static final int FEATURE_INVISIBLE = 0x2;
	public static final int FEATURE_VISIBLE = 0x4;
	protected static final int FEATURE_MASK = 0x7;
	
	public static final int CONTROL_GONE = 0x10;
	public static final int CONTROL_INVISIBLE = 0x20;
	public static final int CONTROL_VISIBLE = 0x40;
	protected static final int CONTROL_MASK = 0x70;
	
	public static final int DETAILED_SETTING_GONE = 0x100;
	public static final int DETAILED_SETTING_INVISIBLE = 0x200;
	public static final int DETAILED_SETTING_VISIBLE = 0x400;
	protected static final int DETAILED_SETTING_MASK = 0x700;
	
	public void switchCamera(int aCameraId)
	{
		if(ApplicationContext.mCameraId != aCameraId)
		{
			ApplicationContext.switchCamera(aCameraId);
			removeLayouts();
			loadLayouts();
		}
	}
	
	public void resumeAndCreate()
	{//if the session is not in foreground, then delay its creation
	//This is an optimization. Since not all session should be created
		//at the start time of the application
		if(mCreated == false)
		{//Create this session first and then resume
			onCreate();
			mCreated = true;
		}
		onResume();
	}
	
	private void loadLayouts()
	{
		getStatusPanel();
		getSettingLists();
		if(mStatusPanel != null)
		{
			mStatusPanel.mountThis(R.id.status_panel_placeholder);
		}
		if(mControlList != null)
		{
			mControlList.mountThis(R.id.id_control_list_placeholder);
		}
		if(mFeatureList != null)
		{
			mFeatureList.mountThis(R.id.id_feature_list_placeholder);
		}
		setVisibility(FEATURE_VISIBLE|CONTROL_VISIBLE);
		mStatusPanel.setVisibility(View.VISIBLE);
	}
	private void removeLayouts()
	{
		if(mStatusPanel != null)
		{
			mStatusPanel.unmountThis();
		}
		if(mControlList != null)
		{
			mControlList.unmountThis();
		}
		if(mFeatureList != null)
		{
			mFeatureList.unmountThis();
		}
		if(mCurrentDetailedSettingList != null)
		{
			mCurrentDetailedSettingList.unmountThis();
		}
	}
	
	public void onCreate()
	{
		//Save new values
		mStatusPanel.onCreate();
	}
	
	public void onStop()
	{
		removeLayouts();
	}
	
	public void onResume()
	{
		loadLayouts();
		mStatusPanel.onResume();
	}
	
	public void onPause()
	{
		mStatusPanel.onPause();
		removeLayouts();
	}
	
	private void setVisibility(int aVisibility)
	{
		if(mFeatureList != null)
		{
			switch(aVisibility & FEATURE_MASK)
			{
			case VISIBILITY_UNCHANGED:
				break;
			case FEATURE_GONE:
				mFeatureList.setVisibility(View.GONE);
				break;
			case FEATURE_INVISIBLE:
				mFeatureList.setVisibility(View.INVISIBLE);
				break;
			case FEATURE_VISIBLE:
				mFeatureList.setVisibility(View.VISIBLE);
				break;
			default:
				Log.e(this.getClass().getName(), "Invalid session visibility, FEATURE: " + aVisibility);
				break;
			}
		}
		
		if(mControlList != null)
		{
			switch(aVisibility & CONTROL_MASK)
			{
			case VISIBILITY_UNCHANGED:
				break;
			case CONTROL_GONE:
				mControlList.setVisibility(View.GONE);
				break;
			case CONTROL_INVISIBLE:
				mControlList.setVisibility(View.INVISIBLE);
				break;
			case CONTROL_VISIBLE:
				mControlList.setVisibility(View.VISIBLE);
				break;
			default:
				Log.e(this.getClass().getName(), "Invalid session visibility, CONTROL: " + aVisibility);
				break;
			}
		}
		if(mCurrentDetailedSettingList != null)
		{
			switch(aVisibility & DETAILED_SETTING_MASK)
			{
			case VISIBILITY_UNCHANGED:
				break;
			case DETAILED_SETTING_GONE:
				mCurrentDetailedSettingList.setVisibility(View.GONE);
				break;
			case DETAILED_SETTING_INVISIBLE:
				mCurrentDetailedSettingList.setVisibility(View.INVISIBLE);
				break;
			case DETAILED_SETTING_VISIBLE:
				mCurrentDetailedSettingList.setVisibility(View.VISIBLE);
				break;
			default:
				Log.e(this.getClass().getName(), "Invalid session visibility, DETAILED_SETTING: " + aVisibility);
				break;
			}
		}
	}
	
	public void showDetailedSettingList(int aListId)
	{
		ApplicationContext.mSessionList.setVisibility(View.INVISIBLE);
		mStatusPanel.setVisibility(View.INVISIBLE);
		setVisibility(DETAILED_SETTING_GONE);
		findCurrentDetailedSettingList(aListId);
		mCurrentDetailedSettingList.mountThis(R.id.detailed_setting_scroll_view);
		setVisibility(DETAILED_SETTING_VISIBLE);
	}
	
	public void detailedSettingGone()
	{
		setVisibility(DETAILED_SETTING_GONE);
		ApplicationContext.mSessionList.setVisibility(View.VISIBLE);
		mStatusPanel.setVisibility(View.VISIBLE);
		AnnotationGone();
	}
	
	protected void AnnotationGone()
	{
		((SelectorList)mFeatureList).setAnnotationVisibility(View.GONE);
		((SelectorList)ApplicationContext.mSessionList).setAnnotationVisibility(View.GONE);
	}
	
	protected void findCurrentDetailedSettingList(int aListId)
	{
		int vListId = 0;
		switch(aListId)
		{
		case R.id.btn_antibanding_option:
			vListId = ListsHandler.list_antibanding_options;
			break;
		case R.id.btn_color_effect_option:
			vListId = ListsHandler.list_color_effect_options;
			break;
		case R.id.btn_flash_option:
			vListId = ListsHandler.list_flash_options;
			break;
		case R.id.btn_focus_option:
			vListId = ListsHandler.list_focus_options;
			break;
		case R.id.btn_scene_option:
			vListId = ListsHandler.list_scene_options;
			break;
		case R.id.btn_white_balance_option:
			vListId = ListsHandler.list_white_balance_options;
			break;
		case R.id.btn_picture_size_option:
			vListId = ListsHandler.list_picture_size_options;
			break;
		case R.id.btn_switch_cam:
			vListId = ListsHandler.list_camera_id_selection;
			break;
		case R.id.btn_video_size_option:
			vListId = ListsHandler.list_video_size_options;
			break;
		case R.id.btn_zoom_option:
			vListId = ListsHandler.list_zoom_options;
			break;
		case R.id.btn_exposure_compensation_option:
			vListId = ListsHandler.list_exposure_compensation_options;
			break;
		case R.id.btn_self_timer_option:
			vListId = ListsHandler.list_self_timer_options;
			break;
		case R.id.btn_burst_option:
			vListId = ListsHandler.list_burst_mode_options;
			break;
		//TODO add more Option list here
		default:
			Log.e(this.getClass().getName(), "Unknown List id: " + aListId);
			break;
		}
		mCurrentDetailedSettingList = ApplicationContext.mListsHandler.getList(vListId);
	}

	protected abstract void getSettingLists();
	protected abstract void getStatusPanel();
}

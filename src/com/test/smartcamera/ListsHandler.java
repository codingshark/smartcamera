package com.test.smartcamera;

import android.view.View;
import android.widget.LinearLayout;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.EVSeekBar;
import com.test.smartcamera.gui.EntryArray;
import com.test.smartcamera.gui.FeatureSettingList;
import com.test.smartcamera.gui.MountableGroupView;
import com.test.smartcamera.gui.SelectorList;
import com.test.smartcamera.gui.ZoomSeekBar;
import com.test.smartcamera.listener.CameraControlClickListener;
import com.test.smartcamera.listener.CameraIdClickListener;
import com.test.smartcamera.listener.FeatureSelectorListClickListener;
import com.test.smartcamera.listener.FeatureSettingListClickListener;
import com.test.smartcamera.listener.OnClickListenerBase;
import com.test.smartcamera.listener.SeekbarClickListenerStub;
import com.test.smartcamera.listener.SelectorListTouchListener;
import com.test.smartcamera.listener.SelfTimerControlClickListener;
import com.test.smartcamera.listener.SessionClickListener;
import com.test.smartcamera.listener.VideoControlClickListener;
import com.test.smartcamera.resources.DefaultValues;

public class ListsHandler
{
	//This class maintain a two dimensional table mListWarehouse 
	//for lists. 
	//One dimension of the table is list type,
	//the other dimension is the camera id
	//The start few items of mListWarehouse are camera independent
	//list, which means the list remains the same for different
	//cameras front or back.
	//Items before MAX_CAM_INDEPENDENT (exclude) are
	//camera independent
	
	//Another table is for Listeners of lists.
	//The first LIST_NUMBER listeners are the
	//listeners for the list in the same
	//place in mListWarehouse
	
	//----------------------------------------------------------------
	//----------------------------------------------------------------
	//list id format:
	//prefix: list_id + {session name|complete} + [list name]
	public static final int list_sessions = 0;// cam independent
	public static final int list_camera_id_selection = 1;// cam independent
	public static final int list_camera_session_control = 2;// cam independent
	public static final int list_video_session_control = 3;// cam independent
	public static final int list_self_timer_options = 4;// cam independent
	public static final int list_burst_mode_options = 5;// cam independent
	public static final int list_self_timer_session_control = 6;// cam independent
	
	
	public static final int list_flash_options = 7;
	public static final int list_antibanding_options = 8;
	public static final int list_color_effect_options = 9;
	public static final int list_focus_options = 10;
	public static final int list_scene_options = 11;
	public static final int list_white_balance_options = 12;
	public static final int list_picture_size_options = 13;
	public static final int list_video_size_options = 14;
	public static final int list_zoom_options = 15;
	public static final int list_exposure_compensation_options = 16;
	public static final int list_video_feature = 17;
	public static final int list_camera_feature = 18;
	//TODO add new list id here
	
	private static final int MAX_CAM_INDEPENDENT = 7; //TODO increase this number after adding new cam independent list
	private static final int LIST_NUMBER = 19; //TODO increase this number after adding new list

	//----------------------------------------------------------------
	//----------------------------------------------------------------
	
	protected static final int LIST_WAREHOSE_SIZE = LIST_NUMBER * CameraHolder.getNumberOfCameras();
	protected static final int LISTENER_WAREHOSE_SIZE = LIST_NUMBER;// + CUSTOMIZED_LISTENER_NUMBER;
	
	protected MountableGroupView[] mListWarehouse = new MountableGroupView[LIST_WAREHOSE_SIZE];
	protected OnClickListenerBase[] mListenerWarehouse = new OnClickListenerBase[LISTENER_WAREHOSE_SIZE];

	protected int getListIndex(int aListId)
	{
		return (ApplicationContext.mCameraId * LIST_NUMBER) + aListId;
	}
	
	public MountableGroupView getList(int aListId)
	{
		assert(getListIndex(aListId) < LIST_NUMBER);
		int vListIndex = getListIndex(aListId);

		if(mListWarehouse[vListIndex] == null)
		{//bind with listener, getListener() gets the listener
			mListWarehouse[vListIndex] = getNewList(aListId, getListener(aListId));
			installCameraIndependentList(aListId, vListIndex);
		}
		//Log.d(this.getClass().getName(), "aListId: " + aListId + ", vListIndex: " + vListIndex);
		//there're several lists mapping to one listener
		//so we need to bind listener with the current fetched list
		mListenerWarehouse[aListId].bindWithList(aListId);
		return mListWarehouse[vListIndex];
	}
	
	protected OnClickListenerBase getListener(int aListId)
	{
		assert(aListId < LIST_NUMBER);
		if(mListenerWarehouse[aListId] == null)
		{
			mListenerWarehouse[aListId] = getNewListener(aListId);
		}
		return mListenerWarehouse[aListId];
	}
	
	protected void installCameraIndependentList(int aListId, int aListIndex)
	{
		if(aListId < MAX_CAM_INDEPENDENT)
		{
			int vListId = aListId;
			while(vListId < LIST_WAREHOSE_SIZE)
			{
				if(vListId != aListIndex)
				{
					mListWarehouse[vListId] = mListWarehouse[aListIndex];
				}
				vListId = vListId + LIST_NUMBER;
			}
		}
	}
	
	protected MountableGroupView getNewList(int aListId, OnClickListenerBase aListener)
	{
		switch(aListId)
		{
		case list_sessions://cam independent
			return constructSelectorList(
					R.layout.selector_list_layout,
					LinearLayout.VERTICAL,
					LinearLayout.HORIZONTAL,
					View.GONE,
					true,
					aListener,
					FeatureHandler.getSessionArray());
		case list_camera_id_selection://cam independent
			return constructFeatureSettingList(
					R.string.menu_title_camera_id,
					aListener,
					FeatureHandler.getCameraIdArray(),
					PreferenceHandler.getCameraIdPreference());
		case list_camera_session_control://cam independent
			return constructSelectorList(
					R.layout.selector_list_layout,
					LinearLayout.HORIZONTAL,
					LinearLayout.VERTICAL,
					View.GONE,
					false,
					aListener,
					FeatureHandler.getCamSessionControlArray());
		case list_video_session_control://cam independent
			return constructSelectorList(
					R.layout.selector_list_layout,
					LinearLayout.HORIZONTAL,
					LinearLayout.VERTICAL,
					View.GONE,
					false,
					aListener,
					FeatureHandler.getVideoSessionControlArray());
		case list_self_timer_options://cam independent
			return constructFeatureSettingList(
					R.string.menu_title_self_timer,
					aListener,
					FeatureHandler.getSelfTimerArray(),
					PreferenceHandler.getSelfTimerPreference());
		case list_burst_mode_options://cam independent
			return constructFeatureSettingList(
					R.string.menu_title_burst_mode,
					aListener,
					FeatureHandler.getBurstArray(),
					PreferenceHandler.getBurstPreference());
		case list_self_timer_session_control://cam independent
			return constructSelectorList(
					R.layout.selector_list_layout,
					LinearLayout.HORIZONTAL,
					LinearLayout.VERTICAL,
					View.GONE,
					false,
					aListener,
					FeatureHandler.getSelfTimerSessionControlArray());
		//TODO
		
		case list_flash_options:
			return constructFeatureSettingList(
					R.string.menu_title_flash,
					aListener,
					ApplicationContext.getFeatureHandler().getFlashArray(),
					ApplicationContext.getPreferenceHandler().getFlashPreference());
		case list_antibanding_options:
			return constructFeatureSettingList(
					R.string.menu_title_antibanding,
					aListener,
					ApplicationContext.getFeatureHandler().getAntibandingArray(),
					ApplicationContext.getPreferenceHandler().getAntibandingPreference());
		case list_color_effect_options:
			return constructFeatureSettingList(
					R.string.menu_title_color_effect,
					aListener,
					ApplicationContext.getFeatureHandler().getColorEffectArray(),
					ApplicationContext.getPreferenceHandler().getColorEffectPreference());
		case list_focus_options:
			return constructFeatureSettingList(
					R.string.menu_title_color_effect,
					aListener,
					ApplicationContext.getFeatureHandler().getFocusArray(),
					ApplicationContext.getPreferenceHandler().getFocusPreference());
		case list_scene_options:
			return constructFeatureSettingList(
					R.string.menu_title_scene,
					aListener,
					ApplicationContext.getFeatureHandler().getSceneArray(),
					ApplicationContext.getPreferenceHandler().getScenePreference());
		case list_white_balance_options:
			return constructFeatureSettingList(
					R.string.menu_title_white_balance,
					aListener,
					ApplicationContext.getFeatureHandler().getWhiteBalanceArray(),
					ApplicationContext.getPreferenceHandler().getWhiteBalancePreference());
		case list_picture_size_options:
			return constructFeatureSettingList(
					R.string.menu_title_picture_size,
					aListener,
					ApplicationContext.getFeatureHandler().getPictureSizeArray(),
					ApplicationContext.getPreferenceHandler().getPictureSizePreference());
		case list_video_size_options:
			return constructFeatureSettingList(
					R.string.menu_title_video_size,
					aListener,
					ApplicationContext.getFeatureHandler().getVideoSizeArray(),
					ApplicationContext.getPreferenceHandler().getVideoSizePreference());
		case list_zoom_options:
			{
				ZoomSeekBar vSeekBar = new ZoomSeekBar();
				vSeekBar.onCreate(R.string.menu_title_zoom,
									ApplicationContext.getFeatureHandler().getMaxZoom(),
									DefaultValues.default_zoom_seekbar_min_value);
				return vSeekBar;
			}
		case list_exposure_compensation_options:
			{
				EVSeekBar vSeekBar = new EVSeekBar();
				vSeekBar.onCreate(R.string.menu_title_exposure_compensation,
						ApplicationContext.getFeatureHandler().getMaxExposureCompensation(),
						ApplicationContext.getFeatureHandler().getMinExposureCompensation());
				return vSeekBar;
			}
		case list_camera_feature:
			return constructSelectorList(
					R.layout.selector_list_layout,
					LinearLayout.HORIZONTAL,
					LinearLayout.VERTICAL,
					View.GONE,
					true,
					aListener,
					ApplicationContext.getFeatureHandler().getCameraFeatureSelectorArray());
		case list_video_feature:
			return constructSelectorList(
					R.layout.selector_list_layout,
					LinearLayout.HORIZONTAL,
					LinearLayout.VERTICAL,
					View.GONE,
					true,
					aListener,
					ApplicationContext.getFeatureHandler().getVideoFeatureSelectorArray());
		//TODO
		default:
			Log.e(this.getClass().getName(), "Invalide List Id: " + aListId);
			break;
		}
		return null;
	}
	
	protected OnClickListenerBase getNewListener(int aListenerId)
	{
		switch(aListenerId)
		{
		case list_sessions:
			return new SessionClickListener();
		case list_camera_id_selection://cam independent
			return new CameraIdClickListener();
		case list_camera_session_control://cam independent
			return new CameraControlClickListener();
		case list_video_session_control://cam independent
			return new VideoControlClickListener();
		case list_self_timer_options://cam independent
			return new FeatureSettingListClickListener();
		case list_burst_mode_options://cam independent
			return new FeatureSettingListClickListener();
		case list_self_timer_session_control://cam independent
			return new SelfTimerControlClickListener();
		//TODO
		
		case list_flash_options:
			return new FeatureSettingListClickListener();
		case list_antibanding_options:
			return new FeatureSettingListClickListener();
		case list_color_effect_options:
			return new FeatureSettingListClickListener();
		case list_focus_options:
			return new FeatureSettingListClickListener();
		case list_scene_options:
			return new FeatureSettingListClickListener();
		case list_white_balance_options:
			return new FeatureSettingListClickListener();
		case list_picture_size_options:
			return new FeatureSettingListClickListener();
		case list_video_size_options:
			return new FeatureSettingListClickListener();
		case list_zoom_options:
			return new SeekbarClickListenerStub();
		case list_exposure_compensation_options:
			return new SeekbarClickListenerStub();
		case list_camera_feature:
			return new FeatureSelectorListClickListener();
		case list_video_feature:
			return new FeatureSelectorListClickListener();
		//case list_complete_feature: reuse listener of list_complete_feature
		//TODO
		default:
			Log.e(this.getClass().getName(), "Invalide Listener Id: " + aListenerId);
			break;
		}
		return null;
	}
	
	private <S, T> FeatureSettingList<S, T> constructFeatureSettingList(
			int aTitleId,
			View.OnClickListener aListener,
			EntryArray<S, T> anEntryArray,
			T aPreference)
	{
		FeatureSettingList<S, T> aList = new FeatureSettingList<S, T>(aTitleId);
		aList.infateGroupView(R.layout.detailed_setting_table, aListener, anEntryArray, aPreference);
		return aList;
	}
	
	private SelectorList constructSelectorList(
			int aLayoutId,
			int anEntryOrientation,
			int anLayoutOrientation,
			int aTextVisibility,
			boolean useTouchListener,
			View.OnClickListener aListener,
			EntryArray<Integer, Integer> anEntryArray)
	{
		SelectorList aList = new SelectorList();
		aList.setEntryOrientation(anEntryOrientation);
		aList.infateGroupView(aLayoutId, aListener, anEntryArray, 0);
		aList.setLayoutOrientation(anLayoutOrientation);
		if(useTouchListener == true)
		{
			aList.installOnTouchListener(new SelectorListTouchListener(aList));
		}
		aList.setAnnotationVisibility(aTextVisibility);
		return aList;
	}
}

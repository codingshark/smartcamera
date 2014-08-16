package com.test.smartcamera.resources;

import com.test.smartcamera.FileFactory;
import com.test.smartcamera.R;
import com.test.smartcamera.SessionHandler;
import com.test.smartcamera.StorageManager;

public class DefaultValues {
	//StorageManager
	public static final int default_storage_media_type = StorageManager.media_type_external;
	public static final int defualt_file_name_rule = StorageManager.file_name_rule_counter;
	public static final int default_photo_cnt = 0;
	//SetupWork
	public static final boolean default_HasBeenSetup = false;
	//CameraSizeEntryArray
	public static final int default_camera_size_icon = R.drawable.ic_action_overflow;
	//CameraIdEntryArray
	public static final int default_camer_id_icon = R.drawable.ic_action_video;
	//SeekBarView
	public static final int default_seekbar_min_value = 0;
	public static final int default_seekbar_max_value = 0;
	//ZoomSeekbar
	public static final int default_zoom_seekbar_min_value = 0;
	//public static final int default_min_zoom_ratio = 0;
	public static final int default_max_zoom = 0;
	//SessionHandler
	public static final int default_session = SessionHandler.session_ex_prophoto;
	//FlashMemUsageView
	public static final int default_dir_type = FileFactory.file_type_image;
	
	//PreferenceHandler
	public static final int default_camera_id = 0;//CameraLogic
	public static final int default_self_timer_option = R.id.btn_self_timer_off;
	public static final int default_burst_option = R.id.btn_burst_single_frame;

}

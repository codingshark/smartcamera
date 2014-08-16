package com.test.smartcamera.resources;

//Keep program internal strings which do not need to be localized
//This will accelerate the performance of the program and simplify
//programming
public class ApplicationConstants
{
	public static final int null_res_id = 0;
	
	//SavePhotoTask
	public static final int const_flash_space_reservation = 2*1024*1024;//2MB
	
	//StorageManager
	public final static String image_file_prefix = "DSC_";
	public final static String video_file_prefix = "VID_";
	public final static String jpeg_file_extension = ".jpeg";
	public final static String mp4_file_extension = ".mp4";
	public final static String image_album_name = "images";
	public final static String video_album_name = "videos";
	
	public static final String key_storage_media_type = "A";
	public static final String key_photo_cnt = "B";
	public static final String key_file_name_rule = "C";
	public final static int const_flash_space_warning_size = 
			const_flash_space_reservation + 3*1024*1024;//2MB + 3MB
	//SetupWork
	public static final String key_has_been_setup = "D";

	//FocusingView
	public static final int const_restore_dealy = 800;
	
	//ZoomSeekBarView
	//this value could not be zero, it is used in a devision
	public static final int const_zoom_ratio = 100;
	
	//DialogueHandler
	public static final String tag_err_dialog = "Error";
	
	//CameraPreviewGestureListener
	public static final int const_filter_length = 2;
	
	//SelfTimerSettingClickListener
	public static final int const_two_seconds_delay = 2000;
	public static final int const_ten_seconds_delay = 10000;
	public static final int const_thirty_seconds_delay = 30000;
	
	//DigitalTimer
	public static final int const_period = 1000;
	
	//MeterView
	public static final int const_max_progress = 10;
	
	//FileCreater
	public static final String const_duplicate_file_suffix = "_DFE_";
	public static final String const_duplicate_file_suffix_2 = "_CIS_";
	public static final String const_date_format = "ddMMyyyy_HHmmss";
	
	//ShutterController
	public static final int const_mem_usage_shift_ratio = 2;// >>2 = 1/4 = 25%
	
	//FileFactory
	public static final long const_space_mb_shift_factor = 20;//MB
	
	//SessionVideo
	public static final long const_min_video_recording_time = 1000;//ms
}

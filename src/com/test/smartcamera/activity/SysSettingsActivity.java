package com.test.smartcamera.activity;

import android.app.Activity;
import android.os.Bundle;

import com.test.smartcamera.R;

public class SysSettingsActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sys_setting_activity);
		
	}
	
	@Override
	protected void onStop()
	{
		 super.onStop();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
    protected void onPause()
	{
		super.onPause();
	}
}

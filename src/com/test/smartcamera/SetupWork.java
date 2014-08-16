package com.test.smartcamera;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.resources.ApplicationConstants;
import com.test.smartcamera.resources.DefaultValues;

public abstract class SetupWork
{
	private static boolean mHasBeenSetup = false;
	private static int mWorkCnt = 0;
	private static boolean mGotSetupFlag	= false;
	private static boolean mPutSetupFlag	= false;
	
	private static void declareStepWork()
	{
		++mWorkCnt;
	}
	private static void setupWorkDone()
	{
		--mWorkCnt;
	}
	
	private static void putSetupFlag(PreferencesSerializer aSerializer)
	{
		if(SetupWork.mPutSetupFlag == false)
		{
			if(SetupWork.mWorkCnt != 0)//Setup failed
			{
				SetupWork.mHasBeenSetup = false;
			}else
			{//Setup succeed
				SetupWork.mHasBeenSetup = true;//Comment this line to debug method Setup()
			}
			aSerializer.putBoolean(ApplicationConstants.key_has_been_setup, SetupWork.mHasBeenSetup);
		}
		SetupWork.mPutSetupFlag = true;
	}
	
	private static void getSetupFlag(PreferencesSerializer aSerializer)
	{
		if(SetupWork.mGotSetupFlag == false)
		{
			SetupWork.mHasBeenSetup = aSerializer.getBoolean(ApplicationConstants.key_has_been_setup, DefaultValues.default_HasBeenSetup);
		}
		SetupWork.mGotSetupFlag = true;
	}
	
	public final void onCreate(PreferencesSerializer aSerializer) throws Exception
	{
		SetupWork.getSetupFlag(aSerializer);
		if(SetupWork.mHasBeenSetup == true)
		{//read more preferences
			getPreferences(aSerializer);
		}else
		{
			Log.d(SetupWork.class.getName(), "onSetup(), run once for each installation");
			SetupWork.declareStepWork();
			onSetup();
			putPreferences(aSerializer);
			SetupWork.setupWorkDone();
		}
		onStart();
		return;
	}
	
	public final void onStop(PreferencesSerializer aSerializer)
	{
		SetupWork.putSetupFlag(aSerializer);
		if(SetupWork.mWorkCnt == 0)//Setup failed
		{
			putPreferences(aSerializer);
		}
	}
	
	//run once after the application is installed
	//Setup basic preferences
	protected abstract boolean onSetup();
	protected abstract void onStart() throws Exception;//onCreate has been used, so onStart do initialisation work of derived class
	protected abstract void getPreferences(PreferencesSerializer aSerializer);
	protected abstract void putPreferences(PreferencesSerializer aSerializer);
}

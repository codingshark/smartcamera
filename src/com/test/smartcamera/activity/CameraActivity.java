package com.test.smartcamera.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.test.smartcamera.ApplicationContext;
import com.test.smartcamera.PreferencesSerializer;
import com.test.smartcamera.R;
import com.test.smartcamera.eng.Log;
import com.test.smartcamera.eng.LogHandler;
import com.test.smartcamera.listener.KeyEventHandler;

public class CameraActivity extends FragmentActivity
{
	private PreferencesSerializer mPreferencesSerializer = new PreferencesSerializer();
	private KeyEventHandler mKeyEventHandler = new KeyEventHandler();
	
	protected LogHandler vLog = new LogHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

//        vLog.onCreate();//TODO Uncomment this line to use LogHandler
        
        Log.d(this.getClass().getName(), "onCreate()");
		mPreferencesSerializer.open(this);
		ApplicationContext.onCreate(this, mPreferencesSerializer);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();

		Log.d(this.getClass().getName(), "onStop()");
		mPreferencesSerializer.open(this);
		ApplicationContext.onStop(mPreferencesSerializer);
		mPreferencesSerializer.close();
		
		vLog.onStop();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		Log.d(this.getClass().getName(), "onResume()");
		ApplicationContext.onResume();
	}
    @Override
    protected void onPause() {
        super.onPause();
        
        Log.d(this.getClass().getName(), "onPause()");
		ApplicationContext.onPause();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
    	if(mKeyEventHandler.onKeyDown(keyCode, event) == false)
    	{
    		return super.onKeyDown(keyCode, event);
    	}
    	return true;
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
    	if(mKeyEventHandler.onKeyUp(keyCode, event) == false)
    	{
    		return super.onKeyUp(keyCode, event);
    	}
    	return true;
    }
    
}

package com.test.smartcamera;

import android.graphics.ImageFormat;
import android.hardware.Camera;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.gui.BufferCounterView;
import com.test.smartcamera.gui.StatusPanel;
import com.test.smartcamera.resources.ApplicationConstants;

public class ShutterController
	implements Camera.PictureCallback,
	Camera.PreviewCallback //for high speed burst mode
{
	public static final int BURST_MODE_OFF = 0;
	public static final int HIGH_RESOLUTION_BURST = 1;
	public static final int HIGH_SPEED_BURST = 2;
	
	//shutter protection
	private boolean mShutterReleased = false;
	
	//for delayed shutter
	private int mShutterDelay = 0;

	//for burst mode
	protected int mLastPhotoSize = 0;
	private int mBurstMode = BURST_MODE_OFF;
	public boolean mShutterButtonPressed = false;
	protected int mPhotoCnt = 0;
	
	//for memory check
	private static long mMemReservation = Runtime.getRuntime().maxMemory() >> ApplicationConstants.const_mem_usage_shift_ratio;
	private static long mMaxMem = Runtime.getRuntime().maxMemory();
	
	//for buffer visualization
	private BufferCounterView mBufferCounterView = null;
	private static long mAvailableMem = mMaxMem - mMemReservation;
	protected long mStartMemUsage = 0;
	
	//photo handlers
	private PhotoHandler mRegularPhotoHandler = null;
	private PhotoHandler mSnapPhotoHandler = null;
	
	public void onCreate()
	{
		mRegularPhotoHandler = new PhotoHandler(null, ImageFormat.JPEG);
		mSnapPhotoHandler = new PhotoHandler(new PhotoFormatProcessor(),
							ApplicationContext.mCameraHolder.
							getCameraParameters().getPreviewFormat());
	}
	
	public void onSnapFormatChange()
	{
		int vNewFormat = ApplicationContext.mCameraHolder.
				getCameraParameters().getPreviewFormat();
		if(mSnapPhotoHandler.getImgFormat() != vNewFormat)
		{
			mSnapPhotoHandler = new PhotoHandler(new PhotoFormatProcessor(), vNewFormat);
		}
	}
	
	public void takeSnapShot()
	{
		if(ApplicationContext.getFeatureHandler().isVideoSnapshotSupported() == true)
		{
			releaseShutter();
		}else
		{
			ApplicationContext.mCameraHolder.setOneShotPreviewCallback(this);
		}
	}
	
	public void takePicture()
	{
		if(mShutterDelay != 0)
		{
			ApplicationContext.mSessionHandler.pushAndPlaySession(SessionHandler.session_im_self_timer);
		}else
		{
			mPhotoCnt = 0;
			mStartMemUsage = Runtime.getRuntime().totalMemory();
			if(mBurstMode == HIGH_SPEED_BURST)
			{
				ApplicationContext.mCameraHolder.setPreviewCallback(this);
			}else
			{
				releaseShutter();
			}
		}
	}
	
	public void releaseShutter()
	{
		if((mShutterReleased == false) && (memoryAvailable(mLastPhotoSize) == true))
		{
			updateBufferMeter();
			ApplicationContext.mCameraHolder.takePicture(null, null, null, this);
			mShutterReleased = true;
		}//else previous shot in progress, cancel current shot
	}
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera)
	{
		Log.d(this.getClass().getName(), "photo size: " + data.length);
		mShutterReleased = false;
		handleRegularPhotoData(data);
		if(mBurstMode == HIGH_RESOLUTION_BURST)
		{//in bust mode
			if(mShutterButtonPressed == true)
			{//keep shooting
				releaseShutter();
			}
		}
    }
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera)
	{
		Log.d(this.getClass().getName(), "fast burst data len: " + data.length);
		updateBufferMeter();
		handleSnapPhotoData(data);
		if(mShutterButtonPressed == false)
		{
			ApplicationContext.mCameraHolder.setPreviewCallback(null);
		}
	}
	
	private void updateBufferMeter()
	{
		if(mBufferCounterView != null)
		{
			++mPhotoCnt;
	
			long vUsagedMem = Runtime.getRuntime().totalMemory() - mStartMemUsage;
			int vRatio = (int)((vUsagedMem * ApplicationConstants.const_max_progress) / (mAvailableMem - mStartMemUsage));
	
			mBufferCounterView.updateView(mPhotoCnt, vRatio);
		}
	}
	
	private void handleRegularPhotoData(byte[] data)
	{
		Log.d(this.getClass().getName(), "start=" + mStartMemUsage/1024);
		Log.d(this.getClass().getName(), "max=" + Runtime.getRuntime().maxMemory()/1024);
		Log.d(this.getClass().getName(), "total=" + Runtime.getRuntime().totalMemory()/1024);
		Log.d(this.getClass().getName(), "Free=" + Runtime.getRuntime().freeMemory()/1024);
		mLastPhotoSize = data.length;
		mRegularPhotoHandler.handleData(data);
	}
	
	private void handleSnapPhotoData(byte[] data)
	{
		//Log.d(this.getClass().getName(), "start=" + mStartMemUsage/1024);
		//Log.d(this.getClass().getName(), "max=" + Runtime.getRuntime().maxMemory()/1024);
		//Log.d(this.getClass().getName(), "total=" + Runtime.getRuntime().totalMemory()/1024);
		//Log.d(this.getClass().getName(), "Free=" + Runtime.getRuntime().freeMemory()/1024);
		mLastPhotoSize = data.length;
		mSnapPhotoHandler.handleData(data);
	}
	
	public void abortShutterEvents()
	{
		Log.e(this.getClass().getName(), "abortShutterEvents");
		mShutterButtonPressed = false;
		if(mBufferCounterView != null)
		{//TODO since we have two photo handlers now
			//this may not very accurate now, since one
			//handler aborted doesn't means the other handler is
			//also aborted
			mBufferCounterView.updateView(0, 0);
		}
	}
	
	public void onPause()
	{
		mRegularPhotoHandler.processSavingTask();
		mSnapPhotoHandler.processSavingTask();
		mShutterButtonPressed = false;
	}
	
	public boolean memoryAvailable(int aDataSize)
	{
		long extendable = mMaxMem - Runtime.getRuntime().totalMemory();
		if((extendable > mMemReservation)//there's still more than 25% memory left
			&& (extendable > aDataSize))//and still have enough memory
		{
			return true;
		}
		mBufferCounterView.updateView(mPhotoCnt, ApplicationConstants.const_max_progress);//buffer full
		return false;
	}
	
	public void setBurstMode(int aMode)
	{
		if(mBurstMode != aMode)
		{
			switch(aMode)
			{
			case BURST_MODE_OFF:
				if(mBurstMode != BURST_MODE_OFF)
				{
					removeBufferMeter();
				}
				break;
			case HIGH_RESOLUTION_BURST:
				if(mBurstMode == BURST_MODE_OFF)
				{
					installBufferMeter();
				}
				break;
			case HIGH_SPEED_BURST:
				if(mBurstMode == BURST_MODE_OFF)
				{
					installBufferMeter();
				}
				break;
			default:
				Log.e(this.getClass().getName(), "Invide burst mode: " + aMode);
				break;
			}
			mBurstMode = aMode;
		}
		
	}
	
	private void installBufferMeter()
	{
		ApplicationContext.getCurrentSession().mStatusPanel.showView(StatusPanel.view_buffer_counter);
		mBufferCounterView = (BufferCounterView)ApplicationContext.getCurrentSession().mStatusPanel.getView(StatusPanel.view_buffer_counter);
	}
	private void removeBufferMeter()
	{
		ApplicationContext.getCurrentSession().mStatusPanel.removeView(StatusPanel.view_buffer_counter);
		mBufferCounterView = null;
	}
	
	public void setShutterDelay(int aDelay)
	{
		mShutterDelay = aDelay;
	}
	
	public int getShutterDelay()
	{
		return mShutterDelay;
	}
	
	public boolean cameraBusy()
	{
		return (mShutterReleased == true)
				|| (mShutterButtonPressed == true);
	}
}

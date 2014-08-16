package com.test.smartcamera;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.test.smartcamera.eng.Log;
import com.test.smartcamera.listener.MediaRecorderInfoErrorListener;
import com.test.smartcamera.resources.ApplicationConstants;

public class CameraHolder
{
	private Camera mCamera = null;
	private MediaRecorder mMediaRecorder = null;

	private Camera.AutoFocusCallback mFocusCallback = null;
	
	private int mVideoWidth = 0;
	private int mVideoHeight = 0;

	private File mVideoFile = null;

	public boolean onCreate(Context aContext,
					int aCameraId,
					Camera.AutoFocusCallback aFocusCallback)
	{
		assert(aContext != null);
		assert(aFocusCallback != null);
		
		// Check if this device has a camera
		if (aContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) == false)
		{// no camera on this device
			Log.e(this.getClass().getName(), "Cannot find camera on this machine");
			return false;	
		}
		
		mFocusCallback = aFocusCallback;
		return openCamera(aCameraId);
	}
	
	public void onResume(SurfaceHolder aSurfaceHolder)
	{
		if(openCamera(ApplicationContext.mCameraId) != false)
		{
			startPreview(aSurfaceHolder);
		}
	}
    public void onPause()
    {
    	if(mVideoFile != null)
    	{//The video recorder is running
    		StopVideoRecording();
    	}
    	releaseMediaRecorder();
        releaseCamera();
    }
//    public void onStop(){}
    
	public void setCameraParameters(Camera.Parameters aParameters)
	{
		assert(mCamera != null);
		mCamera.cancelAutoFocus();
		mCamera.setParameters(aParameters);
	}
	
	public Camera.Parameters getCameraParameters()
	{
		return mCamera.getParameters();
	}
	
	public static int getNumberOfCameras()
	{
		return Camera.getNumberOfCameras();
	}
	
	public void updatePreviewSize(int width, int height, SurfaceHolder aSurfaceHolder)
	{
		mCamera.stopPreview();
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(width, height);
		mCamera.setParameters(parameters);
		startPreview(aSurfaceHolder);
	}

	public boolean openCamera(int aCameraId)
	{
		if(ApplicationContext.mCameraId != aCameraId)
		{
			releaseCamera();//release previous camera
			ApplicationContext.mCameraId = aCameraId;
		}
		return openCamera();
	}
	
	public boolean openCamera()
	{
		boolean vOpened = true;
		if(mCamera == null)
		{
			try {
				mCamera = Camera.open(ApplicationContext.mCameraId);
				vOpened = (mCamera != null);
			} catch(RuntimeException e)
			{
				vOpened = false;
				Log.e(this.getClass().getName(), "Camera is not available (in use or does not exist)", e);
			}
		}
		return vOpened;
	}
	public void startPreview(SurfaceHolder aSurfaceHolder)
	{
		try
		{
			mCamera.setPreviewDisplay(aSurfaceHolder);
			mCamera.startPreview();
		}catch(IOException e)
		{
			Log.e(this.getClass().getName(), "setPreviewDisplay failed", e);
		}
	}
	
	public void releaseCamera()
	{
		if(mCamera != null)
		{
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
	
	//access this method through ShutterController
	public void takePicture(Camera.ShutterCallback shutter,
							Camera.PictureCallback raw,
							Camera.PictureCallback postview,
							Camera.PictureCallback jpeg)
	{
		assert(mCamera != null);
		mCamera.takePicture(shutter, raw, postview, jpeg);
		mCamera.startPreview();
	}

	public void setVideoSize(int width, int height)
	{
		mVideoWidth = width;
		mVideoHeight = height;
	}
	
	private boolean prepareVideoRecorder(String aFilePath, long aMaxFileSize, Surface aSurface)
	{
	    mMediaRecorder = new MediaRecorder();
	    
	    MediaRecorderInfoErrorListener vInfoErrorListener = new MediaRecorderInfoErrorListener();
	    mMediaRecorder.setOnErrorListener(vInfoErrorListener);
	    mMediaRecorder.setOnInfoListener(vInfoErrorListener);
	    
	    // Step 1: Unlock and set camera to MediaRecorder
	    mCamera.unlock();
	    mMediaRecorder.setCamera(mCamera);

	    // Step 2: Set sources
	    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	    
	    // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
	    mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//	    mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
	    //mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_LOW));

	    // Step 4: Set output file
	    mMediaRecorder.setOutputFile(aFilePath);

	    // Step 5: Set the preview output
	    mMediaRecorder.setPreviewDisplay(aSurface);

	    try
	    {
	    
	    	if((mVideoWidth != 0) && (mVideoHeight != 0))
	    	{
	    		mMediaRecorder.setVideoSize(mVideoWidth, mVideoHeight);
	    	}
	    	
	    	mMediaRecorder.setMaxFileSize(aMaxFileSize);
	    	
	    	// Step 6: Prepare configured MediaRecorder
	        mMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        Log.e(this.getClass().getName(), "preparing MediaRecorder: ", e);
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        Log.e(this.getClass().getName(), "preparing MediaRecorder: ", e);
	        releaseMediaRecorder();
	        return false;
	    } catch (Exception e)
	    {
	    	Log.e(this.getClass().getName(), "Exception preparing MediaRecorder: ", e);
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
	}
	
    private void releaseMediaRecorder()
    {
        if (mMediaRecorder != null)
        {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    public boolean startVideoRecording(File aNewFile, int aStorageType, Surface aSurface)
    {
    	// Initialise video camera
        if (prepareVideoRecorder(aNewFile.getPath(), aNewFile.getUsableSpace() - ApplicationConstants.const_flash_space_reservation, aSurface) == true)
        {
            // Camera is available and unlocked, MediaRecorder is prepared,
            // now you can start recording
        	try
        	{
        		mMediaRecorder.start();
        	}catch(IllegalStateException e)
        	{
        		Log.e(this.getClass().getName(), "start MediaRecorder: ", e);
    	        releaseMediaRecorder();
    	        return false;
        	}
        	
            mVideoFile = aNewFile;
            return true;
        } else {
            // prepare didn't work, release the camera
            releaseMediaRecorder();
            // inform user
        }
        return false;
    }
    
    public void StopVideoRecording()
    {
    	if(mVideoFile != null)
    	{
	    	// stop recording and release camera
    		try{
    			mMediaRecorder.stop();  // stop the recording
    	    }catch(RuntimeException stopException)
    	    {
    	    	Log.e(this.getClass().getName(), "Media Recorder Stop failed", stopException);
    	    	if(mVideoFile.exists() == true)
    	    	{//the file has been created, delete it
    	    		if(mVideoFile.delete() == false)
    	    		{
    	    			Log.e(this.getClass().getName(), "File deleting failed");
    	    		}
    	    	}
    	    }
	        releaseMediaRecorder(); // release the MediaRecorder object
	        mCamera.lock(); // take camera access back from MediaRecorder
	        
	        Uri aUri = MediaURIHandler.getMediaURI(
	        		mVideoFile,
					System.currentTimeMillis(),
					0,//anOrientation, //TODO
					0,//data.length,//TODO
					null,//location //TODO
					FileFactory.file_type_video);
	
			if(aUri != null)
			{
				MediaURIHandler.BroadcastURI(aUri, FileFactory.file_type_video);
			}
    	}
		mVideoFile = null;
    }
    
    public boolean isRecording()
    {
    	return mVideoFile != null;
    }
    
    public void startFocus()
    {
    	mCamera.cancelAutoFocus();
    	mCamera.autoFocus(mFocusCallback);
    }
    
    public void setPreviewCallback(Camera.PreviewCallback cb)
    {
    	mCamera.setPreviewCallback(cb);
    }
    
    public void setOneShotPreviewCallback(Camera.PreviewCallback cb)
    {
    	mCamera.setOneShotPreviewCallback(cb);
    }
}

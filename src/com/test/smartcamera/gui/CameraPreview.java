package com.test.smartcamera.gui;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.test.smartcamera.CameraHolder;
import com.test.smartcamera.eng.Log;

public class CameraPreview
	extends SurfaceView
	implements SurfaceHolder.Callback//,View.OnTouchListener
{
	private SurfaceHolder mSurfaceHolder = null;
	private CameraHolder mCameraHolder = null;
	
	public interface CameraViewClickListener
	{
		public void onClick(View aView);
	}
	
	///////callback of SurfaceHolder
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
	// no-op -- wait until surfaceChanged()
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format,
	                               int width, int height)
	{
		//it is possible that the screen get locked and camera released. Without open, camera is null.
		mCameraHolder.openCamera();
		
		Camera.Size previewSize = getBestPreviewSize(width, height, mCameraHolder.getCameraParameters());
		int result_width = 0;
		int result_height = 0;
		if(previewSize != null)
		{
			Log.d(this.getClass().getName(), "preview size:(" + previewSize.width + "," + previewSize.height + ")");
			result_width = previewSize.width;
			result_height = previewSize.height;
		}else
		{
			result_width = width;
			result_height = height;
			Log.d(this.getClass().getName(), "Cannot find proper view size");
		}
		updatePreviewSize(result_width, result_height);
		mCameraHolder.updatePreviewSize(result_width, result_height, getSurfaceHoulder());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// no-op
	}
	///////callback of SurfaceHolder
	
	public CameraPreview(Context aContext){super(aContext);}
	public CameraPreview(Context context, AttributeSet attrs){super(context, attrs);}
	public CameraPreview(Context context, AttributeSet attrs, int defStyle){super(context, attrs, defStyle);}
	
	public void onCreate(CameraHolder aCameraHolder,
			View.OnTouchListener aListener)
	{
		assert(aCameraHolder != null);
		assert(aListener != null);
		
		mCameraHolder = aCameraHolder;
		
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		setOnTouchListener(aListener);//ScalingListener is too slow
	}

	public SurfaceHolder getSurfaceHoulder()
	{
		return mSurfaceHolder;
	}
	
	public void updatePreviewSize(int width, int height)
	{
		ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        params.height = height;
        setLayoutParams(params);
	}
	
	// getPreferredPreviewSizeForVideo()
	// Camera.Size 	getPictureSize()
	// List<Camera.Size> 	getSupportedPictureSizes()
	// List<Camera.Size> 	getSupportedPreviewSizes()
	// List<Camera.Size> 	getSupportedVideoSizes()
	//mediaRecorder.setVideoSize(240, 320);
	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters)
	{
		Camera.Size result = null;
		
		for (Camera.Size size: parameters.getSupportedPreviewSizes())
		{
			if(size.width <= width && size.height <= height)
			{
				if(result == null)
				{
					result = size;
				}else
				{
					if(size.width*size.height > result.width*result.height)
					{
						result = size;
					}
				}
			}
		}
		return result;
	}
}

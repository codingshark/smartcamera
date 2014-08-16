package com.test.smartcamera;

import android.graphics.ImageFormat;

public class PhotoFormatProcessor implements PhotoSaveTask.AsyncImageProcessor
{
	//this method could be accessed concurrently by it is not synchronized
	//use it carefully
	@Override
	public byte[] processImage(int anImgFormat, byte[] aData)
	{
		switch(anImgFormat)
		{
		case ImageFormat.JPEG:
			return aData;//save data directly
		case ImageFormat.NV16:
			//TODO
			break;
		case ImageFormat.NV21:
			//TODO
			break;
		case ImageFormat.RGB_565:
			//TODO
			break;
		case ImageFormat.YUV_420_888:
			//TODO
			break;
		case ImageFormat.YUY2:
			//TODO
			break;
		case ImageFormat.YV12:
			//TODO
			break;
		case ImageFormat.UNKNOWN:
			return null;//unable to process this photo
		}
		return null;
	}
}

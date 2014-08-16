package com.test.smartcamera.eng;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.util.DisplayMetrics;


//WARNING:
//Some readings in this class are not strictly thread-safe
//but we do not need accurate values here, so just read
//in a non-thread-safe way is fine.
//You can use them in multi-thread program

public class SystemInfoCollector
{
	private SystemInfoCollector(){}
	
	public static String getLocationInfo(Activity anActivity)
	{
		String vStr = "LocationManager=[";
		
		synchronized(SystemInfoCollector.class)
		{
			// Acquire a reference to the system Location Manager
			LocationManager vManager = (LocationManager) anActivity.getSystemService(Context.LOCATION_SERVICE);
			List<String> vList = vManager.getAllProviders();
			for(String aProvider: vList)
			{
				vStr += aProvider + ";";
			}
		}
		
		vStr += "]";
		return vStr;
	}
	
	@TargetApi(19)
	public static String getSensorInfo(Activity anActivity)
	{
		String vStr = "SensorManager=[";
		
		synchronized(SystemInfoCollector.class)
		{
			SensorManager vManager = (SensorManager)anActivity.getSystemService(Context.SENSOR_SERVICE);
			List<Sensor> vSensorList = vManager.getSensorList(Sensor.TYPE_ALL);
			
			for(Sensor aSensor: vSensorList)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
				{
					vStr += "FifoMaxEventCount=" + aSensor.getFifoMaxEventCount();
					vStr += "FifoReservedEventCount=" + aSensor.getFifoReservedEventCount();
				}
				vStr += "MaximumRange=" + aSensor.getMaximumRange() + ";"
						+ "MinDelay=" + aSensor.getMinDelay() + ";"
						+ "Name=" + aSensor.getName() + ";"
						+ "Power=" + aSensor.getPower() + ";"
						+ "Resolution=" + aSensor.getResolution() + ";"
						+ "Type=" + aSensor.getType() + ";"
						+ "Vendor=" + aSensor.getVendor() + ";"
						+ "Version=" + aSensor.getVersion() + ";";
			}
		}
		
		vStr += "]";
		return vStr;
	}
	
	public static String getDisplayInfo(Activity anActivity)
	{
		DisplayMetrics metrics = new DisplayMetrics();
		
		synchronized(SystemInfoCollector.class)
		{
			anActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		}
		String vStr = "DisplayMetrics["
				+ "density=" + metrics.density + ";"
				+ "densityDpi=" + metrics.densityDpi + ";"
				+ "heightPixels=" + metrics.heightPixels + ";"
				+ "scaledDensity=" + metrics.scaledDensity + ";"
				+ "widthPixels=" + metrics.widthPixels + ";"
				+ "xdpi=" + metrics.xdpi + ";"
				+ "ydpi=" + metrics.ydpi + ";";
		vStr += "]";
		return vStr;
	}
	
	@SuppressLint("NewApi")
	public static String getConfiguration(Activity anActivity)
	{
		Configuration config = null;
		synchronized(SystemInfoCollector.class)
		{
			config = anActivity.getResources().getConfiguration();
		}
		//The following readings from config are not thread-safe
		//but we do not need accurate values here, so just read
		//in a non-thread-safe way
		String vStr = "Configuration["
		+ "fontScale=" + config.fontScale + ";"
		+ "hardKeyboardHidden=" + config.hardKeyboardHidden + ";"
		+ "keyboard=" + config.keyboard + ";"
		+ "keyboardHidden=" + config.keyboardHidden + ";"
		+ "locale=" + config.locale + ";"
		+ "mcc=" + config.mcc + ";"
		+ "mnc=" + config.mnc + ";"
		+ "navigation=" + config.navigation + ";"
		+ "navigationHidden=" + config.navigationHidden + ";"
		+ "orientation=" + config.orientation + ";"
		+ "screenLayout=" + config.screenLayout + ";"
		+ "touchscreen=" + config.touchscreen + ";"
		+ "uiMode=" + config.uiMode + ";";
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2 )
		{
			vStr += "screenWidthDp=" + config.screenWidthDp + ";"
				+ "smallestScreenWidthDp=" + config.smallestScreenWidthDp + ";"
				+ "screenHeightDp=" + config.screenHeightDp + ";";
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 )
		{
			vStr += "densityDpi=" + config.densityDpi + ";";
		}
		
		vStr += "]";
		return vStr;
	}
	
	public static String getSysProperties()
	{
		String vSysProperties = "System.Properties[";
		
		synchronized(SystemInfoCollector.class)
		{
			Properties p = System.getProperties();
			Enumeration keys = p.keys();
			String key = "";
			while (keys.hasMoreElements())
			{
				key = (String) keys.nextElement();
				vSysProperties += key + " = " + (String) p.get(key) + ";";
			}
		}
		vSysProperties += "]";
		return vSysProperties;
	}
	
	@TargetApi(17)
	public static synchronized String getCameraInfo()
	{
		Camera.CameraInfo vCamInfo = new Camera.CameraInfo();
		String vStr = "Camera.CameraInfo[";
		for(int i = 0; i<Camera.getNumberOfCameras(); ++i)
		{
			synchronized(SystemInfoCollector.class)
			{
				Camera.getCameraInfo(i, vCamInfo);
			}
			vStr += "facing=" + vCamInfo.facing + ";"
					+ "orientation=" + vCamInfo.orientation + ";";
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 )
			{
				vStr += "canDisableShutterSound=" + vCamInfo.canDisableShutterSound + ";";
			}
		}
		vStr += "]";
		return vStr;
	}
	
	public static String getCameraParam()
	{
		Camera vCam = null;
		Camera.Parameters vParam = null;
		
		String vCamStr = "Camera.Parameters[";
		for(int i = 0; i<Camera.getNumberOfCameras(); ++i)
		{
			vCamStr += "cam-id=" + i + ";";
			
			synchronized(SystemInfoCollector.class)
			{
				try {
					vCam = Camera.open(i);
					vParam = vCam.getParameters();
					vCamStr += vParam.flatten();
					vCam.release();
				}catch(RuntimeException e)
				{
					Log.e(SystemInfoCollector.class.getName(), "Cannot open camera, id: " + i, e);
				}
			}
		}
		vCamStr += "]";
		return vCamStr;
	}
	
	@TargetApi(14)
	public static String getBuildInfo()
	{
		String vBuildInfo = "Build["
						+ "BOARD=" + Build.BOARD + ";"
						+ "BOOTLOADER=" + Build.BOOTLOADER + ";"
						+ "BRAND=" + Build.BRAND + ";"
						+ "CPU_ABI=" + Build.CPU_ABI + ";"
						+ "CPU_ABI2=" + Build.CPU_ABI2 + ";"
						+ "DEVICE=" + Build.DEVICE + ";"
						+ "DISPLAY=" + Build.DISPLAY + ";"
						+ "FINGERPRINT=" + Build.FINGERPRINT + ";"
						+ "HARDWARE=" + Build.HARDWARE + ";"
						+ "HOST=" + Build.HOST + ";"
						+ "ID=" + Build.ID + ";"
						+ "MANUFACTURER=" + Build.MANUFACTURER + ";"
						+ "MODEL=" + Build.MODEL + ";"
						+ "PRODUCT=" + Build.PRODUCT + ";"
						+ "SERIAL=" + Build.SERIAL + ";"
						+ "TAGS=" + Build.TAGS + ";"
						+ "TIME=" + Build.TIME + ";"
						+ "TYPE=" + Build.TYPE + ";"
						+ "USER=" + Build.USER + ";"
						+ "CODENAME=" + Build.VERSION.CODENAME + ";"
						+ "INCREMENTAL=" + Build.VERSION.INCREMENTAL + ";"
						+ "RELEASE=" + Build.VERSION.RELEASE + ";"
						+ "SDK_INT=" + Build.VERSION.SDK_INT + ";";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			vBuildInfo += "RADIO=" + Build.getRadioVersion();
		}
		vBuildInfo += "]";
		return vBuildInfo;
	}
}

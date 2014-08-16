package com.test.smartcamera.eng;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.test.smartcamera.PreferencesSerializer;
import com.test.smartcamera.resources.ApplicationConstants;

public class LogHandler
{
//	static final int mail_sending_frequency = 0;
	
	public static final String const_log_file = "camera_log.txt";
	public static final String const_zip_log_file = "_log.zip";
	public static final String const_log_dir = "dev_camera_logs";
	
	protected static final String key_uuid = "KUUID";
	protected static final String key_date = "KDATE";
	
	protected static final long INTERVAL_ALWAYS = 0;
	protected static final long INTERVAL_DAILY = 86400000L;//time in milliseconds
	protected static final long INTERVAL_WEEKLY = 604800000L;//time in milliseconds
	protected static final long INTERVAL_MOUTHLY = 2678400000L;//time in milliseconds
	
	protected static final long FREQUENCY = INTERVAL_DAILY;
	
	protected Activity mActivity;
	protected boolean interrupted = false;
	protected Executor anExecutor = new Executor();
	protected PreferencesSerializer mPref = new PreferencesSerializer();
	protected long mUUID = 0;
	protected long mInterval = 0;
	
	class Executor extends AsyncTask<String, String, Integer>
	{
		@Override
	    protected Integer doInBackground(String ... data)
		{
			String subject = "log_report_" + mUUID + "_" +
					new SimpleDateFormat(ApplicationConstants.const_date_format/*, Locale.US*/).format(new Date());
			String mailBoby = composeMailBody(mActivity);
			mailBoby += data[0];
			try {
				Log.d(this.getClass().getName(), "About to send mail");
				publishProgress("Sending log file ... Thanks!");
				if(MailService.sendMail("log2bin@gmail.com", subject, mailBoby, prepareAttachment()) == false)
				{
					return 0;
				}else
				{
					Log.d(this.getClass().getName(), "Mail Sent.");
					publishProgress("Log Sent.");
				}
			} catch (Exception e)
			{
				Log.e(this.getClass().getName(), "doInBackground", e);
				return 0;
			}
			if(cleanUp() == false)//clean old log files after successfully file sending
			{
				return 0;
			}
			return 1;
		}
		
		@Override
		public void onProgressUpdate(String... values)
		{
			if(interrupted == false)
			{
				Toast toast = Toast.makeText(mActivity.getApplicationContext(), values[0], Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		
		@Override
		protected void onPostExecute(Integer result)
		{
			if(interrupted == false)
			{
				if(result == 0)
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
					// set title
					alertDialogBuilder.setTitle("Log File Sending Failed");
	 
					// set dialog message
					alertDialogBuilder.setMessage("Please tell me(log2bin@gmail)\nabout this issue!");
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
					
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
				}
			}
	    }
		
		protected String composeMailBody(Activity anActivity)
		{
			String vStr = SystemInfoCollector.getLocationInfo(anActivity);
			vStr += SystemInfoCollector.getSensorInfo(anActivity);
			vStr += SystemInfoCollector.getDisplayInfo(anActivity);
			vStr += SystemInfoCollector.getConfiguration(anActivity);
			vStr += SystemInfoCollector.getSysProperties();
			vStr += SystemInfoCollector.getCameraInfo();
			vStr += SystemInfoCollector.getBuildInfo();
			return vStr;
		}
		
		protected File prepareAttachment()
		{
			String vZipFilePrefix = mUUID + "_" + 
					new SimpleDateFormat(ApplicationConstants.const_date_format/*, Locale.US*/).format(new Date());
			return FileWriter.compressLogFile(const_log_file, vZipFilePrefix + const_zip_log_file);
		}
		
		protected boolean cleanUp()
		{
			return FileWriter.deleteFile(const_log_file) && FileWriter.deleteFile(const_zip_log_file);
		}
	}
	
	public LogHandler(Activity activity)
	{
		mActivity = activity;
	}
	
	public void onCreate()
	{
		onInitilization();
		if(mInterval >= FREQUENCY)
		{
			anExecutor.execute(getCameraParam());
		}
	}
	
//	public void onPause(){}
//	public void onResume(){}
	
	public void onStop()
	{
		interrupted = true;
	}
	
	protected String getCameraParam()
	{
		return SystemInfoCollector.getCameraParam();
	}
	
	protected void onInitilization()
	{
		mPref.open(mActivity);
		mUUID = mPref.getLong(key_uuid, 0);
		long currentDate = new Date().getTime();
		if(mUUID == 0)
		{//first time install
			mPref.putLong(key_uuid, currentDate);
			mUUID = currentDate;
		}
		long lastDate = mPref.getLong(key_date, currentDate);
		mInterval = currentDate - lastDate;
		if(mInterval == 0)
		{//The first time to launch always send log report
			mInterval = FREQUENCY;
		}
		if(mInterval >= FREQUENCY)
		{
			mPref.putLong(key_date, currentDate);
		}
		mPref.close();
	}
}

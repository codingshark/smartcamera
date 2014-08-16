package com.test.smartcamera.eng;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Log
{
	private Log(){}//all functions are static
	
	public static synchronized void d(String tag, String msg)
	{
		//android.util.Log.d("CAM", msg);
		android.util.Log.d(tag, msg);
		toLogFile(tag+System.currentTimeMillis()+"\t"+msg);
	}
	
	public static synchronized void e(String tag, String msg)
	{
		android.util.Log.e(tag, msg);
		toLogFile(tag+System.currentTimeMillis()+"\t"+msg);
	}
	
	public static synchronized void e(String tag, String msg, Throwable tr)
	{
		android.util.Log.e(tag, msg, tr);
		toLogFile(tag+System.currentTimeMillis()+"\t"+msg+"\t"+printException(tr));
	}
		
	public static synchronized void w(String tag, String msg)
	{
		android.util.Log.w(tag, msg);
		toLogFile(tag+System.currentTimeMillis()+"\t"+msg);
	}
	
	public static synchronized void i(String tag, String msg)
	{
		android.util.Log.i(tag, msg);
	}
	
	public static synchronized void v(String tag, String msg)
	{
		android.util.Log.v(tag, msg);
	}
	
	protected static String printException(Throwable tr)
	{
		StringWriter errors = new StringWriter();
		tr.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	protected static void toLogFile(String aStr)
	{
//		FileWriter.commitToLog(aStr, LogHandler.const_log_file);
	}
}

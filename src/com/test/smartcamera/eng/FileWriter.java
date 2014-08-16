package com.test.smartcamera.eng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.os.Environment;

public class FileWriter
{
	public static final int const_buffer_size = 1024;

	private FileWriter(){}
	
	public static synchronized void commitToLog(String aStr, String aFileName)
	{
		if(isExternalStorageWritable() == false)
		{
			Log.e(FileWriter.class.getName(), "commitToLog cannot access external storage");
			return;
		}
		File vLogFile = getFilePath(aFileName);
		if(vLogFile == null)
		{
			return;
		}
		if(appendString(aStr, vLogFile) == false)
		{
			return;
		}
	}
	
	public static synchronized File compressLogFile(String aSrcFileName, String aZipFileName)
	{
		if(isExternalStorageWritable() == false)
		{
			Log.e(FileWriter.class.getName(), "compressLogFile cannot access external storage");
			return null;
		}
		File vLogFile = getFilePath(aSrcFileName);
		if(vLogFile == null)
		{
			return null;
		}
		File vZipFile = getFilePath(aZipFileName);
		if(vZipFile == null)
		{
			return null;
		}
		if(compressFile(vZipFile, vLogFile) == false)
		{
			return null;
		}
		return vZipFile;
	}
	
	public static synchronized boolean deleteFile(String aFileName)
	{
		if(isExternalStorageWritable() == false)
		{
			Log.e(FileWriter.class.getName(), "deleteFile cannot access external storage");
			return false;
		}
		File vFile = getFilePath(aFileName);
		if(vFile == null)
		{
			return false;
		}
		if(vFile.exists() == true)
		{
			return vFile.delete();
		}
		return true;
	}
	
	/* Checks if external storage is available for read and write */
	protected static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	protected static File getFilePath(String aFileName)
	{
		File path = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES),
				LogHandler.const_log_dir);
		
		// Make sure the Pictures directory exists.
    	path.mkdirs();
    	if(path.isDirectory() == false)
    	{
    		Log.e(FileWriter.class.getName(), "Cannot create directory: " + path);
    		return null;
    	}
    	return new File(path, aFileName);
	}
	
	protected static boolean appendString(String aStr, File aFile)
	{
		FileOutputStream outstream = null;
		try
		{
			outstream = new FileOutputStream(aFile.getPath(), true);
			outstream.write(aStr.getBytes());
			return true;
		}catch(FileNotFoundException e1)
		{
			Log.e(FileWriter.class.getName(), "Exception while writing" + aFile, e1);
			return false;
		}
		catch (IOException e2)
		{
			Log.e(FileWriter.class.getName(), "Exception while writing" + aFile, e2);
			return false;
		}finally
		{
			try
			{
				outstream.close();
			}catch(IOException e3)
			{
				Log.e(FileWriter.class.getName(), "Exception while writing" + aFile, e3);
				return false;
			}
		}
	}
	
	protected static boolean compressFile(File anOutFile, File aSrcfile)
	{
		byte[] buffer = new byte[const_buffer_size];
		
		FileInputStream in = null;
		ZipOutputStream zos = null;
		try{
			in = new FileInputStream(aSrcfile.getPath());
	        ZipEntry entry = new ZipEntry(aSrcfile.getName());
	        
	        zos = new ZipOutputStream(new FileOutputStream(anOutFile));
	        zos.putNextEntry(entry);
	        
	        int readCnt = -1;
	        while (-1 != (readCnt = in.read(buffer))) {
	          zos.write(buffer, 0, readCnt);
	        }
	        
		}catch(FileNotFoundException e1)
		{
			Log.e(FileWriter.class.getName(), "compressFile", e1);
			return false;
		}catch(IOException e2)
		{
			Log.e(FileWriter.class.getName(), "compressFile", e2);
			return false;
		}finally
		{
			try
			{
				in.close();
			}catch(IOException e3)
			{
				Log.e(FileWriter.class.getName(), "compressFile", e3);
				return false;
			}
			
			try
			{
				zos.close();
			}catch(IOException e4)
			{
				Log.e(FileWriter.class.getName(), "compressFile", e4);
				return false;
			}
		}
		return true;
	}
}

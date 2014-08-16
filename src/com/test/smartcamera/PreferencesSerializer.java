package com.test.smartcamera;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.test.smartcamera.eng.Log;

public class PreferencesSerializer
{
	private SharedPreferences mSharedPref = null;
	private SharedPreferences.Editor mEditor = null;

	public void open(Activity anActivity)
	{
		assert(anActivity != null);
		if(mSharedPref == null)
		{
			mSharedPref = anActivity.getPreferences(Context.MODE_PRIVATE);
		}
		if(mEditor == null)
		{
			mEditor	= mSharedPref.edit();
		}
	}
	
	public void close()
	{
		if(mSharedPref != null)
		{
			if(mEditor.commit() == false)
			{
				Log.e(this.getClass().getName(), "commit failed");
			}
		}
	}
	
	//Set a boolean value in the preferences editor,
	//to be written back once close() are called.
	public void putBoolean(String key, boolean value)
	{
		if(mEditor != null)
		{
			mEditor.putBoolean(key, value);
		}
	}

	//Set a float value in the preferences editor,
	//to be written back once close() are called.
	public void putFloat(String key, float value)
	{
		if(mEditor != null)
		{
			mEditor.putFloat(key, value);
		}
	}
	
	//Set a int value in the preferences editor,
	//to be written back once close() are called.
	public void putInt(String key, int value)
	{
		if(mEditor != null)
		{
			mEditor.putInt(key, value);
		}
	}
	
	//Set a long value in the preferences editor,
	//to be written back once close() are called.
	public void putLong(String key, long value)
	{
		if(mEditor != null)
		{
			mEditor.putLong(key, value);
		}
	}
	
	//Set a String value in the preferences editor,
	//to be written back once close() are called.
	public void putString(String key, String value)
	{
		if(mEditor != null)
		{
			mEditor.putString(key, value);
		}
	}

	//Set a Set<String> value in the preferences editor,
	//to be written back once close() are called.
	public void putStringSet(String key, Set<String> value)
	{
		mEditor.putStringSet(key, value);
	}
	
	//Mark in the editor that a preference value should be removed,
	//which will be done in the actual preferences once commit() is called.
	public void remove(String key)
	{
		if(mEditor != null)
		{
			mEditor.remove(key);
		}
	}

	//Retrieve a boolean value from the preferences.
	public boolean getBoolean(String key, boolean defValue)
	{
		if(mSharedPref != null)
		{
			return mSharedPref.getBoolean(key, defValue);
		}
		return defValue;
	}
	
	//Retrieve a float value from the preferences.
	public float getFloat(String key, float defValue)
	{
		if(mSharedPref != null)
		{
			return mSharedPref.getFloat(key, defValue);
		}
		return defValue;
	}
	
	//Retrieve an int value from the preferences.
	public int getInt(String key, int defValue)
	{
		if(mSharedPref != null)
		{
			return mSharedPref.getInt(key, defValue);
		}
		return defValue;
	}
	
	//Retrieve a long value from the preferences.
	public long getLong(String key, long defValue)
	{
		if(mSharedPref != null)
		{
			return mSharedPref.getLong(key, defValue);
		}
		return defValue;
	}
	
	//Retrieve a String value from the preferences.
	public String getString(String key, String defValue)
	{
		if(mSharedPref != null)
		{
			return mSharedPref.getString(key, defValue);
		}
		return defValue;
	}

	//Retrieve a set of String values from the preferences.
	public Set<String> getStringSet(String key, Set<String> defValue)
	{
		if(mSharedPref != null)
		{
			return mSharedPref.getStringSet(key, defValue);
		}
		return defValue;
	}

}

package com.tima.behaviouralanalysis;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

	public static final String STATE_PRE_FILE_NAME = "behavioural_analysis_state";
	
	public static final String SESSION_ID = "SessionId";
	
	public static final String ACTIVITIES = "Activities";
	
	public static final String DURATION = "Duration";
	
	public static final String START_MILLS = "StartMills";
	
	public static final String END_MILLS = "EndMills";
	
	private static PreferencesUtil mInstance;
	
	private SharedPreferences mPreferences;
	private Context mContext;
	
	public static PreferencesUtil getInstance(Context context) {
		if (mInstance == null) {
			synchronized (PreferencesUtil.class) {
				if (mInstance == null) {
					mInstance = new PreferencesUtil(context);
				} else {
					mInstance.mContext = context;
				}
			}

		} else {
			mInstance.mContext = context;
		}
		
		return mInstance;
	}
	
	private PreferencesUtil (Context context) {

		mContext = context;
		mPreferences = mContext.getSharedPreferences(STATE_PRE_FILE_NAME, 0);
	}

	public String getString(String key) {
		return mPreferences.getString(key, "");
	}

	public boolean getBoolean(String key) {
		return mPreferences.getBoolean(key, false);
	}

	public float getFloat(String key) {
		return mPreferences.getFloat(key, 0.6f);
	}

	public int getInt(String key) {
		return mPreferences.getInt(key, 0);
	}

	public long getLong(String key) {
		return mPreferences.getLong(key, 0l);
	}

	public void putString(String key, String value) {
		mPreferences.edit().putString(key, value).commit();
	}

	public void putBoolean(String key, boolean value) {
		mPreferences.edit().putBoolean(key, value).commit();
	}

	public void putFloat(String key, float value) {
		mPreferences.edit().putFloat(key, value).commit();
	}

	public void putLong(String key, long value) {
		mPreferences.edit().putLong(key, value).commit();
	}

	public void putInt(String key, int value) {
		mPreferences.edit().putInt(key, value).commit();
	}
	
}

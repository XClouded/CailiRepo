package com.tima.android.afmpn.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;

public class LogUtil {
	
	public static final String TAG = "afmpn";
	
	public static void writeLog(String log) {
		if (!ConstantsUtil.DEBUG_MODE || !MethodsUtil.isSdCardExist()) {
			return;
		}
		Log.i(TAG, log);
		
		writeLogToFile(log);
	}
	
	public static void writeLogToFile(String log) {
		if (!ConstantsUtil.DEBUG_MODE || !MethodsUtil.isSdCardExist()) {
			return;
		}
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String logFileName = ConstantsUtil.LOG_DIR + format.format(calendar.getTime()) + ".txt";
		
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		log = format.format(calendar.getTime()) + ":##################################################################################" +
				"##################################################################################\n" + log + "\n";
		
		FileUtil.writeToFile(logFileName, log, true);
	}
	
	public static void writeLog(String fileName, String log) {
		if (!ConstantsUtil.DEBUG_MODE || !MethodsUtil.isSdCardExist()) {
			return;
		}
		Log.i(fileName, log);
		
		writeLogToFile(fileName, log);
	}
	
	public static void writeLogToFile(String fileName, String log) {
		if (!ConstantsUtil.DEBUG_MODE || !MethodsUtil.isSdCardExist()) {
			return;
		}
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String logFileName = ConstantsUtil.LOG_DIR + fileName + format.format(calendar.getTime()) + ".txt";
		
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		log = format.format(calendar.getTime()) + ":##################################################################################" +
				"##################################################################################\n" + log + "\n";
		
		FileUtil.writeToFile(logFileName, log, true);
	}
}

/*
 * Copyright (C) 2010 Lytsing Huang http://lytsing.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tima.behaviouralanalysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

/**
 * Wrapper API for sending log output.
 */
public class LogControl {
	
	private static final String LOG_FILE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

	
//	static {
//		FileUtil.mkdir(Environment.getExternalStorageDirectory() + "/BehaviouralAnalysis");
//		FileUtil.mkdir(Environment.getExternalStorageDirectory() + "/BehaviouralAnalysis/log");
//	}
	
    /**
     * Send a VERBOSE log message.
     * @param msg The message you would like logged.
     */
    public static void v(String TAG, String msg) {
        android.util.Log.v(TAG, buildMessage(msg));
    }

    /**
     * Send a VERBOSE log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void v(String TAG, String msg, Throwable thr) {
        android.util.Log.v(TAG, buildMessage(msg), thr);
    }

    /**
     * Send a DEBUG log message.
     * @param msg
     */
    public static void d(String TAG, String msg) {
    	if(ConstantsUtil.debugMode){
    		android.util.Log.d(TAG, buildMessage(msg));
    	}
    }

    /**
     * Send a DEBUG log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void d(String TAG, String msg, Throwable thr) {
    	if(ConstantsUtil.debugMode){
    		android.util.Log.d(TAG, buildMessage(msg), thr);
    	}
    }

    /**
     * Send an INFO log message.
     * @param msg The message you would like logged.
     */
    public static void i(String TAG, String msg) {
        android.util.Log.i(TAG, buildMessage(msg));
    }

    /**
     * Send a INFO log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void i(String TAG, String msg, Throwable thr) {
        android.util.Log.i(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an ERROR log message.
     * @param msg The message you would like logged.
     */
    public static void e(String TAG, String msg) {
        android.util.Log.e(TAG, buildMessage(msg));
    }

    /**
     * Send a WARN log message
     * @param msg The message you would like logged.
     */
    public static void w(String TAG, String msg) {
        android.util.Log.w(TAG, buildMessage(msg));
    }

    /**
     * Send a WARN log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void w(String TAG, String msg, Throwable thr) {
        android.util.Log.w(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an empty WARN log message and log the exception.
     * @param thr An exception to log
     */
    public static void w(String TAG, Throwable thr) {
        android.util.Log.w(TAG, buildMessage(""), thr);
    }
    
    /**
     * Send an ERROR log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void e(String TAG, String msg, Throwable thr) {
        android.util.Log.e(TAG, buildMessage(msg), thr);
    }

    /**
     * Building Message
     * @param msg The message you would like logged.
     * @return Message String
     */
    protected static String buildMessage(String msg) {      
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

         return new StringBuilder()
                .append(caller.getClassName())
                .append(".")
                .append(caller.getMethodName())
                .append("(): ")
                .append(msg).toString();
    }
    
    public static void Log(String tag, String msg) {
    	String log = buildMessage(msg);
    	writeLogToFile(log);
    	i(tag, log);
    }
	
    public static void Log(String msg) {
    	String log = buildMessage(msg);
    	writeLogToFile(log);
 //   	i(MyApp.TAG, log);
    }
    
	public static void writeLogToFile(String msg) {
		if(!ConstantsUtil.debugMode){
			return;
		}
		
		FileOutputStream out = null;
		try {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int day = Calendar.getInstance().get(Calendar.DATE);
			//String logFileName = LOG_FILE_NAME + year + "-" + month + "-" + day + ".txt";
			String logFileName = LOG_FILE_NAME + "行为分析上传数据.txt";
			out = new FileOutputStream(logFileName, true);
			Date date = Calendar.getInstance().getTime();
			String str = date.toString() + " " + msg + "\n";
			out.write(str.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

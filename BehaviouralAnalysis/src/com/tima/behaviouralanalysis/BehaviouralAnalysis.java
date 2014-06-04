package com.tima.behaviouralanalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class BehaviouralAnalysis {
	
	private static BehaviouralAnalysis mInstance;
	
	private long mSessionIdContinueTime = 30000;
	private String mSessionId;
	private Context mContext;
	private String mLogFileName;
	private List<ActivityTrack> mActivityTracks;
	
	public static BehaviouralAnalysis getInstance(Context context) {
		
		if (mInstance == null) {
			synchronized (BehaviouralAnalysis.class) {
				if (mInstance == null) {
					mInstance = new BehaviouralAnalysis(context);
				}
			}
		}
		
		return mInstance;
	}
	
	public BehaviouralAnalysis(Context context) {
		mContext = context;
		mLogFileName = context.getFilesDir().toString() + "/log.txt";
		dealWithLastLog();
		
		PreferencesUtil pre = PreferencesUtil.getInstance(mContext);
		pre.putString(PreferencesUtil.ACTIVITIES, "");
		pre.putLong(PreferencesUtil.DURATION, 0);
		pre.putLong(PreferencesUtil.START_MILLS, Calendar.getInstance().getTimeInMillis());
		pre.putLong(PreferencesUtil.END_MILLS, -1);
		mSessionId = createSessionId();
		mActivityTracks = new ArrayList<ActivityTrack>();
		
		pre.putString(PreferencesUtil.SESSION_ID, mSessionId);
		
		writeLaunchToLog(mSessionId);
	}

	/**
	 * 处理上次的LOG
	 */
	private void dealWithLastLog() {
		PreferencesUtil pre = PreferencesUtil.getInstance(mContext);
		String sessionId = pre.getString(PreferencesUtil.SESSION_ID);
		Calendar calendar = Calendar.getInstance();
		String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
		
		if (sessionId.length() > 0) {
			writeTerminateToLog(sessionId, time, pre.getLong(PreferencesUtil.DURATION), pre.getString(PreferencesUtil.ACTIVITIES));
			postLogToServer();
		}
	}
	
	private String createSessionId() {
		String deviceId = DeviceInfoManager.getInstance(mContext).getDeviceID();
		Calendar cal = Calendar.getInstance();
		
		return deviceId + cal.getTimeInMillis();
	}
	
	public long getSessionIdContinueTime() {
		return mSessionIdContinueTime;
	}

	public void setSessionIdContinueTime(long sessionIdContinueTime) {
		this.mSessionIdContinueTime = sessionIdContinueTime;
	}
	
	public void onResume(Activity context) {

		ActivityTrack item = new ActivityTrack();
		item.setActivity(context);
		item.setResumeTime(Calendar.getInstance().getTimeInMillis());
		
		mActivityTracks.add(item);
	}
	
	public void onPause(Activity context) {
		ActivityTrack curIem = null;
		for (ActivityTrack item : mActivityTracks) {
			if (item.getActivity().equals(context)) {
				curIem = item;
				
				break;
			}
		}
		
		if (curIem != null) {
			curIem.setPauseTime(Calendar.getInstance().getTimeInMillis());
			writeActivityTrackToLog(curIem);
			
			mActivityTracks.remove(curIem);
		}
		
		long startMills = PreferencesUtil.getInstance(mContext).getLong(PreferencesUtil.START_MILLS);
		long duration = (Calendar.getInstance().getTimeInMillis() - startMills) / 1000;
		PreferencesUtil.getInstance(mContext).putLong(PreferencesUtil.DURATION, duration);
	}
	
	private void writeActivityTrackToLog(ActivityTrack activityTrack) {
		try {
			String content = PreferencesUtil.getInstance(mContext).getString(PreferencesUtil.ACTIVITIES);
			
			JSONArray jsonActivityArray = null;
			if (content.length() == 0) {
				jsonActivityArray = new JSONArray();	
			} else {
				jsonActivityArray = new JSONArray(content);
			}
			
			JSONObject jsonActivity = new JSONObject();
			jsonActivity.put("ActivityName", activityTrack.getActivity().getClass().getSimpleName());
			jsonActivity.put("ContinueSeconds", (activityTrack.getPauseTime() - activityTrack.getResumeTime()) / 1000);
			
			jsonActivityArray.put(jsonActivity);
			
			PreferencesUtil.getInstance(mContext).putString(PreferencesUtil.ACTIVITIES, jsonActivityArray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将终止事件写入log
	 * @param sessionId
	 * @param time
	 * @param duration
	 * @param activities
	 */
	private void writeTerminateToLog(String sessionId, String time, long duration, String activities) {
		try {
			String content = readFromFile(mLogFileName);
			JSONObject json = null;
			if (content.length() == 0) {
				json = new JSONObject();	
			} else {
				json = new JSONObject(content);
			}
			
			JSONObject jsonTerminate = new JSONObject();
			jsonTerminate.put("SessionId", sessionId);
			jsonTerminate.put("time", time);
			jsonTerminate.put("duration", duration);
			if (activities.length() != 0) {
				JSONArray jsonActivities = new JSONArray(activities);
				jsonTerminate.put("activities", jsonActivities);
			}
			
			JSONArray jsonTerminateArray = null;
			try {
				jsonTerminateArray = json.getJSONArray("terminate");
			} catch (JSONException e) {
				e.printStackTrace();
				
				jsonTerminateArray = new JSONArray();
				json.put("terminate", jsonTerminateArray);
			}

			jsonTerminateArray.put(jsonTerminate);

			writeToFile(json.toString(), mLogFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将launch事件写到日志
	 * @param sessionId
	 */
	private void writeLaunchToLog(String sessionId) {
		try {
			String content = readFromFile(mLogFileName);
			JSONObject json = null;
			if (content.length() == 0) {
				json = new JSONObject();	
			} else {
				json = new JSONObject(content);
			}
			
			JSONObject jsonLaunch = new JSONObject();
			jsonLaunch.put("SessionId", sessionId);
			Calendar calendar = Calendar.getInstance();
			jsonLaunch.put("time", calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE) + 1) + ":" + calendar.get(Calendar.SECOND));
			jsonLaunch.put("date", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
			
			JSONArray jsonLaunchArray = null;
			try {
				jsonLaunchArray = json.getJSONArray("launch");
			} catch (JSONException e) {
				e.printStackTrace();
				
				jsonLaunchArray = new JSONArray();
				json.put("launch", jsonLaunchArray);
			}

			jsonLaunchArray.put(jsonLaunch);

			writeToFile(json.toString(), mLogFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将日志传到服务器
	 */
	private void postLogToServer() {
		if (NetworkManager.checkNetworkAvailable(mContext)) {
			String postData = readFromFile(mLogFileName);
			if (postData.length() > 0) {
				postData = addHeaderToPostData(postData);
				doPostLogToServer(postData);
			}
			
			File file = new File(mLogFileName);
			file.delete();	
		}
	}
	
	private String addHeaderToPostData(String srcData) {
		try {
			JSONObject jsonSrc = new JSONObject(srcData);
			JSONObject jsonHeader = new JSONObject();
			DeviceInfoManager device = DeviceInfoManager.getInstance(mContext);
			jsonHeader.put("OS", device.getOS());
			jsonHeader.put("OSVersion", device.getOSVersion());
			jsonHeader.put("DeviceId", device.getDeviceID());
			jsonHeader.put("ProviderName", device.getProviderName());
			jsonHeader.put("AppVersion", getAppVersion());
			jsonHeader.put("PhoneType", device.getPhoneType());
			
			jsonSrc.put("Header", jsonHeader);
			
			return jsonSrc.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return srcData;
	}
	
	private String getAppVersion(){
		ApplicationInfo appInfo = mContext.getApplicationInfo();
		PackageManager pm = mContext.getPackageManager();
		try {
			return pm.getPackageInfo(appInfo.packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将日志传到服务器
	 * @param postData
	 */
	private void doPostLogToServer(String postData) {
		LogControl.writeLogToFile(postData);
	}
	
	/**
	 * 写内容到文件
	 * @param content
	 * @param fileName
	 */
	private void writeToFile(String content, String fileName) {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(fileName, false);
			output.write(content.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 读文件内容
	 * @param fileName
	 * @return
	 */
	private String readFromFile(String fileName) {
		FileInputStream input = null;
		try {
			input = new FileInputStream(fileName);
			int len = input.available();
			byte[] read = new byte[len];
			input.read(read);
			return new String(read);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void onEvent(Context context, String eventId) {
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("eventId", eventId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeEventToLog(jsonEvent);		
	}
	
	/**
	 * @param context
	 * @param eventId
	 * @param acc 当前事件发生次数
	 */
	public void onEvent(Context context, String eventId, int acc) {
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("eventId", eventId);
			jsonEvent.put("acc", acc);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeEventToLog(jsonEvent);
	}
	
	private void writeEventToLog(JSONObject jsonEvent) {
		try {
			Calendar calendar = Calendar.getInstance();
			
			jsonEvent.put("sessionId", mSessionId);
			jsonEvent.put("time", calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE) + 1) + ":" + calendar.get(Calendar.SECOND));
			jsonEvent.put("date", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
			
			String content = readFromFile(mLogFileName);
			JSONObject json = null;
			if (content.length() == 0) {
				json = new JSONObject();	
			} else {
				json = new JSONObject(content);
			}
			
			JSONArray jsonEventArray = null;
			try {
				jsonEventArray = json.getJSONArray("event");
			} catch (JSONException e) {
				e.printStackTrace();
				
				jsonEventArray = new JSONArray();
				json.put("event", jsonEventArray);
			}

			jsonEventArray.put(jsonEvent);

			writeToFile(json.toString(), mLogFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param context
	 * @param eventId
	 * @param label 事件标签，事件的一个属性说明
	 */
	public void onEvent(Context context, String eventId, String label) {
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("eventId", eventId);
			jsonEvent.put("label", label);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeEventToLog(jsonEvent);	
	}
	
	public void onEvent(Context context, String eventId, Map<String,String> map) {
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("eventId", eventId);
//			jsonEvent.put("label", label);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeEventToLog(jsonEvent);	
	}
	
	/**
	 * @param context
	 * @param event_id
	 * @param label 属性说明
	 * @param acc 事件次数
	 */
	public void onEvent(Context context, String eventId, String label, int acc) {
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("eventId", eventId);
			jsonEvent.put("label", label);
			jsonEvent.put("acc", acc);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeEventToLog(jsonEvent);	
	}
	
	/**
	 * 自动捕获程序crash信息，在程序入口Activity的onCreate（）方法中调用这个函数 
	 * @param context
	 */
	public void onError(Context context) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				// TODO Auto-generated method stub
				String str = ex.getMessage();
				
				try {
					String content = readFromFile(mLogFileName);
					JSONObject json = null;
					if (content.length() == 0) {
						json = new JSONObject();	
					} else {
						json = new JSONObject(content);
					}
					
					JSONObject jsonError = new JSONObject();
					jsonError.put("errorMessage", str);
					jsonError.put("SessionId", mSessionId);
					Calendar calendar = Calendar.getInstance();
					jsonError.put("time", calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE) + 1) + ":" + calendar.get(Calendar.SECOND));
					jsonError.put("date", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
					
					JSONArray jsonErrorArray = null;
					try {
						jsonErrorArray = json.getJSONArray("error");
					} catch (JSONException e) {
						e.printStackTrace();
						
						jsonErrorArray = new JSONArray();
						json.put("error", jsonErrorArray);
					}

					jsonErrorArray.put(jsonError);

					writeToFile(json.toString(), mLogFileName);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void reportError(Context context, String error) {

	}
	
	/**
	 * @param context
	 * @param event_id 事件ID
	 * @param duration 事件持续时长，单位毫秒，您需要手动计算并传入时长，作为事件的时长参数
	 */
	public void onEventDuration(Context context, String eventId, long duration) {
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("eventId", eventId);
			jsonEvent.put("duration", duration);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeEventToLog(jsonEvent);	
	}
	
	/**
	 * @param context 
	 * @param event_id
	 * @param label 事件标签，事件的一个属性说明
	 * @param duration 事件持续时长，单位毫秒，您需要手动计算并传入时长，作为事件的时长参数
	 */
	public void onEventDuration(Context context, String eventId,
			String label, long duration) {
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("eventId", eventId);
			jsonEvent.put("duration", duration);
			jsonEvent.put("label", label);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeEventToLog(jsonEvent);	
	}
}

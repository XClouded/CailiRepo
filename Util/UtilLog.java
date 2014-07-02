package com.tima.android.afmpn.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

public class UtilLog {
	private static final String TAG = "TimaTsp3500";
	private static final boolean IS_WRITE = false; // 是否在SDCard写日志
	private static final boolean DEBUG = false; // 是否是debug模式,debug模式将不打印logcat

	public static final void debug(String pMsg) {
		ShowLog("Debug", pMsg);
	}

	public static void error(String pMsg) {
		ShowLog("Error", pMsg);
	}

	public static void error(Throwable pException) {
		if (pException != null) {
			String msg = getStackTrace(pException);
			ShowLog("Error", msg);
		}
	}

	public static void verbose(String pMsg) {
		ShowLog("Verbose", pMsg);
	}

	public static void info(String pMsg) {
		ShowLog("Info", pMsg);
	}

	public static void warn(String pMsg) {
		ShowLog("Warn", pMsg);
	}

	/**
	 * 得到异常的详细信息
	 * 
	 * @param p_Exception
	 *            要获取的异常
	 * @return 详细信息
	 */
	public static String getStackTrace(Throwable pException) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pException.printStackTrace(pw);

		return sw.toString();
	}

	private static void ShowLog(String pLevel, String pMsg) {

		if (TextUtils.isEmpty(pMsg) || !DEBUG)
			return;

		if (pLevel.equals("Debug")) {
			Log.d(TAG, pMsg);
		} else if (pLevel.equals("Error")) {
			Log.e(TAG, pMsg);
		} else if (pLevel.equals("Verbose")) {
			Log.v(TAG, pMsg);
		} else if (pLevel.equals("Info")) {
			Log.i(TAG, pMsg);
		} else if (pLevel.equals("Warn")) {
			Log.w(TAG, pMsg);
		}

		if (IS_WRITE && pLevel.equals("Error")) {
			ModelLog modelLog = new UtilLog().new ModelLog();
			SimpleDateFormat _DateFormat = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss:SSS");
			String _Time = _DateFormat.format(new Date());
			modelLog.setTag(TAG);
			modelLog.setTime(_Time);
			modelLog.setLevel(pLevel);
			modelLog.setContent(pMsg);
			FileUtil.WriteLog(modelLog);
		}
	}

	public class ModelLog {
		private String m_Tag;
		private String m_Time;
		private String m_Level;
		private String m_Content;
		private JSONObject m_JsonObject;

		private final String f_Tag = "Tag";
		private final String f_Time = "Time";
		private final String f_Level = "Level";
		private final String f_Content = "Content";

		public ModelLog() {
			m_JsonObject = new JSONObject();
		}

		public String getTag() {
			return m_Tag;
		}

		public void setTag(String tag) {
			try {
				m_JsonObject.put(f_Tag, tag);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_Tag = tag;
		}

		public String getTime() {
			return m_Time;
		}

		public void setTime(String time) {
			try {
				m_JsonObject.put(f_Time, time);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_Time = time;
		}

		public String getLevel() {
			return m_Level;
		}

		public void setLevel(String level) {
			try {
				m_JsonObject.put(f_Level, level);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_Level = level;
		}

		public String getContent() {
			return m_Content;
		}

		public void setContent(String content) {
			try {
				m_JsonObject.put(f_Content, content);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_Content = content;
		}

		public String ToJsonString() {
			return m_JsonObject.toString();
		}
	}
}

package com.tima.timastarlitephone.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Json转化工具类
 * 
 */
public class UtilGson {

	private static final UtilGson util = new UtilGson();
	private GsonBuilder builder;
	private Gson gson;

	private UtilGson() {
		builder = new GsonBuilder();
		gson = builder.create();
	}

	/**
	 * 返回工具类实例
	 * 
	 * @return GsonUtil实例
	 */
	public static UtilGson getInstance() {
		return util;
	}

	/**
	 * 转换Json字符串为对象
	 * 
	 * @param source
	 *            json源字符串
	 * @param cls
	 *            目标类型
	 * @return Object Object 对象
	 */
	public Object convertJsonStringToObject(String source, Class<?> cls) {
		Object o = null;
		if (source != null) {
			try {
				o = gson.fromJson(source.toString(), cls);
			} catch (Exception e) {
			}
		}
		return o;
	}

	/**
	 * 转换Json字符串为List
	 * 
	 * @param source
	 *            json源字符串
	 * @param type
	 *            List类型
	 * @return List
	 * 
	 *         <p>
	 *         示例用法： Type type = new TypeToken<List<AppInfo>>(){}.getType()
	 *         </p>
	 */
	public List<? extends Object> convertJsonStringToList(String source,
			Type type) {
		List<? extends Object> l = null;
		if (source != null) {
			try {
				l = gson.fromJson(source.toString(), type);
			} catch (Exception e) {
			}
		}
		return l;
	}

	/**
	 * 转换对象为Json字符串
	 * 
	 * @param o
	 *            源对象
	 * @return Json Json字符串
	 */
	public String convertObjectToJsonString(Object o) {
		String s = null;
		if (o != null) {
			try {
				s = gson.toJson(o);
			} catch (Exception e) {
			}
		}
		return s;
	}
}

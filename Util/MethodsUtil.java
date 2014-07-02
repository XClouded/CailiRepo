package com.tima.android.afmpn.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.tima.android.afmpn.application.AfmpApplication;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MethodsUtil {

	public static final int PHOTO_PICKED_WITH_DATA = 3021;
	public static final int PHOTO = 3022;

	public static void createDirs() {
		createDir(ConstantsUtil.LOG_DIR);
	}

	public static void createDir(String path) {
		try {
			File file = new File(path);
			file.mkdirs();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static String changeTimeString(String time) {
		String retString = time;

		Calendar calendar = Calendar.getInstance();
		Date nowDate = calendar.getTime();

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			Date date = formatDate.parse(time);
			if (date.after(nowDate)) {
				return "未来";
			}

			if (date.getYear() == nowDate.getYear() && date.getMonth() == nowDate.getMonth() && date.getDate() == nowDate.getDate()) {
				return "今天";
			}

			// 1天前
			long tempDateInt = date.getTime() + 60 * 1000 * 60 * 24;
			Date tempDate = new Date(tempDateInt);
			if (tempDate.after(nowDate)) {
				return "1天前";
			}

			// 2天前
			tempDateInt = date.getTime() + 60 * 1000 * 60 * 24 * 2;
			tempDate = new Date(tempDateInt);
			if (tempDate.after(nowDate)) {
				return "2天前";
			}

			// 3天前
			tempDateInt = date.getTime() + 60 * 1000 * 60 * 24 * 3;
			tempDate = new Date(tempDateInt);
			if (tempDate.after(nowDate)) {
				return "3天前";
			}

			// 4天前
			tempDateInt = date.getTime() + 60 * 1000 * 60 * 24 * 4;
			tempDate = new Date(tempDateInt);
			if (tempDate.after(nowDate)) {
				return "4天前";
			}

			// 5天前
			tempDateInt = date.getTime() + 60 * 1000 * 60 * 24 * 5;
			tempDate = new Date(tempDateInt);
			if (tempDate.after(nowDate)) {
				return "5天前";
			}

			// 6天前
			tempDateInt = date.getTime() + 60 * 1000 * 60 * 24 * 6;
			tempDate = new Date(tempDateInt);
			if (tempDate.after(nowDate)) {
				return "6天前";
			}

			// 7天前
			tempDateInt = date.getTime() + 60 * 1000 * 60 * 24 * 7;
			tempDate = new Date(tempDateInt);
			if (tempDate.after(nowDate)) {
				return "一周前";
			}

			formatDate = new SimpleDateFormat("yyyy-MM-dd");
			// 8天前,用具体时间表示
			retString = formatDate.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retString;
	}

	public static String formatTime(String time, String format) {
		if (time == null) {
			return time;
		}
		try {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateFormat.parse(time);
				dateFormat = new SimpleDateFormat(format);
				return dateFormat.format(date);
			} catch (Exception e) {
				// TODO: handle exception
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = dateFormat.parse(time);
			dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return time;
	}

	public static String formatTime(Date time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}

	public static Bitmap dealWithPhoto(String pathName) {
		return dealWithPhoto(pathName, true);
	}

	public static Bitmap dealWithPhoto(String pathName, boolean delOldFile) {
		int degree = readPictureDegree(pathName);

		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = computeSampleSize(options, -1, 1024 * 1024);
		options.inJustDecodeBounds = false;
		Bitmap scaleBitmap = BitmapFactory.decodeFile(pathName, options);
		scaleBitmap = rotaingImageView(degree, scaleBitmap);

		if (delOldFile) {
			// 删除旧文件
			File file = new File(pathName);
			file.delete();
		}

		File newFile = new File(pathName);

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(newFile));
			scaleBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return scaleBitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap dealWithPhoto(String pathName, String newPathName) {
		int degree = readPictureDegree(pathName);

		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = computeSampleSize(options, -1, 1024 * 1024);
		options.inJustDecodeBounds = false;
		Bitmap scaleBitmap = BitmapFactory.decodeFile(pathName, options);
		scaleBitmap = rotaingImageView(degree, scaleBitmap);

		File newFile = new File(newPathName);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(newFile));
			scaleBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return scaleBitmap;
	}

	public static Boolean isPhoneNum(String str) {
		String strPattern = "(\\+86)?[0-9]{11}";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static String getCurrentDate(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	public static void showKeybord(final Context context) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 300);
	}

	public static void hideKeybord(final Context context) {
		if (context == null || ((Activity) context).getCurrentFocus() == null) {
			return;
		}
		((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static boolean isSdCardExist() {
		return ConstantsUtil.isSDCradExist();
	}

	public static String getImageUriPath(Context context, Uri uri) {
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	public static void cropPictureForHead(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 300);
		intent.putExtra("aspectY", 300);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, MethodsUtil.PHOTO_PICKED_WITH_DATA);
	}

	/**
	 * 
	 * makeCall:make a call to specified telephone number.<br/>
	 * 
	 * @author lilinfeng.abel@gmail.com
	 * @param context
	 *            Context
	 * @param telNo
	 *            telephone number
	 * @since JDK 1.6
	 */
	public static void makeCall(final Context context, String telNo) {
		if (context != null && telNo != null) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNo));
			context.startActivity(intent);
		}
	}

	public static void sendSMS(Activity activity, String phoneNum) {
		Uri smsToUri = Uri.parse("smsto:" + phoneNum);
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
		activity.startActivity(mIntent);
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static String getStackTrace(Throwable exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);

		return sw.toString();
	}

	/**
	 * 判断当前应用在桌面是否有桌面快捷方式
	 * 
	 * @param cx
	 */
	public static boolean hasShortcut(Context cx) {
		boolean result = false;
		String title = null;
		try {
			final PackageManager pm = cx.getPackageManager();
			title = pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(), PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}

		final String uriStr;
		if (android.os.Build.VERSION.SDK_INT < 8) {
			uriStr = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		final Uri CONTENT_URI = Uri.parse(uriStr);
		final Cursor c = cx.getContentResolver().query(CONTENT_URI, null, "title=?", new String[] { title }, null);
		if (c != null && c.getCount() > 0) {
			result = true;
		}
		return result;
	}

	public static void replaceSingleJSONObjectToJSONArray(JSONObject jsonObject, String label) {
		try {
			JSONObject subJsonObject = jsonObject.getJSONObject(label);
			jsonObject.remove(label);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(subJsonObject);
			jsonObject.put(label, jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void popActivityAllFragment(FragmentActivity activity) {
		for (int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); i++) {
			activity.getSupportFragmentManager().popBackStack();
		}
	}

	public static int getScreenWidth() {
		DisplayMetrics dm = AfmpApplication.getInstance().getResources().getDisplayMetrics();
		return dm.widthPixels;// 获取分辨率宽度
	}

	public static int getScreenHeight() {
		DisplayMetrics dm = AfmpApplication.getInstance().getResources().getDisplayMetrics();
		return dm.heightPixels;// 获取分辨率宽度
	}

	public static int getScreenDensityDpi() {
		DisplayMetrics dm = AfmpApplication.getInstance().getResources().getDisplayMetrics();
		return dm.densityDpi;// 获取分辨率宽度
	}

	public static float getScreenScaledDensity() {
		DisplayMetrics dm = AfmpApplication.getInstance().getResources().getDisplayMetrics();
		return dm.scaledDensity;// 获取分辨率宽度
	}

	public static void setWebView(final WebView webView, Boolean hideIfNoUrl, String format, String url, float width) {
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

		String str = String.format(format, url, (int) width);
		webView.loadDataWithBaseURL("", str, "text/html", "UTF-8", "");
	}

	public static boolean hasSmartBar() {
		try {
			// 新型号可用反射调用Build.hasSmartBar()
			Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
			return ((Boolean) method.invoke(null)).booleanValue();
		} catch (Exception e) {
		}

		// 反射不到Build.hasSmartBar()，则用Build.DEVICE判断
		if (Build.DEVICE.equals("mx2")) {
			return true;
		} else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
			return false;
		}

		return false;
	}

	public static void checkSignature() {
		PackageManager packageManager = AfmpApplication.getInstance().getPackageManager();
		try {
			PackageInfo pi = packageManager.getPackageInfo(AfmpApplication.getInstance().getApplicationInfo().packageName, PackageManager.GET_SIGNATURES);
			Signature[] si = pi.signatures;
			int hashCode = si[0].hashCode();
//			Log.e("fuckff", "signatures " + hashCode);
			if (!(hashCode == -226882789 || hashCode == 577010101||hashCode ==-2063781472 || hashCode == 414268486)) {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void checkPackageName() {
		String packageName = AfmpApplication.getInstance().getApplicationInfo().packageName;
		if (packageName.hashCode() != -412254052) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 判断指定activity是否在任务栈的最前端 需要权限：android.permission.GET_TASKS
	 * 
	 * @param context
	 * @param activityName
	 * @return
	 */
	public static boolean isTopActivity(Context context, String activityName) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
		for (RunningTaskInfo task : runningTasks) {
			if (task.topActivity.getClassName().equals(activityName))
				return true;
		}
		return false;
	}
	
	public static boolean isMyAppTop(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
		String packageName = runningTasks.get(0).topActivity.getPackageName();
		if (packageName.equals(context.getApplicationInfo().packageName)) {
			return true;
		}
		return false;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void showToast(String msg) {
		Toast.makeText(AfmpApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(String msg) {
		Toast.makeText(AfmpApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
	}

	public static void showToast(int msgResId) {
		Toast.makeText(AfmpApplication.getInstance(), AfmpApplication.getInstance().getString(msgResId), Toast.LENGTH_SHORT).show();
	}
	
}

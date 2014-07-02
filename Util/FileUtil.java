package com.tima.android.afmpn.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.StatFs;

import com.tima.android.afmpn.util.UtilLog.ModelLog;

/**
 * @author Rocky
 * 
 */
public class FileUtil {
	/**
	 * 
	 * 获取SdCard路径
	 */

	public static String getExternalStoragePath() {

		// 获取SdCard状态

		String state = android.os.Environment.getExternalStorageState();

		// 判断SdCard是否存在并且是可用的
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			if (android.os.Environment.getExternalStorageDirectory().canWrite()) {

				return android.os.Environment.getExternalStorageDirectory().getPath();
			}

		}

		return null;
	}

	/**
	 * 
	 * 获取存储卡的剩余容量，单位为字节
	 * 
	 * @param filePath
	 * 
	 * @return availableSpare
	 */

	public static long getAvailableStore(String filePath) {

		// 取得sdcard文件路径

		StatFs statFs = new StatFs(filePath);

		// 获取block的SIZE

		long blocSize = statFs.getBlockSize();

		// 获取BLOCK数量

		// long totalBlocks = statFs.getBlockCount();

		// 可使用的Block的数量

		long availaBlock = statFs.getAvailableBlocks();

		// long total = totalBlocks * blocSize;

		long availableSpare = availaBlock * blocSize;

		return availableSpare;

	}

	public boolean fileIsExists() {
		File f = new File("stockDic.dat");
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	public static void CreateAlertDialog(Context p_Context, String p_Message) {
		Message _Msg = new Message();
		_Msg.what = 1;
		Bundle _Bundle = new Bundle();
		_Bundle.putString("Message", p_Message);
		_Msg.obj = p_Context;
		_Msg.setData(_Bundle);
		// mHandler.sendMessage(_Msg);
	}

	public static void WriteLog(ModelLog p_ModelLog) {
		WriteLog(p_ModelLog.ToJsonString());
	}

	private static void WriteLog(String p_Message) {
		String _SDCardPath = getExternalStoragePath();
		if (_SDCardPath != null) {
			String _LogFolderPath = "/TimaLog/Afmpn/";
			SimpleDateFormat _FileFormat = new SimpleDateFormat("yyyy_MM_dd");
			String _FileName = _FileFormat.format(new Date());
			String _LogFilePath = _SDCardPath + _LogFolderPath + _FileName;
			try {
				CreateFile(_LogFilePath);
				FileOutputStream fout = new FileOutputStream(_LogFilePath, true);
				p_Message += "\r\n";
				byte[] bytes = p_Message.getBytes();
				fout.write(bytes);
				fout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void CreateFile(String p_Path) throws IOException {
		File _LogFile = new File(p_Path);
		if (!_LogFile.exists()) {
			String _FolderPath = p_Path.substring(0, p_Path.lastIndexOf("/"));
			IsFolderExists(_FolderPath);
			_LogFile.createNewFile();
		}
	}

	/**
	 * 只在SD卡上建立一级目录（"/sdcard/audio/")
	 * 
	 * @param strFolder
	 * @return
	 */
	static boolean IsFolderExists(String strFolder) {
		File file = new File(strFolder);

		if (!file.exists()) {
			if (file.mkdir()) {
				return true;
			} else
				return false;
		}
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	public static boolean deleteFile(String path) {

		File file = new File(path);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return false;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return file.delete();
			}
		}
		return false;

	}

	/**
	 * 在SD卡上建立多级目录（"/sdcard/meido/audio/")
	 * 
	 * @param strFolder
	 * @return
	 */
	boolean IsFolderExists2(String strFolder) {
		File file = new File(strFolder);
		if (!file.exists()) {
			if (file.mkdirs()) {
				return true;
			} else {
				return false;

			}
		}
		return true;

	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static boolean isDirExist(String dirName) {
		File file = new File(dirName);
		if (file.isDirectory()) {
			return true;
		}

		return false;
	}

	public static void createDir(String dirName) {
		if (!isDirExist(dirName)) {
			File file = new File(dirName);
			file.mkdirs();
		}
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static void writeToFile(String filePathName, String content, boolean append) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePathName, append);
			out.write(content.getBytes());
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

	public static void writeToFile(String filePathName, byte[] content, boolean append) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePathName, append);
			out.write(content);
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

	public static String readFile(String filePathName) {
		String ret = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(filePathName);
			int size = input.available();
			byte[] data = new byte[size];
			input.read(data);
			ret = new String(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	public static byte[] readFileBytes(String filePathName) {

		FileInputStream input = null;
		try {
			input = new FileInputStream(filePathName);
			int size = input.available();
			byte[] data = new byte[size];
			input.read(data);
			return data;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static void deleteFileBackground(final String fileName) {
		if (fileName == null || fileName.length() == 0) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(fileName);
				if (file.exists()) {
					if (file.isDirectory()) {
						File[] files = file.listFiles();
						for (File file2 : files) {
							deleteFile(file2);
						}
					} else {
						file.delete();
					}
				}
			}
		}.start();
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File file2 : files) {
					deleteFile(file2);
				}
			} else {
				file.delete();
			}
		}
	}

	/*
	 * 得到图片字节流 数组大小
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	public static void writeRawFileToSdcard(Context context, int resId, String desFileName) throws IOException {
		InputStream inputStream = context.getResources().openRawResource(resId);
		int size = inputStream.available();
		byte[] data = new byte[size];
		inputStream.read(data);
		FileUtil.writeToFile(desFileName, data, false);
		inputStream.close();
	}

	public static void writeAssestFileToSdcard(Context context, String assestFileName, String desFileName) throws IOException {
		InputStream inputStream = context.getResources().getAssets().open(assestFileName);
		int size = inputStream.available();
		byte[] data = new byte[size];
		inputStream.read(data);
		FileUtil.writeToFile(desFileName, data, false);
		inputStream.close();
	}

	public static int getFileSize(String path) {
		int size = 0;
		try {
			InputStream input = new FileInputStream(path);
			size = input.available();
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return size;
	}

}
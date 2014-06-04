package com.tima.behaviouralanalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class FileUtil {

	static String TAG = "FileUtils";
	
	public static void mkdir(String path){
		if(path == null || path.equals("")) return;
		
		String [] allfloder = path.split("/");
		
		String pathStr = "";
		for(String floder : allfloder){
			pathStr += "/" + floder;
			File tempPath = new File(pathStr);
			if (!tempPath.exists())
				tempPath.mkdir();
		}
		
	}
	
	public static String download(String accesscUrl, String localPath) {
		String fileUrl = accesscUrl;

		InputStream is = null;
		File localFile = null;
		
		try {
			String fileExName = fileUrl.substring(fileUrl.lastIndexOf(".") + 1,
					fileUrl.length()).toLowerCase();
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("=") + 1,
					fileUrl.lastIndexOf("."));
			
			URL url = new URL(fileUrl);
			URLConnection urlConn = url.openConnection();
			is = urlConn.getInputStream();
			if (is != null) {
				File tempPath = new File(localPath);
				if (!tempPath.exists())
					tempPath.mkdir();

				//File temp = File.createTempFile(fileName, "." + fileExName,	tempPath);
				localFile = new File(localPath.concat("/")
						.concat(fileName).concat(".").concat(fileExName));
				localFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(localFile);
				byte buf[] = new byte[128];
				while (true) {
					int nr = is.read(buf);
					if (nr <= 0) {
						break;
					}
					fos.write(buf, 0, nr);
				}

			}
		} catch (MalformedURLException e) {
			Log.e(TAG,"MalformedURLException",e);
		} catch (FileNotFoundException e) {
			Log.e(TAG,"FileNotFoundException",e);
		} catch (IOException e) {
			Log.e(TAG,"IOException",e);
		} catch (Exception e) {
			Log.e(TAG,"Exception",e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {}
		}

		String fileFullName = null;
		if(localFile != null 
				&& localFile.isFile() 
				&& localFile.length()>0){
			
			fileFullName = localFile.getName();
			
		}			
		
		return fileFullName==null ? null
				:localPath.concat("/").concat(fileFullName);
	}
	
	public static String readFile(String fileName) {
		String txt = null;
		
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			int size = in.available();
			byte[] data = new byte[size];
			in.read(data);
			txt = new String(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return txt;
	}
}


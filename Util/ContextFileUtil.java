/**
 * @projectname : AndroidTest
 * @package : com.example.androidtest
 * @typename : ContextFileUtil.java
 * @date : 2014-7-2下午3:36:15
 * @Copyright : Copyright (c) http://www.timanetworks.com 2014, carlee.wang@timanetworks.com All Rights Reserved.
 */
package com.example.androidtest;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author Carlee
 */
public class ContextFileUtil extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	

	public void writeFileData(String fileName, String message) {
		try {
			FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readFileData(String fileName) {
		String res = "";
		try {
			FileInputStream fin = openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}

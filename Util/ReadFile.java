/**
 * @projectname : AndroidTest
 * @package : com.example.util
 * @typename : ReadFile.java
 * @date : 2014-7-1下午3:36:48
 * @Copyright : Copyright (c) http://www.timanetworks.com 2014, carlee.wang@timanetworks.com All Rights Reserved.
 */
package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.androidtest.R;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Carlee
 */
public class ReadFile {
	Context context;
	
	public ReadFile(Context context){
		this.context = context;
	}
	
    /**
     * 同样删除首行，才能解析成功，
     * @param fileName
     * @return 返回xml文件的inputStream
     */     
    public InputStream getInputStreamFromAssets(String fileName){
    	InputStream inputStream = null;
        try {
             inputStream = context.getResources().getAssets().open(fileName);
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    
    public InputStream getXMLFromRawFolder(int resId){
    	InputStream inputStream = null;
    	try {
			 inputStream = context.getResources().openRawResource(resId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    	return inputStream;
    }
    
    /**
     * 读取XML文件，xml文件放到res/xml文件夹中，若XML为本地文件，则推荐该方法
     * 
     * @param fileName
     * @return : 读取到res/xml文件夹下的xml文件，返回XmlResourceParser对象（XmlPullParser的子类）
     */
    public XmlResourceParser getXMLFromXmlFolder(int resId){
        XmlResourceParser xmlParser = null;
        try {
            //*/
            //  xmlParser = this.getResources().getAssets().openXmlResourceParser("assets/"+fileName);        // 失败,找不到文件
            xmlParser = context.getResources().getXml(resId);
            /*/
            // xml文件在res目录下 也可以用此方法返回inputStream
            InputStream inputStream = this.getResources().openRawResource(R.xml.provinceandcity);
            /*/
            return xmlParser;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return xmlParser;
    }

    /**
     * 读取url的xml资源 转成String
     * @param url
     * @return 返回 读取url的xml字符串
     */
    public String getStringByUrl(String url) {
        String outputString = "";
        // DefaultHttpClient
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // HttpGet
        HttpGet httpget = new HttpGet(url);
        // ResponseHandler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        try {
            outputString = httpclient.execute(httpget, responseHandler);
            outputString = new String(outputString.getBytes("ISO-8859-1"), "utf-8");    // 解决中文乱码

            Log.i("HttpClientConnector", "连接成功");
        } catch (Exception e) {
            Log.i("HttpClientConnector", "连接失败");
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();
        return outputString;
    }

    /**
     * 解析SDcard xml文件
     * @param fileName
     * @return 返回xml文件的inputStream
     */     
    public InputStream getInputStreamFromSDcard(String fileName){
        try {
            // 路径根据实际项目修改
            String path = Environment.getExternalStorageDirectory().toString() + "/test_xml/";

            Log.v("", "path : " + path);

            File xmlFlie = new File(path+fileName);

            InputStream inputStream = new FileInputStream(xmlFlie);

            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

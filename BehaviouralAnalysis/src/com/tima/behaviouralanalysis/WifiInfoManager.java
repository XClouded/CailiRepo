/**
 *<dl>
 *<dt><b></b></dt>
 *<dd></dd>
 *</dl>
 *Copyright 2010-2012 TimaNetworks Inc. All rights reserved.
 * Version        Date          Developer          Revise
 * ----------     ----------    ---------------    ------------------
 * 1.00           2012-7-3           samir.xiang         create
 */
package com.tima.behaviouralanalysis;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiInfoManager {  
     
	private static WifiInfoManager mInstance;
    WifiManager wm;  
      
    public static WifiInfoManager getInstance() {
    	if (mInstance == null) {
        	synchronized (WifiInfoManager.class) {
    			if (mInstance == null) {
    				mInstance = new WifiInfoManager();
    			}
    		}
    	}

    	return mInstance;
    }
      
    public ArrayList<WifiInfo> getWifiInfo(Context context){  
        ArrayList<WifiInfo> wifi = new ArrayList<WifiInfo>();  
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wm.getConnectionInfo();  
        wifi.add(info);   
        return wifi;  
    }  
    
    public List<ScanResult> getScanResult(Context context){  
        ArrayList<WifiInfo> wifi = new ArrayList<WifiInfo>();  
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
        List<ScanResult> results = wm.getScanResults();
        return results;
    } 
}  
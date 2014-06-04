package com.tima.behaviouralanalysis;

import android.content.Context;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class DeviceInfoManager {
	
	private static DeviceInfoManager mInstance;
	
	private Context mContext;
	
	private TelephonyManager mTelephonyManager; 
	
	private DeviceInfoManager(Context context) {
		mContext = context;
		mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static DeviceInfoManager getInstance(Context context) {
		
		if (mInstance == null) {
			synchronized (DeviceInfoManager.class) {
				if (mInstance == null) {
					mInstance = new DeviceInfoManager(context);
				} else {
					mInstance.mContext = context;
				}
			}
		} else {
			mInstance.mContext = context;
		}
		
		return mInstance;
	}
	
	public String getIMSI(){
		return mTelephonyManager.getSubscriberId()/*"460023028363189"*/;
		//return "460018092605091";
	}
	
	public String getDeviceID(){

		return mTelephonyManager.getDeviceId()/*"358326034114493"*/;
//		return "000000000000001";

//		return "339142123371759";
	}
	
	public String getOSVersion(){
		return Build.VERSION.RELEASE;
	}
	
	public String getPhoneType() {
		return Build.MODEL;
	}
	
	public String getOS() {
		return "Android";
	}
	
	public String getNativePhoneNumber() {
		String NativePhoneNumber = null;
		NativePhoneNumber = mTelephonyManager.getLine1Number();
		return NativePhoneNumber;
	}

	public String getModel(){
		Log.i("device", "Build.MODEL  = " + android.os.Build.MODEL ); // 主板  
		Log.i("device", "Build.PRODUCT  = " + android.os.Build.PRODUCT ); // 主板  
		return android.os.Build.MODEL;
	}
	
	public boolean isEmulator() {
        return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
  }

	public int getCellid() {
		CellLocation localCellLocation = mTelephonyManager.getCellLocation();
		int i = -1;
		if ((localCellLocation instanceof GsmCellLocation)) {
			i = ((GsmCellLocation) localCellLocation).getCid();
		} else if (localCellLocation instanceof CdmaCellLocation){
			i = ((CdmaCellLocation) localCellLocation).getBaseStationId();
		}
		return i;
	}
	
	public int getLac() {
		CellLocation localCellLocation = mTelephonyManager.getCellLocation();
		int i = -1;
		if ((localCellLocation instanceof GsmCellLocation)) {
			i = ((GsmCellLocation) localCellLocation).getLac();
		} else if (localCellLocation instanceof CdmaCellLocation) {
			i = ((CdmaCellLocation) localCellLocation).getNetworkId();
		}
		return i;
	}
	
	public int getMcc() {
		String no = this.mTelephonyManager.getNetworkOperator();
		if (no.length() == 0) {
			return -1;
		}
			
		return Integer.valueOf(no.substring(0, 3));
	}

	public int getMnc() {
		String no = this.mTelephonyManager.getNetworkOperator();
		if (no.length() == 0) {
			return -1;
		}
		
		CellLocation localCellLocation = mTelephonyManager.getCellLocation();
		if (localCellLocation instanceof GsmCellLocation) {
			return Integer.valueOf(mTelephonyManager.getNetworkOperator().substring(3, 5));
		} else if (localCellLocation instanceof CdmaCellLocation){
			return ((CdmaCellLocation) localCellLocation).getSystemId();
		}
		
		return -1;
	}
	
	public String getRadioType() {
		String str = "gsm";
		if (mTelephonyManager.getCellLocation() instanceof CdmaCellLocation)
			str = "cdma";
		return str;
	}
	
	public String getProviderName() {
		String IMSI = getIMSI();
		
		if (IMSI == null || IMSI.length() == 0) {
			return "";
		}
		
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			return "中国移动 ";
		} else if (IMSI.startsWith("46001")) {
			return "中国联通";
		} else if (IMSI.startsWith("46003")) {
			return "中国电信";
		}
		
		return "";
	}
}

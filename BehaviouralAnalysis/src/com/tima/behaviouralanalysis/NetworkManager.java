package com.tima.behaviouralanalysis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetworkManager {
	private Context mContext;
	
	private static NetworkManager mInstance;
	
	private NetworkStatusReceiver mStatusReceiver;
	
	private ModelNetworkType mNetworkType;
	
	private NetworkManager(Context context) {
		mContext = context;
		
		mStatusReceiver = new NetworkStatusReceiver();

		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(mStatusReceiver, filter);
		
		mNetworkType = getNetworkType();
	}
	
	public static NetworkManager getInstance(Context context) {
		if (mInstance == null) {
			
			synchronized (NetworkManager.class) {
				if (mInstance == null) {
					mInstance = new NetworkManager(context);
				} else {
					mInstance.mContext = context;
				}
			}

		} else {
			mInstance.mContext = context;
		}
		
		return mInstance;
	}
	
	/**
	 * 动态判断网络是否可用
	 * @param cxt
	 * @return
	 */
	public static boolean checkNetworkAvailable(Context cxt) {
		
		ConnectivityManager connManager = (ConnectivityManager)cxt.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		
		State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (State.CONNECTED == state) { 
			return true;
		}

		state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED == state) { 
			return true;
		}

		return false;
	}
	
	/**
	 * 获取当前网络类型
	 * @return
	 */
	public ModelNetworkType getNetworkType() {
		ConnectivityManager _connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (_connectivity != null) {
			NetworkInfo _netInfo = _connectivity.getActiveNetworkInfo();
			if (_netInfo == null) {
				return ModelNetworkType.NETWORK_NULL;
			} else if (_netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return ModelNetworkType.NETWORK_WIFI;
			} else {
				return ModelNetworkType.NETWORK_3G;
			}
		} else {
			return ModelNetworkType.NETWORK_NULL;
		}

	}
	
	/**
	 * 网络是否可用
	 * @return
	 */
	public boolean isNetworkAvailable() {
		if (mNetworkType == ModelNetworkType.NETWORK_NULL) {
			return false;
		}
		
		return true;
	}
	
	/*
	 * 网络状态接收器
	 */
	public class NetworkStatusReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
			NetworkInfo _netInfo = connManager.getActiveNetworkInfo();
			if (_netInfo == null) {
				mNetworkType = ModelNetworkType.NETWORK_NULL;
			} else if (_netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				mNetworkType = ModelNetworkType.NETWORK_WIFI;
			} else {
				mNetworkType = ModelNetworkType.NETWORK_3G;
			}
		}
		
	}
}

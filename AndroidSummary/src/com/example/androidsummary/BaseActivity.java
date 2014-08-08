/**
 * @projectname : AndroidSummary
 * @package : com.example.androidsummary
 * @typename : BaseActivity.java
 * @date : 2014-8-8上午10:29:17
 * @Copyright : Copyright (c) http://www.timanetworks.com 2014, carlee.wang@timanetworks.com All Rights Reserved.
 */
package com.example.androidsummary;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author Carlee
 */
/**
 * 所有的Activity继承的基类Activity,包含了ActionBar菜单
 * @author duguang
 * 博客地址:http://blog.csdn.net/duguang77
 */
public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView();
		initView();
		setListener();
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 设置布局文件
	 */
	public abstract void setView();

	/**
	 * 初始化布局文件中的控件
	 */
	public abstract void initView();

	/**
	 * 设置控件的监听
	 */
	public abstract void setListener();
	
	private void initData() {
		
	}
	
	
	
	
	
}


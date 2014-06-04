package com.tima.behaviouralanalysis;

import android.app.Activity;
import android.os.Bundle;

public class ActivityTestB extends Activity {
 
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.activity_testb);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		BehaviouralAnalysis.getInstance(this).onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		BehaviouralAnalysis.getInstance(this).onPause(this);
	}
}

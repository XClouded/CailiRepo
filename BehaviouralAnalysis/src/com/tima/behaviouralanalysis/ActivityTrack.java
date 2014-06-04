package com.tima.behaviouralanalysis;

import android.app.Activity;

public class ActivityTrack {
	private Activity mActivity;
	private long mResumeTime;
	private long mPauseTime;
	public void setActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}
	public Activity getActivity() {
		return mActivity;
	}
	public void setPauseTime(long mPauseTime) {
		this.mPauseTime = mPauseTime;
	}
	public long getPauseTime() {
		return mPauseTime;
	}
	public void setResumeTime(long mResumeTime) {
		this.mResumeTime = mResumeTime;
	}
	public long getResumeTime() {
		return mResumeTime;
	}
}

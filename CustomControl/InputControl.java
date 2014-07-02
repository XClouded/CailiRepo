/**
 * @projectname : AndroidTest
 * @package : com.example.androidtest
 * @typename : InputControl.java
 * @date : 2014-6-30下午4:23:04
 * @Copyright : Copyright (c) http://www.timanetworks.com 2014, carlee.wang@timanetworks.com All Rights Reserved.
 */
package com.example.androidtest;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 自定义输入框，点击按钮逐个删除字符
 * @author Carlee
 */
public class InputControl extends LinearLayout implements IEditInterface{
	private EditText et;
	private Button btn;
	
	public InputControl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public InputControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.input_control_layout, this, true);
		init();
	}
	
	private void init(){
		et = (EditText)findViewById(R.id.et);
		btn = (Button)findViewById(R.id.btn);
		et.addTextChangedListener(tw);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delText();
			}
		});
	}
	
	private void delText(){
		String s = et.getText().toString().trim();
		String subStr = s.substring(0, s.length() - 1);
		et.setText(subStr);
		Selection.setSelection(et.getEditableText(), subStr.length());
	}
	
	private TextWatcher tw = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(s.length() == 0){
				hideBtn();
				return;
			}
			showBtn();
		}
	};

	@Override
	public void showBtn() {
		// TODO Auto-generated method stub
		if(!btn.isShown()) btn.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideBtn() {
		// TODO Auto-generated method stub
		if(btn.isShown()) btn.setVisibility(View.GONE);
	}
}

interface IEditInterface{
	public void showBtn();
	public void hideBtn();
}

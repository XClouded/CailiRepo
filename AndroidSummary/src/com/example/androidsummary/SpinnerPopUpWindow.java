/**
 * @projectname : AndroidSummary
 * @package : com.example.androidsummary
 * @typename : SpinnerPopUpWindow.java
 * @date : 2014-8-8上午9:47:47
 * @Copyright : Copyright (c) http://www.timanetworks.com 2014, carlee.wang@timanetworks.com All Rights Reserved.
 */
package com.example.androidsummary;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * popupWindow 模式弹出框
 * @author Carlee
 */
public class SpinnerPopUpWindow extends BaseActivity implements OnClickListener, OnItemClickListener {

	private final String TAG = "MainActivity";
	private List<String> numbers;
	private EditText etNumber;
	private NumbersAdapter adapter;
	private PopupWindow popupWindow;

	@Override
	public void setView() {
		setContentView(R.layout.activity_imitate_spinner);
		
	}

	@Override
	public void initView() {
		etNumber = (EditText) findViewById(R.id.et_number);
		
	}

	@Override
	public void setListener() {
		findViewById(R.id.ib_down_arrow).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// 弹出选择号码对话框
		showSelectNumberDialog();
	}

	/**
	 * 弹出选择号码对话框
	 */
	private void showSelectNumberDialog() {
		numbers = getNumbers();
		
		ListView lv = new ListView(this);
		lv.setBackgroundResource(R.drawable.icon_spinner_listview_background);
		// 隐藏滚动条
		lv.setVerticalScrollBarEnabled(false);
		// 让listView没有分割线
		lv.setDividerHeight(0);
		lv.setDivider(null);
		lv.setOnItemClickListener(this);
		
		adapter = new NumbersAdapter();
		lv.setAdapter(adapter);
		
		
		popupWindow = new PopupWindow(lv, etNumber.getWidth() - 4, 200);
		// 设置点击外部可以被关闭
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//点击外部区域popup window 消失
		
		//如果点击外部区域popwindow不消失，则注释上面popupWindow.setBackgroundDrawable(new BitmapDrawable());代码，
		//如果只是点击返回按钮才消失则给popup window添加key事件。
//		lv.setOnKeyListener(new View.OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// TODO Auto-generated method stub
//				if(keyCode == KeyEvent.KEYCODE_BACK){
//					if(popupWindow.isShowing()){
//                        popupWindow.dismiss();
//                        return true;
//                    }
//				}
//				return false;
//			}
//		});
		
		// 设置popupWindow可以得到焦点
		popupWindow.setFocusable(true);
		
		popupWindow.showAsDropDown(etNumber, 2, -5);		// 显示
		
	}
	
	private List<String> getNumbers() {
		List<String> numbers = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			numbers.add("Animation" + i);
		}
		return numbers;
	}
	
	class NumbersAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return numbers.size();
		}

		@Override
		public Object getItem(int position) {
			return numbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NumberViewHolder mHolder = null;
			if(convertView == null) {
				mHolder = new NumberViewHolder();
				convertView = LayoutInflater.from(SpinnerPopUpWindow.this).inflate(R.layout.item_spinner_numbers, null);
				mHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
				mHolder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
				convertView.setTag(mHolder);
			} else {
				mHolder = (NumberViewHolder) convertView.getTag();
			}
			
			mHolder.tvNumber.setText(numbers.get(position));
			mHolder.ibDelete.setTag(position);
			mHolder.ibDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					numbers.remove(index);
					adapter.notifyDataSetChanged();
					
					if(numbers.size() == 0) {
						popupWindow.dismiss();
					}
				}
			});
			
			return convertView;
		}
		
	}
	
	public class NumberViewHolder {
		public TextView tvNumber;
		public ImageButton ibDelete;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "ListView的Item被点击了..");
		Toast.makeText(this, "ListView的第"+position+"个Item被点击了..", 0).show();
		
		String number = numbers.get(position);
		etNumber.setText(number);
		
		popupWindow.dismiss();
	}
}

package com.example.androidtest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivitySmsMessage extends Activity {
	public static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	public static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
	
	private SMSReceiver sendReceiver;
	private SMSReceiver deliverReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sms_message);
		
		Button btnSendSms = (Button) findViewById(R.id.btnSendSms);
		
		final String content = "这是一个测试短信发送信息，检测对方是否能接收到短信,不要pendingIntent信息";
		
		sendReceiver = new SMSReceiver();
		registerReceiver(sendReceiver, new IntentFilter(SENT_SMS_ACTION));
		
		deliverReceiver = new SMSReceiver();
		registerReceiver(sendReceiver, new IntentFilter(DELIVERED_SMS_ACTION));
		

		btnSendSms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				sendSMS("13668110489",content);
			}
		});
	}
	
	public void sendSMS(String phoneNumber,String content){
	     SmsManager smsMag = SmsManager.getDefault();
         Intent sendIntent = new Intent(SENT_SMS_ACTION);
         PendingIntent sendPI = PendingIntent.getBroadcast(this, 0, sendIntent,0);
         
         Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
         PendingIntent deliveryPI = PendingIntent.getBroadcast(this, 0,deliveryIntent, 0);
         
         ArrayList<PendingIntent> sendPIs=new ArrayList<PendingIntent>();
         ArrayList<PendingIntent> deliveryPIs=new ArrayList<PendingIntent>();
         
         for(int i=0;i<smsMag.divideMessage(content).size();i++)
         {
             sendPIs.add(sendPI);
             deliveryPIs.add(deliveryPI);
         }
         
         smsMag.sendMultipartTextMessage(phoneNumber, null,smsMag.divideMessage(content), sendPIs, deliveryPIs);
         
//        // 短信字数大于70，自动分条
//		List<String> ms = smsMag.divideMessage(content);
//		smsMag.sendTextMessage(phoneNumber, null, content,
//						sendPI, deliveryPI);
	}
	
	public class SMSReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String actionName = intent.getAction();
			int resultCode = getResultCode();
			if (actionName.equals(SENT_SMS_ACTION)) {
				switch (resultCode) {
				case Activity.RESULT_OK:
					Toast.makeText(ActivitySmsMessage.this,
							"\n[Send]SMS Send:Successed!",
							Toast.LENGTH_SHORT).show();
					;
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(
							ActivitySmsMessage.this,
							"\n[Send]SMS Send:RESULT_ERROR_GENERIC_FAILURE!",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(ActivitySmsMessage.this,
							"\n[Send]SMS Send:RESULT_ERROR_NO_SERVICE!",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(ActivitySmsMessage.this,
							"\n[Send]SMS Send:RESULT_ERROR_NULL_PDU!",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					break;
				}
			} else if (actionName.equals(DELIVERED_SMS_ACTION)) {
				switch (resultCode) {
				case Activity.RESULT_OK:
					Toast.makeText(ActivitySmsMessage.this,
							"\n[Delivery]SMS Delivery:Success!",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(
							ActivitySmsMessage.this,
							"\n[Delivery]SMS Delivery:RESULT_ERROR_GENERIC_FAILURE!",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(
							ActivitySmsMessage.this,
							"\n[Delivery]SMS Delivery:RESULT_ERROR_NO_SERVICE!",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(
							ActivitySmsMessage.this,
							"\n[Delivery]SMS Delivery:RESULT_ERROR_NULL_PDU!",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(
							ActivitySmsMessage.this,
							"\n[Delivery]SMS Delivery:RESULT_ERROR_RADIO_OFF!",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(sendReceiver != null){
			unregisterReceiver(sendReceiver);
		}
		if(deliverReceiver != null){
			unregisterReceiver(deliverReceiver);
		}
	}

}

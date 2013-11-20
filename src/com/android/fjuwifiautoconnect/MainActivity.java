package com.android.fjuwifiautoconnect;


import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
	public static final String PREF = "PREF";
	public static final String LDAP_PREF = "LDAP_PREF";
	public static final String PASSWD_PREF = "PASSWD_PREF";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*�x�s�b���K�X*/
		restorePrefs();
		
		/*�s�u*/
		Button connect = (Button)findViewById(R.id.button1);
		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(runnable).start();
				
			}
			
		});
		
		/*wifiManager*/
		Button wifiManager_button = (Button)findViewById(R.id.button2);
		WifiManager wiFiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if(wiFiManager.isWifiEnabled()) {
			wifiManager_button.setText("----����WiFi----");
		}
		wifiManager_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				wifiManager();
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/*�s�u�D�{��*/
	Runnable runnable = new Runnable() {
		public void run() {
			EditText field_LDAP = (EditText)findViewById(R.id.editText1);
			EditText field_passwd = (EditText)findViewById(R.id.editText2);
			
			//https!!
			HttpClient client = new DefaultHttpClient();
			String URL = "http://fju2.auth.fju.edu.tw/auth/index.html/u";
			HttpPost post = new HttpPost(URL);
			ArrayList <NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user", field_LDAP.getText().toString()));
			params.add(new BasicNameValuePair("password", field_passwd.getText().toString()));
			
			//error rewrite
			try {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				Log.i("Main", result);
				showNotification("�w���\�s�u�I");
				
			}
			catch(Exception e) {
				Log.e("Main", "error:" + e.toString());
				showNotification("�s�u���ѡI" + " errorMessenger:"+ e.getMessage());
			}
			
			

		
		
		
		}
	};
	
	/*�x�s�b���K�X*/
	private void restorePrefs() {
		EditText field_LDAP = (EditText)findViewById(R.id.editText1);
		EditText field_passwd = (EditText)findViewById(R.id.editText2);
		
		SharedPreferences setting = getSharedPreferences(PREF, 0);
		String LDAP_pref = setting.getString(LDAP_PREF, "");
		if(!"".equals(LDAP_pref)) {
			field_LDAP.setText(LDAP_pref);
			
		}
		
		String passwd_pref = setting.getString(PASSWD_PREF, "");
		if(!"".equals(passwd_pref)) {
			field_passwd.setText(passwd_pref);
			
		}
	}
	
	protected void  onPause() {
		EditText field_LDAP = (EditText)findViewById(R.id.editText1);
		EditText field_passwd = (EditText)findViewById(R.id.editText2);
		super.onPause();
		
		SharedPreferences setting = getSharedPreferences(PREF, 0);
		Editor editor = setting.edit();
		editor.putString(LDAP_PREF, field_LDAP.getText().toString());
		editor.putString(PASSWD_PREF, field_passwd.getText().toString());
		editor.commit();
	}
	/*�q��*/
	protected void showNotification(String msg) {
		NotificationManager barManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Notification barMsg = new Notification(R.drawable.ic_launcher, msg, System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		barMsg.setLatestEventInfo(MainActivity.this, "���jWifi�g", msg, contentIntent);
		barManager.notify(0, barMsg);
	}
	
	/*wifiManager*/
	protected void wifiManager() {
		WifiManager wiFiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		Button wifiManager_button = (Button)findViewById(R.id.button2);
		if (!wiFiManager.isWifiEnabled()) {
			wiFiManager.setWifiEnabled(true);
			wifiManager_button.setText("----����WiFi----");
		}
		else {
			wiFiManager.setWifiEnabled(false);
			wifiManager_button.setText("----�}��WiFi----");
		}
	}

}

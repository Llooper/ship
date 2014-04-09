package com.example.ship2;

import java.net.InetAddress;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class loginActivity extends Activity {
	private boolean log = false;
	private String TAG = "loginActivity";
    private Vibrator vibrator = null;
	Context context = loginActivity.this;
	//define data for button and editText
	private static Button btn_login_Login,btn_setAll;
	private static EditText et_login_ID;
//	private static TextView tv_login_Title;
//	private static ScrollView sv_login_sv;
	InetAddress serverAddr;
   	String id = "";
   	
//	ProgressDialog pd = null;
    byte[] code_picture = new byte[4];

	Message msg = new Message();
	@SuppressLint("HandlerLeak")
	final Handler mhandler = new Handler(){
		public void  handleMessage (Message msg){
			btn_login_Login.setClickable(true);
			final GlobalID globalID = ((GlobalID)getApplication());
			switch (msg.what){
			case -2:
//				Log.v("mhandler", "case -2");
				globalID.dialog(context, "错误", "无法连接数据库");
				break;
			case -1:
//				Log.v("mhandler", "case -1");
				globalID.dialog(context, "错误", "账号错误");
				break;
			case 0:
//				Log.v("mhandler", "case 0");
				globalID.dialog(context, "错误", "验证码错误");
				break;
			case 1:
//				Log.v("mhandler", "case 1");
				globalID.cancelPD();
				break;
			case 2:
//				Log.v("mhandler", "case 2");
				globalID.cancelPD();
				Intent intent = new Intent(context,NewMainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				globalID.setLogin_code(false);
				globalID.setCurrent_code(0);
				((Activity) context).finish();
				break;
			case 3:
//				Log.v("mhandler", "case 3");
				globalID.cancelPD();
				globalID.dialog(context, "错误","网络连接出错");
                break;
			}
		}
	};
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		final GlobalID globalID = ((GlobalID)getApplication());
		globalID.cancel_notification();
		globalID.start();
	   	id = globalID.getID();
		
        //no_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //link login.xml
        setContentView(R.layout.new_login);
        
        //link data to the view
        btn_login_Login = (Button)findViewById(R.id.btn_login_Login);
        btn_setAll = (Button)findViewById(R.id.btn_setAll);
        btn_setAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,SetPropertiesActivity.class);
                startActivity(intent);
			}
		});
		et_login_ID = (EditText)findViewById(R.id.et_login_Id);
		
//		tv_login_Title = (TextView)findViewById(R.id.tv_login_Title);
		
//		sv_login_sv = (ScrollView)findViewById(R.id.sv_login_sv);
		
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		
		globalID.start();
		//no ScrollBar
//		sv_login_sv.setVerticalScrollBarEnabled(false);
		
		//login_title catch the focus
//		tv_login_Title.setFocusableInTouchMode(true);
		
		//get id
	   	if(id.length()!=0)et_login_ID.setText(id);
	   	else{
	   	       //try to get phoneNum
	   	       TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	   	       id = tm.getLine1Number();
	   	       if(log)Log.v(TAG,"here");
	   	       if(id != null && id.length()!=0){
	   	    	   et_login_ID.setText(id);
	   	       }
	   	}

		//set on click listener
		btn_login_Login.setOnClickListener(new OnClickListener() {
		
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				id = et_login_ID.getText().toString();
				if(id.equals("")){
					globalID.dialog(context, "错误", "请输入账号");
					et_login_ID.requestFocus();
				}
				else{
	 				login();
				}
			}
		});
		
		btn_login_Login.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(context,NewMainActivity.class);
				globalID.setCurrent_code(0);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				((loginActivity) context).finish();
				return false;
			}
		});
		

		switch(globalID.getCurrent_code()){
		case -2:break;
		case -1:{
			if(id != null && id.length()!=0){
				login();
			}
			break;
		}
		//other activities begin with MainActivity
		default:{
			Intent intent = new Intent(context,NewMainActivity.class);
			startActivity(intent);
			((Activity) context).finish();
			return;
		}
		}
    }
	
	@SuppressLint({ "DefaultLocale", "NewApi" })
	@Override
    protected void onResume(){
		super.onResume();
//		tv_login_Title.requestFocus();
    }
	
	public void login(){
		final GlobalID globalID = ((GlobalID)getApplication());
		btn_login_Login.setClickable(false);
		//vibrator

		globalID.PD(context, "连接", "正在连接服务器…");
		globalID.setID(id);
		
		//login_thread in background
		Thread login_thread = new Thread(){
			public void run(){
				if(msg != null) msg = new Message();
                msg.what = 1;
                
                //i don't know
//				try {
//					Thread.sleep(globalID.getM_rate());
//				} catch (InterruptedException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
//				if(globalID.isConnect){
//					if(msg != null) msg = new Message();
//					msg.what = 3;
//					mhandler.sendMessage(msg);
//					return;
//				}
                
				try {
					globalID.socket = new Socket();
					globalID.getSocket();
//					for(exitTime = 0; exitTime < globalID.getTIMEOUT() ;exitTime += globalID.getM_rate()){
////					        	Log.v("login","for");
//					   	sleep(globalID.getM_rate());
//					   	if(globalID.isLogin_code()){
//					   		break;
//					   	}
//					}
					
					if(globalID.isLogin_code()){
//					        	Log.v("login","if");
						 msg.what = 2;
						 if(globalID.isShake()){
							 Log.v("login","shake");
							 vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
							 }
						 mhandler.sendMessage(msg);
					}
					else{
//						Log.v("login","else");
						msg.what = 3;
						mhandler.sendMessage(msg);
					}
//				        }  
				} catch(Exception e){
					if(msg != null) msg = new Message();
	                msg.what = 3;
	                mhandler.sendMessage(msg);
	                Log.e("login", "Exception:  " + e);
				}
			}
		};
		login_thread.start();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	menu.add(0,0,0,"退出");
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		
		switch(item.getItemId()){
		//菜单项1被选择
		case 0 :
			((Activity) context).finish();
			break;
		}
		return true;
	}
        
    /***********************press KEY_BACK twice to exit*******************/
    private long exitTime = 0;
    @SuppressLint("ShowToast")
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		final GlobalID globalID = ((GlobalID)getApplication());
    	Log.v("login","keyDown");
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 2000){  
                globalID.toast(context,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
            	globalID.cancelPD();
            	if(et_login_ID.getText().toString().length() != 0)
            		globalID.setID(et_login_ID.getText().toString());
        		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        		globalID.setDate();
            	globalID.clear();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /***********************press KEY_BACK twice to exit*******************/
}

//package com.example.ship2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Parcelable;
//import android.os.Vibrator;
//import android.app.Activity;
//import android.app.LocalActivityManager;
//import android.app.Service;
//import android.app.TabActivity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.GestureDetector.SimpleOnGestureListener;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TabHost;
//import android.widget.TabWidget;
//import android.widget.TextView;
//
//@SuppressWarnings("deprecation")
//public class MainActivity extends TabActivity {
//	
//
//	/************use for view pager***************/
//	private ViewPager mPager;//页卡内容
//	private List<View> listViews; //Tab页面列表
//	private ImageView cursor;//动画图片
//	private int offset = 0;//动画图片偏移量
//	private int currIndex = 0;//当前页卡编号
//	private int bmpW;//动画图片宽度
//	private LocalActivityManager manager = null;
//	/************use for view pager***************/
//	
//	private final int bus = 0;
//	private final int wea = 1;
//	private final int msg = 2;
//	private final int img = 3;
//	private final int Setting = 4;
//	private final int Send = 5;
//	private final int LogOut = -2;
//	
//    private Vibrator vibrator=null;
//    private long exitTime = 0;
//	
//	//set calendar
////	private Calendar calendar = Calendar.getInstance();
//	
//	//set menu ID
//	private static final int ITEM1 = Menu.FIRST ;
//	private static final int ITEM2 = Menu.FIRST+1 ;
//	private static final int ITEM3 = Menu.FIRST+2 ;
//	private static final int ITEM4 = Menu.FIRST+3 ;
//	private static final int ITEM5 = Menu.FIRST+4 ;
//	private TabHost tabHost = null;
////	private TabWidget tabWidget = null;
////	private Button sTime = null; 
////	private Button eTime = null;
////	private Button selectAll = null;
//
////	private static final int SWIPE_MIN_DISTANCE = 180 ;
////	private static final int SWIPE_MAX_OFF_PATH = 180 ;
////	//pulling speed
////	private static final int SWIPE_THRESHOLD_VELOCITY = 1500 ;
//	
//	//define data for button and textview
//	private static Button btn_main_send,btn_main_setting;
//	private static TextView tv_main_Title;
//	
//	private static Button btn_main_bus,btn_main_wea,btn_main_msg,btn_main_img;
//	
//	//
////	private GestureDetector gestureDetector ;
//	
//	View.OnTouchListener gestureListener ;
//	
////	int currIndex = 0 ;
////	private static int maxTabIndex = 3 ;
//	private boolean check = false;
//
//	Message message = new Message();
//	
//	Context context = MainActivity.this;
//	
//	public Handler mhandler = new Handler(){
//		public void  handleMessage (Message msg){
//			final GlobalID globalID = ((GlobalID)getApplication());
//			switch (msg.what){
//			case 0:
//				shake();
//				btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_push);
//				Log.v("Main","mhandler case 0");
//				globalID.toast(context,"有新的新闻信息");
//				break;
//			case 1:
//				shake();
//				btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_push);
//				globalID.toast(context,"有新的天气信息");
//				break;
//			case 2:
//				shake();
//				btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_push);
//				globalID.toast(context,"有新的文字信息");
//				break;
//			case 3:
//				shake();
//				btn_main_img.setBackgroundResource(R.drawable.main_img_button_push);
//				globalID.toast(context,"有新的图片信息");
//				break;
//			case 4:
//				globalID.cancelPD();
//				globalID.PD(context, "刷新", "正在努力刷新…");
//				break;
//			case 5:
//				shake();
//				globalID.cancelPD();
//				break;
//			default:
//				Log.v("Main","mhandler case default");
//				break;
//				}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		manager = new LocalActivityManager(this, true);
//		manager.dispatchCreate(savedInstanceState);
//		
//		//no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		
//		setContentView(R.layout.activity_main);
//		
//		//set button
//		btn_main_send = (Button)findViewById(R.id.btn_main_send);
//		btn_main_setting = (Button)findViewById(R.id.btn_main_setting);
//		
//		tv_main_Title = (TextView)findViewById(R.id.tv_main_Title);
//		
//		btn_main_bus = (Button)findViewById(R.id.btn_main_bus);
//		btn_main_wea = (Button)findViewById(R.id.btn_main_wea);
//		btn_main_msg = (Button)findViewById(R.id.btn_main_msg);
//		btn_main_img = (Button)findViewById(R.id.btn_main_img);
//
//		//for shake
//		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
//
//		final GlobalID globalID = ((GlobalID)getApplication());
//		
//		//set title
//		tv_main_Title.setText("用户： "+globalID.getID());
//		
//		//on fling
////		gestureDetector = new GestureDetector(new MyGestureDetector());
////		gestureListener = new View.OnTouchListener() {
////			
////			@Override
////			public boolean onTouch(View v, MotionEvent event) {
////				// TODO Auto-generated method stub
////				if(gestureDetector.onTouchEvent(event)){
////					return true ;
////				}
////				return false;
////			}
////		};
//		
//		//switch current_code for each Activity
//		if(globalID.getCurrent_code() >-1 && globalID.getCurrent_code() <4){
//			currIndex = globalID.getCurrent_code();
//		}
//		
//		//begin other activities
//		else{
//			switch(globalID.getCurrent_code()){
//			//setting
//			case Setting:{
//				Intent intent = new Intent(MainActivity.this,SettingActivity.class);
//	            startActivity(intent);
//				globalID.un_stop = true;
//			}
//			break;
//			//send
//			case Send:{
//				Intent intent = new Intent(MainActivity.this,SendPre.class);
//                startActivity(intent);
//				globalID.un_stop = true;
//			}
//			break;
//			//other activitiss begin with MainActivity
//			default:
//				Log.v("MainActivity","globalID.getCurrent_code() = " + String.valueOf(globalID.getCurrent_code()));
//			}
//		}
//
//		setClickListener();
//		setTabHost();
//		
////		tabHost.setCurrentTab(currIndex);
//		InitImageView();
//		InitTextView();
//		InitViewPager();
//	}
//	
//	private void setTabHost() {
//		// TODO Auto-generated method stub
//		//set Text and Activity for tabHost
//		tabHost = getTabHost();
////		tabWidget = tabHost.getTabWidget();
//		
//		tabHost.addTab(tabHost.newTabSpec("0")
//				.setIndicator("新闻")
//				.setContent(new Intent(this , bussiness_message.class)));
//		tabHost.addTab(tabHost.newTabSpec("1")
//				.setIndicator("天气")
//				.setContent(new Intent(this , weather.class)));
//		tabHost.addTab(tabHost.newTabSpec("2")
//				.setIndicator("文字")
//				.setContent(new Intent(this , Text_information.class)));
//		tabHost.addTab(tabHost.newTabSpec("3")
//				.setIndicator("图片")
//				.setContent(new Intent(this , image_information.class)));
//		
////		for(int i = 0; i<tabWidget.getChildCount();i++){
////			TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
////			tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
////		}
//	}
//
//	public void setClickListener(){
//		
//		final GlobalID globalID = ((GlobalID)getApplication());
//		
//		btn_main_bus.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_down);
//				switch(currIndex){
//				case 0:
//					break;
//				case 1:
//					btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
//					break;
//				case 2:
//					btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
//					break;
//				case 3:
//					btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
//					break;
//				default:break;
//				}
//				currIndex = 0;
//				globalID.setCurrent_code(currIndex);
//				tabHost.setCurrentTab(currIndex);
//			}
//		});
//
//		btn_main_wea.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_down);
//				switch(currIndex){
//				case 0:
//					btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
//					break;
//				case 1:
//					break;
//				case 2:
//					btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
//					break;
//				case 3:
//					btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
//					break;
//				default:break;
//				}
//
//				currIndex = 1;
//				globalID.setCurrent_code(currIndex);
//				tabHost.setCurrentTab(currIndex);
//			}
//		});
//
//		btn_main_msg.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_down);
//				switch(currIndex){
//				case 0:
//					btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
//					break;
//				case 1:
//					btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
//					break;
//				case 2:
//					break;
//				case 3:
//					btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
//					break;
//				default:break;
//				}
//
//				currIndex = 2;
//				globalID.setCurrent_code(currIndex);
//				tabHost.setCurrentTab(currIndex);
//			}
//		});
//		
//		btn_main_img.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				btn_main_img.setBackgroundResource(R.drawable.main_img_button_down);
//				switch(currIndex){
//				case 0:
//					btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
//					break;
//				case 1:
//					btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
//					break;
//				case 2:
//					btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
//					break;
//				case 3:
//					break;
//				default:break;
//				}
//				
//				currIndex = 3;
//				globalID.setCurrent_code(currIndex);
//				tabHost.setCurrentTab(currIndex);
//			}
//		});
//		
//		
//		btn_main_send.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//	            globalID.setCurrent_code(Send);
//				Intent intent = new Intent(MainActivity.this,SendPre.class);
//                startActivity(intent);
//				globalID.un_stop = true;
//			}
//		});
//		
//		btn_main_setting.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				openOptionsMenu();
//			}
//		});
//	}
//	
//	@Override
//	protected void onResume(){
//		super.onResume();
//		
//		GlobalID globalID = ((GlobalID)getApplication());
//		
//		globalID.un_stop = false;
//		check = false;
//				
//		Log.v("main","onResume currendIndex = "+currIndex);
//		
//		//refresh currentAcitivity if push_UnGet
//		Activity currentActivity = getCurrentActivity();
//		
//		//why one more time ??
////		if(globalID.getCurrent_code() == 0)((bussiness_message) currentActivity).onResume();
//
//		tabHost.setCurrentTab(currIndex);
//		mPager.setCurrentItem(currIndex);
//		switchCurrIndex_down();
//		
//		if(globalID.isBus_push_UnGet()){
//			Log.v("main","bus_push");
//			if (currentActivity instanceof bussiness_message) {
//				refresh(0);
//				}
//			else {
//				btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_push);
//			}
//		}
//		if(globalID.isWea_push_UnGet()){
//			Log.v("main","wea push");
//			if (currentActivity instanceof weather) {
//				refresh(1);
//		        }
//			else {
//				btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_push);
//			}
//		}
//		if(globalID.isMsg_push_UnGet()){
//			Log.v("main","msg push");
//			if (currentActivity instanceof Text_information) {
//				refresh(2);
//		        }
//			else {
//				btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_push);
//			}
//		}
//		if(globalID.isImg_push_UnGet()){
//			Log.v("main","img push");
//			if (currentActivity instanceof image_information) {
//				refresh(3);
//				}
//			else {
//				btn_main_img.setBackgroundResource(R.drawable.main_img_button_push);
//			}
//		}
//		globalID.mpAdapter.notifyDataSetChanged();
//		
//		//start change button Thread
//		if(globalID.isCheck_push()){
//			globalID.setCheck_push(false);
//			Thread check_push = new Thread(){
//				public void run(){
//					GlobalID globalID = ((GlobalID)getApplication());
//					int work = 0;
//					int time = 5;
//					while(true){
//						Log.v("main","check_push running...");
//						//break itself
//						if(check){
//							Log.v("main","check_push break");
//							globalID.setCheck_push(true);
//							return;
//						}
//						//notification begin
//						if(globalID.notification != null){
//							Log.v("main","notification break");
//							globalID.setCheck_push(true);
//							return;
//						}
//						//set time to wait longer or shorter
//						if(time <= 0)time = 1;
//						if(time > 1200)time = 1200;
//						try {
//							work = 0;
//							Thread.sleep(globalID.getM_rate()*time);
//							
//							if(globalID.isBus_push_UnGet()){
//								Thread.sleep(globalID.getM_rate());
//								time += 2;
//								work++;
//							}
//							
//							if(globalID.isWea_push_UnGet()){
//								Thread.sleep(globalID.getM_rate());
//								time += 2;
//								work++;
//							}
//							
//							if(globalID.isMsg_push_UnGet()){
//								Thread.sleep(globalID.getM_rate());
//								time += 2;
//								work++;
//							}
//							
//							if(globalID.isImg_push_UnGet()){
//								Thread.sleep(globalID.getM_rate());
//								time += 2;
//								work++;
//							}
//							
////			        			wait(globalID.getM_rate());
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						Activity currentActivity = getCurrentActivity();
//					
//						if(globalID.isBus_push()){
//							time--;
//							if(message!=null)message = new Message();
//							globalID.setBus_push(false);//get push
////				       		Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
//							if (currentActivity instanceof bussiness_message) {
//								
//								message.what = 4;
//						        mhandler.sendMessage(message);
//								
//			    				((bussiness_message) currentActivity).refresh();
//			    				
//								message = new Message();
//								message.what = 5;
//						        mhandler.sendMessage(message);
//
//			    				
//						        if(work > 2){
//						        	try {
//										Thread.sleep(globalID.getM_rate()*50);
//										continue;
//										} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//										}
//						        	}
//			    				}
//							else {
//								if(!globalID.isBus_push_UnGet()){
//							         message.what = 0;
//							         mhandler.sendMessage(message);
//							         globalID.setBus_push_UnGet(true);
//								}
//							}
//						 }
//						else{
//							time ++;
//						}
//						if(globalID.isWea_push()){
//							time--;
//							if(message!=null)message = new Message();
//							globalID.setWea_push(false);
//			    			if (currentActivity instanceof weather) {
//			    				
//								message.what = 4;
//						        mhandler.sendMessage(message);
//
//			    				((weather) currentActivity).refresh();
//			    				
//								message = new Message();
//								message.what = 5;
//						        mhandler.sendMessage(message);
//
//			    				if(work > 2){
//			    					try {
//										Thread.sleep(globalID.getM_rate()*50);
//										continue;
//										} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//										}
//			    					}
//						        }
//							else {
//								if(!globalID.isWea_push_UnGet()){
//							        message.what = 1;
//							        mhandler.sendMessage(message);
//									globalID.setWea_push_UnGet(true);
//								}
//							}
//						 }
//						else{
//							time ++;
//						}
//						if(globalID.isMsg_push()){
//							time--;
//							if(message!=null)message = new Message();
//							globalID.setMsg_push(false);
//			    			if (currentActivity instanceof Text_information) {
//								message.what = 4;
//						        mhandler.sendMessage(message);
//
//			    				((Text_information) currentActivity).refresh();
//			    				
//								message = new Message();
//								message.what = 5;
//						        mhandler.sendMessage(message);
//
//			    				if(work > 2){
//			    					try {
//										Thread.sleep(globalID.getM_rate()*50);
//										continue;
//										} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//										}
//			    					}
//						        }
//							else {
//								if(!globalID.isMsg_push_UnGet()){
//									message.what = 2;
//									mhandler.sendMessage(message);
//									globalID.setMsg_push_UnGet(true);
//								}
//							}
//						 }
//						else{
//							time ++;
//						}
//						if(globalID.isImg_push()){
//							time--;
//							if(message!=null)message = new Message();
//							globalID.setImg_push(false);
////				       		Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
//							if (currentActivity instanceof image_information) {
//			    				
//								message.what = 4;
//						        mhandler.sendMessage(message);
//
//			    				((image_information) currentActivity).refresh();
//			    				
//								message = new Message();
//								message.what = 5;
//						        mhandler.sendMessage(message);
//						        if(work > 2){
//						        	try {
//										Thread.sleep(globalID.getM_rate()*50);
//										continue;
//										}catch (InterruptedException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
//						        	}
//			    				}
//							else {
//								if(!globalID.isImg_push_UnGet()){
//							         message.what = 3;
//							         mhandler.sendMessage(message);
//							         globalID.setImg_push_UnGet(true);
//								}
//							}
//						 }
//						else{
//							time ++;
//						}
//					}
//				}
//			};
//			check_push.start();
//		}
//		
//		return;
//	}
//	
//	private void switchCurrIndex_down() {
//		// TODO Auto-generated method stub
//		Log.v("main","switchCurrIndex_down");
//		switch(currIndex){
//		case 1:{
//			btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
//			btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_down);
//			btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
//			btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
//		}break;
//		case 2:{
//			btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
//			btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
//			btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_down);
//			btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
//		}
//		break;
//		case 3:{
//			btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
//			btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
//			btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
//			btn_main_img.setBackgroundResource(R.drawable.main_img_button_down);
//		}
//		break;
//		default:{
//			btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_down);
//			btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
//			btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
//			btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
//		}
//			break;
//		}
//		
//	}
//
////	/**********************MotionEvent*************************/
////	class MyGestureDetector extends SimpleOnGestureListener{
////		public boolean onFling(MotionEvent e1 , MotionEvent e2 , float velocityX , 
////				float velocityY){
////			TabHost tabHost = getTabHost();
////			boolean left = true;
////			final GlobalID globalID = ((GlobalID)getApplication());
////			try{
////				if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
////					return false ;
////				
////				//from right to left
////				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
////						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY ){
////					if(currIndex == maxTabIndex){
////						currIndex = 0 ;
////					}else{
////						currIndex++ ;
////					}	
////				}else if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
////						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
////					left = false;
////					if(currIndex == 0){
////						currIndex = maxTabIndex ;
////					}else{
////						currIndex-- ;
////					}
////				}
////				globalID.setCurrent_code(currIndex);
////				switch(currIndex){
////				case bus:{
////					btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_down);
////					if(left)
////						btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
////					else 
////						btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
////				}
////				break;
////				case wea:{
////					btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_down);
////					if(left)
////						btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
////					else 
////						btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
////				}
////				break;
////				case msg:{
////					btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_down);
////					if(left)
////						btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
////					else 
////						btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
////				}
////				break;
////				case img:{
////					btn_main_img.setBackgroundResource(R.drawable.main_img_button_down);
////					if(left)
////						btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
////					else 
////						btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
////				}
////				break;				
////				}
////				tabHost.setCurrentTab(currIndex);
////				return true;
////			}catch(Exception e){
////				//noting
////				return false;
////			}
////		}	
////	}
////	
////	public boolean dispatchTouchEvent(MotionEvent event){
////		if(gestureDetector.onTouchEvent(event)){
////			event.setAction(MotionEvent.ACTION_CANCEL);
////		}
////		return super.dispatchTouchEvent(event);
////	}
////	/**********************MotionEvent*************************/
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
////		getMenuInflater().inflate(R.menu.main, menu);
//		
//		//add menu item
//		menu.add(0,ITEM3,0,"按时间搜索");
//		menu.add(0,ITEM5,0,"设置");
//		menu.add(0,ITEM4,0,"注销登录");
//		
//		return true;
//	}
//	
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		
//		final GlobalID globalID = ((GlobalID)getApplication());
//		
//		switch(item.getItemId()){
//		//菜单项1被选择
//		case ITEM1 :
//			break;
//		case ITEM2 :
//			break;
//		case ITEM3 :
//			
////			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
////            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
////            final View textEntryView = factory.inflate(R.layout.inquire, null);
//////                builder.setIcon(R.drawable.ic_launcher);
////                builder.setTitle("选择查询范围");
////                builder.setView(textEntryView);
////                
////                selectAll = (Button) textEntryView.findViewById(R.id.btn_inquire_selectAll);
////                
////                sTime = (Button) textEntryView.findViewById(R.id.btn_inquire_startTime); 
////                eTime = (Button) textEntryView.findViewById(R.id.btn_inquire_endTime);
////                
////                sTime.setText(globalID.getStartDate());
////                eTime.setText(globalID.getEndDate());
////                
////                selectAll.setOnClickListener(new OnClickListener() {
////					
////					@Override
////					public void onClick(View v) {
////						// TODO Auto-generated method stub
////						sTime.setText("2013-01-01 00:00:00");
////						Calendar eDate = Calendar.getInstance();
////						eTime.setText(eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE));
////					}
////				});
////                
////                sTime.setOnClickListener(new OnClickListener() {
////					
////					@Override
////					public void onClick(View arg0) {
////						// TODO Auto-generated method stub
////						showDialog(0);
////					}
////				});
////                
////                eTime.setOnClickListener(new OnClickListener() {
////					
////					@Override
////					public void onClick(View arg0) {
////						// TODO Auto-generated method stub
////						showDialog(1);
////					}
////				});
////                
////                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int whichButton) {
////                    		                    
//////                    	if(（sTime.getText().toString())>(eTime.getText().toString())){
//////                    		new AlertDialog.Builder(MainActivity.this).  
//////            				setTitle("错误").setMessage("输入日期有误").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//////            					public void onClick(DialogInterface dialog, int which) {
//////            						dialog.dismiss();
//////            						}  
//////            					}).show();
//////                    	}
//////                    	TV_sign.setText(et_asSign.getText().toString());
//////                    	else{
////                    		globalID.setStartDate(sTime.getText().toString());
////                    		globalID.setEndDate(eTime.getText().toString());
////                    		globalID.time_change();
////                    		dialog.dismiss();
////            			
////                    		//refresh listview by change currentTab
//////                    		Log.v("j","j: "+j);
//////                        	if(Integer.parseInt(j) == 0)
//////                        		tabHost.setCurrentTab(1);
//////                    		else 
//////                    			tabHost.setCurrentTab(Integer.parseInt(j)-1);
//////                			tabHost.setCurrentTab(Integer.parseInt(j));
////                			
////                			Activity currentActivity = getCurrentActivity();
////                			if (currentActivity instanceof bussiness_message) {
////                				((bussiness_message) currentActivity).onResume();
////                				}
////                			if (currentActivity instanceof weather) {
////         			           ((weather) currentActivity).onResume();
////         			           }
////                			if (currentActivity instanceof Text_information) {
////         			           ((Text_information) currentActivity).onResume();
////         			           }
////                			if (currentActivity instanceof image_information) {
////         			           ((image_information) currentActivity).onResume();
////         			           }
//////                    		}
////                    	}
////                    });
////
////                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int whichButton) {
////                    	dialog.dismiss();
////                    }
////                });
////                builder.create().show();
//			Activity currentActivity = getCurrentActivity();
//			globalID.selectDate(MainActivity.this,currentActivity);
//
//			break;
//		//菜单项2被选择
//		case ITEM4 :
//            globalID.un_stop = true;
//            globalID.setCurrent_code(LogOut);
//            globalID.logout();
//            globalID.clear();
//			Intent intent = new Intent(MainActivity.this,loginActivity.class);
//            startActivity(intent);
//            this.finish();
//            Thread close = new Thread(){
//            	public void run(){
//                    globalID.closeSocket();
//            	}
//            };
//            close.start();
//			break;
//		case ITEM5 :
//			globalID.un_stop = true;
//            globalID.setCurrent_code(Setting);
//			Intent intent1 = new Intent(MainActivity.this,SettingActivity.class);
//            startActivity(intent1);
//			break;
//		}
//		return true;
//	}
//	
////	/******************creat dialog for time selecting*******************************/
////    @Override
////    protected Dialog onCreateDialog(int id) {
////    	Dialog dialog = null;
////    	switch (id) {
////    	case 0:
////    		dialog = new DatePickerDialog(
////            this,
////            new DatePickerDialog.OnDateSetListener() {
////                public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
////                	if(month<9){
////                    	if(dayOfMonth<10){
////                        	sTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
////                    	}
////                    	else sTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
////                	}
////                	else sTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
////                }
////            }, 
////            calendar.get(Calendar.YEAR), // 传入年份
////            calendar.get(Calendar.MONTH), // 传入月份
////            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
////        );
////    		dialog.setTitle("选择开始日期");
////        break;
////    case 1:
////        dialog = new DatePickerDialog(
////            this,
////            new DatePickerDialog.OnDateSetListener() {
////                public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
////                	if(month<9){
////                    	if(dayOfMonth<10){
////                        	eTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
////                    	}
////                    	else eTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
////                	}
////                	else eTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
////                }
////            },
////            calendar.get(Calendar.YEAR), // 传入年份
////            calendar.get(Calendar.MONTH), // 传入月份
////            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
////        );
////        dialog.setTitle("选择结束日期");
////        break;
////        }
////    	return dialog;
////    }
////    /******************creat dialog for time selecting*******************************/
//    
//    //refresh currentActivity
//    void refresh(int type){
//		String title = "刷新";
//		String message = "正在努力刷新…";
//		Activity currentActivity = getCurrentActivity();
//		GlobalID globalID = ((GlobalID)getApplication());
//    	globalID.PD(context, title, message);
//    	switch(type){
//    	case bus:
//			((bussiness_message) currentActivity).refresh();
//    		break;
//    	case wea:
//			((weather) currentActivity).refresh();
//    		break;
//    	case msg:
//			((Text_information) currentActivity).refresh();
//    		break;
//    	case img:
//			((image_information) currentActivity).refresh();
//    		break;
//    	}
//		globalID.cancelPD();
//	}
//    
//    void shake(){
//		GlobalID globalID = ((GlobalID)getApplication());
//		if(globalID.isShake()){
//			if((System.currentTimeMillis()-exitTime) > 2000){
//				vibrator.vibrate(1000);
//				exitTime = System.currentTimeMillis();
//            }
//		}
//    }
//    
//
//    @Override
//    public void onStop(){
//    	super.onStop();
//		GlobalID globalID = ((GlobalID)getApplication());
//		check = true;
//    	if(globalID.un_stop)return;
//    	else{
//    		globalID.setCurrent_code(currIndex);
//    		globalID.create_notification("后台接收数据", "后台运行", "船客户端", false, false, false);
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.clear();
//	    	this.finish();
//    	}
//    	Log.v("main","stop");
//    }
//    
//    @Override
//    public void onDestroy(){
//    	super.onDestroy();
//    	Log.v("main","onDestroy");
//    }
//    
//    /**
//	 * 初始化头标	 */
//	private void InitTextView() {
//		btn_main_bus.setOnClickListener(new MyOnClickListener(0));
//		btn_main_wea.setOnClickListener(new MyOnClickListener(1));
//		btn_main_msg.setOnClickListener(new MyOnClickListener(2));
//		btn_main_img.setOnClickListener(new MyOnClickListener(3));
//	}
//    
//    /** 初始化ViewPager*/
//    private void InitViewPager() {
//		mPager = (ViewPager) findViewById(R.id.vPager);
//		listViews = new ArrayList<View>();
//		final GlobalID globalID = ((GlobalID)getApplication());
//		globalID.mpAdapter = new MyPagerAdapter(listViews);
//		Intent intent = new Intent(MainActivity.this, bussiness_message.class);
//		Intent intent1 = new Intent(MainActivity.this, weather.class);
//		Intent intent2 = new Intent(MainActivity.this, Text_information.class);
//		Intent intent3 = new Intent(MainActivity.this, image_information.class);
//		listViews.add(getView("bus", intent));
//		listViews.add(getView("wea", intent1));
//		listViews.add(getView("tex", intent2));
//		listViews.add(getView("img", intent3));
//		mPager.setAdapter(globalID.mpAdapter);
////		mPager.setCurrentItem(currIndex);
//		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
//	}
//    
//    /**
//	 * 初始化动画 */
//	private void InitImageView() {
//		cursor = (ImageView) findViewById(R.id.cursor);
//		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
//				.getWidth();//获取图片宽度
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenW = dm.widthPixels;// 获取分辨率宽度	
//		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
//		Matrix matrix = new Matrix();
//		matrix.postTranslate(offset, 0);
//		cursor.setImageMatrix(matrix);// 设置动画初始位置
//	}
//	
//	/**
//	 * ViewPager适配器	 */
//	public class MyPagerAdapter extends PagerAdapter {
//		public List<View> mListViews;
//
//		public MyPagerAdapter(List<View> mListViews) {
//			this.mListViews = mListViews;
//		}
//
//		@Override
//		public void destroyItem(View arg0, int arg1, Object arg2) {
//			((ViewPager) arg0).removeView(mListViews.get(arg1));
//		}
//
//		@Override
//		public void finishUpdate(View arg0) {
//		}
//
//		@Override
//		public int getCount() {
//			return mListViews.size();
//		}
//
//		@Override
//		public Object instantiateItem(View arg0, int arg1) {
//			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
//			return mListViews.get(arg1);
//		}
//
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			return arg0 == (arg1);
//		}
//
//		@Override
//		public void restoreState(Parcelable arg0, ClassLoader arg1) {
//		}
//
//		@Override
//		public Parcelable saveState() {
//			return null;
//		}
//
//		@Override
//		public void startUpdate(View arg0) {
//		}
//	}
//	/**
//	 * 头标点击监听
//	 */
//	public class MyOnClickListener implements View.OnClickListener {
//		private int index = 0;
//
//		public MyOnClickListener(int i) {
//			index = i;
//		}
//
//		@Override
//		public void onClick(View v) {
//			mPager.setCurrentItem(index);
//		}
//	};
//	
//	/**
//	 * 页卡切换监听
//	 */
//	public class MyOnPageChangeListener implements OnPageChangeListener {
//
////		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
////		int two = one * 2;// 页卡1 -> 页卡3 偏移量
//		@Override
//		public void onPageSelected(int arg0) {
////			Animation animation = null;
//			switch (arg0) {
//			case 0:
//				btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_down);
//				break;
//			case 1:
//				btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_down);
//				break;
//			case 2:
//				btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_down);
//				break;
//			case 3:
//				btn_main_img.setBackgroundResource(R.drawable.main_img_button_down);
//				break;
//			}
//			
//			switch(currIndex){
//			case 0:
//				btn_main_bus.setBackgroundResource(R.drawable.main_bus_button_up);
//				break;
//			case 1:
//				btn_main_wea.setBackgroundResource(R.drawable.main_wea_button_up);
//				break;
//			case 2:
//				btn_main_msg.setBackgroundResource(R.drawable.main_msg_button_up);
//				break;
//			case 3:
//				btn_main_img.setBackgroundResource(R.drawable.main_img_button_up);
//				break;
//			}
//			
//			currIndex = arg0;
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//			globalID.setCurrent_code(currIndex);
//			Log.v("main","currIndex = "+globalID.getCurrent_code());
//			tabHost.setCurrentTab(currIndex);
////			animation.setFillAfter(true);// True:鍥剧墖鍋滃湪鍔ㄧ敾缁撴潫浣嶇疆
////			animation.setDuration(300);
////			cursor.startAnimation(animation);
//			Activity currentActivity = getCurrentActivity();
//
//			if (currentActivity instanceof bussiness_message) {
//				Log.v("main","here");
//				((bussiness_message) currentActivity).onResume();
//				}
//			if (currentActivity instanceof weather) {
//				Log.v("main","here0");
//				((weather) currentActivity).onResume();
//				}
//			if (currentActivity instanceof Text_information) {
//				Log.v("main","here1");
//		           ((Text_information) currentActivity).onResume();
//		           }
//			if (currentActivity instanceof image_information) {
//				Log.v("main","here2");
//		           ((image_information) currentActivity).onResume();
//		           }
//		}
//
//		@Override
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//		}
//
//		@Override
//		public void onPageScrollStateChanged(int arg0) {
//		}
//	}
//	
//	private View getView(String id,Intent intent)
//	{
//		return manager.startActivity(id, intent).getDecorView();
//	}
//}

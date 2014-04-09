package com.example.ship2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.shop_util.LogHelper;

import android.R.drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class NewMainActivity extends Activity{
	
	//测试推送图标
	private static boolean flag = true;
	private static final int EMERGENCY_LEVEL = 11;
	//紧急联系船号，需要更改
	private static final int EMERGENCY_PERSON = 0;
	private boolean destroy = false;
	
	private static boolean log = false;
	private Context context = NewMainActivity.this;
	private String TAG = "NewMain";
	//推送图标
	private BadgeView badge0,badge1;
	
	//popup Window
//	private PopupWindow mpopupWindow;
	
	/************use for view pager***************/
	private ViewPager mPager;//页卡内容
	private List<View> listViews; //Tab页面列表
//	private ImageView cursor;//动画图片
	private int offset = 0;//动画图片偏移量
	private int currIndex = 0;//当前页卡编号
	private int bmpW;//动画图片宽度
	private LocalActivityManager manager = null;
	/************use for view pager***************/
	
    private Vibrator vibrator=null;
    private long exitTime = 0;
	
	//set calendar
	private Calendar calendar = Calendar.getInstance();
	
	//set menu ID
	private static final int ITEM1 = Menu.FIRST ;
	private static final int ITEM2 = Menu.FIRST+1 ;
	private static final int ITEM3 = Menu.FIRST+2 ;
	private static final int ITEM4 = Menu.FIRST+3 ;
	private static final int ITEM5 = Menu.FIRST+4 ;
	
	private Button sTime = null; 
	private Button eTime = null;
	private Button selectAll = null;
	
//	private static Button btn_main_setting;
	private static ImageButton btn_main_setting,btn_main_send;
	
	private static RadioButton rb_main_new,rb_main_talk;
	
	View.OnTouchListener gestureListener ;
	
//	int currentView = 0 ;
//	private static int maxTabIndex = 2 ;
	private boolean check = false;

	Message message = new Message();

	ProgressDialog pd = null;
	
	
	public Handler mhandler = new Handler(){
//		GlobalID globalID = ((GlobalID)getApplication());
		public void  handleMessage (Message msg){
			GlobalID globalID = (GlobalID)getApplication();
			switch (msg.what){
			case 0:
				shake();
//				btn_main_news.setBackgroundResource(R.drawable.main_bus_button_push);
				if(log)Log.v(TAG,"mhandler case 0");
//				toast("有新的新闻信息");
				badge0.setText(String.valueOf(globalID.getBandW_push_UnGet()));
				badge0.show();
				globalID.mpAdapter.notifyDataSetChanged();
//		        globalID.setBus_push(false);
				break;
//			case 1:
//				shake();
////				btn_main_text.setBackgroundResource(R.drawable.main_wea_button_push);
////				if(log)Log.v(TAG,"mhandler case 1");
////		        globalID.setWea_push(false);
//				toast("有新的天气信息");
//				break;
			case 1:
				shake();
//				btn_main_text.setBackgroundResource(R.drawable.main_msg_button_push);
//				if(log)Log.v(TAG,"mhandler case 2");
//		        globalID.setMsg_push(false);
//				toast("有新的聊天信息");
				badge1.setText("新");
				badge1.show();
				globalID.mpAdapter.notifyDataSetChanged();
				break;
			case 2:
				shake();
//				btn_main_picture.setBackgroundResource(R.drawable.main_img_button_push);
//				if(log)Log.v(TAG,"mhandler case 3");
//		        globalID.setImg_push(false);
				toast("有新的图片信息");
				break;
			case 4:
//				if(log)Log.v(TAG,"mhandler case 4");
				if(pd == null)pd = ProgressDialog.show(context, "刷新", "正在努力刷新…");
				break;
			case 5:
				shake();
//				if(log)Log.v(TAG,"mhandler case 5");
				if(pd != null)pd.dismiss();
				break;
			case 6:
				badge1.hide();
				break;
			case 7:
				Activity mActivity = manager.getCurrentActivity();
				if(mActivity != null
						&& mActivity instanceof MsgTalkPre)
				((MsgTalkPre)mActivity).mAdapter.notifyDataSetChanged();
				break;
			default:
				if(log)Log.v(TAG,"mhandler case default");
				break;
				}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final GlobalID globalID = ((GlobalID)getApplication());
		if(null != savedInstanceState){
			currIndex = savedInstanceState.getInt("currIndex"); 
			if(log)Log.e(TAG, "onCreate get the savedInstanceState + currIndex = "+currIndex);
			globalID.start();
//	    	globalID.setBus_change(savedInstanceState.getBoolean("Bus_change"));
//	    	globalID.setBusDataArrays(savedInstanceState.getString("BusDataArrays"));
//	    	
//	    	globalID.setAll(savedInstanceState.getString("All"));
//	    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
//			globalID.start(context);
			} 
		//no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_main);
		if(log)Log.v(TAG,"onCreate");
		

		if(manager == null)manager = new LocalActivityManager(this, true);
		if(savedInstanceState == null)LogHelper.trace(TAG,"savedInstanceState.isEmpty()");
		manager.dispatchCreate(savedInstanceState);
		//set button
		btn_main_setting = (ImageButton)findViewById(R.id.title_bar_btn);
		btn_main_send = (ImageButton)findViewById(R.id.title_bar_back);
		
		rb_main_new = (RadioButton)findViewById(R.id.rb_new);
		rb_main_talk = (RadioButton)findViewById(R.id.rb_talk);

		//for shake
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		
		/***********用来搞推送的************/
		badge0 = new BadgeView(context, rb_main_new);
		badge0.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
//		badge0.show();
		
		badge1 = new BadgeView(context, rb_main_talk);
		badge1.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
		badge1.setText("新");
//		badge1.show();
    	
//		badge1 = new BadgeView(this, btn_main_text);
//    	badge1.setBackgroundResource(R.drawable.badge_ifaux);
//    	badge1.setHeight(2);
//    	badge1.setWidth(2);
//		btn_main_picture = (Button)findViewById(R.id.btn_main_picture);
//		badge2 = new BadgeView(this, btn_main_picture);
//    	badge2.setText("");
//    	badge2.setBackgroundColor(Color.RED);
//    	badge2.setTextSize(8);
		
		
		btn_main_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				btn_main_setting.setBackgroundResource(R.drawable.main_more_up);
//				openOptionsMenu();

	            globalID.un_stop = true;
				Intent intent = new Intent(context,SettingActivity.class);
                startActivity(intent);
			}
		});
		
		btn_main_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				btn_main_setting.setBackgroundResource(R.drawable.main_more_up);
//				openOptionsMenu();

	            globalID.un_stop = true;
				Intent intent = new Intent(context,SendActivity.class);
				Bundle data = new Bundle();
				data.putInt("Send_Id", EMERGENCY_PERSON);
				data.putInt("MSG_LEVEL", EMERGENCY_LEVEL);
				intent.putExtras(data);
                startActivity(intent);
			}
		});
		
		InitImageView();
		InitBottonListener();
		InitViewPager();
		
		globalID.PD(context, "连接", "正在获取数据…");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		//set title
		if(log)Log.v(TAG,"onResume");
		
		GlobalID globalID = ((GlobalID)getApplication());
		globalID.cancel_notification();
		
		if(globalID.getCurrent_code() != -1)currIndex = globalID.getCurrent_code();
		else{
			globalID.setCurrent_code(currIndex);
			if(log)Log.v(TAG,"onResume currendIndex = "+currIndex);
		}
		globalID.un_stop = false;
		check = false;
		Activity mActivity = manager.getCurrentActivity();
		
		if(globalID.getBandW_push_UnGet()>0){
			
			//当前界面为BandWList时，自动刷新
			if(mActivity != null
					&& mActivity instanceof BandWList
					){
				((BandWList)mActivity).refresh();
				globalID.setBandW_push_UnGet(0);
				badge0.hide();
			}
			else {
				badge0.setText(String.valueOf(globalID.getBandW_push_UnGet()));
				badge0.show();
			}
		}
		
		if(globalID.getTalk_push_UnGet()>0){
			
			if(mActivity != null
					&& mActivity instanceof MsgTalkPre
					){
				((MsgTalkPre)mActivity).mDataArrays.get(0).setUnget(globalID.getTalk_push_UnGet());
				((MsgTalkPre)mActivity).mAdapter.notifyDataSetChanged();
			}
			
			badge1.show();											
		}
		else badge1.hide();
		
		switch(currIndex){
		case 0:{
			rb_main_new.setChecked(true);
			rb_main_talk.setChecked(false);
		}
		break;
		case 1:{
			rb_main_new.setChecked(false);
			rb_main_talk.setChecked(true);
		}
		break;
		}
		
		mPager.setCurrentItem(currIndex);
		//start change button Thread
		if(log)Log.v(TAG,"check_push running...");
		if(globalID.isCheck_push()){
			if(log)Log.v(TAG,"check_push running...");
			globalID.setCheck_push(false);
			Thread check_push = new Thread(){
				public void run(){
					GlobalID globalID = ((GlobalID)getApplication());
							int time = 5;
							while(true){
//								time += time;
								
								//监控睡眠时间
								time += 20;
								if(time <= 0)time = 1;
								if(time > 1200)time = 1200;
								try {
									Thread.sleep(globalID.getM_rate()*time);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									continue;
								}
								
								if(log)Log.v(TAG,"check_push running...");
								//自己检测，跳出
								if(check){
									if(log)Log.v(TAG,"check_push break");
									globalID.setCheck_push(true);
									return;
								}
								//如果已经在后台，跳出
								if(globalID.notification != null){
									if(log)Log.v(TAG,"notification break");
									globalID.setCheck_push(true);
									return;
								}
								
								Activity mActivity = manager.getCurrentActivity();
								
								if(globalID.isBandW_push())
								{
									globalID.setBandW_push(false);
									time = time / 2;
									//当前界面为BandWList时，自动刷新
									if(mActivity != null
											&& mActivity instanceof BandWList){
										((BandWList)mActivity).refresh();
									}
									else globalID.setBandW_push_UnGet((globalID.getBandW_push_UnGet())+1);
								}
								
								if(globalID.isTalk_push())
								{
									globalID.setTalk_push(false);
									time = time / 2;
									globalID.setTalk_push_UnGet((globalID.getTalk_push_UnGet())+1);
								}
								
								if(globalID.getBandW_push_UnGet()>0){
									
									//当前界面为BandWList时，自动刷新
									if(mActivity != null
											&& mActivity instanceof BandWList
											){
										((BandWList)mActivity).refresh();
									}
									else {
										Message msg = new Message();
										msg.what = 0;
										mhandler.sendMessage(msg);
									}
								}
								
								if(globalID.getTalk_push_UnGet()>0){
									
									if(mActivity != null
											&& mActivity instanceof MsgTalkPre){
										((MsgTalkPre)mActivity).mDataArrays.get(0).setUnget(globalID.getTalk_push_UnGet());
										
										Message msg = new Message();
										msg.what = 7;
										mhandler.sendMessage(msg);
									}
									else{
										Message msg = new Message();
										msg.what = 1;
										mhandler.sendMessage(msg);										
									}
								}
								else {
									Message msg = new Message();
									msg.what = 6;
									mhandler.sendMessage(msg);
								}
							}
						}
					};
					check_push.start();
				}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		
//		showPopMenu();
//		return false;
		
		//add menu item
//		menu.add(0,ITEM1,0,"获取更多信息");
//		menu.add(0,ITEM2,0,"北斗信息");
//		menu.add(0,ITEM3,0,"按时间搜索");
		menu.add(0,ITEM5,0,"设置");
		menu.add(0,ITEM4,0,"注销登录");
		if(log)Log.v(TAG,"createMenu");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		
		final GlobalID globalID = ((GlobalID)getApplication());
		
		switch(item.getItemId()){
		//菜单项1被选择
//		case ITEM1 :
//			AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//            builder1.setTitle("查询最近N条记录");
//            final String[] Lines = {"5条","10条","15条","20条","所有记录"};
//            int k = globalID.getLine()/5-1;
//            if(k==19)k = 4;
//			builder1.setSingleChoiceItems(Lines,k, new DialogInterface.OnClickListener() {
//                @Override
//				public void onClick(DialogInterface dialog, int which) {
//                	if(globalID.getLine() != (which+1)*5){
//                    	globalID.setLine((which+1)*5);
//                    	if(which == 4)globalID.setLine(100);
////                    	if(Integer.parseInt(j) == 0)
////                    		tabHost.setCurrentTab(1);
////                		else 
////                			tabHost.setCurrentTab(Integer.parseInt(j)-1);
////            			tabHost.setCurrentTab(Integer.parseInt(j));
//            			
//            			globalID.time_change();
////            			Activity currentActivity = getCurrentActivity();
////            			if (currentActivity instanceof bussiness_message) {
////            				((bussiness_message) currentActivity).onResume();
////            				}
////            			if (currentActivity instanceof Text_information) {
////     			           ((Text_information) currentActivity).onResume();
////     			           }
////            			if (currentActivity instanceof image_information) {
////     			           ((image_information) currentActivity).onResume();
////     			           }
//                	}
//        			dialog.dismiss();
//                }
//            });
//			builder1.setIcon(android.R.drawable.ic_dialog_info);
//            builder1.create().show();
//			break;
//		case ITEM2 :
//			globalID.un_stop = true;
//			Intent intent1 = new Intent(context,GeoPre.class);
//            startActivity(intent1);
//			break;
//		case ITEM3 :
//			
//			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            LayoutInflater factory = LayoutInflater.from(context);
//            final View textEntryView = factory.inflate(R.layout.inquire, null);
////                builder.setIcon(R.drawable.ic_launcher);
//                builder.setTitle("选择查询范围");
//                builder.setView(textEntryView);
//                
//                selectAll = (Button) textEntryView.findViewById(R.id.btn_inquire_selectAll);
//                
//                sTime = (Button) textEntryView.findViewById(R.id.btn_inquire_startTime); 
//                eTime = (Button) textEntryView.findViewById(R.id.btn_inquire_endTime);
//                
//                sTime.setText(globalID.getStartDate());
//                eTime.setText(globalID.getEndDate());
//                
//                selectAll.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						sTime.setText("2013-01-01");
//						Calendar eDate = Calendar.getInstance();
//						eTime.setText(eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE));
//					}
//				});
//                
//                sTime.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						showDialog(0);
//					}
//				});
//                
//                eTime.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						showDialog(1);
//					}
//				});
//                
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//					public void onClick(DialogInterface dialog, int whichButton) {
//                    		                    
////                    	if(（sTime.getText().toString())>(eTime.getText().toString())){
////                    		new AlertDialog.Builder(context).  
////            				setTitle("错误").setMessage("输入日期有误").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
////            					public void onClick(DialogInterface dialog, int which) {
////            						dialog.dismiss();
////            						}  
////            					}).show();
////                    	}
////                    	TV_sign.setText(et_asSign.getText().toString());
////                    	else{
//                    		globalID.setStartDate(sTime.getText().toString());
//                    		globalID.setEndDate(eTime.getText().toString());
//                    		dialog.dismiss();
//            			
//                    		//refresh listview by change currentTab
////                    		Log.v("j","j: "+j);
////                        	if(Integer.parseInt(j) == 0)
////                        		tabHost.setCurrentTab(1);
////                    		else 
////                    			tabHost.setCurrentTab(Integer.parseInt(j)-1);
////                			tabHost.setCurrentTab(Integer.parseInt(j));
//
//                			globalID.time_change();
////                			Activity currentActivity = getCurrentActivity();
////                			if (currentActivity instanceof bussiness_message) {
////                				((bussiness_message) currentActivity).onResume();
////                				}
////                			if (currentActivity instanceof Text_information) {
////         			           ((Text_information) currentActivity).onResume();
////         			           }
////                			if (currentActivity instanceof image_information) {
////         			           ((image_information) currentActivity).onResume();
////         			           }
//                			
//            				globalID.mpAdapter.notifyDataSetChanged();
////                    		}
//                    	}
//                    });
//
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//					public void onClick(DialogInterface dialog, int whichButton) {
//                    	dialog.dismiss();
//                    }
//                });
//                builder.create().show();
//			break;
		//菜单项2被选择
		case ITEM4 :
//			if(globalID.getNow_ark().equals("-1")){
//				new AlertDialog.Builder(context).  
//				setTitle("错误").setMessage("对不起，您权限被限制").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						}  
//					}).show();
//			}
//			
//			else{
//				final ArrayList<String> Items = new ArrayList<String>();
//				Items.addAll(globalID.getAll());
//				
//				Log.v("ark","ark:"+Items.size());
//				AlertDialog.Builder builder11 = new AlertDialog.Builder(context);
//	            builder11.setTitle("船号选择框");
//	            final String[] ArkItems = new String[Items.size()];
//	            final String[] ArkNames = {"粤台118881","粤台12828 ","粤茂81888 "};
//	            ArkItems[0] = Items.get(0);
//	            for(int i  = 0;i<Items.size();i++){
//	            	ArkItems[i] = Items.get(i)+":  "+ ArkNames[Integer.parseInt(Items.get(i))-1];
//	            }
//				builder11.setSingleChoiceItems(ArkItems,Integer.parseInt(globalID.getNow_ark())-1, new DialogInterface.OnClickListener() {
//	                public void onClick(DialogInterface dialog, int which) {
//	                	globalID.setNow_ark(Items.get(which));
//	        			tv_main_Title.setText("用户:"+globalID.getID()+"船号:("+globalID.getNow_ark()+")"+ArkNames[Integer.parseInt(globalID.getNow_ark())-1]);
//	                	if(Integer.parseInt(j) == 0)
//	                		tabHost.setCurrentTab(1);
//	            		else 
//	            			tabHost.setCurrentTab(Integer.parseInt(j)-1);
//	        			tabHost.setCurrentTab(Integer.parseInt(j));
//	        			dialog.dismiss();
//	                }
//	            });
//				builder11.setIcon(android.R.drawable.ic_dialog_info);
//	            builder11.create().show();
//			}
			check = true;
            globalID.un_stop = true;
			globalID.setCurrent_code(-2);
			Intent intent = new Intent(context,loginActivity.class);
            startActivity(intent);
            globalID.clear();
            globalID.closeSocket();
//            globalID.time_change();
//            globalID.BusDataArrays.clear();
//            globalID.BusDeleteArrays.clear();
//            globalID.MsgDataArrays.clear();
//            globalID.MsgDeleteArrays.clear();
//            globalID.ImgDataArrays.clear();
//            globalID.ImgDeleteArrays.clear();
            
            destroy = true;
            this.finish();
			break;
		case ITEM5 :
            globalID.un_stop = true;
			Intent intent4 = new Intent(context,SettingActivity.class);
            startActivity(intent4);
			break;
		}
		return true;
	}	

//	/***************double touch for finish**************************/
//    private long exitTime = 0;
//
//	@SuppressLint("ShowToast")
//	@Override
//	public void finish(){
//		GlobalID globalID = ((GlobalID)getApplication());
//		if((System.currentTimeMillis()-exitTime) > 2000){
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.toast = Toast.makeText(context, "再按一次退出程序", 2000);
//			globalID.toast.show();
//			exitTime = System.currentTimeMillis();
//			return;
//			}
//		else {
//			globalID.clear();
////			finish();
//			System.exit(0);
//            }
//	}
//	/***************double touch for finish**************************/
	
	/******************creat dialog for time selecting*******************************/
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = null;
    	switch (id) {
    	case 0:
    		dialog = new DatePickerDialog(
            this,R.style.DialogStyle,
            new DatePickerDialog.OnDateSetListener() {
                @Override
				public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                	if(month<9){
                    	if(dayOfMonth<10){
                        	sTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else sTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                	}
                	else {
                		if(dayOfMonth<10){
                        	sTime.setText(year + "-" + (month+1) + "-0" + dayOfMonth);
                    	}
                		else sTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                	}
                }
            }, 
            calendar.get(Calendar.YEAR), // 传入年份
            calendar.get(Calendar.MONTH), // 传入月份
            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
    		dialog.setTitle("选择开始日期");
        break;
    case 1:
        dialog = new DatePickerDialog(
            this,R.style.DialogStyle,
            new DatePickerDialog.OnDateSetListener() {
                @Override
				public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                	if(month<9){
                    	if(dayOfMonth<10){
                        	eTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else eTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                	}
                	else eTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                }
            },
            calendar.get(Calendar.YEAR), // 传入年份
            calendar.get(Calendar.MONTH), // 传入月份
            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.setTitle("选择结束日期");
        break;
        }
    	return dialog;
    }
    /******************creat dialog for time selecting*******************************/
    
    
    /***************************onConfigurationChanged************************************/
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.i("--Main--", "onConfigurationChanged");
//		GlobalID globalID = ((GlobalID)getApplication());
//        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//        	new AlertDialog.Builder(context).  
//			setTitle(TAG).setMessage("横屏").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					}  
//				}).show();
//        }else{
//        	new AlertDialog.Builder(context).  
//			setTitle(TAG).setMessage("竖屏").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					}  
//				}).show();
//        }
//    }
    /***************************onConfigurationChanged************************************/
    
    void toast(String msg){
		GlobalID globalID = ((GlobalID)getApplication());
		if(globalID.toast != null)globalID.toast.cancel();
		globalID.toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		globalID.toast.show();
    }
    
    void shake(){
		GlobalID globalID = ((GlobalID)getApplication());
		if(globalID.isShake()){
			if((System.currentTimeMillis()-exitTime) > 2000){
				vibrator.vibrate(1000);
				exitTime = System.currentTimeMillis();
            }
		}
    }
    @Override
    public void onPause(){
    	super.onPause();
    	if(log)Log.v(TAG,"onPause");
    	GlobalID globalID = ((GlobalID)getApplication());
		check = true;
    	if(globalID.un_stop)return;
    	else{
    		globalID.setCurrent_code(currIndex);
    		globalID.create_notification("后台接收数据", "后台运行", "船客户端", false, false, false
    				,NewMainActivity.class.getName());
			if(globalID.toast != null)globalID.toast.cancel();
			globalID.clear();
//	    	context.finish();
    	}
    }
    @Override
    public void onStop(){
    	super.onStop();
    	if(log)Log.v(TAG,"stop");
//		GlobalID globalID = ((GlobalID)getApplication());
//		check = true;
//    	if(globalID.un_stop)return;
//    	else{
//    		globalID.setCurrent_code(currIndex);
//    		globalID.create_notification("后台接收数据", "后台运行", "船客户端", false, false, false,MainActivity.class.getName());
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.clear();
////	    	context.finish();
//    	}
    }    

    @Override
    public void onBackPressed() { 
    	//实现Home键效果 
    	//super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了 
    	Intent intent= new Intent(Intent.ACTION_MAIN); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
    	intent.addCategory(Intent.CATEGORY_HOME); 
    	startActivity(intent);  
    	if(log)Log.v(TAG,"onBackPressed()");
    	}
    
    @Override
    public void onDestroy(){
    	if(log)Log.v(TAG,"onDestroy");    	
    	super.onDestroy();
    }
    @Override
    public void onRestart(){
    	super.onRestart();
    	if(log)Log.v(TAG,"onRestart");
    	onResume();
    }
    
    @Override
    public void finish(){
    	Log
    	.v(TAG,"finish()");
    	//好有可能会出问题
    	if(!destroy)return;
    	super.finish();
    }
    
    /*******************View Pager********************/
    /**
	 * 初始化头标	 */
	private void InitBottonListener() {
//		btn_main_news.setOnClickListener(new MyOnClickListener(0));
//		btn_main_text.setOnClickListener(new MyOnClickListener(1));
//		btn_main_picture.setOnClickListener(new MyOnClickListener(2));
		
		rb_main_new.setOnClickListener(new MyOnClickListener(0));
		rb_main_talk.setOnClickListener(new MyOnClickListener(1));
	}
    
    /** 初始化ViewPager*/
    private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		final GlobalID globalID = ((GlobalID)getApplication());
		globalID.mpAdapter = new MyPagerAdapter(listViews);
		Intent intent = new Intent(context, BandWList.class);
		Intent intent1 = new Intent(context, MsgTalkPre.class);
		listViews.add(getView("bnw", intent));
		listViews.add(getView("talk", intent1));
		mPager.setAdapter(globalID.mpAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
    
    /**
	 * 初始化动画 */
	private void InitImageView() {
//		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();//获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度	
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
//		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	/**
	 * ViewPager适配器	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;
		private int mChildCount = 0;

		public MyPagerAdapter(List<View> mListViews){
			this.mListViews = mListViews;
		}

		@Override
	     public void notifyDataSetChanged(){
			mChildCount = getCount();
//			Log.v("MyPagerAdapter","notifyDataSetChanged "+mChildCount);
			super.notifyDataSetChanged();
	     }

	     @Override
	     public int getItemPosition(Object object){
	           if ( mChildCount > 0) {
//	  	    	 Log.v("MyPagerAdapter","getItemPosition > 0");
	           mChildCount --;
	           return POSITION_NONE;
	           }
//		    	 Log.v("MyPagerAdapter","getItemPosition");
	           return super.getItemPosition(object);
	     }

	     
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	
	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
	
	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		
		@Override
		public void onPageSelected(int arg0) {
			final GlobalID globalID = ((GlobalID)getApplication());
			currIndex = arg0;
			
			switch (currIndex) {
			case 0:
				rb_main_new.setChecked(true);
				rb_main_talk.setChecked(false);

				globalID.setBandW_push_UnGet(0);
				badge0.hide();
				
				Activity mActivity = manager.getCurrentActivity();
				
				if(mActivity != null && mActivity instanceof BandWList){
					if(globalID.getBandW_push_UnGet() > 0){
						((BandWList)mActivity).refresh();
					}
				}
				break;
			case 1:
				rb_main_new.setChecked(false);
				rb_main_talk.setChecked(true);

				if(globalID.getTalk_push_UnGet() == 0) badge1.hide();
				
				Activity mActivity1 = manager.getCurrentActivity();
				if(mActivity1 != null && mActivity1 instanceof MsgTalkPre){
					
					//以后应该修改为当整个SendPre数组里没有更新时，才取消
				}
				break;
			}

			globalID.setCurrent_code(currIndex);
			if(log)Log.v(TAG,"currIndex = "+globalID.getCurrent_code());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	private View getView(String id,Intent intent)
	{
		return manager.startActivity(id, intent).getDecorView();
	}
    /*******************View Pager********************/

//    /*******************Pop Menu********************/
//	private void showPopMenu() {
//		View view = View.inflate(getApplicationContext(), R.layout.share_popup_menu, null);
//		RelativeLayout rl_weixin = (RelativeLayout) view.findViewById(R.id.rl_weixin);
//		RelativeLayout rl_weibo = (RelativeLayout) view.findViewById(R.id.rl_weibo);
//		RelativeLayout rl_duanxin = (RelativeLayout) view.findViewById(R.id.rl_duanxin);
//		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
//
//		rl_weixin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		rl_weibo.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		rl_duanxin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		bt_cancle.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		
//		view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
//		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
//		ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in));
//		
//		if(mpopupWindow==null){
//			mpopupWindow = new PopupWindow(this);
//			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
//			mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
//			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//			mpopupWindow.setFocusable(true);
//			mpopupWindow.setOutsideTouchable(true);
//		}
//		
//		mpopupWindow.setContentView(view);
//		mpopupWindow.showAtLocation(rb_main_new, Gravity.BOTTOM, 0, 0);
//		mpopupWindow.update();
//	}
//    /*******************Pop Menu********************/
	@Override 
    public void onSaveInstanceState(Bundle savedInstanceState) {  
    	// Save away the original text, so we still have it if the activity   
    	// needs to be killed while paused.
		GlobalID globalID = ((GlobalID)getApplication());
		globalID.clear();
//    	savedInstanceState.putInt("currIndex", currIndex);
//    	
//    	savedInstanceState.putBoolean("Bus_change", globalID.isBus_change());
//    	savedInstanceState.putString("BusDataArrays", globalID.Bus2String());
//    	
//    	savedInstanceState.putString("All", globalID.Ark_line2String());
//    	savedInstanceState.putInt("Now_ark", globalID.getNow_ark());
//    	
    	super.onSaveInstanceState(savedInstanceState);  
    	Log.e(TAG, "onSaveInstanceState");  
//    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
    	}  
    
    @Override  
    public void onRestoreInstanceState(Bundle savedInstanceState) {  
    	super.onRestoreInstanceState(savedInstanceState);
//		GlobalID globalID = ((GlobalID)getApplication());
//    	currIndex = savedInstanceState.getInt("currIndex");
//    	globalID.setAll(savedInstanceState.getString("All"));
//    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
//    	Log.e(TAG, "onRestoreInstanceState + currIndex = "+ currIndex);
//    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
    	}  
}

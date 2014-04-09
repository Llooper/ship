package com.example.ship2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.platform.comapi.map.t;
import com.example.ship2.ChatListView.OnPullDownListener;
import com.example.ship_Adapter.TalkAdapter;
import com.example.ship_Entity.BandWEntity;
import com.example.ship_Entity.TalkEntity;
import com.example.shop_util.FuntionUtil;
import com.example.shop_util.LogHelper;
import com.example.shop_util.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;


public class MsgTalk extends ListActivity{

	private String TAG = "MsgTalk";
	private final static boolean log = false;

	private boolean check = false;
	private static final String msg_level = "41";//发送等级 41表示正常
//	private int ark_id;
	private Context context = MsgTalk.this;
    private Vibrator vibrator=null;
    private EditText et_sendmessage;
    private Button chatting_btn_send;
    private ImageButton title_bar_back;
    private String sendmessage;
    private Boolean isResume = false;
    private TextView title_bar_title;
	
    /*************Picture***************/
	private String[] items = new String[] {
			"选择本地图片" , "拍照"
	};
	
	private static final String IMAGE_FILE_NAME = "/faceImage.jpg";
	
	private static final int IMAGE_REQUEST_CODE = 0 ;
	private static final int CAMERA_REQUEST_CODE = 1 ;
	private static final int RESULT_REQUEST_CODE = 2 ;
	/**************Picture**************/
    
	InetAddress serverAddr;
	private ChatListView mListView;
	private TalkAdapter mAdapter;

	private String currentStart = "2013-01-01 00:00:00";
	private String currentEnd = "3000-12-31 23:59:59";
	private long exitTime = 0;
	private int currentLine = 5;
	private int updateLines = 0;
	
//	private List<TalkEntity> globalID.TalkList = new ArrayList<TalkEntity>();
	
	private ImageButton chatting_picture_btn;
	
	ProgressDialog pd = null;

//	Message msg = new Message();
	final Handler add_handler = new Handler(){
	@Override
	public void handleMessage (Message msg){
		GlobalID globalID = ((GlobalID)getApplication());
		globalID.TalkList.add((TalkEntity) msg.obj);
		mAdapter.notifyDataSetChanged();
		}
	};
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			GlobalID globalID = ((GlobalID)getApplication());
			if(pd!=null)pd.dismiss();
			if(log)Log.v(TAG,"here");
			for(int i = globalID.TalkList.size()-1 ; i > -1 ; i-- ){
				if(globalID.TalkList.get(i).getMsg_id().equals("")
						&& globalID.TalkList.get(i).getState() == 1){
					globalID.TalkList.remove(i);
					if(log)Log.v(TAG,"remove");
					break;
				}
			}
			mAdapter.notifyDataSetChanged();
			switch (msg.what){
			case 0:
				toast("查询错误");
				break;
			case 1:
				if(updateLines!=0){
					toast("该时段最近"+ updateLines +"条信息");
					break;
				}
			case 2:
				if(updateLines!=0){
					toast("刷新"+ updateLines +"条信息");
					break;
				}
			case 3:
				if(updateLines!=0){
					toast("加载"+ updateLines +"条历史信息");
					break;
				}
			case 4:
//				toast("没有找到文字信息");
				break;
			case 5:
				mAdapter.notifyDataSetChanged();
				break;
			case 6:
				((ChatListView) getListView()).onRefreshComplete();
				((ChatListView) getListView()).onMoreComplete();
				break;
				}
		}
	};
	
	final Handler Fail_Handler = new Handler(){
		@Override
		public void  handleMessage (final Message msg){
//			for(int i = globalID.TalkList.size()-1;i>0;i--){
//				if(globalID.TalkList.get(i).getDetail().equals(msg.obj))
//					msg.what = i;
//			}
			if(log)LogHelper.trace(String.valueOf(msg.arg1));
			final GlobalID globalID = ((GlobalID)getApplication());
			final TalkEntity entity = globalID.TalkList.get(msg.arg1);
			AlertDialog.Builder builder = new Builder(MsgTalk.this);
			builder.setTitle("重发");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if(log)Log.v(TAG,"msg.arg1: "+msg.arg1);
							Resend(entity);
//							Resend(msg.arg1);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setMessage("确认重发该条消息");
			builder.create().show();
		}
	};
	final Handler ll_Handler = new Handler(){
		@Override
		public void  handleMessage (final Message msg){
//			for(int i = globalID.TalkList.size()-1;i>0;i--){
//				if(globalID.TalkList.get(i).getDetail().equals(msg.obj))
//					msg.arg1 = i;
//			}
			LogHelper.trace(String.valueOf(msg.arg1) + " " + String.valueOf(msg.arg2));
			final GlobalID globalID = ((GlobalID)getApplication());
			
			final TalkEntity entity = globalID.TalkList.get(msg.arg1);
			if(msg.arg2 == StringUtil.FAIL){

				AlertDialog.Builder builder = new Builder(MsgTalk.this);
				builder.setTitle("重发");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								if(log)Log.v(TAG,"msg.arg1: "+msg.arg1);
								Resend(entity);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage("确认重发该条消息");
				builder.create().show();
				return;
			}
			if(entity.getIsMsg()){
				
				if(msg.arg2 == StringUtil.CLICK){
//					AlertDialog.Builder builder = new Builder(MsgTalk.this);
//					builder.setTitle("文字");
//					builder.setPositiveButton("复制",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									GlobalID globalID = ((GlobalID)getApplication());
//							        ClipData textCd = ClipData.newPlainText("", globalID.TalkList.get(msg.arg1).getDetail());
//							        globalID.getClipboard().setPrimaryClip(textCd);
//									dialog.dismiss();
//									globalID.showCopyed(MsgTalk.this);
//									dialog.dismiss();
//								}
//							});
//					builder.create().show();
				}
				
				else if(msg.arg2 == StringUtil.LONG){

					AlertDialog.Builder builder = new Builder(MsgTalk.this);
//					builder.setTitle("文字");
					builder.setPositiveButton("复制",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									GlobalID globalID = (GlobalID)getApplication();
									String test = entity.getDetail();
									LogHelper.trace(test);
									// 判断API>=11
									if(Build.VERSION.SDK_INT >= 11){
								        ClipData textCd = ClipData.newPlainText("", test);
								        globalID.getClipboard().setPrimaryClip(textCd);
									}
									else {
										ClipboardManager clipboard ;
								        clipboard = (ClipboardManager)getSystemService(MsgTalk.CLIPBOARD_SERVICE);
								        clipboard.setText(test);
									}
									
									dialog.dismiss();
									globalID.showCopyed(MsgTalk.this);
									dialog.dismiss();
								}
							});
//					builder.setNegativeButton("删除",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									dialog.dismiss();
//								}
//							});
					builder.create().show();
				}
			}
			
			if(!entity.getIsMsg() && msg.arg2 == StringUtil.CLICK){
				isResume = true;
				Bundle bundle = new Bundle();
				
//				//set picture
//				Bitmap Image_picture = entity.getImage_picture();
//				if(Image_picture==null){
//					bundle.putByteArray("Image_picture", null);
//					}
//				else{
//					ByteArrayOutputStream image_picture = new ByteArrayOutputStream();
//					Image_picture.compress(Bitmap.CompressFormat.PNG, 100, image_picture);
//					bundle.putByteArray("Image_picture", image_picture.toByteArray());
//					
//				}
				
				bundle.putInt("i", msg.arg1);
				globalID.un_stop = true;
				Intent intent = new Intent(MsgTalk.this,ViewPagerActivity.class);
				//post data
				intent.putExtras(bundle);
	            startActivity(intent);
			}
		}
	};
	
	final Handler mhandler3 = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			final GlobalID globalID = ((GlobalID)getApplication());
			chatting_btn_send.setClickable(true);
			switch (msg.what){
			case -3:
				if(log)Log.v("mhandler3", "case -3");
				if(pd!=null)pd.dismiss();
				globalID.TalkList.get(msg.arg1).setState(2);
				new AlertDialog.Builder(context).
       		 	setTitle("错误").setMessage("发送失败，连接服务器失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case -2:
				if(log)Log.v("mhandler3", "case -2");
				if(pd!=null)pd.dismiss();
				globalID.TalkList.get(msg.arg1).setState(2);
				new AlertDialog.Builder(context).
       		 	setTitle("错误").setMessage("发送失败1").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case -1:
				if(log)Log.v("mhandler3", "case -1");
//				if(pd!=null)pd.dismiss();
//				new AlertDialog.Builder(context).
//				setTitle("错误").setMessage("发送失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//       			 @Override
//				public void onClick(DialogInterface dialog, int which) {
//       				 dialog.dismiss();
//       				 }
//       			 }).show();
				if(log)Log.v("mhandler3", "msg.arg1: "+ msg.arg1);
				globalID.TalkList.get(msg.arg1).setState(2);
				break;
			case 0:
				if(log)Log.v("mhandler3", "case 0");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(context).  
				setTitle("成功").setMessage("成功发送图片信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((Activity) context).finish();
						globalID.is_sended = true;
						}  
					}).show();
				break;
			case 1:
				if(log)Log.v("mhandler3", "case 1");
//				if(pd!=null)pd.dismiss();
//				new AlertDialog.Builder(context).  
//				setTitle("成功").setMessage("成功发送文字信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						((Activity) context).finish();
//						globalID.is_sended = true;
//						}  
//					}).show();
//				if(log)Log.v("mhandler3", "msg.arg1: "+ msg.arg1);
				globalID.TalkList.get(msg.arg1).setState(1);
				break;
			case 2:
				if(log)Log.v("mhandler3", "case 2");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(context).  
				setTitle("成功").setMessage("成功发送所有信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((Activity) context).finish();
						globalID.is_sended = true;
						}  
					}).show();
				break;
			}
			if(log)LogHelper.trace(TAG,"here?");
			mAdapter.notifyDataSetChanged();
		}
	};
		@Override 
		public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		final GlobalID globalID = ((GlobalID)getApplication());
		overridePendingTransition(R.anim.item_in, R.anim.list_out);
		if(null != savedInstanceState){
			final String decode = savedInstanceState.getString("TalkList");
	    	if(log)Log.e(TAG, "null != savedInstanceState TalkList = "+ decode);
	    	globalID.start();
//	    	globalID.start(context);
//	    	Thread decode_thread = new Thread(){
//				public void run(){
//					globalID.TalkJson(decode, true, globalID.TalkList);
//				}
//			};
//			decode_thread.start();
	    	this.finish();
			}
		else{
//			ark_id = Integer.valueOf(globalID.getAll().get(ark_id).getArk_id());
//			if(log)Log.v(TAG,"ark_id onCreate " + ark_id);
			GetDataTask talk = new GetDataTask();
			talk.startTime = currentStart;
			talk.endTime = currentEnd;
			talk.Case = 1;
			talk.Insert_Back = false;
//			talk.setMsg_id = "<> -1";
			talk.DESC = "DESC";
			isResume = true;
			pd = ProgressDialog.show(MsgTalk.this, "连接", "正在获取数据…");
			talk.execute();
		}
		
		//set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chatting_main);
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		title_bar_title = (TextView)findViewById(R.id.talk_title);
//		title_bar_title.setText("文字信息");
//		title_bar_title.requestFocus();
		
		initView();
		et_sendmessage = (EditText)findViewById(R.id.et_sendmessage);
		
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		et_sendmessage.setWidth(display.getWidth());
		Log.v(TAG,"display.getWidth() = "+display.getWidth());
		et_sendmessage.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub

				//do something when press ENTER
				if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
					Log.v("onEditorAction","KeyEvent.KEYCODE_ENTER");
					
					SendMSG_Pre();					
					return true;
				}
				else return false;
			}
            });
		
		chatting_btn_send = (Button)findViewById(R.id.chatting_btn_send);
		chatting_btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				chatting_btn_send.setClickable(false);
				SendMSG_Pre();
				chatting_btn_send.setClickable(true);
			}
		});
		
		chatting_picture_btn = (ImageButton)findViewById(R.id.chatting_picture_btn);
		chatting_picture_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				chatting_picture_btn.setClickable(false);
				showDialog();
			}
		});
		title_bar_back = (ImageButton)findViewById(R.id.title_bar_back);
		title_bar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MsgTalk.this.finish();
			}
		});
		currentStart = globalID.getStartDate();
		currentEnd = globalID.getEndDate();
//		currentLine = globalID.getLine();
		
//		title_bar_title.requestFocus();
		
	}

		@Override
		protected void onResume(){
			super.onResume();
			if(log)Log.v(TAG,"onResume");
//			title_bar_title.requestFocus();
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.cancel_notification();
			globalID.un_stop = false;
			if(!isResume)refresh();
			
			check = false;
			if(globalID.isTalk_check_push()){
				globalID.setTalk_check_push(false);
				Thread Talk_check_push = new Thread(){
					public void run(){
						GlobalID globalID = ((GlobalID)getApplication());
								int time = 5;
								while(true){
//									time += time;
									
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
									
									if(log)Log.v(TAG,"Talk_check_push running...");
									//自己检测，跳出
									if(check){
										if(log)Log.v(TAG,"Talk_check_push break");
										globalID.setTalk_check_push(true);
										return;
									}
									//如果已经在后台，跳出
									if(globalID.notification != null){
										if(log)Log.v(TAG,"notification break");
										globalID.setTalk_check_push(true);
										return;
									}
									
									if(globalID.isTalk_push()){
										globalID.setTalk_push(false);
										refresh();
									}
									
								}
							}
						};
						Talk_check_push.start();
			}
			
			
			return;
		}

		private void initView() {
			// TODO Auto-generated method stub
			mListView = (ChatListView)findViewById(android.R.id.list);
			
//			mListView.setBackgroundResource(R.drawable.text_background);

			final GlobalID globalID = ((GlobalID)getApplication());
			mAdapter = new TalkAdapter(context,globalID.TalkList,Fail_Handler,ll_Handler,globalID.getID());
			mListView.setAdapter(mAdapter);
			
			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
			mListView.enableAutoFetchMore(false, 1);
			mListView.mFooterTextView.setText("点击刷新");
			mListView.setOnPullDownListener(new OnPullDownListener(){
				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/

				@Override
				public void onRefresh() {
					if((System.currentTimeMillis()-exitTime) < 2000){
						Thread sleep_thread = new Thread(){
							public void run(){
								try {
									sleep(500);
									Message msg = new Message();
									msg.what = 6;
									mhandler.sendMessage(msg);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						sleep_thread.start();
		            }
					else{
						GetDataTask talk = new GetDataTask();
						if(globalID.TalkList.size() != 0){
							talk.endTime = globalID.TalkList.get(0).getPostTime();
//							talk.setMsg_id = "<> " + globalID.TalkList.get(0).getMsg_id();
							if(log)Log.v(TAG,"endTime: "+talk.endTime);
						}
						else {
							talk.endTime = StringUtil.SIMPLE_DATEFORMAT.format(Calendar.getInstance().getTime());
//							talk.setMsg_id = "<> -1";
						}
						talk.startTime = "2013-01-01 00:00:00";
						talk.Case = 3;
						talk.Insert_Back = false;
						talk.DESC = "DESC";
						exitTime = System.currentTimeMillis();
						talk.execute();
					}
				}

				@Override
				public void onMore() {
					if((System.currentTimeMillis()-exitTime) < 2000){
						Thread sleep_thread = new Thread(){
							public void run(){
								try {
									sleep(500);
									Message msg = new Message();
									msg.what = 6;
									mhandler.sendMessage(msg);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						sleep_thread.start();
		            }
					else{
						refresh();
					}
				}
//				}
			});
		}
		
		public void refresh(){
			GetDataTask talk = new GetDataTask();
			GlobalID globalID = ((GlobalID)getApplication());
			if(globalID.TalkList.size() != 0){
				talk.startTime = globalID.TalkList.get(globalID.TalkList.size()-1).getPostTime();
//				talk.setMsg_id = "<> " + globalID.TalkList.get(globalID.TalkList.size()-1).getMsg_id();
			}
			else {
				talk.startTime = globalID.getStartDate();
//				talk.setMsg_id = "<> -1";
			}
			Calendar eDate = Calendar.getInstance();
			talk.endTime = StringUtil.SIMPLE_DATEFORMAT.format(eDate.getTime());
			talk.Case = 2;
			talk.Insert_Back = true;
			talk.DESC = "ASC";
            exitTime = System.currentTimeMillis();
			talk.execute();
		}
		
		
		private class GetDataTask extends AsyncTask<Void , Void , Void>{

			Message msg = new Message();
			public String startTime;
			public String endTime;
			public String user_id;
			//set which msg to send
			public int Case;
			public boolean Insert_Back;
//			public String setMsg_id;
			//set DESC or ASC
			public String DESC;
            
			@SuppressLint("NewApi")
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				if(msg != null) msg = new Message();
				GlobalID globalID = ((GlobalID)getApplication());
				globalID.setTalkUpdateLines(0);
				String url = globalID.getUrlServer();
				Socket socket = globalID.getSocket();
			    
				if(socket != null){
					
					String sql = "select TOP 5 aa.* from ( SELECT 0 as msg_type, " +
							"msg_id as id, user_id, ark_id, type, grade, datetime_send, " +
							"datetime_recv, msg " +
							"FROM tbl_msg " +
							"UNION ALL SELECT 1 as msg_type, img_id as id, user_id, ark_id, " +
							"type, grade, datetime_send, datetime_recv, img_name as msg " +
							"FROM tbl_img a) aa "
							+"where datetime_send >= #"+startTime
						 	+"# and datetime_send <= #"+endTime+"#"
						 	
//							+" and (aa.recvArkId = '"+ark_id+"' or aa.ark_id = '"+ark_id+"')"
						 	//use in future
//	                   			+" and tbl_msg_to_user.User_id = '"+globalID.getID() +"'"
						 	
//						 	+" and aa.id " + setMsg_id
						 	+" order by datetime_send "+ DESC+ ", aa.msg_type, aa.id, aa.ark_id \0";
					
//				    if(log)LogHelper.trace(TAG,"sql: "+sql);

					String sql_send = "";
					try {
						sql_send = new String(sql.getBytes("GB2312"),"8859_1");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    int msg_length = 1;
					int msg_send = sql_send.length();
					int Len_total = StringUtil.HEAD_LENGTH
				               +msg_length
				               +msg_send;
					Log.v("Len_total",":"+String.valueOf(Len_total));
					int Cmd_id = StringUtil.AU_SQL;
					int Seq_id = 1;
					int length = 0;
					byte type = StringUtil.Talk;
					
					if(Insert_Back) Seq_id = 0;
					
					byte[] send = new byte[Len_total];
					length = FuntionUtil.send_head(length, send
							, StringUtil.Len_total_length, Len_total
							, StringUtil.Cmd_id_length, Cmd_id
							, StringUtil.Seq_id_length, Seq_id);
					
//				    	Log.v("length","3:"+String.valueOf(length));
					for(int i = 0;i<msg_length;i++){
				        send[length] = type;
				    }
				    length += msg_length;
//				    	Log.v("length","4:"+String.valueOf(length));
					for(int i = 0;i<msg_send;i++){
//				            	if(i<Uid.length())
				    		send[length+i] = (byte)(sql_send.charAt(i));
//				            	else send[length+i] = (byte)(0);
				    }
				    length += msg_send;
//				    	Log.v("length","4:"+String.valueOf(length));
				    
					
					//test
//				        	for(int i = 0 ;i<15;i++){
//				        		Log.v("byte",i+"   :"+send[i]);
//				        	}

					if(log)Log.v(TAG,":"+sql);
					
				    OutputStream os = null;
					try {
						os = socket.getOutputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				    	if(msg != null) msg = new Message();
		                msg.what = 0;
					}
				   	try {
						os.write(send);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				    	if(msg != null) msg = new Message();
		                msg.what = 0;
					}
				   	
				   	for(exitTime = 0;exitTime < globalID.getTIMEOUT()*2;exitTime += globalID.getM_rate()){
			    		try {
								Thread.sleep(globalID.getM_rate());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			           	if(globalID.isTalk_code()){
			           		break;
			           	}	
			    	}
				   	
				   	if(globalID.isTalk_code()){
			    		if(msg != null) msg = new Message();
			             msg.what = Case;
				         globalID.setTalk_code(false);
//			                 mhandler.sendMessage(msg);
			    	 }
			    	 else{
			    		 if(msg != null) msg = new Message();
			             msg.what = 4;
//			                 mhandler.sendMessage(msg);
			    	 }
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result){
				super.onPostExecute(result);
				final GlobalID globalID = ((GlobalID)getApplication());
				mAdapter.notifyDataSetChanged();
				if(log)Log.v(TAG,"get date finish");
				if(Insert_Back)mListView.setSelection(globalID.TalkList.size());
				else {
					int select = globalID.TalkList.size() < globalID.getTalkUpdateLines()+1 ? 
							globalID.TalkList.size() : globalID.getTalkUpdateLines()+1;
					mListView.setSelection(select);
				}
				if(isResume){
					mListView.setSelection(globalID.TalkList.size());
					isResume = false;
				}
				if(Case == 3)((ChatListView) getListView()).onRefreshComplete();
				if(Case == 2)((ChatListView) getListView()).onMoreComplete();
				mhandler.sendMessage(msg);
			}
			
		}
		
//	    /***********Json******************/
//	    private boolean parseJson(String resultObj,boolean Insert_Back){
//	    	boolean insert = false;
//	    	updateLines = 0;
//	    	final GlobalID globalID = ((GlobalID)getApplication());
//	    	if(log)Log.v("resultObj: ",resultObj);
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//						for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//							insert = false;
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							
//							TalkEntity entity = new TalkEntity(
//									//表示船消息类型，0表示文字，1表示图片
//									jsonObject.optString("msg_type").equals("0")
//									//表示船消息方向，0表示船往岸发数据，1表示岸往船发数据
//									, jsonObject.optString("type").equals("1")
//									
//									//从html转换为string
//									, Html.fromHtml(jsonObject.optString("msg")).toString()
//									, jsonObject.optString("datetime_send")
//									, jsonObject.optString("ark_id")
//									, jsonObject.optString("user_id")
//									, jsonObject.optString("msg_id"));
//							if(!entity.getIsMsg()){
//								String pic = entity.getDetail();
//								if(log)Log.v("pic: " , pic);
//								Bitmap bit = null;
////								Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
//								if(bit!=null){
//									entity.setImage_picture(bit);
//			  					}
//								else{
//		 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//									entity.setImage_picture(good);
//		 	  						}
//							}
//							
//							for(int j = globalID.TalkList.size()-1 ; j>-1 ; j--){
//								if(entity.getMsg_id().equals(globalID.TalkList.get(j).getMsg_id()) && 
//										entity.getIsMsg() == globalID.TalkList.get(j).getIsMsg()){
//									insert = true;
//									break;
//								}
//							}
//							if(insert)continue;
//							if(!Insert_Back)globalID.TalkList.add(0,entity);
//							else globalID.TalkList.add(entity);
//							
//							updateLines++;
//					}
//				}
//				
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
//			return true;
//		}
//	    /***********Json******************/
	    
	    void toast(String msg){
			GlobalID globalID = ((GlobalID)getApplication());
			if(pd!=null)pd.dismiss();
			if(globalID.toast != null)globalID.toast.cancel();
			globalID.toast = Toast.makeText(MsgTalk.this, msg, Toast.LENGTH_LONG);
			globalID.toast.show();
	    }
	    
	    @Override 
	    public void onSaveInstanceState(Bundle savedInstanceState) {  
	    	// Save away the original text, so we still have it if the activity   
	    	// needs to be killed while paused.
	    	super.onSaveInstanceState(savedInstanceState);  
	    	if(log)Log.e(TAG, "onSaveInstanceState");
	    	GlobalID globalID = ((GlobalID)getApplication());
//	    	globalID.clear();
	    	savedInstanceState.putString("TalkList", globalID.Talk2String());
	    	if(globalID.un_stop)return;
	    	else{
		    	globalID.create_notification("后台接收数据", "后台运行", "船客户端", false, false, false
		    			,MsgTalk.class.getName());
		    	if(globalID.toast != null)globalID.toast.cancel();
		    	}
	    	}  
	    
	    @Override
	    public void onPause(){
	    	super.onPause();
	    	if(log)Log.v(TAG,"onPause");
			check = true;
	    }
	    
//	    private String Talk2String() {
//			// TODO Auto-generated method stub
//			final GlobalID globalID = ((GlobalID)getApplication());
//	    	TalkEntity entity = new TalkEntity();
//	    	String encode = "[";
//			for(int i = 0 ; i < globalID.TalkList.size();){
//				entity = globalID.TalkList.get(i);
//				encode += "{";
//
//				encode += "\"isMsg\":\"" + entity.getIsMsg() + "\";";
//				encode += "\"isSend\":\"" + entity.getIsSend() + "\";";
//				encode += "\"detail\":\"" + entity.getDetail() + "\";";
//				encode += "\"PostTime\":\"" + entity.getPostTime() + "\";";
////				encode += "\"ark_id\":\"" + entity.getArk_id() + "\";";
//				encode += "\"send_user_id\":\"" + entity.getSend_user_id() + "\";";
//				encode += "\"msg_id\":\"" + entity.getMsg_id() + "\";";
//				encode += "\"state\":\"" + entity.getState() + "\"";
//				
//				encode += "}";
//				if(++i < globalID.TalkList.size()){
//					encode += ",";
//				}
//			}
//			encode += "]";
//			if(log)LogHelper.trace(TAG, encode);
//			return encode;
//		}
	    
	    @Override  
	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
	    	super.onRestoreInstanceState(savedInstanceState);
//	    	final String StrTest = savedInstanceState.getString("globalID.TalkList");
//	    	if(log)Log.e(TAG, "onRestoreInstanceState + globalID.TalkList = "+ StrTest);
//	    	Thread getImg = new Thread(){
//				public void run(){
//					getTalkDataArrays(StrTest);
//					if(msg!=null)msg = new Message();
//					msg.what = 5;
//					mhandler.sendMessage(msg);
//				}
//			};
//			getImg.start();
	    	}  
	    
//	    private void getTalkDataArrays(String decode) {
//			// TODO Auto-generated method stub
//			final GlobalID globalID = ((GlobalID)getApplication());
//			try{
//				JSONArray jsonArray = new JSONArray(decode);
//				if(jsonArray.length()==0){
//					return;
//					}
//				else{
//					for(int i = 0 ; i < jsonArray.length() ; i++){
//						JSONObject jsonObject = jsonArray.getJSONObject(i);
//						
//						TalkEntity entity = new TalkEntity(Boolean.valueOf(jsonObject.optString("isMsg"))
//								, Boolean.valueOf(jsonObject.optString("isSend"))
//								
//								, Html.fromHtml(jsonObject.optString("detail")).toString()
//								, jsonObject.optString("PostTime")
////								, jsonObject.optString("ark_id")
//								, jsonObject.optString("send_user_id")
//								, jsonObject.optString("msg_id"));
//						
//						entity.setState(Integer.valueOf(jsonObject.optString("state")));
//
//						LogHelper.trace(jsonObject.optString("detail"));
//						LogHelper.trace(Html.fromHtml(jsonObject.optString("detail")).toString());
//						
////						if(jsonObject.optString("isMsg").equals("false")){
////							String pic = jsonObject.optString("detail");
////							GlobalID globalID = ((GlobalID)getApplication());
////							Bitmap bit = downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
////							if(bit!=null){
////								entity.setImage_picture(bit);
////		  					}
////							else{
////	 	  		 				Bitmap default_Img  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
////								entity.setImage_picture(default_Img);
////	 	  					}
////						}
//						
//						globalID.TalkList.add(entity);
//					}
//				}
//			}catch(JSONException e){
//				e.printStackTrace();
//				return;
//			}
//		}
	    
		/*********************Picture*********************************/
		private void showDialog(){
			chatting_picture_btn.setClickable(true);
			new AlertDialog.Builder(this).setTitle("选择图片").setItems(items, new
					DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch(which){
							case 0 :
								
								//跳转相册
								Intent intentFromGallery = new Intent();
								intentFromGallery.setType("image/*");
								//catch nothing
								if(intentFromGallery.setAction(Intent.ACTION_GET_CONTENT)==null)break;
								
								startActivityForResult(intentFromGallery , IMAGE_REQUEST_CODE );
								break;
							case 1:
								
								//跳转摄像机
								Intent intentFromCapture = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								if(Tools.hasSdcard()){
									intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT
											,Uri.fromFile(new File(Environment.getExternalStorageDirectory()
													,IMAGE_FILE_NAME)) );
								}
								startActivityForResult(intentFromCapture , CAMERA_REQUEST_CODE);
								break;
							}
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();
		}		

		@Override
		protected void onActivityResult(int requestCode , int resultCode , Intent data){
			if(resultCode != RESULT_CANCELED){
			switch (requestCode){
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if(Tools.hasSdcard()){
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
//					startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
				}else{
					Toast.makeText(context, "未找到存储卡，无法存储照片", Toast.LENGTH_LONG).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if(data != null){
					getImageToView(data);
				}
				break;
			}
		}
			super.onActivityResult(requestCode, resultCode, data);
		}
		
		public void startPhotoZoom(Uri uri){
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			
			intent.putExtra("crop", true);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			
			intent.putExtra("outputX", 90);
			intent.putExtra("outputY", 90);
			intent.putExtra("return-data", true);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true); 
			startActivityForResult(intent , RESULT_REQUEST_CODE);
		}
	
		private void getImageToView(Intent data){
			Bundle extras = data.getExtras();
			GlobalID globalID = ((GlobalID)getApplication());
			if(extras != null ){
				Bitmap photo = extras.getParcelable("data");
//				@SuppressWarnings("deprecation")
//				Drawable drawable = new BitmapDrawable(photo);
				
				TalkEntity entity = new TalkEntity(false
						//设置是否有岸上发送
						, false
						, ""
						, StringUtil.SIMPLE_DATEFORMAT.format(Calendar.getInstance().getTime())
//						, String.valueOf(ark_id)
						, globalID.getID()
						, "");
				entity.setState(0);
				entity.setImage_picture(photo);
				
//				Message msg = new Message();
//				msg.obj = entity;
//				add_handler.sendMessage(msg);
				
				globalID.TalkList.add(entity);
				SendIMG(globalID.TalkList.size()-1,photo);
//				iv_send_HeadPhoto.setImageDrawable(drawable);
//				if(log)Log.v(TAG, "has picture");
//				no_picture = false;
			}
		}
		/*********************Picture*********************************/

		private void SendMSG_Pre(){
			sendmessage = et_sendmessage.getText().toString();
			if(sendmessage.length() == 0){
				chatting_btn_send.setClickable(true);
				return;
			}
			else{
				GlobalID globalID = ((GlobalID)getApplication());				
				TalkEntity entity = new TalkEntity(true
						, false
						, sendmessage
						, StringUtil.SIMPLE_DATEFORMAT.format(Calendar.getInstance().getTime())
//						, String.valueOf(ark_id)
						, globalID.getID()
						, "");
				if(log)LogHelper.trace(TAG,"  "+ entity.getPostTime());
				entity.setState(0);
//				Message msg = new Message();
//				msg.obj = entity;
//				add_handler.sendMessage(msg);
//				mAdapter.notifyDataSetChanged();
				globalID.TalkList.add(entity);
				mListView.setSelection(globalID.TalkList.size());
				et_sendmessage.setText("");
				
				//转换成html格式
				//这里的position globalID.TalkList.size()可能由于上面是用handler，导致长度不太对
				SendMSG(globalID.TalkList.size()-1,FuntionUtil.toHTMLString(entity.getDetail()));
			}
		}

		private void SendMSG(final int position,final String sendmsg) {
			// TODO Auto-generated method stub
//			pd = ProgressDialog.show(MsgTalk.this, "连接", "正在连接服务器…");
			Thread send_thread = new Thread(){
				@Override
				public void run(){
					GlobalID globalID = ((GlobalID)getApplication());
					Socket socket = globalID.getSocket();
					if(globalID.isConnect){
						final int  recieve_num = 0;
						final String level = String.valueOf(msg_level);
						String send_id = "";
						
    		        	int time_send_length = 6;
    		        	int senderId_length = 4;
    		        	int senderMobileLen_length = 1;
    		        	int senderMobile_length = 0;
    		        	int grade_length = 1;
    		        	int option_length = 2;
    		        	int data_type_length = 1;
    		        	int len_data_length = 4;
    		        	int data_length = 0;
    		        	
    		        	int Len_total = 0;
    		        	int Cmd_id = StringUtil.AU_MO_CUST_DATA;
    		        	int Seq_id = 0;
    		        	
    		        	String time_send = "";
    					Calendar eDate = Calendar.getInstance();
    					try {
							time_send = new String(StringUtil.SEND_DATEFORMAT.format(eDate.getTime()).getBytes("GB2312"),"8859_1");
						} catch (UnsupportedEncodingException e4) {
							// TODO Auto-generated catch block
							e4.printStackTrace();
							SthWrong(position);
							return;
						}
//    					if(log)Log.v("send", "time: " + time_send);
    					
    					byte[] time_send1 = new byte [time_send.length()];
    					for(int i = 0 ;i < time_send.length();i++){
    						time_send1[i] = (byte) time_send.charAt(i);
    					}
    					byte[] time_send2 = FuntionUtil.HexStrToBin(time_send1);
    					time_send = new String(time_send2);
//    					byte[] time_send2 = str2Bcd(time_send);
    					
    					String senderMobile = "";
						try {
							senderMobile = new String(globalID.getID().getBytes("GB2312"),"8859_1");
						} catch (UnsupportedEncodingException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
							SthWrong(position);
							return;
						}
    		        	senderMobile_length = senderMobile.length();
    					int grade = Integer.parseInt(level);
    					int cntRecver = 0;
    					int recverLen = 0;
    					int recver = 0;
    					int cntGrp = 0;
    					int lstGrp = 0;
    					char data_type = 3;
    					String data = "";
    					
    					/**************recver group***************/
    					short option = 0;
    					int cntRecver_length = 1;
    					int recverLen_length = 1;
    					int recver_length = 0;
    					int cntGrp_length = 1;
    					int lstGrp_length = 0;
    					/**************recver group***************/
    					
    					cntGrp_length = 0;
    					switch(recieve_num){
    					case 0:
    						option |= 16;
    					break;
    					case 1:
    						option |= 4;
    					break;
    					case 2:
    						option |= 8;
    					break;
    					case 3:
    					break;
    					}
    					
    		        	if(send_id.length() == 0){
    		        		cntRecver_length = 0;
    						recverLen_length = 0;
    						cntRecver = 0;
    						}
    		        	else{
    		        		option |= 2;
    		        		cntRecver_length = 1;
    						recverLen_length = 1;
    						cntRecver = 1;
    		        		}
    					recver_length = send_id.length();
    					recverLen = recver_length;
    					
    					String detail = "";
						try {
							detail = new String(sendmsg.getBytes("GB2312"),"8859_1");
						} catch (UnsupportedEncodingException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
							SthWrong(position);
							return;
						}
						data_type = 0;
						
						int txt_length = detail.length();
						
						data_length = txt_length;
						short Len_txt = (short) detail.length();
						
						Len_total = StringUtil.HEAD_LENGTH
								+ time_send_length + senderId_length
								+ senderMobileLen_length + senderMobile_length
								+ grade_length + option_length
								+ cntRecver_length + recverLen_length
								+ recver_length + cntGrp_length
								+ lstGrp_length + data_type_length
								+ len_data_length + data_length;
						
//						if(log)Log.v("send", "len_total: "+String.valueOf(Len_total));
						
						int length = 0;
						byte[] send = new byte[Len_total];
						length = FuntionUtil.send_head(length, send
								, StringUtil.Len_total_length, Len_total
								, StringUtil.Cmd_id_length, Cmd_id
								, StringUtil.Seq_id_length, Seq_id);
			        	
						length = FuntionUtil.send_body_first(length, send, time_send_length, time_send2
												, senderId_length, senderMobile_length, senderMobileLen_length
												, senderMobile, grade, grade_length, option, option_length
												, cntRecver, cntRecver_length, recverLen, recverLen_length
												, recver_length, send_id, cntGrp_length, data_type, data_type_length
												, len_data_length, data_length);
						
			        	for(int i = 0;i<Len_txt;i++){
			            	send[length+i] = (byte) detail.charAt(i);
			            }
			            length += Len_txt;
//			        	if(log)Log.v("length","18:"+String.valueOf(length));

			            OutputStream os = null;
						try {
							os = socket.getOutputStream();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							SthWrong(position);
							return;
						}
			            try {
							os.write(send);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							SthWrong(position);
							return;
						}
			            
			            for(long exitTime = 0;exitTime < globalID.getTIMEOUT();exitTime += globalID.getM_rate()){
    						try {
    							Thread.sleep(globalID.getM_rate());
    							if(log)Log.v("send", "sleep");
//    			        			wait(globalID.getM_rate());
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    							if(log)Log.e("send", "InterruptedException: "+e);
    						}
    						
    				       	if(globalID.isSend_code()){
    							if(log)Log.v("send", "isSend_code");
//    				       		if(log)Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
    				       		break;
    				       	}	
//    			        	}
    					}
//    			        	
    					if(globalID.isSend_code()){
//    			       		if(log)Log.v("send", "isSend_code = " + String.valueOf(globalID.isSend_code()));
    			       		if(globalID.isShake())vibrator.vibrate(1000);
    			       		Message msg = new Message();
    				        msg.what = 1;
    				        msg.arg1 = position;
    				        mhandler3.sendMessage(msg);
    				        globalID.setSend_code(false);
    					 }
    					 else{
 							SthWrong(position);
 							return;
    					 }
					}
					else{
						SthWrong(position);
						return;
					}
				}
			};
			send_thread.start();
		}
		
		private void SendIMG(final int position,final Bitmap img) {
			// TODO Auto-generated method stub

			Thread send_thread = new Thread(){
				@Override
				public void run(){
					GlobalID globalID = ((GlobalID)getApplication());
					Socket socket = globalID.getSocket();
					if(globalID.isConnect){
						final int  recieve_num = 0;
						final String level = String.valueOf(msg_level);
						String send_id = "";

    		        	int time_send_length = 6;
    		        	int senderId_length = 4;
    		        	int senderMobileLen_length = 1;
    		        	int senderMobile_length = 0;
    		        	int grade_length = 1;
    		        	int option_length = 2;
    		        	int data_type_length = 1;
    		        	int len_data_length = 4;
    		        	int data_length = 0;
    		        	
    		        	int Len_total = 0;
    		        	int Cmd_id = StringUtil.AU_MO_CUST_DATA;
    		        	int Seq_id = 0;
    		        	
    		        	String time_send = "";
    					Calendar eDate = Calendar.getInstance();
    					try {
							time_send = new String(StringUtil.SEND_DATEFORMAT.format(eDate.getTime()).getBytes("GB2312"),"8859_1");
						} catch (UnsupportedEncodingException e4) {
							// TODO Auto-generated catch block
							e4.printStackTrace();
							SthWrong(position);
							return;
						}
    					if(log)Log.v("send", "time: " + time_send);
    					
    					byte[] time_send1 = new byte [time_send.length()];
    					for(int i = 0 ;i < time_send.length();i++){
    						time_send1[i] = (byte) time_send.charAt(i);
    					}
    					byte[] time_send2 = FuntionUtil.HexStrToBin(time_send1);
    					time_send = new String(time_send2);
    					if(log)Log.v(TAG,"time_send: "+time_send);
//    					byte[] time_send2 = str2Bcd(time_send);
    					
    					String senderMobile = "";
						try {
							senderMobile = new String(globalID.getID().getBytes("GB2312"),"8859_1");
						} catch (UnsupportedEncodingException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
							SthWrong(position);
							return;
						}
    		        	senderMobile_length = senderMobile.length();
    					int grade = Integer.parseInt(level);
    					int cntRecver = 0;
    					int recverLen = 0;
    					int recver = 0;
    					int cntGrp = 0;
    					int lstGrp = 0;
    					char data_type = 3;
    					String data = "";
    					
    					/**************recver group***************/
    					short option = 0;
    					int cntRecver_length = 1;
    					int recverLen_length = 1;
    					int recver_length = 0;
    					int cntGrp_length = 1;
    					int lstGrp_length = 0;
    					/**************recver group***************/
    					
    					cntGrp_length = 0;
    					switch(recieve_num){
    					case 0:
    						option |= 16;
    					break;
    					case 1:
    						option |= 4;
    					break;
    					case 2:
    						option |= 8;
    					break;
    					case 3:
    					break;
    					}
    					
    		        	if(send_id.length() == 0){
    		        		cntRecver_length = 0;
    						recverLen_length = 0;
    						cntRecver = 0;
    						}
    		        	else{
    		        		option |= 2;
    		        		cntRecver_length = 1;
    						recverLen_length = 1;
    						cntRecver = 1;
    		        		}
    					recver_length = send_id.length();
    					recverLen = recver_length;

						data_type = 1;
						
    					//compress
    					ByteArrayOutputStream change = new ByteArrayOutputStream();
    					Bitmap image = comp(img);
    					image.compress(Bitmap.CompressFormat.JPEG,90,change);
    					
    					final byte[] send_p = change.toByteArray();
    					
						for(int i = send_p.length-15;i<send_p.length;i++){
							if(log)Log.v("send", String.valueOf(i)+ " send_p: " + send_p[i]);
						}
						
						int img_name_length = 1;
						int img_length = send_p.length;
						
						data_length = img_name_length + img_length;
						
						Len_total = StringUtil.HEAD_LENGTH
								+ time_send_length + senderId_length
								+ senderMobileLen_length + senderMobile_length
								+ grade_length + option_length
								+ cntRecver_length + recverLen_length
								+ recver_length + cntGrp_length
								+ lstGrp_length + data_type_length
								+ len_data_length + data_length;
						
						if(log)Log.v("send", "len_total: "+String.valueOf(Len_total));
						
						int length = 0;
						byte[] send = new byte[Len_total];
						length = FuntionUtil.send_head(length, send
								, StringUtil.Len_total_length, Len_total
								, StringUtil.Cmd_id_length, Cmd_id
								, StringUtil.Seq_id_length, Seq_id);
						
						length = FuntionUtil.send_body_first(length, send, time_send_length, time_send2
								, senderId_length, senderMobile_length, senderMobileLen_length
								, senderMobile, grade, grade_length, option, option_length
								, cntRecver, cntRecver_length, recverLen, recverLen_length
								, recver_length, send_id, cntGrp_length, data_type, data_type_length
								, len_data_length, data_length);
						
			        	send[length] = 0;
			            length += img_name_length;
			        	if(log)Log.v("length","16:"+String.valueOf(length));
			        	
			        	for(int i = 0;i<img_length;i++){
			            	send[length+i] = (byte) send_p[i];
			            }
			            length += img_length;
			        	if(log)Log.v("length","19:"+String.valueOf(length));
			        	
			            
			            OutputStream os = null;
						try {
							os = socket.getOutputStream();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
 							SthWrong(position);
 							return;
						}
			            try {
							os.write(send);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
 							SthWrong(position);
 							return;
						}
			            
			            for(long exitTime = 0;exitTime < globalID.getTIMEOUT();exitTime += globalID.getM_rate()){
    						try {
    							Thread.sleep(globalID.getM_rate());
    							if(log)Log.v("send", "sleep");
//    			        			wait(globalID.getM_rate());
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    							if(log)Log.v("send", "InterruptedException: "+e);
    						}
    				       	if(globalID.isSend_code()){
    							if(log)Log.v("send", "isSend_code");
//    				       		if(log)Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
    				       		break;
    				       	}	
//    			        	}
    					}
//    			        	
    					if(globalID.isSend_code()){
    			       		if(log)Log.v("send", "isSend_code = " + String.valueOf(globalID.isSend_code()));
    			       		if(globalID.isShake())vibrator.vibrate(1000);
    			       		Message msg = new Message();
    				         msg.what = 1;
    				         msg.arg1 = position;
    				         mhandler3.sendMessage(msg);
    				         globalID.setSend_code(false);
    					 }
    					 else{
 							SthWrong(position);
 							return;
    					 }
					}
					else{
						SthWrong(position);
						return;
					}
				}
			};
			send_thread.start();
		}		

		private Bitmap comp(Bitmap image) {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();		
			image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
			if( baos.toByteArray().length / 1024>100) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			//开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
			float hh = 800f;//这里设置高度为800f
			float ww = 480f;//这里设置宽度为480f
			//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;//be=1表示不缩放
//			if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//				be = (int) (newOpts.outWidth / ww);
//			} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//				be = (int) (newOpts.outHeight / hh);
//			}
			be = (int) ((w / ww + h/ hh) / 2);
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;//设置缩放比例
			newOpts.inPreferredConfig = Config.RGB_565;
			//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			isBm = new ByteArrayInputStream(baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
		}
		
		private Bitmap compressImage(Bitmap image) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
				options -= 10;//每次都减少10
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
			return bitmap;
		}
		
//		private void Resend(int position){
//			isResume = true;
//			if(log)Log.v(TAG,"position: "+position);
//			if(position < 0 || position >= globalID.TalkList.size())return;
//			globalID.TalkList.get(position).setState(0);
//			mAdapter.notifyDataSetChanged();
//			if(globalID.TalkList.get(position).getIsMsg()){
//				SendMSG(position, globalID.TalkList.get(position).getDetail());
//			}
//			else{
//				SendIMG(position, globalID.TalkList.get(position).getImage_picture());
//			}
//		}
		
		private void Resend(TalkEntity entity){
			final GlobalID globalID = ((GlobalID)getApplication());
			isResume = true;
			globalID.TalkList.remove(entity);
			int position = globalID.TalkList.size();
			entity.setState(0);
			Message msg = new Message();
			msg.obj = entity;
			add_handler.sendMessage(msg);
			if(log)Log.v(TAG,"position: "+position);
			if(position < 0 || position >= globalID.TalkList.size())return;
			mAdapter.notifyDataSetChanged();
			if(entity.getIsMsg()){
				SendMSG(position, FuntionUtil.toHTMLString(entity.getDetail()));
			}
			else{
				SendIMG(position, entity.getImage_picture());
			}
		}
		
		private void SthWrong(int position){
			Message msg = new Message();
			msg.what = -1;
			msg.arg1 = position;
			mhandler3.sendMessage(msg);
		}
		
		@Override
		 public void finish(){
			 super.finish();
			 final GlobalID globalID = ((GlobalID)getApplication());
			 globalID.TalkList.clear();
			 if(log)Log.v(TAG,"finish");
			 overridePendingTransition(R.anim.list_in, R.anim.item_out);
//			 GlobalID globalID = ((GlobalID)getApplication());
//			 globalID.un_stop = true;
		 }
}

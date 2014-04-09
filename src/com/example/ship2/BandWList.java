package com.example.ship2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ship2.MsgListView.OnPullDownListener;
import com.example.ship_Adapter.BandWAdapter;
import com.example.ship_Entity.BandWEntity;
import com.example.ship_Info.BandWInfo;
import com.example.shop_util.FuntionUtil;
import com.example.shop_util.LogHelper;
import com.example.shop_util.StringUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.text.ClipboardManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class BandWList extends ListActivity{
	
	private Context context = BandWList.this;

	//设置list的距离
	private static final int left = 13;
	private static final int top = 4;
	private static final int right = 13;
	private static final int bottom = 0;
	
	private static final int divider = 20;
	
	private static boolean log = false;
	private String TAG = "BandWList";
	
	//set background_colour here and info background_colour
	private String background_colour = "#D4DEE8";
	
	//connect address
	InetAddress serverAddr;
	
	//test data
//	static int k = 0;
	
	//MsgListView and Adapter
	private MsgListView mListView ;
	private BandWAdapter mAdapter;
	
	private String currentStart = "2013-01-01 00:00:00";
	private String currentEnd = "3000-12-31 23:59:59";
//	private int currentLine = 5;
//	private int updateLines = 0;
	private long exitTime = 0;
	
//	private List<BandWEntity> mArrays = new ArrayList<BandWEntity>();
//	private List<bussinessEntity> mArrays2 = new ArrayList<bussinessEntity>();

//	ProgressDialog pd = null;

	Message msg = new Message();
	final Handler add_handler = new Handler(){
		@Override
		public void handleMessage (Message msg){
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.BandWArrays.add((BandWEntity) msg.obj);
			mAdapter.notifyDataSetChanged();
		}
	};
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			GlobalID globalID = ((GlobalID)getApplication());
			int updateLines = globalID.getBandWUpdateLines();
			mAdapter.notifyDataSetChanged();
			//好像直接用global里面的数组，会有问题
			if(log)Log.v("mhandler", "mArrays.size:"+globalID.BandWArrays.size());
			globalID.cancelPD();
			
			switch (msg.what){
			case 0:
//				if(log)Log.v("mhandler", "case 0");
				globalID.toast(context,"查询错误");
				break;
			case 1:
//				if(log)Log.v("mhandler", "case 1");
				if(updateLines!=0){
					globalID.toast(context,"该时段最近"+ updateLines +"条新信息");
					break;
				}
			case 2:
//				if(log)Log.v("mhandler", "case 2");
				if(updateLines!=0){
					globalID.toast(context,"刷新"+ updateLines +"条新信息");
					break;
				}
			case 3:
//				if(log)Log.v("mhandler", "case 3");
				if(updateLines!=0){
					globalID.toast(context,"加载"+ updateLines +"条历史信息");
					break;
				}
			case 4:
//				if(log)Log.v("mhandler", "case 4");
				globalID.toast(context,"没有找到新信息");
				break;
				}
		}
	};
	
	final Handler ll_Handler = new Handler(){
		@Override
		public void  handleMessage (final Message msg){
			
			LogHelper.trace(String.valueOf(msg.arg1) + " " + String.valueOf(msg.arg2));
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.un_stop = true;
			Bundle bundle = new Bundle();
			bundle.putInt("index", msg.arg1);
			bundle.putString("colour", background_colour);
			
			Intent intent = new Intent(BandWList.this,BandWInfo.class);
			
			//post data
			intent.putExtras(bundle);
            startActivity(intent);
		}
	};
	
		@Override 
		public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		final GlobalID globalID = ((GlobalID)getApplication());
		
		if(null != savedInstanceState){
			final String decode = savedInstanceState.getString("BandWArrays");
	    	if(log)Log.e(TAG, "null != savedInstanceState BandWArrays = "+ decode);
//	    	globalID.start(context);
	    	Thread decode_thread = new Thread(){
				public void run(){
//					getmArrays(StrTest);
					globalID.BandWJson(decode, true, globalID.BandWArrays);
				}
			};
			decode_thread.start();
			}
		
//		if(null != savedInstanceState){
//			String decode = savedInstanceState.getString("BandWArrays"); 
//			if(log)Log.e(TAG, "onCreate get the BandWArrays = "+decode);
//			globalID.setBandWArrays(decode);
//			}
		
		//set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msglistview);
		if(log)Log.v(TAG,"onCreate");

		//set Adapter for ListView
		initView();
		
		if(globalID.getID() == null||globalID.getStartDate() == null||globalID.getEndDate() == null){
			globalID.start();
		}
			currentStart = globalID.getStartDate();
			currentEnd = globalID.getEndDate();
//			currentLine = globalID.getLine();
			
			globalID.BandWArrays.clear();
			
			GetDataTask bus = new GetDataTask();
			bus.startTime = currentStart;
			bus.endTime = currentEnd;
			bus.Case = 1;
			bus.Insert_Head = false;
			bus.DESC = "DESC";
			if(log)Log.v(TAG,"currentStart: "+currentStart+" currentEnd: "+currentEnd);
			globalID.PD(context, "连接", "正在获取数据…");
			bus.execute();
		}

		private void initView() {
			// TODO Auto-generated method stub
			final GlobalID globalID = ((GlobalID)getApplication());
			mListView = (MsgListView)findViewById(android.R.id.list);
			mListView.setBackgroundColor(android.graphics.Color.parseColor(background_colour));
//			mListView.setBackgroundResource(R.drawable.text_background);
			mListView.setPadding(left, top, right, bottom);
			mListView.setDividerHeight(divider);
			
			mAdapter = new BandWAdapter(context,globalID.BandWArrays,ll_Handler);
			mListView.setAdapter(mAdapter);
			

			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
			mListView.enableAutoFetchMore(false, 1);
//			//隐藏 并禁用尾部
//			mListView.setHideFooter();
//			//显示并启用自动获取更多
//			mListView.setShowFooter();
			
			mListView.setOnPullDownListener(new OnPullDownListener(){
				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					if((System.currentTimeMillis()-exitTime) < 2000){
						((MsgListView) getListView()).onRefreshComplete();
		            }
					else{
						refresh();
					}
				}

				@Override
				public void onMore() {
					// TODO Auto-generated method stub
					if((System.currentTimeMillis()-exitTime) < 2000){
						((MsgListView) getListView()).onMoreComplete();
		            }
					else{
						GetDataTask bus = new GetDataTask();
						if(!globalID.BandWArrays.isEmpty()){
							bus.endTime = globalID.BandWArrays.get(globalID.BandWArrays.size()-1).getTime();
							if(log)Log.v("onmore", "startTime = "+ globalID.BandWArrays.get(globalID.BandWArrays.size()-1).getTime());
						}
						else {
							bus.endTime = globalID.getStartDate();
						}
						
						bus.startTime = "2013-01-01 00:00:00";
						bus.Case = 2;
						bus.Insert_Head = false;
						bus.DESC = "ASC";
			            exitTime = System.currentTimeMillis();
			            
						bus.execute();
					}
				}
			});
		}
		
//		public void onOtherResume(){
//			super.onResume();
//			if(log)Log.v(TAG,"bus_onOtherResume");
//			GlobalID globalID = ((GlobalID)getApplication());
//			globalID.un_stop = false;
//			if(globalID.isBus_push_UnGet()){
////				if(log)Log.v(TAG,"bus_push");
//				refresh();
//				globalID.setBus_push_UnGet(false);
//			}
//		}
		
		public void refresh(){

			GlobalID globalID = ((GlobalID)getApplication());
			globalID.setBandW_push_UnGet(0);
			GetDataTask bus = new GetDataTask();
			if(!globalID.BandWArrays.isEmpty()){
				bus.startTime = globalID.BandWArrays.get(0).getTime();
				if(log)Log.v("refresh", "startTime = "+ globalID.BandWArrays.get(0).getTime());
			}
			else {
				bus.startTime = globalID.getStartDate();
			}
			bus.endTime = StringUtil.YYMMDD.format(Calendar.getInstance().getTime());
			bus.Case = 3;
			bus.Insert_Head = true;
			bus.DESC = "DESC";
			bus.execute();
			
            exitTime = System.currentTimeMillis();
		}
		
		@Override
		protected void onResume(){
			super.onResume();
			if(log)Log.v(TAG,"onResume");
			return;
		}
		
		/*********************test data**************************/
//		private void initData(int k) {
//			// TODO Auto-generated method stub
//
//			bussinessEntity entity = new bussinessEntity
//		();
//			entity.setBussiness_detail(String.valueOf(k));
//			Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//			entity.setBussiness_picture(good);
//			
//			//set the last to the top
//				if(mArrays.isEmpty()){
//					mArrays.add(0,entity);
//					mArrays.add(entity);
//					return;
//					}
//				else{
//					mArrays.add(mArrays.get(mArrays.size()-1));
//					for(int i = mArrays.size()-3;i>-1;i--){
//						mArrays.set(i+1, mArrays.get(i));
//						}
//
//					mArrays.set(0,entity);
//					mAdapter.notifyDataSetChanged();
					//complete the refresh picture
//					((MsgListView) getListView()).onRefreshComplete();
//				}
//		}
		/*********************test data**************************/
		
		private class GetDataTask extends AsyncTask<Void , Void , Void>{

			public String startTime;
			public String endTime;

			//set which msg to send
			public int Case;
			public boolean Insert_Head;
			//set DESC or ASC
			public String DESC;
			
			@SuppressLint("NewApi")
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				if(msg != null) msg = new Message();
				GlobalID globalID = ((GlobalID)getApplication());
				globalID.setBandWUpdateLines(0);
				Socket socket = globalID.getSocket();
			    
				if(socket != null){
					String sql = "select TOP 5 * from (Select 1 as list_type, " +
							"ID, Title, '' as Typeid, Content as Detail " +
							",'' as Pic, PostTime FROM Board " +
							" UNION ALL " +
							"SELECT 0 as list_type, " +
							"Id, Title, Typeid, msg as Detail " +
							",ImageFile as Pic, PostTime FROM Goods ) bw "
						 	+"where PostTime >= #"+startTime
						 	+"# and PostTime <= #"+endTime
						    +"# order by PostTime "+ DESC + ", bw.list_type \0";
					
					String sql_send = "";
					
					try {
						sql_send = new String(sql.getBytes("GB2312"),"8859_1");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					int msg_type_length = 1;
					int msg_send = sql_send.length();
					int Len_total = StringUtil.HEAD_LENGTH
				               +msg_type_length
				               +msg_send;
					if(log)Log.v(TAG,"Len_total:"+String.valueOf(Len_total));
					int Cmd_id = StringUtil.AU_SQL;
					int Seq_id = 1;
					int length = 0;
					byte type = StringUtil.BandW;

					if(Insert_Head) Seq_id = 0;
					
					byte[] send = new byte[Len_total];
					length = FuntionUtil.send_head(length, send
							, StringUtil.Len_total_length, Len_total
							, StringUtil.Cmd_id_length, Cmd_id
							, StringUtil.Seq_id_length, Seq_id);
					
					for(int i = 0;i<msg_type_length;i++){
				        send[length] = type;
				    }
				    length += msg_type_length;
//						Log.v("length","4:"+String.valueOf(length));
					for(int i = 0;i<msg_send;i++){
//				            	if(i<Uid.length())
				    		send[length+i] = (byte)(sql_send.charAt(i));
//				            	else send[length+i] = (byte)(0);
				    }
				    length += msg_send;
//						Log.v("length","4:"+String.valueOf(length));
				    
					
					//test
//						for(int i = 0 ;i<15;i++){
//							Log.v("byte",i+"   :"+send[i]);
//						}

//						Log.v("startTime",":"+startTime);
//						Log.v("endTime",":"+endTime);
				    if(log)Log.v(TAG,":"+sql);
				    OutputStream os = null;
					try {
						os = socket.getOutputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   	try {
						os.write(send);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   	//os.flush();
				   	if(log)Log.v(TAG,"sended");

				   	for(exitTime = 0;exitTime < globalID.getTIMEOUT()*2;exitTime += globalID.getM_rate()){
				       		try {
								Thread.sleep(globalID.getM_rate());
				       			if(log)Log.v(TAG, "sleep");
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				       		if(globalID.isBandW_code()){
				       			if(log)Log.v(TAG, "isBandW_code = " + String.valueOf(globalID.isBandW_code()));
				       			break;
				       		}
//					       		Log.v("bus", "isBandW_code = " + String.valueOf(globalID.isBandW_code()));
//				        	}
					}
//				        	
					if(globalID.isBandW_code()){
//				       		Log.v("bus", "isBandW_code = " + String.valueOf(globalID.isBandW_code()));
						 if(msg != null) msg = new Message();
				         msg.what = Case;
				         globalID.setBandW_code(false);
//			        	 Log.v("err", mDataArrays2.get(0).getBussiness_PostTime());
					 }
					 else{
						 if(msg != null) msg = new Message();
				         msg.what = 4;
					 }
					
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result){
				//notify refresh data
				super.onPostExecute(result);
				final GlobalID globalID = ((GlobalID)getApplication());
				mAdapter.notifyDataSetChanged();
				globalID.mpAdapter.notifyDataSetChanged();
				if(!Insert_Head)mListView.setSelection(globalID.BandWArrays.size()-globalID.getBandWUpdateLines());
				else {
					int select = globalID.BandWArrays.size() < globalID.getBandWUpdateLines() ? 
							globalID.BandWArrays.size() : globalID.getBandWUpdateLines();
					mListView.setSelection(select);
				}
				if(log)Log.v(TAG,"get date finish");
				//complete the refresh picture
				if(Case == 3)((MsgListView) getListView()).onRefreshComplete();
				if(Case == 2)((MsgListView) getListView()).onMoreComplete();

				mhandler.sendMessage(msg);
			}
			
		}
		
//	    /***********Json for bussiness list******************/
//	    private boolean parseJson(String resultObj,boolean Insert_Head){
//	    	if(log)Log.v(TAG,"resultObj: "+resultObj);
//	    	boolean insert = false;
//	    	updateLines = 0;
//			final GlobalID globalID = ((GlobalID)getApplication());
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//						for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//							insert = false;
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							BandWEntity entity = new BandWEntity(
//									jsonObject.optString("list_type").equals("1")
//									, jsonObject.optString("Id")
//									, jsonObject.optString("Title")
//									, jsonObject.optString("Typeid")
//									, Html.fromHtml(jsonObject.optString("Detail")).toString()
//									, jsonObject.optString("Pic")
//									, jsonObject.optString("Time"));
//							if(!entity.getList_type()){
//								String pic = entity.getPicName();
//								if(log)Log.v("pic: " , pic);
//								
//								//
//								Bitmap bit = null;
////								Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/admin/images/" + pic);
//								if(bit!=null){
//									entity.setPic(bit);
//			  					}
//								else{
//		 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.shop);
//									entity.setPic(good);
//		 	  						}
//								
//							}
//							
//							for(int j = mArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getId().equals(mArrays.get(j).getId())
//										&& entity.getList_type() == mArrays.get(j).getList_type()){
//									insert = true;
//									break;
//								}
//							}
//							if(insert)continue;
//							if(Insert_Head)mArrays.add(0,entity);
//							else mArrays.add(entity);
//							updateLines++;
////								insert = false;
////							}
////						}
//					}
//				}
//			}catch(JSONException e){
//				e.printStackTrace();
//				return false;
//			}
//			//set current time for next refresh
////			startTime = mArrays.get(mArrays.size()-1).getBussiness_PostTime();
//			return true;
//		}
//	    /***********Json for bussiness list******************/

	    @Override 
	    public void onSaveInstanceState(Bundle savedInstanceState) {  
	    	// Save away the original text, so we still have it if the activity   
	    	// needs to be killed while paused.
			GlobalID globalID = ((GlobalID)getApplication());
	    	savedInstanceState.putString("BandWArrays", globalID.BandW2String());

//	    	savedInstanceState.putString("mArrays", List2String());
	    	super.onSaveInstanceState(savedInstanceState);
	    	if(log)Log.e(TAG, "onSaveInstanceState");  
	    	}  
	    
	    @Override  
	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
	    	super.onRestoreInstanceState(savedInstanceState);
//			GlobalID globalID = ((GlobalID)getApplication());
//	    	String StrTest = savedInstanceState.getString("BandWArrays"); 
	    	if(log)Log.e(TAG, "onRestoreInstanceState");  
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
	    public void onPause(){
	    	super.onPause();
	    	if(log)Log.v(TAG,"onPause");
	    }
	    
	    @Override
	    public void onStop(){
	    	super.onStop();
	    	if(log)Log.v(TAG,"onStop");
	    }
	    
//	    private String List2String() {
//			// TODO Auto-generated method stub
//	    	BandWEntity entity = new BandWEntity();
//			GlobalID globalID = ((GlobalID)getApplication());
//	    	String encode = "[";
//			for(int i = 0 ; i < globalID.BandWArrays.size();){
//				entity = globalID.BandWArrays.get(i);
//				encode += "{";
//
//				encode += "\"list_type\":\"" + entity.getList_type() + "\";";
//				encode += "\"Id\":\"" + entity.getId() + "\";";
//				encode += "\"Title\":\"" + entity.getTitle() + "\";";
//				encode += "\"Typeid\":\"" + entity.getTypeid() + "\";";
//				encode += "\"Detail\":\"" + entity.getDetail() + "\";";
//				encode += "\"Pic\":\"" + entity.getPicName() + "\";";
//				encode += "\"Time\":\"" + entity.getTime() + "\"";
//				
//				encode += "}";
//				if(++i < globalID.BandWArrays.size()){
//					encode += ",";
//				}
//			}
//			encode += "]";
//			if(log)LogHelper.trace(TAG, encode);
//			return encode;
//		}

	    private void getmArrays(String decode) {
			// TODO Auto-generated method stub
			GlobalID globalID = ((GlobalID)getApplication());
			try{
				JSONArray jsonArray = new JSONArray(decode);
				if(jsonArray.length()==0){
					return;
					}
				else{
					for(int i = 0 ; i < jsonArray.length() ; i++){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						BandWEntity entity = new BandWEntity(
								jsonObject.optString("list_type").equals("1")
								, jsonObject.optString("Id")
								, jsonObject.optString("Title")
								, jsonObject.optString("Typeid")
								, Html.fromHtml(jsonObject.optString("Detail")).toString()
								, jsonObject.optString("Pic")
								, jsonObject.optString("Time"));
						
//						if(jsonObject.optString("isMsg").equals("false")){
//							String pic = jsonObject.optString("detail");
//							GlobalID globalID = ((GlobalID)getApplication());
//							Bitmap bit = downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
//							if(bit!=null){
//								entity.setImage_picture(bit);
//		  					}
//							else{
//	 	  		 				Bitmap default_Img  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//								entity.setImage_picture(default_Img);
//	 	  					}
//						}
						
//						globalID.BandWArrays.add(entity);
						msg = new Message();
						msg.obj = entity;
						add_handler.sendMessage(msg);
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
				return;
			}
		}
}

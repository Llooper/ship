package com.example.ship_old_or_out;
//package com.example.ship2;
//
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import com.example.ship2.MsgListView.OnPullDownListener;
////import com.example.ship2.MsgListView.OnRefreshListener;
//import com.example.ship_Adapter.bussinessAdapter;
//import com.example.ship_Entity.bussinessEntity;
//import com.example.ship_Info.bussinessInfo;
//import com.example.shop_util.FuntionUtil;
//import com.example.shop_util.StringUtil;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.ListActivity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//
//
//public class bussiness_message extends ListActivity{
//	
//	//set background_colour here
//	private String background_colour = "#99CCFF";
//	Context context = bussiness_message.this;
//	InetAddress serverAddr;
//	
//	//test data
////	static int k = 0;
//	
//	private MsgListView mListView ;
//	private bussinessAdapter mAdapter;
//	
//	//no use 2013-12-04
////	private String currentStart = "2013-01-01 00:00:00";
////	private String currentEnd = "3000-12-31 00:00:00";
//	private long exitTime = 0;
//	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//	Message msg = new Message();
//	final Handler mhandler = new Handler(){
//		public void  handleMessage (Message msg){
//			Log.v("bus Thread", "handleMessage: "+Thread.currentThread().getName());
//			GlobalID globalID = ((GlobalID)getApplication());
//			int updateLines = globalID.getBusUpdateLines();
//			switch (msg.what){
//			case 0:
////				Log.v("mhandler", "case 0");
//				globalID.toast(context,"没有连接到服务器");
//				break;
//			case 1:
////				Log.v("mhandler", "case 1");
//				if(updateLines!=0){
//					globalID.toast(context,"该时段最近"+ updateLines +"条新闻信息");
//					break;
//				}
//			case 2:
////				Log.v("mhandler", "case 2");
//				if(updateLines!=0){
//					globalID.toast(context,"刷新"+ updateLines +"条新闻信息");
//					break;
//				}
//			case 3:
////				Log.v("mhandler", "case 3");
//				if(updateLines!=0){
//					globalID.toast(context,"加载"+ updateLines +"条历史新闻");
//					break;
//				}
//			case 4:
////				Log.v("mhandler", "case 4");
//				globalID.toast(context,"没有找到新闻信息");
//				break;
//				}
//		}
//	};
//	
//		@Override 
//		public void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState); 
//
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.msglistview);
//
//		//set Adapter for ListView
//		initView();
//
////		Log.v("bus", Thread.currentThread().getName());
//		}
//		
//		private void initView() {
//			// TODO Auto-generated method stub
//			mListView = (MsgListView)findViewById(android.R.id.list);
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//
//			mListView.setOnItemClickListener(new OnItemClickListener(){
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		                long arg3) {
//					globalID.un_stop = true;
//					//set data to inner activity
//
//					String Title = ((bussinessEntity)globalID.BusDataArrays.get(arg2-1)).getBussiness_Title();
//					String PostTime = ((bussinessEntity)globalID.BusDataArrays.get(arg2-1)).getBussiness_PostTime();
//					String Poster = ((bussinessEntity)globalID.BusDataArrays.get(arg2-1)).getBussiness_Poster();
//					String Content = ((bussinessEntity)globalID.BusDataArrays.get(arg2-1)).getBussiness_detail();
//					
//					//set picture
//					
////					Log.v(TAG, String.valueOf(mDataArrays.size()));
//					Bundle bundle = new Bundle();
//					bundle.putString("Title", Title);
//					bundle.putString("PostTime", PostTime);
//					bundle.putString("Poster", Poster);
//					bundle.putString("Content", Content);
//					bundle.putString("colour", background_colour);
//					Intent intent = new Intent(bussiness_message.this,bussinessInfo.class);
//					
//					//post data
//					intent.putExtras(bundle);
//		            startActivity(intent);
//			}
//			});
//			
//			mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//				@Override
//				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//						final int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					
//					new AlertDialog.Builder(bussiness_message.this).  
//					setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							globalID.BusDeleteArrays.add(globalID.BusDataArrays.get(arg2-1));
//							globalID.BusDataArrays.remove(arg2-1);
//							mAdapter.notifyDataSetChanged();
//							globalID.toast(context, "删除一条新闻信息");
//							}
//						}).show();
//					
//					return true;
//				}
//			});
//			mListView.setBackgroundResource(R.drawable.text_background);
//			
//			mAdapter = new bussinessAdapter(this,globalID.BusDataArrays);
//			mListView.setAdapter(mAdapter);
//			
//
//			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
//			mListView.enableAutoFetchMore(false, 1);
//			
//			mListView.setOnPullDownListener(new OnPullDownListener(){
//				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/
//
//				@Override
//				public void onRefresh() {
//					// TODO Auto-generated method stub
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onRefreshComplete();
//		            }
//					else{
//						refresh();
//					}
//				}
//
//				@Override
//				public void onMore() {
//					// TODO Auto-generated method stub
//					
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onMoreComplete();
//		            }
//					else{
//					GetDataTask bus = new GetDataTask();
//					GlobalID globalID = ((GlobalID)getApplication());
//					if(!globalID.BusDataArrays.isEmpty()){
//						bus.endTime = globalID.BusDataArrays.get(globalID.BusDataArrays.size()-1).getBussiness_PostTime();
//						bus.setId = "<> "+globalID.BusDataArrays.get(globalID.BusDataArrays.size()-1).getId();
//					}
//					else {
//						//no use 2013-12-04
////						bus.endTime = currentStart;
//						bus.endTime = globalID.getStartDate();
//						bus.setId = "<> -1";
//					}
//					Log.v("onmore", "endTime = "+ bus.endTime);
//					bus.startTime = "2013-01-01 00:00:00";
//					bus.Case = 3;
//					bus.Insert_Head = false;
//					bus.DESC = "DESC";
//	                exitTime = System.currentTimeMillis();
//					bus.execute();
//					}
//				}
//			});
//		}
//		
//		//no use 2013-12-04
////		public void onOtherResume(){
////			super.onResume();
////			Log.v("bus","bus_onOtherResume");
////			GlobalID globalID = ((GlobalID)getApplication());
////			if(globalID.isBus_push_UnGet()){
//////				Log.v("bus","bus_push");
////				refresh();
////				globalID.setBus_push_UnGet(false);
////			}
////		}
//		
//		public void refresh(){
//			GetDataTask bus = new GetDataTask();
//			GlobalID globalID = ((GlobalID)getApplication());
//			globalID.setBus_push_UnGet(false);
//			
//			if(!globalID.BusDataArrays.isEmpty()){
//				bus.startTime = globalID.BusDataArrays.get(0).getBussiness_PostTime();
//				bus.setId = "<> "+globalID.BusDataArrays.get(0).getId();
//				Log.v("refresh", "startTime = "+ globalID.BusDataArrays.get(0).getBussiness_PostTime());
//			}
//			else {
////				bus.startTime = currentStart;
//				bus.startTime = globalID.getStartDate();
//				bus.setId = "<> -1";
//			}
////			Calendar eDate = Calendar.getInstance();
//			bus.endTime = s.format(Calendar.getInstance().getTime());
//			bus.Case = 2;
//			bus.Insert_Head = true;
//			bus.DESC = "ASC";
//            exitTime = System.currentTimeMillis();
//			
//			bus.execute();
//		}
//		
//		@Override
//		protected void onResume(){
//			super.onResume();
////			Log.v("bus", Thread.currentThread().getName());
//			//refresh headView...not a good idea
//			mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.getCurrent_code() != 0){
//				return;
//			}
//			Log.v("bus","bus_onResume");
//			
//			if(globalID.isBus_push_UnGet()){
//				Log.v("bus","bus_push_unget");
//				globalID.PD(context, "连接", "正在获取数据…");
//				refresh();
//				
//				//refresh PRIORITY more than change
//				globalID.setBus_change(false);
//			}
//			
//			if(globalID.isBus_change()){
//				globalID.setBus_change(false);
////				Log.v("bus onResume","removeAll");
//				globalID.BusDataArrays.removeAll(globalID.BusDataArrays);
//				
//				GetDataTask bus = new GetDataTask();
//				bus.startTime = globalID.getStartDate();
//				bus.endTime = globalID.getEndDate();
//				bus.Case = 1;
//				bus.Insert_Head = false;
//				bus.setId = "<> -1";
//				bus.DESC = "DESC";
//
//				globalID.PD(context, "连接", "正在获取数据…");
//				bus.execute();
////				Log.v("bus","bus done");
//			}
//			return;
//			
//		}
//		
//		/*********************test data**************************/
////		private void initData(int k) {
////			// TODO Auto-generated method stub
////
////			bussinessEntity entity = new bussinessEntity();
////			entity.setBussiness_detail(String.valueOf(k));
////			Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
////			entity.setBussiness_picture(good);
////			
////			//set the last to the top
////				if(mDataArrays.isEmpty()){
////					mDataArrays.add(0,entity);
////					mDataArrays.add(entity);
////					return;
////					}
////				else{
////					mDataArrays.add(mDataArrays.get(mDataArrays.size()-1));
////					for(int i = mDataArrays.size()-3;i>-1;i--){
////						mDataArrays.set(i+1, mDataArrays.get(i));
////						}
////
////					mDataArrays.set(0,entity);
////					mAdapter.notifyDataSetChanged();
//					//complete the refresh picture
////					((MsgListView) getListView()).onRefreshComplete();
////				}
////		}
//		/*********************test data**************************/
//		
//		private class GetDataTask extends AsyncTask<Void , Void , Void>{
//
//			public String startTime;
//			public String endTime;
//			//set which msg to send
//			public int Case;
//			
//			public boolean Insert_Head;
//			public String setId;
//			//set DESC or ASC
//			public String DESC;
//			
//			@SuppressLint("NewApi")
//			@Override
//			protected synchronized Void doInBackground(Void... params) {
////				Log.v("bus Thread", "doInBackground: "+Thread.currentThread().getName());
//				GlobalID globalID = ((GlobalID)getApplication());
//				synchronized(globalID.BusDataArrays){
//					
//					//no use 2013-12-04
////					try {
////						globalID.BusDataArrays.wait(globalID.getM_rate());
////						Log.v("bus", "first wait");
//////						Thread.sleep(globalID.getM_rate());
////					} catch (InterruptedException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////						Log.v("bus","doInBackground wait err");
////					}
//					// TODO Auto-generated method stub
//					
//					if(msg != null) msg = new Message();
//					try {
//						Socket socket = globalID.getSocket();
//						if(socket != null){
//							try {
//								String sql = "select TOP 5 * from Board "
//									 	+"where PostTime >= #"+startTime
//									 	+"# and PostTime <= #"+endTime
//									    +"# and Id "+setId
//									 	+" order by PostTime "+ DESC + "\0";
//								String sql_send = new String(sql.getBytes("GB2312"),"8859_1");
//								
//								int msg_type_length = 1;
//								int msg_send = sql_send.length();
//								int Len_total = StringUtil.HEAD_LENGTH
//							               +msg_type_length
//							               +msg_send;
//								Log.v("bus","Len_total:"+String.valueOf(Len_total));
//								int Cmd_id = 0x00040006;
//								int Seq_id = 1;
//								int length = 0;
//								byte type = 0x00;
//								
//								if(Insert_Head) Seq_id = 0;
//								
//								byte[] send = new byte[Len_total];
//								length = FuntionUtil.send_head(length, send
//										, StringUtil.Len_total_length, Len_total
//										, StringUtil.Cmd_id_length, Cmd_id
//										, StringUtil.Seq_id_length, Seq_id);
//								
//								for(int i = 0;i<msg_type_length;i++){
//							        send[length] = type;
//							    }
//							    length += msg_type_length;
////								Log.v("length","4:"+String.valueOf(length));
//								for(int i = 0;i<msg_send;i++){
////						            	if(i<Uid.length())
//							    		send[length+i] = (byte)(sql_send.charAt(i));
////						            	else send[length+i] = (byte)(0);
//							    }
//							    length += msg_send;
////								Log.v("length","4:"+String.valueOf(length));
//							    
//								
//								//test
////								for(int i = 0 ;i<15;i++){
////									Log.v("byte",i+"   :"+send[i]);
////								}
//
////								Log.v("startTime",":"+startTime);
////								Log.v("endTime",":"+endTime);
//								Log.v("bus sql",":"+sql);
//							    OutputStream os = socket.getOutputStream();
//							   	os.write(send);
//							   	//os.flush();
//								Log.v("bus","sended");
//							   	
//							   	for(exitTime = 0;exitTime < globalID.getTIMEOUT()*2;exitTime += globalID.getM_rate()){
//									try {
//										globalID.BusDataArrays.wait(globalID.getM_rate());
//										Log.v("bus", "wait");
////						        			wait(globalID.getM_rate());
//									} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//										Log.e("bus", "InterruptedException: "+e);
//									}
//							       	if(globalID.isBus_code()){
//										Log.v("bus", "isBus_code");
////							       		Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
//							       		break;
//							       	}	
////						        	}
//								}
////						        	
//								if(globalID.isBus_code()){
////						       		Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
//					        		 if(msg != null) msg = new Message();
//							         msg.what = Case;
//							         globalID.setBus_code(false);
////					        			 Log.v("err", mDataArrays2.get(0).getBussiness_PostTime());
//								 }
//								 else{
//					        		 if(msg != null) msg = new Message();
//							         msg.what = 4;
////							         mhandler.sendMessage(msg);
//								 }
//							} catch(Exception e) {
//								if(msg != null) msg = new Message();
//				                msg.what = 0;
////				                mhandler.sendMessage(msg);
//							        Log.e("bus", "Exception  "+e);
//							}
//						}
//						else{
//							if(msg != null) msg = new Message();
//			                msg.what = 0;
//							Log.v("bus","socket err");
//						}
//					} catch(Exception e){
//						if(msg != null) msg = new Message();
//		                msg.what = 0;
////		                mhandler.sendMessage(msg);
//						e.printStackTrace();
//						Log.v("bus", "Exception "+e);
//					}
//					return null;
//				}
//			}
//			protected synchronized void onPostExecute(Void result){
////				Log.v("bus","onPostExecute");
//				super.onPostExecute(result);
////				Log.v("bus Thread", "onPostExecute: "+Thread.currentThread().getName());
//				
//				//notify refresh data
//				mAdapter.notifyDataSetChanged();
//				//complete the refresh and more picture
//				if(Case == 2)((MsgListView) getListView()).onRefreshComplete();
//				if(Case == 3)((MsgListView) getListView()).onMoreComplete();
//		        mhandler.sendMessage(msg);
//			}
//		}
//	    
//	    @Override
//	    public void onStop(){
//	    	super.onStop();
//        	Log.v("bus","stop");
//	    }
//	    
//	    @Override
//	    public void onDestroy(){
//	    	super.onDestroy();
//        	Log.v("bus","onDestroy");
//	    }
//}
package com.example.ship_old_or_out;
//package com.example.ship2;
//
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
////import com.example.ship2.MsgListView.OnRefreshListener;
//import com.example.ship2.MsgListView.OnPullDownListener;
//import com.example.ship_Adapter.TestAdapter;
//import com.example.ship_Entity.TestEntity;
//import com.example.ship_Info.testInfo;
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
//import android.widget.ListAdapter;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//
//
//public class Text_information extends ListActivity{
//	
//	private String background_colour = "#99CCFF";
//	Context context = Text_information.this;
//	InetAddress serverAddr;
//	
//	//test data
////	static int k = 0;
//	
//	private MsgListView mListView ;
//	private TestAdapter mAdapter;
//	
////	private String currentStart = "2013-01-01 00:00:00";
////	private String currentEnd = "3000-12-31 00:00:00";
////	private String currentArk = "";
//	private long exitTime = 0;
//	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	
////	private List<TestEntity> mDataArrays = new ArrayList<TestEntity>();
////	private List<TestEntity> mDataArrays2 = new ArrayList<TestEntity>();
//
////	ProgressDialog pd = null;
//
//	Message msg = new Message();
//	final Handler mhandler = new Handler(){
//		public void  handleMessage (Message msg){
//			GlobalID globalID = ((GlobalID)getApplication());
//			int updateLines = globalID.getMsgUpdateLines();
//			switch (msg.what){
//			case 0:
//				globalID.toast(context,"查询错误");
//				break;
//			case 1:
//				if(updateLines>0){
//					globalID.toast(context,"该时段最近"+ updateLines +"条文字信息");
//					break;
//				}
//			case 2:
//				if(updateLines>0){
//					globalID.toast(context,"刷新"+ updateLines +"条文字信息");
//					break;
//				}
//			case 3:
//				if(updateLines>0){
//					globalID.toast(context,"加载"+ updateLines +"条历史文字");
//					break;
//				}
//			case 4:
//				globalID.toast(context,"没有找到文字信息");
//				break;
//				}
//		}
//	};
//		@Override 
//		public void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState); 
//
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.msglistview);
//
//		
//		initView();		
//		}
//
//		private void initView() {
//			// TODO Auto-generated method stub
//			mListView = (MsgListView)findViewById(android.R.id.list);
//			final GlobalID globalID = ((GlobalID)getApplication());
//			mListView.setOnItemClickListener(new OnItemClickListener(){
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		                long arg3) {
//					globalID.un_stop = true;
//					//set data to inner activity
//					String PostTime = ((TestEntity)globalID.MsgDataArrays.get(arg2-1)).getTest_PostTime();
//					String Content = ((TestEntity)globalID.MsgDataArrays.get(arg2-1)).getTest_detail();
//					String test_ark_id = ((TestEntity)globalID.MsgDataArrays.get(arg2-1)).getTest_ark_id();
//					String send_user_id = ((TestEntity)globalID.MsgDataArrays.get(arg2-1)).getSend_user_id();
//					
//					//set picture
//					Bundle bundle = new Bundle();
//					bundle.putString("PostTime", PostTime);
//					bundle.putString("Content", Content);
//					bundle.putString("test_ark_id", test_ark_id);
//					bundle.putString("send_user_id", send_user_id);
//					bundle.putString("colour", background_colour);
//					
//					Intent intent = new Intent(Text_information.this,testInfo.class);
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
//					new AlertDialog.Builder(Text_information.this).  
//					setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							globalID.MsgDeleteArrays.add(globalID.MsgDataArrays.get(arg2-1));
//							globalID.MsgDataArrays.remove(arg2-1);
//							mAdapter.notifyDataSetChanged();
//							globalID.toast(context, "删除一条文字信息");
//							}
//						}).show();
//					
//					return true;
//				}
//			});
//			
////			mListView.setBackgroundColor(android.graphics.Color.parseColor(background_colour));
//			mListView.setBackgroundResource(R.drawable.text_background);
//			
////			mAdapter = new TestAdapter(this,mDataArrays);
//			mAdapter = new TestAdapter(this,globalID.MsgDataArrays);
//			mListView.setAdapter((ListAdapter) mAdapter);
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
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onMoreComplete();
//		            }
//					else{
//						GetDataTask tex = new GetDataTask();
//						GlobalID globalID = ((GlobalID)getApplication());
//						if(!globalID.MsgDataArrays.isEmpty()){
//							tex.endTime = globalID.MsgDataArrays.get(globalID.MsgDataArrays.size()-1).getTest_PostTime();
//							tex.setMsg_id = "<> " + globalID.MsgDataArrays.get(globalID.MsgDataArrays.size()-1).getMsg_id();
//						}
//						else {
//							tex.endTime = globalID.getStartDate();
//							tex.setMsg_id = "<> -1";
//						}
////						Log.v("refresh", "endTime = "+ tex.endTime);
//						tex.startTime = "2013-01-01";
//						tex.Case = 3;
//						tex.Insert_Head = false;
//						tex.DESC = "DESC";
//						exitTime = System.currentTimeMillis();
//						tex.execute();
//					}
////				}
//				}
//			});
//		}
//		
////		public void onOtherResume(){
////			super.onResume();
////			GlobalID globalID = ((GlobalID)getApplication());
////			if(globalID.isMsg_push_UnGet()){
////				Log.v("msg","isMsg_push_UnGet");
////				refresh();
////				globalID.setMsg_push_UnGet(false);
////			}
////		}
//		
//		public void refresh(){
//			GetDataTask tex = new GetDataTask();
//			GlobalID globalID = ((GlobalID)getApplication());
//			globalID.setMsg_push_UnGet(false);
//			
//			if(!globalID.MsgDataArrays.isEmpty()){
//				tex.startTime = globalID.MsgDataArrays.get(0).getTest_PostTime();
//				tex.setMsg_id = "<> " + globalID.MsgDataArrays.get(0).getMsg_id();
//			}
//			else {
//				tex.startTime = globalID.getStartDate();
//				tex.setMsg_id = "<> -1";
//			}
////			Calendar eDate = Calendar.getInstance();
//			tex.endTime = s.format(Calendar.getInstance().getTime());
//			tex.Case = 2;
//			tex.Insert_Head = true;
//			tex.DESC = "ASC";
//            exitTime = System.currentTimeMillis();
//			tex.execute();
//		}
//		
//		@Override
//		protected void onResume(){
//			super.onResume();
//			//refresh headView...not a good idea
//			mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
//			
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.isMsg_push_UnGet()){
//				Log.v("msg","isMsg_push_UnGet");
//				globalID.PD(context,  "连接", "正在获取数据…");
//				refresh();
//				
//				//refresh PRIORITY more than change
//				globalID.setMsg_change(false);
//			}
//			
//			if(globalID.isMsg_change()){
//				globalID.setMsg_change(false);
//
//				globalID.MsgDataArrays.removeAll(globalID.MsgDataArrays);
//				GetDataTask tex = new GetDataTask();
//				tex.startTime = globalID.getStartDate();
//				tex.endTime = globalID.getEndDate();
//				tex.Case = 1;
//				tex.Insert_Head = false;
//				tex.setMsg_id = "<> -1";
//				tex.DESC = "DESC";
//
//				globalID.PD(context,  "连接", "正在获取数据…");
//				tex.execute();
//				mAdapter.notifyDataSetChanged();				
//			}
//			return;
//		}
//		
//		private class GetDataTask extends AsyncTask<Void , Void , Void>{
//
//			public String startTime;
//			public String endTime;
//			//set which msg to send
//			public int Case;
//			public boolean Insert_Head;
//			public String setMsg_id;
//			//set DESC or ASC
//			public String DESC;
//            
//			@SuppressLint("NewApi")
//			@Override
//			synchronized protected Void doInBackground(Void... params) {
//				// TODO Auto-generated method stub
//				GlobalID globalID = ((GlobalID)getApplication());
//				synchronized(globalID.MsgDataArrays){
////					try {
////						globalID.MsgDataArrays.wait(globalID.getM_rate());
////						Log.v("msg", "first wait");
//////						Thread.sleep(globalID.getM_rate());
////					} catch (InterruptedException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////						Log.v("msg","doInBackground wait err");
////					}
//					
//					if(msg != null) msg = new Message();
//					msg.what = 0;
//					Socket socket = globalID.getSocket(); 
//					if(socket != null){
//					    try {
//					        
//					    	String sql = "select TOP 5 * from tbl_msg left join tbl_msg_to_user "
//									+"on tbl_msg.msg_id = tbl_msg_to_user.msg_id "
//									+"where datetime_send >= #"+startTime
//					   			 	+"# and datetime_send <= #"+endTime+"#"
//					   			 	
//					   			 	//use in future
////		                   			+" and tbl_msg_to_user.User_id = '"+globalID.getID() +"'"
//					   			 	
//					   			 	+" and tbl_msg.type <> 0"
//					   			 	+" and tbl_msg.msg_id " + setMsg_id
//					   			 	+" order by datetime_send "+ DESC+"\0";
//					    	String sql_send = new String(sql.getBytes("GB2312"),"8859_1");
//					    	
//					    	int msg_length = 1;
//					    	int msg_send = sql_send.length();
//					    	int Len_total = StringUtil.HEAD_LENGTH
//					                   +msg_length
//					                   +msg_send;
//					    	Log.v("Len_total",":"+String.valueOf(Len_total));
//					    	int Cmd_id = 0x00040006;
//					    	int Seq_id = 1;
//					    	int length = 0;
//					    	byte type = 0x02;
//					    	
//					    	if(Insert_Head) Seq_id = 0;
//					    	
//					    	byte[] send = new byte[Len_total];
//							length = FuntionUtil.send_head(length, send
//									, StringUtil.Len_total_length, Len_total
//									, StringUtil.Cmd_id_length, Cmd_id
//									, StringUtil.Seq_id_length, Seq_id);
//							
////					    	Log.v("length","3:"+String.valueOf(length));
//					    	for(int i = 0;i<msg_length;i++){
//					            send[length] = type;
//					        }
//					        length += msg_length;
////					    	Log.v("length","4:"+String.valueOf(length));
//					    	for(int i = 0;i<msg_send;i++){
////					            	if(i<Uid.length())
//					        		send[length+i] = (byte)(sql_send.charAt(i));
////					            	else send[length+i] = (byte)(0);
//					        }
//					        length += msg_send;
////					    	Log.v("length","4:"+String.valueOf(length));
//					        
//					    	
//					    	//test
////					        	for(int i = 0 ;i<15;i++){
////					        		Log.v("byte",i+"   :"+send[i]);
////					        	}
//
//					    	Log.v("msg sql",":"+sql);
//					    	
//					        OutputStream os = socket.getOutputStream();
//					       	os.write(send);
//
//					    	for(exitTime = 0;exitTime < globalID.getTIMEOUT()*2;exitTime += globalID.getM_rate()){
//					    		try {
////										Thread.sleep(globalID.getM_rate());
//					    			globalID.MsgDataArrays.wait(globalID.getM_rate());
//								} catch (InterruptedException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//					           	if(globalID.isMsg_code()){
//					           		break;
//					           	}	
//					    	}
//					    	
//					    	if(globalID.isMsg_code()){
//					    		if(msg != null) msg = new Message();
//					             msg.what = Case;
//						         globalID.setMsg_code(false);
////					                 mhandler.sendMessage(msg);
//					    	 }
//					    	 else{
//					    		 if(msg != null) msg = new Message();
//					             msg.what = 4;
////					                 mhandler.sendMessage(msg);
//					    	 }
//					    	
//					    } catch(Exception e) {
//					    	e.printStackTrace();
//					    	Log.e("tex", "Exception:", e);
//					    	if(msg != null) msg = new Message();
//			                msg.what = 0;
//					    }
//					}
//					else{
//						Log.v("msg","socket err");
//						if(msg != null) msg = new Message();
//		                msg.what = 0;
//					}
//					return null;
//				}
//			}
//			protected void onPostExecute(Void result){
//				super.onPostExecute(result);
//				mAdapter.notifyDataSetChanged();
//				mhandler.sendMessage(msg);
//				if(Case == 2)((MsgListView) getListView()).onRefreshComplete();
//				if(Case == 3)((MsgListView) getListView()).onMoreComplete();
//			}
//			
//		}
//}

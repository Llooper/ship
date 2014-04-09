package com.example.ship_old_or_out;
//package com.example.ship2;
//
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import com.example.ship2.MsgListView.OnPullDownListener;
//import com.example.ship_Adapter.weatherAdapter;
//import com.example.ship_Entity.weatherEntity;
//import com.example.ship_Info.weatherInfo;
//import com.example.shop_util.FuntionUtil;
//import com.example.shop_util.StringUtil;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.ListActivity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
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
//public class weather extends ListActivity{
//
//	private String background_colour = "#99CCFF";
//	Context context = weather.this;	
//	InetAddress serverAddr;
//	
//	private MsgListView mListView ;
//	private weatherAdapter mAdapter;
//
////	private String currentStart = "2013-01-01";
////	private String currentEnd = "3000-12-31";
//	private long exitTime = 0;
//	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////	ProgressDialog pd = null;
//	
//	Message msg = new Message();
//	final Handler mhandler = new Handler(){
//		public void  handleMessage (Message msg){
//			GlobalID globalID = ((GlobalID)getApplication());
//			int updateLines = globalID.getWeaUpdateLines();
//			switch (msg.what){
//			case 0:
//				globalID.toast(context,"没有连接到服务器");
//				break;
//			case 1:
//				if(updateLines>0){
//					globalID.toast(context,"该时段最近"+ updateLines +"条天气概况");
//					break;
//					}
//			case 2:
//				if(updateLines>0){
//					globalID.toast(context,"刷新"+ updateLines +"条天气概况");
//					break;
//				}
//			case 3:
//				if(updateLines>0){
//					globalID.toast(context,"加载"+ updateLines +"条历史天气");
//					break;
//				}
//			case 4:
//				globalID.toast(context,"没有找到天气信息");
//				break;
//				}
//		}
//	};
//	
//	@Override 
//	public void onCreate(Bundle savedInstanceState) { 
//	super.onCreate(savedInstanceState); 
//
//	//set no title
//	requestWindowFeature(Window.FEATURE_NO_TITLE);
//	setContentView(R.layout.msglistview);
//
//	
//	initView();
//	}
//
//	private void initView() {
//		// TODO Auto-generated method stub
//		mListView = (MsgListView)findViewById(android.R.id.list);
//		
//		final GlobalID globalID = ((GlobalID)getApplication());
//		
//		mListView.setOnItemClickListener(new OnItemClickListener(){
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//	                long arg3) {
//				globalID.un_stop = true;
//				//set data to inner activity
//				Bitmap Weather_picture = ((weatherEntity)globalID.WeaDataArrays.get(arg2-1)).getWeather_picture();
//				
//				String Title = ((weatherEntity)globalID.WeaDataArrays.get(arg2-1)).getTitle();
//				String PostTime = ((weatherEntity)globalID.WeaDataArrays.get(arg2-1)).getPostTime();
//				String Type = ((weatherEntity)globalID.WeaDataArrays.get(arg2-1)).getType();
//				String Content = ((weatherEntity)globalID.WeaDataArrays.get(arg2-1)).getMsg();
//				
////				Log.v(TAG, String.valueOf(mDataArrays.size()));
//				Bundle bundle = new Bundle();
//				
//				if(Weather_picture == null){
//					bundle.putByteArray("Weather_picture", null);
//					}
//				else{
//					ByteArrayOutputStream image_picture = new ByteArrayOutputStream();
//					Weather_picture.compress(Bitmap.CompressFormat.PNG, 100, image_picture);
//					bundle.putByteArray("Weather_picture", image_picture.toByteArray());
//				}
//				
//				bundle.putString("Title", Title);
//				bundle.putString("PostTime", PostTime);
//				bundle.putString("Type", Type);
//				bundle.putString("Content", Content);
//				bundle.putString("colour", background_colour);
//				Intent intent = new Intent(weather.this,weatherInfo.class);
//				
//				//post data
//				intent.putExtras(bundle);
//	            startActivity(intent);
//		}
//		});
//		
//		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					final int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				
//				new AlertDialog.Builder(weather.this).  
//				setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						globalID.WeaDeleteArrays.add(globalID.WeaDataArrays.get(arg2-1));
//						globalID.WeaDataArrays.remove(arg2-1);
//						mAdapter.notifyDataSetChanged();
//						globalID.toast(context, "删除一条天气信息");
//						}
//					}).show();
//				
//				return true;
//			}
//		});
//		
////		mListView.setBackgroundColor(android.graphics.Color.parseColor(background_colour));
//		mListView.setBackgroundResource(R.drawable.text_background);
//		
//		mAdapter = new weatherAdapter(this , globalID.WeaDataArrays);
//		mListView.setAdapter(mAdapter);
//		mListView.setOnPullDownListener(new OnPullDownListener(){
//			/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/
//
//			@Override
//			public void onRefresh() {
//				// TODO Auto-generated method stub
//				if((System.currentTimeMillis()-exitTime) < 2000){
//					((MsgListView) getListView()).onRefreshComplete();
//	            }
//				else{
//					refresh();
//				}
//			}
//
//			@Override
//			public void onMore() {
//				// TODO Auto-generated method stub
//				if((System.currentTimeMillis()-exitTime) < 2000){
//					((MsgListView) getListView()).onMoreComplete();
//	            }
//				else{
//				GetDataTask weather = new GetDataTask();
//				GlobalID globalID = ((GlobalID)getApplication());
//				if(!globalID.WeaDataArrays.isEmpty()){
//					weather.endTime = globalID.WeaDataArrays.get(globalID.WeaDataArrays.size()-1).getPostTime();
//					weather.setId = "<> "+globalID.WeaDataArrays.get(globalID.WeaDataArrays.size()-1).getId();
//				}
//				else {
//					weather.endTime = globalID.getStartDate();
//					weather.setId = "<> -1";
//				}
////				Log.v("onmore", "endTime = "+ weather.endTime);
//				weather.startTime = "2013-01-01";
//				weather.Case = 3;
//				weather.Insert_Head = false;
//				weather.DESC = "DESC";
//                exitTime = System.currentTimeMillis();
//				weather.execute();
//				}
//				
//			}
//		});
//	}
//	
////	public void onOtherResume(){
////		super.onResume();
////		GlobalID globalID = ((GlobalID)getApplication());
////		if(globalID.isWea_push_UnGet()){
////			Log.v("wea onResume","push_get");
////			refresh();
////			globalID.setWea_push_UnGet(false);
////		}
////	}
//	
//	public void refresh(){
//		GetDataTask weather = new GetDataTask();
//		GlobalID globalID = ((GlobalID)getApplication());
//		globalID.setWea_push_UnGet(false);
//		
//		if(!globalID.WeaDataArrays.isEmpty()){
//			weather.startTime = globalID.WeaDataArrays.get(0).getPostTime();
//			weather.setId = "<> "+globalID.WeaDataArrays.get(0).getId();
//			Log.v("refresh", "startTime = "+ globalID.WeaDataArrays.get(0).getPostTime());
//		}
//		else {
//			weather.startTime = globalID.getStartDate();
//			weather.setId = "<> -1";
//		}
//		Calendar eDate = Calendar.getInstance();
//		weather.endTime = s.format(eDate.getTime());  
//		weather.Case = 2;
//		weather.Insert_Head = true;
//		weather.DESC = "ASC";
//        exitTime = System.currentTimeMillis();
//		weather.execute();
//	}
//	
//	@Override
//	protected void onResume(){
//		super.onResume();
//		//refresh headView...not a good idea
//		mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
//		GlobalID globalID = ((GlobalID)getApplication());
//		
//		if(globalID.isWea_push_UnGet()){
//			Log.v("wea onResume","push_get");
//			globalID.PD(context,"连接", "正在获取数据…");
//			refresh();
//			globalID.setWea_change(false);
//		}
//		if(globalID.isWea_change()){
//			globalID.setWea_change(false);
//			if((System.currentTimeMillis()-exitTime) < 2000){
//				return;
//	        }
//			else{
//	            exitTime = System.currentTimeMillis();
//			try{
//				globalID.WeaDataArrays.removeAll(globalID.WeaDataArrays);
//				GetDataTask weather = new GetDataTask();
//				weather.startTime = globalID.getStartDate();
//				weather.endTime = globalID.getEndDate();
//				weather.Case = 1;
//				weather.Insert_Head = false;
//				weather.setId = "<> -1";
//				weather.DESC = "DESC";
//
//				globalID.PD(context,"连接", "正在获取数据…");
//				weather.execute();
//				mAdapter.notifyDataSetChanged();
//			}catch(Exception e){
//				Log.v("weather","Exception" + e);
//				}
//			}
//		}
//		return;
//	}
//	
//	private class GetDataTask extends AsyncTask<Void , Void , Void>{
//
//		public String startTime;
//		public String endTime;
//		//set which msg to send
//		public int Case;
//		public boolean Insert_Head;
//		public String setId;
//		//set DESC or ASC
//		public String DESC;
//		
//		@SuppressLint("NewApi")
//		@Override
//		synchronized protected Void doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			GlobalID globalID = ((GlobalID)getApplication());
//			synchronized(globalID.WeaDataArrays){
////				try {
////					globalID.WeaDataArrays.wait(globalID.getM_rate());
////					Log.v("Wea", "first wait");
//////					Thread.sleep(globalID.getM_rate());
////				} catch (InterruptedException e1) {
////					// TODO Auto-generated catch block
////					e1.printStackTrace();
////					Log.v("Wea","doInBackground wait err");
////				}
//				
//				if(msg != null) msg = new Message();
//				Socket socket = globalID.getSocket();
//				if(socket != null){
//					try {
//						String sql = "select TOP 5 * from goods "
//							 	+"where Posttime >= #"+startTime
//							 	+"# and Posttime <= #"+endTime
//							 	+"# and Id "+setId
//							 	+" order by PostTime "+ DESC + "\0";
//						String sql_send = new String(sql.getBytes("GB2312"),"8859_1");
//						
//						int msg_length = 1;
//						int msg_send = sql_send.length();
//						int Len_total = StringUtil.HEAD_LENGTH
//					               +msg_length
//					               +msg_send;
//						Log.v("Len_total",":"+String.valueOf(Len_total));
//						int Cmd_id = 0x00040006;
//						int Seq_id = 1;
//						int length = 0;
//						byte type = 0x01;
//						
//						if(Insert_Head) Seq_id = 0;
//						
//						byte[] send = new byte[Len_total];
//						length = FuntionUtil.send_head(length, send
//								, StringUtil.Len_total_length, Len_total
//								, StringUtil.Cmd_id_length, Cmd_id
//								, StringUtil.Seq_id_length, Seq_id);
//						
//						for(int i = 0;i<msg_length;i++){
//					        send[length] = type;
//					    }
//					    length += msg_length;
////						Log.v("length","4:"+String.valueOf(length));
//						for(int i = 0;i<msg_send;i++){
////					            	if(i<Uid.length())
//					    		send[length+i] = (byte)(sql_send.charAt(i));
////					            	else send[length+i] = (byte)(0);
//					    }
//					    length += msg_send;
////						Log.v("length","4:"+String.valueOf(length));
//					    
//
//						Log.v("wea sql",":"+sql);
//						
//
//					    OutputStream os = socket.getOutputStream();
//					   	os.write(send);
//
//						for(exitTime = 0;exitTime < globalID.getTIMEOUT()*2;exitTime += globalID.getM_rate()){
//							try {
////									Thread.sleep(globalID.getM_rate());
//								globalID.WeaDataArrays.wait(globalID.getM_rate());
//								Log.v("wea","wait");
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								Log.v("wea","InterruptedException: "+e);
//							}
//					       	if(globalID.isWea_code()){
//								Log.v("wea","isWea_code true");
//					       		break;
//					       	}	
//						}
//						
//					    if(globalID.isWea_code()){
//							Log.v("wea","isWea_code true");
//							if(msg != null) msg = new Message();
//					         msg.what = Case;
//					         globalID.setWea_code(false);
////						         mhandler.sendMessage(msg);
//						 }
//						 else{
//							 if(msg != null) msg = new Message();
//					         msg.what = 4;
////						         mhandler.sendMessage(msg);
//					         }
//					    
//					} catch(Exception e) {
//				        Log.e("weather", "Exception  "+e);
//						if(msg != null) msg = new Message();
//		                msg.what = 0;
//					}
//				}
//				else{
//					Log.v("wea","socket err");
//					if(msg != null) msg = new Message();
//	                msg.what = 0;
//				}
//				return null;
//			}
//		}
//		protected void onPostExecute(Void result){
//			super.onPostExecute(result);
//			//notify refresh data
//			mAdapter.notifyDataSetChanged();
//			mhandler.sendMessage(msg);
//			//complete the refresh picture
//			if(Case == 2)((MsgListView) getListView()).onRefreshComplete();
//			if(Case == 3)((MsgListView) getListView()).onMoreComplete();
//		}
//	}
//}
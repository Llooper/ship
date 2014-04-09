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
//import com.example.ship_Adapter.imageAdapter;
//import com.example.ship_Entity.imageEntity;
//import com.example.ship_Info.imageInfo;
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
//import android.widget.ListAdapter;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//
//
//public class image_information extends ListActivity{
//
//	//set background_colour here
//	private String background_colour = "#99CCFF";
//	Context context = image_information.this;
//	
//	InetAddress serverAddr;
//	
//	//test data
////	static int k = 0;
//	
//	private MsgListView mListView ;
//	private imageAdapter mAdapter;
//	
////	private String currentStart = "2013-01-01";
////	private String currentEnd = "3000-12-31";
//	private long exitTime = 0;
//	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	
////	ProgressDialog pd = null;
//
//	Message msg = new Message();
//	public final Handler mhandler = new Handler(){
//		public void  handleMessage (Message msg){
//			GlobalID globalID = ((GlobalID)getApplication());
//			int updateLines = globalID.getImgUpdateLines();
//			switch (msg.what){
//			case 0:
////				Log.v("mhandler", "case 0");
//				globalID.toast(context,"查询错误");
////				mAdapter.notifyDataSetChanged();
//				break;
//			case 1:
////				Log.v("mhandler", "case 1");
//				if(updateLines>0){
//					globalID.toast(context,"该时段最近"+ updateLines +"条图片信息");
////					mAdapter.notifyDataSetChanged();
//					break;
//				}
//			case 2:
////				Log.v("mhandler", "case 2");
//				if(updateLines>0){
//					globalID.toast(context,"刷新"+ updateLines +"条图片信息");
////					mAdapter.notifyDataSetChanged();
//					break;
//				}
//			case 3:
////				Log.v("mhandler", "case 3");
//				if(updateLines>0){
//					globalID.toast(context,"加载"+ updateLines +"条历史图片");
////					mAdapter.notifyDataSetChanged();
//					break;
//				}
//			case 4:
////				Log.v("mhandler", "case 4");
//				globalID.toast(context,"没有找到图片信息");
////				mAdapter.notifyDataSetChanged();
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
//		//set Adapter for ListView
//		initView();
//		}
//
//		private void initView() {
//			// TODO Auto-generated method stub
//			mListView = (MsgListView)findViewById(android.R.id.list);
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//			
//			mListView.setOnItemClickListener(new OnItemClickListener(){
//				@SuppressLint("ShowToast")
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		                long arg3) {
//					globalID.un_stop = true;
//					//set data to inner activity
//					String ark_id = ((imageEntity)globalID.ImgDataArrays.get(arg2-1)).getImage_ark_id();
//					String Image_datetime_send = ((imageEntity)globalID.ImgDataArrays.get(arg2-1)).getImage_datetime_send();
//					String send_user_id = ((imageEntity)globalID.ImgDataArrays.get(arg2-1)).getSend_user_id();
//
//					//set picture
//					Bitmap Image_picture = ((imageEntity)globalID.ImgDataArrays.get(arg2-1)).getImage_picture();
//					
//					
//					Bundle bundle = new Bundle();
//					if(Image_picture==null){
//						bundle.putByteArray("Image_picture", null);
//						}
//					else{
//						ByteArrayOutputStream image_picture = new ByteArrayOutputStream();
//						Image_picture.compress(Bitmap.CompressFormat.PNG, 100, image_picture);
//						bundle.putByteArray("Image_picture", image_picture.toByteArray());
//					}
//					
////					Log.v(TAG, String.valueOf(mDataArrays.size()));
//					bundle.putString("ark_id", ark_id);
//					bundle.putString("Image_datetime_send", Image_datetime_send);
//					bundle.putString("send_user_id", send_user_id);
//					bundle.putString("colour", background_colour);
//					
//					Intent intent = new Intent(image_information.this,imageInfo.class);
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
//					new AlertDialog.Builder(image_information.this).  
//					setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							globalID.ImgDeleteArrays.add(globalID.ImgDataArrays.get(arg2-1));
//							globalID.ImgDataArrays.remove(arg2-1);
//							mAdapter.notifyDataSetChanged();
//							globalID.toast(context, "删除一条图片信息");
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
//			mAdapter = new imageAdapter(this,globalID.ImgDataArrays);
//			mListView.setAdapter((ListAdapter) mAdapter);
//
//			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
//			mListView.enableAutoFetchMore(false, 1);
//			mListView.setOnPullDownListener(new OnPullDownListener(){
//				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/
//
//				@Override
//				public void onRefresh() {
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
//						GetDataTask img = new GetDataTask();
//						GlobalID globalID = ((GlobalID)getApplication());
//						if(!globalID.ImgDataArrays.isEmpty()){
//							img.endTime = globalID.ImgDataArrays.get(globalID.ImgDataArrays.size()-1).getImage_datetime_send();
//							img.setImg_id = "<> " + globalID.ImgDataArrays.get(globalID.ImgDataArrays.size()-1).getImg_id();
//						}
//						else {
//							img.endTime = globalID.getStartDate();
//							img.setImg_id = "<> -1";
//						}
////						Log.v("refresh", "endTime = "+ img.endTime);
//						img.startTime = "2013-01-01";
//						img.Insert_Head = false;
//						img.Case = 3;
//						img.DESC = "DESC";
//						exitTime = System.currentTimeMillis();
//						img.execute();
//						mAdapter.notifyDataSetChanged();
//					}
////				}
//				}
//			});
//		}
//		
////		public void onOtherResume(){
////			super.onResume();
////			Log.v("img","refresh too much 5");
////			GlobalID globalID = ((GlobalID)getApplication());
////			if(globalID.isImg_push_UnGet()){
////				Log.v("img","refresh too much 6");
////				Log.v("img","isImg_push_UnGet");
////				refresh();
////				globalID.setImg_push_UnGet(false);
////			}
////		}
//		
//		public void refresh(){
//			GetDataTask img = new GetDataTask();
//			GlobalID globalID = ((GlobalID)getApplication());
//			globalID.setImg_push_UnGet(false);
//			
//			if(!globalID.ImgDataArrays.isEmpty()){
//				img.startTime = globalID.ImgDataArrays.get(0).getImage_datetime_send();
//				img.setImg_id = "<> " + globalID.ImgDataArrays.get(0).getImg_id();
//			}
//			else {
//				img.startTime = globalID.getStartDate();
//				img.setImg_id = "<> -1";
//			}
////			Log.v("refresh", "startTime = "+ img.startTime);
//			img.endTime = s.format(Calendar.getInstance().getTime());
//			img.Insert_Head = true;
//			img.Case = 2;
//			img.DESC = "ASC";
//            exitTime = System.currentTimeMillis();
//			img.execute();
//		}
//		
//		@Override
//		protected void onResume(){
//			super.onResume();
//			//refresh headView...not a good idea
//			mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.isImg_push_UnGet()){
//				Log.v("img","isImg_push_UnGet");
//				globalID.PD(context, "连接", "正在获取数据…");
//				refresh();
//				
//				globalID.setImg_change(false);
//			}
//
//			if(globalID.isImg_change()){
//				globalID.setImg_change(false);
//				try{
//					globalID.ImgDataArrays.removeAll(globalID.ImgDataArrays);
////					mDataArrays.removeAll(mDataArrays);
////					mDataArrays2.removeAll(mDataArrays2);
//					GetDataTask img = new GetDataTask();
//					img.startTime = globalID.getStartDate();
//					img.endTime = globalID.getEndDate();
////					img.now_ark_id = currentArk;
//					img.Case = 1;
//					img.Insert_Head = false;
//					img.setImg_id = "<> -1";
//					img.DESC = "DESC";
//
//					globalID.PD(context, "连接", "正在获取数据…");
//					Log.v("img","do execute");
//					img.execute();
//					Log.v("img","after execute");
//					mAdapter.notifyDataSetChanged();
//				}catch(Exception e){
//					Log.v("img","Exception" + e);
//				}
//			}
//			return;
//			
//		}
//		
//		private class GetDataTask extends AsyncTask<Void , Void , Void>{
//
//			public String startTime;
//			public String endTime;
//			public boolean Insert_Head;
//			//set which msg to send
//			public int Case;
//			public String setImg_id;
//			//set DESC or ASC
//			public String DESC;
//			
//			@SuppressLint("NewApi")
//			@Override
//			synchronized protected Void doInBackground(Void... params) {
//				// TODO Auto-generated method stub
//				GlobalID globalID = ((GlobalID)getApplication());
//				synchronized(globalID.ImgDataArrays){
////					try {
////						globalID.ImgDataArrays.wait(globalID.getM_rate());
////						Log.v("img", "first wait");
//////						Thread.sleep(globalID.getM_rate());
////					} catch (InterruptedException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////						Log.v("img","doInBackground wait err");
////					}
//					
//					if(msg != null) msg = new Message();
//					Socket socket = globalID.getSocket();
//					if(socket != null){
//						try {
//							String sql = "select TOP 5 * from tbl_img left join tbl_img_to_user "
//									+"on tbl_img.img_id = tbl_img_to_user.img_id "
//									+"where datetime_send>=#"+startTime
//								 	+"# and datetime_send<=#"+endTime+"#"
//								 	
//								 	//use in future
////			                   		+" and tbl_img_to_user.User_id = '"+globalID.getID() +"'"
//								 	
//								 	+" and tbl_img.type <> 0"
//								 	+" and tbl_img.img_id " + setImg_id
//								 	+" order by datetime_send "+ DESC+"\0";
//							String sql_send = new String(sql.getBytes("GB2312"),"8859_1");
//							
//							int msg_length = 1;
//							int msg_send = sql_send.length();
//							int Len_total = StringUtil.HEAD_LENGTH
//						               +msg_length
//						               +msg_send;
//							Log.v("Len_total",":"+String.valueOf(Len_total));
//							int Cmd_id = 0x00040006;
//							int Seq_id = 1;
//							int length = 0;
//							byte type = 0x03;
//							
//							if(Insert_Head) Seq_id = 0;
//							byte[] send = new byte[Len_total];
//							
//							length = FuntionUtil.send_head(length, send
//									, StringUtil.Len_total_length, Len_total
//									, StringUtil.Cmd_id_length, Cmd_id
//									, StringUtil.Seq_id_length, Seq_id);
//							
//							for(int i = 0;i<msg_length;i++){
//						        send[length] = type;
//						    }
//						    length += msg_length;
////							Log.v("length","4:"+String.valueOf(length));
//							for(int i = 0;i<msg_send;i++){
//								send[length+i] = (byte)(sql_send.charAt(i));
//						    }
//						    length += msg_send;
////							Log.v("length","4:"+String.valueOf(length));
//						    
//							
//							//test
////						        	for(int i = 0 ;i<15;i++){
////						        		Log.v("byte",i+"   :"+send[i]);
////						        	}
//
//							Log.v("img sql",":"+sql);
//							
//						    OutputStream os = socket.getOutputStream();
//						   	os.write(send);
//						   	
//						   	for(exitTime = 0;exitTime < globalID.getTIMEOUT()*2;exitTime += globalID.getM_rate()){
//								try {
////										Thread.sleep(globalID.getM_rate());
//									globalID.ImgDataArrays.wait(globalID.getM_rate());
//								} catch (InterruptedException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//						       	if(globalID.isImg_code()){
//						       		break;
//						       	}	
//							}
//						   	
//						   	if(globalID.isImg_code()){
//						   		if(msg != null) msg = new Message();
//						         msg.what = Case;
//						         globalID.setImg_code(false);
////							         mhandler.sendMessage(msg);
////					        			 Log.v("err", mDataArrays2.get(0).getImage_datetime_send());
//							 }
//							 else{
//								 if(msg != null) msg = new Message();
//						         msg.what = 4;
////							         mhandler.sendMessage(msg);
//							 }
//						} catch(Exception e) {
//							if(msg != null) msg = new Message();
//						    msg.what = 0;
////							    mhandler.sendMessage(msg);
//						    Log.e("err", "S: Error", e);
//						}
//					}
//					else{
//						Log.v("img","socket err");
//						if(msg != null) msg = new Message();
//					    msg.what = 0;
//					}
//					return null;
//				}
//			}
//			
//			protected void onPostExecute(Void result){
//				super.onPostExecute(result);
////				Log.v("img","onPostExecute");
//				mAdapter.notifyDataSetChanged();
//				mhandler.sendMessage(msg);
//				if(Case == 2)((MsgListView) getListView()).onRefreshComplete();
//				if(Case == 3)((MsgListView) getListView()).onMoreComplete();
//			}
//			
//		}
//}

package com.example.ship_old_or_out;
//package com.example.ship_Info;
//
//import java.io.IOException;
//import java.net.UnknownHostException;
//
//import com.example.ship2.GlobalID;
//import com.example.ship2.R;
//import com.example.ship2.R.id;
//import com.example.ship2.R.layout;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ClipData;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.Window;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
////just for show bussiness list in detail
//public class bussinessInfo extends Activity{
//	
//	private static TextView tv_bussinessInfo_Title;
//	private static TextView tv_bussinessInfo_PostTime;
//	private static TextView tv_bussinessInfo_Poster;
//	private static TextView tv_bussinessInfo_Detail;
//	private static LinearLayout ll_bussiness_info;
//	
//	@Override 
//	public void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState);
//		
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.bussiness_info);
//		
//		tv_bussinessInfo_Title = (TextView)findViewById(R.id.tv_bussinessInfo_Title);
//		tv_bussinessInfo_PostTime = (TextView)findViewById(R.id.tv_bussinessInfo_PostTime);
//		tv_bussinessInfo_Poster = (TextView)findViewById(R.id.tv_bussinessInfo_Poster);
//		tv_bussinessInfo_Detail = (TextView)findViewById(R.id.tv_bussinessInfo_Detail);
//		ll_bussiness_info = (LinearLayout)findViewById(R.id.ll_bussiness_info);
//		
//		Intent intent = getIntent();
//		Bundle data = intent.getExtras();
//		final String Title = (String)data.getSerializable("Title");
//		final String PostTime = (String)data.getSerializable("PostTime");
//		final String Poster = (String)data.getSerializable("Poster");
//		final String Content = (String)data.getSerializable("Content");
//		final String Colour = (String)data.getSerializable("colour");
//		
//		ll_bussiness_info.setBackgroundColor(android.graphics.Color.parseColor(Colour));
////		ll_bussiness_info.setBackgroundResource(R.drawable.text_background);
//		tv_bussinessInfo_Title.setText("消息类型: " + Title);
//		tv_bussinessInfo_PostTime.setText("发布时间: "+PostTime);
//		tv_bussinessInfo_Poster.setText("发布者: "+Poster);
//		tv_bussinessInfo_Detail.setText("详细信息: \n"+Content);
//		
//		OnLongClickListener LongClickListener = new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				try {
//					SearchLongProcess(v);
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return false;
//			}
//		
//		
//		private void SearchLongProcess(View v) throws UnknownHostException, IOException {
//			// TODO Auto-generated method stub
//			if (tv_bussinessInfo_Title.equals(v)) {
//				copy(Title);
//			}
//			 if (tv_bussinessInfo_PostTime.equals(v)) {
//				copy(PostTime);
//			}
//			 if (tv_bussinessInfo_Poster.equals(v)) {
//				copy(Poster);
//			}
//			 if (tv_bussinessInfo_Detail.equals(v)) {
//				copy(Content);
//			}
//			 }
//		};
//		
//		OnClickListener clickListener = new OnClickListener(){
// 			public void onClick(View v) {
// 					try {
//						SearchButtonProcess(v);
//					} catch (UnknownHostException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
// 			}
//
//			private void SearchButtonProcess(View v) throws UnknownHostException, IOException {
//				// TODO Auto-generated method stub
//					if (tv_bussinessInfo_Title.equals(v)) {		
//						new_dialog("消息类型",Title);
//						}
//					 if (tv_bussinessInfo_PostTime.equals(v)) {
//						 new_dialog("发布时间",PostTime);
//					}
//					 if (tv_bussinessInfo_Poster.equals(v)) {
//						 new_dialog("发布者",Poster);
//					}
//					 if (tv_bussinessInfo_Detail.equals(v)) {
//						 new_dialog("详细信息",Content);
//					}
//			}
//         };
//         
//         tv_bussinessInfo_Title.setOnClickListener(clickListener); 
//         tv_bussinessInfo_PostTime.setOnClickListener(clickListener); 
//         tv_bussinessInfo_Poster.setOnClickListener(clickListener); 
//         tv_bussinessInfo_Detail.setOnClickListener(clickListener); 
//         
//         tv_bussinessInfo_Title.setOnLongClickListener(LongClickListener);
//         tv_bussinessInfo_PostTime.setOnLongClickListener(LongClickListener);
//         tv_bussinessInfo_Poster.setOnLongClickListener(LongClickListener);
//         tv_bussinessInfo_Detail.setOnLongClickListener(LongClickListener);
//	}
//	
//	private void new_dialog (String title, String content){
//		new AlertDialog.Builder(bussinessInfo.this).  
//		setTitle(title).setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				}  
//			}).show();
//	}
//	
//	private void copy (final String copy){
//		new AlertDialog.Builder(bussinessInfo.this).setPositiveButton("复制", new DialogInterface.OnClickListener() {  
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				GlobalID globalID = ((GlobalID)getApplication());
//		        ClipData textCd = ClipData.newPlainText("", copy);
//		        globalID.getClipboard().setPrimaryClip(textCd);
//				dialog.dismiss();
//				globalID.showCopyed(bussinessInfo.this);
//				}  
//			}).show();
//	}
//}

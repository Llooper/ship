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
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class testInfo extends Activity{
//	
//	private static TextView tv_testInfo_PostTime;
//	private static TextView tv_testInfo_Detail;
//	private static TextView tv_testInfo_Title;
//	private static TextView tv_testInfo_Poster;
//	private static LinearLayout ll_test_info;
//	
//	@Override 
//	public void onCreate(Bundle savedInstanceState) { 
//        final String[] ArkNames = {"��̨118881","��̨12828 ","��ï81888 "};
//        
//		super.onCreate(savedInstanceState);
//		
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.test_info);
//		
//		tv_testInfo_PostTime = (TextView)findViewById(R.id.tv_testInfo_PostTime);
//		tv_testInfo_Detail = (TextView)findViewById(R.id.tv_testInfo_Detail);
//		tv_testInfo_Title = (TextView)findViewById(R.id.tv_testInfo_Title);
//		tv_testInfo_Poster = (TextView)findViewById(R.id.tv_testInfo_Poster);
//		ll_test_info = (LinearLayout)findViewById(R.id.ll_test_info);
//		
//		Intent intent = getIntent();
//		Bundle data = intent.getExtras();
//		final String PostTime = (String)data.getSerializable("PostTime");
//		final String Content = (String)data.getSerializable("Content");
//		final String test_ark_id = (String)data.getSerializable("test_ark_id");
//		final String send_user_id = (String)data.getSerializable("send_user_id");
//		final String Colour = (String)data.getSerializable("colour");
//		
//		ll_test_info.setBackgroundColor(android.graphics.Color.parseColor(Colour));
////		ll_test_info.setBackgroundResource(R.drawable.text_background);
//		tv_testInfo_PostTime.setText("����ʱ��:    \n"+PostTime);
//		tv_testInfo_Detail.setText("��ϸ��Ϣ: "+Content);
//		if(Integer.parseInt(test_ark_id)<1){
//			Log.v("textInfo1", "equals "+test_ark_id);
//			tv_testInfo_Title.setText("�������ϵ���Ϣ");
//		}
////		else {
////			Log.v("textInfo2", "equals "+test_ark_id);
////			tv_testInfo_Title.setText("����:  ("+ test_ark_id + ")  "+ArkNames[Integer.parseInt(test_ark_id)-1]);
////		}
//		tv_testInfo_Poster.setText("������:  " + send_user_id);
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
//			if (tv_testInfo_PostTime.equals(v)) {
//				copy(PostTime);
//			}
//			 if (tv_testInfo_Detail.equals(v)) {
//				copy(Content);
//			}
//			 if (tv_testInfo_Title.equals(v)) {
//					if(Integer.parseInt(test_ark_id)<1||Integer.parseInt(test_ark_id)>3){
//						copy("�������ϵ���Ϣ");
//					}
//					else{
//						copy("("+ test_ark_id + ")"+ArkNames[Integer.parseInt(test_ark_id)-1]);
//					}
//			}
//			 if (tv_testInfo_Poster.equals(v)) {
//				copy(send_user_id);
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
//						Log.v("textInfo err", "UnknownHostException: "+e);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						Log.v("textInfo err", "IOException: "+e);
//					}
// 			}
//
//			private void SearchButtonProcess(View v) throws UnknownHostException, IOException {
//				// TODO Auto-generated method stub
//					if (tv_testInfo_PostTime.equals(v)) {		
//						new_dialog("����ʱ��",PostTime);
//						}
//					 if (tv_testInfo_Detail.equals(v)) {
//						 new_dialog("��ϸ��Ϣ",Content);
//					}
//					 if (tv_testInfo_Title.equals(v)) {
//							if(Integer.parseInt(test_ark_id)<1||Integer.parseInt(test_ark_id)>3){
//								new_dialog("","�������ϵ���Ϣ");
//							}
//							else{
//								new_dialog("����","("+ test_ark_id + ")"+ArkNames[Integer.parseInt(test_ark_id)-1]);
//							}
//					}
//					 if (tv_testInfo_Poster.equals(v)) {
//						 new_dialog("������" , send_user_id);
//					}
//			}
//         };
//         
//         tv_testInfo_PostTime.setOnClickListener(clickListener); 
//         tv_testInfo_Detail.setOnClickListener(clickListener); 
//         tv_testInfo_Title.setOnClickListener(clickListener); 
//         tv_testInfo_Poster.setOnClickListener(clickListener); 
//
//         tv_testInfo_PostTime.setOnLongClickListener(LongClickListener);
//         tv_testInfo_Detail.setOnLongClickListener(LongClickListener);
//         tv_testInfo_Title.setOnLongClickListener(LongClickListener);
//         tv_testInfo_Poster.setOnLongClickListener(LongClickListener);
//	}
//	
//	private void new_dialog (String title, String content){
//		new AlertDialog.Builder(testInfo.this).  
//		setTitle(title).setMessage(content).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				}  
//			}).show();
//	}
//	
//	private void copy (final String copy){
//		new AlertDialog.Builder(testInfo.this).setPositiveButton("����", new DialogInterface.OnClickListener() {  
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				GlobalID globalID = ((GlobalID)getApplication());
//		        ClipData textCd = ClipData.newPlainText("", copy);
//		        globalID.getClipboard().setPrimaryClip(textCd);
//				dialog.dismiss();
//				globalID.showCopyed(testInfo.this);
//				}  
//			}).show();
//	}
//}

package com.example.ship_old_or_out;
//package com.example.ship2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.example.ship_Adapter.SendPreAdapter;
//import com.example.ship_Entity.SendPreEntity;
//
//import android.app.ListActivity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//public class SendPre extends ListActivity{
//	
//	private ListView mListView;
//	private Button btn_pre_send_Cancel;
//	private SendPreAdapter mAdapter;
//	private List<SendPreEntity> mDataArrays = new ArrayList<SendPreEntity>();
//	 @Override
//	 public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//			//set no title
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			
//			setContentView(R.layout.send_pre);
//			
//			mListView = (ListView)findViewById(android.R.id.list);
//			btn_pre_send_Cancel = (Button)findViewById(R.id.btn_pre_send_Cancel);
//			
//			btn_pre_send_Cancel.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					SendPre.this.finish();
//				}
//			});
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//			
//			String[] Send_Id = {"1:发送所有联系人"
//					,"2:发送第一联系人"
//					,"3:发送第二联系人"
//					,"4:发送指定联系人"
//					};
//			for(int i = 0;i<Send_Id.length;i++){
////				ark_id[i] = globalID.getAll().get(i).getArk_id()+": "+globalID.getAll().get(i).getArk_no();
//				SendPreEntity entity = new SendPreEntity();
//				entity.setSendPre_detail(Send_Id[i]);
//				Bitmap hv  = BitmapFactory.decodeResource(getResources(),R.drawable.send_pre_head);
//				entity.setSendPre_picture(hv);
//				mDataArrays.add(entity);
//			}
//			mAdapter = new SendPreAdapter(this,mDataArrays);
//	        mListView.setAdapter(mAdapter);
//	        
//	        
//	        mListView.setOnItemClickListener(new OnItemClickListener(){
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		                long arg3) {
//					globalID.un_stop = true;
//					
//					//set data to inner activity
//					
//					Bundle bundle = new Bundle();
//					bundle.putInt("Send_Id", arg2);
//					Log.v("SendPre","Send_Id:"+arg2);
//					Intent intent = new Intent(SendPre.this,SendActivity.class);
//					
//					//post data
//					intent.putExtras(bundle);
//		            startActivity(intent);
//			}
//			});
//	    }
//	 
//	 @Override
//	 public void onResume(){
//		 super.onResume();
//		 GlobalID globalID = ((GlobalID)getApplication());
//		 if(globalID.is_sended){
//			 globalID.is_sended = false;
//			 this.finish();
//		 }
//	 }
//	 
//	 @Override
//	 public void finish(){
//		 super.finish();
//		 overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//		 }
//}

package com.example.ship_Info;

import java.io.IOException;
import java.net.UnknownHostException;

import com.example.ship2.BandWViewPagerActivity;
import com.example.ship2.GlobalID;
import com.example.ship2.NewMainActivity;
import com.example.ship2.R;
import com.example.ship_Entity.BandWEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


//just for show BandW list in detail
public class BandWInfo extends Activity{
	
	private static String TAG = "BandWInfo";
	private static boolean log = true;
	private static TextView tv_BandWInfo_Title;
	private static TextView tv_BandWInfo_PostTime;
	private static TextView tv_BandWInfo_Detail;
	private static LinearLayout ll_BandW_info;
	private static ImageView iv_BandWInfo_view;
	private static TextView talk_title;
	private static ImageButton title_bar_back,title_bar_gps;
	
	private int index = 0;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.item_in, R.anim.list_out);
		
		//set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bandw_info);
		tv_BandWInfo_Title = (TextView)findViewById(R.id.tv_BandWInfo_Title);
		tv_BandWInfo_PostTime = (TextView)findViewById(R.id.tv_BandWInfo_PostTime);
		tv_BandWInfo_Detail = (TextView)findViewById(R.id.tv_BandWInfo_Detail);
		ll_BandW_info = (LinearLayout)findViewById(R.id.ll_BandW_info);
		iv_BandWInfo_view = (ImageView)findViewById(R.id.iv_BandWInfo_view);
		setTitle();
		
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		index = data.getInt("index");
		final String Colour = (String)data.getSerializable("colour");
		
		try{

			GlobalID globalID = ((GlobalID)getApplication());
			final BandWEntity entity = globalID.BandWArrays.get(index);
			
			ll_BandW_info.setBackgroundColor(android.graphics.Color.parseColor(Colour));
			tv_BandWInfo_Title.setText("消息类型: " + entity.getTitle());
			tv_BandWInfo_PostTime.setText("发布时间: "+entity.getTime());
			tv_BandWInfo_Detail.setText("详细信息: \n"+entity.getDetail());
			if(entity.getList_type()){
				iv_BandWInfo_view.setVisibility(View.GONE);
			}
			else iv_BandWInfo_view.setImageBitmap(entity.getPic());
			
			OnLongClickListener LongClickListener = new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					try {
						SearchLongProcess(v);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				}
			
			
			private void SearchLongProcess(View v) throws UnknownHostException, IOException {
				// TODO Auto-generated method stub
				if (tv_BandWInfo_Title.equals(v)) {
					copy(entity.getTitle());
				}
				 if (tv_BandWInfo_PostTime.equals(v)) {
					copy(entity.getTime());
				}
				 if (tv_BandWInfo_Detail.equals(v)) {
					copy(entity.getDetail());
				}
				 }
			};
	     
			
			OnClickListener clickListener = new OnClickListener(){
	 			@Override
				public void onClick(View v) {
	 					try {
							SearchButtonProcess(v);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	 			}

				private void SearchButtonProcess(View v) throws UnknownHostException, IOException {
					// TODO Auto-generated method stub
						if (tv_BandWInfo_Title.equals(v)) {
							new_dialog("消息类型",entity.getTitle());
							}
						 if (tv_BandWInfo_PostTime.equals(v)) {
							 new_dialog("发布时间",entity.getTime());
						}
						 if (tv_BandWInfo_Detail.equals(v)) {
							 new_dialog("详细信息",entity.getDetail());
						}
						 if(iv_BandWInfo_view.equals(v)){
								Bundle bundle = new Bundle();
								bundle.putInt("i", index);
								GlobalID globalID = ((GlobalID)getApplication());
								globalID.un_stop = true;
								Intent intent = new Intent(BandWInfo.this,BandWViewPagerActivity.class);
								//post data
								intent.putExtras(bundle);
					            startActivity(intent);
						 }
				}
	         };
	         
	         tv_BandWInfo_Title.setOnClickListener(clickListener);
	         tv_BandWInfo_PostTime.setOnClickListener(clickListener);
	         tv_BandWInfo_Detail.setOnClickListener(clickListener);
	         iv_BandWInfo_view.setOnClickListener(clickListener);
	         
	         tv_BandWInfo_Title.setOnLongClickListener(LongClickListener);
	         tv_BandWInfo_PostTime.setOnLongClickListener(LongClickListener);
	         tv_BandWInfo_Detail.setOnLongClickListener(LongClickListener);
		}catch(Exception e){
			if(log)Log.e(TAG,"create Exception: " + e);
			this.finish();
		}
	}
	
	private void setTitle() {
		// TODO Auto-generated method stub
				// TODO Auto-generated method stub
		talk_title = (TextView)findViewById(R.id.talk_title);
	    talk_title.setText(R.string.info_title);
	    title_bar_back = (ImageButton)findViewById(R.id.title_bar_back);
	    title_bar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    title_bar_gps = (ImageButton)findViewById(R.id.title_bar_gps);
	    title_bar_gps.setVisibility(View.INVISIBLE);
	}

	private void new_dialog (String title, String content){
		new AlertDialog.Builder(BandWInfo.this).  
		setTitle(title).setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				}  
			}).show();
	}
	
	private void copy (final String copy){
		new AlertDialog.Builder(BandWInfo.this).setPositiveButton("复制", new DialogInterface.OnClickListener() {  
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GlobalID globalID = ((GlobalID)getApplication());
				
				// 判断API>=11
				if(Build.VERSION.SDK_INT >= 11){
			        ClipData textCd = ClipData.newPlainText("", copy);
			        globalID.getClipboard().setPrimaryClip(textCd);
				}
				else {
					ClipboardManager clipboard ;
			        clipboard = (ClipboardManager)getSystemService(BandWInfo.CLIPBOARD_SERVICE);
			        clipboard.setText(copy);
				}
				
				dialog.dismiss();
				globalID.showCopyed(BandWInfo.this);
				}  
			}).show();
	}
	
	 @Override
	 public void onResume(){
		 super.onResume();
		 GlobalID globalID = ((GlobalID)getApplication());
		 globalID.cancel_notification();
		 globalID.un_stop = false;
	}

	@Override 
    public void onSaveInstanceState(Bundle savedInstanceState) {  
    	// Save away the original text, so we still have it if the activity   
    	// needs to be killed while paused.
    	super.onSaveInstanceState(savedInstanceState);  
    	if(log)Log.e(TAG, "onSaveInstanceState");

    	GlobalID globalID = ((GlobalID)getApplication());
    	if(globalID.un_stop)return;
    	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false
    			,BandWInfo.class.getName());
	    if(globalID.toast != null)globalID.toast.cancel();
    	}
	

	@Override
	 public void finish(){
		 super.finish();
		 if(log)Log.v(TAG,"finish");
		 overridePendingTransition(R.anim.list_in, R.anim.item_out);
//		 GlobalID globalID = ((GlobalID)getApplication());
//		 globalID.un_stop = true;
	 }
}

package com.example.ship_old_or_out;
//package com.example.ship_Info;
//
//import java.io.IOException;
//import java.net.UnknownHostException;
//
//import com.example.ship2.DetailPicture;
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
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class weatherInfo extends Activity{
//
//	private static ImageView iv_weatherInfo_picture;
//	private static TextView tv_weatherInfo_Title;
//	private static TextView tv_weatherInfo_PostTime;
//	private static TextView tv_weatherInfo_Type;
//	private static TextView tv_weatherInfo_Detail;
//	private static LinearLayout ll_weather_info;
//	
//	@Override 
//	public void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState);
//		
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.weather_info);
//
//		
//		tv_weatherInfo_Title = (TextView)findViewById(R.id.tv_weatherInfo_title);
//		tv_weatherInfo_PostTime = (TextView)findViewById(R.id.tv_weatherInfo_posttime);
//		tv_weatherInfo_Type = (TextView)findViewById(R.id.tv_weatherInfo_type);
//		tv_weatherInfo_Detail = (TextView)findViewById(R.id.tv_weatherInfo_msg);
//		ll_weather_info = (LinearLayout)findViewById(R.id.ll_weather_info);
//		iv_weatherInfo_picture = (ImageView)findViewById(R.id.iv_weatherInfo_view);
//		
//		Intent intent = getIntent();
//		Bundle data = intent.getExtras();
//		final byte[]  Weather_picture = (byte[])data.getSerializable("Weather_picture");
//		final String Title = (String)data.getSerializable("Title");
//		final String PostTime = (String)data.getSerializable("PostTime");
//		final String Type = (String)data.getSerializable("Type");
//		final String Content = (String)data.getSerializable("Content");
//		final String Colour = (String)data.getSerializable("colour");
//
//		ll_weather_info.setBackgroundColor(android.graphics.Color.parseColor(Colour));
////		ll_weather_info.setBackgroundResource(R.drawable.text_background);
//		tv_weatherInfo_Title.setText("天气标题: " + Title);
//		tv_weatherInfo_PostTime.setText("发布时间: "+PostTime);
//		tv_weatherInfo_Type.setText("区域类型: "+Type);
//		tv_weatherInfo_Detail.setText("详细信息: \n"+Content);
//		if(Weather_picture != null)iv_weatherInfo_picture.setImageBitmap(Bytes2Bimap(Weather_picture));
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
//			if (tv_weatherInfo_Title.equals(v)) {
//				copy(Title);
//			}
//			 if (tv_weatherInfo_PostTime.equals(v)) {
//				copy(PostTime);
//			}
//			 if (tv_weatherInfo_Type.equals(v)) {
//				copy(Type);
//			}
//			 if (tv_weatherInfo_Detail.equals(v)) {
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
//				if(iv_weatherInfo_picture.equals(v)){
//					Bundle bundle = new Bundle();
//					bundle.putByteArray("Image_picture", Weather_picture);
//					Intent intent = new Intent(weatherInfo.this,DetailPicture.class);
//					//post data
//					intent.putExtras(bundle);
//		            startActivity(intent);
//				}
//					if (tv_weatherInfo_Title.equals(v)) {		
//						new_dialog("消息类型",Title);
//						}
//					 if (tv_weatherInfo_PostTime.equals(v)) {
//						 new_dialog("发布时间",PostTime);
//					}
//					 if (tv_weatherInfo_Type.equals(v)) {
//						 new_dialog("发布类型",Type);
//					}
//					 if (tv_weatherInfo_Detail.equals(v)) {
//						 new_dialog("详细信息",Content);
//					}
//			}
//         };
//         
//         tv_weatherInfo_Title.setOnClickListener(clickListener); 
//         tv_weatherInfo_PostTime.setOnClickListener(clickListener); 
//         tv_weatherInfo_Type.setOnClickListener(clickListener); 
//         tv_weatherInfo_Detail.setOnClickListener(clickListener); 
//         iv_weatherInfo_picture.setOnClickListener(clickListener);
//         
//         tv_weatherInfo_Title.setOnLongClickListener(LongClickListener);
//         tv_weatherInfo_PostTime.setOnLongClickListener(LongClickListener);
//         tv_weatherInfo_Type.setOnLongClickListener(LongClickListener);
//         tv_weatherInfo_Detail.setOnLongClickListener(LongClickListener);
//	}
//	
//	private void new_dialog (String title, String content){
//		new AlertDialog.Builder(weatherInfo.this).  
//		setTitle(title).setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				}  
//			}).show();
//	}
//	
//	public Bitmap Bytes2Bimap(byte[] b) {
//        if (b.length != 0) {
//            return BitmapFactory.decodeByteArray(b, 0, b.length);
//        } else {
//            return null;
//        }
//    }
//
//	private void copy (final String copy){
//		new AlertDialog.Builder(weatherInfo.this).setPositiveButton("复制", new DialogInterface.OnClickListener() {  
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				GlobalID globalID = ((GlobalID)getApplication());
//		        ClipData textCd = ClipData.newPlainText("", copy);
//		        globalID.getClipboard().setPrimaryClip(textCd);
//				dialog.dismiss();
//				globalID.showCopyed(weatherInfo.this);
//				}  
//			}).show();
//	}
//}

package com.example.ship2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.ship2.Tools;
import com.example.shop_util.FuntionUtil;
import com.example.shop_util.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SendActivity extends ContactActivity{
	private static final boolean log = false;
	private static final String TAG = "SendActivity";
    private Vibrator vibrator=null;
	Context context = SendActivity.this;
	SimpleDateFormat s=new SimpleDateFormat("yyMMddHHmmss");
	
	//define spinner for kind
//	private Spinner 
//	sp_send_Id,
//	sp_send_level;
	
	//define other data
//	private static TextView tv_send_Title;
	private ImageView iv_send_HeadPhoto;
	private EditText et_send_Id,et_send_Detail;
	private static Button btn_send_search,send_btn;
	private static TextView send_title;
	private static ImageButton send_title_bar_back;
//	private static tryscroll sv_send_sv;
//	private static InnerScrollView sv_send_Detail;
	
	/*************Picture***************/
	private String[] items = new String[] {
			"选择本地图片" , "拍照"
	};
	
	private static final String IMAGE_FILE_NAME = "/faceImage.jpg";
	
	private static final int IMAGE_REQUEST_CODE = 0 ;
	private static final int CAMERA_REQUEST_CODE = 1 ;
	private static final int RESULT_REQUEST_CODE = 2 ;
	/**************Picture**************/
	
	//is any picture here
//	private static boolean 	no_picture = true;
	//get send_id
	private int Send_Id = -1;
	
	private int MSG_LEVEL = 11;
//	String[] Send_level = {"特急"
//			,"加急"
//			,"尽快"
//			,"正常"
//			,"可缓"
//			};

//	ProgressDialog pd = null;

	Message msg = new Message();
	@SuppressLint("HandlerLeak")
	final Handler mhandler = new Handler(){
		public void  handleMessage (Message msg){
			final GlobalID globalID = ((GlobalID)getApplication());
			send_btn.setClickable(true);
			switch (msg.what){
			case -3:
//				if(log)Log.v("mhandler", "case -3");
				Dialog("错误","发送失败，连接服务器失败",-3);
				break;
			case -2:
//				if(log)Log.v("mhandler", "case -2");
				Dialog("错误","发送失败1",-2);
				break;
			case -1:
//				if(log)Log.v("mhandler", "case -1");
				Dialog("错误","发送失败",-1);
			case 0:
//				if(log)Log.v("mhandler", "case 0");
				Dialog("成功","成功发送所有信息",0);
				globalID.is_sended = true;
				break;
			case 1:
//				if(log)Log.v("mhandler", "case 1");
				Dialog("成功","成功发送图片信息",1);
				globalID.is_sended = true;
				break;
			case 2:
//				if(log)Log.v("mhandler", "case 2");
				Dialog("成功","成功发送文字信息",2);
				globalID.is_sended = true;
				break;
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		//set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.send);
		final GlobalID globalID = (GlobalID)getApplication();
		if(null!= savedInstanceState){
			globalID.start();
			Send_Id = savedInstanceState.getInt("Send_Id");
			MSG_LEVEL = savedInstanceState.getInt("MSG_LEVEL");
		}
		else{
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			if(!data.isEmpty()){
				Send_Id = (Integer)data.getSerializable("Send_Id");
				MSG_LEVEL = (Integer)data.getSerializable("MSG_LEVEL");
			}
		}
		//set all items
//		sp_send_Id = (Spinner)findViewById(R.id.sp_send_Id);
//		sp_send_level = (Spinner)findViewById(R.id.sp_send_level);
		setTitle();
//		tv_send_Title = (TextView)findViewById(R.id.tv_send_Title);
		iv_send_HeadPhoto = (ImageView)findViewById(R.id.iv_send_HeadPhoto);
		et_send_Id = (EditText)findViewById(R.id.et_send_Id);
		et_send_Detail = (EditText)findViewById(R.id.et_send_Detail);
//		send_btn = (Button)findViewById(R.id.send_btn);
		btn_send_search = (Button)findViewById(R.id.btn_send_search);
//		btn_send_Cancel = (Button)findViewById(R.id.btn_send_Cancel);
//		sv_send_sv = (tryscroll)findViewById(R.id.sv_send_sv);

		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		
		//no ScrollBar
//		sv_send_sv.setVerticalScrollBarEnabled(false);
		
		//let title get focus
//		tv_send_Title.setFocusableInTouchMode(true);

		edtSelectedContact = this.et_send_Id;
		imvSelectedContact = this.iv_send_HeadPhoto;
		
		iv_send_HeadPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				globalID.un_stop = false;
				showDialog();
			}
		});


		//get window width
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();

		et_send_Detail.setWidth(display.getWidth());
		
		et_send_Detail.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				//do something when press ENTER
				if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
					if(log)Log.v("onEditorAction","KeyEvent.KEYCODE_ENTER");
					return true;
				}
				else return false;
			}
            });
		
		//set button listener
		send_btn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				send();
			}
		});
		
//		btn_send_Cancel.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				SendActivity.this.finish();
//			}
//		});
		
		btn_send_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startContact();
			}
		});
		
		//set spinner
//		String[] Send_Id = {"1:发送所有联系人"
//				,"2:发送第一联系人"
//				,"3:发送第二联系人"
//				,"4:发送指定联系人"
//				};
		
		
		
		//android.R.layout.browser_link_context_header no use??
//		SpinnerAdapter aa_id = new SpinnerAdapter(this,android.R.layout.two_line_list_item,Send_Id);
//		SpinnerAdapter aa_level = new SpinnerAdapter(this,android.R.layout.simple_dropdown_item_1line,Send_level);
		
//		sp_send_Id.setAdapter(aa_id);
//		sp_send_level.setAdapter(aa_level);
	}
	
	
	private void setTitle() {
		// TODO Auto-generated method stub
				// TODO Auto-generated method stub
		GlobalID globalID = (GlobalID)getApplication();
		send_title = (TextView)findViewById(R.id.send_title);
	    send_title.setText(R.string.send_msg);
	    send_title_bar_back = (ImageButton)findViewById(R.id.send_title_bar_back);
	    send_title_bar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    send_btn = (Button)findViewById(R.id.send_btn);
	}


	/*********************Spinner Settings**************************/
	private class SpinnerAdapter extends ArrayAdapter<String> {

	    Context context;
	    String[] items = new String[] {};

	    public SpinnerAdapter(final Context context,
	            final int textViewResourceId, final String[] objects) {
	        super(context, textViewResourceId, objects);
	        this.items = objects;
	        this.context = context;
	    }


	    @Override
	    public View getDropDownView(int position, View convertView,
	            ViewGroup parent) {
	        if (convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(context);
	            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
	        }

	        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
	        tv.setText(items[position]);
	        tv.setTextColor(Color.BLUE);
	        tv.setTextSize(40);
	        return convertView;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        if (convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(context);
	            convertView = inflater.inflate(
	                    android.R.layout.simple_spinner_item, parent, false);
	        }

	        // android.R.id.text1 is default text view in resource of the android.

	        // android.R.layout.simple_spinner_item is default layout in resources of android.

	        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
	        tv.setText(items[position]);
	        tv.setTextColor(Color.RED);
	        tv.setTextSize(18);
	        return convertView;
	    }
	}
	/*********************Spinner Settings**************************/

	
	
	
	/*********************Picture*********************************/
	private void showDialog(){
		new AlertDialog.Builder(this).setTitle("选择图片").setItems(items, new
				DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0 :
							
							//跳转相册
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*");
							//catch nothing
							if(intentFromGallery.setAction(Intent.ACTION_GET_CONTENT)==null)break;
							
							startActivityForResult(intentFromGallery , IMAGE_REQUEST_CODE );
							break;
						case 1:
							
							//跳转摄像机
							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							if(Tools.hasSdcard()){
								intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT
										,Uri.fromFile(new File(Environment.getExternalStorageDirectory()
												,IMAGE_FILE_NAME)) );
							}
							startActivityForResult(intentFromCapture , CAMERA_REQUEST_CODE);
							break;
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
	}
	

//	protected void onActivityResult(int requestCode , int resultCode , Intent data){
//		if(resultCode != RESULT_CANCELED){
//		switch (requestCode){
//		case IMAGE_REQUEST_CODE:
//			startPhotoZoom(data.getData());
//			break;
//		case CAMERA_REQUEST_CODE:
//			if(Tools.hasSdcard()){
//				File tempFile = new File(
//						Environment.getExternalStorageDirectory() + IMAGE_FILE_NAME);
//				startPhotoZoom(Uri.fromFile(tempFile));
////				startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
//			}else{
//				//we use this in my project
//				final GlobalID globalID = ((GlobalID)getApplication());
//				globalID.toast(context, "未找到存储卡，无法存储照片");
//				
////				Toast.makeText(SendActivity.this, "未找到存储卡，无法存储照片", Toast.LENGTH_LONG).show();
//			}
//			break;
//		case RESULT_REQUEST_CODE:
//			if(data != null){
//				getImageToView(data);
//			}
//			break;
//		}
//	}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//	
//	public void startPhotoZoom(Uri uri){
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		
//		intent.putExtra("crop", true);
//		intent.putExtra("aspectX", 0.1);
//		intent.putExtra("aspectY", 0.1);
//		
//		intent.putExtra("outputX", 90);
//		intent.putExtra("outputY", 90);	
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent , 2);
//	}
//
//	private void getImageToView(Intent data){
//		Bundle extras = data.getExtras();
//		if(extras != null ){
//			Bitmap photo = extras.getParcelable("data");
//			@SuppressWarnings("deprecation")
//			Drawable drawable = new BitmapDrawable(photo);
//			iv_send_HeadPhoto.setImageDrawable(drawable);
//			if(log)Log.v(TAG,"has picture");
//			no_picture = false;
//		}
//	}
	/*********************Picture*********************************/
	

	/****************compress Image*****************/
	private Bitmap comp(Bitmap image) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		if( baos.toByteArray().length / 1024>100) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
//		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//			be = (int) (newOpts.outWidth / ww);
//		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//			be = (int) (newOpts.outHeight / hh);
//		}
		be = (int) ((w / ww + h/ hh) / 2);
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		newOpts.inPreferredConfig = Config.RGB_565;
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	
	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			options -= 10;//每次都减少10
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	/****************compress Image*****************/
	
	byte[] HexStrToBin(byte[] hexStr)
	{
		int len = hexStr.length;
		byte[] bin = new byte[len/2];
		if(null == bin || null == hexStr) {
			return bin;
		}
		int lenBin = 0;
		int index = 0;
		while(index<len) {
			bin[lenBin] = (byte) (hexStr[index]-(hexStr[index]<'A'?'0':hexStr[index]<'a'?'A'-10:'a'-10));
			bin[lenBin] = (byte) ((bin[lenBin] << 4) & 0xF0);
			++index;
			
			bin[lenBin] += hexStr[index]-(hexStr[index]<'A'?'0':hexStr[index]<'a'?'A'-10:'a'-10);
			++index;
			
			++lenBin;
			if(log)Log.v(TAG, "HexStrToBin for index: "+String.valueOf(index));
		}
		if(log)Log.v(TAG, "HexStrToBin done.");

		return bin;
	}
	
	void Dialog(String title,String msg,final int i){
		final GlobalID globalID = ((GlobalID)getApplication());
		globalID.cancelPD();
		new AlertDialog.Builder(SendActivity.this).
		 	setTitle(title).setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int which) {
				 dialog.dismiss();
				 if(i>-1){
					 SendActivity.this.finish();
				 }
				 }
			 }).show();
	}
	
	int send_body_first(int length,byte[] send,int time_send_length,byte[] time_send2
						,int senderId_length,int senderMobile_length
						,int senderMobileLen_length,String senderMobile,int grade
						,int grade_length,short option,int option_length,int cntRecver
						,int cntRecver_length,int recverLen,int recverLen_length
						,int recver_length,String send_id,int cntGrp_length
						,int data_type,int data_type_length,int len_data_length
						,int data_length){
		for(int i = 0;i<time_send_length;i++){
        	send[length + i] = time_send2[i];
//        	if(log)Log.v("length",String.valueOf(i)+" time_send16:"+Integer.toHexString(send[length + i]));
//        	if(log)Log.v("length",String.valueOf(i)+" time_send2:"+Integer.toBinaryString(send[length + i]));
//        	if(log)Log.v("length",String.valueOf(i)+" time_send10:"+Integer.toOctalString(send[length + i]));
//        	if(log)Log.v("length",String.valueOf(i)+" time_send:"+String.valueOf(send[length + i]));
        }
        length += time_send_length;
    	if(log)Log.v("length","time_send_length:"+String.valueOf(length));
    	
    	
        for(int i = 0;i<senderId_length;i++){
        	send[length+i] = (byte)(0 >> 24-i*8);
        }
        length += senderId_length;
//    	if(log)Log.v("length","5:"+String.valueOf(length));
    	
    	send[length] = (byte)senderMobile_length;
        length += senderMobileLen_length;
        if(log)Log.v(TAG, "mobile_length: " + String.valueOf(senderMobile_length));
//    	if(log)Log.v("length","6:"+String.valueOf(length));
    	
        for(int i = 0;i<senderMobile_length;i++){
        	send[length+i] = (byte) senderMobile.charAt(i);
        }
        length += senderMobile_length;
        if(log)Log.v(TAG, "mobile: " + senderMobile);
//    	if(log)Log.v("length","7:"+String.valueOf(length));
    	
    	send[length] = (byte)grade;
        length += grade_length;
        if(log)Log.v(TAG, "grade_length: " + String.valueOf(grade));
//    	if(log)Log.v("length","8:"+String.valueOf(length));

    	send[length] = (byte) ((option >> 8) & 0xff);
        send[length + 1] = (byte) (option & 0xff);
        if(log)Log.v(TAG, "option: " + String.valueOf(send[length]));
//        if(log)Log.v(TAG, "option1: " + String.valueOf(send[length+1]));
        length += option_length;
//    	if(log)Log.v("length","9:"+String.valueOf(length));
		
        send[length] = (byte) cntRecver;
        length += cntRecver_length;
//    	if(log)Log.v("length","10:"+String.valueOf(length));
    	
    	send[length] = (byte) recverLen;
    	length += recverLen_length;
//    	if(log)Log.v("length","11:"+String.valueOf(length));
    	
    	for(int i = 0;i<recver_length;i++){
        	send[length+i] = (byte) send_id.charAt(i);
        }
    	length += recver_length;
    	if(log)Log.v("length","recver_length:"+String.valueOf(recver_length));
		
    	send[length] = 0;
    	length += cntGrp_length;
//    	if(log)Log.v("length","12:"+String.valueOf(length));
    	
    	send[length] = (byte) data_type;
    	length += data_type_length;
//    	if(log)Log.v("length","13:"+String.valueOf(length));

        for(int i = 0;i<len_data_length;i++){
            send[length+i] = (byte)(data_length >> 24-i*8);
        }
        length += len_data_length;
        if(log)Log.v(TAG, "data_length: " + String.valueOf(data_length));
//    	if(log)Log.v("length","14:"+String.valueOf(length));
		return length;
	}
	
	int byte2int (int answer,byte[] line){
		answer = line[15] & 0xFF;
        answer |= ((line[14] << 8) & 0xFF00);
        answer |= ((line[13] << 16) & 0xFF0000);
        answer |= ((line[12] << 24) & 0xFF000000);
		return answer;
	}
	
	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
	
	private void send() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		send_btn.setClickable(false);
		final String send_id1;
//		send_btn.setBackgroundColor(android.graphics.Color.parseColor("#FE9B21"));
		send_id1 = new String(et_send_Id.getText().toString());
		
		Log.e("sendid",send_id1);

		final GlobalID globalID = ((GlobalID)getApplication());
		
		if(send_id1.equals(globalID.getID())){
			new AlertDialog.Builder(SendActivity.this).  
			setTitle("错误").setMessage("不能发送信息给自己").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {
					send_btn.setClickable(true);
					dialog.dismiss();
					et_send_Id.requestFocus();
					}  
				}).show();
		}
		
		else{

        	int pre_kind = 0;
			if(no_picture && et_send_Detail.getText().toString().length() == 0){
				globalID.toast(context, "没有要发送的信息");
				et_send_Detail.requestFocus();
				send_btn.setClickable(true);
				return;
			}
			
			else{
				if(no_picture){
					if(log)Log.v(TAG,"no img");
					pre_kind = 2;
				}
				else{
					if(et_send_Detail.getText().toString().length() == 0){
						if(log)Log.v(TAG,"no msg");
						pre_kind = 1;
					}
				}
			}
			final int kind = pre_kind;
			{
    			
    			final int  recieve_num = Send_Id;
    			final String level = String.valueOf(MSG_LEVEL);
    			
//    			if(log)Log.v("ark_id","id:  "+length.toString());
    			globalID.PD(context, "连接", "正在努力发送…");
    			
    			Thread send_thread = new Thread(){
    				@SuppressWarnings("unused")
    				public void run(){
    					if(msg != null) msg = new Message();
    			        msg.what = 2;
    			try {
    				String send_id = new String(send_id1.getBytes("GB2312"),"8859_1");
//    				Socket socket=new Socket(serverAddr,SERVERPORT);
    				Socket socket = globalID.getSocket();
    				if(globalID.isConnect){
    		        	
    		        	int time_send_length = 6;
    		        	int senderId_length = 4;
    		        	int senderMobileLen_length = 1;
    		        	int senderMobile_length = 0;
    		        	int grade_length = 1;
    		        	int option_length = 2;
    		        	int data_type_length = 1;
    		        	int len_data_length = 4;
    		        	int data_length = 0;
    		        	
    		        	int Len_total = 0;
    		        	int Cmd_id = 0x00040004;
    		        	int Seq_id = 0;
    		        	
    		        	String time_send;
    					Calendar eDate = Calendar.getInstance();
    					time_send = new String(s.format(eDate.getTime()).getBytes("GB2312"),"8859_1");
    					if(log)Log.v(TAG, "time: " + time_send);
    					
    					byte[] time_send1 = new byte [time_send.length()];
    					for(int i = 0 ;i<time_send.length();i++){
    						time_send1[i] = (byte) time_send.charAt(i);
    					}
    					byte[] time_send2 = HexStrToBin(time_send1);
    					time_send = new String(time_send2);
//    					byte[] time_send2 = str2Bcd(time_send);
    					String senderMobile = new String(globalID.getID().getBytes("GB2312"),"8859_1");
    		        	senderMobile_length = senderMobile.length();
    					int grade = Integer.parseInt(level);
    					int cntRecver = 0;
    					int recverLen = 0;
    					int recver = 0;
    					int cntGrp = 0;
    					int lstGrp = 0;
    					char data_type = 3;
    					String data = "";
    					
    					/**************recver group***************/
    					short option = 0;
    					int cntRecver_length = 1;
    					int recverLen_length = 1;
    					int recver_length = 0;
    					int cntGrp_length = 1;
    					int lstGrp_length = 0;
    					/**************recver group***************/
    					
    					cntGrp_length = 0;
    					switch(recieve_num){
    					case 0:
    						option |= 16;
    					break;
    					case 1:
    						option |= 4;
    					break;
    					case 2:
    						option |= 8;
    					break;
    					case 3:
    					break;
    					}
    					
    		        	if(send_id.length() == 0){
    		        		cntRecver_length = 0;
    						recverLen_length = 0;
    						cntRecver = 0;
    						}
    		        	else{
    		        		option |= 2;
    		        		cntRecver_length = 1;
    						recverLen_length = 1;
    						cntRecver = 1;
    		        		}
    					recver_length = send_id.length();
    					recverLen = recver_length;

    					switch(kind){
    					case 0:{
    						final String detail = new String(et_send_Detail.getText().toString().getBytes("GB2312"),"8859_1");
    						data_type = 3;
    						
    						iv_send_HeadPhoto.setDrawingCacheEnabled(true);
    						Bitmap send_photo = iv_send_HeadPhoto.getDrawingCache();
    						Bitmap image = ((BitmapDrawable)iv_send_HeadPhoto.getDrawable()).getBitmap();
    						iv_send_HeadPhoto.setDrawingCacheEnabled(false);
//    						if(log)Log.v("send_photo", "photo  " + send_photo.toString());
//    						if(log)Log.v("image", "photo2  " + image.toString());
    						ByteArrayOutputStream change = new ByteArrayOutputStream();
    						
    						//compress
    						image = comp(image);
    						image.compress(Bitmap.CompressFormat.JPEG,90,change);
    						
    						byte[] send_p = change.toByteArray();
    						for(int i = send_p.length-15;i<send_p.length;i++){
    							if(log)Log.v(TAG, String.valueOf(i)+ " send_p: " + send_p[i]);
    						}
    						
    						int type_length = 1;
    						int Len_title_length = 1;
    						int Len_txt_length = 2;
    						int txt_length = detail.length();
    						int img_length = send_p.length;
    						
    						data_length = type_length + Len_title_length + Len_txt_length + txt_length + img_length;
    						short Len_txt = (short) detail.length();
    						
    						Len_total = StringUtil.HEAD_LENGTH
    								+ time_send_length + senderId_length
    								+ senderMobileLen_length + senderMobile_length
    								+ grade_length + option_length
    								+ cntRecver_length + recverLen_length
    								+ recver_length + cntGrp_length
    								+ lstGrp_length + data_type_length
    								+ len_data_length + data_length;
    						
//    						if(log)Log.v(TAG, "len_total: "+String.valueOf(Len_total));
    						
    						int length = 0;
    						byte[] send = new byte[Len_total];
    						length = FuntionUtil.send_head(length, send
    								, StringUtil.Len_total_length, Len_total
    								, StringUtil.Cmd_id_length, Cmd_id
    								, StringUtil.Seq_id_length, Seq_id);
    			        	
    						length = send_body_first(length, send, time_send_length, time_send2
    												, senderId_length, senderMobile_length, senderMobileLen_length
    												, senderMobile, grade, grade_length, option, option_length
    												, cntRecver, cntRecver_length, recverLen, recverLen_length
    												, recver_length, send_id, cntGrp_length, data_type, data_type_length
    												, len_data_length, data_length);
    						
    			        	send[length] = 2;
    			            length += type_length;
    			        	if(log)Log.v("length","15:"+String.valueOf(length));

    			        	send[length] = 0;
    			            length += Len_title_length;
    			        	if(log)Log.v("length","16:"+String.valueOf(length));
    			        	
    			        	send[length] = (byte) ((Len_txt >> 8) & 0xff);
    			            send[length + 1] = (byte) (Len_txt & 0xff);
    			            length += Len_txt_length;
    			        	if(log)Log.v("length","17:"+String.valueOf(length));
    			        	
    			        	for(int i = 0;i<Len_txt;i++){
    			            	send[length+i] = (byte) detail.charAt(i);
    			            }
    			            length += Len_txt;
    			        	if(log)Log.v("length","18:"+String.valueOf(length));
    			        	
    			        	for(int i = 0;i<img_length;i++){
    			            	send[length+i] = (byte) send_p[i];
    			            }
    			            length += img_length;
    			        	if(log)Log.v("length","19:"+String.valueOf(length));
    			        	
    			            socket.setSoTimeout(globalID.getTIMEOUT());
    			            
    			            OutputStream os = socket.getOutputStream();
    			            os.write(send);

    					}break;
    					case 1:{
    						data_type = 1;
    						
    						//from bitmap to byte[]
    						iv_send_HeadPhoto.setDrawingCacheEnabled(true);
    						Bitmap send_photo = iv_send_HeadPhoto.getDrawingCache();
    						Bitmap image = ((BitmapDrawable)iv_send_HeadPhoto.getDrawable()).getBitmap();
    						iv_send_HeadPhoto.setDrawingCacheEnabled(false);
    						if(log)Log.v("send_photo", "photo  " + send_photo.toString());
    						if(log)Log.v("image", "photo2  " + image.toString());
    						ByteArrayOutputStream change = new ByteArrayOutputStream();
    						
    						//compress
    						image = comp(image);
    						image.compress(Bitmap.CompressFormat.JPEG,90,change);
    						
    						byte[] send_p = change.toByteArray();
    						for(int i = send_p.length-15;i<send_p.length;i++){
    							if(log)Log.v(TAG, String.valueOf(i)+ " send_p: " + send_p[i]);
    						}
    						
    						int img_name_length = 1;
    						int img_length = send_p.length;
    						
    						data_length = img_name_length + img_length;
    						
    						Len_total = StringUtil.HEAD_LENGTH
    								+ time_send_length + senderId_length
    								+ senderMobileLen_length + senderMobile_length
    								+ grade_length + option_length
    								+ cntRecver_length + recverLen_length
    								+ recver_length + cntGrp_length
    								+ lstGrp_length + data_type_length
    								+ len_data_length + data_length;
    						
    						if(log)Log.v(TAG, "len_total: "+String.valueOf(Len_total));
    						
    						int length = 0;
    						byte[] send = new byte[Len_total];
    						length = FuntionUtil.send_head(length, send
    								, StringUtil.Len_total_length, Len_total
    								, StringUtil.Cmd_id_length, Cmd_id
    								, StringUtil.Seq_id_length, Seq_id);
    						
    						length = send_body_first(length, send, time_send_length, time_send2
    								, senderId_length, senderMobile_length, senderMobileLen_length
    								, senderMobile, grade, grade_length, option, option_length
    								, cntRecver, cntRecver_length, recverLen, recverLen_length
    								, recver_length, send_id, cntGrp_length, data_type, data_type_length
    								, len_data_length, data_length);
    						
    			        	send[length] = 0;
    			            length += img_name_length;
    			        	if(log)Log.v("length","16:"+String.valueOf(length));
    			        	
    			        	for(int i = 0;i<img_length;i++){
    			            	send[length+i] = (byte) send_p[i];
    			            }
    			            length += img_length;
    			        	if(log)Log.v("length","19:"+String.valueOf(length));
    			        	
    			            
    			            OutputStream os = socket.getOutputStream();
    			            os.write(send);
    			            
    					}break;
    					case 2:{
    						final String detail = new String(et_send_Detail.getText().toString().getBytes("GB2312"),"8859_1");
    						data_type = 0;
    						
    						int txt_length = detail.length();
    						
    						data_length = txt_length;
    						short Len_txt = (short) detail.length();
    						
    						Len_total = StringUtil.HEAD_LENGTH
    								+ time_send_length + senderId_length
    								+ senderMobileLen_length + senderMobile_length
    								+ grade_length + option_length
    								+ cntRecver_length + recverLen_length
    								+ recver_length + cntGrp_length
    								+ lstGrp_length + data_type_length
    								+ len_data_length + data_length;
    						
    						if(log)Log.v(TAG, "len_total: "+String.valueOf(Len_total));
    						
    						int length = 0;
    						byte[] send = new byte[Len_total];
    						length = FuntionUtil.send_head(length, send
    								, StringUtil.Len_total_length, Len_total
    								, StringUtil.Cmd_id_length, Cmd_id
    								, StringUtil.Seq_id_length, Seq_id);
    			        	
    						length = send_body_first(length, send, time_send_length, time_send2
    												, senderId_length, senderMobile_length, senderMobileLen_length
    												, senderMobile, grade, grade_length, option, option_length
    												, cntRecver, cntRecver_length, recverLen, recverLen_length
    												, recver_length, send_id, cntGrp_length, data_type, data_type_length
    												, len_data_length, data_length);
    						
    			        	for(int i = 0;i<Len_txt;i++){
    			            	send[length+i] = (byte) detail.charAt(i);
    			            }
    			            length += Len_txt;
    			        	if(log)Log.v("length","18:"+String.valueOf(length));

    			            OutputStream os = socket.getOutputStream();
    			            os.write(send);
    					}break;
    					}
    					for(long exitTime = 0;exitTime < globalID.getTIMEOUT();exitTime += globalID.getM_rate()){
    						try {
    							Thread.sleep(globalID.getM_rate());
    							if(log)Log.v(TAG, "sleep");
//    			        			wait(globalID.getM_rate());
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    							if(log)Log.v(TAG, "InterruptedException: "+e);
    						}
    				       	if(globalID.isSend_code()){
    							if(log)Log.v(TAG, "isSend_code");
//    				       		if(log)Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
    				       		break;
    				       	}	
//    			        	}
    					}
//    			        	
    					if(globalID.isSend_code()){
    			       		if(log)Log.v(TAG, "isSend_code = " + String.valueOf(globalID.isSend_code()));
    			       		if(globalID.isShake())vibrator.vibrate(1000);
    				         msg.what = kind;
    				         mhandler.sendMessage(msg);
    				         globalID.setSend_code(false);
    					 }
    					 else{
    				         msg.what = -1;
    				         mhandler.sendMessage(msg);
    					 }
    				}
    				else{
    					Log.e(TAG,"socket err");
    			         msg.what = -3;
    			         mhandler.sendMessage(msg);
    				}
    			} catch (UnknownHostException e) {
    				// TODO Auto-generated catch block
    				Log.e("UnknownHostException","err "+e);
            		if(msg != null) msg = new Message();
    				msg.what = -2;
    				mhandler.sendMessage(msg);
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				Log.e("IOException","err "+e);
            		if(msg != null) msg = new Message();
    				msg.what = -3;
    				mhandler.sendMessage(msg);
    				e.printStackTrace();
    			} catch (UnknownError e) {
    				// TODO Auto-generated catch block
    				Log.e("UnknownError","err "+e);
            		if(msg != null) msg = new Message();
    				msg.what = -3;
    				mhandler.sendMessage(msg);
    				e.printStackTrace();
    			}
    				}
    				};
    				send_thread.start();
            }
			send_btn.setClickable(true);
			}
//		}
	}
	
	@Override 
    public void onSaveInstanceState(Bundle savedInstanceState) {  
    	// Save away the original text, so we still have it if the activity   
    	// needs to be killed while paused.
    	super.onSaveInstanceState(savedInstanceState);
    	GlobalID globalID = ((GlobalID)getApplication());

    	savedInstanceState.putInt("Send_Id", Send_Id);
    	savedInstanceState.putInt("MSG_LEVEL", MSG_LEVEL);
    	globalID.create_notification("后台接收数据", "后台运行", "船客户端", false, false, false,SendActivity.class.getName());
	    if(globalID.toast != null)globalID.toast.cancel();
    	}
	
	@Override  
	public void onRestoreInstanceState(Bundle savedInstanceState) {  
	    super.onRestoreInstanceState(savedInstanceState);
//		GlobalID globalID = ((GlobalID)getApplication());
//	    String StrTest = savedInstanceState.getString("BandWArrays"); 
	    if(log)Log.e(TAG, "onRestoreInstanceState");
		GlobalID globalID = ((GlobalID)getApplication());
	    globalID.un_stop = false;
	} 
	
//	/*********************InnerScrollView**************************************/
//	public class InnerScrollView extends ScrollView { 
//		/** 
//		*/ 
//		public ScrollView parentScrollView; 
//		public InnerScrollView(Context context, AttributeSet attrs) { 
//		super(context, attrs); 
//		} 
//		private int lastScrollDelta = 0; 
//		public void resume() { 
//			overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(), 0, 0, true); 
//			lastScrollDelta = 0; 
//		} 
//		
//		int mTop = 10; 
//		/** 
//		* 将targetView滚到最顶端 
//		*/ 
//		
//		public void scrollTo(View targetView) {
//			int oldScrollY = getScrollY(); 
//			int top = targetView.getTop() - mTop; 
//			int delatY = top - oldScrollY; 
//			lastScrollDelta = delatY; 
//			overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0, true); 
//			}
//		
//		private int getScrollRange() { 
//			int scrollRange = 0;
//			if (getChildCount() > 0) { 
//				View child = getChildAt(0); 
//				scrollRange = Math.max(0, child.getHeight() - (getHeight())); 
//				} 
//			return scrollRange; 
//			} 
//		
//		int currentY; 
//		
//		@Override 
//		public boolean onInterceptTouchEvent(MotionEvent ev) { 
//			if (parentScrollView == null) { 
//				return super.onInterceptTouchEvent(ev); 
//				} 
//			else { 
//				if (ev.getAction() == MotionEvent.ACTION_DOWN) { 
//					// 将父scrollview的滚动事件拦截 
//					currentY = (int)ev.getY(); 
//					setParentScrollAble(false); 
//					return super.onInterceptTouchEvent(ev);
//					} 
//				else if (ev.getAction() == MotionEvent.ACTION_UP) { 
//					// 把滚动事件恢复给父Scrollview 
//					setParentScrollAble(true); 
//					} 
//				else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//					} 
//				} 
//			return super.onInterceptTouchEvent(ev); 
//		} 
//		
//		@Override 
//		public boolean onTouchEvent(MotionEvent ev) {
//			View child = getChildAt(0); 
//			if (parentScrollView != null) { 
//				if (ev.getAction() == MotionEvent.ACTION_MOVE) { 
//					int height = child.getMeasuredHeight(); 
//					height = height - getMeasuredHeight(); 
//					
//					// System.out.println("height=" + height); 
//					int scrollY = getScrollY(); 
//					
//					// System.out.println("scrollY" + scrollY); 
//					int y = (int)ev.getY(); 
//					
//					// 手指向下滑动 
//					if (currentY < y) { 
//						if (scrollY <= 0) { 
//							// 如果向下滑动到头，就把滚动交给父Scrollview 
//							setParentScrollAble(true); 
//							return false; 
//							} 
//						else { 
//							setParentScrollAble(false); 
//							} 
//						} 
//					else if (currentY > y) { 
//						if (scrollY >= height) { 
//							// 如果向上滑动到头，就把滚动交给父Scrollview 
//							setParentScrollAble(true); 
//							return false; 
//							} 
//						else { 
//							setParentScrollAble(false); 
//							} 
//						} 
//					currentY = y; 
//					} 
//				} 
//			return super.onTouchEvent(ev); 
//			} 
//		
//		/** 
//		* 是否把滚动事件交给父scrollview 
//		* 
//		* @param flag 
//		*/ 
//		private void setParentScrollAble(boolean flag) { 
//		parentScrollView.requestDisallowInterceptTouchEvent(!flag); 
//		} 
//	}
//	/*********************InnerScrollView**************************************/
}
